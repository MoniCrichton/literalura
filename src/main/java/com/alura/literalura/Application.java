package com.alura.literalura;

import com.alura.literalura.model.Libro;
import com.alura.literalura.service.LibroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

@SpringBootApplication
public class Application implements CommandLineRunner {

    @Autowired
    private LibroService libroService;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        int option = -1;

        while (option != 4) {
            try {
                System.out.println("Menú de opciones:");
                System.out.println("1. Importar libros de la API");
                System.out.println("2. Listar todos los libros");
                System.out.println("3. Buscar libros por autor");
                System.out.println("4. Salir");
                System.out.print("Seleccione una opción: ");
                option = scanner.nextInt();
                scanner.nextLine(); // consumir el salto de línea

                switch (option) {
                    case 1:
                        libroService.fetchAndSaveBooks();
                        System.out.println("Libros importados y guardados en la base de datos.");
                        break;
                    case 2:
                        List<Libro> libros = libroService.getAllBooks();
                        libros.forEach(libro -> System.out.println("Título: " + libro.getTitulo() + ", Autor: " + libro.getAutor()));
                        break;
                    case 3:
                        System.out.print("Ingrese el nombre del autor: ");
                        String autor = scanner.nextLine();
                        List<Libro> librosPorAutor = libroService.getBooksByAuthor(autor);
                        librosPorAutor.forEach(libro -> System.out.println("Título: " + libro.getTitulo() + ", Autor: " + libro.getAutor()));
                        break;
                    case 4:
                        System.out.println("Saliendo...");
                        break;
                    default:
                        System.out.println("Opción no válida. Por favor, intente nuevamente.");
                        break;
                }
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida. Por favor, ingrese un número.");
                scanner.nextLine(); // limpiar el buffer
            }
        }
    }
}
