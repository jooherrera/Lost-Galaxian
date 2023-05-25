package juego;

import entorno.Entorno;
import entorno.Herramientas;
import entorno.InterfaceJuego;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.util.Random;

public class Juego extends InterfaceJuego {

	// El objeto Entorno que controla el tiempo y otros
	private Entorno entorno;
	// Estado del juego
	private int puntaje;
	private int cantEliminados;
	private int enemigosRestantes;
	private int puntosPorDestructor;
	private boolean estaGanado;
	private boolean estaPerdido;
	private int nivel;
	private boolean subioDeNivel;
	private int time;
	private int temporizador;

	// Imagenes
	private Image imagenFondo = cargarImagen("imagenes/fondo.png");
	private Image imagenDelRayo = cargarImagen("imagenes/rayo2.png");
	private Image imagenDelAsteroide = cargarImagen("imagenes/asteroide.png");
	private Image imagenDelDestructor = cargarImagen("imagenes/destructor2.png");
	// Entidades
	private Rayo rayoAstro;
	private Asteroide[] asteroides;
	private Destructor[] destructores;
	private Rayo[] rayosEnemigos;
	private Image imagenAstroMegaShip = Herramientas.cargarImagen("imagenes/naveNueva.png");
	private AstroMegaShip astroMegaShip;
	private Random random = new Random();
	private double p;

	Juego() {

		this.entorno = new Entorno(this, "Lost Galaxian - Grupo ... - v1", 800, 600);

		// Valores de estado del juego
		this.nivel = 1;
		this.puntaje = 0;
		this.cantEliminados = 0;
		this.puntosPorDestructor = 10;
		this.enemigosRestantes = 0;
		this.estaGanado = false;
		this.estaPerdido = false;
		this.subioDeNivel = false;

		this.astroMegaShip = new AstroMegaShip(400, 500, 2, Herramientas.radianes(270));
		instanciarAsteroides(4);
		cargarNivel(nivel);
		this.p = 600;
		this.time = 0;
		this.temporizador = 0;
		this.entorno.iniciar();
	}

