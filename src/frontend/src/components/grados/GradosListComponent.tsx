import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { gradosService, GradoDTO } from '../../services/gradosService';

export default function GradosListComponent() {
  const [grados, setGrados] = useState<GradoDTO[]>([]);
  const [filtro, setFiltro] = useState('');
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string>('');
  const navigate = useNavigate();

  useEffect(() => {
    cargarGrados();
  }, []);

  const cargarGrados = async (criterio?: string) => {
    setLoading(true);
    setError('');
    try {
      const data = await gradosService.getGrados(criterio);
      setGrados(data);
    } catch (err: any) {
      setError(err.response?.data?.error || 'Error al cargar grados');
    } finally {
      setLoading(false);
    }
  };

  const handleFiltrar = (e: React.FormEvent) => {
    e.preventDefault();
    cargarGrados(filtro);
  };

  const handleLimpiarFiltro = () => {
    setFiltro('');
    cargarGrados();
  };

  return (
    <div className="container mt-4">
      <div className="d-flex justify-content-between align-items-center mb-4">
        <h1>Grados</h1>
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
              placeholder="Buscar por título o código..."
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
            <button className="btn btn-success me-2" onClick={() => navigate('/grados/crear')}>
              Crear Grado
            </button>
          </div>

          {grados.length === 0 ? (
            <div className="alert alert-info">No hay grados registrados</div>
          ) : (
            <table className="table table-striped">
              <thead>
                <tr>
                  <th>Código</th>
                  <th>Título</th>
                  <th>Acciones</th>
                </tr>
              </thead>
              <tbody>
                {grados.map((grado) => (
                  <tr key={grado.id}>
                    <td>{grado.codigo}</td>
                    <td>{grado.titulo}</td>
                    <td>
                      <button
                        className="btn btn-sm btn-primary me-2"
                        onClick={() => navigate(`/grados/editar/${grado.id}`)}
                      >
                        Editar
                      </button>
                      <button
                        className="btn btn-sm btn-danger"
                        onClick={() => navigate(`/grados/eliminar/${grado.id}`)}
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