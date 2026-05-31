import { useState, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { gradosService, GradoDTO, AlumnoDTO } from '../../services/gradosService';

export default function EditarGradoComponent() {
  const navigate = useNavigate();
  const { id } = useParams<{ id: string }>();
  const [grado, setGrado] = useState<GradoDTO | null>(null);
  const [titulo, setTitulo] = useState('');
  const [codigo, setCodigo] = useState('');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [showAlumnosModal, setShowAlumnosModal] = useState(false);
  const [alumnosDisponibles, setAlumnosDisponibles] = useState<AlumnoDTO[]>([]);
  const [loadingAlumnos, setLoadingAlumnos] = useState(false);

  useEffect(() => {
    if (id) {
      cargarGrado();
    }
  }, [id]);

  const cargarGrado = async () => {
    setLoading(true);
    setError('');
    try {
      const data = await gradosService.getGradoById(Number(id));
      setGrado(data);
      setTitulo(data.titulo);
      setCodigo(data.codigo);
    } catch (err: any) {
      setError(err.response?.data?.error || 'Error al cargar el grado');
    } finally {
      setLoading(false);
    }
  };

  const handleGuardar = async () => {
    if (!grado) return;
    setSaving(true);
    setError('');
    try {
      const updated = await gradosService.actualizarGrado(grado.id, { titulo, codigo });
      setGrado(updated);
      setTitulo(updated.titulo);
      setCodigo(updated.codigo);
    } catch (err: any) {
      setError(err.response?.data?.error || 'Error al guardar los cambios');
    } finally {
      setSaving(false);
    }
  };

  const handleGuardarYSalir = async () => {
    if (!grado) return;
    setSaving(true);
    setError('');
    try {
      await gradosService.actualizarGrado(grado.id, { titulo, codigo });
      navigate('/grados');
    } catch (err: any) {
      setError(err.response?.data?.error || 'Error al guardar los cambios');
      setSaving(false);
    }
  };

  const handleEliminar = () => {
    if (!grado) return;
    if (window.confirm('¿Estás seguro de eliminar este grado? Esta acción no se puede deshacer.')) {
      navigate(`/grados/eliminar/${grado.id}`);
    }
  };

  const handleCancelar = () => {
    navigate('/grados');
  };

  const handleQuitarAlumno = async (alumnoId: number) => {
    if (!grado) return;
    if (!window.confirm('¿Quitar este alumno del grado?')) return;

    try {
      await gradosService.quitarAlumnoDeGrado(grado.id, alumnoId);
      setGrado({
        ...grado,
        alumnos: grado.alumnos.filter(a => a.id !== alumnoId)
      });
    } catch (err: any) {
      setError(err.response?.data?.error || 'Error al quitar el alumno');
    }
  };

  const handleAnadirAlumno = () => {
    setShowAlumnosModal(true);
    setLoadingAlumnos(true);
    setError('');
    gradosService.getAlumnosSinGrado()
      .then(data => setAlumnosDisponibles(data))
      .catch((err: any) => setError(err.response?.data?.error || 'Error al cargar alumnos'))
      .finally(() => setLoadingAlumnos(false));
  };

  const handleSeleccionarAlumno = async (alumnoId: number) => {
    if (!grado) return;
    try {
      await gradosService.anadirAlumnoAGrado(grado.id, alumnoId);
      const nuevoAlumno = alumnosDisponibles.find(a => a.id === alumnoId);
      if (nuevoAlumno) {
        setGrado({
          ...grado,
          alumnos: [...grado.alumnos, { ...nuevoAlumno, gradoId: grado.id }]
        });
      }
      setShowAlumnosModal(false);
    } catch (err: any) {
      setError(err.response?.data?.error || 'Error al añadir el alumno');
    }
  };

  if (loading) {
    return (
      <div className="container mt-4">
        <div className="text-center">Cargando...</div>
      </div>
    );
  }

  if (!grado) {
    return (
      <div className="container mt-4">
        <div className="alert alert-danger">{error || 'Grado no encontrado'}</div>
      </div>
    );
  }

  return (
    <div className="container mt-4">
      <div className="row justify-content-center">
        <div className="col-md-8">
          <div className="card">
            <div className="card-header d-flex justify-content-between align-items-center">
              <h4>Editar Grado</h4>
              <button className="btn btn-sm btn-outline-secondary" onClick={handleCancelar}>
                Volver a lista
              </button>
            </div>
            <div className="card-body">
              {error && <div className="alert alert-danger">{error}</div>}

              <form onSubmit={(e) => { e.preventDefault(); handleGuardar(); }}>
                <div className="mb-3">
                  <label htmlFor="titulo" className="form-label">Título</label>
                  <input
                    type="text"
                    className="form-control"
                    id="titulo"
                    value={titulo}
                    onChange={(e) => setTitulo(e.target.value)}
                    required
                    minLength={3}
                    maxLength={200}
                  />
                </div>

                <div className="mb-3">
                  <label htmlFor="codigo" className="form-label">Código</label>
                  <input
                    type="text"
                    className="form-control"
                    id="codigo"
                    value={codigo}
                    onChange={(e) => setCodigo(e.target.value)}
                    required
                    minLength={2}
                    maxLength={50}
                  />
                </div>

                <div className="d-flex gap-2 mb-4">
                  <button type="submit" className="btn btn-primary" disabled={saving}>
                    {saving ? 'Guardando...' : 'Guardar cambios'}
                  </button>
                  <button type="button" className="btn btn-success" onClick={handleGuardarYSalir} disabled={saving}>
                    Guardar y salir
                  </button>
                  <button type="button" className="btn btn-secondary" onClick={handleCancelar} disabled={saving}>
                    Cancelar
                  </button>
                  <button type="button" className="btn btn-danger" onClick={handleEliminar}>
                    Eliminar
                  </button>
                </div>
              </form>

              <hr />

              <div className="mt-4">
                <div className="d-flex justify-content-between align-items-center mb-3">
                  <h5>Alumnos del grado ({grado.alumnos.length})</h5>
                  <button className="btn btn-sm btn-success" onClick={handleAnadirAlumno}>
                    + Añadir alumno
                  </button>
                </div>

                {grado.alumnos.length === 0 ? (
                  <div className="alert alert-info">No hay alumnos en este grado</div>
                ) : (
                  <table className="table table-striped">
                    <thead>
                      <tr>
                        <th>DNI</th>
                        <th>Nombre</th>
                        <th>Apellidos</th>
                        <th>Acciones</th>
                      </tr>
                    </thead>
                    <tbody>
                      {grado.alumnos.map((alumno) => (
                        <tr key={alumno.id}>
                          <td>{alumno.dni}</td>
                          <td>{alumno.nombre}</td>
                          <td>{alumno.apellidos}</td>
                          <td>
                            <button
                              className="btn btn-sm btn-danger"
                              onClick={() => handleQuitarAlumno(alumno.id)}
                            >
                              Quitar
                            </button>
                          </td>
                        </tr>
                      ))}
                    </tbody>
                  </table>
                )}
              </div>
            </div>
          </div>
        </div>
      </div>

      {showAlumnosModal && (
        <div className="modal show d-block" tabIndex={-1} style={{ backgroundColor: 'rgba(0,0,0,0.5)' }}>
          <div className="modal-dialog">
            <div className="modal-content">
              <div className="modal-header">
                <h5 className="modal-title">Añadir alumno al grado</h5>
                <button type="button" className="btn-close" onClick={() => setShowAlumnosModal(false)}></button>
              </div>
              <div className="modal-body">
                {loadingAlumnos ? (
                  <div className="text-center">Cargando...</div>
                ) : alumnosDisponibles.length === 0 ? (
                  <div className="alert alert-info">No hay alumnos disponibles sin grado</div>
                ) : (
                  <table className="table table-striped">
                    <thead>
                      <tr>
                        <th>DNI</th>
                        <th>Nombre</th>
                        <th>Apellidos</th>
                        <th>Acciones</th>
                      </tr>
                    </thead>
                    <tbody>
                      {alumnosDisponibles.map((alumno) => (
                        <tr key={alumno.id}>
                          <td>{alumno.dni}</td>
                          <td>{alumno.nombre}</td>
                          <td>{alumno.apellidos}</td>
                          <td>
                            <button
                              className="btn btn-sm btn-primary"
                              onClick={() => handleSeleccionarAlumno(alumno.id)}
                            >
                              Añadir
                            </button>
                          </td>
                        </tr>
                      ))}
                    </tbody>
                  </table>
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