package org.los_buenos.recetas.service;

import org.los_buenos.recetas.entity.Usuario;
import java.util.List;
import java.util.Optional;

public interface IUsuarioService {

    List<Usuario> listarUsuarios();
    Optional<Usuario> buscarUsuarioPorId(Integer idUsuario);
    Optional<Usuario> buscarUsuarioPorNombreUsuario(String nombreUsuario); // <-- Nuevo método
    void guardarUsuario(Usuario usuario);
    void eliminarUsuario(Usuario usuario);
    public Optional<Usuario> buscarUsuarioPorEmailYContraseña(String email, String contraseña);

}