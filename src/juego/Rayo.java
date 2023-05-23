package juego;

import java.awt.Color;
import java.awt.Image;
import java.awt.Point;

import entorno.Entorno;
import entorno.Herramientas;

public class Rayo {
	private Point centro;
	private double alto;
	private double ancho;
	private double velocidad;
	private double angulo;
//	private Image imagen;
	private boolean destruido;

	private int DANIO_A_ASTROMEGASHIP = 7;
	private int DANIO_A_DESTRUCTOR = 100;

	Rayo(int x, int y, double velocidad, double angulo) {
		if (x < 0)
			throw new Error("La posicion x no puede ser menor a 0");
		this.centro = new Point(x, y);
		this.velocidad = velocidad;
		this.ancho = 8;
		this.alto = 50;
//		this.imagen = Herramientas.cargarImagen("imagenes/rayo2.png");
		this.angulo = angulo; // Tiene que venir en radianes
		this.destruido = false;
	}

	public void dibujar(Entorno entorno, Image imagen) {
//		entorno.dibujarRectangulo(centro.getX(),centro.getY(), alto, ancho, angulo , Color.red);
		entorno.dibujarImagen(imagen, centro.getX(), centro.getY(), angulo);
		if (!estaDibujando(entorno))
			destruir();
	}

	public void mover() {
		centro.y += Math.sin(angulo) * this.velocidad;
		centro.x += Math.cos(angulo) * this.velocidad;
	}

	public Point[] tamanio() {
		Point p1 = new Point((int) (centro.getX() - ancho / 2), (int) (centro.getY() + alto / 2));
		Point p2 = new Point((int) (centro.getX() - ancho / 2), (int) (centro.getY() - alto / 2));
		Point p3 = new Point((int) (centro.getX() + ancho / 2), (int) (centro.getY() - alto / 2));
		Point p4 = new Point((int) (centro.getX() + ancho / 2), (int) (centro.getY() + alto / 2));
		return new Point[] { p1, p2, p3, p4 };
	}

	public void golpear(AstroMegaShip nave) {
		nave.descontarVida(DANIO_A_ASTROMEGASHIP);
	}

	public boolean estaEnRango(Destructor[] naves) {
		boolean valor = false;

		for (Destructor nave : naves) {
			if (nave == null)
				continue;
			Point[] mallaDeLaNave = nave.tamanio();
			Point[] mallaDelRayo = this.tamanio();

			for (int i = 0; i < 4; i++) {
				Boolean estaDentroDelRangoX = mallaDelRayo[0].getX() < mallaDeLaNave[i].getX()
						&& mallaDeLaNave[i].getX() < mallaDelRayo[3].getX();
				Boolean estaDentroDelRangoY = mallaDelRayo[2].getY() < mallaDeLaNave[i].getY()
						&& mallaDeLaNave[i].getY() < mallaDelRayo[3].getY();

				Boolean estaDentroDelRangoX2 = mallaDeLaNave[0].getX() < mallaDelRayo[i].getX()
						&& mallaDelRayo[i].getX() < mallaDeLaNave[3].getX();
				Boolean estaDentroDelRangoY2 = mallaDeLaNave[2].getY() < mallaDelRayo[i].getY()
						&& mallaDelRayo[i].getY() < mallaDeLaNave[3].getY();

				if ((estaDentroDelRangoX || estaDentroDelRangoX2) && (estaDentroDelRangoY || estaDentroDelRangoY2)) {
					nave.destruir();
					valor = true;
				}
			}
		}
		return valor;
	}

	public boolean estaEnRango(AstroMegaShip nave) {

		boolean valor = false;
		Point[] mallaDeLaNave = nave.tamanio();
		Point[] mallaDelRayo = this.tamanio();

		for (int i = 0; i < 4; i++) {
			Boolean estaDentroDelRangoX = mallaDelRayo[0].getX() < mallaDeLaNave[i].getX()
					&& mallaDeLaNave[i].getX() < mallaDelRayo[3].getX();
			Boolean estaDentroDelRangoY = mallaDelRayo[2].getY() < mallaDeLaNave[i].getY()
					&& mallaDeLaNave[i].getY() < mallaDelRayo[3].getY();

			Boolean estaDentroDelRangoX2 = mallaDeLaNave[0].getX() < mallaDelRayo[i].getX()
					&& mallaDelRayo[i].getX() < mallaDeLaNave[3].getX();
			Boolean estaDentroDelRangoY2 = mallaDeLaNave[2].getY() < mallaDelRayo[i].getY()
					&& mallaDelRayo[i].getY() < mallaDeLaNave[3].getY();

			if ((estaDentroDelRangoX || estaDentroDelRangoX2) && (estaDentroDelRangoY || estaDentroDelRangoY2)) {
				valor = true;
			}
		}
		return valor;

	}

	public boolean estaDestruido() {
		return this.destruido;
	}

	public void destruir() {
		this.destruido = true;
	}

	// ---------------------------- PRIVADOS -------------------------------------
	private boolean estaDibujando(Entorno entorno) {
		boolean fueraDeYArriba = centro.getY() < 0 - alto;
		boolean fueraDeYAbajo = centro.getY() > entorno.alto() + alto;
		if (fueraDeYAbajo || fueraDeYArriba)
			return false;
		return true;
	}

}
