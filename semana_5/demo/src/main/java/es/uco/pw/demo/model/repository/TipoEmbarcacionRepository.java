package es.uco.pw.demo.model.repository;

import es.uco.pw.demo.model.domain.TipoEmbarcacion;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

@Repository
public class TipoEmbarcacionRepository {

    private JdbcTemplate jdbcTemplate;
    private Properties sqlQueries;
    private String sqlQueriesFileName;

    public TipoEmbarcacionRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void setSQLQueriesFileName(String sqlQueriesFileName) {
        this.sqlQueriesFileName = sqlQueriesFileName;
        createProperties();
    }

    // ===================== FIND ALL =====================
    public List<TipoEmbarcacion> findAllTiposEmbarcacion() {
        try {
            String query = sqlQueries.getProperty("select-findAllTiposEmbarcacion");
            if (query != null) {
                return jdbcTemplate.query(query, new RowMapper<TipoEmbarcacion>() {
                    @Override
                    public TipoEmbarcacion mapRow(ResultSet rs, int rowNum) throws SQLException {
                        String tipoStr = rs.getString("tipo");
                        return TipoEmbarcacion.valueOf(tipoStr);
                    }
                });
            } else {
                return null;
            }
        } catch (DataAccessException e) {
            System.err.println("Unable to find tipos de embarcación");
            e.printStackTrace();
            return null;
        }
    }

    // ===================== FIND BY NOMBRE =====================
    public TipoEmbarcacion findTipoEmbarcacionByNombre(String tipoNombre) {
        try {
            String query = sqlQueries.getProperty("select-findTipoEmbarcacionByNombre");
            if (query != null) {
                return jdbcTemplate.queryForObject(query, new Object[]{tipoNombre}, new RowMapper<TipoEmbarcacion>() {
                    @Override
                    public TipoEmbarcacion mapRow(ResultSet rs, int rowNum) throws SQLException {
                        return TipoEmbarcacion.valueOf(rs.getString("tipo"));
                    }
                });
            } else {
                return null;
            }
        } catch (DataAccessException e) {
            System.err.println("Unable to find tipo de embarcación: " + tipoNombre);
            e.printStackTrace();
            return null;
        }
    }

    // ===================== ADD =====================
    public boolean addTipoEmbarcacion(TipoEmbarcacion tipo) {
        try {
            String query = sqlQueries.getProperty("insert-addTipoEmbarcacion");
            if (query != null) {
                int result = jdbcTemplate.update(query, tipo.name());
                return result > 0;
            } else {
                return false;
            }
        } catch (DataAccessException e) {
            System.err.println("Unable to insert tipo de embarcación: " + tipo);
            e.printStackTrace();
            return false;
        }
    }

    // ===================== DELETE =====================
    public boolean deleteTipoEmbarcacion(String tipoNombre) {
        try {
            String query = sqlQueries.getProperty("delete-tipoEmbarcacion");
            if (query != null) {
                int result = jdbcTemplate.update(query, tipoNombre);
                return result > 0;
            } else {
                return false;
            }
        } catch (DataAccessException e) {
            System.err.println("Unable to delete tipo de embarcación: " + tipoNombre);
            e.printStackTrace();
            return false;
        }
    }

    // ===================== UTILIDAD =====================
    private void createProperties() {
        sqlQueries = new Properties();
        try {
            BufferedReader reader;
            File f = new File(sqlQueriesFileName);
            reader = new BufferedReader(new FileReader(f));
            sqlQueries.load(reader);
        } catch (IOException e) {
            System.err.println("Error creating properties object for SQL queries");
            e.printStackTrace();
        }
    }
}
