import { useState, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { respuestasService, RespuestaCreateDTO, RespuestaDTO } from '../../services/respuestasService';

interface RespuestaFormProps {
  respuesta?: RespuestaDTO;
  isEditing?: boolean;
}

export default function RespuestaFormComponent({ respuesta: respuestaProp, isEditing = false }: RespuestaFormProps) {
  const navigate = useNavigate();
  const params = useParams<{ preguntaId: string; id: string }>();
  const preguntaId = params.preguntaId ? Number(params.preguntaId) : undefined;
  const respuestaId = params.id ? Number(params.id) : undefined;

  const [respuesta, setRespuesta] = useState<RespuestaDTO | undefined>(respuestaProp);
  const [opcion, setOpcion] = useState(respuestaProp?.opcion || '');
  const [esCorrecta, setEsCorrecta] = useState(respuestaProp?.esCorrecta ?? false);
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const [loadingData, setLoadingData] = useState(false);

  useEffect(() => {
    if (isEditing && !respuestaProp && respuestaId) {
      setLoadingData(true);
      respuestasService.getRespuestasPorPregunta(respuestaId)
        .then((data) => {
          const found = data.find(r => r.id === respuestaId);
          if (found) {
            setRespuesta(found);
            setOpcion(found.opcion);
            setEsCorrecta(found.esCorrecta);
          }
        })
        .catch((err: any) => setError(err.response?.data?.error || 'Error al cargar la respuesta'))
        .finally(() => setLoadingData(false));
    }
  }, [isEditing, respuestaProp, respuestaId]);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError('');
    setLoading(true);

    try {
      if (isEditing && respuesta?.id) {
        await respuestasService.actualizarRespuesta && respuestasService.actualizarRespuesta(respuesta.id, { opcion, esCorrecta });
        navigate(`/respuestas/editar/${respuesta.id}`);
      } else if (preguntaId) {
        const dto: RespuestaCreateDTO = {
          preguntaId,
          opcion,
          esCorrecta,
        };
        const created = await respuestasService.crearRespuesta(dto);
        navigate(`/respuestas/editar/${created.id}`);
      }
    } catch (err: any) {
      setError(err.response?.data?.error || `Error al ${isEditing ? 'actualizar' : 'crear'} la respuesta`);
    } finally {
      setLoading(false);
    }
  };

  const handleCancel = () => {
    if (preguntaId) {
      navigate(`/preguntas/${preguntaId}/respuestas`);
    } else {
      navigate('/menu-docente');
    }
  };

  return (
    <div className="container mt-4">
      <div className="row justify-content-center">
        <div className="col-md-6">
          <div className="card">
            <div className="card-header">
              <h4>{isEditing ? 'Editar Respuesta' : 'Crear Respuesta'}</h4>
            </div>
            <div className="card-body">
              {loadingData ? (
                <div className="text-center">Cargando...</div>
              ) : (
                <>
                  {error && <div className="alert alert-danger">{error}</div>}

                  <form onSubmit={handleSubmit}>
                    <div className="mb-3">
                      <label htmlFor="opcion" className="form-label">Opcion de respuesta</label>
                      <textarea
                        className="form-control"
                        id="opcion"
                        value={opcion}
                        onChange={(e) => setOpcion(e.target.value)}
                        required
                        minLength={1}
                        maxLength={500}
                        rows={3}
                      />
                    </div>

                    <div className="mb-3">
                      <div className="form-check form-switch">
                        <input
                          type="checkbox"
                          className="form-check-input"
                          id="esCorrecta"
                          checked={esCorrecta}
                          onChange={(e) => setEsCorrecta(e.target.checked)}
                        />
                        <label className="form-check-label" htmlFor="esCorrecta">
                          {esCorrecta ? 'Respuesta correcta' : 'Respuesta incorrecta'}
                        </label>
                      </div>
                      <div className="form-text">Marcar si esta es la respuesta correcta a la pregunta</div>
                    </div>

                    <div className="d-flex gap-2">
                      <button type="submit" className="btn btn-primary" disabled={loading}>
                        {loading ? 'Guardando...' : isEditing ? 'Guardar Cambios' : 'Crear Respuesta'}
                      </button>
                      <button type="button" className="btn btn-secondary" onClick={handleCancel}>
                        Cancelar
                      </button>
                    </div>
                  </form>
                </>
              )}
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}