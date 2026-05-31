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
};