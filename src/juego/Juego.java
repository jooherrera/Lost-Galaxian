package juego;

import entorno.Entorno;
import entorno.Herramientas;
import entorno.InterfaceJuego;
import java.awt.Image;
import java.util.Random;

public class Juego extends InterfaceJuego {

  // El objeto Entorno que controla el tiempo y otros
  private Entorno entorno;
  // Estado del juego
  private int puntajeTotal;
  private int totalEliminados;
  private int enemigosRestantes;
  private int PUNTOS_POR_DESTRUCTOR;
  private int PUNTOS_POR_BOSS;
  private double VELOCIDAD_IMAGEN_FONDO;
  private boolean estaGanado;
  private boolean estaPerdido;
  private int nivel;
  private boolean subioDeNivel;
  private int timerGlobal;
  private int temporizador;

  // Imagenes
  private Image imagenFondo = Herramientas.cargarImagen("imagenes/fondo.png");
  private Image imagenDelRayo = Herramientas.cargarImagen("imagenes/rayo2.png");
  private Image imagenDelRayoIon = Herramientas.cargarImagen(
    "imagenes/rayoIonDestructor.png"
  );
  private Image imagenDelAsteroide = Herramientas.cargarImagen(
    "imagenes/asteroide.png"
  );
  private Image imagenDelDestructor = Herramientas.cargarImagen(
    "imagenes/destructor2.png"
  );
  private Image imagenDelBoss = Herramientas.cargarImagen("imagenes/boss.png");
  private Image imagenAstroMegaShip = Herramientas.cargarImagen(
    "imagenes/astro.png"
  );
  private Image imagenDelRayoBoss = Herramientas.cargarImagen(
    "imagenes/rayoBoss.png"
  );

  // Entidades
  private Rayo rayoAstro;
  private RayoBoss rayoBoss;
  private Boss boss;
  private Asteroide[] asteroides;
  private Destructor[] destructores;
  private RayoIon[] rayosEnemigos;
  private AstroMegaShip astroMegaShip;

  // Utiles
  private Random random = new Random();
  private double posicionImagenFondo;

  Juego() {
    this.entorno =
      new Entorno(this, "Lost Galaxian - Grupo ... - v1", 800, 600);
    Pantalla.setearEntorno(this.entorno);
    this.nivel = 1;
    this.puntajeTotal = 0;
    this.totalEliminados = 0;
    this.PUNTOS_POR_DESTRUCTOR = 10;
    this.PUNTOS_POR_BOSS = 50;
    this.VELOCIDAD_IMAGEN_FONDO = 0.3;
    this.enemigosRestantes = 0;
    this.estaGanado = false;
    this.estaPerdido = false;
    this.subioDeNivel = false;
    this.posicionImagenFondo = 600;
    this.timerGlobal = 0;
    this.temporizador = 0;

    this.astroMegaShip =
      new AstroMegaShip(400, 500, 70, 68, 3, Herramientas.radianes(270));
    instanciarAsteroides(4);
    cargarNivel(nivel);
    this.entorno.iniciar();
  }

