package es.uco.pw.demo.controller.Reserva;

import es.uco.pw.demo.model.domain.Reserva;
import es.uco.pw.demo.model.repository.ReservaRepository;
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
    public ModelAndView findReserva(@RequestParam(value = "id", required = false) Integer id) {
        ModelAndView mv;
        
        // Si no hay ID, mostrar el formulario vacío
        if (id == null) {
            mv = new ModelAndView("reserva/findReservaViewSuccess");
            mv.addObject("reserva", null);
            return mv;
        }
        
        // Si hay ID, buscar la reserva
        reservaRepository.setSQLQueriesFileName("src/main/resources/db/sql.properties");
        Reserva reserva = reservaRepository.findReservaById(id);

        if (reserva != null) {
            mv = new ModelAndView("reserva/findReservaViewSuccess");
            mv.addObject("reserva", reserva);
        } else {
            mv = new ModelAndView("reserva/findReservaViewFail");
        }
        return mv;
    }
}