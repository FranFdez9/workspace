package org.iesaxilar.daw2.franfernandez.daos;

import org.iesaxilar.daw2.franfernandez.entities.Region;

import java.sql.SQLException;
import java.util.List;

public interface RegionDAO {

    List<Region> listAllRegions() throws SQLException;

    void insertRegion(Region region) throws SQLException;

    boolean existsRegionByCode(String code) throws SQLException;

    void updateRegion(Region region) throws SQLException;

    Region getRegionById(int id) throws SQLException;

    boolean existsRegionByCodeAndNotId(String code, int id) throws SQLException;

    void deleteRegion(int id) throws SQLException;

}