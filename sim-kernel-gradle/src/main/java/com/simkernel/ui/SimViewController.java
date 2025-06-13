package com.simkernel.ui;



import com.simkernel.ai.GroqHelper;
import com.simkernel.core.EstrategiaMemoria;
import com.simkernel.process.Proceso;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Duration;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class SimViewController {

    @FXML private TableView<Proceso> tablaProcesos;
    @FXML private TableColumn<Proceso, String> colPid;
    @FXML private TableColumn<Proceso, String> colNombre;
    @FXML private TableColumn<Proceso, String> colEstado;
    @FXML private TableColumn<Proceso, String> colTiempo;
    @FXML private ProgressBar barraMemoria;
    @FXML private Button btnSimular;
    @FXML private TableView<Proceso> tablaBloqueados;
    @FXML private TableColumn<Proceso, String> colBloqPid;
    @FXML private TableColumn<Proceso, String> colBloqNombre;
    @FXML private TableColumn<Proceso, String> colBloqMotivo;
    @FXML private ComboBox<String> comboEstrategia;
    @FXML private TextArea chatArea;
    @FXML private TextField inputPregunta;
    @FXML private TextArea logArea;
    @FXML private ListView<String> listaProcesosEnEspera;
    @FXML private ListView<String> listaSeguridad;
   // @FXML private Button btnEnviar;
 


    private GroqHelper aiHelper;
    private final ObservableList<Proceso> listaProcesos = FXCollections.observableArrayList();
    private final ObservableList<Proceso> listaBloqueados = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Configuración de columnas
        Tooltip.install(btnSimular, new Tooltip("Haz clic para iniciar la simulación del sistema operativo"));
        Tooltip.install(comboEstrategia, new Tooltip("Selecciona cómo se asignará la memoria a los procesos"));
        colPid.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getPid())));
        colNombre.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getNombre()));
        colEstado.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getEstado().toString()));
        colTiempo.setCellValueFactory(cellData ->
                new SimpleStringProperty(String.valueOf(cellData.getValue().getTiempoCpu())));
        colBloqPid.setCellValueFactory(data -> new SimpleStringProperty(String.valueOf(data.getValue().getPid())));
        colBloqNombre.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNombre()));
        colBloqMotivo.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getEstado().name()));
        //comboEstrategia.getItems().addAll("First Fit", "Best Fit");
        comboEstrategia.setValue("First Fit");

        tablaProcesos.setItems(listaProcesos);

        // Acción del botón
        btnSimular.setOnAction(event -> simularProcesos());

        // Animación de aparición suave
        animarInicio();

        // Initialize aiHelper with environment variable
        // En initialize(), cambia OpenAIHelper por GroqHelper:
        String apiKey = System.getenv("GROQ_API_KEY");  // O ponla directamente: "tu-api-key-aquí"
        if (apiKey == null || apiKey.isEmpty()) {
            chatArea.appendText("⚠️ Error: GROQ_API_KEY no configurada.\n");
        } else {
            aiHelper = new GroqHelper(apiKey);  // ¡Aquí usas GroqHelper!
        }

        // Ejemplo: al presionar enter en el campo de texto
        inputPregunta.setOnAction(e -> {
            if (aiHelper == null) {
                chatArea.appendText("⚠️ Error: AI no está inicializada.\n");
                return;
            }
            String pregunta = inputPregunta.getText();
            if (!pregunta.isEmpty()) {
                try {
                    String respuesta = aiHelper.generarRespuesta("Explícale a un estudiante qué está pasando con los procesos: " + pregunta);
                    chatArea.appendText("🧠 AI: " + respuesta + "\n");
                } catch (IOException ex) {
                    chatArea.appendText("⚠️ Error al conectar con la API de OpenAI: " + ex.getMessage() + "\n");
                }
                inputPregunta.clear();
            }
        });
        FadeTransition fadeChat = new FadeTransition(Duration.millis(400), chatArea);
        fadeChat.setFromValue(0.6);
        fadeChat.setToValue(1.0);
        fadeChat.play();

       // btnEnviar.setOnAction(e -> procesarPregunta());
        inputPregunta.setOnAction(e -> procesarPregunta());


    }

    private void simularProcesos() {
        listaProcesos.clear();
        listaBloqueados.clear();
        listaSeguridad.getItems().clear();
        listaProcesosEnEspera.getItems().clear();
        logArea.clear();

        List<Proceso> procesos = List.of(
                new Proceso(1, "Editor", 10, 100, 1),
                new Proceso(2, "Compilador", 15, 80, 2),
                new Proceso(3, "Navegador", 20, 120, 3),
                new Proceso(4, "Terminal", 5, 90, 1),
                new Proceso(5, "Updater", 12, 70, 2),
                new Proceso(6, "Antivirus", 30, 200, 1),
                new Proceso(7, "Backup", 40, 300, 2),
                new Proceso(8, "Juego", 25, 150, 3)
        );

        for (Proceso p : procesos) {
            // Simulación simple de estado
            if (p.getNombre().equalsIgnoreCase("Updater")) {
                registrarAccesoDenegado(p.getNombre() + " intentó acceder a un recurso exclusivo.");
            }

            if (p.getPid() % 3 == 0) {
                p.setEstado(Proceso.Estado.BLOQUEADO);
                listaBloqueados.add(p);
            } else {
                p.setEstado(Proceso.Estado.LISTO);
                listaProcesos.add(p);
            }

            agregarLog("Proceso " + p.getNombre() + " creado con estado: " + p.getEstado());
        }

        tablaProcesos.setItems(listaProcesos);
        tablaBloqueados.setItems(listaBloqueados);
        actualizarResumen(procesos);
        barraMemoria.setProgress(0.75);
    }

    public void registrarAccesoDenegado(String mensaje) {
        Platform.runLater(() -> listaSeguridad.getItems().add("❌ Acceso denegado: " + mensaje));
    }

    public EstrategiaMemoria getEstrategiaSeleccionada() {
        String seleccion = comboEstrategia.getValue();
        return "Best Fit".equals(seleccion) ? EstrategiaMemoria.BEST_FIT : EstrategiaMemoria.FIRST_FIT;
    }

    public void animarInicio() {
        FadeTransition fade = new FadeTransition(Duration.millis(800), tablaProcesos);
        fade.setFromValue(0.0);
        fade.setToValue(1.0);
        fade.play();
        FadeTransition fadeMem = new FadeTransition(Duration.millis(800), barraMemoria);
        fadeMem.setFromValue(0.0);
        fadeMem.setToValue(1.0);
        fadeMem.setDelay(Duration.millis(300));
        fadeMem.play();
    }
    private void procesarPregunta() {
        if (aiHelper == null) {
            chatArea.appendText("⚠️ Error: AI no está inicializada.\n");
            return;
        }

        String pregunta = inputPregunta.getText().trim();
        if (!pregunta.isEmpty()) {
            chatArea.appendText("📩 Tú: " + pregunta + "\n");
            inputPregunta.clear();

            new Thread(() -> {
                try {
                    String respuesta = aiHelper.generarRespuesta("Explícale a un estudiante qué está pasando con los procesos: " + pregunta);
                    Platform.runLater(() -> chatArea.appendText("🤖 AI: " + respuesta + "\n"));
                } catch (IOException ex) {
                    Platform.runLater(() -> chatArea.appendText("⚠️ Error de conexión: " + ex.getMessage() + "\n"));
                }
            }).start();
        }
    }
    public void agregarLog(String mensaje) {
        Platform.runLater(() -> logArea.appendText(mensaje + "\n"));
    }
    public void actualizarResumen(List<Proceso> procesos) {
        List<String> resumen = procesos.stream()
                .filter(p -> p.getEstado() == Proceso.Estado.LISTO || p.getEstado() == Proceso.Estado.BLOQUEADO)
                .map(p -> p.getPid() + " - " + p.getNombre() + " (" + p.getEstado() + ")")
                .collect(Collectors.toList());

        Platform.runLater(() -> listaProcesosEnEspera.getItems().setAll(resumen));
    }




}