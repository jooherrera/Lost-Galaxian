package juego;

import entorno.Entorno;
import entorno.Herramientas;
import java.awt.Image;
import java.awt.Point;

public class Destructor {

	private double x;
	private double y;
	private double ancho;
	private double alto;
	private double angulo;
	private double velocidadX;
	private double velocidadY;
	private boolean destruido;
	private boolean movil;
	private int DANIO_A_ASTROMEGASHIP = 25;
	private int DANIO_KAMIKAZE = 50;
	private boolean kamikaze;

	public Destructor(double x, double y, double ancho, double alto, double velocidad, double angulo) {
		this.x = x;
		this.y = y;
		this.ancho = ancho;
		this.alto = alto;
		this.angulo = angulo;
		this.velocidadX = .4; // Ajusta la velocidad en el eje X
		this.velocidadY = velocidad; // Ajusta la velocidad en el eje Y
		this.movil = true;
		this.kamikaze = false;
	}

	public void dibujar(Entorno entorno, Image imagen) {
		entorno.dibujarImagen(imagen, x, y, angulo);
	}

	public void mover() {
		if (kamikaze) {
			this.y += Math.sin(angulo) * 2;
			this.x += Math.cos(angulo) * 2;
			return;
		}

		if (movil)
			y += Math.sin(angulo) * velocidadY;
	}

	private boolean estaEnRango(AstroMegaShip nave) {
		boolean valor = false;
		Point[] mallaDeLaAstro = nave.tamanio();
		Point[] mallaDelDestructor = this.tamanio();

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
		movil = false;
	}

	public Point posicion() {
		Point centroNave = new Point((int) x, (int) y);
		return centroNave;
	}

	public void apuntar(AstroMegaShip nave) {
		if (nave == null)
			return;

		if (y > nave.posicion().getY() - 100)
			return;

		double catetoA = nave.posicion().getX() - x;
		double catetoO = nave.posicion().getY() - y;
		double grados = Math.toDegrees(Math.atan(catetoO / catetoA));
		double nuevoGrado = grados > 0 ? grados : 180 + grados;
		angulo = Herramientas.radianes(nuevoGrado);
	}

	public void moverDerecha(Entorno entorno) {
		if (!movil)
			return;
		if (x + ancho / 2 < entorno.ancho())
			x = x + velocidadX;
	}

	public void moverIzquierda(Entorno entorno) {
		if (!movil)
			return;
		if (x - ancho / 2 > 0)
			x = x - velocidadX;
	}

	public void kamikaze() {
		kamikaze = true;
	}

	public Point[] tamanio() {
		Point p1 = new Point((int) (x - ancho / 2), (int) (y));
		Point p2 = new Point((int) (x - ancho / 2), (int) (y - alto / 2));
		Point p3 = new Point((int) (x + ancho / 2), (int) (y - alto / 2));
		Point p4 = new Point((int) (x + ancho / 2), (int) (y));
		return new Point[] { p1, p2, p3, p4 };
	}

	public void destruir() {
		destruido = true;
	}

	public boolean estaDestruido() {
		return destruido;
	}

	public void golpear(AstroMegaShip nave) {
		if (estaEnRango(nave) && !destruido) {
			if (kamikaze)
				nave.descontarVida(DANIO_KAMIKAZE);
			else
				nave.descontarVida(DANIO_A_ASTROMEGASHIP);
			destruir();
		}
	}

	public RayoIon disparar(Entorno entorno) {
		if (!this.kamikaze && y < entorno.alto() && y > 0) {
			return new RayoIon(x, y, 8, 50, 3.4, angulo);
		}
		return null;
	}

	public boolean estaDibujando(Entorno entorno) {
		boolean fueraDeYAbajo = y > entorno.alto() + alto;
		if (fueraDeYAbajo)
			return false;
		return true;
	}
}
