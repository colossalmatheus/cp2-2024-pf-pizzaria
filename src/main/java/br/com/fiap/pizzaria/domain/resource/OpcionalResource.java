package br.com.fiap.pizzaria.domain.resource;


import br.com.fiap.pizzaria.domain.dto.request.OpcionalRequest;
import br.com.fiap.pizzaria.domain.dto.request.SaborRequest;
import br.com.fiap.pizzaria.domain.dto.response.OpcionalResponse;
import br.com.fiap.pizzaria.domain.dto.response.SaborResponse;
import br.com.fiap.pizzaria.domain.entity.Opcional;
import br.com.fiap.pizzaria.domain.entity.Sabor;
import br.com.fiap.pizzaria.domain.service.OpcionalService;
import br.com.fiap.pizzaria.domain.service.SaborService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Collection;
import java.util.Objects;

@RestController
@RequestMapping(value = "/opcionais")
public class OpcionalResource {

    @Autowired
    OpcionalService service;

    @GetMapping
    public ResponseEntity<Collection<OpcionalResponse>> findAll(
            @RequestParam(name = "nome", required = false) final String nome
    ) {
        var opcional = Opcional.builder()
                .nome(nome)
                .build();

        ExampleMatcher matcher = ExampleMatcher
                .matchingAll()
                .withIgnoreNullValues()
                .withIgnoreCase();

        Example<Opcional> example = Example.of(opcional, matcher);

        var entity = service.findAll(example);

        var response = entity.stream().map(service::toResponse).toList();

        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<OpcionalResponse> findById(@PathVariable final Long id) {
        var entity = service.findById(id);
        if (Objects.isNull(entity)) return ResponseEntity.notFound().build();
        var response = service.toResponse(entity);
        return ResponseEntity.ok(response);
    }


    @Transactional
    @PostMapping
    public ResponseEntity<OpcionalResponse> save(@RequestBody @Valid OpcionalRequest dto) {
        var entity = service.toEntity(dto);
        var saved = service.save(entity);
        var uri = ServletUriComponentsBuilder.fromCurrentRequestUri()
                .path("/{id}")
                .buildAndExpand(saved.getId())
                .toUri();

        var response = service.toResponse(saved);

        return ResponseEntity.created(uri).body(response);
    }
}
