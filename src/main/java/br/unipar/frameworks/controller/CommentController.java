package br.unipar.frameworks.controller;

import br.unipar.frameworks.dto.CommentRequest;
import br.unipar.frameworks.dto.CommentResponse;
import br.unipar.frameworks.model.Comment;
import br.unipar.frameworks.model.Product;
import br.unipar.frameworks.repository.CommentRepository;
import br.unipar.frameworks.repository.ProductRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

/**
 * Sprint 02 - Fase 2, item 6 e Stored XSS:
 * Bean Validation no request; texto escapado com HtmlUtils antes de persistir; retorno via CommentResponse.
 */
@RestController
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentRepository commentRepository;
    private final ProductRepository productRepository;

    public CommentController(CommentRepository commentRepository, ProductRepository productRepository) {
        this.commentRepository = commentRepository;
        this.productRepository = productRepository;
    }

    @GetMapping("/product/{productId}")
    public List<CommentResponse> listByProduct(@PathVariable Long productId) {
        return commentRepository.findByProductId(productId)
                .stream().map(CommentResponse::from).toList();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommentResponse create(@Valid @RequestBody CommentRequest request) {
        Product product = productRepository.findById(request.productId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Produto não encontrado"));
        Comment comment = new Comment();
        comment.setText(HtmlUtils.htmlEscape(request.text()));
        comment.setProduct(product);
        return CommentResponse.from(commentRepository.save(comment));
    }
}