	public void tick() {
		time++;
		temporizador++;

		if (estaGanado) {
			entorno.cambiarFont(Font.SANS_SERIF, 50, Color.green);
			entorno.escribirTexto("GANASTE!!", entorno.ancho() / 2 - 120, entorno.alto() / 2);
			return;
		}

		if (estaPerdido) {
			entorno.cambiarFont(Font.SANS_SERIF, 50, Color.red);
			entorno.escribirTexto("PERDISTE!!", entorno.ancho() / 2 - 120, entorno.alto() / 2);
			return;
		}

		if (subioDeNivel) {

			if (nivel % 4 == 0) {
				entorno.cambiarFont(Font.SERIF, 75, Color.WHITE);
				entorno.escribirTexto("CHAPTER BOSS", entorno.ancho() / 2 - 280, entorno.alto() / 2);
			} else {

				entorno.cambiarFont(Font.SANS_SERIF, 50, Color.CYAN);
				entorno.escribirTexto("NIVEL: " + nivel, entorno.ancho() / 2 - 120, entorno.alto() / 2);
			}

			entorno.cambiarFont(Font.SANS_SERIF, 25, Color.green);
			// Hace titilar el "Press ENTER" dependiendo de time. 
			if (time / 60 % 2 == 0) {
				entorno.escribirTexto("Press ENTER", entorno.ancho() / 2 - 100, entorno.alto() / 2 + 70);
			}

			// Esperar el enter para cambiar al proximo nivel.
			if (entorno.sePresiono(entorno.TECLA_ENTER))
				subioDeNivel = false;
			return;
		}

		dibujarFondo();

		// -----------------------------RAYO ASTROMEGASHIP ----------------------------

		if (rayoAstro != null) {

			rayoAstro.dibujar(entorno, imagenDelRayo);
			rayoAstro.mover();

			if (rayoAstro.estaEnRango(destructores)) {
				rayoAstro.destruir();
			}
			
			if(rayoAstro.estaEnRango(asteroides))
				rayoAstro.destruir();

			if (rayoAstro.estaDestruido())
				rayoAstro = null;
		}

		// -----------------------------RAYOS ENEMIGOS ----------------------------
		for (int r = 0; r < rayosEnemigos.length; r++) {
			Rayo rayoEnemigo = rayosEnemigos[r];

			if (rayoEnemigo != null) {

				rayoEnemigo.dibujar(entorno, imagenDelRayo);
				rayoEnemigo.mover();

				if (!rayoEnemigo.estaDestruido() && rayoEnemigo.estaEnRango(astroMegaShip)) {
					rayoEnemigo.golpear(astroMegaShip);
					rayoEnemigo.destruir();
				}

				if (rayoEnemigo.estaDestruido())
					rayosEnemigos[r] = null;

			}
		}

		// ----------------------------DESTRUCTORES--------------------------------
		int fueraDePantalla = 0;

		for (int j = 0; j < destructores.length; j++) {
			if (destructores[j] != null) {

				int numeroAleatorio = random.nextInt(100);

				Destructor destructor = destructores[j];
				destructor.mover();

				if (temporizador / 60 > 2) {
					destructor.moverDerecha(entorno);
					if (temporizador / 60 > 5)
						temporizador = 0;	
				} else {
					destructor.moverIzquierda(entorno);
				}

				if (nivel % 4 == 0) {
					destructor.inmovilizar();
					destructor.apuntar(entorno,astroMegaShip);
				} else {
					destructor.movilizar();
					destructor.NoApuntar();
				}

				destructor.dibujar(entorno, imagenDelDestructor);

				if (rayosEnemigos[j] == null && numeroAleatorio > 95)
					rayosEnemigos[j] = destructor.disparar();

				if (!destructor.estaDibujando(entorno)) {
					fueraDePantalla++;
				}

				if (fueraDePantalla == enemigosRestantes) {
					mezclarDestructoresRestantes();
				}
				
				if (destructor.estaEnRango(astroMegaShip) && !destructor.estaDestruido()) {
					destructor.golpear(astroMegaShip);
					destructor.destruir();
				}
				
				if (destructor.estaDestruido()) {
					destructorEliminado();
					destructores[j] = null;
				}
			}
		}

		// ----------------------------ASTEROIDES--------------------------------
		for (int i = 0; i < asteroides.length; i++) {
			Asteroide asteroide = asteroides[i];

			if (asteroide.estaDestruido()) {
				asteroides[i] = nuevoAsteroide();
			}

			asteroide.dibujar(entorno, imagenDelAsteroide);
			asteroide.mover();

			if (asteroide.estaEnRango(astroMegaShip) && !asteroide.estaDestruido()) {
				asteroide.golpear(astroMegaShip);
				asteroide.destruir();
			}
//			if(asteroide.estaExplotando() && !asteroide.estaChocando(astroMegaShip)) {
//			asteroide.destruir();
//			}

//			int indexDelColisionado = comprobarColisionEntreAsteroides(i);
//			if (indexDelColisionado >= 0) {
//				asteroide.explotar(imagenExplosionAsteroide);
//				this.asteroides[indexDelColisionado].explotar(imagenExplosionAsteroide);
//			}

//			if (asteroide.estaChocando(astroMegaShip) && !asteroide.estaExplotando()) {
//				asteroide.golpear(astroMegaShip);
//				asteroide.explotar();
//				asteroide.dibujar(entorno, imagenExplosionAsteroide);
//			}
		}
		// if (astroMegaShip != null) {

//		astroMegaShip.comprobarColisionCon(asteroides);

		// IMPRIMIR EN PANTALLA
		// ----------------------------ASTROMEGASHIP--------------------------------
		if (astroMegaShip != null) {
			astroMegaShip.dibujar(entorno, imagenAstroMegaShip);

			if (entorno.sePresiono(entorno.TECLA_ESPACIO)) {
				if (rayoAstro == null)
					rayoAstro = astroMegaShip.disparar();
			}

			if (entorno.estaPresionada(entorno.TECLA_DERECHA))
				astroMegaShip.moverDerecha(entorno);

			if (entorno.estaPresionada(entorno.TECLA_IZQUIERDA))
				astroMegaShip.moverIzquierda(entorno);

			if (astroMegaShip.estaDestruido()) {
				estaPerdido = true;
				return;
			}
		}

		infoDelJuego();
	}
	
