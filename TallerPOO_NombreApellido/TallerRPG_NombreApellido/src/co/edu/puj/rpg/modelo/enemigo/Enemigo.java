package co.edu.puj.rpg.modelo.enemigo;

import co.edu.puj.rpg.modelo.Personaje;

public class Enemigo {
    private String nombre;
    private int puntosVida;
    private int vidaMaxima;
    private int puntosAtaque;
    private boolean estaVivo;
    private double recompensaOro;
    private int recompensaExperiencia;

    public Enemigo(String nombre, int vidaMaxima, int puntosAtaque,
                   double recompensaOro, int recompensaExperiencia) {
        this.nombre = nombre;
        this.vidaMaxima = Math.max(1, vidaMaxima);
        this.puntosAtaque = Math.max(0, puntosAtaque);
        this.recompensaOro = Math.max(0.0, recompensaOro);
        this.recompensaExperiencia = Math.max(0, recompensaExperiencia);
        this.estaVivo = true;
        setPuntosVida(this.vidaMaxima);
    }

    public void atacar(Personaje heroe) {
        heroe.recibirDano(puntosAtaque);
    }

    public void recibirDano(int cantidadDano) {
        if (cantidadDano > 0) {
            setPuntosVida(puntosVida - cantidadDano);
        }
    }

    private void setPuntosVida(int vida) {
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

    public String getNombre() { return nombre; }
    public int getPuntosVida() { return puntosVida; }
    public int getVidaMaxima() { return vidaMaxima; }
    public int getPuntosAtaque() { return puntosAtaque; }
    public boolean isEstaVivo() { return estaVivo; }
    public double getRecompensaOro() { return recompensaOro; }
    public int getRecompensaExperiencia() { return recompensaExperiencia; }
}
