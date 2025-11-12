package main.java.es.uco.pw.demo.controller.Patron;

import main.java.es.uco.pw.demo.model.Patron;
import main.java.es.uco.pw.demo.model.PatronRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;

@Controller
public class AddPatronController {

    @Autowired
    private PatronRepository patronRepository;

    @GetMapping("/addPatron")
    public ModelAndView addPatron() {
        // Crear un nuevo Patron (ejemplo simple)
        Patron patron = new Patron(
                "12345678A",
                "Juan",
                "Pérez",
                LocalDate.of(1985, 3, 12),
                LocalDate.of(2020, 5, 1)
        );

        patronRepository.setSQLQueriesFileName("src/main/resources/sql/patron_queries.sql");
        boolean success = patronRepository.addPatron(patron);

        ModelAndView mv;
        if (success) {
            mv = new ModelAndView("addPatronViewSuccess");
            mv.addObject("patron", patron);
        } else {
            mv = new ModelAndView("addPatronViewFail");
        }
        return mv;
    }
}
