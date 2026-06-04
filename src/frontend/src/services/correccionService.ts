import axios from 'axios';
import { ResultadoCorreccion } from '../types';

const API_URL = process.env.REACT_APP_API_URL || 'http://localhost:8080/api';

export const correccionService = {
    corregirExamenes: async (archivoPdf: File): Promise<ResultadoCorreccion[]> => {
        const formData = new FormData();
        formData.append('archivo', archivoPdf);

        const authData = localStorage.getItem('auth');
        if (!authData) {
            throw new Error('No hay sesión activa');
        }
        const { token } = JSON.parse(authData);

        const response = await axios.post<ResultadoCorreccion[] | { error: string }>(
            `${API_URL}/examenes/corregir`,
            formData,
            {
                headers: {
                    'Authorization': `Bearer ${token}`,
                    'Content-Type': 'multipart/form-data',
                },
            }
        );

        if ('error' in response.data) {
            throw new Error(response.data.error);
        }

        return response.data as ResultadoCorreccion[];
    },
};