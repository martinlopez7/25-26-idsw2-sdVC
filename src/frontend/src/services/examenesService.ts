import { api } from './api';

export interface ConfigGradoDTO {
  numExamenes: number;
  numTiposExamen: number;
  proporcionDificultad: {
    facil: number;
    medio: number;
    dificil: number;
  };
}

export interface GenerarExamenesRequest {
  asignaturaId: number;
  evaluacion: string;
  temas: string[];
  numPreguntas: number;
  configPorGrado: Record<number, ConfigGradoDTO>;
}

export interface GenerarExamenesResponse {
  plantillaExamenIds: string[];
  totalExamenesGenerados: number;
}

export interface AsignaturaConGradosDTO {
  id: number;
  titulo: string;
  codigo: string;
  cursoAcademico: string;
  grados: GradoConAlumnosDTO[];
  alumnos: AlumnoDTO[];
}

export interface GradoConAlumnosDTO {
  id: number;
  titulo: string;
  codigo: string;
  numAlumnos: number;
}

export interface AlumnoDTO {
  id: number;
  nombre: string;
  apellidos: string;
  dni: string;
  gradoId?: number;
  gradoNombre?: string;
}

export interface PlantillaExamenDTO {
  id: string;
  asignaturaId: number;
  tituloAsignatura: string;
  evaluacion: string;
  numPreguntas: number;
  alumnoId: number | null;
  claveCorreccion: string | null;
  asignada: boolean;
  gradoId: number;
}

export interface AsignarExamenRequest {
  plantillaId: string;
  alumnoId: number;
}

export interface AsignacionDTO {
  plantillaId: string;
  alumnoId: number;
}

export interface AlumnoPorGradoDTO {
  gradoId: number;
  gradoNombre: string;
  alumnos: AlumnoDTO[];
}

export const examenesService = {
  generarExamenes: async (request: GenerarExamenesRequest): Promise<GenerarExamenesResponse> => {
    const response = await api.post<GenerarExamenesResponse>('/examenes/generar', request);
    return response.data;
  },

  cancelarGeneracion: async (): Promise<void> => {
    await api.delete('/examenes/generar/cancelar');
  },

  getAsignaturaConGrados: async (asignaturaId: number): Promise<AsignaturaConGradosDTO> => {
    const response = await api.get<AsignaturaConGradosDTO>(`/asignaturas/${asignaturaId}/completa`);
    return response.data;
  },

  obtenerPlantillas: async (): Promise<PlantillaExamenDTO[]> => {
    const response = await api.get<PlantillaExamenDTO[]>('/examenes/plantillas');
    return response.data;
  },

  obtenerAlumnosPorGrado: async (asignaturaId: number): Promise<AlumnoPorGradoDTO[]> => {
    const response = await api.get<AlumnoPorGradoDTO[]>(`/examenes/alumnos-por-grado?asignaturaId=${asignaturaId}`);
    return response.data;
  },

  asignarExamen: async (request: AsignarExamenRequest): Promise<void> => {
    await api.post('/examenes/asignar', request);
  },

  asignarTodos: async (asignaciones: AsignacionDTO[]): Promise<{ totalAsignadas: number }> => {
    const response = await api.post('/examenes/asignar-todos', { asignaciones });
    return response.data;
  },

  descargarPdfLote: async (): Promise<Blob> => {
    const response = await api.get('/examenes/asignados/pdf-lote', {
      responseType: 'blob',
    });
    return response.data;
  },

  cancelarAsignacion: async (): Promise<void> => {
    await api.delete('/examenes/plantillas');
  },
};