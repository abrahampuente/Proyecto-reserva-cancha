package cl.duoc.userservice.service;

import cl.duoc.userservice.controller.UserController;
import cl.duoc.userservice.dto.UserResponse;
import org.springframework.hateoas.EntityModel;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class UserLinkAssembler {

    public EntityModel<UserResponse> toModel(UserResponse user) {
        EntityModel<UserResponse> model = EntityModel.of(user);

        model.add(linkTo(methodOn(UserController.class)
                .getById(user.getId())).withSelfRel());

        model.add(linkTo(methodOn(UserController.class)
                .getAll()).withRel("all"));

        model.add(linkTo(methodOn(UserController.class)
                .update(user.getId(), null)).withRel("update"));

        model.add(linkTo(methodOn(UserController.class)
                .delete(user.getId())).withRel("delete"));

        model.add(linkTo(methodOn(UserController.class)
                .existsById(user.getId())).withRel("exists"));

        model.add(linkTo(methodOn(UserController.class)
                .getRoleById(user.getId())).withRel("role"));

        return model;
    }
}