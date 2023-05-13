package juego;

import java.awt.Image;

import entorno.Entorno;
import entorno.Herramientas;
import entorno.InterfaceJuego;

public class Juego extends InterfaceJuego {
	// El objeto Entorno que controla el tiempo y otros
	private Entorno entorno;
	private Image imagenDelRayo = Herramientas.cargarImagen("imagenes/rayo2.png");
	private Rayo rayo;

	Juego() {
		// Inicializa el objeto entorno
		this.entorno = new Entorno(this, "Lost Galaxian - Grupo ... - v1", 800, 600);
		// Rayo
		this.rayo = new Rayo(500, 50, 4, Herramientas.radianes(90), imagenDelRayo);
		//
		this.entorno.iniciar();
	}

	public void tick() {
		if (rayo != null) {
			rayo.dibujar(entorno);
			rayo.mover();
			if (rayo.posicion() > 500)
				rayo = null;
		}
	}

	@SuppressWarnings("unused")
	public static void main(String[] args) {
		Juego juego = new Juego();
	}
}
