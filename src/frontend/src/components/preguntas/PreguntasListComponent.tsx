import { useState, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { preguntasService, PreguntaDTO } from '../../services/preguntasService';

interface Props {
  asignaturaId?: number;
}

export default function PreguntasListComponent({ asignaturaId: propsAsignaturaId }: Props) {
  const params = useParams<{ id: string }>();
  const urlAsignaturaId = params.id ? parseInt(params.id) : undefined;
  const asignaturaId = propsAsignaturaId || urlAsignaturaId;

  const [preguntas, setPreguntas] = useState<PreguntaDTO[]>([]);
  const [filtro, setFiltro] = useState('');
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string>('');
  const navigate = useNavigate();

  const isContextual = Boolean(asignaturaId);

  useEffect(() => {
    cargarPreguntas();
  }, [asignaturaId]);

  const cargarPreguntas = async (criterio?: string) => {
    setLoading(true);
    setError('');
    try {
      const data = asignaturaId
        ? await preguntasService.getPreguntasPorAsignatura(asignaturaId, criterio)
        : await preguntasService.getPreguntas(criterio);
      setPreguntas(data);
    } catch (err: any) {
      setError(err.response?.data?.error || 'Error al cargar preguntas');
    } finally {
      setLoading(false);
    }
  };

  const handleFiltrar = (e: React.FormEvent) => {
    e.preventDefault();
    cargarPreguntas(filtro);
  };

  const handleLimpiarFiltro = () => {
    setFiltro('');
    cargarPreguntas();
  };

  const getTemaDisplay = (tema: string) => {
    const temas: Record<string, string> = {
      'TEMA_1': 'Tema 1',
      'TEMA_2': 'Tema 2',
      'TEMA_3': 'Tema 3',
      'TEMA_4': 'Tema 4',
      'TEMA_5': 'Tema 5',
    };
    return temas[tema] || tema;
  };

  const getDificultadDisplay = (dificultad: string) => {
    const dificultades: Record<string, string> = {
      'FACIL': 'Fácil',
      'MEDIO': 'Medio',
      'DIFICIL': 'Difícil',
    };
    return dificultades[dificultad] || dificultad;
  };

  const handleVolver = () => {
    if (isContextual && asignaturaId) {
      navigate(`/asignaturas/editar/${asignaturaId}`);
    } else {
      navigate('/menu-docente');
    }
  };

  return (
    <div className="container mt-4">
      <div className="d-flex justify-content-between align-items-center mb-4">
        <h1>{isContextual ? 'Preguntas de la Asignatura' : 'Preguntas'}</h1>
        <button className="btn btn-secondary" onClick={handleVolver}>
          {isContextual ? 'Volver a Asignatura' : 'Completar Gestión'}
        </button>
      </div>

      <div className="card mb-4">
        <div className="card-body">
          <form onSubmit={handleFiltrar} className="d-flex gap-2">
            <input
              type="text"
              className="form-control"
              placeholder="Buscar por enunciado, tema, dificultad..."
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
            <button className="btn btn-success me-2" onClick={() => navigate('/preguntas/crear')}>
              Crear Pregunta
            </button>
          </div>

          {preguntas.length === 0 ? (
            <div className="alert alert-info">No hay preguntas registradas</div>
          ) : (
            <table className="table table-striped">
              <thead>
                <tr>
                  <th>Enunciado</th>
                  <th>Tema</th>
                  <th>Dificultad</th>
                  <th>Respuestas</th>
                  <th>Acciones</th>
                </tr>
              </thead>
              <tbody>
                {preguntas.map((pregunta) => (
                  <tr key={pregunta.id}>
                    <td>{pregunta.enunciado}</td>
                    <td>{getTemaDisplay(pregunta.tema)}</td>
                    <td>{getDificultadDisplay(pregunta.dificultad)}</td>
                    <td>{pregunta.respuestas?.length || 0}</td>
                    <td>
                      <button
                        className="btn btn-sm btn-primary me-2"
                        onClick={() => navigate(`/preguntas/editar/${pregunta.id}`)}
                      >
                        Editar
                      </button>
                      <button
                        className="btn btn-sm btn-danger"
                        onClick={() => navigate(`/preguntas/eliminar/${pregunta.id}`)}
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