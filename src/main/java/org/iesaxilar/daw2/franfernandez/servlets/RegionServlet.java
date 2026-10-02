package org.iesaxilar.daw2.franfernandez.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.iesaxilar.daw2.franfernandez.daos.RegionDAO;
import org.iesaxilar.daw2.franfernandez.daos.RegionDAOImpl;
import org.iesaxilar.daw2.franfernandez.entities.Region;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * Servlet que maneja las operaciones CRUD para la entidad Region.
 * Utiliza RegionDAO para interactuar con la base de datos.
 */
@WebServlet("/regions")
public class RegionServlet extends HttpServlet {

    // DAO para gestionar las operaciones de las regiones en la base de datos
    private RegionDAO regionDAO;

    @Override
    public void init() throws ServletException {

        try {
            regionDAO = new RegionDAOImpl();
        } catch (Exception e) {
            throw new ServletException("Error al inicializar el RegionDAO", e);
        }
    }

    /**
     * Maneja las solicitudes GET al servlet.
     * Según el parámetro "action", decide qué método invocar.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        response.setContentType("text/html;charset=UTF-8");
        response.setCharacterEncoding("UTF-8");

        try {

            // Manejo para evitar el valor null
            if (action == null) {
                action = "list";
            }

            switch (action) {

                case "new":
                    showNewForm(request, response);
                    break;

                case "edit":
                    showEditForm(request, response);
                    break;

                default:
                    listRegions(request, response);
                    break;
            }

        } catch (SQLException ex) {
            throw new ServletException(ex);
        }
    }
    /**
     * Muestra el formulario para editar una región existente.
     *
     * @param request Solicitud HTTP.
     * @param response Respuesta HTTP.
     * @throws SQLException en caso de error en la consulta SQL.
     * @throws ServletException en caso de error en el servlet.
     * @throws IOException en caso de errores de E/S.
     */
    private void showEditForm(HttpServletRequest request,
                              HttpServletResponse response)
            throws SQLException, ServletException, IOException {

        int id = Integer.parseInt(request.getParameter("id"));

        Region existingRegion = regionDAO.getRegionById(id);

        request.setAttribute("region", existingRegion);

        // Redirigir a la vista para editar
        request.getRequestDispatcher("views/region/region-form.jsp")
                .forward(request, response);
    }

    /**
     * Muestra el formulario para crear una nueva Comunidad Autónoma.
     */
    private void showNewForm(HttpServletRequest request,
                             HttpServletResponse response)
            throws ServletException, IOException {

        request.getRequestDispatcher("views/region/region-form.jsp")
                .forward(request, response);
    }

    /**
     * Lista todas las regiones y las pasa como atributo a la vista.
     */
    private void listRegions(HttpServletRequest request,
                             HttpServletResponse response)
            throws SQLException, IOException, ServletException {

        List<Region> listRegions = regionDAO.listAllRegions();

        request.setAttribute("listRegions", listRegions);

        request.getRequestDispatcher("views/region/region-list.jsp")
                .forward(request, response);
    }

    /**
     * Maneja las solicitudes POST al servlet.
     * Según el parámetro "action", decide qué método invocar.
     */
    @Override
    protected void doPost(HttpServletRequest request,
                          HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        try {
            switch (action) {

                case "insert":
                    insertRegion(request, response);
                    break;

                case "update":
                    updateRegion(request, response);
                    break;

                case "delete":
                    deleteRegion(request, response);
                    break;

                default:
                    listRegions(request, response);
                    break;
            }

        } catch (SQLException ex) {
            throw new ServletException(ex);
        }
    }

    /**
     * Inserta una nueva región en la base de datos después de realizar
     * las validaciones necesarias.
     */
    private void insertRegion(HttpServletRequest request,
                              HttpServletResponse response)
            throws SQLException, IOException, ServletException {

        // Obtener los datos del formulario
        String code = request.getParameter("code").trim().toUpperCase();
        String name = request.getParameter("name").trim();

        // Validaciones básicas
        if (code.isEmpty() || name.isEmpty()) {

            request.setAttribute(
                    "errorMessage",
                    "El código y el nombre no pueden estar vacíos."
            );

            request.getRequestDispatcher(
                    "views/region/region-form.jsp"
            ).forward(request, response);

            return;
        }

        // Validar si el código ya existe
        if (regionDAO.existsRegionByCode(code)) {

            request.setAttribute(
                    "errorMessage",
                    "El código de la región ya existe."
            );

            request.getRequestDispatcher(
                    "views/region/region-form.jsp"
            ).forward(request, response);

            return;
        }

        // Crear la nueva región
        Region newRegion = new Region(code, name);

        try {

            regionDAO.insertRegion(newRegion);

        } catch (SQLException e) {

            // Código SQL para una restricción única
            if ("23505".equals(e.getSQLState())) {

                request.setAttribute(
                        "errorMessage",
                        "El código de la región debe ser único."
                );

                request.getRequestDispatcher(
                        "views/region/region-form.jsp"
                ).forward(request, response);

            } else {
                throw e;
            }
        }

        // Volver al listado
        response.sendRedirect("regions");
    }

    /**
     * Actualiza una región existente en la base de datos después de realizar
     * las validaciones necesarias.
     */
    private void updateRegion(HttpServletRequest request,
                              HttpServletResponse response)
            throws SQLException, IOException, ServletException {

        int id = Integer.parseInt(request.getParameter("id"));

        String code = request.getParameter("code").trim().toUpperCase();
        String name = request.getParameter("name").trim();

        // Validaciones básicas
        if (code.isEmpty() || name.isEmpty()) {

            request.setAttribute(
                    "errorMessage",
                    "El código y el nombre no pueden estar vacíos."
            );

            request.getRequestDispatcher(
                    "views/region/region-form.jsp"
            ).forward(request, response);

            return;
        }

        // Comprobar que el código no pertenece a otra región
        if (regionDAO.existsRegionByCodeAndNotId(code, id)) {

            request.setAttribute(
                    "errorMessage",
                    "El código de la región ya existe para otra región."
            );

            request.getRequestDispatcher(
                    "views/region/region-form.jsp"
            ).forward(request, response);

            return;
        }

        Region updatedRegion = new Region(id, code, name);

        try {

            regionDAO.updateRegion(updatedRegion);

        } catch (SQLException e) {

            if ("23505".equals(e.getSQLState())) {

                request.setAttribute(
                        "errorMessage",
                        "El código de la región debe ser único."
                );

                request.getRequestDispatcher(
                        "views/region/region-form.jsp"
                ).forward(request, response);

                return;
            } else {
                throw e;
            }
        }

        response.sendRedirect("regions");
    }
    /**
     * Elimina una región de la base de datos según su ID.
     *
     * @param request Solicitud HTTP.
     * @param response Respuesta HTTP.
     * @throws SQLException en caso de error en la consulta SQL.
     * @throws IOException en caso de error de E/S.
     */
    private void deleteRegion(HttpServletRequest request,
                              HttpServletResponse response)
            throws SQLException, IOException {

        int id = Integer.parseInt(request.getParameter("id"));

        regionDAO.deleteRegion(id);

        response.sendRedirect("regions");
    }
}