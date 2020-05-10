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
		.antMatchers("/indexprocesar/**").hasAnyRole("ADMIN", "PATOLOGIA") //este me permite solo visualizar la ruta a quienes tengan el role ADMIN y PATOLOGIA, se puede usar con mas rutas
		.antMatchers("/procedimientopatologia/**").hasAnyRole("ADMIN", "PATOLOGIA") //este me permite solo visualizar la ruta a quienes tengan el role ADMIN y PATOLOGIA, se puede usar con mas rutas
		.antMatchers("/procedimientopatologiageneral/**").hasAnyRole("ADMIN", "PATOLOGIA") //este me permite solo visualizar la ruta a quienes tengan el role ADMIN y PATOLOGIA, se puede usar con mas rutas
		.antMatchers("/procesarpatologia/**").hasAnyRole("ADMIN", "PATOLOGIA") //este me permite solo visualizar la ruta a quienes tengan el role ADMIN y PATOLOGIA, se puede usar con mas rutas
		.antMatchers("/procedimientopatologiageneral/**").hasAnyRole("ADMIN", "PATOLOGIA") //este me permite solo visualizar la ruta a quienes tengan el role ADMIN y PATOLOGIA, se puede usar con mas rutas
		.antMatchers("/editarpatologiaprocesada/**").hasAnyRole("ADMIN", "PATOLOGIA") //este me permite solo visualizar la ruta a quienes tengan el role ADMIN y PATOLOGIA, se puede usar con mas rutas
		.antMatchers("/eliminarpatologiaprocesada/**").hasAnyRole("ADMIN") //este me permite solo visualizar la ruta a quienes tengan el role ADMIN, se puede usar con mas rutas
		.antMatchers("/usuarioform/**").hasAnyRole("ADMIN") //este me permite solo visualizar la ruta a quienes tengan el role ADMIN, se puede usar con mas rutas
		.antMatchers("/usuariolistado/**").hasAnyRole("ADMIN") //este me permite solo visualizar la ruta a quienes tengan el role ADMIN, se puede usar con mas rutas
		.antMatchers("/rolform/**").hasAnyRole("ADMIN") //este me permite solo visualizar la ruta a quienes tengan el role ADMIN, se puede usar con mas rutas
		.antMatchers("/rollistado/**").hasAnyRole("ADMIN") //este me permite solo visualizar la ruta a quienes tengan el role ADMIN, se puede usar con mas rutas
		.antMatchers("/indexcalidad/**").hasAnyRole("ADMIN", "CALIDAD") //este me permite solo visualizar la ruta a quienes tengan el role ADMIN y CALIDAD, se puede usar con mas rutas
		.antMatchers("/indexcalendario/**").hasAnyRole("ADMIN", "CALIDAD") //este me permite solo visualizar la ruta a quienes tengan el role ADMIN y CALIDAD, se puede usar con mas rutas
		.antMatchers("/empleadocumple/**").hasAnyRole("ADMIN", "CALIDAD") //este me permite solo visualizar la ruta a quienes tengan el role ADMIN y CALIDAD, se puede usar con mas rutas
		.antMatchers("/cumplecarform/**").hasAnyRole("ADMIN", "CALIDAD") //este me permite solo visualizar la ruta a quienes tengan el role ADMIN y CALIDAD, se puede usar con mas rutas
		.antMatchers("/indexmedicinainterna/**").hasAnyRole("ADMIN", "MEDICINA_INTERNA", "ENFERMERA") //este me permite solo visualizar la ruta a quienes tengan el role ADMIN y MEDICINA_INTERNA, se puede usar con mas rutas
		.antMatchers("/indexasignacioncamas/**").hasAnyRole("ADMIN", "MEDICINA_INTERNA", "ENFERMERA") //este me permite solo visualizar la ruta a quienes tengan el role ADMIN, MEDICINA_INTERNA, ENFERMERAS se puede usar con mas rutas
		.antMatchers("/indexestadistica/**").hasAnyRole("ADMIN", "ESTADISTICA", "ESTADISTICA_MEDICO") //este me permite solo visualizar la ruta a quienes tengan el role ADMIN, ESTADISTICA, MEDICO se puede usar con mas rutas
		.antMatchers("/indexcertificado/**").hasAnyRole("ADMIN", "ESTADISTICA", "ESTADISTICA_MEDICO") //este me permite solo visualizar la ruta a quienes tengan el role ADMIN, ESTADISTICA, MEDICO se puede usar con mas rutas
		.antMatchers("/certificadoestadistica/**").hasAnyRole("ADMIN", "ESTADISTICA", "ESTADISTICA_MEDICO") //este me permite solo visualizar la ruta a quienes tengan el role ADMIN, ESTADISTICA, MEDICO se puede usar con mas rutas
		.antMatchers("/certificadocsvform/**").hasAnyRole("ADMIN", "ESTADISTICA") //este me permite solo visualizar la ruta a quienes tengan el role ADMIN, ESTADISTICA se puede usar con mas rutas
		.antMatchers("/indexsiau/**").hasAnyRole("ADMIN", "SIAU") //este me permite solo visualizar la ruta a quienes tengan el role ADMIN, SIAU se puede usar con mas rutas
		.antMatchers("/indexencuestasat/**").hasAnyRole("ADMIN", "SIAU") //este me permite solo visualizar la ruta a quienes tengan el role ADMIN, SIAU se puede usar con mas rutas
		.antMatchers("/encuestaformt/**").hasAnyRole("ADMIN", "SIAU") //este me permite solo visualizar la ruta a quienes tengan el role ADMIN, SIAU se puede usar con mas rutas
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
