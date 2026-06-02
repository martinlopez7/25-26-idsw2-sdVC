import { api } from './api';

export interface RespuestaDTO {
  id: number;
  opcion: string;
  esCorrecta: boolean;
}

export interface RespuestaCreateDTO {
  preguntaId: number;
  opcion: string;
  esCorrecta: boolean;
}

export interface RespuestaUpdateDTO {
  opcion: string;
  esCorrecta: boolean;
}

export const respuestasService = {
  getRespuestasPorPregunta: async (preguntaId: number, filtro?: string): Promise<RespuestaDTO[]> => {
    const url = filtro
      ? `/respuestas/pregunta/${preguntaId}?filtro=${encodeURIComponent(filtro)}`
      : `/respuestas/pregunta/${preguntaId}`;
    const response = await api.get<RespuestaDTO[]>(url);
    return response.data;
  },

  crearRespuesta: async (dto: RespuestaCreateDTO): Promise<RespuestaDTO> => {
    const response = await api.post<RespuestaDTO>('/respuestas', dto);
    return response.data;
  },

  getRespuestaPorId: async (id: number): Promise<RespuestaDTO> => {
    const response = await api.get<RespuestaDTO>(`/respuestas/${id}`);
    return response.data;
  },

  actualizarRespuesta: async (id: number, dto: RespuestaUpdateDTO): Promise<RespuestaDTO> => {
    const response = await api.put<RespuestaDTO>(`/respuestas/${id}`, dto);
    return response.data;
  },

  eliminarRespuesta: async (id: number): Promise<void> => {
    await api.delete(`/respuestas/${id}`);
  },
};
