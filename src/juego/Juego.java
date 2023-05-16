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
	private Image imagenAstroMS = Herramientas.cargarImagen("imagenes/nave3.png");
	private AstroMegaShip astroMegaShip;

	Juego() {
		// Inicializa el objeto entorno
		this.entorno = new Entorno(this, "Lost Galaxian - Grupo ... - v1", 800, 600);
		// Rayo
		this.rayo = new Rayo(500, 50, 4, Herramientas.radianes(180), imagenDelRayo);
		// AstroMegaShip
		this.astroMegaShip = new AstroMegaShip(400, 500, 2, Herramientas.radianes(0), imagenAstroMS);
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
		//if (astroMegaShip != null) {
		this.astroMegaShip.dibujar(this.entorno);
		if (this.entorno.estaPresionada(this.entorno.TECLA_DERECHA)
			&& this.astroMegaShip.getX() + this.astroMegaShip.getAncho() / 2 < this.entorno.ancho()) {
			this.astroMegaShip.moverDerecha();
		}
		if (this.entorno.estaPresionada(this.entorno.TECLA_IZQUIERDA)
			&& this.astroMegaShip.getX() - this.astroMegaShip.getAncho() / 2 > 0) {
			this.astroMegaShip.moverIzquierda();
		}
	}

	@SuppressWarnings("unused")
	public static void main(String[] args) {
		Juego juego = new Juego();
	}
}
