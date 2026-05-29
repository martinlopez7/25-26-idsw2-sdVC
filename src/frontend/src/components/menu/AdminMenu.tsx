import { useAuth } from '../../context/AuthContext';
import LogoutButton from '../auth/LogoutButton';

export default function AdminMenu() {
  const { user } = useAuth();

  const handleNavigation = (path: string) => {
    window.location.href = path;
  };

  return (
    <div className="container mt-4">
      <div className="d-flex justify-content-between align-items-center mb-4">
        <h1>Panel del Administrador Institucional</h1>
        <LogoutButton />
      </div>

      <div className="row mt-4">
        <div className="col-md-4 mb-3">
          <div className="card">
            <div className="card-body">
              <h5 className="card-title">Docentes</h5>
              <p className="card-text">Gestionar docentes del sistema</p>
              <button
                className="btn btn-primary w-100"
                onClick={() => handleNavigation('/docentes')}
              >
                Ver Docentes
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}