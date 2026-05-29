import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { AuthProvider, useAuth } from './context/AuthContext';
import 'bootstrap/dist/css/bootstrap.min.css';
import LoginPage from './components/auth/LoginPage';
import LogoutButton from './components/auth/LogoutButton';
import AdminMenu from './components/menu/AdminMenu';
import DocenteMenu from './components/menu/DocenteMenu';
import DocentesListComponent from './components/docentes/DocentesListComponent';

function ProtectedRoute({ children }: { children: React.ReactNode }) {
  const { isAuthenticated } = useAuth();
  return isAuthenticated ? <>{children}</> : <Navigate to="/login" />;
}

function AdminRoute({ children }: { children: React.ReactNode }) {
  const { isAuthenticated, user } = useAuth();
  if (!isAuthenticated) return <Navigate to="/login" />;
  if (user?.tipoActor !== 'ADMINISTRADOR_INSTITUCIONAL') return <Navigate to="/menu-docente" />;
  return <>{children}</>;
}

function AppRoutes() {
  const { isAuthenticated, user } = useAuth();

  return (
    <Routes>
      <Route path="/login" element={isAuthenticated ? <Navigate to={user?.tipoActor === 'ADMINISTRADOR_INSTITUCIONAL' ? "/menu-admin" : "/menu-docente"} /> : <LoginPage />} />
      <Route path="/menu-admin" element={<ProtectedRoute><AdminMenu /></ProtectedRoute>} />
      <Route path="/menu-docente" element={<ProtectedRoute><DocenteMenu /></ProtectedRoute>} />
      <Route path="/docentes" element={<AdminRoute><DocentesListComponent /></AdminRoute>} />
      <Route path="*" element={<Navigate to={isAuthenticated ? (user?.tipoActor === 'ADMINISTRADOR_INSTITUCIONAL' ? "/menu-admin" : "/menu-docente") : "/login"} />} />
    </Routes>
  );
}

function App() {
  return (
    <BrowserRouter>
      <AuthProvider>
        <AppRoutes />
      </AuthProvider>
    </BrowserRouter>
  );
}

export default App;
