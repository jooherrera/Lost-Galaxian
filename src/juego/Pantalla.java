package juego;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;

import entorno.Entorno;
import entorno.Herramientas;

public class Pantalla {

	private static Image imagenHpBoss = Herramientas.cargarImagen("imagenes/hp2.png");
	private static Entorno entorno = null;

	public static void setearEntorno(Entorno e) {
		entorno = e;
	}

	public static void dibujarAstroMegaShipStatus(AstroMegaShip nave) {
		entorno.cambiarFont(Font.MONOSPACED, 25, Color.white);
		entorno.escribirTexto("HP: " + Integer.toString(nave.vidaRestante()), 10, 60);
	}

	public static void dibujarCartelGanado() {
		entorno.cambiarFont(Font.SANS_SERIF, 50, Color.green);
		entorno.escribirTexto("GANASTE!!", entorno.ancho() / 2 - 120, entorno.alto() / 4);
		entorno.cambiarFont(Font.SANS_SERIF, 26, Color.red);
		entorno.escribirTexto("Presione S para salir.", entorno.ancho() / 2 - 140, entorno.alto() * .4);
		entorno.cambiarFont(Font.SANS_SERIF, 26, Color.red);
		entorno.escribirTexto("Presione R para volver a jugar.", entorno.ancho() / 2 - 140, entorno.alto() * .5);
	}

	public static void dibujarCartelPerdido() {
		entorno.cambiarFont(Font.SANS_SERIF, 50, Color.red);
		entorno.escribirTexto("PERDISTE!!", entorno.ancho() / 2 - 120, entorno.alto() / 4);
		entorno.cambiarFont(Font.SANS_SERIF, 26, Color.red);
		entorno.escribirTexto("Presione S para salir.", entorno.ancho() / 2 - 140, entorno.alto() * .4);
		entorno.cambiarFont(Font.SANS_SERIF, 26, Color.red);
		entorno.escribirTexto("Presione R para volver a jugar.", entorno.ancho() / 2 - 140, entorno.alto() * .5);
	}

	public static void dibujarCartelBoss() {
		entorno.cambiarFont(Font.SERIF, 75, Color.WHITE);
		entorno.escribirTexto("CHAPTER BOSS", entorno.ancho() / 2 - 280, entorno.alto() / 2);
	}

	public static void dibujarCartelNivel(int nivel) {
		entorno.cambiarFont(Font.SANS_SERIF, 50, Color.CYAN);
		entorno.escribirTexto("NIVEL: " + nivel, entorno.ancho() / 2 - 120, entorno.alto() / 2);
	}

	public static void dibujarPressEnter() {
		entorno.cambiarFont(Font.SANS_SERIF, 25, Color.green);
		entorno.escribirTexto("Press ENTER", entorno.ancho() / 2 - 100, entorno.alto() / 2 + 70);
	}

	public static void dibujarPuntaje(int valor) {
		entorno.cambiarFont(Font.MONOSPACED, 25, Color.white);
		String numeroEnString = Integer.toString(valor);
		while (numeroEnString.length() < 7) {
			numeroEnString = "0" + numeroEnString;
		}
		entorno.escribirTexto("Puntos: " + numeroEnString, 10, 30);
	}

	public static void dibujarBossStatus(Boss boss) {
		if (boss == null)
			return;
		int vida = boss.vidaDisponible() * 210 / boss.vidaInicial();
		int posInicial = 90;
		int ancho = boss.vidaDisponible() * 105 / boss.vidaInicial();
		entorno.dibujarRectangulo(entorno.ancho() / 2 - posInicial + ancho, 30, vida, 18, 0, Color.red);
		entorno.dibujarImagen(imagenHpBoss, entorno.ancho() / 2, 30, 0);
	}

	public static void dibujarCantidadEliminados(int valor) {
		entorno.escribirTexto("Eliminados : " + Integer.toString(valor), 10, entorno.alto() - 20);
	}

}
