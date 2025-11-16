package es.uco.pw.demo.model.repository;

import es.uco.pw.demo.model.domain.Patron;
import org.springframework.beans.factory.annotation.Autowired;
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
public class PatronRepository {

    private final JdbcTemplate jdbcTemplate;
    private Properties sqlQueries;

    @Autowired
    public PatronRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        setSQLQueriesFileName("db/sql.properties");
    }

    public final void setSQLQueriesFileName(String sqlQueriesFileName) {
        try {
            this.sqlQueries = loadProperties(sqlQueriesFileName);
        } catch (IOException e) {
            System.err.println("Error cargando SQL properties en PatronRepository");
            e.printStackTrace();
        }
    }

    private Properties loadProperties(String fileName) throws IOException {
        Properties properties = new Properties();
        InputStream input = getClass().getClassLoader().getResourceAsStream(fileName);
        if (input == null) {
            throw new IOException("No se encontró el archivo: " + fileName);
        }
        properties.load(input);
        return properties;
    }

    // Mapper reutilizable
    private static final class PatronMapper implements RowMapper<Patron> {
        @Override
        public Patron mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Patron(
                rs.getString("dni"),
                rs.getString("nombre"),
                rs.getString("apellidos"),
                rs.getDate("fechaNacimiento").toLocalDate(),
                rs.getDate("fechaExpedicionTitulo").toLocalDate()
            );
        }
    }

    public List<Patron> findAllPatrones() {
        try {
            String query = sqlQueries.getProperty("select-findAllPatrones");
            return jdbcTemplate.query(query, new PatronMapper());
        } catch (DataAccessException e) {
            System.err.println("Unable to retrieve patrones from the database");
            e.printStackTrace();
            return null;
        }
    }

    public Patron findPatronByDni(String dni) {
        try {
            String query = sqlQueries.getProperty("select-findPatronByDni");
            List<Patron> results = jdbcTemplate.query(query, new PatronMapper(), dni);
            return results.isEmpty() ? null : results.get(0);
        } catch (DataAccessException e) {
            System.err.println("Unable to find patron with DNI = " + dni);
            e.printStackTrace();
            return null;
        }
    }

    public boolean addPatron(Patron patron) {
        try {
            String query = sqlQueries.getProperty("insert-addPatron");
            int result = jdbcTemplate.update(query,
                patron.getDni(),
                patron.getNombre(),
                patron.getApellidos(),
                Date.valueOf(patron.getFechaNacimiento()),
                Date.valueOf(patron.getFechaExpedicionTitulo())
            );
            return result > 0;
        } catch (DataAccessException e) {
            System.err.println("Unable to insert patron into the database");
            e.printStackTrace();
            return false;
        }
    }

    public boolean updatePatron(Patron patron) {
        try {
            String query = sqlQueries.getProperty("update-updatePatron");
            int result = jdbcTemplate.update(query,
                patron.getNombre(),
                patron.getApellidos(),
                Date.valueOf(patron.getFechaNacimiento()),
                Date.valueOf(patron.getFechaExpedicionTitulo()),
                patron.getDni()
            );
            return result > 0;
        } catch (DataAccessException e) {
            System.err.println("Unable to update patron with DNI = " + patron.getDni());
            e.printStackTrace();
            return false;
        }
    }

    public boolean deletePatron(String dni) {
        try {
            String query = sqlQueries.getProperty("delete-deletePatronByDni"); // Corregido nombre de query
            int result = jdbcTemplate.update(query, dni);
            return result > 0;
        } catch (DataAccessException e) {
            System.err.println("Unable to delete patron with DNI = " + dni);
            e.printStackTrace();
            return false;
        }
    }
}