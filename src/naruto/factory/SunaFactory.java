package naruto.factory;

import naruto.model.Jutsu;
import naruto.model.Ninja;
import naruto.model.Rank;

public class SunaFactory implements NinjaFactory {

    @Override
    public Ninja createNinja(String nombre, Rank rango) {
        Ninja n = new Ninja(nombre, rango, 9, 10, 10, "Suna");
        n.learn(new Jutsu("Manipulación de Arena", 18));
        return n;
    }
}
