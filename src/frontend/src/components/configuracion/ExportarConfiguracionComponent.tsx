import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { configuracionService } from '../../services/configuracionService';

export default function ExportarConfiguracionComponent() {
  const [loading, setLoading] = useState(false);
  const [mostrarDialogo, setMostrarDialogo] = useState(true);
  const [error, setError] = useState('');
  const navigate = useNavigate();

  const handleExportar = async () => {
    setLoading(true);
    setError('');
    try {
      await configuracionService.descargarConfiguracion();
      navigate('/menu-docente');
    } catch (err: any) {
      setError(err.response?.data?.error || 'Error al exportar la configuración');
    } finally {
      setLoading(false);
    }
  };

  const handleCancelar = () => {
    navigate('/menu-docente');
  };

  if (!mostrarDialogo) {
    return null;
  }

  return (
    <div className="container mt-4">
      <div className="card">
        <div className="card-header bg-info text-white">
          <h5>Exportar Configuración Global</h5>
        </div>
        <div className="card-body">
          {error && <div className="alert alert-danger mb-3">{error}</div>}

          <p>¿Está seguro de que desea exportar toda la configuración del sistema?</p>

          <div className="alert alert-warning">
            <strong>Nota:</strong> La exportación incluirá todos los elementos de su configuración:
            <ul className="mb-0 mt-2">
              <li>Grados</li>
              <li>Asignaturas</li>
              <li>Alumnos</li>
              <li>Preguntas (con sus respuestas)</li>
            </ul>
          </div>

          <p className="text-muted">
            El archivo generado podrá ser importado en otro sistema.
          </p>

          <div className="d-flex gap-2 justify-content-end">
            <button
              className="btn btn-secondary"
              onClick={handleCancelar}
              disabled={loading}
            >
              Cancelar
            </button>
            <button
              className="btn btn-primary"
              onClick={handleExportar}
              disabled={loading}
            >
              {loading ? 'Exportando...' : 'Exportar'}
            </button>
          </div>
        </div>
      </div>
    </div>
  );
}