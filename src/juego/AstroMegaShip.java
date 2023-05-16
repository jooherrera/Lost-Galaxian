package juego;

import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Point;
import entorno.Entorno;

public class AstroMegaShip {
	private int x;
	private int y;
	private int alto;
	private int ancho;
	private int velocidad;
	private double angulo;
	private Image imagen;

	AstroMegaShip(int x, int y, int velocidad, double angulo, Image imagen) {
		this.x = x;
		this.y = y;
		this.alto = 70; //Alto de la imagen de la nave
		this.ancho = 68; //Ancho de la imagen de la nave
		this.velocidad = 3;
		this.angulo = angulo;
		this.imagen = imagen;
	}
	
	//Metodo para que se dibuje la nave en la pantalla
	void dibujar(Entorno entorno) {
		entorno.dibujarImagen(this.imagen, this.x , this.y , this.angulo);
	}
	
	//Metodo para que la nave se dirija a la derecha
	void moverDerecha() {
		this.x = this.x + this.velocidad;
	}
	
	//Metodo para que la nave se dirija a la izquierda
	void moverIzquierda() {
		this.x = this.x - this.velocidad;
	}
	
	//Metodo para que de su posicion
	Point posicion() {
		Point centroNave = new Point(this.x, this.y);
		return centroNave;
	}
	
	//Rectangulo de la nave
	Rectangle rectangulo() {
	Rectangle centroNave = new Rectangle(this.x, this.y, this.ancho, this.alto);
		return centroNave;
	}
	
	//getters
	int getX() {
		return x;
	}
	
	int getAncho() {
		return ancho;
	}

}
