import { useState, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { asignaturasService, AsignaturaCreateDTO, AsignaturaDTO } from '../../services/asignaturasService';

export default function AsignaturaFormComponent() {
  const navigate = useNavigate();
  const { id } = useParams<{ id: string }>();
  const isEditing = Boolean(id);

  const [loading, setLoading] = useState(false);
  const [loadingData, setLoadingData] = useState(isEditing);
  const [error, setError] = useState('');
  const [formData, setFormData] = useState<AsignaturaCreateDTO>({
    titulo: '',
    codigo: '',
    cursoAcademico: '',
  });
  const [validationErrors, setValidationErrors] = useState<Partial<AsignaturaCreateDTO>>({});

  useEffect(() => {
    if (isEditing && id) {
      cargarAsignatura(parseInt(id));
    }
  }, [id]);

  const cargarAsignatura = async (asignaturaId: number) => {
    try {
      const asignatura = await asignaturasService.getAsignaturaById(asignaturaId);
      setFormData({
        titulo: asignatura.titulo,
        codigo: asignatura.codigo,
        cursoAcademico: asignatura.cursoAcademico,
      });
    } catch (err: any) {
      setError(err.response?.data?.error || 'Error al cargar la asignatura');
    } finally {
      setLoadingData(false);
    }
  };

  const validate = (): boolean => {
    const errors: Partial<AsignaturaCreateDTO> = {};

    if (!formData.titulo.trim()) {
      errors.titulo = 'El título es obligatorio';
    } else if (formData.titulo.length < 2 || formData.titulo.length > 100) {
      errors.titulo = 'El título debe tener entre 2 y 100 caracteres';
    }

    if (!formData.codigo.trim()) {
      errors.codigo = 'El código es obligatorio';
    } else if (formData.codigo.length < 2 || formData.codigo.length > 20) {
      errors.codigo = 'El código debe tener entre 2 y 20 caracteres';
    }

    if (!formData.cursoAcademico.trim()) {
      errors.cursoAcademico = 'El curso académico es obligatorio';
    } else if (formData.cursoAcademico.length < 2 || formData.cursoAcademico.length > 50) {
      errors.cursoAcademico = 'El curso académico debe tener entre 2 y 50 caracteres';
    }

    setValidationErrors(errors);
    return Object.keys(errors).length === 0;
  };

  const handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const { name, value } = e.target;
    setFormData(prev => ({ ...prev, [name]: value }));
    if (validationErrors[name as keyof AsignaturaCreateDTO]) {
      setValidationErrors(prev => ({ ...prev, [name]: undefined }));
    }
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();

    if (!validate()) return;

    setLoading(true);
    setError('');

    try {
      if (isEditing && id) {
        await asignaturasService.actualizarAsignatura(parseInt(id), formData);
        navigate('/asignaturas');
      } else {
        await asignaturasService.crearAsignatura(formData);
        navigate('/asignaturas');
      }
    } catch (err: any) {
      setError(err.response?.data?.error || 'Error al guardar la asignatura');
    } finally {
      setLoading(false);
    }
  };

  const handleCancel = () => {
    navigate('/asignaturas');
  };

  if (loadingData) {
    return <div className="container mt-4"><div className="text-center">Cargando...</div></div>;
  }

  return (
    <div className="container mt-4">
      <div className="row justify-content-center">
        <div className="col-md-6">
          <div className="card">
            <div className="card-header">
              <h4>{isEditing ? 'Editar Asignatura' : 'Nueva Asignatura'}</h4>
            </div>
            <div className="card-body">
              {error && <div className="alert alert-danger">{error}</div>}

              <form onSubmit={handleSubmit}>
                <div className="mb-3">
                  <label htmlFor="titulo" className="form-label">Título *</label>
                  <input
                    type="text"
                    className={`form-control ${validationErrors.titulo ? 'is-invalid' : ''}`}
                    id="titulo"
                    name="titulo"
                    value={formData.titulo}
                    onChange={handleChange}
                    placeholder="Ej: Introducción a la Programación"
                  />
                  {validationErrors.titulo && (
                    <div className="invalid-feedback">{validationErrors.titulo}</div>
                  )}
                </div>

                <div className="mb-3">
                  <label htmlFor="codigo" className="form-label">Código *</label>
                  <input
                    type="text"
                    className={`form-control ${validationErrors.codigo ? 'is-invalid' : ''}`}
                    id="codigo"
                    name="codigo"
                    value={formData.codigo}
                    onChange={handleChange}
                    placeholder="Ej: IP-101"
                  />
                  {validationErrors.codigo && (
                    <div className="invalid-feedback">{validationErrors.codigo}</div>
                  )}
                </div>

                <div className="mb-3">
                  <label htmlFor="cursoAcademico" className="form-label">Curso Académico *</label>
                  <input
                    type="text"
                    className={`form-control ${validationErrors.cursoAcademico ? 'is-invalid' : ''}`}
                    id="cursoAcademico"
                    name="cursoAcademico"
                    value={formData.cursoAcademico}
                    onChange={handleChange}
                    placeholder="Ej: 2025-2026"
                  />
                  {validationErrors.cursoAcademico && (
                    <div className="invalid-feedback">{validationErrors.cursoAcademico}</div>
                  )}
                </div>

                <div className="d-flex justify-content-between">
                  <button type="button" className="btn btn-secondary" onClick={handleCancel}>
                    Cancelar
                  </button>
                  <button type="submit" className="btn btn-primary" disabled={loading}>
                    {loading ? 'Guardando...' : 'Guardar'}
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