import axios from 'axios';

const API_URL = 'http://localhost:8080/api/auth';

export interface LoginRequest {
  username: string;
  password: string;
}

export interface JwtResponse {
  token: string;
  type: string;
  tipoActor: string;
}

export const authService = {
  login: async (credentials: LoginRequest): Promise<JwtResponse> => {
    const response = await axios.post<JwtResponse>(`${API_URL}/login`, credentials);
    return response.data;
  },

  logout: async (token: string): Promise<void> => {
    await axios.post(`${API_URL}/logout`, {}, {
      headers: { Authorization: `Bearer ${token}` }
    });
  }
};
