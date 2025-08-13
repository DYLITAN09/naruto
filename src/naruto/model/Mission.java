package naruto.model;

import naruto.visitor.Acceptable;
import naruto.visitor.NarutoVisitor;

public class Mission implements Acceptable {
    private static int idCounter = 1;

    private final String id;
    private final MissionRank rank;
    private final int reward;
    private final Rank minRankRequired;
    private volatile MissionStatus status;
    private Ninja assignedNinja;

    public Mission(MissionRank rank, int reward, Rank minRankRequired) {
        this.id = String.valueOf(idCounter++);
        this.rank = rank;
        this.reward = reward;
        this.minRankRequired = minRankRequired;
        this.status = MissionStatus.PENDIENTE;
        this.assignedNinja = null;
    }

    public String getId() {
        return id;
    }

    public MissionRank getRank() {
        return rank;
    }

    public int getReward() {
        return reward;
    }

    public Rank getMinRankRequired() {
        return minRankRequired;
    }

    public MissionStatus getStatus() {
        return status;
    }

    public Ninja getAssignedNinja() {
        return assignedNinja;
    }

    /**
     * Intenta asignar la misión a un ninja si está disponible y cumple con el
     * rango.
     * 
     * @return true si se asignó correctamente, false si no está disponible.
     */
    public synchronized boolean assignTo(Ninja ninja) {
        if (status == MissionStatus.EN_PROGRESO || status == MissionStatus.COMPLETADA) {
            // No se puede asignar si ya está en progreso o completada
            return false;
        }
        if (ninja.getRango().ordinal() < minRankRequired.ordinal()) {
            throw new IllegalArgumentException("El ninja no cumple con el rango mínimo para esta misión.");
        }
        this.assignedNinja = ninja;
        this.status = MissionStatus.EN_PROGRESO;
        startMission();
        return true;
    }

    /**
     * Inicia un hilo para simular la duración de la misión.
     * Actualiza el estado y asignación según éxito o fracaso.
     */
    private void startMission() {
        int seconds = switch (rank) {
            case D -> 20;
            case C -> 15;
            case B -> 10;
            case A -> 30;
            case S -> 25;
        };

        new Thread(() -> {
            try {
                Thread.sleep(seconds * 1000L);
                if (Math.random() < 0.5) {
                    status = MissionStatus.COMPLETADA;
                    System.out.println("El ninja " + assignedNinja.getNombre() + " completó la misión " + id);
                } else {
                    System.out.println(
                            "El ninja " + assignedNinja.getNombre() + " fracasó en la ejecución de la misión " + id);
                    status = MissionStatus.PENDIENTE;
                    assignedNinja = null;
                }
            } catch (InterruptedException e) {
                System.out.println("La misión " + id + " fue interrumpida. Misión vuelve a estar disponible.");
                status = MissionStatus.PENDIENTE;
                assignedNinja = null;
            }
        }).start();
    }

    @Override
    public void accept(NarutoVisitor visitor) {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        return String.format("%s (%s) reward:%d min:%s estado:%s ninja:%s",
                id,
                rank,
                reward,
                minRankRequired,
                status,
                assignedNinja != null ? assignedNinja.getNombre() : "N/A");
    }
}
