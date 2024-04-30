package br.com.fiap.pizzaria.domain.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record OpcionalRequest(

        @NotNull(message = "O opcinal precisa de nome")
        String nome,

        @Valid
        @NotNull(message = "O opcional precisa de sabor")
        AbstractRequest sabor,

        @NotNull(message = "O opcioanl precisa de preco")
        BigDecimal preco

) {
}
