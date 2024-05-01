package colegio.comedor.interfaceService;

import java.util.List;
import java.util.Optional;

import colegio.comedor.modelo.User;

public interface IUserService {

	public List<User>listar();
	public Optional<User> listarId(String id);
	public void save(User e);
	public void delete(String id);
	public Optional<User> findByUsername(String username);
	
}
