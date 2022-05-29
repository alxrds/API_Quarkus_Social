package com.alexandre.quarkussocial.rest.dtos;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class CreateUserRequest {

    @NotBlank(message = "Campo nome é obrigatório")
    private String name;

    @NotNull(message = "Campo idade não pode ser nulo")
    private Integer age;

}
