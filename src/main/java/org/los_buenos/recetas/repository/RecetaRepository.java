package org.los_buenos.recetas.repository;

import org.los_buenos.recetas.entity.Receta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalTime;
import java.util.List;

@Repository
public interface RecetaRepository extends JpaRepository<Receta, Integer> {

    List<Receta> findByIngredientesContainingIgnoreCase(String ingredientes);

    List<Receta> findByTiempoPreparacionIsLessThan(Integer tiempo);
}