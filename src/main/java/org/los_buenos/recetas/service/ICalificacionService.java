package org.los_buenos.recetas.service;

import org.los_buenos.recetas.entity.Calificacion;
import java.util.List;
import java.util.Optional;

public interface ICalificacionService {
    List<Calificacion> listarCalificaciones();
    Optional<Calificacion> buscarCalificacionPorId(Integer idCalificacion);
    void guardarCalificacion(Calificacion calificacion);
    void eliminarCalificacion(Calificacion calificacion);
}