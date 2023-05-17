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

	public static void dibujarVidaAstro(Entorno entorno, int valor) {
		entorno.escribirTexto("HP: " + Integer.toString(valor), 10, 60);
	}

	public static void dibujarCantidadEliminados(Entorno entorno, int valor) {
		entorno.escribirTexto("Eliminados : " + Integer.toString(valor), 10, entorno.alto() - 20);
	}

	public static void dibujarCartelGanado(Entorno entorno) {
		entorno.cambiarFont(Font.SANS_SERIF, 50, Color.green);
		entorno.escribirTexto("GANASTE!!", entorno.ancho() / 2 - 120, entorno.alto() / 2);
	}

	public static void dibujarCartelPerdido(Entorno entorno) {
		entorno.cambiarFont(Font.SANS_SERIF, 50, Color.red);
		entorno.escribirTexto("PERDISTE!!", entorno.ancho() / 2 - 120, entorno.alto() / 2);
	}
}
