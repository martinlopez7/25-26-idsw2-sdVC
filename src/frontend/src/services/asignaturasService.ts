import { api } from './api';

export interface GradoDTO {
  id: number;
  titulo: string;
  codigo: string;
}

export interface AlumnoDTO {
  id: number;
  nombre: string;
  apellidos: string;
  dni: string;
}

export interface AsignaturaDTO {
  id: number;
  titulo: string;
  codigo: string;
  cursoAcademico: string;
  grados: GradoDTO[];
  alumnos: AlumnoDTO[];
}

export interface AsignaturaCreateDTO {
  titulo: string;
  codigo: string;
  cursoAcademico: string;
}

export interface AsignaturaUpdateDTO {
  titulo: string;
  codigo: string;
  cursoAcademico: string;
  gradoIds: number[];
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

  actualizarAsignatura: async (id: number, asignatura: AsignaturaUpdateDTO): Promise<AsignaturaDTO> => {
    const response = await api.put<AsignaturaDTO>(`/asignaturas/${id}`, asignatura);
    return response.data;
  },

  getAlumnosDisponibles: async (asignaturaId: number): Promise<AlumnoDTO[]> => {
    const response = await api.get<AlumnoDTO[]>(`/asignaturas/${asignaturaId}/alumnos-disponibles`);
    return response.data;
  },

  matricularAlumno: async (asignaturaId: number, alumnoId: number): Promise<void> => {
    await api.post(`/asignaturas/${asignaturaId}/alumnos/${alumnoId}`);
  },

  desmatricularAlumno: async (asignaturaId: number, alumnoId: number): Promise<void> => {
    await api.delete(`/asignaturas/${asignaturaId}/alumnos/${alumnoId}`);
  },

  eliminarAsignatura: async (id: number): Promise<void> => {
    await api.delete(`/asignaturas/${id}`);
  },
};