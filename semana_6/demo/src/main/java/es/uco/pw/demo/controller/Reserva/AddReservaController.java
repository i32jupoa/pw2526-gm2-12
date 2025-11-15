package es.uco.pw.demo.controller.Reserva;

import es.uco.pw.demo.model.domain.Reserva;
import es.uco.pw.demo.model.repository.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
    @PostMapping("/addReserva")
    public ModelAndView addReserva(
            @RequestParam String dniSocio,
            @RequestParam String matricula,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha,
            @RequestParam int plazasReservadas,
            @RequestParam String proposito,
            @RequestParam double precioTotal
    ) {
        ModelAndView mv;

        try {
            // Crear la reserva con ID temporal (0)
            Reserva reserva = new Reserva(
                    0,
                    dniSocio,
                    matricula,
                    fecha,
                    plazasReservadas,
                    proposito,
                    precioTotal
            );

            // Cargar archivo de queries SQL desde classpath
            reservaRepository.setSQLQueriesFileName("db/sql.properties");

            // Intentar añadir la reserva y obtener ID generado
            boolean success = reservaRepository.addReserva(reserva);

            if (success) {
                mv = new ModelAndView("reserva/addReservaViewSuccess");
                mv.addObject("reserva", reserva);
            } else {
                mv = new ModelAndView("reserva/addReservaViewFail");
            }
        } catch (Exception e) {
            e.printStackTrace();
            mv = new ModelAndView("reserva/addReservaViewFail");
            mv.addObject("errorMessage", e.getMessage());
        }

        return mv;
    }
}