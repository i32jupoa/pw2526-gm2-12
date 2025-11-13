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
public class PatronRepository {

    private JdbcTemplate jdbcTemplate;
    private Properties sqlQueries;
    private String sqlQueriesFileName;

    public PatronRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void setSQLQueriesFileName(String sqlQueriesFileName) {
        this.sqlQueriesFileName = sqlQueriesFileName;
        createProperties();
    }

    // ===================== FIND ALL =====================
    public List<Patron> findAllPatrones() {
        try {
            String query = sqlQueries.getProperty("select-findAllPatrones");
            if (query != null) {
                return jdbcTemplate.query(query, new RowMapper<Patron>() {
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
                });
            } else {
                return null;
            }
        } catch (DataAccessException e) {
            System.err.println("Unable to retrieve patrones from the database");
            e.printStackTrace();
            return null;
        }
    }

    // ===================== FIND BY DNI =====================
    public Patron findPatronByDni(String dni) {
        try {
            String query = sqlQueries.getProperty("select-findPatronByDni");
            if (query != null) {
                return jdbcTemplate.queryForObject(query, new Object[]{dni}, new RowMapper<Patron>() {
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
                });
            } else {
                return null;
            }
        } catch (DataAccessException e) {
            System.err.println("Unable to find patron with DNI = " + dni);
            e.printStackTrace();
            return null;
        }
    }

    // ===================== INSERT =====================
    public boolean addPatron(Patron patron) {
        try {
            String query = sqlQueries.getProperty("insert-addPatron");
            if (query != null) {
                int result = jdbcTemplate.update(query,
                    patron.getDni(),
                    patron.getNombre(),
                    patron.getApellidos(),
                    Date.valueOf(patron.getFechaNacimiento()),
                    Date.valueOf(patron.getFechaExpedicionTitulo())
                );
                return result > 0;
            } else {
                return false;
            }
        } catch (DataAccessException e) {
            System.err.println("Unable to insert patron into the database");
            e.printStackTrace();
            return false;
        }
    }

    // ===================== UPDATE =====================
    public boolean updatePatron(Patron patron) {
        try {
            String query = sqlQueries.getProperty("update-patron");
            if (query != null) {
                int result = jdbcTemplate.update(query,
                    patron.getNombre(),
                    patron.getApellidos(),
                    Date.valueOf(patron.getFechaNacimiento()),
                    Date.valueOf(patron.getFechaExpedicionTitulo()),
                    patron.getDni()
                );
                return result > 0;
            } else {
                return false;
            }
        } catch (DataAccessException e) {
            System.err.println("Unable to update patron with DNI = " + patron.getDni());
            e.printStackTrace();
            return false;
        }
    }

    // ===================== DELETE =====================
    public boolean deletePatron(String dni) {
        try {
            String query = sqlQueries.getProperty("delete-patron");
            if (query != null) {
                int result = jdbcTemplate.update(query, dni);
                return result > 0;
            } else {
                return false;
            }
        } catch (DataAccessException e) {
            System.err.println("Unable to delete patron with DNI = " + dni);
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
            System.err.println("Error loading SQL properties for PatronRepository");
            e.printStackTrace();
        }
    }
}