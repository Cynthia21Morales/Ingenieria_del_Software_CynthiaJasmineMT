package com.jasminesystems.reconocimientofacial.service;

public class AsistenciaService {

    private ApiService apiService = new ApiService();

    public void registrar(int idDocente) {

        apiService.registrarAsistencia(idDocente);

    }

}
