package naruto.village;

import naruto.factory.NinjaFactory;
import naruto.model.Ninja;
import naruto.model.Rank;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Village {
    private final String name;
    private final NinjaFactory factory;
    private final List<Ninja> ninjas = new ArrayList<>();

    public Village(String name, NinjaFactory factory) {
        this.name = name;
        this.factory = factory;
    }

    public Ninja recruit(String nombre, Rank rango) {
        Ninja n = factory.createNinja(nombre, rango);
        ninjas.add(n);
        return n;
    }

    public void addNinja(Ninja ninja) {
        ninjas.add(ninja);
    }

    public List<Ninja> getNinjas() {
        return Collections.unmodifiableList(ninjas);
    }

    public String getName() {
        return name;
    }

    public NinjaFactory getFactory() {
        return factory;
    }
}
