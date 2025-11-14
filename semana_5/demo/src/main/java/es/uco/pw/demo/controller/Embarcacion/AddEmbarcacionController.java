package es.uco.pw.demo.controller.Embarcacion;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import es.uco.pw.demo.model.domain.Embarcacion;
import es.uco.pw.demo.model.domain.TipoEmbarcacion;
import es.uco.pw.demo.model.repository.EmbarcacionRepository;

import java.util.List;

@Controller
public class AddEmbarcacionController {

    private final EmbarcacionRepository embarcacionRepository;

    public AddEmbarcacionController(EmbarcacionRepository embarcacionRepository) {
        this.embarcacionRepository = embarcacionRepository;
        String sqlQueriesFileName = "./src/main/resources/db/sql.properties";
        this.embarcacionRepository.setSQLQueriesFileName(sqlQueriesFileName);
    }

    /*** Añadir embarcación ***/
    @GetMapping("/addEmbarcacion")
    public ModelAndView addEmbarcacion(
            @RequestParam(value = "matricula", required = false) String matricula,
            @RequestParam(value = "nombre", required = false) String nombre,
            @RequestParam(value = "tipo", required = false) String tipo,
            @RequestParam(value = "numPlazas", required = false) Integer numPlazas,
            @RequestParam(value = "dimensiones", required = false) String dimensiones,
            @RequestParam(value = "dniPatron", required = false) String dniPatron
    ) {
        ModelAndView model;

        // Si no se envió matrícula u otros datos, mostramos error
        if (matricula == null || nombre == null || tipo == null || numPlazas == null || dimensiones == null) {
            model = new ModelAndView("embarcacion/addEmbarcacionViewFail.html");
            return model;
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

        if (success) {
            model = new ModelAndView("embarcacion/addEmbarcacionViewSuccess.html");
            model.addObject("embarcacion", embarcacion);
        } else {
            model = new ModelAndView("embarcacion/addEmbarcacionViewFail.html");
        }

        return model;
    }

    /*** Buscar embarcaciones por tipo ***/
    @GetMapping("/findEmbarcacionesByTipo")
    public ModelAndView findEmbarcacionesByTipo(
            @RequestParam(value = "tipo", required = false) String tipo
    ) {
        ModelAndView model = new ModelAndView("embarcacion/findEmbarcacionByTipo.html");

        if (tipo != null && !tipo.isEmpty()) {
            List<Embarcacion> embarcaciones = embarcacionRepository.findEmbarcacionesByTipo(
                    TipoEmbarcacion.valueOf(tipo.toUpperCase())
            );
            model.addObject("embarcaciones", embarcaciones);
        }

        return model;
    }
}
