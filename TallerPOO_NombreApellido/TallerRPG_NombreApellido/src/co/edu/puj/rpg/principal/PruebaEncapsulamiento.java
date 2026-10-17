package co.edu.puj.rpg.principal;

import co.edu.puj.rpg.modelo.Personaje;

public class PruebaEncapsulamiento {
    public static void main(String[] args) {
        int[] valores = {-10, 0, 100000};
        for (int valor : valores) {
            Personaje heroe = new Personaje("Aragorn", 100, 15, 50.0);
            heroe.setPuntosVida(valor);
            System.out.printf("setPuntosVida(%d) -> vida=%d | vivo=%b%n",
                    valor, heroe.getPuntosVida(), heroe.isEstaVivo());
        }

        Personaje p = new Personaje("Aragorn", 100, 15, 50.0);
        boolean r1 = p.setOro(-10);
        System.out.printf("setOro(-10) -> aceptado=%b | oro=%.2f%n", r1, p.getOro());
        boolean r2 = p.setOro(0);
        System.out.printf("setOro(0) -> aceptado=%b | oro=%.2f%n", r2, p.getOro());
        boolean r3 = p.setOro(100000);
        System.out.printf("setOro(100000) -> aceptado=%b | oro=%.2f%n", r3, p.getOro());

        Personaje malo = new Personaje("Debil", -50, -5, -20.0);
        System.out.printf("Constructor con datos inválidos -> vidaMax=%d | vida=%d | ataque=%d | oro=%.2f | vivo=%b%n",
                malo.getVidaMaxima(), malo.getPuntosVida(), malo.getPuntosAtaque(), malo.getOro(), malo.isEstaVivo());
    }
}
