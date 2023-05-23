package juego;

import java.awt.Color;
import java.awt.Font;

import entorno.Entorno;

public class Pantalla {

	public static void dibujarPuntaje(Entorno entorno, int valor) {
		entorno.cambiarFont(Font.MONOSPACED, 25, Color.white);

		String numeroEnString = Integer.toString(valor);
		while (numeroEnString.length() < 7) {
			numeroEnString = "0" + numeroEnString;
		}

		entorno.escribirTexto("Puntos: " + numeroEnString, 10, 30);
	}


	public static void dibujarCantidadEliminados(Entorno entorno, int valor) {
		entorno.escribirTexto("Eliminados : " + Integer.toString(valor), 10, entorno.alto() - 20);
	}

	public static void dibujarCartelGanado(Entorno entorno) {
	
	}

	public static void dibujarCartelPerdido(Entorno entorno) {
		
	}
}
