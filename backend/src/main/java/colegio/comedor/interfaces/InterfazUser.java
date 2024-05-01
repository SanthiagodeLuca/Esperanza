	package colegio.comedor.interfaces;
	
	import java.util.List;
	import java.util.Optional;
	
	import org.springframework.data.repository.CrudRepository;
	import org.springframework.stereotype.Repository;
	
	import colegio.comedor.modelo.User;
	
	@Repository
	public interface InterfazUser extends CrudRepository<User,String> {
	
	  Optional<User> findByUsername(String username); // Agrega este método
 	
	
		
	
	}
