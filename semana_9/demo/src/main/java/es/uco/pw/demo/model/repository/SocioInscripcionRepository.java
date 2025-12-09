package es.uco.pw.demo.model.repository;

import es.uco.pw.demo.model.domain.SocioInscripcion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.io.InputStream;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

@Repository
public class SocioInscripcionRepository {

    private final JdbcTemplate jdbcTemplate;
    private Properties sqlQueries;

    @Autowired
    public SocioInscripcionRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        setSQLQueriesFileName("db/sql.properties");
    }

    public final void setSQLQueriesFileName(String sqlQueriesFileName) {
        try {
            this.sqlQueries = loadProperties(sqlQueriesFileName);
        } catch (IOException e) {
            System.err.println("Error cargando SQL properties en SocioInscripcionRepository");
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

    public List<SocioInscripcion> findByIdInscripcion(int idInscripcion) {
        try {
            String sql = sqlQueries.getProperty("select-findSocioInscripcionByIdInscripcion");
            return jdbcTemplate.query(sql, new SocioInscripcionMapper(), idInscripcion);
        } catch (DataAccessException e) {
            System.err.println("Error en findByIdInscripcion: " + idInscripcion);
            e.printStackTrace();
            return null; // O List.of()
        }
    }

    public List<SocioInscripcion> findByDniSocio(String dni) {
        try {
            String sql = sqlQueries.getProperty("select-findSocioInscripcionByDni");
            return jdbcTemplate.query(sql, new SocioInscripcionMapper(), dni);
        } catch (DataAccessException e) {
            System.err.println("Error en findByDniSocio: " + dni);
            e.printStackTrace();
            return null;
        }
    }

    public boolean addSocioInscripcion(SocioInscripcion socioInscripcion) {
        try {
            String sql = sqlQueries.getProperty("insert-addSocioInscripcion");
            int rows = jdbcTemplate.update(sql,
                    socioInscripcion.getIdInscripcion(),
                    socioInscripcion.getDniSocio());
            return rows > 0;
        } catch (DataAccessException e) {
            System.err.println("Error en addSocioInscripcion");
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteSocioInscripcion(int idInscripcion, String dniSocio) {
        try {
            String sql = sqlQueries.getProperty("delete-deleteSocioInscripcion");
            int rows = jdbcTemplate.update(sql, idInscripcion, dniSocio);
            return rows > 0;
        } catch (DataAccessException e) {
            System.err.println("Error en deleteSocioInscripcion");
            e.printStackTrace();
            return false;
        }
    }

    public List<SocioInscripcion> findAllSocioInscripciones() {
        try {
            String sql = sqlQueries.getProperty("select-findAllSocioInscripciones");
            return jdbcTemplate.query(sql, new SocioInscripcionMapper());
        } catch (DataAccessException e) {
            System.err.println("Error en findAllSocioInscripciones");
            e.printStackTrace();
            return null;
        }
    }

    public int countByInscripcionId(int idInscripcion) {
        try {
            String sql = sqlQueries.getProperty("select-countByInscripcionId");
            // queryForObject(sql, tipoDeRetorno, argumentos...)
            Integer count = jdbcTemplate.queryForObject(sql, Integer.class, idInscripcion);
            return (count != null) ? count : 0;
        } catch (DataAccessException e) {
            System.err.println("Error al contar socios para idInscripcion=" + idInscripcion);
            e.printStackTrace();
            return 0;
        }
    }

    // Mapper reutilizable para SocioInscripcion
    private static final class SocioInscripcionMapper implements RowMapper<SocioInscripcion> {
        public SocioInscripcion mapRow(ResultSet rs, int rowNum) throws SQLException {
            SocioInscripcion si = new SocioInscripcion();
            si.setId(rs.getInt("id"));
            si.setIdInscripcion(rs.getInt("idInscripcion"));
            si.setDniSocio(rs.getString("dniSocio"));
            return si;
        }
    }
}