import { useState, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { respuestasService, RespuestaDTO } from '../../services/respuestasService';

interface Props {
  preguntaId?: number;
}

export default function RespuestasListComponent({ preguntaId: propsPreguntaId }: Props) {
  const params = useParams<{ id: string }>();
  const urlPreguntaId = params.id ? parseInt(params.id) : undefined;
  const preguntaId = propsPreguntaId || urlPreguntaId;

  const [respuestas, setRespuestas] = useState<RespuestaDTO[]>([]);
  const [filtro, setFiltro] = useState('');
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string>('');
  const navigate = useNavigate();

  useEffect(() => {
    if (preguntaId) {
      cargarRespuestas();
    }
  }, [preguntaId]);

  const cargarRespuestas = async (criterio?: string) => {
    if (!preguntaId) return;
    setLoading(true);
    setError('');
    try {
      const data = await respuestasService.getRespuestasPorPregunta(preguntaId, criterio);
      setRespuestas(data);
    } catch (err: any) {
      setError(err.response?.data?.error || 'Error al cargar respuestas');
    } finally {
      setLoading(false);
    }
  };

  const handleFiltrar = (e: React.FormEvent) => {
    e.preventDefault();
    cargarRespuestas(filtro);
  };

  const handleLimpiarFiltro = () => {
    setFiltro('');
    cargarRespuestas();
  };

  const handleVolver = () => {
    if (preguntaId) {
      navigate(`/preguntas/editar/${preguntaId}`);
    } else {
      navigate('/menu-docente');
    }
  };

  return (
    <div className="container mt-4">
      <div className="d-flex justify-content-between align-items-center mb-4">
        <h1>Respuestas</h1>
        <button className="btn btn-secondary" onClick={handleVolver}>
          Volver a Pregunta
        </button>
      </div>

      <div className="card mb-4">
        <div className="card-body">
          <form onSubmit={handleFiltrar} className="d-flex gap-2">
            <input
              type="text"
              className="form-control"
              placeholder="Buscar por contenido..."
              value={filtro}
              onChange={(e) => setFiltro(e.target.value)}
            />
            <button type="submit" className="btn btn-primary">Filtrar</button>
            <button type="button" className="btn btn-outline-secondary" onClick={handleLimpiarFiltro}>
              Limpiar
            </button>
          </form>
        </div>
      </div>

      {error && <div className="alert alert-danger">{error}</div>}

      {loading ? (
        <div className="text-center">Cargando...</div>
      ) : (
        <>
          <div className="mb-3">
            <button className="btn btn-success me-2" onClick={() => navigate(`/respuestas/crear/${preguntaId}`)}>
              Crear Respuesta
            </button>
          </div>

          {respuestas.length === 0 ? (
            <div className="alert alert-info">No hay respuestas registradas</div>
          ) : (
            <table className="table table-striped">
              <thead>
                <tr>
                  <th>Opción</th>
                  <th>¿Es Correcta?</th>
                  <th>Acciones</th>
                </tr>
              </thead>
              <tbody>
                {respuestas.map((respuesta) => (
                  <tr key={respuesta.id}>
                    <td>{respuesta.opcion}</td>
                    <td>{respuesta.esCorrecta ? 'Sí' : 'No'}</td>
                    <td>
                      <button
                        className="btn btn-sm btn-primary me-2"
                        onClick={() => navigate(`/respuestas/editar/${respuesta.id}`)}
                      >
                        Editar
                      </button>
                      <button
                        className="btn btn-sm btn-danger"
                        onClick={() => navigate(`/respuestas/eliminar/${respuesta.id}`)}
                      >
                        Eliminar
                      </button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          )}
        </>
      )}
    </div>
  );
}
