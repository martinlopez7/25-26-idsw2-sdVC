import { useState, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { preguntasService, PreguntaDTO } from '../../services/preguntasService';

export default function EliminarPreguntaComponent() {
  const { id } = useParams<{ id: string }>();
  const [pregunta, setPregunta] = useState<PreguntaDTO | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const navigate = useNavigate();

  useEffect(() => {
    if (id) {
      cargarPregunta();
    }
  }, [id]);

  const cargarPregunta = async () => {
    try {
      const data = await preguntasService.getPreguntaById(Number(id));
      setPregunta(data);
    } catch (err: any) {
      setError(err.response?.data?.error || 'Error al cargar la pregunta');
    } finally {
      setLoading(false);
    }
  };

  const handleEliminar = async () => {
    try {
      await preguntasService.eliminarPregunta(Number(id));
      navigate('/preguntas');
    } catch (err: any) {
      const errorMsg = err.response?.data?.error || 'No se pudo eliminar la pregunta';
      setError(errorMsg);
    }
  };

  const handleCancelar = () => {
    navigate('/preguntas');
  };

  if (loading) {
    return <div className="container mt-4">Cargando...</div>;
  }

  if (!pregunta) {
    return (
      <div className="container mt-4">
        <div className="alert alert-danger">{error || 'Pregunta no encontrada'}</div>
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
          <h5>Confirmar Eliminación</h5>
        </div>
        <div className="card-body">
          {error && <div className="alert alert-danger mb-3">{error}</div>}

          <p>¿Está seguro de que desea eliminar la siguiente pregunta?</p>

          <table className="table table-bordered">
            <tbody>
              <tr>
                <th style={{ width: '30%' }}>Enunciado:</th>
                <td>{pregunta.enunciado}</td>
              </tr>
              <tr>
                <th>Tema:</th>
                <td>{pregunta.tema}</td>
              </tr>
              <tr>
                <th>Dificultad:</th>
                <td>{pregunta.dificultad}</td>
              </tr>
              <tr>
                <th>Respuestas:</th>
                <td>
                  {pregunta.respuestas && pregunta.respuestas.length > 0 ? (
                    <ul className="mb-0">
                      {pregunta.respuestas.map((resp) => (
                        <li key={resp.id}>
                          {resp.opcion} {resp.esCorrecta ? '(Correcta)' : ''}
                        </li>
                      ))}
                    </ul>
                  ) : (
                    'Sin respuestas'
                  )}
                </td>
              </tr>
            </tbody>
          </table>

          <div className="alert alert-warning">
            <strong>Advertencia:</strong> Esta acción es irreversible. Se eliminarán también todas las respuestas asociadas a esta pregunta.
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