package es.uco.pw.demo.controller.Embarcacion;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import es.uco.pw.demo.model.domain.Embarcacion;
import es.uco.pw.demo.model.domain.TipoEmbarcacion;
import es.uco.pw.demo.model.repository.EmbarcacionRepository;

@Controller
public class ShowTiposEmbarcacionController {

    private final EmbarcacionRepository embarcacionRepository;

    public ShowTiposEmbarcacionController(EmbarcacionRepository embarcacionRepository) {
        this.embarcacionRepository = embarcacionRepository;
        String sqlQueriesFileName = "./src/main/resources/db/sql.properties";
        this.embarcacionRepository.setSQLQueriesFileName(sqlQueriesFileName);
    }

    @GetMapping("/showTiposEmbarcacion")
    public ModelAndView showTiposEmbarcacion() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("showTiposEmbarcacionView.html");

        List<Embarcacion> todas = embarcacionRepository.findAllEmbarcaciones();

        List<TipoResumen> resumenTipos = new ArrayList<>();
        for (TipoEmbarcacion tipo : TipoEmbarcacion.values()) {
            int cantidad = (int) todas.stream().filter(e -> e.getTipo() == tipo).count();
            resumenTipos.add(new TipoResumen(tipo.name(), cantidad));
        }

        modelAndView.addObject("resumenTipos", resumenTipos);
        return modelAndView;
    }

    public static class TipoResumen {
        private final String tipo;
        private final int cantidad;

        public TipoResumen(String tipo, int cantidad) {
            this.tipo = tipo;
            this.cantidad = cantidad;
        }

        public String getTipo() {
            return tipo;
        }

        public int getCantidad() {
            return cantidad;
        }
    }
}