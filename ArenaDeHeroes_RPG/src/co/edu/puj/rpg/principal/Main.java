package co.edu.puj.rpg.principal;

import co.edu.puj.rpg.modelo.Personaje;
import co.edu.puj.rpg.modelo.enemigo.Enemigo;

import java.util.Scanner;

public class Main {

    private static final int CURACION_POCION = 30;
    private static final int CURACION_POCION_GRANDE = 70;
    private static final double COSTO_POCION_GRANDE = 40.00;

    private static int pociones = 3;
    private static int pocionesGrandes = 0;

    public static void main(String[] args) {
        Scanner teclado = new Scanner(System.in);
        Personaje heroe = new Personaje("Aragorn", 100, 15, 50.0);
        Enemigo enemigo = new Enemigo("Orco Guerrero", 80, 12, 35.0);
        StringBuilder historial = new StringBuilder();
        boolean huyo = false;
        int ronda = 0;

        System.out.println("==============================================");
        System.out.println("      SIMULADOR DE BATALLA - ARENA DE HEROES");
        System.out.println("==============================================");
        System.out.printf("%s se enfrenta a %s%n", heroe.getNombre(), enemigo.getNombre());

        do {
            ronda++;
            boolean turnoConsumido = false;

            System.out.printf("%n----------- RONDA %d -----------%n", ronda);
            System.out.println("1. Atacar");
            System.out.println("2. Usar Pocion");
            System.out.println("3. Ver Estado");
            System.out.println("4. Huir");
            System.out.println("5. Tienda");
            System.out.print("Seleccione una opcion: ");

            int opcion = leerEntero(teclado);

            if (opcion < 1 || opcion > 5) {
                System.out.println("Opcion invalida. Intente de nuevo.");
            } else {
                switch (opcion) {
                    case 1:
                        heroe.atacar(enemigo);
                        if (heroe.isUltimoGolpeCritico()) {
                            historial.append(String.format("[Ronda %d] GOLPE CRITICO! %s inflige %d de dano a %s.%n",
                                    ronda, heroe.getNombre(), heroe.getUltimoDano(), enemigo.getNombre()));
                        } else {
                            historial.append(String.format("[Ronda %d] %s inflige %d de dano a %s.%n",
                                    ronda, heroe.getNombre(), heroe.getUltimoDano(), enemigo.getNombre()));
                        }
                        turnoConsumido = true;
                        break;
                    case 2:
                        turnoConsumido = usarPocionDelInventario(heroe, historial, ronda);
                        break;
                    case 3:
                        mostrarEstado(heroe, enemigo);
                        break;
                    case 4:
                        huyo = true;
                        historial.append(String.format("[Ronda %d] %s huye del combate.%n", ronda, heroe.getNombre()));
                        break;
                    case 5:
                        abrirTienda(teclado, heroe, historial, ronda);
                        break;
                    default:
                        break;
                }
            }

            if (turnoConsumido && enemigo.isEstaVivo()) {
                int danoEnemigo = enemigo.getPuntosAtaque();
                heroe.recibirDano(danoEnemigo);
                historial.append(String.format("[Ronda %d] %s inflige %d de dano a %s.%n",
                        ronda, enemigo.getNombre(), danoEnemigo, heroe.getNombre()));
            }

            if (!enemigo.isEstaVivo() && heroe.isEstaVivo()) {
                historial.append(String.format("[Ronda %d] %s ha sido derrotado.%n", ronda, enemigo.getNombre()));
                heroe.setOro(heroe.getOro() + enemigo.getOro());
                historial.append(String.format("[Ronda %d] %s obtiene %.2f de oro.%n",
                        ronda, heroe.getNombre(), enemigo.getOro()));
                boolean subioNivel = heroe.ganarExperiencia(enemigo.getExperienciaRecompensa());
                historial.append(String.format("[Ronda %d] %s obtiene %d de experiencia.%n",
                        ronda, heroe.getNombre(), enemigo.getExperienciaRecompensa()));
                if (subioNivel) {
                    historial.append(String.format("[Ronda %d] %s sube al nivel %d.%n",
                            ronda, heroe.getNombre(), heroe.getNivel()));
                }
            }

            if (!heroe.isEstaVivo()) {
                historial.append(String.format("[Ronda %d] %s ha caido en batalla.%n", ronda, heroe.getNombre()));
            }

            System.out.println("\n===== HISTORIAL DE BATALLA =====");
            System.out.print(historial);
            System.out.println("================================");

        } while (heroe.isEstaVivo() && enemigo.isEstaVivo() && !huyo);

        System.out.println();
        if (huyo) {
            System.out.printf("%s escapo de la arena.%n", heroe.getNombre());
        } else if (heroe.isEstaVivo()) {
            System.out.printf("VICTORIA! %s derroto a %s.%n", heroe.getNombre(), enemigo.getNombre());
        } else {
            System.out.printf("DERROTA. %s fue vencido por %s.%n", heroe.getNombre(), enemigo.getNombre());
        }
        mostrarEstado(heroe, enemigo);

        teclado.close();
    }

