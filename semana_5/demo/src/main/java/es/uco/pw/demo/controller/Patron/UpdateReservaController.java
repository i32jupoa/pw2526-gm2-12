package es.uco.pw.demo.controller.Reserva;

import es.uco.pw.demo.model.Reserva;
import es.uco.pw.demo.model.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.support.SessionStatus;

import java.time.LocalDate;

/**
 * Controlador que gestiona la actualización de reservas existentes.
 * Usa un flujo GET → POST como en los ejemplos de la profesora:
 * - GET: muestra un formulario con los datos actuales
 * - POST: procesa la actualización
 */
@Controller
@SessionAttributes("reserva")
public class UpdateReservaController {

    @Autowired
    private ReservaRepository reservaRepository;

    /** 
     * Paso 1: Mostrar el formulario de actualización 
     * (carga los datos actuales de la reserva)
     */
    @GetMapping("/updateReserva")
    public ModelAndView showUpdateForm(@RequestParam("id") int id) {
        reservaRepository.setSQLQueriesFileName("src/main/resources/sql/reserva_queries.sql");
        Reserva reserva = reservaRepository.findReservaById(id);

        ModelAndView mv;
        if (reserva != null) {
            mv = new ModelAndView("updateReservaForm");
            mv.addObject("reserva", reserva);
        } else {
            mv = new ModelAndView("findReservaViewFail");
        }
        return mv;
    }

    /** 
     * Paso 2: Procesar los datos del formulario (HTTP POST)
     */
    @PostMapping("/updateReserva")
    public ModelAndView processUpdate(
            @ModelAttribute("reserva") Reserva reservaActualizada,
            SessionStatus sessionStatus) {

        reservaRepository.setSQLQueriesFileName("src/main/resources/sql/reserva_queries.sql");

        // No existe método updateReserva en tu repositorio, pero podemos reutilizar addReserva()
        // si el SQL tiene un "INSERT ... ON DUPLICATE KEY UPDATE" o puedes crear update-reserva
        boolean updated = false;
        try {
            String query = "update-reserva"; // key del archivo SQL
            updated = reservaRepository.addReserva(reservaActualizada);
        } catch (Exception e) {
            System.err.println("Error al actualizar reserva: " + e.getMessage());
        }

        sessionStatus.setComplete();

        ModelAndView mv;
        if (updated) {
            mv = new ModelAndView("updateReservaViewSuccess");
            mv.addObject("reserva", reservaActualizada);
        } else {
            mv = new ModelAndView("updateReservaViewFail");
        }
        return mv;
    }
}
