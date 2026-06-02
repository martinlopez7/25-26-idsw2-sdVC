import { api } from './api';

export interface RespuestaDTO {
  id: number;
  opcion: string;
  esCorrecta: boolean;
}

export interface PreguntaDTO {
  id: number;
  enunciado: string;
  tema: string;
  dificultad: string;
  habilitada: boolean;
  asignaturaId: number;
  respuestas: RespuestaDTO[];
}

export interface PreguntaCreateDTO {
  asignaturaId: number;
  enunciado: string;
  tema: string;
  dificultad: 'FACIL' | 'MEDIO' | 'DIFICIL';
}

export const preguntasService = {
  crearPregunta: async (pregunta: PreguntaCreateDTO): Promise<PreguntaDTO> => {
    const response = await api.post<PreguntaDTO>('/preguntas', pregunta);
    return response.data;
  },

  getPreguntas: async (filtro?: string): Promise<PreguntaDTO[]> => {
    const url = filtro ? `/preguntas/mias?filtro=${encodeURIComponent(filtro)}` : '/preguntas/mias';
    const response = await api.get<PreguntaDTO[]>(url);
    return response.data;
  },

  getPreguntasPorAsignatura: async (asignaturaId: number, filtro?: string): Promise<PreguntaDTO[]> => {
    const url = filtro ? `/preguntas/asignatura/${asignaturaId}?filtro=${encodeURIComponent(filtro)}` : `/preguntas/asignatura/${asignaturaId}`;
    const response = await api.get<PreguntaDTO[]>(url);
    return response.data;
  },

  getPreguntaById: async (id: number): Promise<PreguntaDTO> => {
    const response = await api.get<PreguntaDTO>(`/preguntas/${id}`);
    return response.data;
  },
};
