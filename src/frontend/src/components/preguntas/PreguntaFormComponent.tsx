import { useState, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { preguntasService, PreguntaDTO, PreguntaCreateDTO, PreguntaUpdateDTO } from '../../services/preguntasService';
import { asignaturasService, AsignaturaDTO } from '../../services/asignaturasService';

interface PreguntaFormProps {
  pregunta?: PreguntaDTO;
  isEditing?: boolean;
}

export default function PreguntaFormComponent({ pregunta: preguntaProp, isEditing = false }: PreguntaFormProps) {
  const navigate = useNavigate();
  const params = useParams();
  const urlAsignaturaId = params.asignaturaId ? Number(params.asignaturaId) : undefined;
  const preguntaId = params.id ? Number(params.id) : undefined;

  const [pregunta, setPregunta] = useState<PreguntaDTO | undefined>(preguntaProp);
  const [asignaturas, setAsignaturas] = useState<AsignaturaDTO[]>([]);
  const [selectedAsignatura, setSelectedAsignatura] = useState<number>(preguntaProp?.asignaturaId || urlAsignaturaId || 0);
  const [enunciado, setEnunciado] = useState(preguntaProp?.enunciado || '');
  const [tema, setTema] = useState<string>(preguntaProp?.tema || 'TEMA_1');
  const [dificultad, setDificultad] = useState<string>(preguntaProp?.dificultad || 'FACIL');
  const [habilitada, setHabilitada] = useState<boolean>(preguntaProp?.habilitada ?? true);
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const [loadingData, setLoadingData] = useState(isEditing && !preguntaProp);

  useEffect(() => {
    if (isEditing && !preguntaProp && preguntaId) {
      setLoadingData(true);
      preguntasService.getPreguntaById(preguntaId)
        .then((data) => {
          setPregunta(data);
          setEnunciado(data.enunciado);
          setTema(data.tema);
          setDificultad(data.dificultad);
          setHabilitada(data.habilitada);
          setSelectedAsignatura(data.asignaturaId);
        })
        .catch((err: any) => setError(err.response?.data?.error || 'Error al cargar la pregunta'))
        .finally(() => setLoadingData(false));
      return;
    }

    if (!isEditing) {
      setLoadingData(true);
      asignaturasService.getAsignaturas()
        .then((data) => {
          setAsignaturas(data);
          if (urlAsignaturaId) {
            setSelectedAsignatura(urlAsignaturaId);
          }
        })
        .catch((err: any) => setError(err.response?.data?.error || 'Error al cargar asignaturas'))
        .finally(() => setLoadingData(false));
    }
  }, [isEditing, preguntaProp, preguntaId, urlAsignaturaId]);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError('');
    setLoading(true);

    try {
      if (isEditing && pregunta?.id) {
        const dto: PreguntaUpdateDTO = {
          enunciado,
          tema,
          dificultad: dificultad as 'FACIL' | 'MEDIO' | 'DIFICIL',
          habilitada,
        };
        await preguntasService.actualizarPregunta(pregunta.id, dto);
        navigate('/preguntas');
      } else {
        const dto: PreguntaCreateDTO = {
          asignaturaId: selectedAsignatura,
          enunciado,
          tema,
          dificultad: dificultad as 'FACIL' | 'MEDIO' | 'DIFICIL',
        };
        const created = await preguntasService.crearPregunta(dto);
        navigate(`/preguntas/editar/${created.id}`);
      }
    } catch (err: any) {
      setError(err.response?.data?.error || `Error al ${isEditing ? 'actualizar' : 'crear'} la pregunta`);
    } finally {
      setLoading(false);
    }
  };

  const handleCancel = () => {
    navigate('/preguntas');
  };

  const handleVerRespuestas = () => {
    if (pregunta?.id) {
      navigate(`/preguntas/${pregunta.id}/respuestas`);
    }
  };

  return (
    <div className="container mt-4">
      <div className="row justify-content-center">
        <div className="col-md-6">
          <div className="card">
            <div className="card-header">
              <h4>{isEditing ? 'Editar Pregunta' : 'Crear Pregunta'}</h4>
            </div>
            <div className="card-body">
              {loadingData ? (
                <div className="text-center">Cargando...</div>
              ) : (
                <>
                  {error && <div className="alert alert-danger">{error}</div>}

                  <form onSubmit={handleSubmit}>
                    {urlAsignaturaId && !isEditing ? (
                      <div className="mb-3">
                        <label className="form-label">Asignatura</label>
                        <div className="form-control-plaintext">
                          {asignaturas.find(a => a.id === urlAsignaturaId)?.titulo || 'Cargando...'}
                        </div>
                      </div>
                    ) : !isEditing ? (
                      <div className="mb-3">
                        <label htmlFor="asignatura" className="form-label">Asignatura</label>
                        <select
                          className="form-select"
                          id="asignatura"
                          value={selectedAsignatura}
                          onChange={(e) => setSelectedAsignatura(Number(e.target.value))}
                          required
                        >
                          <option value="">Seleccionar asignatura</option>
                          {asignaturas.map((asig) => (
                            <option key={asig.id} value={asig.id}>
                              {asig.titulo} ({asig.codigo})
                            </option>
                          ))}
                        </select>
                      </div>
                    ) : (
                      <div className="mb-3">
                        <label className="form-label">Asignatura</label>
                        <div className="form-control-plaintext">
                          {asignaturas.find(a => a.id === selectedAsignatura)?.titulo || `ID: ${selectedAsignatura}`}
                        </div>
                      </div>
                    )}

                    <div className="mb-3">
                      <label htmlFor="enunciado" className="form-label">Enunciado</label>
                      <textarea
                        className="form-control"
                        id="enunciado"
                        value={enunciado}
                        onChange={(e) => setEnunciado(e.target.value)}
                        required
                        minLength={10}
                        maxLength={500}
                        rows={3}
                      />
                      <div className="form-text">Minimo 10 caracteres, maximo 500</div>
                    </div>

                    <div className="mb-3">
                      <label htmlFor="tema" className="form-label">Tema</label>
                      <input
                        type="text"
                        className="form-control"
                        id="tema"
                        value={tema}
                        onChange={(e) => setTema(e.target.value)}
                        required
                        placeholder="Ej: Tema 1 - Introduccion"
                      />
                    </div>

                    <div className="mb-3">
                      <label htmlFor="dificultad" className="form-label2">Dificultad</label>
                      <select
                        className="form-select"
                        id="dificultad"
                        value={dificultad}
                        onChange={(e) => setDificultad(e.target.value)}
                        required
                      >
                        <option value="FACIL">Facil</option>
                        <option value="MEDIO">Medio</option>
                        <option value="DIFICIL">Dificil</option>
                      </select>
                    </div>

                    {isEditing && (
                      <div className="mb-3">
                        <label htmlFor="habilitada" className="form-label">Habilitada</label>
                        <div className="form-check form-switch">
                          <input
                            type="checkbox"
                            className="form-check-input"
                            id="habilitada"
                            checked={habilitada}
                            onChange={(e) => setHabilitada(e.target.checked)}
                          />
                          <label className="form-check-label" htmlFor="habilitada">
                            {habilitada ? 'Habilitada para examenes' : 'Deshabilitada'}
                          </label>
                        </div>
                      </div>
                    )}

                    <div className="d-flex gap-2">
                      <button type="submit" className="btn btn-primary" disabled={loading}>
                        {loading ? 'Guardando...' : isEditing ? 'Guardar Cambios' : 'Crear Pregunta'}
                      </button>
                      <button type="button" className="btn btn-secondary" onClick={handleCancel}>
                        Cancelar
                      </button>
                      {isEditing && (
                        <>
                          <button
                            type="button"
                            className="btn btn-info"
                            onClick={handleVerRespuestas}
                          >
                            Ver Respuestas
                          </button>
                          <button
                            type="button"
                            className="btn btn-danger ms-auto"
                            onClick={() => navigate(`/preguntas/eliminar/${preguntaId}`)}
                          >
                            Eliminar
                          </button>
                        </>
                      )}
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
