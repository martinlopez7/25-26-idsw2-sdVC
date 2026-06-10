import { useState, useEffect } from 'react';
import { useNavigate, useSearchParams } from 'react-router-dom';
import { examenesService, GenerarExamenesRequest, ConfigGradoDTO, AsignaturaConGradosDTO } from '../../services/examenesService';
import { asignaturasService, AsignaturaDTO } from '../../services/asignaturasService';
import { preguntasService } from '../../services/preguntasService';

export default function GenerarExamenesComponent() {
  const navigate = useNavigate();
  const [searchParams] = useSearchParams();

  const asignaturaIdParam = searchParams.get('asignaturaId');
  const isContextual = Boolean(asignaturaIdParam);

  const [loading, setLoading] = useState(false);
  const [loadingAsignaturas, setLoadingAsignaturas] = useState(true);
  const [loadingTemas, setLoadingTemas] = useState(false);
  const [error, setError] = useState('');
  const [successMessage, setSuccessMessage] = useState('');
  const [generatedCount, setGeneratedCount] = useState(0);

  const [asignaturas, setAsignaturas] = useState<AsignaturaDTO[]>([]);
  const [selectedAsignatura, setSelectedAsignatura] = useState<AsignaturaConGradosDTO | null>(null);
  const [evaluacion, setEvaluacion] = useState('PARCIAL_1');
  const [temas, setTemas] = useState<string[]>([]);
  const [temasDisponibles, setTemasDisponibles] = useState<string[]>([]);
  const [numPreguntas, setNumPreguntas] = useState(5);
  const [configPorGrado, setConfigPorGrado] = useState<Record<number, ConfigGradoDTO>>({});

  useEffect(() => {
    cargarAsignaturas();
  }, []);

  useEffect(() => {
    if (asignaturaIdParam) {
      handleAsignaturaChange(Number(asignaturaIdParam));
    }
  }, [asignaturaIdParam]);

  const cargarAsignaturas = async () => {
    setLoadingAsignaturas(true);
    try {
      const data = await asignaturasService.getAsignaturas();
      setAsignaturas(data);
    } catch (err: any) {
      setError(err.response?.data?.error || 'Error al cargar asignaturas');
    } finally {
      setLoadingAsignaturas(false);
    }
  };

  const handleAsignaturaChange = async (asignaturaId: number) => {
    setSelectedAsignatura(null);
    setConfigPorGrado({});
    setTemas([]);
    setTemasDisponibles([]);

    if (asignaturaId) {
      try {
        const completa = await examenesService.getAsignaturaConGrados(asignaturaId);
        setSelectedAsignatura(completa);

        setLoadingTemas(true);
        const temasData = await preguntasService.obtenerTemasPorAsignatura(asignaturaId);
        setTemasDisponibles(temasData);
      } catch (err: any) {
        setError(err.response?.data?.error || 'Error al cargar datos de la asignatura');
      } finally {
        setLoadingTemas(false);
      }
    }
  };

  const handleTemaToggle = (tema: string) => {
    if (temas.includes(tema)) {
      setTemas(temas.filter(t => t !== tema));
    } else {
      setTemas([...temas, tema]);
    }
  };

  const handleGradoConfigChange = (gradoId: number, field: string, value: number) => {
    setConfigPorGrado(prev => ({
      ...prev,
      [gradoId]: {
        ...prev[gradoId],
        numExamenes: prev[gradoId]?.numExamenes || 1,
        numTiposExamen: prev[gradoId]?.numTiposExamen || 1,
        proporcionDificultad: {
          facil: field === 'facil' ? value : (prev[gradoId]?.proporcionDificultad?.facil ?? 33),
          medio: field === 'medio' ? value : (prev[gradoId]?.proporcionDificultad?.medio ?? 34),
          dificil: field === 'dificil' ? value : (prev[gradoId]?.proporcionDificultad?.dificil ?? 33),
        },
      },
    }));
  };

  const handleNumExamenesChange = (gradoId: number, value: number) => {
    setConfigPorGrado(prev => ({
      ...prev,
      [gradoId]: {
        ...prev[gradoId],
        numExamenes: value,
        numTiposExamen: prev[gradoId]?.numTiposExamen || 1,
        proporcionDificultad: {
          facil: prev[gradoId]?.proporcionDificultad?.facil ?? 33,
          medio: prev[gradoId]?.proporcionDificultad?.medio ?? 34,
          dificil: prev[gradoId]?.proporcionDificultad?.dificil ?? 33,
        },
      },
    }));
  };

  const handleNumTiposExamenChange = (gradoId: number, value: number) => {
    setConfigPorGrado(prev => ({
      ...prev,
      [gradoId]: {
        ...prev[gradoId],
        numExamenes: prev[gradoId]?.numExamenes || 1,
        numTiposExamen: value,
        proporcionDificultad: {
          facil: prev[gradoId]?.proporcionDificultad?.facil ?? 33,
          medio: prev[gradoId]?.proporcionDificultad?.medio ?? 34,
          dificil: prev[gradoId]?.proporcionDificultad?.dificil ?? 33,
        },
      },
    }));
  };

  const validate = (): boolean => {
    if (!selectedAsignatura) {
      setError('Debe seleccionar una asignatura');
      return false;
    }
    if (temas.length === 0) {
      setError('Debe añadir al menos un tema');
      return false;
    }
    if (numPreguntas < 1 || numPreguntas > 50) {
      setError('El número de preguntas debe estar entre 1 y 50');
      return false;
    }
    for (const grado of selectedAsignatura.grados) {
      const config = configPorGrado[grado.id];
      if (!config || !config.numExamenes || config.numExamenes < 1) {
        setError(`Debe configurar el número de exámenes para el grado: ${grado.titulo}`);
        return false;
      }
    }
    return true;
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError('');
    setSuccessMessage('');

    if (!validate()) return;

    setLoading(true);
    try {
      const request: GenerarExamenesRequest = {
        asignaturaId: selectedAsignatura!.id,
        evaluacion,
        temas,
        numPreguntas,
        configPorGrado,
      };

      const response = await examenesService.generarExamenes(request);
      setSuccessMessage(`Exámenes generados correctamente. Total: ${response.totalExamenesGenerados}`);
      setGeneratedCount(response.totalExamenesGenerados);
    } catch (err: any) {
      setError(err.response?.data?.error || 'Error al generar exámenes');
    } finally {
      setLoading(false);
    }
  };

  const handleCancelar = async () => {
    if (window.confirm('¿Está seguro de que desea cancelar la generación de exámenes?')) {
      try {
        await examenesService.cancelarGeneracion();
        navigate('/menu-docente');
      } catch (err: any) {
        setError(err.response?.data?.error || 'Error al cancelar la generación');
      }
    }
  };

  return (
    <div className="container mt-4">
      <div className="row justify-content-center">
        <div className="col-md-10">
          <div className="card">
            <div className="card-header bg-success text-white">
              <h4>{isContextual ? 'Generar Exámenes (Contexto)' : 'Generar Exámenes'}</h4>
            </div>
            <div className="card-body">
              {error && <div className="alert alert-danger">{error}</div>}
              {successMessage && (
                  <div className="alert alert-success">
                    <p className="mb-3">{successMessage}</p>
                    <div className="d-flex gap-2">
                      <button
                        type="button"
                        className="btn btn-success"
                        onClick={() => navigate('/examenes/asignar')}
                      >
                        Asignar Exámenes
                      </button>
                      <button
                        type="button"
                        className="btn btn-danger"
                        onClick={handleCancelar}
                      >
                        Cancelar Generación
                      </button>
                    </div>
                  </div>
                )}

              <form onSubmit={handleSubmit}>
                <div className="mb-3">
                  <label className="form-label">Asignatura</label>
                  <select
                    className="form-select"
                    value={selectedAsignatura?.id || ''}
                    onChange={e => handleAsignaturaChange(Number(e.target.value))}
                    disabled={loadingAsignaturas || isContextual}
                    required
                  >
                    <option value="">-- Seleccionar asignatura --</option>
                    {asignaturas.map(asignatura => (
                      <option key={asignatura.id} value={asignatura.id}>
                        {asignatura.titulo} ({asignatura.codigo})
                      </option>
                    ))}
                  </select>
                </div>

                <div className="mb-3">
                  <label className="form-label">Evaluación</label>
                  <select
                    className="form-select"
                    value={evaluacion}
                    onChange={e => setEvaluacion(e.target.value)}
                    required
                  >
                    <option value="PARCIAL_1">Parcial 1</option>
                    <option value="PARCIAL_2">Parcial 2</option>
                    <option value="PARCIAL_3">Parcial 3</option>
                    <option value="EXAMEN_FINAL">Examen Final</option>
                    <option value="EXAMEN_EXTRAORDINARIO">Examen Extraordinario</option>
                  </select>
                </div>

                <div className="mb-3">
                  <label className="form-label">Temas disponibles en la batería</label>
                  <div className="border rounded p-3" style={{ maxHeight: '200px', overflowY: 'auto' }}>
                    {loadingTemas ? (
                      <p>Cargando temas...</p>
                    ) : temasDisponibles.length === 0 ? (
                      <p className="text-muted">No hay temas disponibles para esta asignatura</p>
                    ) : (
                      temasDisponibles.map(tema => (
                        <div key={tema} className="form-check">
                          <input
                            type="checkbox"
                            className="form-check-input"
                            id={`tema-${tema}`}
                            checked={temas.includes(tema)}
                            onChange={() => handleTemaToggle(tema)}
                          />
                          <label className="form-check-label" htmlFor={`tema-${tema}`}>
                            {tema}
                          </label>
                        </div>
                      ))
                    )}
                  </div>
                  <small className="text-muted">{temas.length} tema(s) seleccionado(s)</small>
                </div>

                <div className="mb-3">
                  <label className="form-label">Número de preguntas por examen</label>
                  <input
                    type="number"
                    className="form-control"
                    value={numPreguntas}
                    onChange={e => setNumPreguntas(Number(e.target.value))}
                    min={1}
                    max={50}
                    required
                  />
                </div>

                {selectedAsignatura && selectedAsignatura.grados.length > 0 && (
                  <div className="mb-3">
                    <label className="form-label">Configuración por Grado</label>
                    {selectedAsignatura.grados.map(grado => (
                      <div key={grado.id} className="card mb-2">
                        <div className="card-body">
                          <h6>{grado.titulo} ({grado.codigo}) - {grado.numAlumnos} alumnos</h6>
                          <div className="row">
                            <div className="col-md-4">
                              <label className="form-label">Nº Exámenes</label>
                              <input
                                type="number"
                                className="form-control"
                                value={configPorGrado[grado.id]?.numExamenes || 1}
                                onChange={e => handleNumExamenesChange(grado.id, Number(e.target.value))}
                                min={1}
                              />
                            </div>
                            <div className="col-md-4">
                              <label className="form-label">Nº Tipos Examen</label>
                              <input
                                type="number"
                                className="form-control"
                                value={configPorGrado[grado.id]?.numTiposExamen || 1}
                                onChange={e => handleNumTiposExamenChange(grado.id, Number(e.target.value))}
                                min={1}
                                max={3}
                              />
                            </div>
                            <div className="col-md-4">
                              <label className="form-label">% Fácil</label>
                              <input
                                type="number"
                                className="form-control"
                                value={configPorGrado[grado.id]?.proporcionDificultad?.facil || 33}
                                onChange={e => handleGradoConfigChange(grado.id, 'facil', Number(e.target.value))}
                                min={0}
                                max={100}
                              />
                            </div>
                          </div>
                          <div className="row mt-2">
                            <div className="col-md-6">
                              <label className="form-label">% Medio</label>
                              <input
                                type="number"
                                className="form-control"
                                value={configPorGrado[grado.id]?.proporcionDificultad?.medio || 34}
                                onChange={e => handleGradoConfigChange(grado.id, 'medio', Number(e.target.value))}
                                min={0}
                                max={100}
                              />
                            </div>
                            <div className="col-md-6">
                              <label className="form-label">% Difícil</label>
                              <input
                                type="number"
                                className="form-control"
                                value={configPorGrado[grado.id]?.proporcionDificultad?.dificil || 33}
                                onChange={e => handleGradoConfigChange(grado.id, 'dificil', Number(e.target.value))}
                                min={0}
                                max={100}
                              />
                            </div>
                          </div>
                        </div>
                      </div>
                    ))}
                  </div>
                )}

                <div className="d-flex gap-2">
                  <button type="submit" className="btn btn-success" disabled={loading}>
                    {loading ? 'Generando...' : 'Generar Exámenes'}
                  </button>
                  <button
                    type="button"
                    className="btn btn-secondary"
                    onClick={() => navigate(isContextual ? `/asignaturas/editar/${asignaturaIdParam}` : '/menu-docente')}
                  >
                    Volver
                  </button>
                </div>
              </form>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}