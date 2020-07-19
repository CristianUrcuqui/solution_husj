package com.chapumix.activo.app;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.chapumix.solution.app.models.entity.ComTipoDocumentoMipres;
import com.chapumix.solution.app.models.entity.ComTipoTecnologia;
import com.chapumix.solution.app.models.entity.ComTokenMipres;
import com.chapumix.solution.app.models.entity.FarMipres;
import com.chapumix.solution.app.models.entity.GenPacien;
import com.chapumix.solution.app.models.service.IComTipoDocumentoMipresService;
import com.chapumix.solution.app.models.service.IComTipoTecnologiaService;
import com.chapumix.solution.app.models.service.IComTokenMipresService;
import com.chapumix.solution.app.models.service.IFarMipresService;
import com.chapumix.solution.app.models.service.IGenPacienService;

public class Test {
	
	public static final String MetodoGetPrescripcion = "https://wsmipres.sispro.gov.co/WSSUMMIPRESNOPBS/api/EntregaXPrescripcion/"; //url mipres metodo para get consulta
	
	@Autowired
	private static IFarMipresService iFarMipresService;
	
	@Autowired
	private static IComTokenMipresService iComTokenMipresService;
	
	@Autowired
	private static IComTipoTecnologiaService iComTipoTecnologiaService;
	
	@Autowired
	private static IComTipoDocumentoMipresService iComTipoDocumentoMipresService;
	
	@Autowired
	private static IGenPacienService iGenPacienService;

	public static void main(String[] args) {
		
		
		

	}

	
	
}
