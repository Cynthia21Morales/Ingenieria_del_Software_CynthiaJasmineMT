package com.jasminesystems.reconocimientofacial;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.opencv.core.Core;

public class MainApplication extends Application {

    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    @Override
    public void start(Stage stage) throws Exception {

        FXMLLoader loader = new FXMLLoader(MainApplication.class.getResource("main-view.fxml"));
        Scene scene = new Scene(loader.load());
        stage.setTitle("Sistema de Reconocimiento Facial");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}