package org.los_buenos.recetas.controller;

import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import lombok.Data;
import org.los_buenos.recetas.entity.Calificacion;
import org.los_buenos.recetas.service.CalificacionService;
import org.primefaces.PrimeFaces;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ViewScoped
@Data
public class CalificacionController {

    @Autowired
    private CalificacionService calificacionService;

    private List<Calificacion> calificaciones;
    private Calificacion calificacionSeleccionada;
    private static final Logger logger = LoggerFactory.getLogger(CalificacionController.class);

    @PostConstruct
    public void init() {
        cargarDatos();
    }

    public void cargarDatos() {
        this.calificaciones = this.calificacionService.listarCalificaciones();
        this.calificaciones.forEach(calificacion -> logger.info(calificacion.toString()));
    }

    public void agregarCalificacion() {
        this.calificacionSeleccionada = new Calificacion();
    }

    public void guardarCalificacion() {
        logger.info("Calificacion a guardar: " + this.calificacionSeleccionada);
        if (this.calificacionSeleccionada.getIdCalificacion() == null) {
            this.calificacionService.guardarCalificacion(this.calificacionSeleccionada);
            this.calificaciones.add(this.calificacionSeleccionada);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Calificación agregada"));
        } else {
            this.calificacionService.guardarCalificacion(this.calificacionSeleccionada);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Calificación actualizada"));
        }
        PrimeFaces.current().executeScript("PF('ventanaModalCalificacion').hide()");
        PrimeFaces.current().ajax().update("formulario-calificaciones:mensaje-emergente", "formulario-calificaciones:tabla-calificaciones");
        this.calificacionSeleccionada = null;
    }

    public void eliminarCalificacion() {
        logger.info("Calificacion a eliminar: " + this.calificacionSeleccionada);
        this.calificacionService.eliminarCalificacion(this.calificacionSeleccionada);
        this.calificaciones.remove(this.calificacionSeleccionada);
        this.calificacionSeleccionada = null;
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Calificación eliminada"));
        PrimeFaces.current().ajax().update("formulario-calificaciones:mensaje-emergente", "formulario-calificaciones:tabla-calificaciones");
    }
}