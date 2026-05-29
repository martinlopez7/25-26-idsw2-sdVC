import { api } from './api';

export interface UsuarioDTO {
  id: number;
  username: string;
  tipoActor: string;
  nombre?: string;
  apellidos?: string;
  dni?: string;
  email?: string;
}

export interface UsuarioCreateDTO {
  nombre: string;
  apellidos: string;
  dni: string;
  username: string;
  email: string;
  password: string;
}

export interface UsuarioUpdateDTO {
  nombre: string;
  apellidos: string;
  dni: string;
  username: string;
  email: string;
  password?: string;
}

export const docentesService = {
  getDocentes: async (filtro?: string): Promise<UsuarioDTO[]> => {
    const url = filtro ? `/docentes?criterio=${encodeURIComponent(filtro)}` : '/docentes';
    const response = await api.get<UsuarioDTO[]>(url);
    return response.data;
  },
  crearDocente: async (docente: UsuarioCreateDTO): Promise<UsuarioDTO> => {
    const response = await api.post<UsuarioDTO>('/docentes', docente);
    return response.data;
  },
  obtenerDocentePorId: async (id: number): Promise<UsuarioDTO> => {
    const response = await api.get<UsuarioDTO>(`/docentes/${id}`);
    return response.data;
  },
  actualizarDocente: async (id: number, docente: UsuarioUpdateDTO): Promise<UsuarioDTO> => {
    const response = await api.put<UsuarioDTO>(`/docentes/${id}`, docente);
    return response.data;
  },
};
