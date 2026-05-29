import { BrowserRouter, Routes, Route, Navigate } from 'react-router-dom';
import { AuthProvider, useAuth } from './context/AuthContext';
import 'bootstrap/dist/css/bootstrap.min.css';
import LoginPage from './components/auth/LoginPage';
import LogoutButton from './components/auth/LogoutButton';

function ProtectedRoute({ children }: { children: React.ReactNode }) {
  const { isAuthenticated } = useAuth();
  return isAuthenticated ? <>{children}</> : <Navigate to="/login" />;
}

function AppRoutes() {
  const { isAuthenticated } = useAuth();

  return (
    <Routes>
      <Route path="/login" element={isAuthenticated ? <Navigate to="/menu" /> : <LoginPage />} />
      <Route path="/menu" element={<ProtectedRoute><div className="p-4"><h1>Menú Principal</h1><LogoutButton /></div></ProtectedRoute>} />
      <Route path="*" element={<Navigate to={isAuthenticated ? "/menu" : "/login"} />} />
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
