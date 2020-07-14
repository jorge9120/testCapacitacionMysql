package com.personalsoft.capacitacion.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.personalsoft.capacitacion.model.dto.UserDto;
import com.personalsoft.capacitacion.model.entity.UserEntity;
import com.personalsoft.capacitacion.repository.UserRepository;
@Service
public class UserService {
	
	@Autowired
	UserRepository userRepository;
	
	public List<UserEntity> list(){
		return (List<UserEntity>)userRepository.findAll();
	}
	
	public Optional<UserEntity> obtenerPorId(int Id){
		
		return userRepository.findById(Id);
	}
	
	public UserEntity guardarModificar(UserDto user) {
		UserEntity userEntity = new UserEntity();
		userEntity.setCorreo(user.getCorreo());
		userEntity.setNombre(user.getNombre());
		
		return userRepository.save(userEntity);
	}
	
	public UserEntity update(UserDto user, int id) {
		//get the user, if it doesn't exist put null
		
		UserEntity userEntity = userRepository.findById(id).orElse(null);

			if (userEntity != null && user.getEdad()>25) {
				if(userEntity.getCorreo().equals(user.getCorreo())) return null;
				if(userEntity.getNombre().equals(user.getNombre())) return null;
				if(userEntity.getEdad() == user.getEdad() ) return null;
				
				userEntity.setCorreo(user.getCorreo());
				userEntity.setNombre(user.getNombre());
				
				userRepository.save(userEntity);
					
			
		}
			return userEntity;
	}
	
	public void eliminar(int id) {
		userRepository.deleteById(id);
	}
	
	
}
