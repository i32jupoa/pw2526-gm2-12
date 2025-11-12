package main.java.es.uco.pw.demo.controller.Patron;

import main.java.es.uco.pw.demo.model.Patron;
import main.java.es.uco.pw.demo.model.PatronRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ShowPatronController {

    @Autowired
    private PatronRepository patronRepository;

    @GetMapping("/showPatron")
    public ModelAndView showPatron(@RequestParam("dni") String dni) {
        patronRepository.setSQLQueriesFileName("src/main/resources/sql/patron_queries.sql");

        Patron patron = patronRepository.findPatronByDni(dni);

        ModelAndView mv;
        if (patron != null) {
            mv = new ModelAndView("showPatronView");
            mv.addObject("patron", patron);
        } else {
            // Reutilizamos la vista de fallo de búsqueda por si existe; si prefieres otra vista, dímelo.
            mv = new ModelAndView("findPatronViewFail");
            mv.addObject("dni", dni);
        }
        return mv;
    }
}
