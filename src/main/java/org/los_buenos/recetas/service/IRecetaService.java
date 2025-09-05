package org.los_buenos.recetas.service;

import org.los_buenos.recetas.entity.Receta;
import java.util.List;
import java.util.Optional;

public interface IRecetaService {

    List<Receta> listarRecetas();
    Optional<Receta> buscarRecetaPorId(Integer idReceta);
    void guardarReceta(Receta receta);
    void eliminarReceta(Receta receta);
    List<Receta> buscarRecetasPorIngredientes(String ingredientes);
    List<Receta> buscarRecetasPorTiempo(String tiempo); // <-- Agrega esta línea
}