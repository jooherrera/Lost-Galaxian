package juego;

import java.awt.Color;
import java.awt.Font;
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

	AstroMegaShip(int x, int y, int velocidad, double angulo) {
		this.x = x;
		this.y = y;
		this.alto = 70; // Alto de la imagen de la nave
		this.ancho = 68; // Ancho de la imagen de la nave
		this.velocidad = 3;
		this.angulo = angulo;
		this.hp = 100;
		this.destruido = false;
	}

	// Metodo para que se dibuje la nave en la pantalla
	void dibujar(Entorno entorno, Image imagen) {
		entorno.dibujarImagen(imagen, this.x, this.y, this.angulo); // Dibuja la nave en pantalla.
		entorno.cambiarFont(Font.MONOSPACED, 25, Color.white);

		entorno.escribirTexto("HP: " + Integer.toString(hp), 10, 60); // Dibuja el hp disponible.
	}

	// Metodo para que la nave se dirija a la derecha
	void moverDerecha(Entorno entorno) {
		if (x + ancho / 2 < entorno.ancho())
			this.x = this.x + this.velocidad;
	}

	// Metodo para que la nave se dirija a la izquierda
	void moverIzquierda(Entorno entorno) {
		if (x - ancho / 2 > 0)
			this.x = this.x - this.velocidad;
	}

	// Metodo para que de su posicion
	Point posicion() {
		Point centroNave = new Point(this.x, this.y);
		return centroNave;
	}

	public void descontarVida(int hp) {
		if (this.hp - hp <= 0) {
			destruir();
			return;
		}
		this.hp -= hp;
	}

	Point[] tamanio() {
		Point p1 = new Point((int) (x - ancho / 4), (int) (y + alto / 4));
		Point p2 = new Point((int) (x - ancho / 4), (int) (y - alto / 4));
		Point p3 = new Point((int) (x + ancho / 4), (int) (y - alto / 4));
		Point p4 = new Point((int) (x + ancho / 4), (int) (y + alto / 4));
		return new Point[] { p1, p2, p3, p4 };
	}

	public Rayo disparar() {
		return new Rayo(x, y - alto / 2, 7, Herramientas.radianes(270));
	}

	private void destruir() {
		destruido = true;
	}

	public boolean estaDestruido() {
		return destruido;
	}
}
