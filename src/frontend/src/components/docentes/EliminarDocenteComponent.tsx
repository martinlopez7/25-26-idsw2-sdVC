import { useState, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { docentesService, UsuarioDTO } from '../../services/docentesService';

export default function EliminarDocenteComponent() {
  const { id } = useParams<{ id: string }>();
  const [docente, setDocente] = useState<UsuarioDTO | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const navigate = useNavigate();

  useEffect(() => {
    if (id) {
      cargarDocente();
    }
  }, [id]);

  const cargarDocente = async () => {
    try {
      const data = await docentesService.obtenerDocentePorId(Number(id));
      setDocente(data);
    } catch (err: any) {
      setError(err.response?.data?.error || 'Error al cargar el docente');
    } finally {
      setLoading(false);
    }
  };

  const handleEliminar = async () => {
    try {
      await docentesService.eliminarDocente(Number(id));
      navigate('/docentes');
    } catch (err: any) {
      const errorMsg = err.response?.data?.error || 'No se pudo eliminar el docente';
      setError(errorMsg);
    }
  };

  const handleCancelar = () => {
    navigate('/docentes');
  };

  if (loading) {
    return <div className="container mt-4">Cargando...</div>;
  }

  if (!docente) {
    return (
      <div className="container mt-4">
        <div className="alert alert-danger">{error || 'Docente no encontrado'}</div>
        <button className="btn btn-secondary" onClick={handleCancelar}>
          Volver
        </button>
      </div>
    );
  }

  return (
    <div className="container mt-4">
      <div className="card">
        <div className="card-header bg-danger text-white">
          <h5>Confirmar Eliminación</h5>
        </div>
        <div className="card-body">
          {error && <div className="alert alert-danger mb-3">{error}</div>}

          <p>¿Está seguro de que desea eliminar al siguiente docente?</p>

          <table className="table table-bordered">
            <tbody>
              <tr>
                <th style={{ width: "30%" }}>Nombre:</th>
                <td>{docente.nombre} {docente.apellidos}</td>
              </tr>
              <tr>
                <th>DNI:</th>
                <td>{docente.dni}</td>
              </tr>
              <tr>
                <th>Username:</th>
                <td>{docente.username}</td>
              </tr>
              <tr>
                <th>Email:</th>
                <td>{docente.email}</td>
              </tr>
            </tbody>
          </table>

          <div className="alert alert-warning">
            <strong>Advertencia:</strong> Esta acción es irreversible y puede afectar a datos asociados al docente.
          </div>

          <div className="d-flex gap-2 justify-content-end">
            <button className="btn btn-secondary" onClick={handleCancelar}>
              Cancelar
            </button>
            <button className="btn btn-danger" onClick={handleEliminar}>
              Eliminar
            </button>
          </div>
        </div>
      </div>
    </div>
  );
}