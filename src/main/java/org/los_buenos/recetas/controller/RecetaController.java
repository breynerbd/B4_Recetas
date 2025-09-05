package org.los_buenos.recetas.controller;

import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import lombok.Data;
import org.los_buenos.recetas.entity.Receta;
import org.los_buenos.recetas.service.RecetaService;
import org.primefaces.PrimeFaces;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ViewScoped
@Data
public class RecetaController {

    @Autowired
    private RecetaService recetaService;

    private List<Receta> recetas;
    private Receta recetaSeleccionada;
    private String filtroIngredientes;
    private String filtroTiempo;
    private static final Logger logger = LoggerFactory.getLogger(RecetaController.class);

    @PostConstruct
    public void init() {
        cargarDatos();
    }

    public void cargarDatos() {
        this.recetas = this.recetaService.listarRecetas();
        this.recetas.forEach(receta -> logger.info(receta.toString()));
    }

    public void agregarReceta() {
        this.recetaSeleccionada = new Receta();
    }

    public void guardarReceta() {
        logger.info("Receta a guardar: " + this.recetaSeleccionada);
        if (this.recetaSeleccionada.getIdReceta() == null) {
            // Agregar nueva receta
            this.recetaService.guardarReceta(this.recetaSeleccionada);
            this.recetas.add(this.recetaSeleccionada);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Receta agregada"));
        } else {
            // Modificar receta existente
            this.recetaService.guardarReceta(this.recetaSeleccionada);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Receta actualizada"));
        }
        PrimeFaces.current().executeScript("PF('ventanaModalReceta').hide()");
        PrimeFaces.current().ajax().update("formulario-recetas:mensaje-emergente", "formulario-recetas:tabla-recetas");
        this.recetaSeleccionada = null;
    }

    public void eliminarReceta() {
        logger.info("Receta a eliminar: " + this.recetaSeleccionada);
        this.recetaService.eliminarReceta(this.recetaSeleccionada);
        this.recetas.remove(this.recetaSeleccionada);
        this.recetaSeleccionada = null;
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Receta eliminada"));
        PrimeFaces.current().ajax().update("formulario-recetas:mensaje-emergente", "formulario-recetas:tabla-recetas");
    }

    public void buscarRecetas() {
        // Si ambos filtros están vacíos, carga todas las recetas
        if ((filtroIngredientes == null || filtroIngredientes.trim().isEmpty()) &&
                (filtroTiempo == null || filtroTiempo.trim().isEmpty())) {
            this.recetas = this.recetaService.listarRecetas();
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Info", "Mostrando todas las recetas."));
        }
        // Si solo se busca por ingredientes
        else if (filtroTiempo == null || filtroTiempo.trim().isEmpty()) {
            this.recetas = this.recetaService.buscarRecetasPorIngredientes(filtroIngredientes);
        }
        // Si solo se busca por tiempo
        else if (filtroIngredientes == null || filtroIngredientes.trim().isEmpty()) {
            this.recetas = this.recetaService.buscarRecetasPorTiempo(filtroTiempo);
        }
        // Si se buscan ambas cosas
        else {
            // Debes implementar una lógica que combine ambos filtros en el servicio
            // Por ahora, solo usaremos el de ingredientes como ejemplo
            this.recetas = this.recetaService.buscarRecetasPorIngredientes(filtroIngredientes);
        }

        if (this.recetas.isEmpty()) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Advertencia", "No se encontraron recetas con los filtros indicados."));
        }

        PrimeFaces.current().ajax().update("formulario-recetas:tabla-recetas");
    }
}