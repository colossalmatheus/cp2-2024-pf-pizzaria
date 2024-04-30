package br.com.fiap.pizzaria.domain.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record ProdutoRequest(

        @NotNull(message = "O produto precisa de nome")
        String nome,

        @Valid
        @NotNull(message = "O produto precisa de sabor")
        AbstractRequest sabor,

        @Positive
        @NotNull(message = "O produto precisa de preco")
        BigDecimal preco

) {
}
