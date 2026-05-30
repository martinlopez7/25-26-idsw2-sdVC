import { useState, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { alumnosService, AlumnoDTO } from '../../services/alumnosService';

export default function EliminarAlumnoComponent() {
  const { id } = useParams<{ id: string }>();
  const [alumno, setAlumno] = useState<AlumnoDTO | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const navigate = useNavigate();

  useEffect(() => {
    if (id) {
      cargarAlumno();
    }
  }, [id]);

  const cargarAlumno = async () => {
    try {
      const data = await alumnosService.getAlumnoById(Number(id));
      setAlumno(data);
    } catch (err: any) {
      setError(err.response?.data?.error || 'Error al cargar el alumno');
    } finally {
      setLoading(false);
    }
  };

  const handleEliminar = async () => {
    try {
      await alumnosService.eliminarAlumno(Number(id));
      navigate('/alumnos');
    } catch (err: any) {
      const errorMsg = err.response?.data?.error || 'No se pudo eliminar el alumno';
      setError(errorMsg);
    }
  };

  const handleCancelar = () => {
    navigate('/alumnos');
  };

  if (loading) {
    return <div className="container mt-4">Cargando...</div>;
  }

  if (!alumno) {
    return (
      <div className="container mt-4">
        <div className="alert alert-danger">{error || 'Alumno no encontrado'}</div>
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

          <p>¿Está seguro de que desea eliminar al siguiente alumno?</p>

          <table className="table table-bordered">
            <tbody>
              <tr>
                <th style={{ width: "30%" }}>DNI:</th>
                <td>{alumno.dni}</td>
              </tr>
              <tr>
                <th>Nombre:</th>
                <td>{alumno.nombre}</td>
              </tr>
              <tr>
                <th>Apellidos:</th>
                <td>{alumno.apellidos}</td>
              </tr>
            </tbody>
          </table>

          <div className="alert alert-warning">
            <strong>Advertencia:</strong> Esta acción es irreversible.
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