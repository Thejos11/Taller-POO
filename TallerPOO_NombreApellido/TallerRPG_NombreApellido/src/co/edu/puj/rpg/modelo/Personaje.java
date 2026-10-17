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
    private int pociones;
    private int ultimoDano;
    private boolean ultimoGolpeCritico;

    public Personaje() {
        this("Héroe", 100, 10, 0.0);
    }

    public Personaje(String nombre, int vidaMaxima, int puntosAtaque, double oro) {
        this.nombre = nombre;
        this.vidaMaxima = Math.max(1, vidaMaxima);
        this.puntosAtaque = Math.max(0, puntosAtaque);
        this.oro = Math.max(0.0, oro);
        this.nivel = 1;
        this.experiencia = 0;
        this.pociones = 1;
        this.estaVivo = true;
        setPuntosVida(this.vidaMaxima);
    }

    public void atacar(Enemigo enemigo) {
        int dano = puntosAtaque;
        ultimoGolpeCritico = Math.random() < 0.15;
        if (ultimoGolpeCritico) {
            dano = dano * 2;
        }
        ultimoDano = dano;
        enemigo.recibirDano(dano);
    }

    public void usarPocion(int cantidadRestaurada) {
        if (cantidadRestaurada > 0) {
            setPuntosVida(puntosVida + cantidadRestaurada);
        }
    }

    public void recibirDano(int cantidadDano) {
        if (cantidadDano > 0) {
            setPuntosVida(puntosVida - cantidadDano);
        }
    }

    public boolean ganarExperiencia(int puntos) {
        boolean subioNivel = false;
        if (puntos > 0) {
            experiencia += puntos;
            while (experiencia >= umbralExperiencia()) {
                experiencia -= umbralExperiencia();
                setNivel(nivel + 1);
                subioNivel = true;
            }
        }
        return subioNivel;
    }

    private int umbralExperiencia() {
        return nivel * 100;
    }

    public void agregarPocion() {
        pociones++;
    }

    public boolean consumirPocion() {
        if (pociones > 0) {
            pociones--;
            return true;
        }
        return false;
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

    public boolean setOro(double oro) {
        if (oro < 0.0) {
            return false;
        }
        this.oro = oro;
        return true;
    }

    protected void setNivel(int nivel) {
        if (nivel >= 1) {
            this.nivel = nivel;
        }
    }

    public String getNombre() { return nombre; }
    public int getPuntosVida() { return puntosVida; }
    public int getVidaMaxima() { return vidaMaxima; }
    public int getPuntosAtaque() { return puntosAtaque; }
    public int getNivel() { return nivel; }
    public double getOro() { return oro; }
    public boolean isEstaVivo() { return estaVivo; }
    public int getExperiencia() { return experiencia; }
    public int getPociones() { return pociones; }
    public int getUltimoDano() { return ultimoDano; }
    public boolean isUltimoGolpeCritico() { return ultimoGolpeCritico; }
}
