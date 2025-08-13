package naruto.builder;

import naruto.model.Jutsu;
import naruto.model.Ninja;
import naruto.model.Rank;

import java.util.ArrayList;
import java.util.List;

public class NinjaBuilder {
    private String nombre = "SinNombre";
    private Rank rango = Rank.GENIN;
    private int ataque = 5;
    private int defensa = 5;
    private int chakra = 5;
    private String aldea = "Desconocida";
    private List<Jutsu> jutsus = new ArrayList<>();

    public NinjaBuilder setNombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public NinjaBuilder setRango(Rank rango) {
        this.rango = rango;
        return this;
    }

    public NinjaBuilder setAtaque(int ataque) {
        this.ataque = Math.max(ataque, 0);
        ;
        return this;
    }

    public NinjaBuilder setDefensa(int defensa) {
        this.defensa = Math.max(defensa, 0);
        return this;
    }

    public NinjaBuilder setChakra(int chakra) {
        this.chakra = Math.max(chakra, 0);
        return this;
    }

    public NinjaBuilder setAldea(String aldea) {
        this.aldea = aldea;
        return this;
    }

    public NinjaBuilder addJutsu(Jutsu jutsu) {
        this.jutsus.add(jutsu);
        return this;
    }

    public Ninja build() {
        Ninja ninja = new Ninja(nombre, rango, ataque, defensa, chakra, aldea);
        for (Jutsu j : jutsus) {
            ninja.learn(j);
        }
        return ninja;
    }
}
