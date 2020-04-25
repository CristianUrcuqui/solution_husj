package com.chapumix.solution.app.models.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.chapumix.solution.app.models.entity.EstSerial;

public interface IEstSerialDao extends CrudRepository<EstSerial, Long>{
	
	
	//query personalizado para obtener el serial disponible por tipo de certificado
	@Query(value = "SELECT * FROM est_serial INNER JOIN est_tipo_certificado ON est_serial.id_tipo_certificado = est_tipo_certificado.id_tipo_certificado WHERE est_tipo_certificado.id_tipo_certificado = ?1 AND est_serial.estado = FALSE LIMIT 1", nativeQuery = true)
	EstSerial findSerialByTipo(Long id);
	
	
	//query personalizado para obtener el serial disponible, por serial y tipo de certificado string
	@Query("SELECT s FROM EstSerial s JOIN s.estTipoCertificado t WHERE s.serial = ?1 AND t.tipoCertificado = ?2")
	EstSerial findSerialBySerialAndTipo(String serial, String tipoCertificado);
	
	
	//query personalizado para obtener el serial disponible, por serial y tipo de certificado Long
	@Query("SELECT s FROM EstSerial s JOIN s.estTipoCertificado t WHERE s.serial = ?1 AND t.id = ?2")
	EstSerial findSerialBySerialAndTipoCertificado(String serial, Long tipoCertificado);
	
	//query personalizado para obtener la cantidad de seriales de defuncion disponibles
	@Query("SELECT s FROM EstSerial s JOIN s.estTipoCertificado t WHERE s.estado = false AND t.id = 1")
	List<EstSerial> countSerialDefuncion();
	
	// query personalizado para obtener la cantidad de seriales de nacido vivos disponibles
	@Query("SELECT s FROM EstSerial s JOIN s.estTipoCertificado t WHERE s.estado = false AND t.id = 2")
	List<EstSerial> countSerialNacidoVivo();
		
}
