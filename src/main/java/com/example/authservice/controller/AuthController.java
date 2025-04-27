package com.example.authservice.controller;

import com.example.authservice.model.Usuario;
import com.example.authservice.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UsuarioService usuarioService;

    public AuthController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @PostMapping("/register")
    public ResponseEntity<Usuario> register(@RequestBody Usuario usuario) {
        //return ResponseEntity.ok(usuarioService.registrarUsuario(usuario));
        System.out.println("Petición POST /register recibida");
        Usuario usuarioRegistrado = usuarioService.registrarUsuario(usuario);
        System.out.println("Usuario guardado: " + usuarioRegistrado);
        if (usuarioRegistrado != null) {
            return ResponseEntity.ok(usuarioRegistrado); // Devuelve el usuario registrado
        } else {
            return ResponseEntity.status(500).body(null); // Error si no se guarda
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String email, @RequestParam String password) {
        boolean autenticado = usuarioService.autenticarUsuario(email, password);
        if (autenticado) {
            return ResponseEntity.ok("Login exitoso");
        } else {
            return ResponseEntity.status(401).body("Credenciales inválidas");
        }
    }
}
