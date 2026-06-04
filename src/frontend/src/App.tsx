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
import AsignaturaFormComponent from './components/asignaturas/AsignaturaFormComponent';
import EliminarAsignaturaComponent from './components/asignaturas/EliminarAsignaturaComponent';
import PreguntasListComponent from './components/preguntas/PreguntasListComponent';
import PreguntaFormComponent from './components/preguntas/PreguntaFormComponent';
import EliminarPreguntaComponent from './components/preguntas/EliminarPreguntaComponent';
import RespuestasListComponent from './components/respuestas/RespuestasListComponent';
import RespuestaFormComponent from './components/respuestas/RespuestaFormComponent';
import EliminarRespuestaComponent from './components/respuestas/EliminarRespuestaComponent';
import ExportarConfiguracionComponent from './components/configuracion/ExportarConfiguracionComponent';
import ImportarConfiguracionComponent from './components/configuracion/ImportarConfiguracionComponent';
import GenerarExamenesComponent from './components/examenes/GenerarExamenesComponent';
import AsignarExamenesComponent from './components/examenes/AsignarExamenesComponent';
import CorregirExamenesComponent from './components/examenes/CorregirExamenesComponent';

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
      <Route path="/asignaturas/crear" element={<ProtectedRoute><AsignaturaFormComponent /></ProtectedRoute>} />
      <Route path="/asignaturas/editar/:id" element={<ProtectedRoute><AsignaturaFormComponent /></ProtectedRoute>} />
      <Route path="/asignaturas/eliminar/:id" element={<ProtectedRoute><EliminarAsignaturaComponent /></ProtectedRoute>} />
      <Route path="/preguntas" element={<ProtectedRoute><PreguntasListComponent /></ProtectedRoute>} />
      <Route path="/preguntas/crear/:asignaturaId" element={<ProtectedRoute><PreguntaFormComponent /></ProtectedRoute>} />
      <Route path="/preguntas/crear" element={<ProtectedRoute><PreguntaFormComponent /></ProtectedRoute>} />
      <Route path="/preguntas/editar/:id" element={<ProtectedRoute><PreguntaFormComponent isEditing /></ProtectedRoute>} />
      <Route path="/preguntas/eliminar/:id" element={<ProtectedRoute><EliminarPreguntaComponent /></ProtectedRoute>} />
      <Route path="/preguntas/asignatura/:id" element={<ProtectedRoute><PreguntasListComponent /></ProtectedRoute>} />
      <Route path="/preguntas/:id/respuestas" element={<ProtectedRoute><RespuestasListComponent /></ProtectedRoute>} />
      <Route path="/respuestas/crear/:preguntaId" element={<ProtectedRoute><RespuestaFormComponent /></ProtectedRoute>} />
      <Route path="/respuestas/editar/:id" element={<ProtectedRoute><RespuestaFormComponent isEditing /></ProtectedRoute>} />
      <Route path="/respuestas/eliminar/:id/:preguntaId" element={<ProtectedRoute><EliminarRespuestaComponent /></ProtectedRoute>} />
      <Route path="/respuestas/eliminar/:id" element={<ProtectedRoute><EliminarRespuestaComponent /></ProtectedRoute>} />
      <Route path="/configuracion/exportar" element={<ProtectedRoute><ExportarConfiguracionComponent /></ProtectedRoute>} />
      <Route path="/configuracion/importar" element={<ProtectedRoute><ImportarConfiguracionComponent /></ProtectedRoute>} />
      <Route path="/examenes/generar" element={<ProtectedRoute><GenerarExamenesComponent /></ProtectedRoute>} />
      <Route path="/examenes/asignar" element={<ProtectedRoute><AsignarExamenesComponent /></ProtectedRoute>} />
      <Route path="/examenes/corregir" element={<ProtectedRoute><CorregirExamenesComponent /></ProtectedRoute>} />
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