  public void tick() {
    timerGlobal++;
    temporizador++;

    if (estaGanado) {
      Pantalla.dibujarCartelGanado();

      if (entorno.sePresiono('s')) salirDelJuego();

      if (entorno.sePresiono('r')) reiniciarJuego(this);

      return;
    }

    if (estaPerdido) {
      Pantalla.dibujarCartelPerdido();
      if (entorno.sePresiono('s')) salirDelJuego();

      if (entorno.sePresiono('r')) reiniciarJuego(this);
      return;
    }

    if (subioDeNivel) {
      if (
        nivel % 4 == 0
      ) Pantalla.dibujarCartelBoss(); else Pantalla.dibujarCartelNivel(nivel);

      // Hace titilar el "Press ENTER" dependiendo de time.
      if (timerGlobal / 60 % 2 == 0) Pantalla.dibujarPressEnter();

      // Espera el enter para cambiar al proximo nivel.
      if (entorno.sePresiono(entorno.TECLA_ENTER)) subioDeNivel = false;
      return;
    }

    // ----------------------- DIBUJA EL FONDO ----------------------
    posicionImagenFondo -= VELOCIDAD_IMAGEN_FONDO;
    if (posicionImagenFondo <= 0) posicionImagenFondo = 600;

    entorno.dibujarImagen(imagenFondo, 400, posicionImagenFondo - 300, 0);
    entorno.dibujarImagen(imagenFondo, 400, posicionImagenFondo + 300, 0);

    // -----------------------------BOSS ----------------------------
    if (boss != null) {
      boss.dibujar(entorno, imagenDelBoss);
      boss.apuntar(astroMegaShip);
      if (rayoBoss == null) rayoBoss = boss.disparar();

      if (boss.estaDestruido()) {
        enemigoEliminado(PUNTOS_POR_BOSS);
        boss = null;
      }
    }

    // -----------------------------RAYO BOSS ----------------------------
    if (rayoBoss != null) {
      rayoBoss.dibujar(entorno, imagenDelRayoBoss);
      rayoBoss.mover();
      rayoBoss.golpear(astroMegaShip);
      if (rayoBoss.estaDestruido()) rayoBoss = null;
    }

    // -----------------------------RAYO ASTROMEGASHIP ----------------------------
    if (rayoAstro != null) {
      rayoAstro.dibujar(entorno, imagenDelRayo);
      rayoAstro.mover();
      rayoAstro.golpear(destructores);
      rayoAstro.golpear(asteroides);
      rayoAstro.golpear(boss);
      if (rayoAstro.estaDestruido()) rayoAstro = null;
    }

    // -----------------------------RAYOS ENEMIGOS ----------------------------
    for (int r = 0; r < rayosEnemigos.length; r++) {
      RayoIon rayoEnemigo = rayosEnemigos[r];
      if (rayoEnemigo == null) continue;
      rayoEnemigo.dibujar(entorno, imagenDelRayoIon);
      rayoEnemigo.mover();
      rayoEnemigo.golpear(astroMegaShip);
      if (rayoEnemigo.estaDestruido()) rayosEnemigos[r] = null;
    }

    // ----------------------------DESTRUCTORES--------------------------------
    int fueraDePantalla = 0;
    for (int j = 0; j < destructores.length; j++) {
      Destructor destructor = destructores[j];
      if (destructor == null) continue;
      boolean esPar = j % 2 == 0 ? true : false;
      destructor.dibujar(entorno, imagenDelDestructor);
      destructor.mover();

      //------ MOVIMIENTO EN ZIG-ZAG -----
      if (temporizador / 60 > 2) {
        destructor.moverDerecha(entorno);
        if (temporizador / 60 > 5) temporizador = 0;
      } else {
        destructor.moverIzquierda(entorno);
      }
      //------ FIN MOVIMIENTO EN ZIG-ZAG ------

      // ------ EN NIVELES BOSS LOS DESTRUCTORES APUNTAN -----
      if (nivel % 4 == 0) destructor.apuntar(astroMegaShip);
      if (esPar) {
        destructor.kamikaze();
        if (
          destructor.posicion().getY() > astroMegaShip.posicion().getY() - 30
        ) {
          destructor.movilizar();
          destructor.NoApuntar();
        }
      }

      int numeroAleatorio = random.nextInt(100);
      if (rayosEnemigos[j] == null && numeroAleatorio > 95) rayosEnemigos[j] =
        destructor.disparar();

      if (!destructor.estaDibujando(entorno)) {
        fueraDePantalla++;
      }

      if (fueraDePantalla == enemigosRestantes) {
        mezclarDestructoresRestantes();
      }

      destructor.golpear(astroMegaShip);

      if (destructor.estaDestruido()) {
        enemigoEliminado(PUNTOS_POR_DESTRUCTOR);
        destructores[j] = null;
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
      asteroide.golpear(astroMegaShip);
    }

    // ----------------------------ASTROMEGASHIP--------------------------------
    if (astroMegaShip != null) {
      astroMegaShip.dibujar(entorno, imagenAstroMegaShip);
      if (
        entorno.sePresiono(entorno.TECLA_ESPACIO) && rayoAstro == null
      ) rayoAstro = astroMegaShip.disparar();

      if (
        entorno.estaPresionada(entorno.TECLA_DERECHA) ||
        entorno.estaPresionada('d')
      ) astroMegaShip.moverDerecha(entorno);

      if (
        entorno.estaPresionada(entorno.TECLA_IZQUIERDA) ||
        entorno.estaPresionada('a')
      ) astroMegaShip.moverIzquierda(entorno);

      if (astroMegaShip.estaDestruido()) {
        estaPerdido = true;
        return;
      }
    }
    //Imprime la info del juego en pantalla.
    infoDelJuego();
  }

  // --------------------- FIN DE LOS TICKS ---------------------

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
      Herramientas.radianes(randomAngulo)
    );
  }

  private void enemigoEliminado(int puntaje) {
    enemigosRestantes--;
    totalEliminados++;
    puntajeTotal += puntaje;
  }

  private void infoDelJuego() {
    Pantalla.dibujarPuntaje(puntajeTotal);
    Pantalla.dibujarCantidadEliminados(totalEliminados);
    Pantalla.dibujarBossStatus(boss);
    Pantalla.dibujarAstroMegaShipStatus(astroMegaShip);

    if (enemigosRestantes == 0) {
      nivel++;
      subioDeNivel = true;
      if (nivel == 5) {
        estaGanado = true;
        return;
      }
      cargarNivel(nivel);
    }
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
    rayosEnemigos = new RayoIon[distribucion.length()];

    for (int i = 0; i < destructores.length; i++) {
      //  SI ES 1 ES UN DESTRUCTOR
      if (distribucion.charAt(i) == '1') {
        enemigosRestantes++;

        if (nivel % 4 != 0) {
          int numeroAleatorio = random.nextInt(200);
          while (numeroAleatorio < 60) {
            numeroAleatorio = random.nextInt(200);
          }

          destructores[i] =
            new Destructor(
              85 + (i * 70),
              -numeroAleatorio,
              60,
              60,
              1 * nivel,
              Herramientas.radianes(90)
            );
        } else {
          destructores[i] =
            new Destructor(
              85 + (i * 70),
              120,
              60,
              60,
              1,
              Herramientas.radianes(90)
            );
          destructores[i].inmovilizar();
        }
        continue;
      }
      //SI ES 2 ES UN BOSS
      if (distribucion.charAt(i) == '2') {
        enemigosRestantes++;
        boss = new Boss(400, 150, 100, 73, 1000, Herramientas.radianes(90));
      }
    }
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

          nuevaDistribucion[posicionAleatoria] =
            new Destructor(
              85 + (posicionAleatoria * 70),
              -numeroAleatorio,
              60,
              60,
              1 * nivel,
              Herramientas.radianes(90)
            );
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

  public static void salirDelJuego() {
    System.exit(0);
  }

  public static void reiniciarJuego(Juego juego) {
    juego = new Juego();
  }
}
