package org.los_buenos.recetas.controller;

import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;
import jakarta.annotation.PostConstruct;
import java.io.Serializable;
import org.los_buenos.recetas.entity.Usuario;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import org.los_buenos.recetas.service.UsuarioService;
import jakarta.inject.Inject;

@Named
@ViewScoped
public class RegisterController implements Serializable {

    @Inject
    private UsuarioService usuarioService;

    private Usuario nuevoUsuario; // Esta es la propiedad que necesitas

    @PostConstruct
    public void init() {
        nuevoUsuario = new Usuario();
    }

    // **¡AÑADE ESTOS GETTERS Y SETTERS!**
    public Usuario getNuevoUsuario() {
        return nuevoUsuario;
    }

    public void setNuevoUsuario(Usuario nuevoUsuario) {
        this.nuevoUsuario = nuevoUsuario;
    }

    public String guardarUsuario() {
        try {
            usuarioService.guardarUsuario(nuevoUsuario);

            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Registro Exitoso", "¡Bienvenido a RecetasWeb!"));
            return "login?faces-redirect=true";
        } catch (Exception e) {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error de Registro", "No se pudo registrar al usuario."));
            return null;
        }
    }
}