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
		//usuarios administradores
		.antMatchers("/usuarioform/**").hasAnyRole("ADMIN") 
		.antMatchers("/usuariolistado/**").hasAnyRole("ADMIN") 
		.antMatchers("/rolform/**").hasAnyRole("ADMIN") 
		.antMatchers("/rollistado/**").hasAnyRole("ADMIN") 
		//servicio calidad cumpleaños
		.antMatchers("/indexcalidad/**").hasAnyRole("ADMIN", "CALIDAD_CUMPLEANOS") 
		.antMatchers("/indexcalendario/**").hasAnyRole("ADMIN", "CALIDAD_CUMPLEANOS_EMPLEADOS", "CALIDAD_CUMPLEANOS_HOY", "ROLE_CALIDAD_CUMPLEANOS_CARGAR") 
		.antMatchers("/empleadocumple/**").hasAnyRole("ADMIN", "CALIDAD_CUMPLEANOS_EMPLEADOS", "CALIDAD_CUMPLEANOS_HOY", "ROLE_CALIDAD_CUMPLEANOS_CARGAR") 		
		.antMatchers("/cumplecarform/**").hasAnyRole("ADMIN", "CALIDAD_CUMPLEANOS_EMPLEADOS", "CALIDAD_CUMPLEANOS_HOY", "ROLE_CALIDAD_CUMPLEANOS_CARGAR") 
		//servicio estadistica certificado
		.antMatchers("/indexestadistica/**").hasAnyRole("ADMIN", "ESTADISTICA_CERTIFICADO", "ESTADISTICA_MORTALIDAD") 
		.antMatchers("/indexcertificado/**").hasAnyRole("ADMIN", "ESTADISTICA_CERTIFICADO_CERTIFICADO", "ESTADISTICA_CERTIFICADO_CARGAR") 
		.antMatchers("/certificadoestadistica/**").hasAnyRole("ADMIN", "ESTADISTICA_CERTIFICADO_CERTIFICADO", "ESTADISTICA_CERTIFICADO_CARGAR") 
		.antMatchers("/certificadocsvform/**").hasAnyRole("ADMIN", "ESTADISTICA_CERTIFICADO_CERTIFICADO", "ESTADISTICA_CERTIFICADO_CARGAR")
		//servicio farmacia mipres
		.antMatchers("/indexfarmacia/**").hasAnyRole("ADMIN", "FARMACIA_MIPRES") 
		.antMatchers("/indexmipres/**").hasAnyRole("ADMIN", "FARMACIA_MIPRES_ENTREGA", "FARMACIA_MIPRES_PARAMETROS") 
		.antMatchers("/tokenform/**").hasAnyRole("ADMIN", "FARMACIA_MIPRES_ENTREGA", "FARMACIA_MIPRES_PARAMETROS") 		
		//servicio estadistica mortalidad
		.antMatchers("/indexmortalidad/**").hasAnyRole("ADMIN", "ESTADISTICA_MORTALIDAD_ANALISIS", "ESTADISTICA_MORTALIDAD_REPORTE")
		.antMatchers("/mortalidadform/**").hasAnyRole("ADMIN", "ESTADISTICA_MORTALIDAD_ANALISIS", "ESTADISTICA_MORTALIDAD_REPORTE")
		.antMatchers("/reportemortalidad/**").hasAnyRole("ADMIN", "ESTADISTICA_MORTALIDAD_ANALISIS", "ESTADISTICA_MORTALIDAD_REPORTE")		
		//servicio medicina interna
		.antMatchers("/indexmedicinainterna/**").hasAnyRole("ADMIN", "MEDICINAINTERNA_ASIGNACIONCAMAS") 
		.antMatchers("/indexasignacioncamas/**").hasAnyRole("ADMIN", "MEDICINAINTERNA_ASIGNACIONCAMAS_SOLICITUDES", "MEDICINAINTERNA_ASIGNACIONCAMAS_TRAMITADAS","MEDICINAINTERNA_ASIGNACIONCAMAS_RECHAZADAS") 
		//servicio patologia
		.antMatchers("/indexpatologia/**").hasAnyRole("ADMIN", "PATOLOGIA_PROCEDIMIENTOS") 
		.antMatchers("/indexprocesar/**").hasAnyRole("ADMIN", "PATOLOGIA_PROCEDIMIENTOS_PROCESAR","PATOLOGIA_PROCEDIMIENTOS_CONSULTAS") 
		.antMatchers("/procedimientopatologia/**").hasAnyRole("ADMIN", "PATOLOGIA_PROCEDIMIENTOS_PROCESAR","PATOLOGIA_PROCEDIMIENTOS_CONSULTAS") 
		.antMatchers("/procesarpatologia/**").hasAnyRole("ADMIN", "PATOLOGIA_PROCEDIMIENTOS_PROCESAR","PATOLOGIA_PROCEDIMIENTOS_CONSULTAS") 
		.antMatchers("/procedimientopatologiageneral/**").hasAnyRole("ADMIN", "PATOLOGIA_PROCEDIMIENTOS_PROCESAR","PATOLOGIA_PROCEDIMIENTOS_CONSULTAS")		
		.antMatchers("/editarpatologiaprocesada/**").hasAnyRole("ADMIN", "PATOLOGIA_PROCEDIMIENTOS_PROCESAR","PATOLOGIA_PROCEDIMIENTOS_CONSULTAS")			
		//servicio siau
		.antMatchers("/indexsiau/**").hasAnyRole("ADMIN", "SIAU_ENCUESTAS") 
		.antMatchers("/indexencuestasat/**").hasAnyRole("ADMIN", "SIAU_ENCUESTAS_ENCUESTA","SIAU_ENCUESTAS_CONSOLIDADO") 
		.antMatchers("/encuestaformt/**").hasAnyRole("ADMIN", "SIAU_ENCUESTAS_ENCUESTA","SIAU_ENCUESTAS_CONSOLIDADO") 
		.antMatchers("/consolidadoencuesta/**").hasAnyRole("ADMIN", "SIAU_ENCUESTAS_ENCUESTA","SIAU_ENCUESTAS_CONSOLIDADO") 		
		.anyRequest().authenticated()
		.and()
			.formLogin()
			.loginPage("/login")
			.defaultSuccessUrl("/index")
			.permitAll()
		.and()
			.rememberMe().key("uniqueAndSecret").rememberMeParameter("remember-me").tokenValiditySeconds(3600)//el cookie durara dos horas y se llamara remember-me
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
