package com.app.controllers;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin("http://localhost:4200")
@RequestMapping("/auth")
public class TestAuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Map<String, String> credentials) {
        String username = credentials.get("username");
        String password = credentials.get("password");
        System.out.println(username + password);
        

        // Crear un objeto de autenticación con el nombre de usuario y la contraseña recibidos
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(username, password));

        // Establecer el objeto de autenticación en el contexto de seguridad
        SecurityContextHolder.getContext().setAuthentication(authentication);

        Map<String,String> response = new HashMap<>();
        response.put("message", "Autenticación exitosa");
        // Si se llega a este punto, la autenticación ha sido exitosa
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/get")
    @PreAuthorize("hasAuthority('READ')")
    public ResponseEntity hello(){
        Map<String,String> response = new HashMap<>();
        response.put("message", "hello with GET");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PreAuthorize("hasAuthority('CREATE')")
    @PostMapping("/create")
    public ResponseEntity<String> createResource() {
        return ResponseEntity.ok("Recurso creado");
    }

    @PreAuthorize("hasAuthority('READ')")
    @PostMapping("/read")
    public ResponseEntity<String> readResource() {
        return ResponseEntity.ok("Recurso leído");
    }

    @PreAuthorize("hasAuthority('UPDATE')")
    @PostMapping("/update")
    public ResponseEntity<String> updateResource() {
        return ResponseEntity.ok("Recurso actualizado");
    }

    @PreAuthorize("hasAuthority('DELETE')")
    @PostMapping("/delete")
    public ResponseEntity<String> deleteResource() {
        return ResponseEntity.ok("Recurso eliminado");
    }

    @PreAuthorize("hasAuthority('REFACTOR')")
    @PostMapping("/refactor")
    public ResponseEntity<String> refactorResource() {
        return ResponseEntity.ok("Recurso refactorizado");
    }
}

