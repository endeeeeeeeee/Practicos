module com.simkernel {
    // Módulos de JavaFX
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    // Dependencias externas
    requires okhttp3;
    requires com.fasterxml.jackson.databind;
    requires org.apache.logging.log4j;
    //requires com.theokanning.openai.gpt3.java;

    // Permisos para reflexión
    opens com.simkernel.ui to javafx.fxml;
    opens com.simkernel.ai to com.fasterxml.jackson.databind;

    // Exporta los paquetes necesarios
    exports com.simkernel.ui;
    exports com.simkernel.ai;
    exports com.simkernel.io;
}