package juego;
import java.awt.Color;
import java.awt.Image;

import entorno.Entorno;

public class Destructor {
    private double x;
    private double y;
    private double ancho;
    private double alto;
    private double velocidadx;
    private int direccion;
    private Image imagen;
    private double velocidadY;
    private double cambioDireccion;
 
    public Destructor(double x, double y, double ancho, double alto, Image imagenDestructores, Image imagen) {
        this.x = x;
        this.y = y;
        this.ancho = 4;
        this.alto = 4;
        this.imagen = imagen;
        this.velocidadx = 1; // Ajusta la velocidad en el eje X
        this.velocidadY = 1; // Ajusta la velocidad en el eje Y
        this.cambioDireccion = 70; // Ajusta el cambio de dirección en el eje X
    }



    public void dibujarse(Entorno entorno) {
        entorno.dibujarImagen(this.imagen, this.x , this.y , 0, 0.15);
    }

    public void mover(int anchoEntorno, int altoEntorno) {
        double angulo = 0;
		double velocidad = 0;
		double nuevoX = this.x + Math.cos(angulo) * velocidad;
        double nuevoY = this.y + Math.sin(angulo) * velocidad;

        if (nuevoX >= 0 && nuevoX <= anchoEntorno && nuevoY >= 0 && nuevoY <= altoEntorno) {
            this.x = nuevoX;
            this.y = nuevoY;
        }
    
        this.x += this.velocidadx;
        this.y += this.velocidadY;

        // Cambia la dirección en el eje X cada cierta cantidad de pasos
        if (this.y % this.cambioDireccion == 0) {
            this.velocidadx = -this.velocidadx;
        }
    }

    

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getAncho() {
        return ancho;
    }

    public double getAlto() {
        return alto;
    }

    public double getVelocidad() {
        return velocidadx;
    }

    public int getDireccion() {
        return direccion;
    }

    public double getVelocidadY() {
        return velocidadY;
    }
}