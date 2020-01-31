package com.chapumix.solution.app.custom;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

//clase personalizada que sobre escribe la clase USER
public class CustomUserDetails extends User {

	private static final long serialVersionUID = 1L;

	private String nombreCompleto;
	private String nombreCorto;

	public CustomUserDetails(String username, String password, String nombreCompleto, String nombreCorto, boolean enabled, boolean accountNonExpired,
			boolean credentialsNonExpired, boolean accountNonLocked,
			Collection<GrantedAuthority> authorities) {
		super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
		this.nombreCompleto = nombreCompleto;
		this.nombreCorto = nombreCorto;
	}

	public String getNombreCompleto() {
		return nombreCompleto;
	}

	public void setNombreCompleto(String nombreCompleto) {
		this.nombreCompleto = nombreCompleto;
	}

	public String getNombreCorto() {
		return nombreCorto;
	}

	public void setNombreCorto(String nombreCorto) {
		this.nombreCorto = nombreCorto;
	}	

}