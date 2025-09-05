// RecetaController.java
package org.los_buenos.recetas.controller;

import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import lombok.Data;
import org.los_buenos.recetas.entity.Receta;
import org.los_buenos.recetas.entity.Usuario;
import org.los_buenos.recetas.service.IRecetaService;

import org.primefaces.PrimeFaces;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Component
@ViewScoped
@Data
public class RecetaController implements Serializable {

    @Autowired
    private IRecetaService recetaService;

    private List<Receta> recetas;
    private Receta recetaSeleccionada;
    private String filtroIngredientes;
    private Integer filtroTiempo;
    private String filtroCategoria;

    // Nueva propiedad para el carrusel, usaremos una lista de recetas.
    private List<Receta> carouselRecetas;

    private static final Logger logger = LoggerFactory.getLogger(RecetaController.class);

    @PostConstruct
    public void init() {
        cargarDatos();
        this.recetaSeleccionada = new Receta();
        // Inicializamos el carrusel con las 5 recetas más recientes.
        if (this.recetas != null && !this.recetas.isEmpty()) {
            // Ordenamos la lista por ID de forma descendente para obtener las más recientes.
            this.recetas.sort((r1, r2) -> r2.getIdReceta().compareTo(r1.getIdReceta()));
            // Tomamos las primeras 5 o menos si no hay suficientes.
            this.carouselRecetas = this.recetas.stream()
                    .limit(5)
                    .collect(Collectors.toList());
        }
    }

    public void cargarDatos() {
        this.recetas = this.recetaService.listarRecetas();
        this.recetas.forEach(receta -> logger.info(receta.toString()));
    }

    public void nuevaReceta() {
        this.recetaSeleccionada = new Receta();
        PrimeFaces.current().ajax().update("dialog-receta-form");
    }

    public void guardarReceta() {
        FacesContext context = FacesContext.getCurrentInstance();
        Usuario usuarioLogeado = (Usuario) context.getExternalContext().getSessionMap().get("usuarioLogeado");

        if (usuarioLogeado == null) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Debe iniciar sesión para agregar una receta."));
            return;
        }

        this.recetaSeleccionada.setUsuario(usuarioLogeado);

        logger.info("Receta a guardar: " + this.recetaSeleccionada);
        if (this.recetaSeleccionada.getIdReceta() == null) {
            this.recetaService.guardarReceta(this.recetaSeleccionada);
            this.recetas.add(this.recetaSeleccionada);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Receta agregada"));
        } else {
            this.recetaService.guardarReceta(this.recetaSeleccionada);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Receta actualizada"));
        }
        PrimeFaces.current().executeScript("PF('ventanaModalReceta').hide()");
        PrimeFaces.current().ajax().update("formulario-recetas:mensaje-emergente", "formulario-recetas:tabla-recetas");
        this.recetaSeleccionada = new Receta();
    }

    public void eliminarReceta() {
        logger.info("Receta a eliminar: " + this.recetaSeleccionada);
        this.recetaService.eliminarReceta(this.recetaSeleccionada);
        this.recetas.remove(this.recetaSeleccionada);
        this.recetaSeleccionada = null;
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Receta eliminada"));
        PrimeFaces.current().ajax().update("formulario-recetas:mensaje-emergente", "formulario-recetas:tabla-recetas");
    }

    // Lógica de búsqueda mejorada para incluir todos los filtros
    public void buscarRecetas() {
        // Si no hay ningún filtro, mostramos todas las recetas.
        if ((filtroIngredientes == null || filtroIngredientes.trim().isEmpty()) &&
                filtroTiempo == null &&
                (filtroCategoria == null || filtroCategoria.trim().isEmpty())) {
            this.recetas = this.recetaService.listarRecetas();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Mostrando todas las recetas."));
        } else {
            // Filtramos la lista en memoria para simplificar la lógica de búsqueda.
            this.recetas = this.recetaService.listarRecetas().stream()
                    .filter(receta -> {
                        boolean match = true;
                        if (filtroIngredientes != null && !filtroIngredientes.trim().isEmpty()) {
                            match = receta.getIngredientes().toLowerCase().contains(filtroIngredientes.trim().toLowerCase());
                        }
                        if (match && filtroTiempo != null) {
                            match = receta.getTiempoPreparacion() <= filtroTiempo;
                        }
                        // La lógica para filtrar por categoría
                        // Asumiendo que `Receta` tiene una propiedad `categoria`
                        // que podemos usar para el filtro.
                        // Si la propiedad no existe, se deberá agregar a la entidad Receta.
                        if (match && filtroCategoria != null && !filtroCategoria.trim().isEmpty()) {
                            // Aquí deberías tener una propiedad de categoría en tu entidad Receta
                            // y usarla para filtrar. Por ejemplo:
                            // match = receta.getCategoria().equals(filtroCategoria);
                        }
                        return match;
                    })
                    .collect(Collectors.toList());
        }

        if (this.recetas.isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Advertencia", "No se encontraron recetas con los filtros indicados."));
        }
        PrimeFaces.current().ajax().update("formulario-recetas:tabla-recetas");
    }
}
