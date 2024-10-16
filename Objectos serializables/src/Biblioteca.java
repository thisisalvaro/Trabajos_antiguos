import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Biblioteca {
    private List<Libro> libros;
    private static final String NOMBRE_FICHERO = "libros.dat";

    // Constructor
    public Biblioteca() {
        libros = new ArrayList<>();
        cargarLibros();
    }

    // Método para cargar libros desde el archivo
    public void cargarLibros() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(NOMBRE_FICHERO))) {
            libros = (List<Libro>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Archivo no encontrado."); }
    }

    // Método para guardar libros en el archivo
    public void guardarLibros() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(NOMBRE_FICHERO))) {
            oos.writeObject(libros);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Método para agregar un nuevo libro
    public void agregarNuevoLibro(Libro libro) {
        for (Libro l : libros) {
            if (l.getId() == libro.getId()) {
                System.out.println("El libro con el ID " + libro.getId() + " ya existe y no se puede agregar.");
                return;
            }
        }
        libros.add(libro);
        guardarLibros();
        System.out.println("Libro agregado correctamente.");
    }

    // Método para consultar un libro por ID
    public Libro consultarLibro(int id) {
        for (Libro libro : libros) {
            if (libro.getId() == id && !libro.isEliminado()) {
                return libro;
            }
        }
        System.out.println("Libro con ID " + id + " no encontrado.");
        return null;
    }
}

