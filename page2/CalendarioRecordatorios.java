import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.StringConverter;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class CalendarioRecordatorios extends Application {

    private HashMap<LocalDate, String> recordatorios = new HashMap<>();
    private Color colorFondo = Color.WHITE;
    private String idioma = "es";
    private LocalDate fechaActual = LocalDate.now();

    @Override
    public void start(Stage primaryStage) {
        BorderPane root = new BorderPane();
        root.setPrefSize(800, 600);

        VBox vbox = new VBox();
        vbox.setAlignment(Pos.CENTER);
        vbox.setSpacing(10);
        vbox.setPadding(new Insets(10));

        DatePicker datePicker = new DatePicker();
        datePicker.setConverter(new StringConverter<LocalDate>() {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", new Locale(idioma));

            @Override
            public String toString(LocalDate date) {
                if (date != null) {
                    return dateFormatter.format(date);
                } else {
                    return "";
                }
            }

            @Override
            public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, dateFormatter);
                } else {
                    return null;
                }
            }
        });

        TextArea textArea = new TextArea();
        textArea.setPrefSize(200, 100);

        Button agregarButton = new Button("Agregar Recordatorio");
        agregarButton.setOnAction(e -> {
            LocalDate fecha = datePicker.getValue();
            String recordatorio = textArea.getText();
            recordatorios.put(fecha, recordatorio);
            actualizarCalendario(root);
        });

        Button mesAnteriorButton = new Button("<<");
        mesAnteriorButton.setOnAction(e -> {
            fechaActual = fechaActual.minusMonths(1);
            actualizarCalendario(root);
        });

        Button mesSiguienteButton = new Button(">>");
        mesSiguienteButton.setOnAction(e -> {
            fechaActual = fechaActual.plusMonths(1);
            actualizarCalendario(root);
        });

        HBox botonesNavegacion = new HBox(mesAnteriorButton, mesSiguienteButton);
        botonesNavegacion.setAlignment(Pos.CENTER);
        botonesNavegacion.setSpacing(10);

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
                    colorFondo = Color.GREEN;
                    break;
                case"Azul":
                    colorFondo = Color.BLUE;
                    break;
            }
            root.setStyle("-fx-background-color: #" + Double.toHexString(colorFondo.getRed()).substring(2) + Double.toHexString(colorFondo.getGreen()).substring(2) + Double.toHexString(colorFondo.getBlue()).substring(2));
        });

        ComboBox<String> idiomaComboBox = new ComboBox<>();
        idiomaComboBox.getItems().addAll("Español", "English");
        idiomaComboBox.setValue("Español");
        idiomaComboBox.setOnAction(e -> {
            switch (idiomaComboBox.getValue()) {
                case "Español":
                    idioma = "es";
                    break;
                case "English":
                    idioma = "en";
                    break;
            }
            actualizarCalendario(root);
        });

        vbox.getChildren().addAll(new Label("Selecciona una fecha:"), datePicker, new Label("Escribe un recordatorio:"), textArea, agregarButton, new Label("Selecciona un color:"), colorComboBox, new Label("Selecciona un idioma:"), idiomaComboBox, botonesNavegacion);

        root.setLeft(vbox);

        actualizarCalendario(root);

        Scene scene = new Scene(root);
        scene.getStylesheets().add("styles.css");

        primaryStage.setScene(scene);
        primaryStage.setTitle("Calendario de Recordatorios");
        primaryStage.show();
    }

    private void actualizarCalendario(BorderPane root) {
        GridPane calendario = new GridPane();
        calendario.setAlignment(Pos.CENTER);
        calendario.setHgap(10);
        calendario.setVgap(10);

        DateTimeFormatter formatoMes = DateTimeFormatter.ofPattern("MMMM yyyy", new Locale(idioma));
        DateTimeFormatter formatoDia = DateTimeFormatter.ofPattern("d");

        LocalDate fecha = LocalDate.of(fechaActual.getYear(), fechaActual.getMonth(), 1);
        int diaMesActual = fecha.getDayOfMonth();
        LocalDate primerDiaMes = fecha.minusDays(diaMesActual - 1);
        int diaSemanaPrimerDia = primerDiaMes.getDayOfWeek().getValue();

        Label mesLabel = new Label(fechaActual.format(formatoMes));
        mesLabel.setFont(Font.font("Arial", FontWeight.BOLD, 20));
        calendario.add(mesLabel, 0, 0, 7, 1);

        for (int i = 0; i < 7; i++) {
            Label diaSemanaLabel = new Label(formatoDiaSemana(i + 1));
            diaSemanaLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
            calendario.add(diaSemanaLabel, i, 1);
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
            calendario.add(vBox, columna, fila + 2);
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

    public void setRecordatorios(HashMap<LocalDate, String> recordatorios) {
        this.recordatorios = recordatorios;
    }

    public static void main(String[] args) throws Exception {
        launch(args);
    }
}