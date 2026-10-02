package org.iesaxilar.daw2.franfernandez.daos;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.stream.Collectors;

/**
 * Clase de utilidad encargada de inicializar datos en la base de datos
 * a partir de un archivo SQL proporcionado como InputStream.
 */
public class DataInitializer {

    // Logger de SLF4J para registrar la ejecución del proceso
    private static final Logger logger =
            LoggerFactory.getLogger(DataInitializer.class);

    /**
     * Carga y ejecuta las sentencias SQL contenidas en un archivo.
     *
     * @param sqlFileStream InputStream del archivo SQL
     * @throws SQLException si ocurre un error en la ejecución de las sentencias SQL
     * @throws IOException si ocurre un error al leer el archivo
     */
    public static void loadDataFromSQL(InputStream sqlFileStream)
            throws SQLException, IOException {

        logger.info("Entrando en el método loadDataFromSQL");

        // Validar que el archivo no sea nulo
        if (sqlFileStream == null) {
            logger.error("El archivo SQL no se ha proporcionado o es nulo");
            throw new IOException("El archivo SQL es nulo o no se ha encontrado");
        }

        // Apertura de conexión usando el gestor de conexiones de la aplicación
        try (Connection connection = DatabaseConnectionManager.getConnection()) {

            logger.info("Conexión a la base de datos establecida");

            // Leer el contenido del archivo SQL completo en un String
            String sql;

            try (BufferedReader reader =
                         new BufferedReader(new InputStreamReader(sqlFileStream))) {

                // Une todas las líneas en un solo string separado por saltos de línea
                sql = reader.lines().collect(Collectors.joining("\n"));

            } catch (IOException e) {

                logger.error("Error al leer el archivo SQL: {}",
                        e.getMessage(), e);

                throw new IOException("Error al leer el archivo SQL", e);
            }

            // Dividir el contenido en sentencias individuales separadas por ';'
            String[] statements = sql.split(";");

            // Desactivar auto-commit para manejar transacciones manualmente
            connection.setAutoCommit(false);

            try (Statement statement = connection.createStatement()) {

                // Ejecutar cada sentencia por separado
                for (String sqlStatement : statements) {

                    if (!sqlStatement.trim().isEmpty()) {

                        logger.info("Ejecutando la sentencia SQL: {}",
                                sqlStatement);

                        statement.execute(sqlStatement.trim());
                    }
                }

                // Si todas las sentencias fueron correctas → confirmar transacción
                connection.commit();

                logger.info("Datos cargados exitosamente desde el archivo SQL");

            } catch (SQLException e) {

                // Si alguna sentencia falla → revertir transacción
                connection.rollback();

                logger.error(
                        "Error al ejecutar el archivo SQL, se ha revertido la transacción: {}",
                        e.getMessage(), e);

                throw new SQLException(
                        "Error al ejecutar el archivo SQL, transacción revertida",
                        e);
            }

        } catch (SQLException e) {

            // Error general de conexión o ejecución
            logger.error(
                    "Error durante la conexión a la base de datos o la ejecución SQL: {}",
                    e.getMessage(), e);

            throw e;
        }

        logger.info("Saliendo del método loadDataFromSQL");
    }
}