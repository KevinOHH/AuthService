package com.example.authservice.service;

import com.example.authservice.model.Usuario;
import com.example.authservice.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    private BCryptPasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        passwordEncoder = new BCryptPasswordEncoder();
    }

    @Test
    void registrarUsuario_deberiaGuardarUsuarioConPasswordEncriptado() {
        // Arrange
        Usuario usuario = new Usuario();
        usuario.setNombre("Juan Pérez");
        usuario.setEmail("juan@example.com");
        usuario.setPassword("123456");

        when(usuarioRepository.save(any(Usuario.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Usuario guardado = usuarioService.registrarUsuario(usuario);

        // Assert
        assertNotNull(guardado);
        assertNotEquals("123456", guardado.getPassword()); // No debe ser texto plano
        assertTrue(guardado.getPassword().startsWith("$2a$")); // BCrypt siempre empieza así
    }

    @Test
    void autenticarUsuario_conCredencialesValidas_deberiaRetornarTrue() {
        // Arrange
        Usuario usuario = new Usuario();
        usuario.setEmail("juan@example.com");
        usuario.setPassword(passwordEncoder.encode("123456"));

        when(usuarioRepository.findByEmail("juan@example.com")).thenReturn(Optional.of(usuario));

        // Act
        boolean resultado = usuarioService.autenticarUsuario("juan@example.com", "123456");

        // Assert
        assertTrue(resultado);
    }

    @Test
    void autenticarUsuario_conCredencialesInvalidas_deberiaRetornarFalse() {
        // Arrange
        when(usuarioRepository.findByEmail("noexiste@example.com")).thenReturn(Optional.empty());

        // Act
        boolean resultado = usuarioService.autenticarUsuario("noexiste@example.com", "cualquierpassword");

        // Assert
        assertFalse(resultado);
    }
}