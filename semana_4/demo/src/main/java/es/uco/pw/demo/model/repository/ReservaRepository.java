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
public class ReservaRepository {

    private JdbcTemplate jdbcTemplate;
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
            System.err.println("Unable to find reservas");
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
            System.err.println("Unable to find reserva with id=" + id);
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
            System.err.println("Unable to find reservas with dniSocio=" + dniSocio);
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
            System.err.println("Unable to find reservas with matricula=" + matricula);
            e.printStackTrace();
            return null;
        }
    }

    public boolean addReserva(Reserva reserva) {
        try {
            String query = sqlQueries.getProperty("insert-addReserva");
            if (query != null) {
                int result = jdbcTemplate.update(query,
                        reserva.getId(),
                        reserva.getDniSocio(),
                        reserva.getMatricula(),
                        Date.valueOf(reserva.getFecha()),
                        reserva.getPlazasReservadas(),
                        reserva.getProposito(),
                        reserva.getPrecioTotal());
                return result > 0;
            } else {
                return false;
            }
        } catch (DataAccessException e) {
            System.err.println("Unable to insert reserva into the database");
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteReservaById(int id) {
        try {
            String query = sqlQueries.getProperty("delete-reservaById");
            if (query != null) {
                int result = jdbcTemplate.update(query, id);
                return result > 0;
            } else {
                return false;
            }
        } catch (DataAccessException e) {
            System.err.println("Unable to delete reserva with id=" + id);
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