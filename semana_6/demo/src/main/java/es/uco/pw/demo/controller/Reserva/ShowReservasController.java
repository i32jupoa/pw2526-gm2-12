package main.java.es.uco.pw.demo.controller.Reserva;

import main.java.es.uco.pw.demo.model.Reserva;
import main.java.es.uco.pw.demo.model.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class ShowReservasController {

    @Autowired
    private ReservaRepository reservaRepository;

    @GetMapping("/showReservas")
    public ModelAndView showReservas() {
        reservaRepository.setSQLQueriesFileName("src/main/resources/sql/reserva_queries.sql");

        List<Reserva> reservas = reservaRepository.findAllReservas();

        ModelAndView mv = new ModelAndView("showReservasView");
        mv.addObject("reservas", reservas);
        return mv;
    }
}
