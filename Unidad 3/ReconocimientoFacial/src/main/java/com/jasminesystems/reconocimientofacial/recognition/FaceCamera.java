package com.jasminesystems.reconocimientofacial.recognition;

import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;

import java.io.ByteArrayInputStream;

public class FaceCamera {

    private VideoCapture camera;
    private CascadeClassifier faceDetector;
    private ImageView cameraView;

    private volatile boolean cameraActive = false;

    public FaceCamera(ImageView cameraView) {

        this.cameraView = cameraView;

        camera = new VideoCapture();

        faceDetector = new CascadeClassifier(
                "src/main/resources/com/jasminesystems/reconocimientofacial/haarcascade_frontalface_alt.xml"
        );
    }

    public void startCamera() {

        if (cameraActive) return;

        // usar DirectShow (más estable en Windows)
        camera.open(0, Videoio.CAP_DSHOW);

        if (!camera.isOpened()) {
            System.out.println("No se pudo abrir la cámara");
            return;
        }

        // bajar resolución para mejorar rendimiento
        camera.set(Videoio.CAP_PROP_FRAME_WIDTH, 320);
        camera.set(Videoio.CAP_PROP_FRAME_HEIGHT, 240);

        cameraActive = true;

        Thread cameraThread = new Thread(() -> {

            Mat frame = new Mat();
            int frameCounter = 0;

            while (cameraActive) {

                if (camera.read(frame)) {

                    frameCounter++;

                    // detectar rostro solo cada 5 frames
                    if (frameCounter % 5 == 0) {
                        detectFaces(frame);
                    }

                    Image image = matToImage(frame);

                    Platform.runLater(() -> cameraView.setImage(image));
                }

                try {
                    Thread.sleep(30);
                } catch (Exception ignored) {}
            }

        });

        cameraThread.setDaemon(true);
        cameraThread.start();
    }

    public void stopCamera() {

        cameraActive = false;

        if (camera.isOpened()) {
            camera.release();
        }
    }

    private void detectFaces(Mat frame) {

        Mat gray = new Mat();

        Imgproc.cvtColor(frame, gray, Imgproc.COLOR_BGR2GRAY);
        Imgproc.equalizeHist(gray, gray);

        MatOfRect faces = new MatOfRect();

        faceDetector.detectMultiScale(gray, faces);

        for (Rect rect : faces.toArray()) {

            Imgproc.rectangle(
                    frame,
                    new Point(rect.x, rect.y),
                    new Point(rect.x + rect.width, rect.y + rect.height),
                    new Scalar(0,255,0),
                    2
            );
        }
    }

    public void captureFaces(String idDocente) {

        Mat frame = new Mat();

        if (camera.read(frame)) {

            Mat gray = new Mat();
            Imgproc.cvtColor(frame, gray, Imgproc.COLOR_BGR2GRAY);

            MatOfRect faces = new MatOfRect();
            faceDetector.detectMultiScale(gray, faces);

            int contador = 0;

            for (Rect rect : faces.toArray()) {

                Mat rostro = new Mat(gray, rect);

                Imgcodecs.imwrite(
                        "faces/" + idDocente + "_" + contador + ".jpg",
                        rostro
                );

                contador++;
            }
        }
    }

    public int recognizeFace() {

        // Simulación
        return 1;
    }

    private Image matToImage(Mat frame) {

        MatOfByte buffer = new MatOfByte();

        // usar BMP es mucho más rápido que PNG
        Imgcodecs.imencode(".bmp", frame, buffer);

        return new Image(new ByteArrayInputStream(buffer.toArray()));
    }

}