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
public class InscripcionRepository {
    
    private JdbcTemplate jdbcTemplate;
    private Properties sqlQueries;
    private String sqlQueriesFileName;

    public InscripcionRepository(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

    public void setSQLQueriesFileName(String sqlQueriesFileName){
        this.sqlQueriesFileName = sqlQueriesFileName;
        createProperties();
    }

    public List<Inscripcion> findAllInscripciones(){
        try{
            String query = sqlQueries.getProperty("select-findAllInscripciones");
            if(query != null){
                List<Inscripcion> result = jdbcTemplate.query(query, new RowMapper<Inscripcion>(){
                    public Inscripcion mapRow(ResultSet rs, int rowNumber) throws SQLException{
                        return new Inscripcion(
                            rs.getInt("id"),
                            Date.valueOf(rs.getString("fechaCreacion")).toLocalDate(),
                            TipoInscripcion.valueOf(rs.getString("tipo")),
                            rs.getDouble("cuotaActual"),
                            rs.getString("dniTitular"));
                    };
                });
                return result;
            } else
                return null;
        } catch(DataAccessException exception){
            System.err.println("Unable to find inscripciones");
            exception.printStackTrace();
            return null;
        }
    }

    public Inscripcion findInscripcionById(int id){
        try{
            String query = sqlQueries.getProperty("select-findInscripcionById");
            List<Inscripcion> result = jdbcTemplate.query(query, new RowMapper<Inscripcion>(){
                public Inscripcion mapRow(ResultSet rs, int rowNumber) throws SQLException{
                    return new Inscripcion(
                        rs.getInt("id"),
                        Date.valueOf(rs.getString("fechaCreacion")).toLocalDate(),
                        TipoInscripcion.valueOf(rs.getString("tipo")),
                        rs.getDouble("cuotaActual"),
                        rs.getString("dniTitular"));
                };
            }, id);
            return result.isEmpty() ? null : result.get(0);
        } catch(DataAccessException exception){
            System.err.println("Unable to find inscripcion with id=" + id);
            exception.printStackTrace();
            return null;
        }
    }

    public boolean addInscripcion(Inscripcion inscripcion){
        try{
            String query = sqlQueries.getProperty("insert-addInscripcion");
            if(query != null){
                int result = jdbcTemplate.update(query,
                    inscripcion.getFechaCreacion().toString(),
                    inscripcion.getTipo().toString(),
                    inscripcion.getCuotaActual(),
                    inscripcion.getDniTitular());
                return result > 0;
            } else
                return false;
        } catch(DataAccessException exception){
            System.err.println("Unable to insert inscripcion in the database");
            exception.printStackTrace();
            return false;
        }
    }

    public boolean deleteInscripcion(int id){
        try{
            String query = sqlQueries.getProperty("delete-deleteInscripcion");
            if(query != null){
                int result = jdbcTemplate.update(query, id);
                return result > 0;
            } else
                return false;
        } catch(DataAccessException exception){
            System.err.println("Unable to delete inscripcion with id=" + id);
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
