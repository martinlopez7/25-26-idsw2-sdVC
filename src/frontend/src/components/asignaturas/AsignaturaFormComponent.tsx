import { useState, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { asignaturasService, AsignaturaDTO, AlumnoDTO, GradoDTO } from '../../services/asignaturasService';
import { gradosService } from '../../services/gradosService';

export default function AsignaturaFormComponent() {
  const navigate = useNavigate();
  const { id } = useParams<{ id: string }>();
  const isEditing = Boolean(id);

  const [loading, setLoading] = useState(false);
  const [loadingData, setLoadingData] = useState(isEditing);
  const [error, setError] = useState('');
  const [successMessage, setSuccessMessage] = useState('');

  const [asignaturaData, setAsignaturaData] = useState<AsignaturaDTO | null>(null);
  const [formData, setFormData] = useState({
    titulo: '',
    codigo: '',
    cursoAcademico: '',
  });
  const [selectedGradoIds, setSelectedGradoIds] = useState<number[]>([]);
  const [validationErrors, setValidationErrors] = useState<Record<string, string>>({});

  const [showAlumnosModal, setShowAlumnosModal] = useState(false);
  const [alumnosDisponibles, setAlumnosDisponibles] = useState<AlumnoDTO[]>([]);
  const [loadingAlumnos, setLoadingAlumnos] = useState(false);

  const [allGrados, setAllGrados] = useState<GradoDTO[]>([]);
  const [loadingGrados, setLoadingGrados] = useState(false);

  useEffect(() => {
    if (isEditing && id) {
      cargarAsignatura(parseInt(id));
    }
    cargarGrados();
  }, [id]);

  const cargarAsignatura = async (asignaturaId: number) => {
    try {
      const data = await asignaturasService.getAsignaturaById(asignaturaId);
      setAsignaturaData(data);
      setFormData({
        titulo: data.titulo,
        codigo: data.codigo,
        cursoAcademico: data.cursoAcademico,
      });
      setSelectedGradoIds(data.grados.map(g => g.id));
    } catch (err: any) {
      setError(err.response?.data?.error || 'Error al cargar la asignatura');
    } finally {
      setLoadingData(false);
    }
  };

  const cargarGrados = async () => {
    setLoadingGrados(true);
    try {
      const grados = await gradosService.getGrados();
      setAllGrados(grados);
    } catch (err: any) {
      console.error('Error al cargar grados:', err);
    } finally {
      setLoadingGrados(false);
    }
  };

  const validate = (): boolean => {
    const errors: Record<string, string> = {};

    if (!formData.titulo.trim()) {
      errors.titulo = 'El título es obligatorio';
    } else if (formData.titulo.length < 2 || formData.titulo.length > 100) {
      errors.titulo = 'El título debe tener entre 2 y 100 caracteres';
    }

    if (!formData.codigo.trim()) {
      errors.codigo = 'El código es obligatorio';
    } else if (formData.codigo.length < 2 || formData.codigo.length > 20) {
      errors.codigo = 'El código debe tener entre 2 y 20 caracteres';
    }

    if (!formData.cursoAcademico.trim()) {
      errors.cursoAcademico = 'El curso académico es obligatorio';
    } else if (formData.cursoAcademico.length < 2 || formData.cursoAcademico.length > 50) {
      errors.cursoAcademico = 'El curso académico debe tener entre 2 y 50 caracteres';
    }

    setValidationErrors(errors);
    return Object.keys(errors).length === 0;
  };

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setFormData(prev => ({ ...prev, [name]: value }));
    if (validationErrors[name]) {
      setValidationErrors(prev => ({ ...prev, [name]: '' }));
    }
  };

  const handleGradoToggle = (gradoId: number) => {
    setSelectedGradoIds(prev =>
      prev.includes(gradoId)
        ? prev.filter(id => id !== gradoId)
        : [...prev, gradoId]
    );
  };

  const handleRemoveAlumno = async (alumnoId: number) => {
    if (!id) return;

    try {
      const updated = await asignaturasService.desmatricularAlumno(parseInt(id), alumnoId);
      setAsignaturaData(updated);
      setSuccessMessage('Alumno desmatriculado correctamente');
      setTimeout(() => setSuccessMessage(''), 3000);
    } catch (err: any) {
      setError(err.response?.data?.error || 'Error al desmatricular alumno');
    }
  };

  const handleOpenAlumnosModal = async () => {
    if (!id) return;

    setShowAlumnosModal(true);
    setLoadingAlumnos(true);
    setError('');

    try {
      const disponibles = await asignaturasService.getAlumnosDisponibles(parseInt(id));
      setAlumnosDisponibles(disponibles);
    } catch (err: any) {
      setError(err.response?.data?.error || 'Error al cargar alumnos disponibles');
    } finally {
      setLoadingAlumnos(false);
    }
  };

  const handleMatricularAlumno = async (alumnoId: number) => {
    if (!id) return;

    try {
      const updated = await asignaturasService.matricularAlumno(parseInt(id), alumnoId);
      setAsignaturaData(updated);
      setAlumnosDisponibles(prev => prev.filter(a => a.id !== alumnoId));
      setSuccessMessage('Alumno matriculado correctamente');
      setTimeout(() => setSuccessMessage(''), 3000);
    } catch (err: any) {
      setError(err.response?.data?.error || 'Error al matricular alumno');
    }
  };

  const handleSave = async () => {
    if (!validate()) return;

    setLoading(true);
    setError('');

    try {
      if (isEditing && id) {
        await asignaturasService.actualizarAsignatura(parseInt(id), {
          ...formData,
          gradoIds: selectedGradoIds,
        });
        setSuccessMessage('Asignatura actualizada correctamente');
        setTimeout(() => {
          navigate('/asignaturas');
        }, 1500);
      } else {
        await asignaturasService.crearAsignatura(formData);
        navigate('/asignaturas');
      }
    } catch (err: any) {
      setError(err.response?.data?.error || 'Error al guardar la asignatura');
    } finally {
      setLoading(false);
    }
  };

  const handleCancel = () => {
    navigate('/asignaturas');
  };

  if (loadingData) {
    return <div className="container mt-4"><div className="text-center">Cargando...</div></div>;
  }

  return (
    <div className="container mt-4">
      <div className="row justify-content-center">
        <div className="col-md-8">
          <div className="card">
            <div className="card-header">
              <h4>{isEditing ? 'Editar Asignatura' : 'Nueva Asignatura'}</h4>
            </div>
            <div className="card-body">
              {error && <div className="alert alert-danger">{error}</div>}
              {successMessage && <div className="alert alert-success">{successMessage}</div>}

              <form onSubmit={(e) => { e.preventDefault(); handleSave(); }}>
                <div className="mb-3">
                  <label htmlFor="titulo" className="form-label">Título *</label>
                  <input
                    type="text"
                    className={`form-control ${validationErrors.titulo ? 'is-invalid' : ''}`}
                    id="titulo"
                    name="titulo"
                    value={formData.titulo}
                    onChange={handleChange}
                    placeholder="Ej: Introducción a la Programación"
                  />
                  {validationErrors.titulo && (
                    <div className="invalid-feedback">{validationErrors.titulo}</div>
                  )}
                </div>

                <div className="mb-3">
                  <label htmlFor="codigo" className="form-label">Código *</label>
                  <input
                    type="text"
                    className={`form-control ${validationErrors.codigo ? 'is-invalid' : ''}`}
                    id="codigo"
                    name="codigo"
                    value={formData.codigo}
                    onChange={handleChange}
                    placeholder="Ej: IP-101"
                  />
                  {validationErrors.codigo && (
                    <div className="invalid-feedback">{validationErrors.codigo}</div>
                  )}
                </div>

                <div className="mb-3">
                  <label htmlFor="cursoAcademico" className="form-label">Curso Académico *</label>
                  <input
                    type="text"
                    className={`form-control ${validationErrors.cursoAcademico ? 'is-invalid' : ''}`}
                    id="cursoAcademico"
                    name="cursoAcademico"
                    value={formData.cursoAcademico}
                    onChange={handleChange}
                    placeholder="Ej: 2025-2026"
                  />
                  {validationErrors.cursoAcademico && (
                    <div className="invalid-feedback">{validationErrors.cursoAcademico}</div>
                  )}
                </div>

                {isEditing && (
                  <>
                    <hr />
                    <h5>Grados Asociados</h5>
                    <div className="mb-3">
                      {loadingGrados ? (
                        <p>Cargando grados...</p>
                      ) : (
                        <div className="d-flex flex-wrap gap-2">
                          {allGrados.map(grado => (
                            <div
                              key={grado.id}
                              className={`badge ${selectedGradoIds.includes(grado.id) ? 'bg-primary' : 'bg-secondary'} p-2`}
                              style={{ cursor: 'pointer', fontSize: '0.9rem' }}
                              onClick={() => handleGradoToggle(grado.id)}
                            >
                              {selectedGradoIds.includes(grado.id) ? '✓ ' : ''}{grado.titulo} ({grado.codigo})
                            </div>
                          ))}
                        </div>
                      )}
                      {selectedGradoIds.length === 0 && (
                        <small className="text-muted">Ningun grado asociado. Los alumnos no podrín ser matriculados.</small>
                      )}
                    </div>

                    <hr />
                    <div className="d-flex justify-content-between align-items-center mb-3">
                      <h5 className="mb-0">Alumnos Matriculados ({asignaturaData?.alumnos.length || 0})</h5>
                      <button
                        type="button"
                        className="btn btn-sm btn-success"
                        onClick={handleOpenAlumnosModal}
                      >
                        + Matricular Alumno
                      </button>
                    </div>
                    {asignaturaData?.alumnos && asignaturaData.alumnos.length > 0 ? (
                      <ul className="list-group mb-3">
                        {asignaturaData.alumnos.map(alumno => (
                          <li key={alumno.id} className="list-group-item d-flex justify-content-between align-items-center">
                            <span>{alumno.nombre} {alumno.apellidos} ({alumno.dni})</span>
                            <button
                              type="button"
                              className="btn btn-sm btn-outline-danger"
                              onClick={() => handleRemoveAlumno(alumno.id)}
                            >
                              Quitar
                            </button>
                          </li>
                        ))}
                      </ul>
                    ) : (
                      <p className="text-muted">No hay alumnos matriculados en esta asignatura</p>
                    )}
                  </>
                )}

                <hr />
                <div className="d-flex justify-content-between">
                  <button type="button" className="btn btn-secondary" onClick={handleCancel}>
                    Cancelar
                  </button>
                  <button type="submit" className="btn btn-primary" disabled={loading}>
                    {loading ? 'Guardando...' : 'Guardar'}
                  </button>
                </div>
              </form>
            </div>
          </div>
        </div>
      </div>

      {showAlumnosModal && (
        <div className="modal show d-block" tabIndex={-1} style={{ backgroundColor: 'rgba(0,0,0,0.5)' }}>
          <div className="modal-dialog">
            <div className="modal-content">
              <div className="modal-header">
                <h5 className="modal-title">Matricular Alumno</h5>
                <button type="button" className="btn-close" onClick={() => setShowAlumnosModal(false)}></button>
              </div>
              <div className="modal-body">
                {loadingAlumnos ? (
                  <p>Cargando alumnos disponibles...</p>
                ) : alumnosDisponibles.length > 0 ? (
                  <ul className="list-group">
                    {alumnosDisponibles.map(alumno => (
                      <li key={alumno.id} className="list-group-item d-flex justify-content-between align-items-center">
                        <span>{alumno.nombre} {alumno.apellidos} ({alumno.dni})</span>
                        <button
                          type="button"
                          className="btn btn-sm btn-primary"
                          onClick={() => handleMatricularAlumno(alumno.id)}
                        >
                          Matricular
                        </button>
                      </li>
                    ))}
                  </ul>
                ) : (
                  <p className="text-muted">No hay alumnos disponibles para matricular. Los alumnos deben pertenecer a un grado de la asignatura y no estar ya matriculados.</p>
                )}
              </div>
              <div className="modal-footer">
                <button type="button" className="btn btn-secondary" onClick={() => setShowAlumnosModal(false)}>
                  Cerrar
                </button>
              </div>
            </div>
          </div>
        </div>
      )}
    </div>
  );
}