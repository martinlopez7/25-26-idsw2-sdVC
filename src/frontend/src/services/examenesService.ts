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
};