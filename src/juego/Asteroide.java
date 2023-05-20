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
	private Image imagen;
	private boolean destruido;
	private boolean exploto;

	Asteroide(double x, double y, double radio, double velocidad, double angulo, Image imagen) {
		this.x = x;
		this.y = y;
		this.radio = radio;
		this.velocidad = velocidad;
		this.angulo = angulo;
		this.imagen = imagen;
		this.destruido = false;
		this.exploto = false;
	}

	void dibujar(Entorno entorno) {
		entorno.dibujarImagen(this.imagen, this.x, this.y, this.angulo);
	}

	void dibujarExplosion(Image imagen) {
		this.imagen = imagen;
	}

	void mover() {
		this.x += Math.cos(angulo) * this.velocidad;
		this.y += Math.sin(angulo) * this.velocidad;
	}

	Point posicion() {
		Point p = new Point((int) x, (int) y);
		return p;
	}

	Point[] tamanio() {
		Point p1 = new Point((int) (this.x - this.radio), (int) this.y);
		Point p2 = new Point((int) (this.x), (int) (this.y - this.radio));
		Point p3 = new Point((int) (this.x + this.radio), (int) this.y);
		Point p4 = new Point((int) (this.x), (int) (this.y + this.radio));
		return new Point[] { p1, p2, p3, p4 };
	}

	void destruir() {
		this.destruido = true;
	}

	boolean estaDestruido() {
		return this.destruido;
	}

	void explotar(Image imagen) {
		this.imagen = imagen;
		this.exploto = true;
	}

	boolean estaExplotando() {
		return this.exploto;
	}

	boolean estaDibujando(Entorno entorno) {
		boolean fueraDeY = this.posicion().getY() > entorno.alto() + 30;
		boolean fueraDeXIzq = this.posicion().getX() < -30;
		boolean fueraDeXDer = this.posicion().getX() > entorno.ancho() + 30;
		if (fueraDeY || fueraDeXIzq || fueraDeXDer)
			return false;
		return true;
	}
	
	void golpear(AstroMegaShip nave) {
		nave.descontarVida(3);
	}
	
boolean estaChocando(AstroMegaShip nave) {
	boolean valor = false;
	Point[] mallaDeLaNave = nave.tamanio();
	Point[] mallaDelRayo = this.tamanio();

	// Comprueba si la nave esta dentro del espacio del rayo o viceversa.
	for (int i = 0; i < 4; i++) {
		Boolean estaDentroDelRangoX = mallaDelRayo[0].getX() < mallaDeLaNave[i].getX()
				&& mallaDeLaNave[i].getX() < mallaDelRayo[3].getX();
		Boolean estaDentroDelRangoY = mallaDelRayo[2].getY() < mallaDeLaNave[i].getY()
				&& mallaDeLaNave[i].getY() < mallaDelRayo[3].getY();

		Boolean estaDentroDelRangoX2 = mallaDeLaNave[0].getX() < mallaDelRayo[i].getX()
				&& mallaDelRayo[i].getX() < mallaDeLaNave[3].getX();
		Boolean estaDentroDelRangoY2 = mallaDeLaNave[2].getY() < mallaDelRayo[i].getY()
				&& mallaDelRayo[i].getY() < mallaDeLaNave[3].getY();

		if ((estaDentroDelRangoX || estaDentroDelRangoX2) && (estaDentroDelRangoY || estaDentroDelRangoY2))
			valor = true;
	}
	return valor;
}
	
}
