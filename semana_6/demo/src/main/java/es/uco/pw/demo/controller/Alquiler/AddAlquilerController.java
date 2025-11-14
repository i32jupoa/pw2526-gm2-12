package es.uco.pw.demo.controller.Alquiler;

import es.uco.pw.demo.model.domain.Alquiler;
import es.uco.pw.demo.model.repository.AlquilerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;

@Controller
@SessionAttributes("alquiler")
public class AddAlquilerController {

    @Autowired
    private AlquilerRepository alquilerRepository;

    /**
     * Mostrar formulario para añadir un nuevo alquiler
     */
    @GetMapping("/addAlquiler")
    public ModelAndView showAddAlquilerForm() {
        ModelAndView modelAndView = new ModelAndView("alquiler/addAlquilerForm");
        // Creamos un objeto vacío con valores por defecto
        modelAndView.addObject("alquiler", new Alquiler(
                0,               // id
                0,               // idInscripcion
                "",              // matricula
                LocalDate.now(), // fechaInicio
                LocalDate.now(), // fechaFin
                0,               // plazasReservadas
                0.0              // precioTotal
        ));
        return modelAndView;
    }

    /**
     * Procesar el formulario y añadir el alquiler
     */
    @PostMapping("/addAlquiler")
    public ModelAndView processAddAlquiler(@ModelAttribute("alquiler") Alquiler alquiler,
                                           SessionStatus status) {
        ModelAndView modelAndView;

        boolean success = alquilerRepository.addAlquiler(alquiler);

        if (success) {
            modelAndView = new ModelAndView("alquiler/addAlquilerViewSuccess");
            modelAndView.addObject("alquiler", alquiler);
        } else {
            modelAndView = new ModelAndView("alquiler/addAlquilerViewFail");
        }

        // Limpiamos la sesión para no mantener el objeto alquiler
        status.setComplete();
        return modelAndView;
    }
}
