import { useState, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { docentesService, UsuarioCreateDTO, UsuarioUpdateDTO, UsuarioDTO } from '../../services/docentesService';

export default function UsuarioFormComponent() {
  const navigate = useNavigate();
  const { id } = useParams<{ id: string }>();
  const isEditMode = Boolean(id);

  const emptyForm: UsuarioCreateDTO = {
    nombre: '',
    apellidos: '',
    dni: '',
    username: '',
    email: '',
    password: '',
  };

  const [formData, setFormData] = useState<UsuarioCreateDTO>(emptyForm);
  const [updateData, setUpdateData] = useState<UsuarioUpdateDTO>({
    nombre: '',
    apellidos: '',
    dni: '',
    username: '',
    email: '',
    password: '',
  });
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    if (isEditMode && id) {
      setLoading(true);
      docentesService.obtenerDocentePorId(Number(id))
        .then((docente: UsuarioDTO) => {
          const data = {
            nombre: docente.nombre || '',
            apellidos: docente.apellidos || '',
            dni: docente.dni || '',
            username: docente.username,
            email: docente.email || '',
            password: '',
          };
          setFormData(data);
          setUpdateData(data);
        })
        .catch(() => setError('Error al cargar los datos del docente'))
        .finally(() => setLoading(false));
    }
  }, [id, isEditMode]);

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    if (isEditMode) {
      setUpdateData(prev => ({ ...prev, [name]: value }));
    } else {
      setFormData(prev => ({ ...prev, [name]: value }));
    }
    setError('');
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setLoading(true);
    setError('');

    try {
      if (isEditMode && id) {
        await docentesService.actualizarDocente(Number(id), updateData);
        navigate('/docentes');
      } else {
        const created = await docentesService.crearDocente(formData);
         navigate(`/docentes/editar/${created.id}`);
      }
    } catch (err: any) {
      setError(err.response?.data?.error || 'Error al guardar el docente');
    } finally {
      setLoading(false);
    }
  };

  const handleCancel = () => {
    navigate('/docentes');
  };

  const currentData = isEditMode ? updateData : formData;

  return (
    <div className="container mt-4">
      <div className="row justify-content-center">
        <div className="col-md-6">
          <div className="card">
            <div className="card-header">
              <h4>{isEditMode ? 'Editar Docente' : 'Crear Docente'}</h4>
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
                    name="nombre"
                    value={currentData.nombre}
                    onChange={handleChange}
                    required
                  />
                </div>

                <div className="mb-3">
                  <label htmlFor="apellidos" className="form-label">Apellidos</label>
                  <input
                    type="text"
                    className="form-control"
                    id="apellidos"
                    name="apellidos"
                    value={currentData.apellidos}
                    onChange={handleChange}
                    required
                  />
                </div>

                <div className="mb-3">
                  <label htmlFor="dni" className="form-label">DNI</label>
                  <input
                    type="text"
                    className="form-control"
                    id="dni"
                    name="dni"
                    value={currentData.dni}
                    onChange={handleChange}
                    pattern="^[0-9]{8}[A-Z]$"
                    title="Formato: 12345678A"
                    required
                  />
                </div>

                <div className="mb-3">
                  <label htmlFor="username" className="form-label">Nombre de usuario</label>
                  <input
                    type="text"
                    className="form-control"
                    id="username"
                    name="username"
                    value={currentData.username}
                    onChange={handleChange}
                    required
                  />
                </div>

                <div className="mb-3">
                  <label htmlFor="email" className="form-label">Email</label>
                  <input
                    type="email"
                    className="form-control"
                    id="email"
                    name="email"
                    value={currentData.email}
                    onChange={handleChange}
                    required
                  />
                </div>

                <div className="mb-3">
                  <label htmlFor="password" className="form-label">
                    {isEditMode ? 'Nueva Contraseña (dejar vacío para mantener)' : 'Contraseña'}
                  </label>
                  <input
                    type="password"
                    className="form-control"
                    id="password"
                    name="password"
                    value={currentData.password}
                    onChange={handleChange}
                    minLength={isEditMode ? 0 : 6}
                    required={!isEditMode}
                  />
                </div>

                <div className="d-flex justify-content-between">
                  <button type="submit" className="btn btn-primary" disabled={loading}>
                    {loading ? 'Guardando...' : (isEditMode ? 'Guardar Cambios' : 'Crear Docente')}
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
