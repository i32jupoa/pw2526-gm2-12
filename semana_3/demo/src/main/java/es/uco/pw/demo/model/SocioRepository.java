package main.java.es.uco.pw.demo.model;

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
public class SocioRepository {
    
    private JdbcTemplate jdbcTemplate;
    private Properties sqlQueries;
    private String sqlQueriesFileName;

    public SocioRepository(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    public void setSQLQueriesFileName(String sqlQueriesFileName){
        this.sqlQueriesFileName = sqlQueriesFileName;
        createProperties();
    }

    public List<Socio> findAllSocios(){
        try{
            String query = sqlQueries.getProperty("select-findAllSocios");
            if(query != null){
                List<Socio> result = jdbcTemplate.query(query, new RowMapper<Socio>(){
                    public Socio mapRow(ResultSet rs, int rowNumber) throws SQLException{
                        return new Socio(
                            rs.getString("dni"),
                            rs.getString("nombre"),
                            rs.getString("apellidos"),
                            Date.valueOf(rs.getString("fechaNacimiento")).toLocalDate(),
                            rs.getString("direccion"),
                            Date.valueOf(rs.getString("fechaInscripcion")).toLocalDate(),
                            rs.getBoolean("esTitular"),
                            rs.getBoolean("tieneTituloPatron"));
                    };
                });
                return result;
            } else
                return null;
        } catch(DataAccessException exception){
            System.err.println("Unable to find socios");
            exception.printStackTrace();
            return null;
        }
    }

    public Socio findSocioByDni(String dni){
        try{
            String query = sqlQueries.getProperty("select-findSocioByDni");
            List<Socio> result = jdbcTemplate.query(query, new RowMapper<Socio>(){
                public Socio mapRow(ResultSet rs, int rowNumber) throws SQLException{
                    return new Socio(
                        rs.getString("dni"),
                        rs.getString("nombre"),
                        rs.getString("apellidos"),
                        Date.valueOf(rs.getString("fechaNacimiento")).toLocalDate(),
                        rs.getString("direccion"),
                        Date.valueOf(rs.getString("fechaInscripcion")).toLocalDate(),
                        rs.getBoolean("esTitular"),
                        rs.getBoolean("tieneTituloPatron"));
                };
            }, dni);
            return result.isEmpty() ? null : result.get(0);
        } catch(DataAccessException exception){
            System.err.println("Unable to find socio with dni=" + dni);
            exception.printStackTrace();
            return null;
        }
    }

    public boolean addSocio(Socio socio){
        try{
            String query = sqlQueries.getProperty("insert-addSocio");
            if(query != null){
                int result = jdbcTemplate.update(query,
                    socio.getDni(),
                    socio.getNombre(),
                    socio.getApellidos(),
                    socio.getFechaNacimiento().toString(),
                    socio.getDireccion(),
                    socio.getFechaInscripcion().toString(),
                    socio.isEsTitular(),
                    socio.isTieneTituloPatron());
                return result > 0;
            } else
                return false;
        } catch(DataAccessException exception){
            System.err.println("Unable to insert socio in the database");
            exception.printStackTrace();
            return false;
        }
    }

    public boolean deleteSocio(String dni){
        try{
            String query = sqlQueries.getProperty("delete-deleteSocio");
            if(query != null){
                int result = jdbcTemplate.update(query, dni);
                return result > 0;
            } else
                return false;
        } catch(DataAccessException exception){
            System.err.println("Unable to delete socio with dni=" + dni);
            exception.printStackTrace();
            return false;
        }
    }

    private void createProperties(){
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
