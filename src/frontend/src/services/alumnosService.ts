import { api } from './api';

export interface AlumnoDTO {
  id: number;
  nombre: string;
  apellidos: string;
  dni: string;
}

export const alumnosService = {
  getAlumnos: async (filtro?: string): Promise<AlumnoDTO[]> => {
    const url = filtro ? `/alumnos/mios?filtro=${encodeURIComponent(filtro)}` : '/alumnos/mios';
    const response = await api.get<AlumnoDTO[]>(url);
    return response.data;
  },
};