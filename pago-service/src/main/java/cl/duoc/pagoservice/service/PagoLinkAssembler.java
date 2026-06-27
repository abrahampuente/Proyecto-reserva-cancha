package cl.duoc.pagoservice.service;

import cl.duoc.pagoservice.controller.PagoController;
import cl.duoc.pagoservice.dto.PagoResponse;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class PagoLinkAssembler {

    public EntityModel<PagoResponse> toModel(PagoResponse response) {
        return EntityModel.of(response,
                linkTo(methodOn(PagoController.class).getById(response.getId())).withSelfRel(),
                linkTo(methodOn(PagoController.class).getAll()).withRel("pagos"),
                linkTo(methodOn(PagoController.class).delete(response.getId())).withRel("anular"));
    }

    public CollectionModel<EntityModel<PagoResponse>> toCollectionModel(List<PagoResponse> responses) {
        List<EntityModel<PagoResponse>> pagos = responses.stream()
                .map(this::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(pagos,
                linkTo(methodOn(PagoController.class).getAll()).withSelfRel());
    }
}
