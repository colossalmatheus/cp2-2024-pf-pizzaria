package br.com.fiap.pizzaria.domain.resource;

import br.com.fiap.pizzaria.domain.dto.response.SaborResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collection;

public interface ResourceDTO<Request, Response> {


    ResponseEntity<Response> findById(Long id);

    ResponseEntity<Response> save(Request r);
}
