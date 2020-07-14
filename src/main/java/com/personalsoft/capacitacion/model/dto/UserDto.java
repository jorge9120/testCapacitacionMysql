package com.personalsoft.capacitacion.model.dto;

import javax.validation.constraints.Min;

import com.sun.istack.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Generated;


@Data
@Builder
@AllArgsConstructor
@Generated
public class UserDto {
	private Long id;
	@NotNull
	private String nombre;
	@NotNull
	private String correo;
	@NotNull
	@Min(18)
	private int edad;
}
