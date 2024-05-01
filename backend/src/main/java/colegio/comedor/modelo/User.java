package colegio.comedor.modelo;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import colegio.comedor.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="user",uniqueConstraints= {@UniqueConstraint(columnNames= {"username"})})
public class User implements UserDetails, Serializable {
	 private static final long serialVersionUID = 1L;
	
	@Id
 @GeneratedValue
 Integer Id;
 @Column(nullable=false)
 String username;
 String lastname;
 String firstname;
 String country;
 String password;
 @Enumerated(EnumType.STRING)
 Role role;
 
 
@Override
public Collection<? extends GrantedAuthority> getAuthorities() {
	// TODO Auto-generated method stub
	return List.of(new SimpleGrantedAuthority(role.name()));
}
@Override
public String getPassword() {
	// TODO Auto-generated method stub
	return this.password;
}
@Override
public String getUsername() {
	// TODO Auto-generated method stub
	return this.username;
}
@Override
public boolean isAccountNonExpired() {
	// TODO Auto-generated method stub
	return true;
}
@Override
public boolean isAccountNonLocked() {
	// TODO Auto-generated method stub
	return true;
}
@Override
public boolean isCredentialsNonExpired() {
	// TODO Auto-generated method stub
	return true;
}
@Override
public boolean isEnabled() {
	// TODO Auto-generated method stub
	return true;
}
}
