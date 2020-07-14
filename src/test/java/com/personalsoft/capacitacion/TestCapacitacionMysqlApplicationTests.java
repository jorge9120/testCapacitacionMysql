package com.personalsoft.capacitacion;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import java.io.UnsupportedEncodingException;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.personalsoft.capacitacion.controller.Controller;
import com.personalsoft.capacitacion.model.dto.UserDto;
import com.personalsoft.capacitacion.model.entity.UserEntity;
import com.personalsoft.capacitacion.repository.UserRepository;
import com.personalsoft.capacitacion.service.UserService;
import static org.mockito.ArgumentMatchers.any;
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestCapacitacionMysqlApplication.class)
///Clases a testear
@WebMvcTest({Controller.class,UserService.class,UserRepository.class})
class TestCapacitacionMysqlApplicationTests {
	private static final Logger logger = LoggerFactory.getLogger(TestCapacitacionMysqlApplicationTests.class);
	
	private ObjectMapper mapper = new ObjectMapper();
	@Autowired
	Controller ctrl;
	@Autowired
	MockMvc mock;
	@Autowired
	MockMvc mvc;
	
	@MockBean
	UserRepository userRepo;
	
	UserDto  dto;
	
	@BeforeEach
	void contextLoads() {
		//ctrl.guardarAtualizar(null);}
		// Se instancia el objeto global
	UserDto  dto = UserDto.builder().nombre("jorge sanchez").correo("jorge@gmail.com").edad(19).build();
		
	}
	@Test
	void user_UT01_CreateUserSuccess_ReturnOkAndAnUser() throws Exception {
		logger.info("User_UT01_CreateUserSuccess_ReturnOkAndAnUse");
		///GIVEN (Se tienen que crear las respuestas de la db)
		 dto = UserDto.builder().nombre("jorge sanchez").correo("jorge@gmail.com").edad(19).build();
		//// prueba fallida dto.setEdad(16);
		UserEntity userEntityRepo =  UserEntity.builder().nombre("jorge sanchez").id(1).correo("jorge@gmail.com").edad(26).build();
		
		///Se hace la prueba de que es lo que se debe enviar y lo que se debe retornar
		//when(userRepo.save(userEntityRequest)).thenReturn(userEntityRepo);
		when(userRepo.save(any(UserEntity.class))).thenReturn(userEntityRepo);
		
		///WHEN
		//UserEntity userEntityResponse = ctrl.guardarAtualizar(dto);
		MvcResult mvcResult =getResult(dto);
		String res = mvcResult.getResponse().getContentAsString();
		UserEntity userEntityResponse = mapper.readValue(res, UserEntity.class);
		
		///THEN
		assertEquals(dto.getNombre(), userEntityResponse.getNombre());
		assertEquals(dto.getCorreo(), userEntityResponse.getCorreo());
		assertTrue(dto.getEdad()>=18);
		assertNotNull(userEntityResponse.getId());
	}
	
	@Test
	void user_UT02_UpdateUserSuccess_AgeBD_menor_25() throws Exception {
		logger.info("user_UT02_UpdateUserSuccess_ReturnERROR_User");
		int id=5;
		// GIVEN
		UserEntity entityResponse = UserEntity.builder().id(id).nombre("nuevojuan2").edad(25).correo("nuevojuan@email2.com").build();
		UserEntity entityResponseBD = UserEntity.builder().id(id).nombre("nuevojuan").edad(18).correo("nuevojuan@email.com").build();
		dto = UserDto.builder().nombre("nuevojuan2").correo("jorge@gmail.com").edad(30).build();
		
		
		when(userRepo.save(any(UserEntity.class))).thenReturn(entityResponse);
		when(userRepo.findById(any(Integer.class))).thenReturn(Optional.of(entityResponseBD));
		
		// WHEN
		MvcResult jsonResponse = getResultPut(dto, id);
		String userJson = jsonResponse.getResponse().getContentAsString();
		UserEntity entity = mapper.readValue(userJson, UserEntity.class);

		// THEN		
		// validando lo que hay en BD
		assertNotNull(entityResponseBD);				// valida que la rta de la consulta a BD sea diferente de null
		assertFalse(entityResponseBD.getEdad()>=25);		// valida que la edad en BD no sea mayor a 25
		
		//assertNotEquals(dto.getNombre(), entityResponseBD.getNombre());	
		//assertNotEquals(dto.getEdad(),  entityResponseBD.getEdad());	
						
				
	}
	
	
	/// convertir el dto a json
	private MvcResult getResult(UserDto requestObject) throws Exception {
		String json = mapper.writeValueAsString(requestObject);

		return this.mvc.perform(post("/guardarActualizar")
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(json))
			.andReturn();
	}
	
	private MvcResult getResultPut(UserDto requestObject, Integer id) throws Exception {
		String json = mapper.writeValueAsString(requestObject);
		
		StringBuilder url = new StringBuilder("/");
		url.append(id);
		
		return this.mock.perform(put(url.toString())
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(json))
			.andReturn();
	}

}
