package co.edu.puj.rpg.principal;

import co.edu.puj.rpg.modelo.Personaje;
import co.edu.puj.rpg.modelo.enemigo.Enemigo;

import java.util.Locale;
import java.util.Scanner;

public class Main {
    private static final double COSTO_POCION_GRANDE = 30.0;
    private static final int CURA_POCION_GRANDE = 50;

    public static void main(String[] args) {
        Locale.setDefault(Locale.US);
        Scanner sc = new Scanner(System.in);

        Personaje heroe = new Personaje("Aragorn", 100, 15, 60.0);
        Enemigo enemigo = new Enemigo("Orco Bruto", 80, 12, 40.0, 120);
        StringBuilder historial = new StringBuilder();
        boolean huyo = false;

        System.out.printf("=== ARENA DE HÉROES ===%n");
        System.out.printf("%s se enfrenta a %s%n", heroe.getNombre(), enemigo.getNombre());

        do {
            System.out.printf("%n%s: %d/%d HP | %s: %d/%d HP%n",
                    heroe.getNombre(), heroe.getPuntosVida(), heroe.getVidaMaxima(),
                    enemigo.getNombre(), enemigo.getPuntosVida(), enemigo.getVidaMaxima());
            System.out.printf("1. Atacar%n2. Usar Poción%n3. Ver Estado%n4. Huir%n5. Tienda%n");
            System.out.printf("Seleccione una opción: ");

            int opcion = leerEntero(sc);
            boolean turnoConsumido = false;

            if (opcion < 1 || opcion > 5) {
                System.out.printf("Opción inválida. Ingrese un número entre 1 y 5.%n");
            } else {
                switch (opcion) {
                    case 1:
                        heroe.atacar(enemigo);
                        historial.append(String.format("%s ataca a %s y causa %d de daño%s.%n",
                                heroe.getNombre(), enemigo.getNombre(), heroe.getUltimoDano(),
                                heroe.isUltimoGolpeCritico() ? " (¡GOLPE CRÍTICO!)" : ""));
                        turnoConsumido = true;
                        break;
                    case 2:
                        if (heroe.getPuntosVida() == heroe.getVidaMaxima()) {
                            System.out.printf("Ya tienes la vida completa.%n");
                        } else if (heroe.consumirPocion()) {
                            int vidaAntes = heroe.getPuntosVida();
                            heroe.usarPocion(CURA_POCION_GRANDE);
                            historial.append(String.format("%s usa una poción y recupera %d de vida.%n",
                                    heroe.getNombre(), heroe.getPuntosVida() - vidaAntes));
                            turnoConsumido = true;
                        } else {
                            System.out.printf("No tienes pociones en el inventario.%n");
                        }
                        break;
                    case 3:
                        mostrarEstado(heroe);
                        break;
                    case 4:
                        huyo = true;
                        historial.append(String.format("%s huye de la batalla.%n", heroe.getNombre()));
                        break;
                    case 5:
                        abrirTienda(sc, heroe, historial);
                        break;
                    default:
                        break;
                }
            }

            if (turnoConsumido && enemigo.isEstaVivo()) {
                enemigo.atacar(heroe);
                historial.append(String.format("%s ataca a %s y causa %d de daño.%n",
                        enemigo.getNombre(), heroe.getNombre(), enemigo.getPuntosAtaque()));
                if (!heroe.isEstaVivo()) {
                    historial.append(String.format("%s ha sido derrotado.%n", heroe.getNombre()));
                }
            }
            if (turnoConsumido && !enemigo.isEstaVivo()) {
                historial.append(String.format("%s ha sido derrotado.%n", enemigo.getNombre()));
            }

            if (turnoConsumido || huyo) {
                System.out.printf("%n--- HISTORIAL DE BATALLA ---%n%s", historial);
            }
        } while (heroe.isEstaVivo() && enemigo.isEstaVivo() && !huyo);

        if (huyo) {
            System.out.printf("%nHas huido de la arena.%n");
        } else if (heroe.isEstaVivo()) {
            heroe.setOro(heroe.getOro() + enemigo.getRecompensaOro());
            boolean subioNivel = heroe.ganarExperiencia(enemigo.getRecompensaExperiencia());
            System.out.printf("%n¡VICTORIA! Ganas %.2f de oro y %d de experiencia.%n",
                    enemigo.getRecompensaOro(), enemigo.getRecompensaExperiencia());
            if (subioNivel) {
                System.out.printf("¡%s sube al nivel %d!%n", heroe.getNombre(), heroe.getNivel());
            }
            mostrarEstado(heroe);
        } else {
            System.out.printf("%nDERROTA. %s ha caído en la arena.%n", heroe.getNombre());
        }

        sc.close();
    }

    private static int leerEntero(Scanner sc) {
        try {
            return Integer.parseInt(sc.nextLine().trim());
        } catch (NumberFormatException ex) {
            return -1;
        }
    }

    private static void mostrarEstado(Personaje heroe) {
        System.out.printf("%n--- ESTADO DE %s ---%n", heroe.getNombre());
        System.out.printf("Vida: %d/%d%n", heroe.getPuntosVida(), heroe.getVidaMaxima());
        System.out.printf("Ataque: %d%n", heroe.getPuntosAtaque());
        System.out.printf("Nivel: %d (XP: %d)%n", heroe.getNivel(), heroe.getExperiencia());
        System.out.printf("Oro: %.2f%n", heroe.getOro());
        System.out.printf("Pociones: %d%n", heroe.getPociones());
        System.out.printf("Vivo: %b%n", heroe.isEstaVivo());
    }

    private static void abrirTienda(Scanner sc, Personaje heroe, StringBuilder historial) {
        System.out.printf("%n=== TIENDA === (Oro disponible: %.2f)%n", heroe.getOro());
        System.out.printf("1. Poción Grande de Salud (%.2f oro, cura %d)%n2. Volver%n",
                COSTO_POCION_GRANDE, CURA_POCION_GRANDE);
        System.out.printf("Seleccione una opción: ");

        int opcion = leerEntero(sc);
        if (opcion == 1) {
            boolean compraAprobada = heroe.setOro(heroe.getOro() - COSTO_POCION_GRANDE);
            if (compraAprobada) {
                heroe.agregarPocion();
                historial.append(String.format("%s compra una Poción Grande por %.2f de oro.%n",
                        heroe.getNombre(), COSTO_POCION_GRANDE));
                System.out.printf("Compra exitosa. Oro restante: %.2f%n", heroe.getOro());
            } else {
                System.out.printf("Saldo insuficiente. Necesitas %.2f y tienes %.2f.%n",
                        COSTO_POCION_GRANDE, heroe.getOro());
            }
        } else if (opcion == 2) {
            System.out.printf("Vuelves a la arena.%n");
        } else {
            System.out.printf("Opción inválida en la tienda.%n");
        }
    }
}
