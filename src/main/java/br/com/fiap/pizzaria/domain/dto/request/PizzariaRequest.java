package br.com.fiap.pizzaria.domain.dto.request;

import jakarta.validation.constraints.NotNull;

import java.util.Collection;

public record PizzariaRequest(

        @NotNull(message = "Pizzaria precisa de nome")
        String nome,
        Collection<AbstractRequest> cardapio
) {
}
