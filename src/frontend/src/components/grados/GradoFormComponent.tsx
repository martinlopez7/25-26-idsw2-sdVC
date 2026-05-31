import { useState, useEffect } from 'react';
import { useNavigate, useParams } from 'react-router-dom';
import { gradosService, GradoDTO } from '../../services/gradosService';

interface GradoFormProps {
  grado?: GradoDTO;
  isEditing?: boolean;
}

export default function GradoFormComponent({ grado: gradoProp, isEditing = false }: GradoFormProps) {
  const navigate = useNavigate();
  const { id } = useParams<{ id: string }>();
  const [grado, setGrado] = useState<GradoDTO | undefined>(gradoProp);
  const [titulo, setTitulo] = useState(gradoProp?.titulo || '');
  const [codigo, setCodigo] = useState(gradoProp?.codigo || '');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);
  const [loadingData, setLoadingData] = useState(isEditing && !gradoProp);

  useEffect(() => {
    if (isEditing && !gradoProp && id) {
      setLoadingData(true);
      gradosService.getGradoById(Number(id))
        .then((data) => {
          setGrado(data);
          setTitulo(data.titulo);
          setCodigo(data.codigo);
        })
        .catch((err: any) => {
          setError(err.response?.data?.error || 'Error al cargar el grado');
        })
        .finally(() => {
          setLoadingData(false);
        });
    }
  }, [isEditing, gradoProp, id]);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError('');
    setLoading(true);

    try {
      if (isEditing && grado?.id) {
        await gradosService.actualizarGrado(grado.id, { titulo, codigo });
        navigate('/grados');
      } else {
        const created = await gradosService.crearGrado({ titulo, codigo });
        navigate(`/grados/editar/${created.id}`);
      }
    } catch (err: any) {
      setError(err.response?.data?.error || `Error al ${isEditing ? 'actualizar' : 'crear'} el grado`);
    } finally {
      setLoading(false);
    }
  };

  const handleCancel = () => {
    navigate('/grados');
  };

  return (
    <div className="container mt-4">
      <div className="row justify-content-center">
        <div className="col-md-6">
          <div className="card">
            <div className="card-header">
              <h4>{isEditing ? 'Editar Grado' : 'Crear Grado'}</h4>
            </div>
            <div className="card-body">
              {loadingData ? (
                <div className="text-center">Cargando...</div>
              ) : (
                <>
                  {error && <div className="alert alert-danger">{error}</div>}

                  <form onSubmit={handleSubmit}>
                    <div className="mb-3">
                      <label htmlFor="titulo" className="form-label">Título</label>
                      <input
                        type="text"
                        className="form-control"
                        id="titulo"
                        value={titulo}
                        onChange={(e) => setTitulo(e.target.value)}
                        required
                        minLength={3}
                        maxLength={200}
                      />
                    </div>

                    <div className="mb-3">
                      <label htmlFor="codigo" className="form-label">Código</label>
                      <input
                        type="text"
                        className="form-control"
                        id="codigo"
                        value={codigo}
                        onChange={(e) => setCodigo(e.target.value)}
                        required
                        minLength={2}
                        maxLength={50}
                      />
                    </div>

                    <div className="d-flex gap-2">
                      <button type="submit" className="btn btn-primary" disabled={loading}>
                        {loading ? 'Guardando...' : isEditing ? 'Guardar Cambios' : 'Crear Grado'}
                      </button>
                      <button type="button" className="btn btn-secondary" onClick={handleCancel}>
                        Cancelar
                      </button>
                    </div>
                  </form>
                </>
              )}
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
