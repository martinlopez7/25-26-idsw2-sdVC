import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { docentesService, UsuarioDTO } from '../../services/docentesService';

export default function DocentesListComponent() {
  const [docentes, setDocentes] = useState<UsuarioDTO[]>([]);
  const [filtro, setFiltro] = useState('');
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string>('');
  const navigate = useNavigate();

  useEffect(() => {
    cargarDocentes();
  }, []);

  const cargarDocentes = async (criterio?: string) => {
    setLoading(true);
    setError('');
    try {
      const data = await docentesService.getDocentes(criterio);
      setDocentes(data);
    } catch (err: any) {
      setError(err.response?.data?.error || 'Error al cargar docentes');
    } finally {
      setLoading(false);
    }
  };

  const handleFiltrar = (e: React.FormEvent) => {
    e.preventDefault();
    cargarDocentes(filtro);
  };

  const handleLimpiarFiltro = () => {
    setFiltro('');
    cargarDocentes();
  };

  return (
    <div className="container mt-4">
      <div className="d-flex justify-content-between align-items-center mb-4">
        <h1>Docentes</h1>
        <button className="btn btn-secondary" onClick={() => navigate('/menu-admin')}>
          Completar Gestión
        </button>
      </div>

      <div className="card mb-4">
        <div className="card-body">
          <form onSubmit={handleFiltrar} className="d-flex gap-2">
            <input
              type="text"
              className="form-control"
              placeholder="Buscar por username..."
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
            <button className="btn btn-success me-2" onClick={() => navigate('/docentes/crear')}>
              Crear Docente
            </button>
          </div>

          {docentes.length === 0 ? (
            <div className="alert alert-info">No hay docentes registrados</div>
          ) : (
            <table className="table table-striped">
              <thead>
                <tr>
                  <th>Username</th>
                  <th>Tipo Actor</th>
                  <th>Acciones</th>
                </tr>
              </thead>
              <tbody>
                {docentes.map((docente) => (
                  <tr key={docente.id}>
                    <td>{docente.username}</td>
                    <td>{docente.tipoActor}</td>
                    <td>
                      <button
                        className="btn btn-sm btn-primary me-2"
                        onClick={() => navigate(`/docentes/editar/${docente.id}`)}
                      >
                        Editar
                      </button>
                      <button
                        className="btn btn-sm btn-danger"
                        onClick={() => navigate(`/docentes/eliminar/${docente.id}`)}
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