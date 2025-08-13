package naruto.combat;

import naruto.model.Ninja;

public class CombatSimulator {

    public static Ninja fight(Ninja a, Ninja b) {
        int pa = a.powerLevel();
        int pb = b.powerLevel();
        System.out.println(a.getNombre() + " pwr=" + pa + " vs " + b.getNombre() + " pwr=" + pb);

        if (pa == pb) {
            return null;
        }
        return (pa > pb) ? a : b;
    }
}
