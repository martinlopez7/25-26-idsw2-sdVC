import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { examenesService } from '../../services/examenesService';

export default function AsignarExamenesComponent() {
  const navigate = useNavigate();
  const [loading, setLoading] = useState(false);

  const handleCancelar = async () => {
    if (window.confirm('¿Está seguro de que desea cancelar la generación de exámenes?')) {
      try {
        setLoading(true);
        await examenesService.cancelarGeneracion();
        navigate('/menu-docente');
      } catch (err: any) {
        alert(err.response?.data?.error || 'Error al cancelar la generación');
      } finally {
        setLoading(false);
      }
    }
  };

  return (
    <div className="container mt-4">
      <div className="row justify-content-center">
        <div className="col-md-8">
          <div className="card">
            <div className="card-header bg-warning text-dark">
              <h4>Asignar Exámenes</h4>
            </div>
            <div className="card-body text-center">
              <p className="lead">Funcionalidad en desarrollo...</p>
              <p>Esta pantalla permitirá asignar los exámenes generados a los alumnos.</p>
              <div className="d-flex gap-2 justify-content-center mt-4">
                <button
                  className="btn btn-danger"
                  onClick={handleCancelar}
                  disabled={loading}
                >
                  {loading ? 'Cancelando...' : 'Cancelar Generación'}
                </button>
                <button
                  className="btn btn-secondary"
                  onClick={() => navigate('/menu-docente')}
                >
                  Volver al Menú
                </button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}