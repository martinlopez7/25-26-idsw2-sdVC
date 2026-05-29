import { useAuth } from '../../context/AuthContext';
import { authService } from '../../services/authService';

export default function LogoutButton() {
  const { token, logout } = useAuth();

  const handleLogout = async () => {
    if (token) {
      try {
        await authService.logout(token);
      } catch (err) {
        console.error('Logout error:', err);
      }
    }
    logout();
  };

  return (
    <button className="btn btn-outline-danger" onClick={handleLogout}>
      Cerrar Sesión
    </button>
  );
}
