import { useState, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { asignaturasService, AsignaturaDTO } from '../../services/asignaturasService';

export default function EliminarAsignaturaComponent() {
  const { id } = useParams<{ id: string }>();
  const [asignatura, setAsignatura] = useState<AsignaturaDTO | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const navigate = useNavigate();

  useEffect(() => {
    if (id) {
      cargarAsignatura();
    }
  }, [id]);

  const cargarAsignatura = async () => {
    try {
      const data = await asignaturasService.getAsignaturaById(Number(id));
      setAsignatura(data);
    } catch (err: any) {
      setError(err.response?.data?.error || 'Error al cargar la asignatura');
    } finally {
      setLoading(false);
    }
  };

  const handleEliminar = async () => {
    try {
      await asignaturasService.eliminarAsignatura(Number(id));
      navigate('/asignaturas');
    } catch (err: any) {
      const errorMsg = err.response?.data?.error || 'No se pudo eliminar la asignatura';
      setError(errorMsg);
    }
  };

  const handleCancelar = () => {
    navigate('/asignaturas');
  };

  if (loading) {
    return <div className="container mt-4">Cargando...</div>;
  }

  if (!asignatura) {
    return (
      <div className="container mt-4">
        <div className="alert alert-danger">{error || 'Asignatura no encontrada'}</div>
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

          <p>¿Está seguro de que desea eliminar la siguiente asignatura?</p>

          <table className="table table-bordered">
            <tbody>
              <tr>
                <th style={{ width: '30%' }}>Código:</th>
                <td>{asignatura.codigo}</td>
              </tr>
              <tr>
                <th>Título:</th>
                <td>{asignatura.titulo}</td>
              </tr>
              <tr>
                <th>Curso Académico:</th>
                <td>{asignatura.cursoAcademico}</td>
              </tr>
            </tbody>
          </table>

          <div className="alert alert-warning">
            <strong>Advertencia:</strong> Perderás las preguntas asociadas y los alumnos y grados asociados perderán su relación con esta asignatura.
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