import { createContext, useContext, useState, ReactNode } from 'react';

interface AuthUser {
  username: string;
  tipoActor: 'DOCENTE' | 'ADMINISTRADOR_INSTITUCIONAL';
}

interface AuthContextType {
  user: AuthUser | null;
  token: string | null;
  login: (token: string, tipoActor: string) => void;
  logout: () => void;
  isAuthenticated: boolean;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export function AuthProvider({ children }: { children: ReactNode }) {
  const [user, setUser] = useState<AuthUser | null>(() => {
    const stored = localStorage.getItem('auth');
    if (stored) {
      const { token, tipoActor } = JSON.parse(stored);
      return { username: '', tipoActor: tipoActor as AuthUser['tipoActor'] };
    }
    return null;
  });

  const [token, setToken] = useState<string | null>(() => {
    const stored = localStorage.getItem('auth');
    return stored ? JSON.parse(stored).token : null;
  });

  const login = (newToken: string, tipoActor: string) => {
    localStorage.setItem('auth', JSON.stringify({ token: newToken, tipoActor }));
    setToken(newToken);
    setUser({ username: '', tipoActor: tipoActor as AuthUser['tipoActor'] });
  };

  const logout = () => {
    localStorage.removeItem('auth');
    setToken(null);
    setUser(null);
  };

  return (
    <AuthContext.Provider value={{ user, token, login, logout, isAuthenticated: !!token }}>
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth() {
  const context = useContext(AuthContext);
  if (context === undefined) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
}

export type { AuthUser };
