package es.uco.pw.demo.controller.Reserva;

import es.uco.pw.demo.model.domain.Reserva;
import es.uco.pw.demo.model.repository.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;

@Controller
public class AddReservaController {

    @Autowired
    private ReservaRepository reservaRepository;

    // Mostrar el formulario
    @GetMapping("/addReservaForm")
    public String showAddReservaForm() {
        return "reserva/addReservaForm";
    }

    // Procesar el formulario
    @GetMapping("/addReserva")
    public ModelAndView addReserva(
            @RequestParam int id,
            @RequestParam String dniSocio,
            @RequestParam String matricula,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
            @RequestParam int plazasReservadas,
            @RequestParam String proposito,
            @RequestParam double precioTotal
    ) {
        // Crear la reserva con los datos del formulario
        Reserva reserva = new Reserva(
                id,
                dniSocio,
                matricula,
                fecha,
                plazasReservadas,
                proposito,
                precioTotal
        );

        reservaRepository.setSQLQueriesFileName("db/sql.properties");
        boolean success = reservaRepository.addReserva(reserva);

        ModelAndView mv;
        if (success) {
            mv = new ModelAndView("reserva/addReservaViewSuccess");
            mv.addObject("reserva", reserva);
        } else {
            mv = new ModelAndView("reserva/addReservaViewFail");
        }
        return mv;
    }
}