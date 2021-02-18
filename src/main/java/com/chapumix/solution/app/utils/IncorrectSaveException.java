package com.chapumix.solution.app.utils;

public class IncorrectSaveException extends Exception{
	
	private static final long serialVersionUID = 1L;

	public IncorrectSaveException(String errorMessage) {
        super(errorMessage);
    }

}
