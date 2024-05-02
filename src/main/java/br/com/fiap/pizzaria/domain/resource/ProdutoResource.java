package br.com.fiap.pizzaria.domain.resource;

import br.com.fiap.pizzaria.domain.dto.request.OpcionalRequest;
import br.com.fiap.pizzaria.domain.dto.request.PizzariaRequest;
import br.com.fiap.pizzaria.domain.dto.request.ProdutoRequest;
import br.com.fiap.pizzaria.domain.dto.response.PizzariaResponse;
import br.com.fiap.pizzaria.domain.dto.response.ProdutoResponse;
import br.com.fiap.pizzaria.domain.dto.response.SaborResponse;
import br.com.fiap.pizzaria.domain.entity.Pizzaria;
import br.com.fiap.pizzaria.domain.entity.Produto;
import br.com.fiap.pizzaria.domain.repository.OpcionalRepository;
import br.com.fiap.pizzaria.domain.service.OpcionalService;
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
import java.util.Objects;
import java.util.Set;

@RestController
@RequestMapping(value = "/produtos")
public class ProdutoResource{


    @Autowired
    ProdutoService service;

    @Autowired
    private OpcionalService opcionalService;


    @Autowired
    private OpcionalRepository opcionalRepository;
    @GetMapping
    public ResponseEntity<Collection<ProdutoResponse>> findAll(
            @RequestParam(name = "nome", required = false) final String nome,
            @RequestParam(name = "preco", required = false) final BigDecimal preco,
            @RequestParam(name = "sabor", required = false) final Long idSabor,
            @RequestParam(name = "opcional", required = false) final Long idOpcional
    ) {
        var produto = Produto.builder()
                .nome(nome)
                .preco(preco)
                .id(idSabor)
                .id(idOpcional)
                .build();

        ExampleMatcher matcher = ExampleMatcher
                .matchingAll()
                .withIgnoreNullValues()
                .withIgnoreCase();

        Example<Produto> example = Example.of(produto, matcher);

        var entity = service.findAll(example);

        var response = entity.stream().map(service::toResponse).toList();

        return ResponseEntity.ok(response);
    }


    @GetMapping(value = "/{id}")
    public ResponseEntity<ProdutoResponse> findById(@PathVariable final Long id) {
        var entity = service.findById(id);
        if (Objects.isNull(entity)) return ResponseEntity.notFound().build();
        var response = service.toResponse(entity);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/opcionais")
    public ResponseEntity<ProdutoResponse> getOpcional(@PathVariable Long id) {
        var entity = service.findById(id);
        if (Objects.isNull(entity)) return ResponseEntity.notFound().build();
        var response = service.toResponse(entity);
        return ResponseEntity.ok(response);
    }
    @Transactional
    @PostMapping
    public ResponseEntity<ProdutoResponse> save(@RequestBody @Valid ProdutoRequest dto) {
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
    @PostMapping("/{id}/opcionais")
    public ResponseEntity<ProdutoResponse> addOptionalToProduct(@PathVariable Long id, @RequestBody @Valid OpcionalRequest opcionalRequest) {
        var produto = service.findById(id);
        var opcional = opcionalService.toEntity(opcionalRequest);
        var savedOpcional = opcionalService.save(opcional);

        produto.getOpcionais().add(savedOpcional);
        var updatedProduto = service.save(produto);

        var response = service.toResponse(updatedProduto);
        return ResponseEntity.ok(response);
    }



}
