package com.chapumix.activo.app;

public class Test {

	public static void main(String[] args) {
		String prueba = "[{\"Id\":30656469,\"IdEntrega\":18411609}]";
		System.out.println(prueba.replaceAll("\\{", "").replaceAll("\\}", ""));

	}

}
