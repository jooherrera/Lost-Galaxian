package juego;

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
	private double direccion;
	private double velocidadY;
	private boolean destruido;
	private boolean movil;
	private int DANIO_ASTRO;
	private boolean kamikaze;

	public Destructor(double x, double y, double ancho, double alto, double velocidad, double angulo) {
		this.x = x;
		this.y = y;
		this.ancho = 60;
		this.alto = 60;
		this.angulo = angulo;
		this.direccion = angulo;
		this.velocidadX = .5; // Ajusta la velocidad en el eje X
		this.velocidadY = velocidad; // Ajusta la velocidad en el eje Y
		this.movil = true;
		this.DANIO_ASTRO = 20;
		this.kamikaze = false;
	}

	
	boolean estaEnRango(AstroMegaShip nave) {
		boolean valor = false;
		Point[] mallaDeLaAstro = nave.tamanio();
		Point[] mallaDelDestructor = this.tamanio();

		// Comprueba si la nave esta dentro del espacio del rayo o viceversa.
		for (int i = 0; i < 4; i++) {
			Boolean estaDentroDelRangoX = mallaDelDestructor[0].getX() < mallaDeLaAstro[i].getX()
					&& mallaDeLaAstro[i].getX() < mallaDelDestructor[3].getX();
			Boolean estaDentroDelRangoY = mallaDelDestructor[2].getY() < mallaDeLaAstro[i].getY()
					&& mallaDeLaAstro[i].getY() < mallaDelDestructor[3].getY();

			Boolean estaDentroDelRangoX2 = mallaDeLaAstro[0].getX() < mallaDelDestructor[i].getX()
					&& mallaDelDestructor[i].getX() < mallaDeLaAstro[3].getX();
			Boolean estaDentroDelRangoY2 = mallaDeLaAstro[2].getY() < mallaDelDestructor[i].getY()
					&& mallaDelDestructor[i].getY() < mallaDeLaAstro[3].getY();

			if ((estaDentroDelRangoX || estaDentroDelRangoX2) && (estaDentroDelRangoY || estaDentroDelRangoY2))
				valor = true;
		}
		return valor;
	}

	public void inmovilizar() {
		this.movil = false;
	}

	public void movilizar() {
		this.movil = true;
	}

	// Metodo para que de su posicion
	Point posicion() {
		Point centroNave = new Point((int) this.x, (int) this.y);
		return centroNave;
	}

	public void apuntar(AstroMegaShip nave) {
		
		double catetoA = nave.posicion().getX() - this.x; // Cateto Adyacente
		double catetoO = nave.posicion().getY() - this.y; // Cateto Opuesto
		
		double grados = Math.toDegrees(Math.atan(catetoO / catetoA));
		
		double nuevoGrado = 0;
		
		if(grados > 0) 
			 nuevoGrado = (Math.abs(grados) - Math.toDegrees(direccion));
		else
			nuevoGrado = Math.abs(Math.abs(grados) - Math.toDegrees(direccion));
		
		this.angulo = direccion + Herramientas.radianes(nuevoGrado);

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
		if (!movil)
			return;
		if (x + ancho / 2 < entorno.ancho())
			this.x = this.x + velocidadX;
	}

	// Metodo para que la nave se dirija a la izquierda
	void moverIzquierda(Entorno entorno) {
		if (!movil)
			return;
		if (x - ancho / 2 > 0)
			this.x = this.x - velocidadX;
	}
	
	public void kamikaze() {
		this.kamikaze = true;
	}
	
	public void mover() {
		if (kamikaze) {
			this.y += Math.sin(angulo) * 2;
			this.x += Math.cos(angulo) * 2;
			return;
		}
		if (movil) {
			y += Math.sin(angulo) * velocidadY;
		}
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

	void golpear(AstroMegaShip nave) {
		nave.descontarVida(DANIO_ASTRO);
	}

	public Rayo disparar() {
		if (!this.kamikaze) {
			return new Rayo(x, y, 3.4,angulo);
		}
		else {
			return null;
		}
	}

	public boolean estaDibujando(Entorno entorno) {
		boolean fueraDeYAbajo = y > entorno.alto() + alto;
		if (fueraDeYAbajo)
			return false;
		return true;
	}

}