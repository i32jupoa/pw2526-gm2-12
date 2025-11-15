package es.uco.pw.demo.model.repository;

import es.uco.pw.demo.model.domain.Reserva;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Properties;

@Repository
public class ReservaRepository {

    private final JdbcTemplate jdbcTemplate;
    private Properties sqlQueries;
    private String sqlQueriesFileName;

    public ReservaRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void setSQLQueriesFileName(String sqlQueriesFileName) {
        this.sqlQueriesFileName = sqlQueriesFileName;
        createProperties();
    }

    public List<Reserva> findAllReservas() {
        try {
            String query = sqlQueries.getProperty("select-findAllReservas");
            if (query != null) {
                return jdbcTemplate.query(query, new RowMapper<Reserva>() {
                    public Reserva mapRow(ResultSet rs, int rowNum) throws SQLException {
                        return new Reserva(
                                rs.getInt("id"),
                                rs.getString("dniSocio"),
                                rs.getString("matricula"),
                                rs.getDate("fecha").toLocalDate(),
                                rs.getInt("plazasReservadas"),
                                rs.getString("proposito"),
                                rs.getDouble("precioTotal")
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

    public Reserva findReservaById(int id) {
        try {
            String query = sqlQueries.getProperty("select-findReservaById");
            if (query != null) {
                return jdbcTemplate.queryForObject(query, new Object[]{id}, new RowMapper<Reserva>() {
                    public Reserva mapRow(ResultSet rs, int rowNum) throws SQLException {
                        return new Reserva(
                                rs.getInt("id"),
                                rs.getString("dniSocio"),
                                rs.getString("matricula"),
                                rs.getDate("fecha").toLocalDate(),
                                rs.getInt("plazasReservadas"),
                                rs.getString("proposito"),
                                rs.getDouble("precioTotal")
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

    public List<Reserva> findReservasByDniSocio(String dniSocio) {
        try {
            String query = sqlQueries.getProperty("select-findReservasByDniSocio");
            if (query != null) {
                return jdbcTemplate.query(query, new RowMapper<Reserva>() {
                    public Reserva mapRow(ResultSet rs, int rowNum) throws SQLException {
                        return new Reserva(
                                rs.getInt("id"),
                                rs.getString("dniSocio"),
                                rs.getString("matricula"),
                                rs.getDate("fecha").toLocalDate(),
                                rs.getInt("plazasReservadas"),
                                rs.getString("proposito"),
                                rs.getDouble("precioTotal")
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

    public List<Reserva> findReservasByMatricula(String matricula) {
        try {
            String query = sqlQueries.getProperty("select-findReservasByMatricula");
            if (query != null) {
                return jdbcTemplate.query(query, new RowMapper<Reserva>() {
                    public Reserva mapRow(ResultSet rs, int rowNum) throws SQLException {
                        return new Reserva(
                                rs.getInt("id"),
                                rs.getString("dniSocio"),
                                rs.getString("matricula"),
                                rs.getDate("fecha").toLocalDate(),
                                rs.getInt("plazasReservadas"),
                                rs.getString("proposito"),
                                rs.getDouble("precioTotal")
                        );
                    }
                }, matricula);
            } else {
                return null;
            }
        } catch (DataAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    // ---------------- addReserva con ID generado por BD ----------------
    public boolean addReserva(Reserva reserva) {
        try {
            String query = sqlQueries.getProperty("insert-addReserva");
            if (query != null) {
                KeyHolder keyHolder = new GeneratedKeyHolder();

                jdbcTemplate.update(connection -> {
                    PreparedStatement ps = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
                    ps.setString(1, reserva.getDniSocio());
                    ps.setString(2, reserva.getMatricula());
                    ps.setDate(3, Date.valueOf(reserva.getFecha()));
                    ps.setInt(4, reserva.getPlazasReservadas());
                    ps.setString(5, reserva.getProposito());
                    ps.setDouble(6, reserva.getPrecioTotal());
                    return ps;
                }, keyHolder);

                Number generatedId = keyHolder.getKey();
                if (generatedId != null) {
                    reserva.setId(generatedId.intValue()); // asigna el ID generado
                }
                return true;
            } else {
                System.err.println("SQL query for addReserva is null");
                return false;
            }
        } catch (DataAccessException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteReservaById(int id) {
        try {
            String query = sqlQueries.getProperty("delete-deleteReservaById");
            if (query != null) {
                int result = jdbcTemplate.update(query, id);
                return result > 0;
            } else {
                return false;
            }
        } catch (DataAccessException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void createProperties() {
        sqlQueries = new Properties();
        try {
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            try (BufferedReader reader = new BufferedReader(
                    new java.io.InputStreamReader(loader.getResourceAsStream(sqlQueriesFileName))
            )) {
                sqlQueries.load(reader);
            }
        } catch (IOException | NullPointerException e) {
            System.err.println("Error loading SQL properties file: " + sqlQueriesFileName);
            e.printStackTrace();
        }
    }
}