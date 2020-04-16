package com.chapumix.solution.app.models.dao;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.chapumix.solution.app.models.entity.EstSerial;

public interface IEstSerialDao extends CrudRepository<EstSerial, Long>{
	
	
	//query personalizado para obtener el serial disponible por tipo de certificado
	@Query(value = "SELECT * FROM est_serial INNER JOIN est_tipo_certificado ON est_serial.id_tipo_certificado = est_tipo_certificado.id_tipo_certificado WHERE est_tipo_certificado.id_tipo_certificado = ?1 AND est_serial.estado = FALSE LIMIT 1", nativeQuery = true)
	EstSerial findSerialByTipo(Long id);
	
	
	//query personalizado para obtener el serial disponible por serial y tipo de certificado
	@Query("SELECT s FROM EstSerial s JOIN s.estTipoCertificado t WHERE s.serial = ?1 AND t.tipoCertificado = ?2")
	EstSerial findSerialBySerialAndTipo(String serial, String tipoCertificado);
	
		
}
