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

  getAlumnoById: async (id: number): Promise<AlumnoDTO> => {
    const response = await api.get<AlumnoDTO>(`/alumnos/${id}`);
    return response.data;
  },

  crearAlumno: async (alumno: Omit<AlumnoDTO, 'id'>): Promise<AlumnoDTO> => {
    const response = await api.post<AlumnoDTO>('/alumnos', alumno);
    return response.data;
  },

  actualizarAlumno: async (id: number, alumno: Omit<AlumnoDTO, 'id'>): Promise<AlumnoDTO> => {
    const response = await api.put<AlumnoDTO>(`/alumnos/${id}`, alumno);
    return response.data;
  },

  eliminarAlumno: async (id: number): Promise<void> => {
    await api.delete(`/alumnos/${id}`);
  },
};