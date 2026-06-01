import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { asignaturasService, AsignaturaDTO } from '../../services/asignaturasService';

export default function AsignaturasListComponent() {
  const [asignaturas, setAsignaturas] = useState<AsignaturaDTO[]>([]);
  const [filtro, setFiltro] = useState('');
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string>('');
  const navigate = useNavigate();

  useEffect(() => {
    cargarAsignaturas();
  }, []);

  const cargarAsignaturas = async (criterio?: string) => {
    setLoading(true);
    setError('');
    try {
      const data = await asignaturasService.getAsignaturas(criterio);
      setAsignaturas(data);
    } catch (err: any) {
      setError(err.response?.data?.error || 'Error al cargar asignaturas');
    } finally {
      setLoading(false);
    }
  };

  const handleFiltrar = (e: React.FormEvent) => {
    e.preventDefault();
    cargarAsignaturas(filtro);
  };

  const handleLimpiarFiltro = () => {
    setFiltro('');
    cargarAsignaturas();
  };

  return (
    <div className="container mt-4">
      <div className="d-flex justify-content-between align-items-center mb-4">
        <h1>Asignaturas</h1>
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
              placeholder="Buscar por título, código, curso académico..."
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
            <button className="btn btn-success me-2" onClick={() => navigate('/asignaturas/crear')}>
              Crear Asignatura
            </button>
          </div>

          {asignaturas.length === 0 ? (
            <div className="alert alert-info">No hay asignaturas registradas</div>
          ) : (
            <table className="table table-striped">
              <thead>
                <tr>
                  <th>Código</th>
                  <th>Título</th>
                  <th>Curso Académico</th>
                  <th>Acciones</th>
                </tr>
              </thead>
              <tbody>
                {asignaturas.map((asignatura) => (
                  <tr key={asignatura.id}>
                    <td>{asignatura.codigo}</td>
                    <td>{asignatura.titulo}</td>
                    <td>{asignatura.cursoAcademico}</td>
                    <td>
                      <button
                        className="btn btn-sm btn-primary me-2"
                        onClick={() => navigate(`/asignaturas/editar/${asignatura.id}`)}
                      >
                        Editar
                      </button>
                      <button
                        className="btn btn-sm btn-danger"
                        onClick={() => navigate(`/asignaturas/eliminar/${asignatura.id}`)}
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