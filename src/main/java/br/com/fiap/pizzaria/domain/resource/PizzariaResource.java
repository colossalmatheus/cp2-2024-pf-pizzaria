package br.com.fiap.pizzaria.domain.resource;

import br.com.fiap.pizzaria.domain.dto.request.PizzariaRequest;
import br.com.fiap.pizzaria.domain.dto.request.ProdutoRequest;
import br.com.fiap.pizzaria.domain.dto.response.PizzariaResponse;
import br.com.fiap.pizzaria.domain.dto.response.ProdutoResponse;
import br.com.fiap.pizzaria.domain.entity.Pizzaria;
import br.com.fiap.pizzaria.domain.entity.Produto;
import br.com.fiap.pizzaria.domain.service.PizzariaService;
import br.com.fiap.pizzaria.domain.service.ProdutoService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@RestController
@RequestMapping(value = "/pizzarias")
public class PizzariaResource {

    @Autowired
    PizzariaService service;

    @Autowired
    private ProdutoService produtoService;

    @GetMapping
    public ResponseEntity<Collection<PizzariaResponse>> findAll(
            @RequestParam(name = "nome", required = false) final String nome,
            @RequestParam(name = "cardapio", required = false) final Long idProduto,
            @RequestParam(name = "produto", required = false) String produtoNome
    ) {
        var pizzaria = Pizzaria.builder()
                .nome(nome)
                .id(idProduto)
                .build();

        ExampleMatcher matcher = ExampleMatcher
                .matchingAll()
                .withIgnoreNullValues()
                .withIgnoreCase();

        Example<Pizzaria> example = Example.of(pizzaria, matcher);

        var entity = service.findAll(example);

        var response = entity.stream().map(service::toResponse).toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<PizzariaResponse> findById(@PathVariable final Long id) {
        var entity = service.findById(id);
        if (Objects.isNull(entity)) return ResponseEntity.notFound().build();
        var response = service.toResponse(entity);
        return ResponseEntity.ok(response);
    }


    @GetMapping("/{id}/cardapio")
    public ResponseEntity<PizzariaResponse> getCardapio(@PathVariable Long id) {
        var entity = service.findById(id);
        if (Objects.isNull(entity)) return ResponseEntity.notFound().build();
        var response = service.toResponse(entity);
        return ResponseEntity.ok(response);
    }

    @Transactional
    @PostMapping
    public ResponseEntity<PizzariaResponse> save(@RequestBody @Valid PizzariaRequest dto) {
        var entity = service.toEntity(dto);
        var saved = service.save(entity);
        var uri = ServletUriComponentsBuilder.fromCurrentRequestUri()
                .path("/{id}")
                .buildAndExpand(saved.getId())
                .toUri();

        var response = service.toResponse(saved);

        return ResponseEntity.created(uri).body(response);
    }

    @Transactional
    @PostMapping(value = "/{id}/cardapio")
    public ResponseEntity<ProdutoResponse> saveOpcional(@PathVariable Long id, @RequestBody ProdutoRequest r){
        Pizzaria pizzaria =  service.findById(id);
        Set<Produto> cardapio = new LinkedHashSet<>();
        var entity = produtoService.toEntity(r);
        cardapio.add(entity);
        var saved = produtoService.save(entity);
        var response = produtoService.toResponse(saved);
        return ResponseEntity.ok(response);
    }
}
