package es.uco.pw.demo.model.repository;

import es.uco.pw.demo.model.domain.TipoEmbarcacion;
import es.uco.pw.demo.model.domain.Embarcacion;
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
import java.sql.Date; 
import java.time.LocalDate; 

@Repository
public class EmbarcacionRepository {

    private final JdbcTemplate jdbcTemplate;
    private Properties sqlQueries;

    @Autowired
    public EmbarcacionRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        setSQLQueriesFileName("db/sql.properties");
    }

    public final void setSQLQueriesFileName(String sqlQueriesFileName) {
        try {
            this.sqlQueries = loadProperties(sqlQueriesFileName);
        } catch (IOException e) {
            System.err.println("Error cargando SQL properties en EmbarcacionRepository");
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

    public List<Embarcacion> findAllEmbarcaciones() {
        try {
            String query = sqlQueries.getProperty("select-findAllEmbarcaciones");
            return jdbcTemplate.query(query, embarcacionMapper);
        } catch (DataAccessException e) {
            System.err.println("Unable to find embarcaciones");
            e.printStackTrace();
            return null;
        }
    }

    public Embarcacion findEmbarcacionByMatricula(String matricula) {
        try {
            String query = sqlQueries.getProperty("select-findEmbarcacionByMatricula");
            List<Embarcacion> results = jdbcTemplate.query(query, embarcacionMapper, matricula);
            return results.isEmpty() ? null : results.get(0);
        } catch (DataAccessException e) {
            System.err.println("Unable to find embarcacion with matricula=" + matricula);
            e.printStackTrace();
            return null;
        }
    }

    public List<Embarcacion> findEmbarcacionByTipo(TipoEmbarcacion tipo) {
        try {
            String query = sqlQueries.getProperty("select-findEmbarcacionByTipo"); 
            return jdbcTemplate.query(query, embarcacionMapper, tipo.name());
        } catch (DataAccessException e) {
            System.err.println("Unable to find embarcaciones of tipo=" + tipo);
            e.printStackTrace();
            return null;
        }
    }

    public Embarcacion findEmbarcacionByNombre(String nombre) {
        try {
            String query = sqlQueries.getProperty("select-findEmbarcacionByNombre");
            List<Embarcacion> results = jdbcTemplate.query(query, embarcacionMapper, nombre);
            return results.isEmpty() ? null : results.get(0);
        } catch (DataAccessException e) {
            System.err.println("Unable to find embarcacion with nombre=" + nombre);
            e.printStackTrace();
            return null;
        }
    }

    public List<Embarcacion> findEmbarcacionesDisponibles(LocalDate fechaInicio, LocalDate fechaFin) {
        try {
            String query = sqlQueries.getProperty("select-findEmbarcacionesDisponibles");
            
            return jdbcTemplate.query(query, embarcacionMapper,
                Date.valueOf(fechaFin), Date.valueOf(fechaInicio),
                Date.valueOf(fechaInicio), Date.valueOf(fechaFin),
                Date.valueOf(fechaInicio), Date.valueOf(fechaFin),
                Date.valueOf(fechaInicio), Date.valueOf(fechaFin)
            );
            
        } catch (DataAccessException e) {
            System.err.println("Error al buscar embarcaciones disponibles");
            e.printStackTrace();
            return null;
        }
    }

    public boolean addEmbarcacion(Embarcacion embarcacion) {
        try {
            String query = sqlQueries.getProperty("insert-addEmbarcacion");
            int result = jdbcTemplate.update(query,
                    embarcacion.getMatricula(),
                    embarcacion.getNombre(),
                    embarcacion.getTipo().name(),
                    embarcacion.getNumPlazas(),
                    embarcacion.getDimensiones(),
                    embarcacion.getDniPatron()
            );
            return result > 0;
        } catch (DataAccessException e) {
            System.err.println("Unable to insert embarcacion into the database");
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteEmbarcacionByMatricula(String matricula) {
        try {
            String query = sqlQueries.getProperty("delete-deleteEmbarcacionByMatricula");
            int result = jdbcTemplate.update(query, matricula);
            return result > 0;
        } catch (DataAccessException e) {
            System.err.println("Unable to delete embarcacion with matricula=" + matricula);
            e.printStackTrace();
            return false;
        }
    }

    private final RowMapper<Embarcacion> embarcacionMapper = new RowMapper<Embarcacion>() {
        @Override
        public Embarcacion mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Embarcacion(
                    rs.getString("matricula"),
                    rs.getString("nombre"),
                    TipoEmbarcacion.valueOf(rs.getString("tipo")),
                    rs.getInt("numPlazas"),
                    rs.getString("dimensiones"),
                    rs.getString("dniPatron")
            );
        }
    };
}