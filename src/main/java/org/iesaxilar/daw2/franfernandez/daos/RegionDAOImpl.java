package org.iesaxilar.daw2.franfernandez.daos;

import org.iesaxilar.daw2.franfernandez.entities.Region;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class RegionDAOImpl implements RegionDAO {

    @Override
    public List<Region> listAllRegions() throws SQLException {

        List<Region> regions = new ArrayList<>();

        String query = "SELECT * FROM regions";

        try (Connection connection = DatabaseConnectionManager.getConnection();
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            while (resultSet.next()) {

                int id = resultSet.getInt("id");
                String code = resultSet.getString("code");
                String name = resultSet.getString("name");

                regions.add(new Region(id, code, name));
            }
        }

        return regions;
    }

    @Override
    public void insertRegion(Region region) throws SQLException {

        String query = "INSERT INTO regions (code, name) VALUES (?, ?)";

        try (Connection connection = DatabaseConnectionManager.getConnection();
             PreparedStatement preparedStatement =
                     connection.prepareStatement(query)) {

            preparedStatement.setString(1, region.getCode());
            preparedStatement.setString(2, region.getName());

            preparedStatement.executeUpdate();
        }
    }

    @Override
    public boolean existsRegionByCode(String code) throws SQLException {

        String sql = "SELECT COUNT(*) FROM regions WHERE UPPER(code) = ?";

        try (Connection connection = DatabaseConnectionManager.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, code.toUpperCase());

            try (ResultSet resultSet = statement.executeQuery()) {

                resultSet.next();

                return resultSet.getInt(1) > 0;
            }
        }
    }

    /**
     * Actualiza una región existente en la base de datos.
     */
    @Override
    public void updateRegion(Region region) throws SQLException {

        String query = "UPDATE regions SET code = ?, name = ? WHERE id = ?";

        try (Connection connection = DatabaseConnectionManager.getConnection();
             PreparedStatement preparedStatement =
                     connection.prepareStatement(query)) {

            preparedStatement.setString(1, region.getCode());
            preparedStatement.setString(2, region.getName());
            preparedStatement.setInt(3, region.getId());

            preparedStatement.executeUpdate();
        }
    }

    /**
     * Obtiene una región mediante su identificador.
     */
    @Override
    public Region getRegionById(int id) throws SQLException {

        String query = "SELECT * FROM regions WHERE id = ?";
        Region region = null;

        try (Connection connection = DatabaseConnectionManager.getConnection();
             PreparedStatement preparedStatement =
                     connection.prepareStatement(query)) {

            preparedStatement.setInt(1, id);

            try (ResultSet resultSet = preparedStatement.executeQuery()) {

                if (resultSet.next()) {

                    String code = resultSet.getString("code");
                    String name = resultSet.getString("name");

                    region = new Region(id, code, name);
                }
            }
        }

        return region;
    }

    /**
     * Comprueba si existe otra región con el mismo código,
     * excluyendo la región cuyo ID se indica.
     */
    @Override
    public boolean existsRegionByCodeAndNotId(String code, int id)
            throws SQLException {

        String sql =
                "SELECT COUNT(*) FROM regions WHERE UPPER(code) = ? AND id != ?";

        try (Connection connection = DatabaseConnectionManager.getConnection();
             PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, code.toUpperCase());
            statement.setInt(2, id);

            try (ResultSet resultSet = statement.executeQuery()) {

                resultSet.next();

                return resultSet.getInt(1) > 0;
            }
        }
    }
    /**
     * Elimina una región de la base de datos mediante su identificador.
     *
     * @param id Identificador de la región que se desea eliminar.
     * @throws SQLException si ocurre un error durante la eliminación.
     */
    @Override
    public void deleteRegion(int id) throws SQLException {

        String query = "DELETE FROM regions WHERE id = ?";

        try (Connection connection = DatabaseConnectionManager.getConnection();
             PreparedStatement preparedStatement =
                     connection.prepareStatement(query)) {

            preparedStatement.setInt(1, id);

            preparedStatement.executeUpdate();
        }
    }
}