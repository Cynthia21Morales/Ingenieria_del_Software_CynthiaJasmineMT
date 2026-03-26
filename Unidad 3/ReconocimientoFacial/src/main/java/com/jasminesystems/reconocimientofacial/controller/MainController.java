package com.jasminesystems.reconocimientofacial.controller;

import com.jasminesystems.reconocimientofacial.recognition.FaceCamera;
import com.jasminesystems.reconocimientofacial.service.ApiService;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class MainController {

    @FXML
    private ImageView cameraView;

    @FXML
    private TextField txtIdDocente;

    @FXML
    private Label statusLabel;

    @FXML
    private Label fechaLabel;

    @FXML
    private Label horaLabel;

    private FaceCamera faceCamera;
    private ApiService apiService;

    @FXML
    public void initialize() {

        faceCamera = new FaceCamera(cameraView);
        apiService = new ApiService();

        Image apagada = new Image(
                getClass().getResourceAsStream("/images/camara_off.png")
        );

        cameraView.setImage(apagada);

        statusLabel.setText("Presione Iniciar Cámara");

        iniciarReloj();

    }

    private void iniciarReloj() {

        Timeline clock = new Timeline(new KeyFrame(Duration.seconds(1), e -> {

            LocalDate fecha = LocalDate.now();
            LocalTime hora = LocalTime.now();

            DateTimeFormatter formatoFecha =
                    DateTimeFormatter.ofPattern("dd/MM/yyyy");

            DateTimeFormatter formatoHora =
                    DateTimeFormatter.ofPattern("HH:mm:ss");

            fechaLabel.setText(fecha.format(formatoFecha));
            horaLabel.setText(hora.format(formatoHora));

        }));

        clock.setCycleCount(Timeline.INDEFINITE);
        clock.play();

    }

    @FXML
    private void iniciarCamara() {

        faceCamera.startCamera();
        statusLabel.setText("Cámara iniciada");

    }

    @FXML
    private void detenerCamara() {

        faceCamera.stopCamera();

        Image apagada = new Image(
                getClass().getResourceAsStream("/images/camara_off.png")
        );

        cameraView.setImage(apagada);

        statusLabel.setText("Cámara apagada");

    }

    @FXML
    private void registrarRostro() {

        String id = txtIdDocente.getText();

        if (id.isEmpty()) {

            statusLabel.setText("Ingrese ID del docente");
            return;

        }

        faceCamera.captureFaces(id);

        statusLabel.setText("Rostro registrado");

    }

    @FXML
    private void tomarAsistencia() {

        int idDocente = faceCamera.recognizeFace();

        if (idDocente != -1) {

            apiService.registrarAsistencia(idDocente);

            statusLabel.setText("Asistencia registrada: " + idDocente);

        } else {

            statusLabel.setText("Docente no reconocido");

        }

    }

}