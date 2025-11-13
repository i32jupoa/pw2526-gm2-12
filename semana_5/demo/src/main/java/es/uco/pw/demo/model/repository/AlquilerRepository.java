package es.uco.pw.demo.model.repository;

import es.uco.pw.demo.model.domain.Alquiler;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

@Repository
public class AlquilerRepository {

    private final JdbcTemplate jdbcTemplate;
    private final Properties sqlQueries = new Properties();

    public AlquilerRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        loadSQLQueries();
    }

    /**
     * Carga todas las queries desde sql.properties en resources
     */
    private void loadSQLQueries() {
        try (InputStream is = getClass().getClassLoader().getResourceAsStream("db/sql.properties")) {
            if (is == null) {
                throw new IOException("No se encontró el archivo sql.properties en resources.");
            }
            sqlQueries.load(is);
        } catch (IOException e) {
            System.err.println("Error cargando archivo sql.properties");
            e.printStackTrace();
        }
    }

    public List<Alquiler> findAllAlquileres() {
        try {
            String query = sqlQueries.getProperty("select-findAllAlquileres");
            return jdbcTemplate.query(query, new AlquilerRowMapper());
        } catch (DataAccessException e) {
            System.err.println("Unable to find alquileres");
            e.printStackTrace();
            return null;
        }
    }

    public Alquiler findAlquilerById(int id) {
        try {
            String query = sqlQueries.getProperty("select-findAlquilerById");
            return jdbcTemplate.queryForObject(query, new Object[]{id}, new AlquilerRowMapper());
        } catch (DataAccessException e) {
            System.err.println("Unable to find alquiler with id=" + id);
            e.printStackTrace();
            return null;
        }
    }

    public List<Alquiler> findAlquileresByMatricula(String matricula) {
        try {
            String query = sqlQueries.getProperty("select-findAlquileresByMatricula");
            return jdbcTemplate.query(query, new AlquilerRowMapper(), matricula);
        } catch (DataAccessException e) {
            System.err.println("Unable to find alquileres with matricula=" + matricula);
            e.printStackTrace();
            return null;
        }
    }

    public boolean addAlquiler(Alquiler alquiler) {
        try {
            String query = sqlQueries.getProperty("insert-addAlquiler");
            int result = jdbcTemplate.update(query,
                    alquiler.getId(),
                    alquiler.getIdInscripcion(),
                    alquiler.getMatricula(),
                    Date.valueOf(alquiler.getFechaInicio()),
                    Date.valueOf(alquiler.getFechaFin()),
                    alquiler.getPlazasReservadas(),
                    alquiler.getPrecioTotal());
            return result > 0;
        } catch (DataAccessException e) {
            System.err.println("Unable to insert alquiler into the database");
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteAlquilerById(int id) {
        try {
            String query = sqlQueries.getProperty("delete-alquilerById");
            int result = jdbcTemplate.update(query, id);
            return result > 0;
        } catch (DataAccessException e) {
            System.err.println("Unable to delete alquiler with id=" + id);
            e.printStackTrace();
            return false;
        }
    }

    private static class AlquilerRowMapper implements RowMapper<Alquiler> {
        @Override
        public Alquiler mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Alquiler(
                    rs.getInt("id"),
                    rs.getInt("idInscripcion"),
                    rs.getString("matricula"),
                    rs.getDate("fechaInicio").toLocalDate(),
                    rs.getDate("fechaFin").toLocalDate(),
                    rs.getInt("plazasReservadas"),
                    rs.getDouble("precioTotal")
            );
        }
    }
}
