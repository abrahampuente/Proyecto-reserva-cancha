package cl.duoc.canchaservice.service;

import cl.duoc.canchaservice.controller.CanchaController;
import cl.duoc.canchaservice.dto.CanchaResponse;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class CanchaLinkAssembler {

    public EntityModel<CanchaResponse> toModel(CanchaResponse cancha) {
        EntityModel<CanchaResponse> model = EntityModel.of(cancha);

        model.add(linkTo(methodOn(CanchaController.class)
                .getById(cancha.getId())).withSelfRel());

        model.add(linkTo(methodOn(CanchaController.class)
                .getAll()).withRel("all"));

        model.add(linkTo(methodOn(CanchaController.class)
                .update(cancha.getId(), null)).withRel("update"));

        model.add(linkTo(methodOn(CanchaController.class)
                .delete(cancha.getId())).withRel("delete"));

        model.add(linkTo(methodOn(CanchaController.class)
                .existsById(cancha.getId())).withRel("exists"));

        return model;
    }
}