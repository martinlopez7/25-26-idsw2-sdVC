import { useAuth } from '../../context/AuthContext';
import LogoutButton from '../auth/LogoutButton';

export default function DocenteMenu() {
  const { user } = useAuth();

  const handleNavigation = (path: string) => {
    window.location.href = path;
  };

  return (
    <div className="container mt-4">
      <div className="d-flex justify-content-between align-items-center mb-4">
        <h1>Panel del Docente</h1>
        <LogoutButton />
      </div>

      <div className="row mt-4">
        <div className="col-md-3 mb-3">
          <div className="card">
            <div className="card-body">
              <h5 className="card-title">Grados</h5>
              <p className="card-text">Gestionar grados</p>
              <button
                className="btn btn-primary w-100"
                onClick={() => handleNavigation('/grados')}
              >
                Ver Grados
              </button>
            </div>
          </div>
        </div>

        <div className="col-md-3 mb-3">
          <div className="card">
            <div className="card-body">
              <h5 className="card-title">Asignaturas</h5>
              <p className="card-text">Gestionar asignaturas</p>
              <button
                className="btn btn-primary w-100"
                onClick={() => handleNavigation('/asignaturas')}
              >
                Ver Asignaturas
              </button>
            </div>
          </div>
        </div>

        <div className="col-md-3 mb-3">
          <div className="card">
            <div className="card-body">
              <h5 className="card-title">Alumnos</h5>
              <p className="card-text">Gestionar alumnos</p>
              <button
                className="btn btn-primary w-100"
                onClick={() => handleNavigation('/alumnos')}
              >
                Ver Alumnos
              </button>
            </div>
          </div>
        </div>

        <div className="col-md-3 mb-3">
          <div className="card">
            <div className="card-body">
              <h5 className="card-title">Preguntas</h5>
              <p className="card-text">Gestionar preguntas</p>
              <button
                className="btn btn-primary w-100"
                onClick={() => handleNavigation('/preguntas')}
              >
                Ver Preguntas
              </button>
            </div>
          </div>
        </div>
      </div>

      <div className="row mt-3">
        <div className="col-md-4 mb-3">
          <div className="card">
            <div className="card-body">
              <h5 className="card-title">Generar Exámenes</h5>
              <p className="card-text">Generar y asignar exámenes</p>
              <button
                className="btn btn-success w-100"
                onClick={() => handleNavigation('/examenes/generar')}
              >
                Generar
              </button>
            </div>
          </div>
        </div>

        <div className="col-md-4 mb-3">
          <div className="card">
            <div className="card-body">
              <h5 className="card-title">Exportar</h5>
              <p className="card-text">Exportar configuración global</p>
              <button
                className="btn btn-info w-100"
                onClick={() => handleNavigation('/configuracion/exportar')}
              >
                Exportar
              </button>
            </div>
          </div>
        </div>

        <div className="col-md-4 mb-3">
          <div className="card">
            <div className="card-body">
              <h5 className="card-title">Importar</h5>
              <p className="card-text">Importar configuración global</p>
              <button
                className="btn btn-info w-100"
                onClick={() => handleNavigation('/configuracion/importar')}
              >
                Importar
              </button>
            </div>
          </div>
        </div>

        <div className="col-md-4 mb-3">
          <div className="card">
            <div className="card-body">
              <h5 className="card-title">Corregir Exámenes</h5>
              <p className="card-text">Corregir exámenes realizados</p>
              <button
                className="btn btn-warning w-100"
                onClick={() => handleNavigation('/examenes/corregir')}
              >
                Corregir
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}