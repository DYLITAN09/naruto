package naruto.factory;

import naruto.model.Jutsu;
import naruto.model.Ninja;
import naruto.model.Rank;

public class KonohaFactory implements NinjaFactory {

    @Override
    public Ninja createNinja(String nombre, Rank rango) {
        Ninja n = new Ninja(nombre, rango, 10, 8, 12, "Konoha");
        n.learn(new Jutsu("Rasengan", 20));
        if (rango != Rank.GENIN) {
            n.learn(new Jutsu("Sello del Cuarto Hokage", 15));
        }
        return n;
    }
}
