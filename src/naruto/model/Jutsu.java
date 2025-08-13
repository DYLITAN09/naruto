package naruto.model;

public class Jutsu {
    private final String nombre;
    private final int poder;

    public Jutsu(String nombre, int poder) {
        this.nombre = nombre;
        this.poder = poder;
    }

    public String getNombre() {
        return nombre;
    }

    public int getPoder() {
        return poder;
    }

    @Override
    public String toString() {
        return nombre + "(p=" + poder + ")";
    }
}
