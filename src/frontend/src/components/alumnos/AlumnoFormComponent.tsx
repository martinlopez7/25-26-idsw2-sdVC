import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { alumnosService, AlumnoDTO } from '../../services/alumnosService';

interface AlumnoFormProps {
  alumno?: AlumnoDTO;
  isEditing?: boolean;
}

export default function AlumnoFormComponent({ alumno, isEditing = false }: AlumnoFormProps) {
  const navigate = useNavigate();
  const [nombre, setNombre] = useState(alumno?.nombre || '');
  const [apellidos, setApellidos] = useState(alumno?.apellidos || '');
  const [dni, setDni] = useState(alumno?.dni || '');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError('');
    setLoading(true);

    try {
      if (isEditing && alumno?.id) {
        await alumnosService.actualizarAlumno(alumno.id, { nombre, apellidos, dni });
        navigate(`/alumnos/editar/${alumno.id}`);
      } else {
        const created = await alumnosService.crearAlumno({ nombre, apellidos, dni });
        navigate(`/alumnos/editar/${created.id}`);
      }
    } catch (err: any) {
      setError(err.response?.data?.error || `Error al ${isEditing ? 'actualizar' : 'crear'} el alumno`);
    } finally {
      setLoading(false);
    }
  };

  const handleCancel = () => {
    navigate('/alumnos');
  };

  return (
    <div className="container mt-4">
      <div className="row justify-content-center">
        <div className="col-md-6">
          <div className="card">
            <div className="card-header">
              <h4>{isEditing ? 'Editar Alumno' : 'Crear Alumno'}</h4>
            </div>
            <div className="card-body">
              {error && <div className="alert alert-danger">{error}</div>}

              <form onSubmit={handleSubmit}>
                <div className="mb-3">
                  <label htmlFor="nombre" className="form-label">Nombre</label>
                  <input
                    type="text"
                    className="form-control"
                    id="nombre"
                    value={nombre}
                    onChange={(e) => setNombre(e.target.value)}
                    required
                    minLength={2}
                    maxLength={100}
                  />
                </div>

                <div className="mb-3">
                  <label htmlFor="apellidos" className="form-label">Apellidos</label>
                  <input
                    type="text"
                    className="form-control"
                    id="apellidos"
                    value={apellidos}
                    onChange={(e) => setApellidos(e.target.value)}
                    required
                    minLength={2}
                    maxLength={200}
                  />
                </div>

                <div className="mb-3">
                  <label htmlFor="dni" className="form-label">DNI</label>
                  <input
                    type="text"
                    className="form-control"
                    id="dni"
                    value={dni}
                    onChange={(e) => setDni(e.target.value)}
                    required
                    pattern="^\d{8}[A-Z]$"
                    title="DNI: 8 dígitos seguidos de una letra mayúscula"
                  />
                  <div className="form-text">Formato: 12345678A</div>
                </div>

                <div className="d-flex gap-2">
                  <button type="submit" className="btn btn-primary" disabled={loading}>
                    {loading ? 'Guardando...' : isEditing ? 'Guardar Cambios' : 'Crear Alumno'}
                  </button>
                  <button type="button" className="btn btn-secondary" onClick={handleCancel}>
                    Cancelar
                  </button>
                </div>
              </form>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}