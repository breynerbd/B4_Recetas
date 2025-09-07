package org.los_buenos.recetas.controller;
import org.los_buenos.recetas.entity.Usuario;
import org.los_buenos.recetas.service.UsuarioService;
import jakarta.inject.Named;
import jakarta.faces.view.ViewScoped;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import java.io.Serializable;
import java.util.Optional;
import jakarta.inject.Inject;

@Named
@ViewScoped
public class loginController implements Serializable {

    @Inject
    private UsuarioService usuarioService;


    private String email;
    private String password;


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String login() {

        Optional<Usuario> usuarioEncontrado = usuarioService.buscarUsuarioPorEmailYContraseña(this.email, this.password);

        if (usuarioEncontrado.isPresent()) {
            Usuario usuario = usuarioEncontrado.get();

            FacesContext.getCurrentInstance().getExternalContext().getSessionMap().put("usuarioLogeado", usuario);
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_INFO, "Login Exitoso", "Bienvenido de nuevo!"));

            if ("Usuario".equalsIgnoreCase(usuario.getRol())) {
                return "menuPrincipal?faces-redirect=true";
            } else if ("Moderador".equalsIgnoreCase(usuario.getRol())) {
                return "gestionAdmin?faces-redirect=true";
            } else {
                FacesContext.getCurrentInstance().addMessage(null,
                        new FacesMessage(FacesMessage.SEVERITY_WARN, "No tienes rol",
                                "No tienes permisos para ir ahi."));
                return null;
            }

        } else {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error de Login", "Correo o contraseña incorrectos."));
            return null;
        }
    }
}