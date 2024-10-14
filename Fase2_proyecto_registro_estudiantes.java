
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;

import javax.swing.text.Position;

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

				switch (decision.toLowerCase()) { // toLowerCase()-> poner la R mayuscula en minusculas
				case "r":
					try {
						System.out.println("Ingresa el ID del estudiante:");
						String change = out.readLine();
						int id = Integer.parseInt(change);
						id = Math.abs(id); // Asegurar que el ID sea positivo

						System.out.println("Ingresa el nombre del estudiante:");
						String nombre = out.readLine();

						System.out.println("Ingresa la nota del estudiante:");
						change = out.readLine();
						float nota = Float.parseFloat(change);

						if (nota < 0 || nota > 10) {
							System.out.println("La nota debe estar entre 0 y 10.");
							return;
						}

						escribir(id, nombre, nota);

					} catch (NumberFormatException e) {
						System.out.println("Formato numérico invalido");
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

				case "n": //demomento no funciona
					System.out.println("Ingresa el id de la nota que quieras cambiar");
					int idnota = Integer.parseInt( out.readLine());
					
						
					System.out.println("Ingresa la nota nueva a cambiar");
					Long nota = Long.parseLong(out.readLine());
					updateNote(ac, idnota,nota );
					
					break;
				case "a": //funciona
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
			System.out.println("Formato numérico invalido");
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

	//te dice la posicion ene el fichero de donde esta el id introducido 
	private static long positionid(int id, RandomAccessFile file) throws IOException {
		for (int i = 0; i < file.length()/REGISTRO; i++) {
			file.seek(i * REGISTRO);
			int idExistente = file.readInt();
			if (idExistente == id) {
				System.out.println(file.getFilePointer());
				return file.getFilePointer();
			}
		}
		
		System.out.println("no se hizo");
		return -1;
	}

	private static void updateNote(RandomAccessFile file, int id, float nota) throws IOException {
		if(findout(id)) {
			long positionnota = positionid(id, file) + 44;
			
			System.out.println(positionnota); //el de leer //la funcion positionid esta mal hecha 
			
			file.seek(positionnota);
			file.writeFloat(nota);
		}
		else {
			System.out.println("El id introducido no existe");
		}
		
	}

	private static void readAllRecord(RandomAccessFile file) throws IOException {
		file.seek(0);
		System.out.println("leyendo...");

		for (int i = 0; i < file.length() / 48; i++) {
			System.out.println("Nuevo registro " + i);

			file.seek(file.getFilePointer());
			int id = file.readInt();
			System.out.println(id);

			char[] nombreChars = new char[20];
			for (int j = 0; j < 20; j++) {
				nombreChars[j] = file.readChar();
			}

			String nombre = new String(nombreChars).trim();
			System.out.println(nombre);

			float nota = file.readFloat();
			System.out.println(nota);

		}

	}

}
