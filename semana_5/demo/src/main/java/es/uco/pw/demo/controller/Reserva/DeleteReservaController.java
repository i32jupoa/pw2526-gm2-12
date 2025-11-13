package es.uco.pw.demo.controller.Reserva;

import es.uco.pw.demo.model.repository.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class DeleteReservaController {

    @Autowired
    private ReservaRepository reservaRepository;

    @GetMapping("/deleteReserva")
    public ModelAndView deleteReserva(@RequestParam("id") int id) {
        reservaRepository.setSQLQueriesFileName("src/main/resources/sql/reserva_queries.sql");
        boolean deleted = reservaRepository.deleteReservaById(id);

        ModelAndView mv;
        if (deleted) {
            mv = new ModelAndView("reserva/deleteReservaViewSuccess");
            mv.addObject("id", id);
        } else {
            mv = new ModelAndView("reserva/deleteReservaViewFail");
        }
        return mv;
    }
}
