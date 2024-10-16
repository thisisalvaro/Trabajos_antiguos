import java.io.Serializable;

public class Libro implements Serializable {
    private int id;
    private String autor;
    private double precio;
    private boolean eliminado;

    // Constructor
    public Libro(int id, String autor, double precio) {
        this.id = id;
        this.autor = autor;
        this.precio = precio;
        this.eliminado = false;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public boolean isEliminado() {
        return eliminado;
    }

    public void setEliminado(boolean eliminado) {
        this.eliminado = eliminado;
    }

    @Override
    public String toString() {
        return "Libro{" +
                "id=" + id +
                ", autor='" + autor + '\'' +
                ", precio=" + precio +
                ", eliminado=" + eliminado +
                '}';
    }
}
