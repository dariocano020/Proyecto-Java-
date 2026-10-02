package inventario.vista;

import inventario.utilidades.*;
import inventario.modelo.*;
import inventario.controlador.*;
import java.util.Scanner;

public class ConsolaVista {
    private Scanner scanner;
    private static final String RESET = "\u001B[0m";
    private static final String RED = "\u001B[31m";
    private static final String GREEN = "\u001B[32m";
    private static final String YELLOW = "\u001B[33m";
    private static final String BLUE = "\u001B[34m";
    private static final String PURPLE = "\u001B[35m";
    private static final String CYAN = "\u001B[36m";
    private static final String BOLD = "\u001B[1m";

    public ConsolaVista() {
        this.scanner = new Scanner(System.in);
    }

    public void mostrarBanner() {
        System.out.println(CYAN + BOLD + "\n=======================================================");
        System.out.println("||   " + YELLOW + "   BIENVENIDO AL SISTEMA DE INVENTARIO    " + CYAN + "   ||");
        System.out.println("=======================================================" + RESET);
    }

    public void mostrarMensaje(String msg) {
        System.out.println(BLUE + " [i] " + RESET + msg);
    }

    public void mostrarError(String msg) {
        System.out.println(RED + BOLD + " [X] ERROR: " + RESET + RED + msg + RESET);
    }

    public void mostrarExito(String msg) {
        System.out.println(GREEN + BOLD + " [V] EXITO: " + RESET + GREEN + msg + RESET);
    }

    public String pedirTexto(String label) {
        System.out.print(YELLOW + " > " + label + RESET);
        return scanner.nextLine();
    }

    public int pedirEntero(String label) {
        System.out.print(YELLOW + " > " + label + RESET);
        while (!scanner.hasNextInt()) {
            System.out.print(RED + " [X] Numero no valido. Intenta de nuevo: " + RESET);
            scanner.next();
        }
        int num = scanner.nextInt();
        scanner.nextLine();
        return num;
    }

    public double pedirDouble(String label) {
        System.out.print(YELLOW + " > " + label + RESET);
        while (!scanner.hasNextDouble()) {
            System.out.print(RED + " [X] Valor no valido. Usa comas (ej. 15,5): " + RESET);
            scanner.next();
        }
        double num = scanner.nextDouble();
        scanner.nextLine();
        return num;
    }

    public void mostrarMenuLogin() {
        System.out.println(PURPLE + "\n-------------------------------------------------------");
        System.out.println("| " + RESET + BOLD + "  1. Iniciar sesion (OTP)" + PURPLE + "                         |");
        System.out.println("| " + RESET + BOLD + "  2. Registrarse" + PURPLE + "                                  |");
        System.out.println("| " + RESET + BOLD + "  3. Salir" + PURPLE + "                                        |");
        System.out.println("-------------------------------------------------------" + RESET);
    }

    public void mostrarMenuAdmin(String nombre) {
        System.out.println(CYAN + "\n-------------------------------------------------------");
        System.out.println("| " + YELLOW + BOLD + "  MENU ADMINISTRADOR - Hola, " + String.format("%-14s", nombre) + CYAN + " |");
        System.out.println("-------------------------------------------------------");
        System.out.println("| " + RESET + "1.   Alta de producto" + CYAN + "                             |");
        System.out.println("| " + RESET + "2.   Baja de producto" + CYAN + "                             |");
        System.out.println("| " + RESET + "3.   Listado de productos" + CYAN + "                         |");
        System.out.println("| " + RESET + "4.   Ver estadisticas y top ventas" + CYAN + "                |");
        System.out.println("| " + RESET + "5.   Exportar historial a CSV" + CYAN + "                     |");
        System.out.println("| " + RESET + "6.   Cerrar sesion" + CYAN + "                                |");
        System.out.println("-------------------------------------------------------" + RESET);
    }

    public void mostrarMenuUsuario(String nombre) {
        System.out.println(GREEN + "\n-------------------------------------------------------");
        System.out.println("| " + YELLOW + BOLD + "  MENU CLIENTE - Hola, " + String.format("%-20s", nombre) + GREEN + " |");
        System.out.println("-------------------------------------------------------");
        System.out.println("| " + RESET + "1.   Ver catalogo de productos" + GREEN + "                    |");
        System.out.println("| " + RESET + "2.   Anadir al carrito" + GREEN + "                            |");
        System.out.println("| " + RESET + "3.   Quitar del carrito" + GREEN + "                           |");
        System.out.println("| " + RESET + "4.   Ver mi carrito y total" + GREEN + "                       |");
        System.out.println("| " + RESET + "5.   Cerrar pedido (Pagar)" + GREEN + "                        |");
        System.out.println("| " + RESET + "6.   Cerrar sesion" + GREEN + "                                |");
        System.out.println("-------------------------------------------------------" + RESET);
    }
}