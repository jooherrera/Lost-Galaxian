package juego;

import java.awt.Color;
import java.awt.Image;
import java.awt.Point;

import entorno.Entorno;

public class Rayo {
	private double x;
	private double y;
	private double alto;
	private double ancho;
	private double velocidad;
	private double angulo;
	private Image imagen;
	private int DANIO_A_ASTROMEGASHIP = 7;
	private int DANIO_A_DESTRUCTOR = 100;

	Rayo(int x, int y, float velocidad, double angulo, Image imagen) {
		if (x < 0)
			throw new Error("La posicion x no puede ser menor a 0");
		this.x = x;
		this.y = y;
		this.velocidad = velocidad;
		this.ancho = 50;
		this.alto = 8;
		this.imagen = imagen;
		this.angulo = angulo; // Tiene que venir en radianes
	}

	public void dibujar(Entorno entorno) {
//		entorno.dibujarRectangulo(x, y, ancho, alto, angulo, Color.red);
		entorno.dibujarImagen(this.imagen, this.x, this.y, this.angulo);

		System.out.println("angulo: " + Math.abs(Math.cos(angulo)));

		
		
		// p1
		entorno.dibujarCirculo(this.x - Math.cos(angulo) * this.ancho / 2, this.y  + Math.sin(angulo) * this.alto / 2, 4, Color.blue);
		// p2
		entorno.dibujarCirculo(this.x - Math.cos(angulo) * this.ancho / 2, this.y - Math.sin(angulo) * this.alto / 2, 4, Color.yellow);
		// p3
//		entorno.dibujarCirculo(this.x + Math.cos(angulo) * this.ancho / 2, this.y - Math.cos(angulo) * this.alto / 2, 4, Color.green);
//		// p4
//		entorno.dibujarCirculo(this.x + Math.cos(angulo) * this.ancho / 2, this.y + Math.cos(angulo) * this.alto / 2, 4, Color.cyan);
	}

	public void mover() {
		this.x += Math.cos(this.angulo) * this.velocidad;
		this.y += Math.sin(this.angulo) * this.velocidad;
	}

	public Point posicion() {
		Point p = new Point((int) x, (int) y);
		return p;
	}

//	Point[] tamanio() {
//		Point p1 = new Point((int) (this.x - this.alto /2), (int) (this.y - this.ancho / 2));
//		Point p2 = new Point((int) (this.x), (int) (this.y - this.radio));
//		Point p3 = new Point((int) (this.x + this.radio), (int) this.y);
//		Point p4 = new Point((int) (this.x), (int) (this.y + this.radio));
//		return new Point[] { p1, p2, p3, p4 };
//	}

	public boolean estaDibujando(Entorno entorno) {
		boolean fueraDeYAbajo = this.y > entorno.alto() + alto;
		boolean fueraDeYArriba = this.y < 0 - alto;
		if (fueraDeYAbajo || fueraDeYArriba)
			return false;
		return true;
	}

	public void golpear(AstroMegaShip nave) {
		nave.descontarVida(DANIO_A_ASTROMEGASHIP);
	}

//	public void golpear(Destructor nave) {
//		nave.descontarVida(DANIO_A_DESTRUCTOR);
//	}

}
