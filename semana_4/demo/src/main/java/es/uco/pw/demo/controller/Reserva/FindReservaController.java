package es.uco.pw.demo.controller.reserva;

import es.uco.pw.demo.model.Reserva;
import es.uco.pw.demo.model.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class FindReservaController {

    @Autowired
    private ReservaRepository reservaRepository;

    @GetMapping("/findReserva")
    public ModelAndView findReserva(@RequestParam("id") int id) {
        reservaRepository.setSQLQueriesFileName("src/main/resources/sql/reserva_queries.sql");
        Reserva reserva = reservaRepository.findReservaById(id);

        ModelAndView mv;
        if (reserva != null) {
            mv = new ModelAndView("findReservaViewSuccess");
            mv.addObject("reserva", reserva);
        } else {
            mv = new ModelAndView("findReservaViewFail");
        }
        return mv;
    }
}
