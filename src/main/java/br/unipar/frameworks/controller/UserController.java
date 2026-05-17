package br.unipar.frameworks.controller;

import br.unipar.frameworks.dto.UserResponse;
import br.unipar.frameworks.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

/**
 * Sprint 02 - Fase 2, item 5/6:
 * endpoint de injeção JPQL removido; única busca usa parâmetro seguro; retorno via UserResponse.
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    public List<UserResponse> listUsers() {
        return userRepository.findAll().stream().map(UserResponse::from).toList();
    }

    @GetMapping("/{id}")
    public UserResponse getUser(@PathVariable Long id) {
        return userRepository.findById(id)
                .map(UserResponse::from)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuário não encontrado"));
    }

    @GetMapping("/search")
    public List<UserResponse> search(@RequestParam String term) {
        return userRepository.safeSearchByName(term).stream().map(UserResponse::from).toList();
    }
}
