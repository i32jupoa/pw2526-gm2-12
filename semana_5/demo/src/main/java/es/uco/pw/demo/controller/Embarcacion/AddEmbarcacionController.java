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

    private EmbarcacionRepository embarcacionRepository;

    public AddEmbarcacionController(EmbarcacionRepository embarcacionRepository) {
        this.embarcacionRepository = embarcacionRepository;
        String sqlQueriesFileName = "./src/main/resources/db/sql.properties";
        this.embarcacionRepository.setSQLQueriesFileName(sqlQueriesFileName);
    }

    /**
     * Añadir embarcación (success/fail)
     */
    @GetMapping("/addEmbarcacion")
    public ModelAndView addEmbarcacion(
            @RequestParam("matricula") String matricula,
            @RequestParam("nombre") String nombre,
            @RequestParam("tipo") String tipo,
            @RequestParam("numPlazas") int numPlazas,
            @RequestParam("dimensiones") String dimensiones,
            @RequestParam(value = "dniPatron", required = false) String dniPatron
    ) {
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
                ? "embarcacion/addEmbarcacionViewSuccess.html"
                : "embarcacion/addEmbarcacionViewFail.html";

        ModelAndView model = new ModelAndView(view);
        model.addObject("embarcacion", embarcacion);
        return model;
    }

    /**
     * Buscar embarcaciones por tipo
     */
    @GetMapping("/findEmbarcacionesByTipo")
    public ModelAndView findEmbarcacionesByTipo(
            @RequestParam(value = "tipo", required = false) String tipo
    ) {
        // Si no viene tipo, mostrar la vista con el formulario vacío
        if (tipo == null) {
            return new ModelAndView("embarcacion/findEmbarcacionByTipo.html");
        }

        List<Embarcacion> embarcaciones = embarcacionRepository.findEmbarcacionesByTipo(
                TipoEmbarcacion.valueOf(tipo.toUpperCase())
        );

        ModelAndView model = new ModelAndView("embarcacion/findEmbarcacionByTipo.html");
        model.addObject("embarcaciones", embarcaciones);
        return model;
    }
}
