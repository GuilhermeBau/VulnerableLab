package br.unipar.frameworks.controller;

import br.unipar.frameworks.dto.UserResponse;
import br.unipar.frameworks.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * Sprint 02 - Fase 1, item 2: controle de acesso real via @PreAuthorize (defesa em profundidade);
 * retorno via UserResponse; 404 explícito antes de deletar.
 */
@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final UserRepository userRepository;

    public AdminController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/users")
    public List<UserResponse> adminUsers() {
        return userRepository.findAll().stream().map(UserResponse::from).toList();
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        if (!userRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado");
        }
        userRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
