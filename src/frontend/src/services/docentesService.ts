import { api } from './api';

export interface UsuarioDTO {
  id: number;
  username: string;
  tipoActor: string;
}

export const docentesService = {
  getDocentes: async (filtro?: string): Promise<UsuarioDTO[]> => {
    const url = filtro ? `/docentes?filtro=${encodeURIComponent(filtro)}` : '/docentes';
    const response = await api.get<UsuarioDTO[]>(url);
    return response.data;
  },
};