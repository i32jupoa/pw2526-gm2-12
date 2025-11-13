package es.uco.pw.demo.controller.Patron;

import es.uco.pw.demo.model.domain.Patron;
import es.uco.pw.demo.model.repository.PatronRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;

@Controller
public class AddPatronController {

    @Autowired
    private PatronRepository patronRepository;

    // Mostrar el formulario
    @GetMapping("/addPatronForm")
    public String showAddPatronForm() {
        return "patron/addPatronForm";
    }

    // Procesar el formulario
    @GetMapping("/addPatron")
    public ModelAndView addPatron(
            @RequestParam String dni,
            @RequestParam String nombre,
            @RequestParam String apellidos,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaNacimiento,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaExpedicionTitulo
    ) {
        // Crear un nuevo Patron con los datos del formulario
        Patron patron = new Patron(
                dni,
                nombre,
                apellidos,
                fechaNacimiento,
                fechaExpedicionTitulo
        );

        patronRepository.setSQLQueriesFileName("src/main/resources/db/sql.properties");
        boolean success = patronRepository.addPatron(patron);

        ModelAndView mv;
        if (success) {
            mv = new ModelAndView("patron/addPatronViewSuccess");
            mv.addObject("patron", patron);
        } else {
            mv = new ModelAndView("patron/addPatronViewFail");
        }
        return mv;
    }
}