import { api } from './api';

export interface GradoDTO {
  id: number;
  titulo: string;
  codigo: string;
}

export const gradosService = {
  getGrados: async (filtro?: string): Promise<GradoDTO[]> => {
    const url = filtro ? `/grados/mios?filtro=${encodeURIComponent(filtro)}` : '/grados/mios';
    const response = await api.get<GradoDTO[]>(url);
    return response.data;
  },

  getGradoById: async (id: number): Promise<GradoDTO> => {
    const response = await api.get<GradoDTO>(`/grados/${id}`);
    return response.data;
  },

  crearGrado: async (grado: Omit<GradoDTO, 'id'>): Promise<GradoDTO> => {
    const response = await api.post<GradoDTO>('/grados', grado);
    return response.data;
  },

  actualizarGrado: async (id: number, grado: Omit<GradoDTO, 'id'>): Promise<GradoDTO> => {
    const response = await api.put<GradoDTO>(`/grados/${id}`, grado);
    return response.data;
  },
};