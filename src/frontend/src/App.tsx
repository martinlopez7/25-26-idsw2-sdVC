import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { AuthProvider, useAuth } from './context/AuthContext';
import 'bootstrap/dist/css/bootstrap.min.css';
import LoginPage from './components/auth/LoginPage';
import LogoutButton from './components/auth/LogoutButton';
import AdminMenu from './components/menu/AdminMenu';
import DocenteMenu from './components/menu/DocenteMenu';
import DocentesListComponent from './components/docentes/DocentesListComponent';
import UsuarioFormComponent from './components/docentes/UsuarioFormComponent';
import EliminarDocenteComponent from './components/docentes/EliminarDocenteComponent';
import AlumnosListComponent from './components/alumnos/AlumnosListComponent';
import AlumnoFormComponent from './components/alumnos/AlumnoFormComponent';
import EliminarAlumnoComponent from './components/alumnos/EliminarAlumnoComponent';
import GradosListComponent from './components/grados/GradosListComponent';
import GradoFormComponent from './components/grados/GradoFormComponent';
import EditarGradoComponent from './components/grados/EditarGradoComponent';
import EliminarGradoComponent from './components/grados/EliminarGradoComponent';
import AsignaturasListComponent from './components/asignaturas/AsignaturasListComponent';

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
      <Route path="/docentes/crear" element={<AdminRoute><UsuarioFormComponent /></AdminRoute>} />
      <Route path="/docentes/editar/:id" element={<AdminRoute><UsuarioFormComponent /></AdminRoute>} />
      <Route path="/docentes/eliminar/:id" element={<AdminRoute><EliminarDocenteComponent /></AdminRoute>} />
      <Route path="/alumnos" element={<ProtectedRoute><AlumnosListComponent /></ProtectedRoute>} />
      <Route path="/alumnos/crear" element={<ProtectedRoute><AlumnoFormComponent /></ProtectedRoute>} />
      <Route path="/alumnos/editar/:id" element={<ProtectedRoute><AlumnoFormComponent isEditing /></ProtectedRoute>} />
      <Route path="/alumnos/eliminar/:id" element={<ProtectedRoute><EliminarAlumnoComponent /></ProtectedRoute>} />
      <Route path="/grados" element={<ProtectedRoute><GradosListComponent /></ProtectedRoute>} />
      <Route path="/grados/crear" element={<ProtectedRoute><GradoFormComponent /></ProtectedRoute>} />
      <Route path="/grados/editar/:id" element={<ProtectedRoute><EditarGradoComponent /></ProtectedRoute>} />
      <Route path="/grados/eliminar/:id" element={<ProtectedRoute><EliminarGradoComponent /></ProtectedRoute>} />
      <Route path="/asignaturas" element={<ProtectedRoute><AsignaturasListComponent /></ProtectedRoute>} />
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
