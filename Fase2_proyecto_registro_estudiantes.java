import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;

public class Fase2_proyecto_registro_estudiantes {

    private final static int REGISTRO = 48; // Tamaño del registro

    public static void main(String[] args) {
        menu();
    }

    private static void menu() {
        try {
            BufferedReader out = new BufferedReader(new InputStreamReader(System.in));
            String decision;
            Boolean flag = true;
            String nombreArchivo = "estudiantes.dat";
            RandomAccessFile ac = new RandomAccessFile(nombreArchivo, "rw");

            while (flag) {
                System.out.println(
                        "Escribe la acción que deseas hacer (r para Registrar | s para Leer | n para cambiar la nota | a para leer todos los registros)");
                decision = out.readLine();

                switch (decision.toLowerCase()) { // toLowerCase() convierte a minúsculas
                    case "r":
                        try {
                            System.out.println("Ingresa el ID del estudiante:");
                            String change = out.readLine();
                            int id = Integer.parseInt(change);

                            // Validar el ID
                            if (!validarId(id)) {
                                System.out.println("ID no válido. Debe estar entre 0 y 15 (4 bits).");
                                break;
                            }

                            System.out.println("Ingresa el nombre del estudiante:");
                            String nombre = out.readLine();

                            System.out.println("Ingresa la nota del estudiante:");
                            change = out.readLine();
                            float nota = Float.parseFloat(change);

                            // Validar la nota
                            if (!validarNota(nota)) {
                                System.out.println("Nota no válida. Debe estar entre 0 y 10.");
                                break;
                            }

                            escribir(id, nombre, nota);

                        } catch (NumberFormatException e) {
                            System.out.println("Formato numérico inválido");
                        } catch (IOException e) {
                            System.out.println("Error al registrar: " + e.getMessage());
                        }
                        break;

                    case "s":
                        System.out.println("Ingresa el ID del estudiante a buscar:");
                        String idString = out.readLine();
                        int id = Integer.parseInt(idString);
                        leer(nombreArchivo, id);
                        break;

                    case "n":
                        System.out.println("Ingresa el id de la nota que quieras cambiar");
                        int idnota = Integer.parseInt(out.readLine());

                        System.out.println("Ingresa la nota nueva a cambiar");
                        float notaNueva = Float.parseFloat(out.readLine());
                        
                        updateNote(ac, idnota, notaNueva);

                        break;
                    case "a":
                        readAllRecord(ac);
                        break;

                    default:
                        System.out.println("Opción no válida. Intenta de nuevo.");
                }
            }
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private static void leer(String nombreArchivo, int idBuscado) {
        try {
            RandomAccessFile file = new RandomAccessFile(nombreArchivo, "r");
            long numRecords = file.length() / REGISTRO;

            for (int i = 0; i < numRecords; i++) {
                file.seek(i * REGISTRO);
                int id = file.readInt();

                char[] nombreChars = new char[20];
                for (int j = 0; j < 20; j++) {
                    nombreChars[j] = file.readChar();
                }

                String nombre = new String(nombreChars).trim();
                float nota = file.readFloat();

                if (id == idBuscado) {
                    System.out.println("ID: " + id + ", Nombre: " + nombre + ", Nota: " + nota);
                    break;
                }
            }

            file.close();

        } catch (NumberFormatException e) {
            System.out.println("Formato numérico inválido");
        } catch (IOException e) {
            System.out.println("Error al leer: " + e.getMessage());
        }
    }

    private static void escribir(int id, String nombre, float nota) throws IOException {
        String nombreArchivo = "estudiantes.dat";
        RandomAccessFile file = new RandomAccessFile(nombreArchivo, "rw");
        file.seek(file.length()); // Posiciona al final del archivo

        StringBuilder nombreSB = new StringBuilder(nombre);
        nombreSB.setLength(20); // Asegurar que el nombre sea de 20 caracteres

        if (!findout(id)) {
            file.writeInt(id);
            file.writeChars(nombreSB.toString());
            file.writeFloat(nota);
            System.out.println("Estudiante registrado.");
        } else {
            System.out.println("Error: El ID ya existe");
        }

        file.close();
    }

    private static boolean findout(int id) throws IOException {
        String nombreArchivo = "estudiantes.dat";
        RandomAccessFile file = new RandomAccessFile(nombreArchivo, "r");
        boolean idUnico = false;
        long numRecords = file.length() / REGISTRO;

        for (int i = 0; i < numRecords; i++) {
            file.seek(i * REGISTRO);
            int idExistente = file.readInt();
            if (idExistente == id) {
                idUnico = true;
                break;
            }
        }

        file.close();
        return idUnico;
    }

    private static long positionid(int id, RandomAccessFile file) throws IOException {
        for (int i = 0; i < file.length() / REGISTRO; i++) {
            file.seek(i * REGISTRO);
            int idExistente = file.readInt();
            if (idExistente == id) {
                return file.getFilePointer(); // Devuelve la posición del ID encontrado
            }
        }
        return -1; // Devuelve -1 si no se encontró el ID
    }

    private static void updateNote(RandomAccessFile file, int id, float nota) throws IOException {
        // Validar el ID antes de continuar
        if (!validarId(id)) {
            System.out.println("ID no válido. Debe estar entre 0 y 15 (4 bits).");
            return;
        }

        // Validar la nota antes de continuar
        if (!validarNota(nota)) {
            System.out.println("Nota no válida. Debe estar entre 0 y 10.");
            return;
        }

        long positionid = findout(id) ? positionid(id, file) : -1; // Obtener la posición del registro si existe

        if (positionid != -1) {  // Si el ID existe
            long positionnota = positionid + 44;  // La posición de la nota es 44 bytes después del inicio del registro
            
            System.out.println("Posición de la nota: " + positionnota);

            file.seek(positionnota);  // Ir a la posición de la nota
            file.writeFloat(nota);    // Sobrescribir la nota
            System.out.println("Nota actualizada.");
        } else {
            System.out.println("El ID introducido no existe.");
        }
    }

    private static void readAllRecord(RandomAccessFile file) throws IOException {
        file.seek(0);
        System.out.println("Leyendo...");

        for (int i = 0; i < file.length() / REGISTRO; i++) {
            System.out.println("Nuevo registro " + i);

            file.seek(file.getFilePointer());
            int id = file.readInt();
            System.out.println("ID: " + id);

            char[] nombreChars = new char[20];
            for (int j = 0; j < 20; j++) {
                nombreChars[j] = file.readChar();
            }

            String nombre = new String(nombreChars).trim();
            System.out.println("Nombre: " + nombre);

            float nota = file.readFloat();
            System.out.println("Nota: " + nota);
        }
    }

    // Método para validar si el ID está en el rango permitido (0-15)
    private static boolean validarId(int id) {
        return id >= 0 && id <= 15;  // Verifica que el ID esté en el rango de 4 bits
    }

    // Método para validar si la nota está en el rango permitido (0-10)
    private static boolean validarNota(float nota) {
        return nota >= 0 && nota <= 10;  // Verifica que la nota esté entre 0 y 10
    }
}

