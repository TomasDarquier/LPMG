package com.example.user_service.services;

import com.example.user_service.entities.User;

public interface UserActivityService {
    /**
     * Registra la actividad de registro de un usuario en el sistema.
     *
     * @param user El usuario que ha realizado el registro.
     */
    void registerSignUpActivity(User user);

    /**
     * Registra la actividad de inicio de sesión de un usuario.
     *
     * @param user El usuario que ha iniciado sesión.
     */
    void registerLoginActivity(User user);

    /**
     * Registra la actividad de generar codigo en el canvas
     *
     * @param user El usuario que ha iniciado sesion.
     */
    void registerCodeGenerationActivity(User user);

    /**
     * Registra la actividad de descargar codigo(generado previamente en el canvas ) desde el perfil de usuario
     *
     * @param user El usuario que ha iniciado sesion.
     */
    void registerDownloadActivity(User user);

}
