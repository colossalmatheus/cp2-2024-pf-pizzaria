package br.com.fiap.pizzaria.domain.dto.response;

import java.util.Collection;

public record PizzariaResponse(
        Long id,
        String nome,
        Collection<ProdutoResponse> cardapio
) {
}