    private static int leerEntero(Scanner teclado) {
        if (teclado.hasNextInt()) {
            int valor = teclado.nextInt();
            teclado.nextLine();
            return valor;
        }
        if (teclado.hasNextLine()) {
            teclado.nextLine();
        }
        return -1;
    }

    private static void mostrarEstado(Personaje heroe, Enemigo enemigo) {
        System.out.println("\n========== ESTADO DEL COMBATE ==========");
        System.out.printf("Heroe   : %s (Nivel %d)%n", heroe.getNombre(), heroe.getNivel());
        System.out.printf("Vida    : %d / %d%n", heroe.getPuntosVida(), heroe.getVidaMaxima());
        System.out.printf("Ataque  : %d%n", heroe.getPuntosAtaque());
        System.out.printf("Oro     : %.2f%n", heroe.getOro());
        System.out.printf("XP      : %d / %d%n", heroe.getExperiencia(), heroe.getExperienciaRequerida());
        System.out.printf("Estado  : %s%n", heroe.isEstaVivo() ? "Vivo" : "Derrotado");
        System.out.printf("Pociones: %d | Pociones Grandes: %d%n", pociones, pocionesGrandes);
        System.out.println("----------------------------------------");
        System.out.printf("Enemigo : %s (Nivel %d)%n", enemigo.getNombre(), enemigo.getNivel());
        System.out.printf("Vida    : %d / %d%n", enemigo.getPuntosVida(), enemigo.getVidaMaxima());
        System.out.printf("Ataque  : %d%n", enemigo.getPuntosAtaque());
        System.out.printf("Estado  : %s%n", enemigo.isEstaVivo() ? "Vivo" : "Derrotado");
        System.out.println("========================================");
    }

    private static boolean usarPocionDelInventario(Personaje heroe, StringBuilder historial, int ronda) {
        if (pocionesGrandes > 0) {
            int vidaAntes = heroe.getPuntosVida();
            heroe.usarPocion(CURACION_POCION_GRANDE);
            pocionesGrandes--;
            historial.append(String.format("[Ronda %d] %s usa una Pocion Grande y recupera %d de vida.%n",
                    ronda, heroe.getNombre(), heroe.getPuntosVida() - vidaAntes));
            return true;
        }
        if (pociones > 0) {
            int vidaAntes = heroe.getPuntosVida();
            heroe.usarPocion(CURACION_POCION);
            pociones--;
            historial.append(String.format("[Ronda %d] %s usa una Pocion y recupera %d de vida.%n",
                    ronda, heroe.getNombre(), heroe.getPuntosVida() - vidaAntes));
            return true;
        }
        System.out.println("No tienes pociones disponibles.");
        return false;
    }

    private static void abrirTienda(Scanner teclado, Personaje heroe, StringBuilder historial, int ronda) {
        int opcionTienda;
        do {
            System.out.println("\n============== TIENDA ==============");
            System.out.printf("Oro disponible: %.2f%n", heroe.getOro());
            System.out.printf("1. Pocion Grande de Salud (%.2f de oro, restaura %d)%n",
                    COSTO_POCION_GRANDE, CURACION_POCION_GRANDE);
            System.out.println("2. Salir de la tienda");
            System.out.print("Seleccione una opcion: ");

            opcionTienda = leerEntero(teclado);

            if (opcionTienda == 1) {
                double nuevoSaldo = heroe.getOro() - COSTO_POCION_GRANDE;
                heroe.setOro(nuevoSaldo);
                if (heroe.getOro() == nuevoSaldo) {
                    pocionesGrandes++;
                    historial.append(String.format("[Ronda %d] %s compra una Pocion Grande de Salud.%n",
                            ronda, heroe.getNombre()));
                    System.out.println("Compra realizada con exito.");
                } else {
                    System.out.println("Transaccion rechazada: no se entrego el articulo.");
                }
            } else if (opcionTienda != 2) {
                System.out.println("Opcion invalida. Intente de nuevo.");
            }
        } while (opcionTienda != 2);
    }
}
