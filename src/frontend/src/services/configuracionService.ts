import { api } from './api';

export interface ConfiguracionExportDTO {
  version: string;
  fechaExportacion: string;
  docenteOrigen: {
    id: number;
    nombre: string;
    apellidos: string;
  };
  elementos: {
    grados: Array<{
      id: number;
      titulo: string;
      codigo: string;
    }>;
    asignaturas: Array<{
      id: number;
      titulo: string;
      codigo: string;
      cursoAcademico: string;
      gradoIds: number[];
    }>;
    alumnos: Array<{
      id: number;
      nombre: string;
      apellidos: string;
      dni: string;
      gradoId: number | null;
      asignaturaIds: number[];
    }>;
    preguntas: Array<{
      id: number;
      enunciado: string;
      tema: string;
      dificultad: string;
      asignaturaId: number;
      respuestas: Array<{
        opcion: string;
        esCorrecta: boolean;
      }>;
    }>;
  };
}

export const configuracionService = {
  exportarConfiguracionGlobal: async (): Promise<ConfiguracionExportDTO> => {
    const response = await api.get<ConfiguracionExportDTO>('/configuracion/exportar');
    return response.data;
  },

  descargarConfiguracion: async (): Promise<void> => {
    const data = await configuracionService.exportarConfiguracionGlobal();
    const json = JSON.stringify(data, null, 2);
    const blob = new Blob([json], { type: 'application/json' });
    const url = URL.createObjectURL(blob);
    const link = document.createElement('a');
    link.href = url;
    const fecha = new Date().toISOString().split('T')[0];
    link.download = `configuracion_${fecha}.json`;
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
    URL.revokeObjectURL(url);
  },

  importarConfiguracionGlobal: async (archivo: File): Promise<void> => {
    const formData = new FormData();
    formData.append('archivo', archivo);
    await api.post('/configuracion/importar', formData, {
      headers: {
        'Content-Type': 'multipart/form-data',
      },
    });
  },
};