import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { alumnosService, AlumnoDTO } from '../../services/alumnosService';

export default function AlumnosListComponent() {
  const [alumnos, setAlumnos] = useState<AlumnoDTO[]>([]);
  const [filtro, setFiltro] = useState('');
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string>('');
  const navigate = useNavigate();

  useEffect(() => {
    cargarAlumnos();
  }, []);

  const cargarAlumnos = async (criterio?: string) => {
    setLoading(true);
    setError('');
    try {
      const data = await alumnosService.getAlumnos(criterio);
      setAlumnos(data);
    } catch (err: any) {
      setError(err.response?.data?.error || 'Error al cargar alumnos');
    } finally {
      setLoading(false);
    }
  };

  const handleFiltrar = (e: React.FormEvent) => {
    e.preventDefault();
    cargarAlumnos(filtro);
  };

  const handleLimpiarFiltro = () => {
    setFiltro('');
    cargarAlumnos();
  };

  return (
    <div className="container mt-4">
      <div className="d-flex justify-content-between align-items-center mb-4">
        <h1>Alumnos</h1>
        <button className="btn btn-secondary" onClick={() => navigate('/menu-docente')}>
          Completar Gestión
        </button>
      </div>

      <div className="card mb-4">
        <div className="card-body">
          <form onSubmit={handleFiltrar} className="d-flex gap-2">
            <input
              type="text"
              className="form-control"
              placeholder="Buscar por nombre, apellido, DNI..."
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
            <button className="btn btn-success me-2" onClick={() => navigate('/alumnos/crear')}>
              Crear Alumno
            </button>
          </div>

          {alumnos.length === 0 ? (
            <div className="alert alert-info">No hay alumnos registrados</div>
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
                {alumnos.map((alumno) => (
                  <tr key={alumno.id}>
                    <td>{alumno.dni}</td>
                    <td>{alumno.nombre}</td>
                    <td>{alumno.apellidos}</td>
                    <td>
                      <button
                        className="btn btn-sm btn-primary me-2"
                        onClick={() => navigate(`/alumnos/editar/${alumno.id}`)}
                      >
                        Editar
                      </button>
                      <button
                        className="btn btn-sm btn-danger"
                        onClick={() => navigate(`/alumnos/eliminar/${alumno.id}`)}
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