	// --------------------- FIN DE LOS TICKS ---------------------

	
//	private int comprobarColisionEntreAsteroides(int index) {
//		Asteroide asteroide = this.asteroides[index];
//		for (int i = 0; i < this.asteroides.length; i++) {
//			if (i == index)
//				continue;
//
//			boolean chocaron = estanChocando(asteroide.tamanio(), this.asteroides[i].tamanio());
//
//			if (chocaron) {
//				return i;
//			}
//		}
//		return -1;
//	}

	private Asteroide nuevoAsteroide() {
		double randomX = Math.random() * 800 + 1;
		double randomY = (Math.random() * 100 + 1) - 200;
		double randomAngulo = Math.random() * 80 + 50;
		double randomVelocidad = Math.random() * 1.5 + 1;
		return new Asteroide(randomX, randomY, 25, randomVelocidad, Herramientas.radianes(randomAngulo));
	}

	// -------------------------------------------------------------------------------
	private Image cargarImagen(String archivo) {
		return Herramientas.cargarImagen(archivo);
	}

	private void destructorEliminado() {
		this.enemigosRestantes--;
		this.cantEliminados++;
	}

	private void calcularPuntaje() {
		this.puntaje = this.cantEliminados * puntosPorDestructor;
	}

	private void infoDelJuego() {
		calcularPuntaje();

		if (enemigosRestantes == 0) {

			nivel++;

			subioDeNivel = true;

			if (nivel == 5) {
				estaGanado = true;
				return;
			}

			cargarNivel(nivel);

		}

		Pantalla.dibujarPuntaje(entorno, this.puntaje);
		Pantalla.dibujarCantidadEliminados(entorno, this.cantEliminados);

	}

	// -------------------------------------------------------------------------------

	private void cargarNivel(int nivel) {

		String distribucion = null;
		switch (nivel) {
		case 1:
			distribucion = Nivel.uno();
			break;
		case 2:
			distribucion = Nivel.dos();
			break;
		case 3:
			distribucion = Nivel.tres();
			break;
		case 4:
			distribucion = Nivel.boss();
			break;
		default:
			distribucion = null;
			break;
		}

		destructores = new Destructor[distribucion.length()];
		rayosEnemigos = new Rayo[distribucion.length()];

		for (int i = 0; i < destructores.length; i++) {
			if (distribucion.charAt(i) == '1') {
				enemigosRestantes++;

				if (nivel % 4 != 0) {
					int numeroAleatorio = random.nextInt(200);

					while (numeroAleatorio < 60) {

						numeroAleatorio = random.nextInt(200);
					}

					destructores[i] = new Destructor(85 + (i * 70), -numeroAleatorio , 60, 60, 1* nivel ,
							Herramientas.radianes(90));
				} else {
					destructores[i] = new Destructor(85 + (i * 70), 120, 60, 60, 1, Herramientas.radianes(90));
				}

			}
		}

	}

	private void dibujarFondo() {
		p -= .3;
		if (p <= 0) {
			p = 600;
		}
		entorno.dibujarImagen(imagenFondo, 400, p - 300, 0);
		entorno.dibujarImagen(imagenFondo, 400, p + 300, 0);
	}

	private void mezclarDestructoresRestantes() {
		Destructor[] nuevaDistribucion = new Destructor[destructores.length];

		for (int i = 0; i < enemigosRestantes; i++) {

			while (true) {
				int posicionAleatoria = random.nextInt(destructores.length);
				if (nuevaDistribucion[posicionAleatoria] == null) {

					int numeroAleatorio = random.nextInt(200);

					while (numeroAleatorio < 60) {
						numeroAleatorio = random.nextInt(200);
					}

					nuevaDistribucion[posicionAleatoria] = new Destructor(85 + (posicionAleatoria * 70),
							-numeroAleatorio, 60, 60, 1 * nivel, Herramientas.radianes(90));
					break;
				}

			}
		}

		for (int j = 0; j < destructores.length; j++) {
			destructores[j] = nuevaDistribucion[j];
		}

		nuevaDistribucion = null;
	}

	private void instanciarAsteroides(int cantidad) {
		this.asteroides = new Asteroide[cantidad];
		for (int i = 0; i < asteroides.length; i++) {
			asteroides[i] = nuevoAsteroide();
		}
	}
	@SuppressWarnings("unused")
	public static void main(String[] args) {
		Juego juego = new Juego();
	}
}
