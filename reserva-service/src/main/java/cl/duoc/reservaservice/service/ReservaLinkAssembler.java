package cl.duoc.reservaservice.service;

import cl.duoc.reservaservice.controller.ReservaController;
import cl.duoc.reservaservice.dto.ReservaResponse;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class ReservaLinkAssembler {

    public EntityModel<ReservaResponse> toModel(ReservaResponse reserva) {
        EntityModel<ReservaResponse> model = EntityModel.of(reserva);

        model.add(linkTo(methodOn(ReservaController.class)
                .getById(reserva.getId())).withSelfRel());

        model.add(linkTo(methodOn(ReservaController.class)
                .getAll()).withRel("all"));

        model.add(linkTo(methodOn(ReservaController.class)
                .update(reserva.getId(), null)).withRel("update"));

        model.add(linkTo(methodOn(ReservaController.class)
                .delete(reserva.getId())).withRel("delete"));

        return model;
    }
}