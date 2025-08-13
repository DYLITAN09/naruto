// Dylan Gutierrez, Camilo Castillo y Santiago Daza
package naruto;

import naruto.builder.NinjaBuilder;
import naruto.combat.CombatSimulator;
import naruto.factory.KiriFactory;
import naruto.factory.KonohaFactory;
import naruto.factory.SunaFactory;
import naruto.model.Jutsu;
import naruto.model.Mission;
import naruto.model.MissionRank;
import naruto.model.MissionStatus;
import naruto.model.Ninja;
import naruto.model.Rank;
import naruto.visitor.ExportVisitor;
import naruto.village.Village;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;

public class ConsoleApp {

    private final Scanner scanner = new Scanner(System.in);
    private final Map<String, Village> villages = new HashMap<>();
    private final List<Mission> missions = new ArrayList<>();

    public ConsoleApp() {
        villages.put("Konoha", new Village("Konoha", new KonohaFactory()));
        villages.put("Suna", new Village("Suna", new SunaFactory()));
        villages.put("Kiri", new Village("Kiri", new KiriFactory()));
    }

    public void run() {
        boolean exit = false;
        while (!exit) {
            printMenu();
            int choice = readInt("Seleccione opción: ");
            System.out.println();
            switch (choice) {
                case 1 -> createNinja(); // Builder manual
                case 2 -> createNinjaFactory(); // Factory automático
                case 3 -> listNinjas();
                case 4 -> trainNinja();
                case 5 -> createMission();
                case 6 -> listMissions();
                case 7 -> assignMission();
                case 8 -> viewMissionStatus();
                case 9 -> simulateCombat();
                case 10 -> exportData();
                case 0 -> {
                    System.out.println("Saliendo...");
                    System.out.println("Proyecto creado por Dylan Gutierrez, Camilo Castillo y Santiago Daza");
                    exit = true;
                }
                default -> System.out.println("Opción inválida.");
            }
        }
    }

    private void printMenu() {
        System.out.println("\n--- Sistema Naruto ---");
        System.out.println("1. Crear ninja (manual con Builder)");
        System.out.println("2. Crear ninja por aldea (Factory)");
        System.out.println("3. Listar ninjas");
        System.out.println("4. Entrenar ninja");
        System.out.println("5. Crear misión");
        System.out.println("6. Listar misiones");
        System.out.println("7. Asignar misión a ninja");
        System.out.println("8. Ver estado de misiones");
        System.out.println("9. Simular combate");
        System.out.println("10. Exportar datos");
        System.out.println("0. Salir");
    }

    private int readInt(String prompt) {
        System.out.print(prompt);
        while (!scanner.hasNextInt()) {
            System.out.println("Por favor, ingrese un número válido.");
            scanner.next();
            System.out.print(prompt);
        }
        int num = scanner.nextInt();
        scanner.nextLine();
        return num;
    }

