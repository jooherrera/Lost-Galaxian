package juego;

import java.awt.Image;
import java.awt.Point;

import entorno.Entorno;

public class Asteroide {
	private double x;
	private double y;
	private double radio;
	private double velocidad;
	private double angulo;
	private boolean destruido = false;
	private int DANIO_A_ASTROMEGASHIP = 20;

	Asteroide(double x, double y, double radio, double velocidad, double angulo) {
		this.x = x;
		this.y = y;
		this.radio = radio;
		this.velocidad = velocidad;
		this.angulo = angulo;
	}

	public void dibujar(Entorno entorno, Image imagen) {
		entorno.dibujarImagen(imagen, x, y, angulo);
		if (!estaDibujando(entorno))
			destruir();
	}

	public void mover() {
		x += Math.cos(angulo) * velocidad;
		y += Math.sin(angulo) * velocidad;
	}

	public Point posicion() {
		Point p = new Point((int) x, (int) y);
		return p;
	}

	public Point[] tamanio() {
		Point p1 = new Point((int) (x - radio), (int) (y + radio));
		Point p2 = new Point((int) (x - radio), (int) (y - radio * .7));
		Point p3 = new Point((int) (x + radio * .8), (int) (y - radio * .7));
		Point p4 = new Point((int) (x + radio * .8), (int) (y + radio));
		return new Point[] { p1, p2, p3, p4 };
	}

	public void destruir() {
		destruido = true;
	}

	public boolean estaDestruido() {
		return destruido;
	}

	public void golpear(AstroMegaShip nave) {
		if (estaEnRango(nave) && !destruido) {
			nave.descontarVida(DANIO_A_ASTROMEGASHIP);
			destruir();
		}
	}

	private boolean estaDibujando(Entorno entorno) {
		boolean fueraDeY = posicion().getY() > entorno.alto() + 30;
		boolean fueraDeXIzq = posicion().getX() < -30;
		boolean fueraDeXDer = posicion().getX() > entorno.ancho() + 30;
		if (fueraDeY || fueraDeXIzq || fueraDeXDer)
			return false;
		return true;
	}

	private boolean estaEnRango(AstroMegaShip nave) {
		boolean valor = false;
		Point[] mallaDeLaNave = nave.tamanio();
		Point[] mallaDelAsteroide = this.tamanio();

		for (int i = 0; i < 4; i++) {
			Boolean estaDentroDelRangoX = mallaDelAsteroide[0].getX() < mallaDeLaNave[i].getX()
					&& mallaDeLaNave[i].getX() < mallaDelAsteroide[3].getX();
			Boolean estaDentroDelRangoY = mallaDelAsteroide[2].getY() < mallaDeLaNave[i].getY()
					&& mallaDeLaNave[i].getY() < mallaDelAsteroide[3].getY();

			Boolean estaDentroDelRangoX2 = mallaDeLaNave[0].getX() < mallaDelAsteroide[i].getX()
					&& mallaDelAsteroide[i].getX() < mallaDeLaNave[3].getX();
			Boolean estaDentroDelRangoY2 = mallaDeLaNave[2].getY() < mallaDelAsteroide[i].getY()
					&& mallaDelAsteroide[i].getY() < mallaDeLaNave[3].getY();

			if ((estaDentroDelRangoX || estaDentroDelRangoX2) && (estaDentroDelRangoY || estaDentroDelRangoY2))
				valor = true;
		}
		return valor;
	}

}
