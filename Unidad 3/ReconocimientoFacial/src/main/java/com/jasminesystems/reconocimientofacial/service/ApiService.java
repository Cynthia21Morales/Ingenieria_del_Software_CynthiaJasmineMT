package com.jasminesystems.reconocimientofacial.service;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ApiService {

    public void registrarAsistencia(int idDocente) {

        try {

            URL url = new URL("http://localhost:8081/api/registrar-asistencia");

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setDoOutput(true);

            String json = "{ \"id_docente\": " + idDocente + " }";

            OutputStream os = conn.getOutputStream();
            os.write(json.getBytes());
            os.flush();

            int response = conn.getResponseCode();

            System.out.println("Respuesta backend: " + response);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}