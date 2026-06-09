import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import {
  examenesService,
  PlantillaExamenDTO,
  AlumnoPorGradoDTO,
  AlumnoDTO,
} from '../../services/examenesService';

export default function AsignarExamenesComponent() {
  const navigate = useNavigate();

  const [loading, setLoading] = useState(false);
  const [loadingData, setLoadingData] = useState(true);
  const [error, setError] = useState('');

  const [plantillas, setPlantillas] = useState<PlantillaExamenDTO[]>([]);
  const [alumnosPorGrado, setAlumnosPorGrado] = useState<AlumnoPorGradoDTO[]>([]);
  const [alumnoNombreMap, setAlumnoNombreMap] = useState<Record<number, string>>({});
  const [gradoNombreMap, setGradoNombreMap] = useState<Record<number, string>>({});

  const [selectedPlantilla, setSelectedPlantilla] = useState<string | null>(null);
  const [selectedAlumno, setSelectedAlumno] = useState<number | null>(null);

  const [asignacionExito, setAsignacionExito] = useState('');
  const [todosAsignados, setTodosAsignados] = useState(false);

  useEffect(() => {
    cargarDatos();
  }, []);

  const cargarDatos = async () => {
    setLoadingData(true);
    setError('');

    try {
      const plantillasData = await examenesService.obtenerPlantillas();
      setPlantillas(plantillasData);

      if (plantillasData.length > 0) {
        const asignaturaId = plantillasData[0].asignaturaId;
        const alumnosData = await examenesService.obtenerAlumnosPorGrado(asignaturaId);
        setAlumnosPorGrado(alumnosData);

        const nombreMap: Record<number, string> = {};
        const gradoMap: Record<number, string> = {};
        alumnosData.forEach((agr) => {
          gradoMap[agr.gradoId] = agr.gradoNombre;
          agr.alumnos.forEach((alumno) => {
            nombreMap[alumno.id] = `${alumno.nombre} ${alumno.apellidos}`;
          });
        });
        setAlumnoNombreMap(nombreMap);
        setGradoNombreMap(gradoMap);

        const todasAsignadas = plantillasData.every((p) => p.asignada);
        setTodosAsignados(todasAsignadas);
      }
    } catch (err: any) {
      setError(err.response?.data?.error || 'Error al cargar los datos');
    } finally {
      setLoadingData(false);
    }
  };

  const getAlumnosDelGrado = (gradoId: number | undefined): AlumnoDTO[] => {
    if (!gradoId) return [];
    const agr = alumnosPorGrado.find((ag) => ag.gradoId === gradoId);
    if (!agr) return [];
    return agr.alumnos.filter((a) => {
      return !plantillas.some((p) => p.alumnoId === a.id && p.asignada);
    });
  };

  const handleSelectPlantilla = (plantillaId: string) => {
    setSelectedPlantilla(plantillaId);
    setSelectedAlumno(null);
  };

  const handleAsignarIndividual = async () => {
    if (!selectedPlantilla || !selectedAlumno) {
      setError('Seleccione un examen y un alumno');
      return;
    }

    setLoading(true);
    setError('');
    setAsignacionExito('');

    try {
      await examenesService.asignarExamen({
        plantillaId: selectedPlantilla,
        alumnoId: selectedAlumno,
      });

      setAsignacionExito('Examen asignado correctamente');

      const plantillasActualizadas = await examenesService.obtenerPlantillas();
      setPlantillas(plantillasActualizadas);

      const todasAsignadas = plantillasActualizadas.every((p) => p.asignada);
      setTodosAsignados(todasAsignadas);

      setSelectedPlantilla(null);
      setSelectedAlumno(null);
    } catch (err: any) {
      setError(err.response?.data?.error || 'Error al asignar el examen');
    } finally {
      setLoading(false);
    }
  };

  const handleAsignarTodos = async () => {
    setLoading(true);
    setError('');
    setAsignacionExito('');

    try {
      const alumnosYaUsados = new Set<number>();
      const asignaciones = plantillas
        .filter((p) => !p.asignada)
        .map((plantilla) => {
          const alumnosDelGrado = getAlumnosDelGrado(plantilla.gradoId);
          const alumnoNoAsignado = alumnosDelGrado.find((a) => {
            return !alumnosYaUsados.has(a.id) &&
                   !plantillas.some((p) => p.alumnoId === a.id && p.asignada);
          });
          if (!alumnoNoAsignado) return null;
          alumnosYaUsados.add(alumnoNoAsignado.id);
          return { plantillaId: plantilla.id, alumnoId: alumnoNoAsignado.id };
        })
        .filter((a) => a !== null) as { plantillaId: string; alumnoId: number }[];

      if (asignaciones.length === 0) {
        setError('No hay exámenes sin asignar o alumnos disponibles');
        setLoading(false);
        return;
      }

      const result = await examenesService.asignarTodos(asignaciones);
      setAsignacionExito(`${result.totalAsignadas} exámenes asignados correctamente`);

      const plantillasActualizadas = await examenesService.obtenerPlantillas();
      setPlantillas(plantillasActualizadas);

      const todasAsignadas = plantillasActualizadas.every((p) => p.asignada);
      setTodosAsignados(todasAsignadas);
    } catch (err: any) {
      setError(err.response?.data?.error || 'Error al asignar los exámenes');
    } finally {
      setLoading(false);
    }
  };

  const handleDescargarPdfs = async () => {
    setLoading(true);
    setError('');

    try {
      const blob = await examenesService.descargarPdfLote();
      const url = window.URL.createObjectURL(blob);
      const link = document.createElement('a');
      link.href = url;
      link.download = `examenes_${new Date().toISOString().split('T')[0]}.zip`;
      document.body.appendChild(link);
      link.click();
      document.body.removeChild(link);
      window.URL.revokeObjectURL(url);
    } catch (err: any) {
      setError(err.response?.data?.error || 'Error al descargar los PDFs');
    } finally {
      setLoading(false);
    }
  };

  const handleCancelar = async () => {
    if (window.confirm('¿Está seguro de que desea cancelar la asignación de exámenes?')) {
      try {
        setLoading(true);
        await examenesService.cancelarAsignacion();
        navigate('/menu-docente');
      } catch (err: any) {
        setError(err.response?.data?.error || 'Error al cancelar la asignación');
      } finally {
        setLoading(false);
      }
    }
  };

  const handleCompletarYVolver = () => {
    navigate('/menu-docente');
  };

  if (loadingData) {
    return (
      <div className="container mt-4 text-center">
        <div className="spinner-border text-primary" role="status">
          <span className="visually-hidden">Cargando...</span>
        </div>
        <p className="mt-2">Cargando datos...</p>
      </div>
    );
  }

  const examenesSinAsignar = plantillas.filter((p) => !p.asignada).length;
  const examenesAsignados = plantillas.filter((p) => p.asignada).length;

  const selectedPlantillaData = plantillas.find((p) => p.id === selectedPlantilla);
  const alumnosDisponibles = getAlumnosDelGrado(selectedPlantillaData?.gradoId);

  return (
    <div className="container mt-4">
      <div className="row justify-content-center">
        <div className="col-md-12">
          <div className="card">
            <div className="card-header bg-warning text-dark">
              <h4>Asignar Exámenes</h4>
            </div>
            <div className="card-body">
              {error && <div className="alert alert-danger">{error}</div>}
              {asignacionExito && (
                <div className="alert alert-success">{asignacionExito}</div>
              )}

              <div className="mb-4">
                <h5>Resumen</h5>
                <div className="row">
                  <div className="col-md-4">
                    <div className="card bg-light">
                      <div className="card-body">
                        <h6>Total Exámenes</h6>
                        <p className="h3">{plantillas.length}</p>
                      </div>
                    </div>
                  </div>
                  <div className="col-md-4">
                    <div className="card bg-success text-white">
                      <div className="card-body">
                        <h6>Asignados</h6>
                        <p className="h3">{examenesAsignados}</p>
                      </div>
                    </div>
                  </div>
                  <div className="col-md-4">
                    <div className="card bg-warning">
                      <div className="card-body">
                        <h6>Sin Asignar</h6>
                        <p className="h3">{examenesSinAsignar}</p>
                      </div>
                    </div>
                  </div>
                </div>
              </div>

              <div className="mb-4">
                <h5>Exámenes Generados</h5>
                <div className="table-responsive">
                  <table className="table table-striped table-hover">
                    <thead>
                      <tr>
                        <th>Asignatura</th>
                        <th>Evaluación</th>
                        <th>Grado</th>
                        <th>Preguntas</th>
                        <th>Estado</th>
                        <th>Alumno Asignado</th>
                      </tr>
                    </thead>
                    <tbody>
                      {plantillas.map((plantilla) => (
                        <tr
                          key={plantilla.id}
                          className={
                            selectedPlantilla === plantilla.id
                              ? 'table-primary'
                              : ''
                          }
                          onClick={() => handleSelectPlantilla(plantilla.id)}
                          style={{ cursor: 'pointer' }}
                        >
                          <td>{plantilla.tituloAsignatura}</td>
                          <td>{plantilla.evaluacion.replace('_', ' ')}</td>
                          <td>
                            <span className="badge bg-info">
                              {gradoNombreMap[plantilla.gradoId] || `Grado #${plantilla.gradoId}`}
                            </span>
                          </td>
                          <td>{plantilla.numPreguntas}</td>
                          <td>
                            {plantilla.asignada ? (
                              <span className="badge bg-success">Asignado</span>
                            ) : (
                              <span className="badge bg-warning text-dark">
                                Pendiente
                              </span>
                            )}
                          </td>
                          <td>
                            {plantilla.asignada && plantilla.alumnoId
                              ? alumnoNombreMap[plantilla.alumnoId] ||
                                `Alumno #${plantilla.alumnoId}`
                              : '-'}
                          </td>
                        </tr>
                      ))}
                    </tbody>
                  </table>
                </div>
              </div>

              {examenesSinAsignar > 0 && (
                <div className="mb-4">
                  <h5>Asignación Manual</h5>
                  <div className="row">
                    <div className="col-md-6">
                      <label className="form-label">
                        {selectedPlantilla
                          ? `Alumnos del grado: ${gradoNombreMap[selectedPlantillaData?.gradoId] || ''}`
                          : 'Seleccione un examen para ver los alumnos disponibles'}
                      </label>
                      <select
                        className="form-select"
                        value={selectedAlumno || ''}
                        onChange={(e) =>
                          setSelectedAlumno(
                            e.target.value ? Number(e.target.value) : null
                          )
                        }
                        disabled={!selectedPlantilla}
                      >
                        <option value="">
                          {selectedPlantilla
                            ? '-- Seleccionar alumno --'
                            : '-- Primero seleccione un examen --'}
                        </option>
                        {alumnosDisponibles.map((alumno) => (
                          <option key={alumno.id} value={alumno.id}>
                            {alumno.nombre} {alumno.apellidos}
                          </option>
                        ))}
                      </select>
                      {selectedPlantilla && alumnosDisponibles.length === 0 && (
                        <small className="text-warning">
                          No hay alumnos disponibles de este grado
                        </small>
                      )}
                    </div>
                    <div className="col-md-6 d-flex align-items-end">
                      <button
                        className="btn btn-primary w-100"
                        onClick={handleAsignarIndividual}
                        disabled={
                          loading || !selectedPlantilla || !selectedAlumno
                        }
                      >
                        {loading
                          ? 'Asignando...'
                          : 'Asignar Examen Seleccionado'}
                      </button>
                    </div>
                  </div>
                </div>
              )}

              {examenesSinAsignar > 1 && (
                <div className="mb-4">
                  <h5>Asignación Automática</h5>
                  <p className="text-muted">
                    Cada examen se asignará automáticamente a un alumno de su grado correspondiente.
                  </p>
                  <button
                    className="btn btn-success"
                    onClick={handleAsignarTodos}
                    disabled={loading}
                  >
                    {loading
                      ? 'Asignando...'
                      : `Asignar Todos Automáticamente (${examenesSinAsignar})`}
                  </button>
                </div>
              )}

              <div className="d-flex gap-2 mt-4">
                {todosAsignados && (
                  <button
                    className="btn btn-success"
                    onClick={handleDescargarPdfs}
                    disabled={loading}
                  >
                    {loading ? 'Generando...' : 'Descargar PDFs (ZIP)'}
                  </button>
                )}
                <button
                  className="btn btn-secondary"
                  onClick={handleCancelar}
                  disabled={loading}
                >
                  Cancelar
                </button>
                {examenesAsignados > 0 && (
                  <button
                    className="btn btn-primary"
                    onClick={handleCompletarYVolver}
                    disabled={loading}
                  >
                    Completar gestión
                  </button>
                )}
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}