package com.simkernel.ui;

import javafx.animation.PauseTransition;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.Duration;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        // Carga intro.fxml
        FXMLLoader introLoader = new FXMLLoader(getClass().getResource("/fxml/intro.fxml"));
        Parent introRoot = introLoader.load();

        IntroController controller = introLoader.getController();
        controller.setStage(stage); // ESTA LÍNEA FALTABA

        Scene introScene = new Scene(introRoot);
        stage.setTitle("Booting Sim-Kernel...");
        stage.setScene(introScene);
        stage.show();

        // Espera 3 segundos y luego cambia a la interfaz principal
        PauseTransition pause = new PauseTransition(Duration.seconds(3));
        pause.setOnFinished(event -> {
            try {
                FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("/fxml/sim_view.fxml"));
                Parent mainRoot = mainLoader.load();

                Scene mainScene = new Scene(mainRoot);
                mainScene.getStylesheets().add(getClass().getResource("/styles/styles.css").toExternalForm());

                stage.setScene(mainScene);
                stage.setTitle("Sim-Kernel: Sistema Operativo Simulado");
                stage.show();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        pause.play();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
