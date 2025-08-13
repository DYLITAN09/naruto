package naruto.factory;

import naruto.model.Jutsu;
import naruto.model.Ninja;
import naruto.model.Rank;

public class KiriFactory implements NinjaFactory {

    @Override
    public Ninja createNinja(String nombre, Rank rango) {
        Ninja n = new Ninja(nombre, rango, 12, 9, 14, "Kiri");
        n.learn(new Jutsu("Niebla Ocultadora", 18));
        n.learn(new Jutsu("Técnica del Agua: Dragón Acuático", 22));

        if (rango != Rank.GENIN) {
            n.learn(new Jutsu("Técnica del Agua: Gran Cascada", 25));
        }
        return n;
    }
}
