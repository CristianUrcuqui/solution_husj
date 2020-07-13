package com.chapumix.solution.app.test;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Base64;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class Test {

	public static void main(String[] args) throws UnsupportedEncodingException, NoSuchAlgorithmException {
		
		//encriptar password con Bcrypt
		/*BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
		System.out.println(bCryptPasswordEncoder.encode("123456"));*/
		
		//
		String contrasena = "chapumix";
		
		MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] hash = md.digest(contrasena.getBytes()); // <-- note encoding
        //return new String(Base64.encodeBase64(hash));
        System.out.println(new String(Base64.encodeBase64(hash)));
		
		StringBuilder s = new StringBuilder(new String(Base64.encodeBase64(hash)));
		System.out.println(s);
		

	}

}
