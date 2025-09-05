package org.los_buenos.recetas.service;

import org.los_buenos.recetas.entity.Usuario;
import org.los_buenos.recetas.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService implements IUsuarioService {

    @Override
    public Optional<Usuario> buscarUsuarioPorEmailYContraseña(String email, String contraseña) {
        // Aquí es crucial que implementes la lógica de encriptación de contraseñas.
        // NUNCA guardes las contraseñas en texto plano.
        Optional<Usuario> usuario = usuarioRepository.findByEmail(email); // Asume que tienes este método en tu repositorio

        // Simulación de una validación de contraseña:
        if (usuario.isPresent() && usuario.get().getContraseña().equals(contraseña)) {
            return usuario;
        }

        return Optional.empty();
    }

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll();
    }

    @Override
    public Optional<Usuario> buscarUsuarioPorId(Integer idUsuario) {
        return usuarioRepository.findById(idUsuario);
    }

    @Override
    public Optional<Usuario> buscarUsuarioPorNombreUsuario(String nombreUsuario) {
        return usuarioRepository.findByNombreUsuario(nombreUsuario);
    } // <-- Implementación del nuevo método

    @Override
    public void guardarUsuario(Usuario usuario) {
        usuarioRepository.save(usuario);
    }

    @Override
    public void eliminarUsuario(Usuario usuario) {
        usuarioRepository.delete(usuario);
    }
}