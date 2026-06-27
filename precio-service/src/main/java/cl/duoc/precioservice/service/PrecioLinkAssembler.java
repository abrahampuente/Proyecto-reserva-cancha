package cl.duoc.precioservice.service;

import cl.duoc.precioservice.controller.PrecioController;
import cl.duoc.precioservice.dto.PrecioResponse;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class PrecioLinkAssembler {

    public EntityModel<PrecioResponse> toModel(PrecioResponse response) {
        return EntityModel.of(response,
                linkTo(methodOn(PrecioController.class).getById(response.getId())).withSelfRel(),
                linkTo(methodOn(PrecioController.class).getAll()).withRel("precios"),
                linkTo(methodOn(PrecioController.class).delete(response.getId())).withRel("desactivar"));
    }

    public CollectionModel<EntityModel<PrecioResponse>> toCollectionModel(List<PrecioResponse> responses) {
        List<EntityModel<PrecioResponse>> precios = responses.stream()
                .map(this::toModel)
                .collect(Collectors.toList());

        return CollectionModel.of(precios,
                linkTo(methodOn(PrecioController.class).getAll()).withSelfRel());
    }
}
