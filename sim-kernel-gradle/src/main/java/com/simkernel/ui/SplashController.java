package com.simkernel.ui;

import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.stage.Stage;
import javafx.util.Duration;

public class SplashController {

    @FXML private Label labelMensaje;
    @FXML private ProgressBar barraCarga;

    private final String mensaje = "Iniciando Sim-Kernel...";

    @FXML
    public void initialize() {
        escribirTexto();
        simularCarga();
    }

    private void escribirTexto() {
        Timeline timeline = new Timeline();
        for (int i = 0; i < mensaje.length(); i++) {
            final int idx = i;
            KeyFrame frame = new KeyFrame(Duration.millis(50 * i), e -> {
                labelMensaje.setText(mensaje.substring(0, idx + 1));
            });
            timeline.getKeyFrames().add(frame);
        }
        timeline.play();
    }

    private void simularCarga() {
        Timeline carga = new Timeline(
                new KeyFrame(Duration.seconds(0), new KeyValue(barraCarga.progressProperty(), 0)),
                new KeyFrame(Duration.seconds(2.5), e -> irAMain())
        );
        carga.play();
    }

    private void irAMain() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/sim_view.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) labelMensaje.getScene().getWindow();
            stage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
