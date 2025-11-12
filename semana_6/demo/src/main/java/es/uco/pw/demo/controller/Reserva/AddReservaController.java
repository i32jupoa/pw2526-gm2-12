package main.java.es.uco.pw.demo.controller.Reserva;

import main.java.es.uco.pw.demo.model.Reserva;
import main.java.es.uco.pw.demo.model.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;

@Controller
public class AddReservaController {

    @Autowired
    private ReservaRepository reservaRepository;

    @GetMapping("/addReserva")
    public ModelAndView addReserva() {
        Reserva reserva = new Reserva(
                1,
                "12345678A",
                "ABC123",
                LocalDate.now(),
                3,
                "Excursión familiar",
                250.0
        );

        reservaRepository.setSQLQueriesFileName("src/main/resources/sql/reserva_queries.sql");
        boolean success = reservaRepository.addReserva(reserva);

        ModelAndView mv;
        if (success) {
            mv = new ModelAndView("addReservaViewSuccess");
            mv.addObject("reserva", reserva);
        } else {
            mv = new ModelAndView("addReservaViewFail");
        }
        return mv;
    }
}