    private String readString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine().trim();
    }

    private Rank readRank(String prompt) {
        while (true) {
            System.out.print(prompt + " (GENIN, CHUNIN, JONIN): ");
            String input = scanner.nextLine().trim().toUpperCase(Locale.ROOT);
            try {
                return Rank.valueOf(input);
            } catch (IllegalArgumentException e) {
                System.out.println("Rango inválido.");
            }
        }
    }

    private MissionRank readMissionRank(String prompt) {
        while (true) {
            System.out.print(prompt + " (D, C, B, A, S): ");
            String input = scanner.nextLine().trim().toUpperCase(Locale.ROOT);
            try {
                return MissionRank.valueOf(input);
            } catch (IllegalArgumentException e) {
                System.out.println("Rango de misión inválido.");
            }
        }
    }

    private Village chooseVillage() {
        System.out.println("Seleccione aldea:");
        List<String> keys = new ArrayList<>(villages.keySet());
        for (int i = 0; i < keys.size(); i++) {
            System.out.printf("%d. %s\n", i + 1, keys.get(i));
        }
        int idx = readInt("Opción: ") - 1;
        if (idx >= 0 && idx < keys.size()) {
            return villages.get(keys.get(idx));
        }
        System.out.println("Aldea inválida. Se usará Konoha por defecto.");
        return villages.get("Konoha");
    }

    // Builder manual
    private void createNinja() {
        System.out.println("--- Crear Ninja (Manual con Builder) ---");
        Village village = chooseVillage();
        String nombre = readString("Nombre: ");
        Rank rango = readRank("Rango");
        int ataque = readInt("Ataque: ");
        int defensa = readInt("Defensa: ");
        int chakra = readInt("Chakra: ");

        NinjaBuilder builder = new NinjaBuilder()
                .setNombre(nombre)
                .setRango(rango)
                .setAtaque(ataque)
                .setDefensa(defensa)
                .setChakra(chakra)
                .setAldea(village.getName());

        boolean addJutsus = true;
        while (addJutsus) {
            String jutsuNombre = readString("Nombre Jutsu (deje vacío para terminar): ");
            if (jutsuNombre.isEmpty()) {
                addJutsus = false;
            } else {
                int poder = readInt("Poder del Jutsu: ");
                builder.addJutsu(new Jutsu(jutsuNombre, poder));
            }
        }
        Ninja ninja = builder.build();
        village.addNinja(ninja);
        System.out.println("Ninja creado: " + ninja);
    }

    // Factory automático
    private void createNinjaFactory() {
        System.out.println("--- Crear Ninja por Aldea (Factory) ---");
        Village village = chooseVillage();
        String nombre = readString("Nombre: ");
        Rank rango = readRank("Rango");

        Ninja ninja = village.getFactory().createNinja(nombre, rango);
        village.addNinja(ninja);

        System.out.println("Ninja creado con Factory:\n" + ninja);
    }

    private void listNinjas() {
        System.out.println("--- Listado de Ninjas ---");
        for (Village village : villages.values()) {
            System.out.println("Aldea: " + village.getName());
            if (village.getNinjas().isEmpty()) {
                System.out.println("  No hay ninjas.");
            } else {
                for (Ninja ninja : village.getNinjas()) {
                    System.out.println("  " + ninja);
                }
            }
        }
    }

    private Ninja selectNinja() {
        List<Ninja> allNinjas = new ArrayList<>();
        for (Village v : villages.values()) {
            allNinjas.addAll(v.getNinjas());
        }
        if (allNinjas.isEmpty()) {
            System.out.println("No hay ninjas disponibles.");
            return null;
        }
        for (int i = 0; i < allNinjas.size(); i++) {
            System.out.printf("%d. %s (%s, %s)\n", i + 1, allNinjas.get(i).getNombre(),
                    allNinjas.get(i).getAldea(), allNinjas.get(i).getRango());
        }
        int idx = readInt("Seleccione ninja: ") - 1;
        if (idx >= 0 && idx < allNinjas.size()) {
            return allNinjas.get(idx);
        } else {
            System.out.println("Selección inválida.");
            return null;
        }
    }

    private void trainNinja() {
        System.out.println("--- Entrenar Ninja ---");
        Ninja ninja = selectNinja();
        if (ninja == null)
            return;

        int incremento = 50;
        ninja.train(incremento, incremento, incremento);

        System.out.println("Entrenamiento completado: +" + incremento + " a ataque, defensa y chakra.");
        System.out.println("Ninja entrenado: " + ninja);
    }

    private void createMission() {
        System.out.println("--- Crear Misión ---");
        MissionRank rank = readMissionRank("Rango de misión");
        int reward = readInt("Recompensa: ");
        Rank minRank = readRank("Rango mínimo requerido");

        Mission mission = new Mission(rank, reward, minRank);
        missions.add(mission);
        System.out.println("Misión creada con ID automática: " + mission.getId());
    }

    private void listMissions() {
        System.out.println("--- Listado de Misiones ---");
        if (missions.isEmpty()) {
            System.out.println("No hay misiones.");
        } else {
            for (Mission m : missions) {
                System.out.println(m);
            }
        }
    }

    private void assignMission() {
        System.out.println("--- Asignar Misión ---");
        if (missions.isEmpty()) {
            System.out.println("No hay misiones disponibles.");
            return;
        }
        Mission mission = chooseMission();
        if (mission == null)
            return;

        Ninja ninja = selectNinja();
        if (ninja == null)
            return;

        if (ninja.getRango().ordinal() < mission.getMinRankRequired().ordinal()) {
            System.out.printf("El ninja %s con rango %s NO puede realizar una misión de rango mínimo %s.%n",
                    ninja.getNombre(), ninja.getRango(), mission.getMinRankRequired());
            return;
        }

        boolean assigned = false;
        try {
            assigned = mission.assignTo(ninja);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
            return;
        }

        if (!assigned) {
            System.out.println("La misión no está disponible para asignarse (ya está en progreso o completada).");
            return;
        }

        System.out.println("Misión asignada: " + mission.getId() + " a " + ninja.getNombre());
    }

    private Mission chooseMission() {
        for (int i = 0; i < missions.size(); i++) {
            System.out.printf("%d. %s\n", i + 1, missions.get(i));
        }
        int idx = readInt("Seleccione misión: ") - 1;
        if (idx >= 0 && idx < missions.size()) {
            return missions.get(idx);
        }
        System.out.println("Selección inválida.");
        return null;
    }

    private void viewMissionStatus() {
        System.out.println("--- Estado de Misiones ---");
        boolean hayMisionesAsignadas = false;
        for (Mission mission : missions) {
            if (mission.getStatus() != MissionStatus.PENDIENTE) {
                hayMisionesAsignadas = true;
                String ninjaNombre = mission.getAssignedNinja() != null ? mission.getAssignedNinja().getNombre()
                        : "Ninguno";

                if (mission.getStatus() == MissionStatus.FRACASADA) {
                    System.out.printf("El ninja %s fracasó en la ejecución de la misión %s.%n", ninjaNombre,
                            mission.getId());
                } else {
                    System.out.printf("Misión %s: Estado: %s, Ninja asignado: %s%n",
                            mission.getId(),
                            mission.getStatus(),
                            ninjaNombre);
                }
            }
        }

        if (!hayMisionesAsignadas) {
            System.out.println("No hay misiones asignadas.");
        }
    }

    private void simulateCombat() {
        System.out.println("--- Simular Combate ---");
        Ninja ninja1 = selectNinja();
        if (ninja1 == null)
            return;

        Ninja ninja2 = selectNinja();
        if (ninja2 == null)
            return;

        Ninja winner = CombatSimulator.fight(ninja1, ninja2);

        if (winner == null) {
            System.out.println("¡El combate terminó en empate!");
        } else {
            System.out.println("¡Ganador: " + winner.getNombre() + "!");
        }
    }

    private void exportData() {
        System.out.println("--- Exportar Datos ---");
        System.out.print("Formato (TEXT, JSON, XML): ");
        String format = scanner.nextLine().trim().toUpperCase(Locale.ROOT);

        ExportVisitor visitor = new ExportVisitor(format);
        for (Village v : villages.values()) {
            for (Ninja ninja : v.getNinjas()) {
                ninja.accept(visitor);
            }
        }
        for (Mission m : missions) {
            m.accept(visitor);
        }

        String reportContent = visitor.getResult();

        File reportsDir = Paths.get(
                System.getProperty("user.dir"),
                "src", "naruto", "reports").toFile();

        if (!reportsDir.exists()) {
            reportsDir.mkdirs();
        }

        String extension = switch (format) {
            case "JSON" -> ".json";
            case "XML" -> ".xml";
            default -> ".txt";
        };

        String timestamp = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss").format(new Date());
        String fileName = "reporte_" + timestamp + extension;
        File reportFile = new File(reportsDir, fileName);

        try (FileWriter writer = new FileWriter(reportFile)) {
            writer.write(reportContent);
            System.out.println("Informe exportado a: " + reportFile.getAbsolutePath());
        } catch (IOException e) {
            System.err.println("Error al guardar el informe: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        new ConsoleApp().run();
    }
}
