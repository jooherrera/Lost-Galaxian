package juego;

import java.awt.Color;
import java.awt.Image;
import java.awt.Point;

import entorno.Entorno;
import entorno.Herramientas;

public class Destructor {
	private double x;
	private double y;
	private double ancho;
	private double alto;
	private double angulo;
	private double velocidadX;
	private int direccion;
	private double velocidadY;
	private double cambioDireccion;
	private boolean destruido;
	private boolean movil;

	public Destructor(double x, double y, double ancho, double alto, double velocidad, double angulo) {
		this.x = x;
		this.y = y;
		this.ancho = 60;
		this.alto = 60;
		this.angulo = angulo;
		this.velocidadX = .5; // Ajusta la velocidad en el eje X
		this.velocidadY = velocidad; // Ajusta la velocidad en el eje Y
		this.cambioDireccion = 70; // Ajusta el cambio de dirección en el eje X
		this.movil = true;
	}

	public void inmovilizar() {
		this.movil = false;
	}

	public void movilizar() {
		this.movil = true;
	}

	public void apuntar(AstroMegaShip nave) {
		this.angulo = Herramientas.radianes(95);
	}

	public void dibujar(Entorno entorno, Image imagen) {
		entorno.dibujarImagen(imagen, this.x, this.y, angulo);
//		if(!estaDibujando(entorno)) {
//			y = -50;
//		}
	}

	public void NoApuntar() {
		this.angulo = Herramientas.radianes(90);
	}

	void moverDerecha(Entorno entorno) {
		if (x + ancho / 2 < entorno.ancho())
			this.x = this.x + velocidadX;
	}

	// Metodo para que la nave se dirija a la izquierda
	void moverIzquierda(Entorno entorno) {
		if (x - ancho / 2 > 0)
			this.x = this.x - velocidadX;
	}
	
	
	public void mover() {
		if (movil) {
			y += Math.sin(angulo) * velocidadY;
		}

//		double angulo = 0;
//		double velocidad = 0;
//		double nuevoX = this.x + Math.cos(angulo) * velocidad;
//		double nuevoY = this.y + Math.sin(angulo) * velocidad;
//
//		if (nuevoX >= 0 && nuevoX <= anchoEntorno && nuevoY >= 0 && nuevoY <= altoEntorno) {
//			this.x = nuevoX;
//			this.y = nuevoY;
//		}
//
//		this.x += this.velocidadx;
//		this.y += this.velocidadY;
//
//		// Cambia la dirección en el eje X cada cierta cantidad de pasos
//		if (this.y % this.cambioDireccion == 0) {
//			this.velocidadx = -this.velocidadx;
//		}
	}

	public Point[] tamanio() {
		Point p1 = new Point((int) (x - ancho / 2), (int) (y));
		Point p2 = new Point((int) (x - ancho / 2), (int) (y - alto / 2));
		Point p3 = new Point((int) (x + ancho / 2), (int) (y - alto / 2));
		Point p4 = new Point((int) (x + ancho / 2), (int) (y));
		return new Point[] { p1, p2, p3, p4 };
	}

	public void destruir() {
		this.destruido = true;
	}

	public boolean estaDestruido() {
		return this.destruido;
	}

	public Rayo disparar() {
		return new Rayo((int) (x), (int) (y), 4, angulo);
	}

	public boolean estaDibujando(Entorno entorno) {
		boolean fueraDeYAbajo = y > entorno.alto() + alto;
		if (fueraDeYAbajo)
			return false;
		return true;
	}

}