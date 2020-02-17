package com.chapumix.solution.app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import com.chapumix.solution.app.models.service.JpaComUsuarioDetalleService;

@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Autowired
	private JpaComUsuarioDetalleService comUsuarioDetalleService;
	
	
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests().antMatchers("/", "/dist/**", "/plugins/**").permitAll() //rutas permitidas
		.antMatchers("/indexpatologia/**").hasAnyRole("ADMIN", "PATOLOGIA") //este me permite solo visualizar la ruta a quienes tengan el role ADMIN y PATOLOGIA, se puede usar con mas rutas
		.antMatchers("/procedimientopatologiainterno/**").hasAnyRole("ADMIN", "PATOLOGIA") //este me permite solo visualizar la ruta a quienes tengan el role ADMIN y PATOLOGIA, se puede usar con mas rutas
		.antMatchers("/procedimientopatologiaexterno/**").hasAnyRole("ADMIN", "PATOLOGIA") //este me permite solo visualizar la ruta a quienes tengan el role ADMIN y PATOLOGIA, se puede usar con mas rutas
		.antMatchers("/procedimientopatologiageneral/**").hasAnyRole("ADMIN", "PATOLOGIA") //este me permite solo visualizar la ruta a quienes tengan el role ADMIN y PATOLOGIA, se puede usar con mas rutas
		.antMatchers("/procesarpatologiaexterno/**").hasAnyRole("ADMIN", "PATOLOGIA") //este me permite solo visualizar la ruta a quienes tengan el role ADMIN y PATOLOGIA, se puede usar con mas rutas
		.antMatchers("/procesarpatologiainterno/**").hasAnyRole("ADMIN", "PATOLOGIA") //este me permite solo visualizar la ruta a quienes tengan el role ADMIN y PATOLOGIA, se puede usar con mas rutas
		.antMatchers("/procedimientopatologiageneral/**").hasAnyRole("ADMIN", "PATOLOGIA") //este me permite solo visualizar la ruta a quienes tengan el role ADMIN y PATOLOGIA, se puede usar con mas rutas
		.antMatchers("/editarpatologiaprocesada/**").hasAnyRole("ADMIN", "PATOLOGIA") //este me permite solo visualizar la ruta a quienes tengan el role ADMIN y PATOLOGIA, se puede usar con mas rutas
		.antMatchers("/eliminarpatologiaprocesada/**").hasAnyRole("ADMIN") //este me permite solo visualizar la ruta a quienes tengan el role ADMIN, se puede usar con mas rutas
		.antMatchers("/parametropatologia/**").hasAnyRole("ADMIN") //este me permite solo visualizar la ruta a quienes tengan el role ADMIN, se puede usar con mas rutas
		.anyRequest().authenticated()
		.and()
			.formLogin()
			.loginPage("/login")
			.defaultSuccessUrl("/index")
			.permitAll()
		.and()
			.rememberMe().key("uniqueAndSecret").rememberMeParameter("remember-me").tokenValiditySeconds(1440)//el cookie durara solo un dia y se llamara remember-me
		.and()
			.logout()
			.permitAll()
			.deleteCookies("JSESSIONID")
		.and()
		.exceptionHandling().accessDeniedPage("/error_403");
		
		
	}

	//metodo para los usuarios en base de datos
	@Autowired
	public void cofigurerGlobal(AuthenticationManagerBuilder build) throws Exception {
		
		build.userDetailsService(comUsuarioDetalleService).passwordEncoder(passwordEncoder);		
	}

}
