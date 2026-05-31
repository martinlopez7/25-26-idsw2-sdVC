import { useState, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { gradosService, GradoDTO } from '../../services/gradosService';

export default function EliminarGradoComponent() {
  const { id } = useParams<{ id: string }>();
  const [grado, setGrado] = useState<GradoDTO | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const navigate = useNavigate();

  useEffect(() => {
    if (id) {
      cargarGrado();
    }
  }, [id]);

  const cargarGrado = async () => {
    try {
      const data = await gradosService.getGradoById(Number(id));
      setGrado(data);
    } catch (err: any) {
      setError(err.response?.data?.error || 'Error al cargar el grado');
    } finally {
      setLoading(false);
    }
  };

  const handleEliminar = async () => {
    try {
      await gradosService.eliminarGrado(Number(id));
      navigate('/grados');
    } catch (err: any) {
      const errorMsg = err.response?.data?.error || 'No se pudo eliminar el grado';
      setError(errorMsg);
    }
  };

  const handleCancelar = () => {
    navigate('/grados');
  };

  if (loading) {
    return <div className="container mt-4">Cargando...</div>;
  }

  if (!grado) {
    return (
      <div className="container mt-4">
        <div className="alert alert-danger">{error || 'Grado no encontrado'}</div>
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

          <p>¿Está seguro de que desea eliminar el siguiente grado?</p>

          <table className="table table-bordered">
            <tbody>
              <tr>
                <th style={{ width: '30%' }}>Código:</th>
                <td>{grado.codigo}</td>
              </tr>
              <tr>
                <th>Título:</th>
                <td>{grado.titulo}</td>
              </tr>
              {grado.alumnos && grado.alumnos.length > 0 && (
                <tr>
                  <th>Alumnos enlistados:</th>
                  <td>
                    <ul className="mb-0">
                      {grado.alumnos.map((alumno) => (
                        <li key={alumno.id}>
                          {alumno.nombre} {alumno.apellidos} ({alumno.dni})
                        </li>
                      ))}
                    </ul>
                  </td>
                </tr>
              )}
            </tbody>
          </table>

          <div className="alert alert-warning">
            <strong>Advertencia:</strong> Esta acción es irreversible. Los alumnos y asignaturas asociados perderán su referencia a este grado.
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
