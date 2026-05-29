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

export const docentesService = {
  getDocentes: async (filtro?: string): Promise<UsuarioDTO[]> => {
    const url = filtro ? `/docentes?filtro=${encodeURIComponent(filtro)}` : '/docentes';
    const response = await api.get<UsuarioDTO[]>(url);
    return response.data;
  },
  crearDocente: async (docente: UsuarioCreateDTO): Promise<UsuarioDTO> => {
    const response = await api.post<UsuarioDTO>('/docentes', docente);
    return response.data;
  },
};