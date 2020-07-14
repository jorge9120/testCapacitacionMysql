package com.personalsoft.capacitacion.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.personalsoft.capacitacion.model.dto.UserDto;
import com.personalsoft.capacitacion.model.entity.UserEntity;
import com.personalsoft.capacitacion.service.UserService;

@RestController
public class Controller {
	private static final Logger logger = LoggerFactory.getLogger(Controller.class);
	
	@Autowired
	UserService userService;
	@GetMapping("listar")
	public ResponseEntity<List<UserEntity>> getAll(){
		return ResponseEntity.ok(userService.list());
	}
	@GetMapping("/buscar/{id}")
	public ResponseEntity<UserEntity> buscarId(@PathVariable int id){
		UserEntity userEntity = userService.obtenerPorId(id).get(); 
		
		return new ResponseEntity<UserEntity>(userEntity,HttpStatus.OK);
	}
	@PostMapping("guardarActualizar")
	public UserEntity guardarAtualizar(@Valid @RequestBody UserDto userDto ){
		/*return new ResponseEntity("Creado",HttpStatus.CREATED);
		logger.info("Creating new user");
		logger.warn("Creating new user warning");
		logger.error("Creating new user error");
		logger.info("guardarAtualizar(): Email: {}, Name: {}", userDto.getCorreo(), userDto.getNombre());
		logger.info("guardarAtualizar(): {}", userDto);
		Crear un objeto con los atributos deseados
		UserDto dto =  UserDto.builder().correo("").nombre("").build();*/
		return userService.guardarModificar(userDto);

	}
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
		Map<String, String> errors = new HashMap<>();
		ex.getBindingResult().getAllErrors().forEach((error) -> {
			String fieldName = ((FieldError) error).getField();
			String errorMessage = error.getDefaultMessage();
			errors.put(fieldName, errorMessage);
		});
		return errors;
	}
	@PutMapping ("/{id}")
	public UserEntity updateUser(
		@RequestBody UserDto user, 
			@PathVariable int id) {
	return userService.update(user, id);
	}
	
}
