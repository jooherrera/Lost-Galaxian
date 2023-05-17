package juego;

import entorno.Entorno;
import entorno.Herramientas;
import entorno.InterfaceJuego;
import java.awt.Image;
import java.awt.Point;

public class Juego extends InterfaceJuego {

  // El objeto Entorno que controla el tiempo y otros
  private Entorno entorno;
  // Estado del juego
  private int puntaje;
  private int cantEliminados;
  private int puntosPorDestructor;
  private boolean estaGanado;
  private boolean estaPerdido;

  // Imagenes
  private Image imagenDelRayo = cargarImagen("imagenes/rayo2.png");
  private Image imagenDelAsteroide = cargarImagen("imagenes/asteroide.png");
  private Image imagenExplosionAsteroide = cargarImagen(
    "imagenes/explosionAsteroide.png"
  );
  // Entidades
  private Rayo rayo;
  private Asteroide[] asteroides;
  private Image imagenAstroMS = Herramientas.cargarImagen("imagenes/nave3.png");
  private AstroMegaShip astroMegaShip;

  Juego() {
    // Inicializa el objeto entorno
    this.entorno =
      new Entorno(this, "Lost Galaxian - Grupo ... - v1", 800, 600);
    // Rayo
    this.rayo = new Rayo(500, 150, 1, Herramientas.radianes(45), imagenDelRayo);
    // Asteroides
    instanciarAsteroides(7);
//    this.rayo = new Rayo(500, 50, 4, Herramientas.radianes(180), imagenDelRayo);
    // AstroMegaShip
    this.astroMegaShip =
      new AstroMegaShip(400, 500, 2, Herramientas.radianes(0), imagenAstroMS);
    //
    this.puntaje = 0;
    this.cantEliminados = 0;
    this.puntosPorDestructor = 10;

    this.estaGanado = false;
    this.estaPerdido = false;
    this.entorno.iniciar();
  }

  public void tick() {
    if (estaGanado) {
      mostrarMensajeGanado();
      return;
    }

    if (estaPerdido) {
      mostrarMensajePerdido();
      return;
    }

     if (rayo != null) {
     rayo.dibujar(entorno);
//     rayo.mover();
     if (rayo.posicion().getY() > 500)
     rayo = null;
     }

    for (int i = 0; i < asteroides.length; i++) {
      Asteroide asteroide = asteroides[i];
      asteroide.dibujar(entorno);
      asteroide.mover();

      if (asteroide.estaDestruido()) {
        // asteroide.golpear(astroMegaShip);
        asteroides[i] = nuevoAsteroide();
        destructorEliminado();
        continue;
      }

      if (
        !asteroide.estaDibujando(entorno) || asteroide.estaExplotando()
      ) asteroide.destruir();

      int indexDelColisionado = comprobarColisionEntreAsteroides(i);
      if (indexDelColisionado >= 0) {
        asteroide.explotar(imagenExplosionAsteroide);
        this.asteroides[indexDelColisionado].explotar(imagenExplosionAsteroide);
      }
    }
    // if (astroMegaShip != null) {
    this.astroMegaShip.dibujar(this.entorno);
    if (
      this.entorno.estaPresionada(this.entorno.TECLA_DERECHA) &&
      this.astroMegaShip.getX() +
      this.astroMegaShip.getAncho() /
      2 <
      this.entorno.ancho() ||
      (
        this.entorno.estaPresionada('d') &&
        this.astroMegaShip.getX() +
        this.astroMegaShip.getAncho() /
        2 <
        this.entorno.ancho()
      )
    ) {
      this.astroMegaShip.moverDerecha();
    }
    if (
      this.entorno.estaPresionada(this.entorno.TECLA_IZQUIERDA) &&
      this.astroMegaShip.getX() - this.astroMegaShip.getAncho() / 2 > 0 ||
      (
        this.entorno.estaPresionada('a') &&
        this.astroMegaShip.getX() - this.astroMegaShip.getAncho() / 2 > 0
      )
    ) {
      this.astroMegaShip.moverIzquierda();
    }

    // IMPRIMIR EN PANTALLA
    infoDelJuego();
    infoDeAstroMegaShip(astroMegaShip);
  }

  private void instanciarAsteroides(int cantidad) {
    this.asteroides = new Asteroide[cantidad];
    for (int i = 0; i < asteroides.length; i++) {
      asteroides[i] = nuevoAsteroide();
    }
  }

  private int comprobarColisionEntreAsteroides(int index) {
    Asteroide asteroide = this.asteroides[index];
    for (int i = 0; i < this.asteroides.length; i++) {
      if (i == index) continue;

      boolean chocaron = estanChocando(
        asteroide.tamanio(),
        this.asteroides[i].tamanio()
      );

      if (chocaron) {
        return i;
      }
    }
    return -1;
  }

  private Boolean estanChocando(Point[] objeto1, Point[] objeto2) {
    boolean valor = false;
    for (int i = 0; i < 4; i++) {
      Boolean estaDentroDelRangoX =
        objeto1[0].getX() < objeto2[i].getX() &&
        objeto2[i].getX() < objeto1[2].getX();
      Boolean estaDentroDelRangoY =
        objeto1[1].getY() < objeto2[i].getY() &&
        objeto2[i].getY() < objeto1[3].getY();

      if (estaDentroDelRangoX && estaDentroDelRangoY) valor = true;
    }
    return valor;
  }

  private Asteroide nuevoAsteroide() {
    double randomX = Math.random() * 800 + 1;
    double randomY = (Math.random() * 100 + 1) - 200;
    double randomAngulo = Math.random() * 80 + 50;
    double randomVelocidad = Math.random() * 1.5 + 1;
    return new Asteroide(
      randomX,
      randomY,
      25,
      randomVelocidad,
      Herramientas.radianes(randomAngulo),
      imagenDelAsteroide
    );
  }

  private Image cargarImagen(String archivo) {
    return Herramientas.cargarImagen(archivo);
  }

  private void destructorEliminado() {
    this.cantEliminados++;
  }

  private void calcularPuntaje() {
    this.puntaje = this.cantEliminados * puntosPorDestructor;
  }

  private void mostrarMensajeGanado() {
    Pantalla.dibujarCartelGanado(entorno);
  }

  private void mostrarMensajePerdido() {
    Pantalla.dibujarCartelPerdido(entorno);
  }

  private void infoDelJuego() {
    calcularPuntaje();
    if (puntaje > 50000) {
      estaGanado = true;
      return;
    }

    Pantalla.dibujarPuntaje(entorno, this.puntaje);
    Pantalla.dibujarCantidadEliminados(entorno, this.cantEliminados);
  }

  private void infoDeAstroMegaShip(AstroMegaShip nave) {
    Pantalla.dibujarVidaAstro(entorno, nave.hpDisponible());
    estaPerdido = nave.hpDisponible() <= 0 ? true : false;
  }

  @SuppressWarnings("unused")
  public static void main(String[] args) {
    Juego juego = new Juego();
  }
}
