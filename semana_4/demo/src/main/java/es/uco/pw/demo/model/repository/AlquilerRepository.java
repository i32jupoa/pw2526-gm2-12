package es.uco.pw.demo.model;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

@Repository
public class AlquilerRepository {

    private JdbcTemplate jdbcTemplate;
    private Properties sqlQueries;
    private String sqlQueriesFileName;

    public AlquilerRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void setSQLQueriesFileName(String sqlQueriesFileName) {
        this.sqlQueriesFileName = sqlQueriesFileName;
        createProperties();
    }

    public List<Alquiler> findAllAlquileres() {
        try {
            String query = sqlQueries.getProperty("select-findAllAlquileres");
            if (query != null) {
                return jdbcTemplate.query(query, new RowMapper<Alquiler>() {
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
                });
            } else {
                return null;
            }
        } catch (DataAccessException e) {
            System.err.println("Unable to find alquileres");
            e.printStackTrace();
            return null;
        }
    }

    public Alquiler findAlquilerById(int id) {
        try {
            String query = sqlQueries.getProperty("select-findAlquilerById");
            if (query != null) {
                return jdbcTemplate.queryForObject(query, new Object[]{id}, new RowMapper<Alquiler>() {
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
                });
            } else {
                return null;
            }
        } catch (DataAccessException e) {
            System.err.println("Unable to find alquiler with id=" + id);
            e.printStackTrace();
            return null;
        }
    }

    public List<Alquiler> findAlquileresByMatricula(String matricula) {
        try {
            String query = sqlQueries.getProperty("select-findAlquileresByMatricula");
            if (query != null) {
                return jdbcTemplate.query(query, new RowMapper<Alquiler>() {
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
                }, matricula);
            } else {
                return null;
            }
        } catch (DataAccessException e) {
            System.err.println("Unable to find alquileres with matricula=" + matricula);
            e.printStackTrace();
            return null;
        }
    }

    public boolean addAlquiler(Alquiler alquiler) {
        try {
            String query = sqlQueries.getProperty("insert-addAlquiler");
            if (query != null) {
                int result = jdbcTemplate.update(query,
                        alquiler.getId(),
                        alquiler.getIdInscripcion(),
                        alquiler.getMatricula(),
                        Date.valueOf(alquiler.getFechaInicio()),
                        Date.valueOf(alquiler.getFechaFin()),
                        alquiler.getPlazasReservadas(),
                        alquiler.getPrecioTotal());
                return result > 0;
            } else {
                return false;
            }
        } catch (DataAccessException e) {
            System.err.println("Unable to insert alquiler into the database");
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteAlquilerById(int id) {
        try {
            String query = sqlQueries.getProperty("delete-alquilerById");
            if (query != null) {
                int result = jdbcTemplate.update(query, id);
                return result > 0;
            } else {
                return false;
            }
        } catch (DataAccessException e) {
            System.err.println("Unable to delete alquiler with id=" + id);
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
            System.err.println("Error creating properties object for SQL queries");
            e.printStackTrace();
        }
    }
}
