package br.com.fiap.pizzaria.domain.dto.response;

import lombok.Builder;

import java.math.BigDecimal;
import java.util.Collection;

@Builder
public record ProdutoResponse(

        Collection<OpcionalResponse> opcionais,
        BigDecimal preco,
        Long id,
        SaborResponse sabor,
        String nome



) {
}
