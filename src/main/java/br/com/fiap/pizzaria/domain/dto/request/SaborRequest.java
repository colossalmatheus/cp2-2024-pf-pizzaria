package br.com.fiap.pizzaria.domain.dto.request;

import jakarta.validation.constraints.NotNull;

public record SaborRequest(

        @NotNull(message = "Sabor precisa de uma descricao")
        String descricao,

        @NotNull(message = "Sabor precisa de nome")
        String nome
) {
}
