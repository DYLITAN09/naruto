package naruto.visitor;

import naruto.model.Mission;
import naruto.model.Ninja;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.stream.Collectors;

public class ExportVisitor implements NarutoVisitor {
    private final StringBuilder sb = new StringBuilder();
    private final String format;

    public ExportVisitor(String format) {
        this.format = format.toUpperCase(Locale.ROOT);
    }

    @Override
    public void visit(Ninja ninja) {
        switch (format) {
            case "JSON":
                if (sb.length() == 0)
                    sb.append("[");
                else
                    sb.append(",");
                sb.append("{")
                        .append("\"nombre\":\"").append(ninja.getNombre()).append("\",")
                        .append("\"rango\":\"").append(ninja.getRango()).append("\",")
                        .append("\"aldea\":\"").append(ninja.getAldea()).append("\",")
                        .append("\"ataque\":").append(ninja.getAtaque()).append(",")
                        .append("\"defensa\":").append(ninja.getDefensa()).append(",")
                        .append("\"chakra\":").append(ninja.getChakra()).append(",")
                        .append("\"jutsus\":[")
                        .append(ninja.getJutsus().stream()
                                .map(j -> "\"" + j.getNombre() + "\"")
                                .collect(Collectors.joining(",")))
                        .append("]}\n");
                break;

            case "XML":
                sb.append("<ninja>")
                        .append("<nombre>").append(ninja.getNombre()).append("</nombre>")
                        .append("<rango>").append(ninja.getRango()).append("</rango>")
                        .append("<aldea>").append(ninja.getAldea()).append("</aldea>")
                        .append("<ataque>").append(ninja.getAtaque()).append("</ataque>")
                        .append("<defensa>").append(ninja.getDefensa()).append("</defensa>")
                        .append("<chakra>").append(ninja.getChakra()).append("</chakra>")
                        .append("<jutsus>")
                        .append(ninja.getJutsus().stream()
                                .map(j -> "<jutsu>" + j.getNombre() + "</jutsu>")
                                .collect(Collectors.joining()))
                        .append("</jutsus>")
                        .append("</ninja>\n");
                break;

            default: // TXT
                sb.append(ninja.toString()).append("\n");
        }
    }

    @Override
    public void visit(Mission mission) {
        switch (format) {
            case "JSON":
                if (sb.length() == 0)
                    sb.append("[");
                else
                    sb.append(",");
                sb.append("{")
                        .append("\"id\":\"").append(mission.getId()).append("\",")
                        .append("\"rank\":\"").append(mission.getRank()).append("\",")
                        .append("\"reward\":").append(mission.getReward()).append(",")
                        .append("\"minRankRequired\":\"").append(mission.getMinRankRequired()).append("\",")
                        .append("\"status\":\"").append(mission.getStatus()).append("\",")
                        .append("\"assignedNinja\":")
                        .append(mission.getAssignedNinja() != null
                                ? "\"" + mission.getAssignedNinja().getNombre() + "\""
                                : "null")
                        .append("}\n");
                break;

            case "XML":
                sb.append("<mission>")
                        .append("<id>").append(mission.getId()).append("</id>")
                        .append("<rank>").append(mission.getRank()).append("</rank>")
                        .append("<reward>").append(mission.getReward()).append("</reward>")
                        .append("<minRankRequired>").append(mission.getMinRankRequired()).append("</minRankRequired>")
                        .append("<status>").append(mission.getStatus()).append("</status>")
                        .append("<assignedNinja>")
                        .append(mission.getAssignedNinja() != null
                                ? mission.getAssignedNinja().getNombre()
                                : "")
                        .append("</assignedNinja>")
                        .append("</mission>\n");
                break;

            default: // TXT
                sb.append(mission.toString()).append("\n");
        }
    }

    public String getResult() {
        if (format.equals("JSON") && sb.length() > 0 && sb.charAt(0) == '[' && sb.charAt(sb.length() - 1) != ']') {
            sb.append("]");
        }
        return sb.toString();
    }

    public void exportToFile(String fileName) {
        try {
            String reportsPath = Paths.get(System.getProperty("user.dir"), "src", "naruto", "reports").toString();

            // Crear carpeta si no existe
            File folder = new File(reportsPath);
            if (!folder.exists()) {
                folder.mkdirs();
            }

            String dateStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

            String extension = format.toLowerCase(Locale.ROOT);
            File file = new File(folder, fileName + "_" + dateStamp + "." + extension);

            try (FileWriter writer = new FileWriter(file)) {
                writer.write(getResult());
            }

            System.out.println("Reporte exportado en: " + file.getAbsolutePath());

        } catch (IOException e) {
            System.err.println("Error al exportar el reporte: " + e.getMessage());
        }
    }
}
