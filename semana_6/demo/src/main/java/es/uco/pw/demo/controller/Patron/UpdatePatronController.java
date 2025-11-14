package es.uco.pw.demo.controller.Patron;

import es.uco.pw.demo.model.domain.Patron;
import es.uco.pw.demo.model.repository.PatronRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.support.SessionStatus;

/**
 * Controlador que gestiona la actualización de patrones existentes.
 * Sigue un flujo GET → POST:
 * - GET: muestra el formulario con los datos actuales del patrón
 * - POST: procesa la actualización en la base de datos
 */
@Controller
@SessionAttributes("patron")
public class UpdatePatronController {

    @Autowired
    private PatronRepository patronRepository;

    /**
     * Paso 1: Mostrar el formulario de actualización del patrón
     */
    @GetMapping("/updatePatron")
    public ModelAndView showUpdateForm(@RequestParam("dni") String dni) {
        patronRepository.setSQLQueriesFileName("src/main/resources/sql/patron_queries.sql");
        Patron patron = patronRepository.findPatronByDni(dni);

        ModelAndView mv;
        if (patron != null) {
            mv = new ModelAndView("updatePatronForm");
            mv.addObject("patron", patron);
        } else {
            mv = new ModelAndView("findPatronViewFail");
        }
        return mv;
    }

    /**
     * Paso 2: Procesar los datos del formulario (HTTP POST)
     */
    @PostMapping("/updatePatron")
    public ModelAndView processUpdate(
            @ModelAttribute("patron") Patron patronActualizado,
            SessionStatus sessionStatus) {

        patronRepository.setSQLQueriesFileName("src/main/resources/sql/patron_queries.sql");

        boolean updated = false;
        try {
            // Se asume que el PatronRepository tiene un método updatePatron()
            updated = patronRepository.updatePatron(patronActualizado);
        } catch (Exception e) {
            System.err.println("Error al actualizar patrón: " + e.getMessage());
        }

        sessionStatus.setComplete();

        ModelAndView mv;
        if (updated) {
            mv = new ModelAndView("updatePatronViewSuccess");
            mv.addObject("patron", patronActualizado);
        } else {
            mv = new ModelAndView("updatePatronViewFail");
        }
        return mv;
    }
}
