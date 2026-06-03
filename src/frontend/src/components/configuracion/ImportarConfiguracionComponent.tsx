import { useState, useRef } from 'react';
import { useNavigate } from 'react-router-dom';
import { configuracionService, ConfiguracionExportDTO } from '../../services/configuracionService';

export default function ImportarConfiguracionComponent() {
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [mostrarConfirmacion, setMostrarConfirmacion] = useState(false);
  const [archivoSeleccionado, setArchivoSeleccionado] = useState<ConfiguracionExportDTO | null>(null);
  const [archivoRaw, setArchivoRaw] = useState<File | null>(null);
  const fileInputRef = useRef<HTMLInputElement>(null);
  const navigate = useNavigate();

  const handleFileChange = async (event: React.ChangeEvent<HTMLInputElement>) => {
    const file = event.target.files?.[0];
    if (!file) return;

    setError('');
    try {
      const text = await file.text();
      const json = JSON.parse(text) as ConfiguracionExportDTO;

      if (!json.version || !json.elementos || !json.elementos.grados) {
        throw new Error('Archivo JSON no válido');
      }

      setArchivoSeleccionado(json);
      setArchivoRaw(file);
      setMostrarConfirmacion(true);
    } catch (err) {
      setError('El archivo seleccionado no es un JSON válido de configuración');
      setArchivoSeleccionado(null);
      setArchivoRaw(null);
    }
  };

  const handleImportar = async () => {
    if (!archivoRaw) return;

    setLoading(true);
    setError('');
    try {
      await configuracionService.importarConfiguracionGlobal(archivoRaw);
      navigate('/menu-docente');
    } catch (err: any) {
      setError(err.response?.data?.error || 'Error al importar la configuración');
    } finally {
      setLoading(false);
    }
  };

  const handleCancelar = () => {
    setMostrarConfirmacion(false);
    setArchivoSeleccionado(null);
    setArchivoRaw(null);
    if (fileInputRef.current) {
      fileInputRef.current.value = '';
    }
  };

  const handleVolver = () => {
    navigate('/menu-docente');
  };

  if (!mostrarConfirmacion) {
    return (
      <div className="container mt-4">
        <div className="card">
          <div className="card-header bg-secondary text-white">
            <h5>Importar Configuración Global</h5>
          </div>
          <div className="card-body">
            {error && <div className="alert alert-danger mb-3">{error}</div>}

            <div className="alert alert-info">
              <strong>Información:</strong> La importación reemplazará TODA su configuración actual.
              Se eliminarán los datos existentes y se importarán los nuevos.
            </div>

            <p>Seleccione un archivo JSON de configuración previamente exportado:</p>

            <input
              type="file"
              ref={fileInputRef}
              accept=".json"
              onChange={handleFileChange}
              className="form-control mb-3"
            />

            <div className="d-flex justify-content-end gap-2">
              <button
                className="btn btn-secondary"
                onClick={handleVolver}
              >
                Volver
              </button>
            </div>
          </div>
        </div>
      </div>
    );
  }

  return (
    <div className="container mt-4">
      <div className="card">
        <div className="card-header bg-warning text-dark">
          <h5>Confirmar Importación</h5>
        </div>
        <div className="card-body">
          {error && <div className="alert alert-danger mb-3">{error}</div>}

          <div className="alert alert-warning">
            <strong>⚠️ Atención:</strong> Se borrarán todos sus datos actuales y se importarán los nuevos.
            Esta acción no se puede deshacer.
          </div>

          {archivoSeleccionado && (
            <div className="mb-3">
              <p><strong>Archivo:</strong> {archivoRaw?.name}</p>
              <p><strong>Versión:</strong> {archivoSeleccionado.version}</p>
              <p><strong>Fecha de exportación:</strong> {archivoSeleccionado.fechaExportacion}</p>
              <p><strong>Docente origen:</strong> {archivoSeleccionado.docenteOrigen?.nombre} {archivoSeleccionado.docenteOrigen?.apellidos}</p>
              <p><strong>Elementos a importar:</strong></p>
              <ul>
                <li>Grados: {archivoSeleccionado.elementos?.grados?.length || 0}</li>
                <li>Asignaturas: {archivoSeleccionado.elementos?.asignaturas?.length || 0}</li>
                <li>Alumnos: {archivoSeleccionado.elementos?.alumnos?.length || 0}</li>
                <li>Preguntas: {archivoSeleccionado.elementos?.preguntas?.length || 0}</li>
              </ul>
            </div>
          )}

          <div className="d-flex gap-2 justify-content-end">
            <button
              className="btn btn-secondary"
              onClick={handleCancelar}
              disabled={loading}
            >
              Cancelar
            </button>
            <button
              className="btn btn-primary"
              onClick={handleImportar}
              disabled={loading}
            >
              {loading ? 'Importando...' : 'Confirmar Importación'}
            </button>
          </div>
        </div>
      </div>
    </div>
  );
}