import { useState, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { preguntasService, PreguntaDTO, PreguntaCreateDTO } from '../../services/preguntasService';
import { asignaturasService, AsignaturaDTO } from '../../services/asignaturasService';

interface PreguntaFormProps {
  pregunta?: PreguntaDTO;
  isEditing?: boolean;
}

export default function PreguntaFormComponent({ pregunta: preguntaProp, isEditing = false }: PreguntaFormProps) {
  const navigate = useNavigate();
  const params = useParams();
  const urlAsignaturaId = params.asignaturaId ? Number(params.asignaturaId) : undefined;

  const [asignaturas, setAsignaturas] = useState<AsignaturaDTO[]>([]);
  const [selectedAsignatura, setSelectedAsignatura] = useState<number>(preguntaProp?.asignaturaId || urlAsignaturaId || 0);
  const [enunciado, setEnunciado] = useState(preguntaProp?.enunciado || '');
  const [tema, setTema] = useState<string>(preguntaProp?.tema || 'TEMA_1');
  const [dificultad, setDificultad] = useState<string>(preguntaProp?.dificultad || 'FACIL');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const [loadingData, setLoadingData] = useState(false);

  useEffect(() => {
    if (isEditing) {
      setLoadingData(false);
      return;
    }
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
  }, [isEditing, urlAsignaturaId]);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError('');
    setLoading(true);

    try {
      const dto: PreguntaCreateDTO = {
        asignaturaId: selectedAsignatura,
        enunciado,
        tema,
        dificultad: dificultad as 'FACIL' | 'MEDIO' | 'DIFICIL',
      };

      const created = await preguntasService.crearPregunta(dto);
      navigate(`/preguntas/editar/${created.id}`);
    } catch (err: any) {
      setError(err.response?.data?.error || 'Error al crear la pregunta');
    } finally {
      setLoading(false);
    }
  };

  const handleCancel = () => {
    navigate('/preguntas');
  };

  return (
    <div className="container mt-4">
      <div className="row justify-content-center">
        <div className="col-md-6">
          <div className="card">
            <div className="card-header">
              <h4>Crear Pregunta</h4>
            </div>
            <div className="card-body">
              {loadingData ? (
                <div className="text-center">Cargando...</div>
              ) : (
                <>
                  {error && <div className="alert alert-danger">{error}</div>}

                  <form onSubmit={handleSubmit}>
                    {urlAsignaturaId ? (
                      <div className="mb-3">
                        <label className="form-label">Asignatura</label>
                        <div className="form-control-plaintext">
                          {asignaturas.find(a => a.id === urlAsignaturaId)?.titulo || 'Cargando...'}
                        </div>
                      </div>
                    ) : (
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
                      <label htmlFor="dificultad" className="form-label">Dificultad</label>
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

                    <div className="d-flex gap-2">
                      <button type="submit" className="btn btn-primary" disabled={loading}>
                        {loading ? 'Guardando...' : 'Crear Pregunta'}
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
