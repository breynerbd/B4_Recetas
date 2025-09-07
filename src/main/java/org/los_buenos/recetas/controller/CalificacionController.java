package org.los_buenos.recetas.controller;

import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import lombok.Data;
import org.los_buenos.recetas.entity.Calificacion;
import org.los_buenos.recetas.entity.Receta;
import org.los_buenos.recetas.entity.Usuario;
import org.los_buenos.recetas.service.CalificacionService;
import org.primefaces.PrimeFaces;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.List;

@Component
@ViewScoped
@Data
public class CalificacionController implements Serializable {

    @Autowired
    private CalificacionService calificacionService;
    // Debes inyectar también el servicio de usuario para obtener el usuario actual
    // @Autowired
    // private UsuarioService usuarioService;

    private List<Calificacion> calificaciones;
    private Calificacion calificacionSeleccionada;
    private Receta recetaACalificar; // Propiedad añadida para la receta seleccionada
    private static final Logger logger = LoggerFactory.getLogger(CalificacionController.class);

    @PostConstruct
    public void init() {
        cargarDatos();
    }

    public void cargarDatos() {
        this.calificaciones = this.calificacionService.listarCalificaciones();
        this.calificaciones.forEach(calificacion -> logger.info(calificacion.toString()));
    }

    public void prepararCalificacion(Receta receta) {
        logger.info("Preparando calificación para la receta: " + receta.getTituloReceta());
        this.recetaACalificar = receta;
        this.calificacionSeleccionada = new Calificacion();
        this.calificacionSeleccionada.setReceta(receta);

        // Aquí deberías obtener el usuario actual. Como no tenemos el servicio, lo simulamos.
        // Usuario usuarioActual = usuarioService.obtenerUsuarioActual();
        // this.calificacionSeleccionada.setUsuario(usuarioActual);
        // NOTA: Recuerda descomentar las líneas anteriores e inyectar el servicio de usuario cuando lo tengas.
        Usuario usuarioDemo = new Usuario();
        usuarioDemo.setIdUsuario(1);
        this.calificacionSeleccionada.setUsuario(usuarioDemo);
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
        PrimeFaces.current().ajax().update("formulario-recetas:mensaje-emergente", "formulario-recetas:tabla-recetas");
        PrimeFaces.current().executeScript("PF('ventanaModalCalificar').hide()");
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