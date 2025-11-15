package es.uco.pw.demo.controller.Embarcacion;

import es.uco.pw.demo.model.domain.Embarcacion;
import es.uco.pw.demo.model.domain.TipoEmbarcacion;
import es.uco.pw.demo.model.repository.EmbarcacionRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AddEmbarcacionController {

    private final EmbarcacionRepository embarcacionRepository;

    public AddEmbarcacionController(EmbarcacionRepository embarcacionRepository) {
        this.embarcacionRepository = embarcacionRepository;
        String sqlQueriesFileName = "./src/main/resources/db/sql.properties";
        this.embarcacionRepository.setSQLQueriesFileName(sqlQueriesFileName);
    }

    /**
     * Añadir embarcación (success/fail). Si no vienen parámetros, muestra el formulario.
     */
    @GetMapping("/addEmbarcacion")
    public ModelAndView addEmbarcacion(
            @RequestParam(value = "matricula", required = false) String matricula,
            @RequestParam(value = "nombre", required = false) String nombre,
            @RequestParam(value = "tipo", required = false) String tipo,
            @RequestParam(value = "numPlazas", required = false) Integer numPlazas,
            @RequestParam(value = "dimensiones", required = false) String dimensiones,
            @RequestParam(value = "dniPatron", required = false) String dniPatron
    ) {
        if (matricula == null || nombre == null || tipo == null || numPlazas == null || dimensiones == null) {
            return new ModelAndView("embarcacion/addEmbarcacionForm");
        }

        Embarcacion embarcacion = new Embarcacion(
                matricula,
                nombre,
                TipoEmbarcacion.valueOf(tipo.toUpperCase()),
                numPlazas,
                dimensiones,
                dniPatron
        );

        boolean success = embarcacionRepository.addEmbarcacion(embarcacion);
        String view = success
                ? "embarcacion/addEmbarcacionViewSuccess"
                : "embarcacion/addEmbarcacionViewFail";

        ModelAndView model = new ModelAndView(view);
        model.addObject("embarcacion", embarcacion);
        return model;
    }

    // ⚠ Eliminamos el método duplicado de búsqueda
}
