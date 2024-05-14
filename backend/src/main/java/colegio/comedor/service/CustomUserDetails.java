package colegio.comedor.service;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import colegio.comedor.modelo.User;

//clase interna que maneja la logica de la informacion del usuario
public class CustomUserDetails implements UserDetails {

	private User user;
	
	  public CustomUserDetails(User user) {
	        this.user = user;
	    }
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		 return user.getAuthorities();
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		 return user.getPassword();
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		  return user.getUsername();
	}
	  public Integer getId() {
	        return user.getId();
	    }

	  @Override
	  public boolean isAccountNonExpired() {
	      // Aquí puedes implementar la lógica para verificar si la cuenta del usuario ha expirado
	      // Devuelve true si la cuenta no ha expirado, de lo contrario, devuelve false
	      return true; // Cambia esto según tu lógica de negocio
	  }

	  @Override
	  public boolean isAccountNonLocked() {
	      // Aquí puedes implementar la lógica para verificar si la cuenta del usuario está bloqueada
	      // Devuelve true si la cuenta no está bloqueada, de lo contrario, devuelve false
	      return true; // Cambia esto según tu lógica de negocio
	  }

	  @Override
	  public boolean isCredentialsNonExpired() {
	      // Aquí puedes implementar la lógica para verificar si las credenciales del usuario han expirado
	      // Devuelve true si las credenciales no han expirado, de lo contrario, devuelve false
	      return true; // Cambia esto según tu lógica de negocio
	  }
	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return false;
	}

	
}
