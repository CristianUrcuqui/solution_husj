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
		.antMatchers("/indexajustes/**").hasAnyRole("ADMIN", "INDEX_AJUSTES")	
		.antMatchers("/usuariolistado/**").hasAnyRole("ADMIN", "AJUSTES_USUARIOS")		
		.antMatchers("/usuarioform/**").hasAnyRole("ADMIN", "AJUSTES_EDITAR_USUARIOS")		
		.antMatchers("/rollistado/**").hasAnyRole("ADMIN", "AJUSTES_ROL")
		.antMatchers("/rolform/**").hasAnyRole("ADMIN", "AJUSTES_EDITAR_ROL")
		.antMatchers("/sincronizausuarioform/**").hasAnyRole("ADMIN", "AJUSTES_SINCRONIZAR_USUARIOS")
		.antMatchers("/sincronizapacienteform/**").hasAnyRole("ADMIN", "AJUSTES_SINCRONIZAR_PACIENTES")		
		//servicio calidad cumpleaños
		.antMatchers("/indexcalidad/**").hasAnyRole("ADMIN", "INDEX_CALIDAD") 
		.antMatchers("/indexcalendario/**").hasAnyRole("ADMIN", "CALIDAD_CUMPLEANOS_APP") 		
		.antMatchers("/empleadocumple/**").hasAnyRole("ADMIN", "CALIDAD_CUMPLEANOS_EMPLEADOS")
		.antMatchers("/empleadohoy/**").hasAnyRole("ADMIN", "CALIDAD_CUMPLEANOS_HOY")
		.antMatchers("/cumplecarform/**").hasAnyRole("ADMIN", "CALIDAD_CUMPLEANOS_CARGAR")
		//servicio calidad eventos clinicos
		.antMatchers("/indexeventosclinicos/**").hasAnyRole("ADMIN", "CALIDAD_EVENTOSCLINICOS_APP")
		.antMatchers("/eventoclinicoregform/**").hasAnyRole("ADMIN", "CALIDAD_EVENTOSCLINICOS_REGISTRO")
		
		//servicio estadistica certificado
		.antMatchers("/indexestadistica/**").hasAnyRole("ADMIN", "INDEX_ESTADISTICA") 
		.antMatchers("/indexcertificado/**").hasAnyRole("ADMIN", "ESTADISTICA_CERTIFICADO_APP") 
		.antMatchers("/certificadoestadistica/**").hasAnyRole("ADMIN", "ESTADISTICA_CERTIFICADO")
		.antMatchers("/certificadoform/**").hasAnyRole("ADMIN", "ESTADISTICA_CERTIFICADO")
		.antMatchers("/certificadocsvform/**").hasAnyRole("ADMIN", "ESTADISTICA_CERTIFICADO_CARGAR")
		//servicio estadistica mortalidad
		.antMatchers("/indexmortalidad/**").hasAnyRole("ADMIN", "ESTADISTICA_MORTALIDAD_APP")
		.antMatchers("/mortalidadform/**").hasAnyRole("ADMIN", "ESTADISTICA_MORTALIDAD_ANALISIS")
		.antMatchers("/consolidadomortalidad/**").hasAnyRole("ADMIN", "ESTADISTICA_MORTALIDAD_CONSOLIDADO")
		.antMatchers("/reportemortalidad/**").hasAnyRole("ADMIN", "ESTADISTICA_MORTALIDAD_REPORTE")	
		//servicio farmacia mipres
		.antMatchers("/indexfarmacia/**").hasAnyRole("ADMIN", "INDEX_FARMACIA") 
		.antMatchers("/indexmipres/**").hasAnyRole("ADMIN", "FARMACIA_MIPRES_APP")
		.antMatchers("/pendientesmipreshosp/**").hasAnyRole("ADMIN", "FARMACIA_MIPRES_ENTREGA_URGENCIAS")
		.antMatchers("/procesadosmipreshosp/**").hasAnyRole("ADMIN", "FARMACIA_MIPRES_PROCESADO_URGENCIAS")
		.antMatchers("/sincronizaprescripcionhospform/**").hasAnyRole("ADMIN", "FARMACIA_MIPRES_SINCRONIZAR_URGENCIAS")
		.antMatchers("/anulaprescripcionhospform/**").hasAnyRole("ADMIN", "FARMACIA_MIPRES_ANULAR_URGENCIAS")
		.antMatchers("/pendientesmipresamb/**").hasAnyRole("ADMIN", "FARMACIA_MIPRES_ENTREGA_AMBULATORIO")
		.antMatchers("/procesadosmipresamb/**").hasAnyRole("ADMIN", "FARMACIA_MIPRES_PROCESADO_AMBULATORIO")
		.antMatchers("/sincronizaprescripcionambform/**").hasAnyRole("ADMIN", "FARMACIA_MIPRES_SINCRONIZAR_AMBULATORIO")
		.antMatchers("/ripsmipres/**").hasAnyRole("ADMIN", "FARMACIA_MIPRES_RIPS")
		.antMatchers("/tokenformprimario/**").hasAnyRole("ADMIN", "FARMACIA_MIPRES_PARAMETRO_PRINCIPAL")
		.antMatchers("/tokenformsecundario/**").hasAnyRole("ADMIN", "FARMACIA_MIPRES_PARAMETRO_SECUNDARIO")
		//servicio farmacia stickers
		.antMatchers("/indexstickers/**").hasAnyRole("ADMIN", "FARMACIA_STICKERS_APP")
		.antMatchers("/generarsticker/**").hasAnyRole("ADMIN", "FARMACIA_STICKERS_GENERAR")
		
		//servicio medicina interna
		.antMatchers("/indexmedicinainterna/**").hasAnyRole("ADMIN", "INDEX_INTERNACION") 
		.antMatchers("/indexasignacioncamas/**").hasAnyRole("ADMIN", "INTERNACION_ASIGNACIONCAMAS_APP") 
		.antMatchers("/camasolicitud/**").hasAnyRole("ADMIN", "INTERNACION_ASIGNACIONCAMAS_SOLICITUDES")
		.antMatchers("/camatramitada/**").hasAnyRole("ADMIN", "INTERNACION_ASIGNACIONCAMAS_TRAMITADAS")
		//servicio patologia
		.antMatchers("/indexpatologia/**").hasAnyRole("ADMIN", "INDEX_PATOLOGIA") 
		.antMatchers("/indexprocesar/**").hasAnyRole("ADMIN", "PATOLOGIA_PROCEDIMIENTOS_APP") 
		.antMatchers("/procedimientopatologia/**").hasAnyRole("ADMIN", "PATOLOGIA_PROCEDIMIENTOS_PROCESAR") 
		.antMatchers("/procedimientopatologiageneral/**").hasAnyRole("ADMIN", "PATOLOGIA_PROCEDIMIENTOS_CONSULTAS")					
		//servicio siau
		.antMatchers("/indexsiau/**").hasAnyRole("ADMIN", "INDEX_SIAU") 
		.antMatchers("/indexencuestasat/**").hasAnyRole("ADMIN", "SIAU_ENCUESTAS_APP") 
		.antMatchers("/encuestaformt/**").hasAnyRole("ADMIN", "SIAU_ENCUESTAS_ENCUESTA") 
		.antMatchers("/consolidadoencuesta/**").hasAnyRole("ADMIN", "SIAU_ENCUESTAS_CONSOLIDADO")
		//servicio sistemas
		.antMatchers("/indexsistemas/**").hasAnyRole("ADMIN", "INDEX_SISTEMAS")
		.antMatchers("/indexanulacioningreso/**").hasAnyRole("ADMIN", "SISTEMAS_UTILIDADES_ANULACION_INGRESO_APP")
		.antMatchers("/anulaingreso/**").hasAnyRole("ADMIN", "SISTEMAS_UTILIDADES_ANULACION_INGRESO_PROCESAR")		
		.antMatchers("/indexactualizarplan/**").hasAnyRole("ADMIN", "SISTEMAS_UTILIDADES_ACTUALIZAR_PLAN_BENEFICIO_APP")
		.antMatchers("/actualizarplan/**").hasAnyRole("ADMIN", "SISTEMAS_UTILIDADES_ACTUALIZAR_PLAN_BENEFICIO_PROCESAR")		
		.anyRequest().authenticated()
		.and()
			.formLogin()
			.loginPage("/login")
			.defaultSuccessUrl("/index")
			.permitAll()
		.and()			
			.rememberMe().key("uniqueAndSecret").rememberMeParameter("remember-me").tokenValiditySeconds(7200)//el cookie durara dos horas y se llamara remember-me
		.and()
			.logout()
			.permitAll()
			.deleteCookies("JSESSIONID")
		.and()
		.exceptionHandling().accessDeniedPage("/error_403")
		.and()
		.headers().frameOptions().sameOrigin().httpStrictTransportSecurity().disable();//puede ser usado para indicar si debería permitírsele a un navegador renderizar una página en un <frame>	
		
	}

	//metodo para los usuarios en base de datos
	@Autowired
	public void cofigurerGlobal(AuthenticationManagerBuilder build) throws Exception {
		
		build.userDetailsService(comUsuarioDetalleService).passwordEncoder(passwordEncoder);		
	}

}
