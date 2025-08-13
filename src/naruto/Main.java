// Archivo demo o de prueba que usamos para comprobar los Export y otros apartados
package naruto;

import naruto.builder.NinjaBuilder;
import naruto.combat.CombatSimulator;
import naruto.factory.KonohaFactory;
import naruto.factory.SunaFactory;
import naruto.model.Mission;
import naruto.model.MissionRank;
import naruto.model.Ninja;
import naruto.model.Rank;
import naruto.visitor.ExportVisitor;
import naruto.village.Village;

import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        // Fábricas
        KonohaFactory konoha = new KonohaFactory();
        SunaFactory suna = new SunaFactory();

        Village vKonoha = new Village("Konoha", konoha);
        Village vSuna = new Village("Suna", suna);

        // Builder: ninja personalizado
        Ninja naruto = new NinjaBuilder()
                .setNombre("Naruto Uzumaki")
                .setRango(Rank.GENIN)
                .setAtaque(12)
                .setDefensa(9)
                .setChakra(18)
                .setAldea("Konoha")
                .addJutsu(new naruto.model.Jutsu("Rasengan", 25))
                .addJutsu(new naruto.model.Jutsu("Sello de la Bestia", 10))
                .build();

        // Factory: otros ninjas
        Ninja kakashi = konoha.createNinja("Kakashi Hatake", Rank.JONIN);
        Ninja gaara = suna.createNinja("Gaara", Rank.JONIN);

        // Entrenamiento
        naruto.train(3, 1, 5);
        gaara.train(1, 2, 3);

        // Añadir ninjas a aldeas
        vKonoha.addNinja(naruto);
        vKonoha.addNinja(kakashi);
        vSuna.addNinja(gaara);

        // Misiones
        Mission m1 = new Mission(MissionRank.C, 300, Rank.GENIN);
        Mission m2 = new Mission(MissionRank.A, 1200, Rank.JONIN);

        // Combate
        Ninja ganador = CombatSimulator.fight(naruto, gaara);
        System.out.println("Ganador: " + ganador.getNombre());

        // Visitor export
        ExportVisitor textExporter = new ExportVisitor("TEXT");
        Arrays.asList(naruto, kakashi, gaara).forEach(n -> n.accept(textExporter));
        m1.accept(textExporter);
        m2.accept(textExporter);
        System.out.println("--- EXPORT TEXT ---\n" + textExporter.getResult());

        ExportVisitor jsonExporter = new ExportVisitor("JSON");
        Arrays.asList(naruto, kakashi, gaara).forEach(n -> n.accept(jsonExporter));
        m1.accept(jsonExporter);
        m2.accept(jsonExporter);
        System.out.println("--- EXPORT JSON ---\n" + jsonExporter.getResult());
    }
}
