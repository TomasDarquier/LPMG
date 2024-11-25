package com.example.user_service.services;

import com.example.user_service.dtos.UserDto;
import com.example.user_service.entities.User;

import java.util.Optional;

public interface UserService {

    /**
     * Registra un nuevo usuario en el sistema.
     *
     * @param user El DTO que contiene la información del usuario a registrar.
     * @return El objeto {@link User} registrado en el sistema.
     */
    User register(UserDto user);

    /**
     * Busca un usuario en el sistema por su ID.
     *
     * @param id El ID del usuario a buscar.
     * @return Un objeto {@link Optional} que contiene el usuario si se encuentra, o un {@link Optional#empty()} si no se encuentra.
     */
    Optional<User> findById(Long id);

    /**
     * Verifica si un usuario existe en el sistema por su correo electrónico.
     *
     * @param email El correo electrónico del usuario a verificar.
     * @return {@code true} si el usuario existe, {@code false} en caso contrario.
     */
    Boolean existsByEmail(String email);

    /**
     * Busca un usuario en el sistema por su correo electrónico.
     *
     * @param email El correo electrónico del usuario a buscar.
     * @return Un objeto {@link Optional} que contiene el usuario si se encuentra, o un {@link Optional#empty()} si no se encuentra.
     */
    Optional<User> findByEmail(String email);

    /**
     * Obtiene el nombre de un usuario en base a su ID.
     *
     * @param id El ID del usuario cuyo nombre se desea obtener.
     * @return Un objeto {@link Optional} que contiene el nombre del usuario si se encuentra, o un {@link Optional#empty()} si no se encuentra.
     */
    Optional<String> getNameById(Long id);
}