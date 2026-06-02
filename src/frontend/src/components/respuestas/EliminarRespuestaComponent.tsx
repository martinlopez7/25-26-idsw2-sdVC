import { useState, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { respuestasService, RespuestaDTO } from '../../services/respuestasService';

export default function EliminarRespuestaComponent() {
  const { id, preguntaId } = useParams<{ id: string; preguntaId: string }>();
  const [respuesta, setRespuesta] = useState<RespuestaDTO | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const navigate = useNavigate();

  useEffect(() => {
    if (id) {
      cargarRespuesta();
    }
  }, [id]);

  const cargarRespuesta = async () => {
    try {
      const data = await respuestasService.getRespuestaPorId(Number(id));
      setRespuesta(data);
    } catch (err: any) {
      setError(err.response?.data?.error || 'Error al cargar la respuesta');
    } finally {
      setLoading(false);
    }
  };

  const handleEliminar = async () => {
    try {
      await respuestasService.eliminarRespuesta(Number(id));
      if (preguntaId) {
        navigate('/preguntas/' + preguntaId + '/respuestas');
      } else {
        navigate('/respuestas');
      }
    } catch (err: any) {
      const errorMsg = err.response?.data?.error || 'No se pudo eliminar la respuesta';
      setError(errorMsg);
    }
  };

  const handleCancelar = () => {
    if (preguntaId) {
      navigate('/preguntas/' + preguntaId + '/respuestas');
    } else {
      navigate('/respuestas');
    }
  };

  if (loading) {
    return <div className="container mt-4">Cargando...</div>;
  }

  if (!respuesta) {
    return (
      <div className="container mt-4">
        <div className="alert alert-danger">{error || 'Respuesta no encontrada'}</div>
        <button className="btn btn-secondary" onClick={handleCancelar}>
          Volver
        </button>
      </div>
    );
  }

  return (
    <div className="container mt-4">
      <div className="card">
        <div className="card-header bg-danger text-white">
          <h5>Confirmar Eliminacion</h5>
        </div>
        <div className="card-body">
          {error && <div className="alert alert-danger mb-3">{error}</div>}

          <p>Esta seguro de que desea eliminar la siguiente respuesta?</p>

          <table className="table table-bordered">
            <tbody>
              <tr>
                <th style={{ width: '30%' }}>Opcion:</th>
                <td>{respuesta.opcion}</td>
              </tr>
              <tr>
                <th>Es Correcta?</th>
                <td>{respuesta.esCorrecta ? 'Si' : 'No'}</td>
              </tr>
            </tbody>
          </table>

          <div className="alert alert-warning">
            <strong>Advertencia:</strong> Esta accion es irreversible.
          </div>

          <div className="d-flex gap-2 justify-content-end">
            <button className="btn btn-secondary" onClick={handleCancelar}>
              Cancelar
            </button>
            <button className="btn btn-danger" onClick={handleEliminar}>
              Eliminar
            </button>
          </div>
        </div>
      </div>
    </div>
  );
}
