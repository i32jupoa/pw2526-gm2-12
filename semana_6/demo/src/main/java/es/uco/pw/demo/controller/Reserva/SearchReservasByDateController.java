package es.uco.pw.demo.controller.Reserva;

import es.uco.pw.demo.model.Reserva;
import es.uco.pw.demo.model.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

@Controller
public class SearchReservasByDateController {

    @Autowired
    private ReservaRepository reservaRepository;

    /**
     * Busca reservas por fecha. Se espera un parámetro 'date' en formato ISO (yyyy-MM-dd).
     * Nota: asume que ReservaRepository implementa findReservasByDate(LocalDate date).
     * Si prefieres filtrado en memoria con findAllReservas(), puedo cambiarlo.
     */
    @GetMapping("/searchReservasByDate")
    public ModelAndView searchReservasByDate(@RequestParam("date") String dateStr) {
        reservaRepository.setSQLQueriesFileName("src/main/resources/sql/reserva_queries.sql");

        ModelAndView mv = new ModelAndView("searchReservasByDateView");
        try {
            LocalDate date = LocalDate.parse(dateStr);
            List<Reserva> reservas = reservaRepository.findReservasByDate(date); // método esperado
            mv.addObject("date", date);
            mv.addObject("reservas", reservas);
        } catch (DateTimeParseException e) {
            mv.addObject("error", "Formato de fecha inválido. Use yyyy-MM-dd.");
            System.err.println("Invalid date format for searchReservasByDate: " + dateStr);
        } catch (Exception e) {
            System.err.println("Error searching reservas by date = " + dateStr);
            e.printStackTrace();
            mv.addObject("error", "Error interno buscando reservas.");
        }

        return mv;
    }
}
