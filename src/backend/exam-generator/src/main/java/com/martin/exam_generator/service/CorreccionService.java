package com.martin.exam_generator.service;

import com.martin.exam_generator.dto.ResultadoCorreccionDTO;
import org.apache.pdfbox.Loader;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class CorreccionService {

    private final Random random = new Random();

    public List<ResultadoCorreccionDTO> procesarPdf(MultipartFile archivoPdf) {
        List<ResultadoCorreccionDTO> resultados = new ArrayList<>();

        int numPaginas = contarPaginas(archivoPdf);

        for (int i = 1; i <= numPaginas; i++) {
            int nota = generarNotaAleatoria();
            resultados.add(new ResultadoCorreccionDTO(i, nota));
        }

        return resultados;
    }

    private int contarPaginas(MultipartFile archivoPdf) {
        try (InputStream inputStream = archivoPdf.getInputStream();
             PDDocument document = Loader.loadPDF(inputStream.readAllBytes())) {
            return document.getNumberOfPages();
        } catch (IOException e) {
            throw new IllegalArgumentException("No se pudo leer el PDF: " + e.getMessage());
        }
    }

    private int generarNotaAleatoria() {
        return random.nextInt(10) + 1;
    }
}