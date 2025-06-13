package com.simkernel.ui;

import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;

public class IntroController {

    @FXML private Label txtGlitch;
    @FXML private ImageView bgGlitch;
    @FXML private ImageView tecnico;
    @FXML private Label txtFixing;
    private Stage stage;

    @FXML
    public void initialize() {
        // Cargar imágenes desde resources/assets/
        bgGlitch.setImage(new Image("/assets/glitch.gif"));
        tecnico.setImage(new Image("/assets/tecnico.png"));

        // Animaciones
        FadeTransition glitchOut = new FadeTransition(Duration.seconds(1.5), txtGlitch);
        glitchOut.setFromValue(1);
        glitchOut.setToValue(0);

        FadeTransition tecnicoIn = new FadeTransition(Duration.seconds(1.5), tecnico);
        tecnicoIn.setFromValue(0);
        tecnicoIn.setToValue(1);

        FadeTransition textoFix = new FadeTransition(Duration.seconds(1), txtFixing);
        textoFix.setFromValue(0);
        textoFix.setToValue(1);

        PauseTransition pausa = new PauseTransition(Duration.seconds(1.5));

        PauseTransition pausa1 = new PauseTransition(Duration.seconds(1.5));
        PauseTransition pausa2 = new PauseTransition(Duration.seconds(1.5));

        SequentialTransition secuencia = new SequentialTransition(
                glitchOut, pausa1, tecnicoIn, textoFix, pausa2
        );

        secuencia.setOnFinished(e -> cargarSimulador());

        secuencia.play();
    }

    private void cargarSimulador() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/sim_view.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/styles/styles.css").toExternalForm());

            if (stage == null) {
                System.err.println("⚠️ Stage es null en IntroController");
                return;
            }

            stage.setScene(scene);
            stage.setTitle("Sim-Kernel: Sistema Operativo Simulado");
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }
}
