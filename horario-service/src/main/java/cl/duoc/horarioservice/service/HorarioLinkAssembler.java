package cl.duoc.horarioservice.service;

import cl.duoc.horarioservice.controller.HorarioController;
import cl.duoc.horarioservice.dto.HorarioResponse;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class HorarioLinkAssembler {

    public EntityModel<HorarioResponse> toModel(HorarioResponse horario) {
        EntityModel<HorarioResponse> model = EntityModel.of(horario);

        model.add(linkTo(methodOn(HorarioController.class)
                .getById(horario.getId())).withSelfRel());

        model.add(linkTo(methodOn(HorarioController.class)
                .getAll()).withRel("all"));

        model.add(linkTo(methodOn(HorarioController.class)
                .update(horario.getId(), null)).withRel("update"));

        model.add(linkTo(methodOn(HorarioController.class)
                .delete(horario.getId())).withRel("delete"));

        model.add(linkTo(methodOn(HorarioController.class)
                .existsById(horario.getId())).withRel("exists"));

        return model;
    }
}