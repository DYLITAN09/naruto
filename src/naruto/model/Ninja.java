package naruto.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import naruto.visitor.NarutoVisitor;
import naruto.visitor.Acceptable;

public class Ninja implements Acceptable {
    private String nombre;
    private Rank rango;
    private int ataque;
    private int defensa;
    private int chakra;
    private final String aldea;
    private final List<Jutsu> jutsus = new ArrayList<>();

    public Ninja(String nombre, Rank rango, int ataque, int defensa, int chakra, String aldea) {
        this.nombre = nombre;
        this.rango = rango;
        this.ataque = ataque;
        this.defensa = defensa;
        this.chakra = chakra;
        this.aldea = aldea;
    }

    public void learn(Jutsu jutsu) {
        jutsus.add(jutsu);
    }

    public void train(int ataqueInc, int defensaInc, int chakraInc) {
        ataque += ataqueInc;
        defensa += defensaInc;
        chakra += chakraInc;
    }

    public int powerLevel() {
        int jutsuPower = jutsus.stream().mapToInt(Jutsu::getPoder).sum();
        return ataque + defensa + chakra + jutsuPower;
    }

    public String getNombre() {
        return nombre;
    }

    public Rank getRango() {
        return rango;
    }

    public String getAldea() {
        return aldea;
    }

    public List<Jutsu> getJutsus() {
        return Collections.unmodifiableList(jutsus);
    }

    public int getAtaque() {
        return ataque;
    }

    public int getDefensa() {
        return defensa;
    }

    public int getChakra() {
        return chakra;
    }

    @Override
    public void accept(NarutoVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        return String.format(
                "%s [%s] (%s) ATK:%d DEF:%d CH:%d Jutsus:%s",
                nombre, rango, aldea, ataque, defensa, chakra, jutsus);
    }
}
