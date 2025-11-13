package es.uco.pw.demo.model.repository;

import es.uco.pw.demo.model.domain.AlquilerSocio;
import jakarta.annotation.PostConstruct;
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

@Repository
public class AlquilerSocioRepository {

    private final JdbcTemplate jdbcTemplate;
    private Properties sqlQueries;

    public AlquilerSocioRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostConstruct
    private void loadSqlQueries() {
        sqlQueries = new Properties();
        try (InputStream inputStream = getClass().getResourceAsStream("/db/sqlQueries.properties")) {
            if (inputStream != null) {
                sqlQueries.load(inputStream);
            } else {
                System.err.println("sqlQueries.properties not found in /db/");
            }
        } catch (IOException e) {
            System.err.println("Error loading SQL properties for AlquilerSocioRepository");
            e.printStackTrace();
        }
    }

    public List<AlquilerSocio> findAllAlquilerSocios() {
        try {
            String query = sqlQueries.getProperty("select-findAllAlquilerSocios");
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
            e.printStackTrace();
            return false;
        }
    }
}
