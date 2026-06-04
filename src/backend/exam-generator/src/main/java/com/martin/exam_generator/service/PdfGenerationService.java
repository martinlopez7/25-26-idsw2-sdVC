package com.martin.exam_generator.service;

import com.martin.exam_generator.dto.PlantillaExamen;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PdfGenerationService {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    public byte[] generarPdfExamen(PlantillaExamen plantilla, String nombreAlumno,
                                    String apellidosAlumno, String claveCorreccion) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Document document = new Document(PageSize.A4);

        try {
            PdfWriter.getInstance(document, outputStream);
            document.open();

            addHeader(document, plantilla, nombreAlumno, apellidosAlumno);
            addExamInfo(document, plantilla, claveCorreccion);
            addQuestions(document, plantilla);

            document.close();
        } catch (DocumentException e) {
            throw new RuntimeException("Error al generar el PDF del examen", e);
        }

        return outputStream.toByteArray();
    }

    public byte[] generarZipExamenes(List<PlantillaExamen> plantillas,
                                      Map<Long, String> alumnoInfoMap) {
        ByteArrayOutputStream zipOutputStream = new ByteArrayOutputStream();
        java.util.zip.ZipOutputStream zipOut = new java.util.zip.ZipOutputStream(zipOutputStream);

        try {
            for (PlantillaExamen plantilla : plantillas) {
                if (plantilla.getAsignada() != null && plantilla.getAsignada()) {
                    String nombreAlumno = "";
                    String apellidosAlumno = "";

                    if (plantilla.getAlumnoId() != null && alumnoInfoMap.containsKey(plantilla.getAlumnoId())) {
                        String fullName = alumnoInfoMap.get(plantilla.getAlumnoId());
                        String[] parts = fullName.split(" ", 2);
                        nombreAlumno = parts[0];
                        apellidosAlumno = parts.length > 1 ? parts[1] : "";
                    }

                    byte[] pdfBytes = generarPdfExamen(plantilla, nombreAlumno, apellidosAlumno,
                            plantilla.getClaveCorreccion());

                    String fileName = String.format("Examen_%s_%s_%s.pdf",
                            sanitizeFileName(plantilla.getTituloAsignatura()),
                            sanitizeFileName(apellidosAlumno + "_" + nombreAlumno),
                            plantilla.getId().substring(0, 8));

                    zipOut.putNextEntry(new java.util.zip.ZipEntry(fileName));
                    zipOut.write(pdfBytes);
                    zipOut.closeEntry();
                }
            }
            zipOut.finish();
        } catch (Exception e) {
            throw new RuntimeException("Error al generar el ZIP de exámenes", e);
        }

        return zipOutputStream.toByteArray();
    }

    private void addHeader(Document document, PlantillaExamen plantilla, String nombreAlumno,
                           String apellidosAlumno) throws DocumentException {
        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
        Font subtitleFont = FontFactory.getFont(FontFactory.HELVETICA, 12);

        Paragraph title = new Paragraph(plantilla.getTituloAsignatura(), titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);

        document.add(new Paragraph(" "));

        Paragraph evalParagraph = new Paragraph("Tipo: " + formatEvaluacion(plantilla.getEvaluacion().name()), subtitleFont);
        evalParagraph.setAlignment(Element.ALIGN_CENTER);
        document.add(evalParagraph);

        document.add(new Paragraph(" "));

        PdfPTable headerTable = new PdfPTable(2);
        headerTable.setWidthPercentage(100);
        headerTable.setWidths(new float[]{1, 1});

        PdfPCell alumnoCell = new PdfPCell(new Phrase("Alumno: " + nombreAlumno + " " + apellidosAlumno));
        alumnoCell.setBorder(Rectangle.NO_BORDER);
        headerTable.addCell(alumnoCell);

        PdfPCell fechaCell = new PdfPCell(new Phrase("Fecha: " + LocalDateTime.now().format(DATE_FORMATTER)));
        fechaCell.setBorder(Rectangle.NO_BORDER);
        fechaCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        headerTable.addCell(fechaCell);

        document.add(headerTable);

        document.add(new Paragraph(" "));
        document.add(new Paragraph("_".repeat(70)));
        document.add(new Paragraph(" "));
    }

    private void addExamInfo(Document document, PlantillaExamen plantilla, String claveCorreccion)
            throws DocumentException {
        Font infoFont = FontFactory.getFont(FontFactory.HELVETICA, 10);
        Font claveFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10);

        PdfPTable infoTable = new PdfPTable(2);
        infoTable.setWidthPercentage(100);
        infoTable.setWidths(new float[]{3, 1});

        PdfPCell preguntasCell = new PdfPCell(new Phrase("Número de preguntas: " +
                (plantilla.getPreguntas() != null ? plantilla.getPreguntas().size() : 0), infoFont));
        preguntasCell.setBorder(Rectangle.NO_BORDER);
        infoTable.addCell(preguntasCell);

        PdfPCell claveCell = new PdfPCell(new Phrase("Clave: " + claveCorreccion, claveFont));
        claveCell.setBorder(Rectangle.NO_BORDER);
        claveCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        infoTable.addCell(claveCell);

        document.add(infoTable);
        document.add(new Paragraph(" "));
    }

    private void addQuestions(Document document, PlantillaExamen plantilla) throws DocumentException {
        Font questionFont = FontFactory.getFont(FontFactory.HELVETICA, 11);
        Font answerFont = FontFactory.getFont(FontFactory.HELVETICA, 10);

        List<PlantillaExamen.PreguntaSeleccionada> preguntas = plantilla.getPreguntas();
        if (preguntas == null || preguntas.isEmpty()) {
            document.add(new Paragraph("No hay preguntas en este examen.", questionFont));
            return;
        }

        int numeroPregunta = 1;
        for (PlantillaExamen.PreguntaSeleccionada pregunta : preguntas) {
            Paragraph preguntaParagraph = new Paragraph();
            preguntaParagraph.add(new Chunk(numeroPregunta + ". ", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11)));
            preguntaParagraph.add(new Chunk(pregunta.getEnunciado(), questionFont));
            preguntaParagraph.setSpacingAfter(5);
            document.add(preguntaParagraph);

            if (pregunta.getTema() != null) {
                Font temaFont = FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 9);
                document.add(new Paragraph("Tema: " + pregunta.getTema(), temaFont));
            }

            document.add(new Paragraph(" "));

            if (pregunta.getRespuestas() != null && !pregunta.getRespuestas().isEmpty()) {
                PdfPTable answersTable = new PdfPTable(1);
                answersTable.setWidthPercentage(80);
                answersTable.setHorizontalAlignment(Element.ALIGN_LEFT);

                for (PlantillaExamen.RespuestaSeleccionada respuesta : pregunta.getRespuestas()) {
                    PdfPCell cell = new PdfPCell();
                    cell.setBorder(Rectangle.BOX);
                    cell.setPadding(5);

                    String opcionLabel = getOpcionLabel(respuesta.getPosicionAleatoria() != null ?
                            respuesta.getPosicionAleatoria() : respuesta.getPosicionOriginal());
                    cell.addElement(new Phrase(opcionLabel + ". " + respuesta.getOpcion(), answerFont));
                    answersTable.addCell(cell);
                }

                document.add(answersTable);
            }

            document.add(new Paragraph(" "));
            numeroPregunta++;
        }
    }

    private String getOpcionLabel(int posicion) {
        return String.valueOf((char) ('A' + posicion));
    }

    private String formatEvaluacion(String evaluacion) {
        if (evaluacion == null) return "";
        return evaluacion.replace("_", " ");
    }

    private String sanitizeFileName(String fileName) {
        if (fileName == null) return "unknown";
        return fileName.replaceAll("[^a-zA-Z0-9_\\-]", "_").substring(0, Math.min(fileName.length(), 30));
    }
}