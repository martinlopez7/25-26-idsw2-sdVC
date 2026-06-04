import React, { useState } from 'react';
import { correccionService } from '../../services/correccionService';
import { ResultadoCorreccion } from '../../types';

const CorregirExamenesComponent: React.FC = () => {
    const [archivo, setArchivo] = useState<File | null>(null);
    const [resultados, setResultados] = useState<ResultadoCorreccion[] | null>(null);
    const [error, setError] = useState<string | null>(null);
    const [cargando, setCargando] = useState(false);

    const handleArchivoChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        const file = e.target.files?.[0];
        if (file) {
            if (file.type !== 'application/pdf') {
                setError('Por favor, selecciona un archivo PDF');
                setArchivo(null);
                return;
            }
            setArchivo(file);
            setError(null);
            setResultados(null);
        }
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        if (!archivo) {
            setError('Por favor, selecciona un archivo PDF');
            return;
        }

        setCargando(true);
        setError(null);

        try {
            const resultados = await correccionService.corregirExamenes(archivo);
            setResultados(resultados);
        } catch (err) {
            setError(err instanceof Error ? err.message : 'Error al procesar el archivo');
        } finally {
            setCargando(false);
        }
    };

    const handleVolver = () => {
        window.location.href = '/menu-docente';
    };

    return (
        <div className="container mt-4">
            <h2>Corregir Exámenes</h2>
            <hr />

            <div className="card">
                <div className="card-body">
                    <h5 className="card-title">Subir exámenes resueltos</h5>
                    <p className="card-text text-muted">
                        Sube un PDF donde cada página corresponde al examen respondido por un alumno.
                        El sistema asignará una nota aleatoria del 1 al 10 a cada página.
                    </p>

                    <form onSubmit={handleSubmit}>
                        <div className="mb-3">
                            <label htmlFor="archivoPdf" className="form-label">
                                Archivo PDF con exámenes resueltos
                            </label>
                            <input
                                type="file"
                                className="form-control"
                                id="archivoPdf"
                                accept=".pdf"
                                onChange={handleArchivoChange}
                                disabled={cargando}
                            />
                        </div>

                        {error && (
                            <div className="alert alert-danger" role="alert">
                                {error}
                            </div>
                        )}

                        <div className="d-grid gap-2 d-md-flex justify-content-md-end">
                            <button
                                type="submit"
                                className="btn btn-primary"
                                disabled={!archivo || cargando}
                            >
                                {cargando ? 'Procesando...' : 'Corregir'}
                            </button>
                            <button
                                type="button"
                                className="btn btn-secondary"
                                onClick={handleVolver}
                                disabled={cargando}
                            >
                                Volver al menú
                            </button>
                        </div>
                    </form>
                </div>
            </div>

            {resultados && (
                <div className="card mt-4">
                    <div className="card-body">
                        <h5 className="card-title">Resultados de la corrección</h5>
                        <p className="card-text">
                            Se procesaron {resultados.length} exámenes.
                        </p>

                        <table className="table table-striped">
                            <thead>
                                <tr>
                                    <th scope="col">Página</th>
                                    <th scope="col">Nota</th>
                                </tr>
                            </thead>
                            <tbody>
                                {resultados.map((resultado) => (
                                    <tr key={resultado.numeroPagina}>
                                        <td>Página {resultado.numeroPagina}</td>
                                        <td>
                                            <span className={`badge ${
                                                resultado.nota >= 5 ? 'bg-success' : 'bg-danger'
                                            }`}>
                                                {resultado.nota}
                                            </span>
                                        </td>
                                    </tr>
                                ))}
                            </tbody>
                        </table>
                    </div>
                </div>
            )}
        </div>
    );
};

export default CorregirExamenesComponent;