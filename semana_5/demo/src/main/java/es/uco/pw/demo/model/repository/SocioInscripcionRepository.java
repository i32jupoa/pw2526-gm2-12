package es.uco.pw.demo.model.repository;

import es.uco.pw.demo.model.domain.SocioInscripcion;
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
public class SocioInscripcionRepository {

    private JdbcTemplate jdbcTemplate;
    private Properties sqlQueries;
    private String sqlQueriesFileName;

    public SocioInscripcionRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void setSQLQueriesFileName(String sqlQueriesFileName) {
        this.sqlQueriesFileName = sqlQueriesFileName;
        createProperties();
    }

    public List<SocioInscripcion> findAllSocioInscripciones() {
        try {
            String query = sqlQueries.getProperty("select-findAllSocioInscripciones");
            if (query != null) {
                return jdbcTemplate.query(query, new RowMapper<SocioInscripcion>() {
                    @Override
                    public SocioInscripcion mapRow(ResultSet rs, int rowNum) throws SQLException {
                        return new SocioInscripcion(
                            rs.getInt("id"),
                            rs.getInt("idInscripcion"),
                            rs.getString("dniSocio")
                        );
                    }
                });
            } else {
                return null;
            }
        } catch (DataAccessException e) {
            System.err.println("Unable to retrieve SocioInscripcion records from the database");
            e.printStackTrace();
            return null;
        }
    }


    public SocioInscripcion findById(int id) {
        try {
            String query = sqlQueries.getProperty("select-findByIdSocioInscripcion");
            if (query != null) {
                return jdbcTemplate.queryForObject(query, new RowMapper<SocioInscripcion>() {
                    @Override
                    public SocioInscripcion mapRow(ResultSet rs, int rowNum) throws SQLException {
                        return new SocioInscripcion(
                            rs.getInt("id"),
                            rs.getInt("idInscripcion"),
                            rs.getString("dniSocio")
                        );
                    }
                }, id);
            } else {
                return null;
            }
        } catch (DataAccessException e) {
            System.err.println("Unable to find SocioInscripcion with id=" + id);
            e.printStackTrace();
            return null;
        }
    }


    public List<SocioInscripcion> findByIdInscripcion(int idInscripcion) {
        try {
            String query = sqlQueries.getProperty("select-findByIdInscripcion");
            if (query != null) {
                return jdbcTemplate.query(query, new RowMapper<SocioInscripcion>() {
                    @Override
                    public SocioInscripcion mapRow(ResultSet rs, int rowNum) throws SQLException {
                        return new SocioInscripcion(
                            rs.getInt("id"),
                            rs.getInt("idInscripcion"),
                            rs.getString("dniSocio")
                        );
                    }
                }, idInscripcion);
            } else {
                return null;
            }
        } catch (DataAccessException e) {
            System.err.println("Unable to find SocioInscripcion with idInscripcion=" + idInscripcion);
            e.printStackTrace();
            return null;
        }
    }


    public boolean addSocioInscripcion(SocioInscripcion socioInscripcion) {
        try {
            String query = sqlQueries.getProperty("insert-addSocioInscripcion");
            if (query != null) {
                int result = jdbcTemplate.update(query,
                    socioInscripcion.getId(),
                    socioInscripcion.getIdInscripcion(),
                    socioInscripcion.getDniSocio()
                );
                return result > 0;
            } else {
                return false;
            }
        } catch (DataAccessException e) {
            System.err.println("Unable to insert SocioInscripcion record");
            e.printStackTrace();
            return false;
        }
    }


    public boolean deleteSocioInscripcion(int id) {
        try {
            String query = sqlQueries.getProperty("delete-socioInscripcion");
            if (query != null) {
                int result = jdbcTemplate.update(query, id);
                return result > 0;
            } else {
                return false;
            }
        } catch (DataAccessException e) {
            System.err.println("Unable to delete SocioInscripcion with id=" + id);
            e.printStackTrace();
            return false;
        }
    }


    private void createProperties() {
        sqlQueries = new Properties();
        try {
            BufferedReader reader;
            File f = new File(sqlQueriesFileName);
            reader = new BufferedReader(new FileReader(f));
            sqlQueries.load(reader);
        } catch (IOException e) {
            System.err.println("Error loading SQL properties for SocioInscripcionRepository");
            e.printStackTrace();
        }
    }
}
