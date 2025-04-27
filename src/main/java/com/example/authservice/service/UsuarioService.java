package com.example.authservice.service;

import com.example.authservice.model.Usuario;
import com.example.authservice.repository.UsuarioRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final BCryptPasswordEncoder passwordEncoder;// = new BCryptPasswordEncoder();

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public Usuario registrarUsuario(Usuario usuario) {
        //usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));
        //return usuarioRepository.save(usuario);
        if (usuarioRepository.findByEmail(usuario.getEmail()).isPresent()) {
            return null;  // Si el usuario ya existe con ese email, no lo registra
        }
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword())); // Encriptación de la contraseña
        return usuarioRepository.save(usuario); // Guardamos el usuario
    }

    public boolean autenticarUsuario(String email, String password) {
        Optional<Usuario> usuarioOpt = usuarioRepository.findByEmail(email);
        return usuarioOpt.isPresent() && passwordEncoder.matches(password, usuarioOpt.get().getPassword());
    }
}
