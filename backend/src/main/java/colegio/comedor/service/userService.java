package colegio.comedor.service;

import java.util.List;
import java.util.Optional;

import javax.ws.rs.container.Suspended;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import colegio.comedor.interfaceService.IUserService; // Asegúrate de importar IUserService en lugar de InterfazUser
import colegio.comedor.interfaces.InterfazUser;
import colegio.comedor.modelo.User;

@Service	
public class userService implements IUserService {
	@Autowired
	private  InterfazUser userRepository;
	@Override
	public List<User> listar() {
		  return (List<User>) userRepository.findAll();
	}
	@Override
	public Optional<User> listarId(String id) {
		// TODO Auto-generated method stub
		return userRepository.findById(id);

	}
	@Override
	public void save(User e) {
		userRepository.save(e);
		
	}
	@Override
	public void delete(String id) {
		userRepository.deleteById(id);		
	}

	@Override
	public Optional<User> findByUsername(String username) {
		 
		Optional<User> userOptional=userRepository.findByUsername(username);
		   if (username == null || username.isEmpty()) {
	            throw new IllegalArgumentException("El nombre de usuario no puede estar vacío");
	        }
	        
		if (userOptional.isPresent()) {
	            // Si se encontró el usuario, devuélvelo
			 System.out.println("Usuario encontrado en la base de datos: " + userOptional.toString());
	            return userOptional;
	        } else {
	            // Si no se encontró el usuario, lanza una excepción o devuelve un Optional vacío, según tu implementación
	            // Puedes lanzar una excepción UsernameNotFoundException o devolver Optional.empty() dependiendo de cómo quieras manejarlo
	            // throw new UsernameNotFoundException("Usuario no encontrado con el nombre de usuario: " + username);
	            return Optional.empty();
	        }
      
	}
	@Override
	public Optional<User> buscarUsuario(double id) {
		// TODO Auto-generated method stub
		 return userRepository.findById(String.valueOf((int) id));
	}

}
