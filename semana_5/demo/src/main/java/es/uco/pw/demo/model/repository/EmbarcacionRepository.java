package es.uco.pw.demo.model.repository;

import es.uco.pw.demo.model.domain.TipoEmbarcacion;
import es.uco.pw.demo.model.domain.Embarcacion;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

@Repository
public class EmbarcacionRepository {

    private final JdbcTemplate jdbcTemplate;
    private Properties sqlQueries;
    private String sqlQueriesFileName;

    public EmbarcacionRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void setSQLQueriesFileName(String sqlQueriesFileName) {
        this.sqlQueriesFileName = sqlQueriesFileName;
        createProperties();
    }

    public List<Embarcacion> findAllEmbarcaciones() {
        try {
            String query = sqlQueries.getProperty("select-findAllEmbarcaciones");
            if (query != null) {
                return jdbcTemplate.query(query, embarcacionMapper);
            } else {
                return null;
            }
        } catch (DataAccessException e) {
            System.err.println("Unable to find embarcaciones");
            e.printStackTrace();
            return null;
        }
    }

    public Embarcacion findEmbarcacionByMatricula(String matricula) {
        try {
            String query = sqlQueries.getProperty("select-findEmbarcacionByMatricula");
            if (query != null) {
                return jdbcTemplate.queryForObject(query, new Object[]{matricula}, embarcacionMapper);
            } else {
                return null;
            }
        } catch (DataAccessException e) {
            System.err.println("Unable to find embarcacion with matricula=" + matricula);
            e.printStackTrace();
            return null;
        }
    }

    public List<Embarcacion> findEmbarcacionByTipo(TipoEmbarcacion tipo) {
        try {
            String query = sqlQueries.getProperty("select-findEmbarcacionByTipo"); // clave consistente
            if (query != null) {
                return jdbcTemplate.query(query, new Object[]{tipo.name()}, embarcacionMapper);
            } else {
                return null;
            }
        } catch (DataAccessException e) {
            System.err.println("Unable to find embarcaciones of tipo=" + tipo);
            e.printStackTrace();
            return null;
        }
    }

    public boolean addEmbarcacion(Embarcacion embarcacion) {
        try {
            String query = sqlQueries.getProperty("insert-addEmbarcacion");
            if (query != null) {
                int result = jdbcTemplate.update(query,
                        embarcacion.getMatricula(),
                        embarcacion.getNombre(),
                        embarcacion.getTipo().name(),
                        embarcacion.getNumPlazas(),
                        embarcacion.getDimensiones(),
                        embarcacion.getDniPatron()
                );
                return result > 0;
            } else {
                return false;
            }
        } catch (DataAccessException e) {
            System.err.println("Unable to insert embarcacion into the database");
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteEmbarcacionByMatricula(String matricula) {
        try {
            String query = sqlQueries.getProperty("delete-deleteEmbarcacionByMatricula");
            if (query != null) {
                int result = jdbcTemplate.update(query, matricula);
                return result > 0;
            } else {
                return false;
            }
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

    private void createProperties() {
        sqlQueries = new Properties();
        try {
            File f = new File(sqlQueriesFileName);
            BufferedReader reader = new BufferedReader(new FileReader(f));
            sqlQueries.load(reader);
        } catch (IOException e) {
            System.err.println("Error creating properties object for SQL queries");
            e.printStackTrace();
        }
    }
}
