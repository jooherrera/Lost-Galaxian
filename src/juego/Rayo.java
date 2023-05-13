package juego;

import java.awt.Color;
import java.awt.Image;
import entorno.Entorno;

public class Rayo {
	private double x;
	private double y;
	private double alto;
	private double ancho;
	private double velocidad;
	private double angulo;
	private Image imagen;

	Rayo(int x, int y, float velocidad, double angulo, Image imagen) {
		if(x < 0) throw new Error("La posicion x no puede ser menor a 0");
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

	}

	public void mover() {
		this.x += Math.cos(this.angulo) * this.velocidad;
		this.y += Math.sin(this.angulo) * this.velocidad;
	}

	public double posicion() {
		return this.y;
	}

}
