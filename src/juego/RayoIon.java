package juego;

import java.awt.Image;
import java.awt.Point;
import entorno.Entorno;

public class RayoIon {
	private double x;
	private double y;
	private double alto;
	private double ancho;
	private double velocidad;
	private double angulo;
	private boolean destruido;
	private int DANIO_A_ASTROMEGASHIP = 7;

	RayoIon(double x, double y, double ancho, double alto, double velocidad, double angulo) {
		this.x = x;
		this.y = y;
		this.velocidad = velocidad;
		this.ancho = ancho;
		this.alto = alto;
		this.angulo = angulo;
		this.destruido = false;
	}

	public void dibujar(Entorno entorno, Image imagen) {
		entorno.dibujarImagen(imagen, x, y, angulo);
		if (!estaDibujando(entorno))
			destruir();
	}

	public void mover() {
		y += Math.sin(angulo) * velocidad;
		x += Math.cos(angulo) * velocidad;
	}

	public Point[] tamanio() {
		Point p1 = new Point((int) (x - ancho / 2), (int) (y + alto / 2));
		Point p2 = new Point((int) (x - ancho / 2), (int) (y - alto / 2));
		Point p3 = new Point((int) (x + ancho / 2), (int) (y - alto / 2));
		Point p4 = new Point((int) (x + ancho / 2), (int) (y + alto / 2));
		return new Point[] { p1, p2, p3, p4 };
	}

	public void golpear(AstroMegaShip nave) {
		if (estaEnRango(nave) && !destruido) {
			nave.descontarVida(DANIO_A_ASTROMEGASHIP);
			destruir();
		}
	}

	public boolean estaDestruido() {
		return destruido;
	}

	public void destruir() {
		destruido = true;
	}

	// ---------------------------- PRIVADOS -------------------------------------
	private boolean estaDibujando(Entorno entorno) {
		boolean fueraDeYArriba = y < 0 - alto;
		boolean fueraDeYAbajo = y > entorno.alto() + alto;
		if (fueraDeYAbajo || fueraDeYArriba)
			return false;
		return true;
	}

	private boolean estaEnRango(AstroMegaShip nave) {
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
}
