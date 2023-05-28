package juego;

import java.awt.Image;
import java.awt.Point;
import entorno.Entorno;
import entorno.Herramientas;

public class AstroMegaShip {
	private int x;
	private int y;
	private int alto;
	private int ancho;
	private int velocidad;
	private double angulo;
	private int hp;
	private boolean destruido;

	AstroMegaShip(int x, int y, int alto, int ancho, int velocidad, double angulo) {
		this.x = x;
		this.y = y;
		this.alto = alto;
		this.ancho = ancho;
		this.velocidad = velocidad;
		this.angulo = angulo;
		this.hp = 100;
		this.destruido = false;
	}

	void dibujar(Entorno entorno, Image imagen) {
		entorno.dibujarImagen(imagen, x, y, angulo);
	}

	void moverDerecha(Entorno entorno) {
		if (x + ancho / 2 < entorno.ancho())
			x = x + velocidad;
	}

	void moverIzquierda(Entorno entorno) {
		if (x - ancho / 2 > 0)
			x = x - velocidad;
	}

	Point posicion() {
		Point centroNave = new Point(x, y);
		return centroNave;
	}

	public void descontarVida(int danio) {
		if (hp - danio <= 0) {
			destruir();
			return;
		}
		hp -= danio;
	}

	public Point[] tamanio() {
		Point p1 = new Point((int) (x - ancho * .5), (int) (y + alto * .5));
		Point p2 = new Point((int) (x - ancho * .5), (int) (y - alto * .1));
		Point p3 = new Point((int) (x + ancho * .5), (int) (y - alto * .1));
		Point p4 = new Point((int) (x + ancho * .5), (int) (y + alto * .5));
		return new Point[] { p1, p2, p3, p4 };
	}

	public Rayo disparar() {
		Herramientas.play("sonidos/blasterAstro.wav");
		return new Rayo(x, y - alto / 2, 8, 50, 7, Herramientas.radianes(270));
	}

	private void destruir() {
		destruido = true;
	}

	public boolean estaDestruido() {
		return destruido;
	}

	// --------------------STATUS---------------------
	public int vidaRestante() {
		return hp;
	}
}
