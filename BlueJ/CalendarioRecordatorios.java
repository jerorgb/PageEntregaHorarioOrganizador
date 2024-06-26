import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.*;

public class CalendarioRecordatorios extends Application {

    private HashMap<LocalDate, String> recordatorios = new HashMap<>();
    private Color colorFondo = Color.WHITE;
    private String idioma = "es";

    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();
        root.setPrefSize(800, 600);

        VBox vbox = new VBox();
        vbox.setAlignment(Pos.CENTER);
        vbox.setSpacing(10);

        DatePicker datePicker = new DatePicker();
        TextArea textArea = new TextArea();
        textArea.setPrefSize(200, 100);

        Button agregarButton = new Button("Agregar Recordatorio");
        agregarButton.setOnAction(e -> {
            LocalDate fecha = datePicker.getValue();
            String recordatorio = textArea.getText();
            recordatorios.put(fecha, recordatorio);
            actualizarCalendario(root);
        });

        ComboBox<String> colorComboBox = new ComboBox<>();
        colorComboBox.getItems().addAll("Blanco", "Amarillo", "Verde", "Azul");
        colorComboBox.setValue("Blanco");
        colorComboBox.setOnAction(e -> {
            switch (colorComboBox.getValue()) {
                case "Blanco":
                    colorFondo = Color.WHITE;
                    break;
                case "Amarillo":
                    colorFondo = Color.YELLOW;
                    break;
                case "Verde":
                    colorFondo = Color.LIGHTGREEN;
                    break;
                case "Azul":
                    colorFondo = Color.LIGHTBLUE;
                    break;
                default:
                    colorFondo = Color.WHITE;
            }
            root.setStyle("-fx-background-color: #" + colorFondo.toString().substring(2, 8));
        });

        ComboBox<String> idiomaComboBox = new ComboBox<>();
        idiomaComboBox.getItems().addAll("Español", "Inglés");
        idiomaComboBox.setValue("Español");
        idiomaComboBox.setOnAction(e -> {
            idioma = idiomaComboBox.getValue().substring(0, 2).toLowerCase();
            actualizarCalendario(root);
        });

        vbox.getChildren().addAll(datePicker, textArea, agregarButton, colorComboBox, idiomaComboBox);
        root.setLeft(vbox);

        Scene scene = new Scene(root);
        scene.getStylesheets().add("styles.css");

        primaryStage.setScene(scene);
        primaryStage.setTitle("Calendario de Recordatorios");
        primaryStage.show();

        actualizarCalendario(root);
    }

    private void actualizarCalendario(BorderPane root) {
        GridPane calendario = new GridPane();
        calendario.setAlignment(Pos.CENTER);
        calendario.setHgap(10);
        calendario.setVgap(10);

        DateTimeFormatter formatoMes = DateTimeFormatter.ofPattern("MMMM yyyy", new Locale(idioma));
        DateTimeFormatter formatoDia = DateTimeFormatter.ofPattern("d");

        LocalDate fecha = LocalDate.now();
        int diaMesActual = fecha.getDayOfMonth();
        LocalDate primerDiaMes = fecha.minusDays(diaMesActual - 1);
        int diaSemanaPrimerDia = primerDiaMes.getDayOfWeek().getValue();

        for (int i = 0; i < 7; i++) {
            Label diaSemanaLabel = new Label(formatoDiaSemana(i + 1));
            diaSemanaLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
            calendario.add(diaSemanaLabel, i, 0);
        }

        for (int i = 1; i <= primerDiaMes.lengthOfMonth(); i++) {
            fecha = LocalDate.of(primerDiaMes.getYear(), primerDiaMes.getMonth(), i);
            int fila = (i + diaSemanaPrimerDia - 1) / 7;
            int columna = (i + diaSemanaPrimerDia - 1) % 7;
            VBox vBox = new VBox();
            vBox.setAlignment(Pos.TOP_CENTER);
            Label diaLabel = new Label(Integer.toString(i));
            vBox.getChildren().add(diaLabel);
            if (recordatorios.containsKey(fecha)) {
                Label recordatorioLabel = new Label(recordatorios.get(fecha));
                recordatorioLabel.setFont(Font.font("Arial", FontWeight.NORMAL, 10));
                vBox.getChildren().add(recordatorioLabel);
            }
            calendario.add(vBox, columna, fila + 1);
        }

        root.setCenter(calendario);
    }

    private String formatoDiaSemana(int dia) {
        switch (dia) {
            case 1:
                return "L";
            case 2:
                return "M";
            case 3:
                return "M";
            case 4:
                return "J";
            case 5:
                return "V";
            case 6:
                return "S";
            case 7:
                return "D";
            default:
                return "";
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}