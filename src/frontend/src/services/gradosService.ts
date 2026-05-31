import { api } from './api';

export interface AlumnoDTO {
  id: number;
  nombre: string;
  apellidos: string;
  dni: string;
  gradoId: number | null;
}

export interface GradoDTO {
  id: number;
  titulo: string;
  codigo: string;
  alumnos: AlumnoDTO[];
}

export interface GradoUpdateDTO {
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

  crearGrado: async (grado: Omit<GradoDTO, 'id' | 'alumnos'>): Promise<GradoDTO> => {
    const response = await api.post<GradoDTO>('/grados', grado);
    return response.data;
  },

  actualizarGrado: async (id: number, grado: GradoUpdateDTO): Promise<GradoDTO> => {
    const response = await api.put<GradoDTO>(`/grados/${id}`, grado);
    return response.data;
  },

  getAlumnosSinGrado: async (): Promise<AlumnoDTO[]> => {
    const response = await api.get<AlumnoDTO[]>('/grados/sin-grado');
    return response.data;
  },

  anadirAlumnoAGrado: async (gradoId: number, alumnoId: number): Promise<AlumnoDTO> => {
    const response = await api.put<AlumnoDTO>(`/grados/${gradoId}/alumnos/${alumnoId}`, {});
    return response.data;
  },

  quitarAlumnoDeGrado: async (gradoId: number, alumnoId: number): Promise<void> => {
    await api.delete(`/grados/${gradoId}/alumnos/${alumnoId}`);
  },
};