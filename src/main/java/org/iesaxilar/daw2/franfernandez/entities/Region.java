package org.iesaxilar.daw2.franfernandez.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * La clase Region representa una entidad que modela una región dentro de la base de datos.
 * Contiene tres campos: 'id', 'code' y 'name'.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Region {

    // Identificador único de la región.
    private int id;

    // Código de la región, por ejemplo "01" para Andalucía.
    private String code;

    // Nombre completo de la región.
    private String name;

    /**
     * Constructor personalizado que no incluye el campo 'id'.
     * Se utiliza cuando no es necesario conocer el id de la región,
     * por ejemplo, al insertar una nueva región.
     *
     * @param code Código de la región.
     * @param name Nombre de la región.
     */
    public Region(String code, String name) {
        this.code = code;
        this.name = name;
    }
}