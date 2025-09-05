package org.los_buenos.recetas.service;

import org.los_buenos.recetas.entity.Calificacion;
import org.los_buenos.recetas.repository.CalificacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CalificacionService implements ICalificacionService {

    @Autowired
    private CalificacionRepository calificacionRepository;

    @Override
    public List<Calificacion> listarCalificaciones() {
        return calificacionRepository.findAll();
    }

    @Override
    public Optional<Calificacion> buscarCalificacionPorId(Integer idCalificacion) {
        return calificacionRepository.findById(idCalificacion);
    }

    @Override
    public void guardarCalificacion(Calificacion calificacion) {
        calificacionRepository.save(calificacion);
    }

    @Override
    public void eliminarCalificacion(Calificacion calificacion) {
        calificacionRepository.delete(calificacion);
    }
}