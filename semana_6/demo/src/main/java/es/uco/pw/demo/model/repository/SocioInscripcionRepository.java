package es.uco.pw.demo.model.repository;

import es.uco.pw.demo.model.domain.SocioInscripcion;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Repository
public class SocioInscripcionRepository {

    private String sqlQueriesFileName = "db/sql.properties";
    private Properties sqlQueries;

    // Configuración de la base de datos (ajusta según tu entorno)
    private static final String DB_URL = "jdbc:mysql://oraclepr.uco.es:3306/i32jupoa?useSSL=true&allowPublicKeyRetrieval=true&serverTimezone=UTC";
    private static final String DB_USER = "i32jupoa";
    private static final String DB_PASSWORD = "grupo12PW";

    public SocioInscripcionRepository() {
        setSQLQueriesFileName(this.sqlQueriesFileName);
    }

    public void setSQLQueriesFileName(String fileName) {
        this.sqlQueriesFileName = fileName;
        try {
            this.sqlQueries = loadProperties(fileName);
        } catch (Exception e) {
            System.err.println("Error cargando SQL properties en SocioInscripcionRepository");
            e.printStackTrace();
        }
    }

    private Properties loadProperties(String fileName) throws Exception {
        Properties properties = new Properties();
        InputStream input = getClass().getClassLoader().getResourceAsStream(fileName);
        if (input == null) {
            throw new FileNotFoundException("No se encontró el archivo: " + fileName);
        }
        properties.load(input);
        return properties;
    }

    // ---------------- MÉTODOS PRIVADOS CON CONNECTION ---------------- //

    private List<SocioInscripcion> findByIdInscripcion(int idInscripcion, Connection conn) throws DataAccessException {
        List<SocioInscripcion> results = new ArrayList<>();
        try {
            String sql = sqlQueries.getProperty("select-findSocioInscripcionByIdInscripcion");
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, idInscripcion);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String dniSocio = rs.getString("dniSocio");
                results.add(new SocioInscripcion(id, idInscripcion, dniSocio));
            }
            rs.close();
            ps.close();
        } catch (Exception e) {
            throw new DataAccessException("Error en findByIdInscripcion", e) {};
        }
        return results;
    }

    private List<SocioInscripcion> findByDni(String dni, Connection conn) throws DataAccessException {
        List<SocioInscripcion> results = new ArrayList<>();
        try {
            String sql = sqlQueries.getProperty("select-findSocioInscripcionByDni");
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, dni);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                int idInscripcion = rs.getInt("idInscripcion");
                results.add(new SocioInscripcion(id, idInscripcion, dni));
            }
            rs.close();
            ps.close();
        } catch (Exception e) {
            throw new DataAccessException("Error en findByDni", e) {};
        }
        return results;
    }

    private void add(SocioInscripcion socioInscripcion, Connection conn) throws DataAccessException {
        try {
            String sql = sqlQueries.getProperty("insert-addSocioInscripcion");
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, socioInscripcion.getIdInscripcion());
            ps.setString(2, socioInscripcion.getDniSocio());
            ps.executeUpdate();
    
            ResultSet generatedKeys = ps.getGeneratedKeys();
            if (generatedKeys.next()) {
                socioInscripcion.setId(generatedKeys.getInt(1));
            }
    
            generatedKeys.close();
            ps.close();
        } catch (Exception e) {
            throw new DataAccessException("Error en add()", e) {};
        }
    }
    

    private int delete(int idInscripcion, String dniSocio, Connection conn) throws DataAccessException {
        try {
            String sql = sqlQueries.getProperty("delete-deleteSocioInscripcion");
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, idInscripcion);
            ps.setString(2, dniSocio);
            int affectedRows = ps.executeUpdate();
            ps.close();
            return affectedRows;
        } catch (Exception e) {
            throw new DataAccessException("Error en delete()", e) {};
        }
    }

    // ---------------- MÉTODOS PÚBLICOS ---------------- //

    public List<SocioInscripcion> findByIdInscripcionPublic(int idInscripcion) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            return findByIdInscripcion(idInscripcion, conn);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public List<SocioInscripcion> findByDniSocio(String dni) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            return findByDni(dni, conn);
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public boolean addSocioInscripcion(SocioInscripcion socioInscripcion) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            add(socioInscripcion, conn);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteSocioInscripcion(int idInscripcion, String dniSocio) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            int affectedRows = delete(idInscripcion, dniSocio, conn);
            return affectedRows > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public SocioInscripcion findById(int id) {
        List<SocioInscripcion> results = findByIdInscripcionPublic(id);
        return results.isEmpty() ? null : results.get(0);
    }

}
