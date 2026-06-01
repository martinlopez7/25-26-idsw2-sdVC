import { api } from './api';

export interface AsignaturaDTO {
  id: number;
  titulo: string;
  codigo: string;
  cursoAcademico: string;
}

export interface AsignaturaCreateDTO {
  titulo: string;
  codigo: string;
  cursoAcademico: string;
}

export const asignaturasService = {
  getAsignaturas: async (filtro?: string): Promise<AsignaturaDTO[]> => {
    const url = filtro ? `/asignaturas/mias?filtro=${encodeURIComponent(filtro)}` : '/asignaturas/mias';
    const response = await api.get<AsignaturaDTO[]>(url);
    return response.data;
  },

  getAsignaturaById: async (id: number): Promise<AsignaturaDTO> => {
    const response = await api.get<AsignaturaDTO>(`/asignaturas/${id}`);
    return response.data;
  },

  crearAsignatura: async (asignatura: AsignaturaCreateDTO): Promise<AsignaturaDTO> => {
    const response = await api.post<AsignaturaDTO>('/asignaturas', asignatura);
    return response.data;
  },

  actualizarAsignatura: async (id: number, asignatura: AsignaturaCreateDTO): Promise<AsignaturaDTO> => {
    const response = await api.put<AsignaturaDTO>(`/asignaturas/${id}`, asignatura);
    return response.data;
  },
};