package org.los_buenos.recetas.repository;


import org.los_buenos.recetas.entity.Calificacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CalificacionRepository extends JpaRepository<Calificacion, Integer> {
    // Aquí puedes agregar métodos personalizados si es necesario,
    // como buscar todas las calificaciones para una receta.
    // Ejemplo:
    // List<Calificacion> findByReceta(Receta receta);
}