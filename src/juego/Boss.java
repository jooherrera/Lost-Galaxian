package juego;

import java.awt.Image;
import java.awt.Point;

import entorno.Entorno;
import entorno.Herramientas;

public class Boss {
	private double x;
	private double y;
	private double ancho;
	private double alto;
	private double angulo;
	private int hp;
	private int hpInicial;
	private boolean destruido;

	Boss(double x, double y, double ancho, double alto, int hp, double angulo) {
		this.x = x;
		this.y = y;
		this.ancho = ancho;
		this.alto = alto;
		this.angulo = angulo;
		this.hp = hp;
		this.hpInicial = hp;
		this.destruido = false;
	}

	public void dibujar(Entorno entorno, Image image) {
		entorno.dibujarImagen(image, x, y, angulo);
	}

	public void apuntar(AstroMegaShip nave) {
		double catetoA = nave.posicion().getX() - x;
		double catetoO = nave.posicion().getY() - y;
		double grados = Math.toDegrees(Math.atan(catetoO / catetoA));
		double nuevoGrado = grados > 0 ? grados : 180 + grados;
		angulo = Herramientas.radianes(nuevoGrado);
	}

	public void descontarVida(int danio) {
		if (hp - danio <= 0) {
			destruir();
			return;
		}
		hp -= danio;
	}

	public Point[] tamanio() {
		Point p1 = new Point((int) (x - alto * .8), (int) (y + ancho / 2));
		Point p2 = new Point((int) (x - alto * .8), (int) (y - ancho / 2));
		Point p3 = new Point((int) (x + alto * .8), (int) (y - ancho / 2));
		Point p4 = new Point((int) (x + alto * .8), (int) (y + ancho / 2));
		return new Point[] { p1, p2, p3, p4 };
	}

	public RayoBoss disparar() {
		Herramientas.play("sonidos/disparojefe.wav");
		return new RayoBoss(x, y,100,16, 4, angulo);
	}

	public void destruir() {
		destruido = true;
	}

	public boolean estaDestruido() {
		return destruido;
	}

	// ------------ STATUS -----------
	public int vidaInicial() {
		return hpInicial;
	}

	public int vidaDisponible() {
		return hp;
	}
}
