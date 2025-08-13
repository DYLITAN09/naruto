package naruto.factory;

import naruto.model.Ninja;
import naruto.model.Rank;

public interface NinjaFactory {
    Ninja createNinja(String nombre, Rank rango);
}
