package org.los_buenos.recetas.service;

import org.los_buenos.recetas.entity.Receta;
import org.los_buenos.recetas.repository.RecetaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class RecetaService implements IRecetaService {

    @Autowired
    private RecetaRepository recetaRepository;

    @Override
    public List<Receta> listarRecetas() {
        return recetaRepository.findAll();
    }

    @Override
    public Optional<Receta> buscarRecetaPorId(Integer idReceta) {
        return recetaRepository.findById(idReceta);
    }

    @Override
    public void guardarReceta(Receta receta) {
        recetaRepository.save(receta);
    }

    @Override
    public void eliminarReceta(Receta receta) {
        recetaRepository.delete(receta);
    }

    @Override
    public List<Receta> buscarRecetasPorIngredientes(String ingredientes) {
        return recetaRepository.findByIngredientesContainingIgnoreCase(ingredientes);
    }

    @Override
    public List<Receta> buscarRecetasPorTiempo(Integer tiempo) { // Cambiado a Integer
        if (tiempo == null) {
            return Collections.emptyList();
        }
        return recetaRepository.findByTiempoPreparacionIsLessThan(tiempo);
    }
}