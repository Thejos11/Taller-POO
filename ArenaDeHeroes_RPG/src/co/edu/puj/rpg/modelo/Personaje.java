package co.edu.puj.rpg.modelo;

import co.edu.puj.rpg.modelo.enemigo.Enemigo;

public class Personaje {

    private String nombre;
    private int puntosVida;
    private int vidaMaxima;
    private int puntosAtaque;
    private int nivel;
    private double oro;
    private boolean estaVivo;
    private int experiencia;
    private int ultimoDano;
    private boolean ultimoGolpeCritico;

    public Personaje() {
        this("Heroe", 100, 10, 0.0);
    }

    public Personaje(String nombre, int vidaMaxima, int puntosAtaque, double oro) {
        this.nombre = nombre;
        this.vidaMaxima = vidaMaxima > 0 ? vidaMaxima : 1;
        this.puntosAtaque = Math.max(puntosAtaque, 0);
        this.oro = oro < 0.0 ? 0.0 : oro;
        this.nivel = 1;
        this.experiencia = 0;
        this.estaVivo = true;
        this.setPuntosVida(this.vidaMaxima);
    }

    public void atacar(Enemigo enemigo) {
        if (!estaVivo || enemigo == null || !enemigo.isEstaVivo()) {
            ultimoDano = 0;
            ultimoGolpeCritico = false;
            return;
        }
        ultimoGolpeCritico = Math.random() < 0.15;
        int dano = ultimoGolpeCritico ? puntosAtaque * 2 : puntosAtaque;
        ultimoDano = dano;
        enemigo.recibirDano(dano);
    }

    public void usarPocion(int cantidadRestaurada) {
        if (!estaVivo || cantidadRestaurada <= 0) {
            return;
        }
        setPuntosVida(puntosVida + cantidadRestaurada);
    }

    public void recibirDano(int cantidadDano) {
        if (cantidadDano <= 0) {
            return;
        }
        setPuntosVida(puntosVida - cantidadDano);
    }

    public boolean ganarExperiencia(int puntos) {
        if (puntos <= 0) {
            return false;
        }
        boolean subioNivel = false;
        experiencia += puntos;
        while (experiencia >= getExperienciaRequerida()) {
            experiencia -= getExperienciaRequerida();
            setNivel(nivel + 1);
            vidaMaxima += 10;
            puntosAtaque += 2;
            setPuntosVida(vidaMaxima);
            subioNivel = true;
        }
        return subioNivel;
    }

    public void setPuntosVida(int vida) {
        if (vida <= 0) {
            this.puntosVida = 0;
            this.estaVivo = false;
        } else if (vida > vidaMaxima) {
            this.puntosVida = vidaMaxima;
            this.estaVivo = true;
        } else {
            this.puntosVida = vida;
            this.estaVivo = true;
        }
    }

    public void setOro(double oro) {
        if (oro < 0.0) {
            System.out.println("Saldo insuficiente: la operacion fue rechazada.");
            return;
        }
        this.oro = oro;
    }

    protected void setNivel(int nivel) {
        if (nivel < 1) {
            return;
        }
        this.nivel = nivel;
    }

    public String getNombre() {
        return nombre;
    }

    public int getPuntosVida() {
        return puntosVida;
    }

    public int getVidaMaxima() {
        return vidaMaxima;
    }

    public int getPuntosAtaque() {
        return puntosAtaque;
    }

    public int getNivel() {
        return nivel;
    }

    public double getOro() {
        return oro;
    }

    public boolean isEstaVivo() {
        return estaVivo;
    }

    public int getExperiencia() {
        return experiencia;
    }

    public int getExperienciaRequerida() {
        return nivel * 50;
    }

    public int getUltimoDano() {
        return ultimoDano;
    }

    public boolean isUltimoGolpeCritico() {
        return ultimoGolpeCritico;
    }
}
