package org.los_buenos.recetas.controller;

import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import lombok.Data;
import org.los_buenos.recetas.entity.Usuario;
import org.los_buenos.recetas.service.UsuarioService;
import org.primefaces.PrimeFaces;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@ViewScoped
@Data
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    private List<Usuario> usuarios;
    private Usuario usuarioSeleccionado;
    private static final Logger logger = LoggerFactory.getLogger(UsuarioController.class);

    @PostConstruct
    public void init() {
        cargarDatos();
    }

    public void cargarDatos() {
        this.usuarios = this.usuarioService.listarUsuarios();
        this.usuarios.forEach(usuario -> logger.info(usuario.toString()));
    }

    public void agregarUsuario() {
        this.usuarioSeleccionado = new Usuario();
    }

    public void guardarUsuario() {
        logger.info("Usuario a guardar: " + this.usuarioSeleccionado);
        if (this.usuarioSeleccionado.getIdUsuario() == null) {
            // Agregar nuevo usuario
            this.usuarioService.guardarUsuario(this.usuarioSeleccionado);
            this.usuarios.add(this.usuarioSeleccionado);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Usuario agregado"));
        } else {
            // Modificar usuario existente
            this.usuarioService.guardarUsuario(this.usuarioSeleccionado);
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Usuario actualizado"));
        }
        PrimeFaces.current().executeScript("PF('ventanaModalUsuario').hide()");
        PrimeFaces.current().ajax().update("formulario-usuarios:mensaje-emergente", "formulario-usuarios:tabla-usuarios");
        this.usuarioSeleccionado = null;
    }

    public void eliminarUsuario() {
        logger.info("Usuario a eliminar: " + this.usuarioSeleccionado);
        this.usuarioService.eliminarUsuario(this.usuarioSeleccionado);
        this.usuarios.remove(this.usuarioSeleccionado);
        this.usuarioSeleccionado = null;
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Usuario eliminado"));
        PrimeFaces.current().ajax().update("formulario-usuarios:mensaje-emergente", "formulario-usuarios:tabla-usuarios");
    }
}