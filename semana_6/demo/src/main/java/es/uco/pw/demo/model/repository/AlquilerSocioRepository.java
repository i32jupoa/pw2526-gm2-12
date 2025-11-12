package es.uco.pw.demo.model;

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
public class AlquilerSocioRepository {

    private JdbcTemplate jdbcTemplate;
    private Properties sqlQueries;
    private String sqlQueriesFileName;

    public AlquilerSocioRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void setSQLQueriesFileName(String sqlQueriesFileName) {
        this.sqlQueriesFileName = sqlQueriesFileName;
        createProperties();
    }


    public List<AlquilerSocio> findAllAlquilerSocio() {
        try {
            String query = sqlQueries.getProperty("select-findAllAlquilerSocio");
            if (query != null) {
                return jdbcTemplate.query(query, new RowMapper<AlquilerSocio>() {
                    @Override
                    public AlquilerSocio mapRow(ResultSet rs, int rowNum) throws SQLException {
                        return new AlquilerSocio(
                            rs.getInt("idAlquiler"),
                            rs.getString("dniSocio")
                        );
                    }
                });
            } else {
                return null;
            }
        } catch (DataAccessException e) {
            System.err.println("Unable to retrieve AlquilerSocio records from the database");
            e.printStackTrace();
            return null;
        }
    }

   
    public List<AlquilerSocio> findByIdAlquiler(int idAlquiler) {
        try {
            String query = sqlQueries.getProperty("select-findByIdAlquiler");
            if (query != null) {
                return jdbcTemplate.query(query, new RowMapper<AlquilerSocio>() {
                    @Override
                    public AlquilerSocio mapRow(ResultSet rs, int rowNum) throws SQLException {
                        return new AlquilerSocio(
                            rs.getInt("idAlquiler"),
                            rs.getString("dniSocio")
                        );
                    }
                }, idAlquiler);
            } else {
                return null;
            }
        } catch (DataAccessException e) {
            System.err.println("Unable to find AlquilerSocio for idAlquiler = " + idAlquiler);
            e.printStackTrace();
            return null;
        }
    }

 
    public List<AlquilerSocio> findByDniSocio(String dniSocio) {
        try {
            String query = sqlQueries.getProperty("select-findByDniSocio");
            if (query != null) {
                return jdbcTemplate.query(query, new RowMapper<AlquilerSocio>() {
                    @Override
                    public AlquilerSocio mapRow(ResultSet rs, int rowNum) throws SQLException {
                        return new AlquilerSocio(
                            rs.getInt("idAlquiler"),
                            rs.getString("dniSocio")
                        );
                    }
                }, dniSocio);
            } else {
                return null;
            }
        } catch (DataAccessException e) {
            System.err.println("Unable to find AlquilerSocio for dniSocio = " + dniSocio);
            e.printStackTrace();
            return null;
        }
    }

 
    public boolean addAlquilerSocio(AlquilerSocio alquilerSocio) {
        try {
            String query = sqlQueries.getProperty("insert-addAlquilerSocio");
            if (query != null) {
                int result = jdbcTemplate.update(query,
                    alquilerSocio.getIdAlquiler(),
                    alquilerSocio.getDniSocio()
                );
                return result > 0;
            } else {
                return false;
            }
        } catch (DataAccessException e) {
            System.err.println("Unable to insert AlquilerSocio record");
            e.printStackTrace();
            return false;
        }
    }

  
    public boolean deleteAlquilerSocio(int idAlquiler, String dniSocio) {
        try {
            String query = sqlQueries.getProperty("delete-alquilerSocio");
            if (query != null) {
                int result = jdbcTemplate.update(query, idAlquiler, dniSocio);
                return result > 0;
            } else {
                return false;
            }
        } catch (DataAccessException e) {
            System.err.println("Unable to delete AlquilerSocio with idAlquiler=" + idAlquiler + " and dniSocio=" + dniSocio);
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
            System.err.println("Error loading SQL properties for AlquilerSocioRepository");
            e.printStackTrace();
        }
    }
}
