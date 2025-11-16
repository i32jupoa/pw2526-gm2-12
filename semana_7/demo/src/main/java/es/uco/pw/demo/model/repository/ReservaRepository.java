package es.uco.pw.demo.model.repository;

import es.uco.pw.demo.model.domain.Reserva;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;
import java.util.Properties;

@Repository
public class ReservaRepository {

    private final JdbcTemplate jdbcTemplate;
    private Properties sqlQueries;

    @Autowired
    public ReservaRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        setSQLQueriesFileName("db/sql.properties");
    }

    public final void setSQLQueriesFileName(String sqlQueriesFileName) {
        try {
            this.sqlQueries = loadProperties(sqlQueriesFileName);
        } catch (IOException e) {
            System.err.println("Error cargando SQL properties en ReservaRepository");
            e.printStackTrace();
        }
    }

    private Properties loadProperties(String fileName) throws IOException {
        Properties properties = new Properties();
        InputStream input = getClass().getClassLoader().getResourceAsStream(fileName);
        if (input == null) {
            throw new IOException("No se encontró el archivo: " + fileName);
        }
        properties.load(input);
        return properties;
    }

    // Mapper reutilizable
    private static final class ReservaMapper implements RowMapper<Reserva> {
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
    }

    public List<Reserva> findAllReservas() {
        try {
            String query = sqlQueries.getProperty("select-findAllReservas");
            return jdbcTemplate.query(query, new ReservaMapper());
        } catch (DataAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Reserva findReservaById(int id) {
        try {
            String query = sqlQueries.getProperty("select-findReservaById");
            List<Reserva> results = jdbcTemplate.query(query, new ReservaMapper(), id);
            return results.isEmpty() ? null : results.get(0);
        } catch (DataAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean addReserva(Reserva reserva) {
        try {
            String query = sqlQueries.getProperty("insert-addReserva");
            KeyHolder keyHolder = new GeneratedKeyHolder();

            int rows = jdbcTemplate.update(connection -> {
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
            return rows > 0;
        } catch (DataAccessException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteReservaById(int id) {
        try {
            String query = sqlQueries.getProperty("delete-deleteReservaById");
            int result = jdbcTemplate.update(query, id);
            return result > 0;
        } catch (DataAccessException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean isReservada(String matricula, LocalDate fecha) {
        try {
            String query = sqlQueries.getProperty("select-isReservaDisponible");
            Integer count = jdbcTemplate.queryForObject(query, Integer.class, matricula, Date.valueOf(fecha));
            return (count != null && count > 0);
        } catch (DataAccessException e) {
            e.printStackTrace();
            return true; // Asumir no disponible en caso de error
        }
    }
    
    public boolean isAlquilada(String matricula, LocalDate fecha) {
        try {
            String query = sqlQueries.getProperty("select-isAlquilerDisponible");
            Integer count = jdbcTemplate.queryForObject(query, Integer.class, matricula, Date.valueOf(fecha));
            return (count != null && count > 0);
        } catch (DataAccessException e) {
            e.printStackTrace();
            return true; // Asumir no disponible en caso de error
        }
    }
}