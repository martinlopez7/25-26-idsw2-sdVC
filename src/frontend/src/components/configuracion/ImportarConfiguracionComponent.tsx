import { useNavigate } from 'react-router-dom';

export default function ImportarConfiguracionComponent() {
  const navigate = useNavigate();

  return (
    <div className="container mt-4">
      <div className="card">
        <div className="card-header bg-secondary text-white">
          <h5>Importar Configuración Global</h5>
        </div>
        <div className="card-body">
          <div className="alert alert-info">
            <strong>Funcionalidad pendiente de implementar.</strong>
            <p className="mb-0">El caso de uso importarConfiguracionGlobal() aún no está implementado.</p>
          </div>
          <div className="d-flex justify-content-end">
            <button
              className="btn btn-secondary"
              onClick={() => navigate('/menu-docente')}
            >
              Volver
            </button>
          </div>
        </div>
      </div>
    </div>
  );
}