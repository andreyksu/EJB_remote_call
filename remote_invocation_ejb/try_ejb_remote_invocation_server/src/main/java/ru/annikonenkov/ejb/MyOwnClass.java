package ru.annikonenkov.ejb;

import java.io.Serializable;

public class MyOwnClass implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1184357625711041183L;

	public int multi(int a, int b) {
		return a * b;
	}

}
