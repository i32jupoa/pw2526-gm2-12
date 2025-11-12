package es.uco.pw.demo.controller.embarcacion;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import es.uco.pw.demo.model.Embarcacion;
import es.uco.pw.demo.model.TipoEmbarcacion;
import es.uco.pw.demo.model.EmbarcacionRepository;

@Controller
public class ShowTiposEmbarcacionController {
    
    private EmbarcacionRepository embarcacionRepository;
    private ModelAndView modelAndView = new ModelAndView();
    
    public ShowTiposEmbarcacionController(EmbarcacionRepository embarcacionRepository) {
        this.embarcacionRepository = embarcacionRepository;
        String sqlQueriesFileName = "./src/main/resources/db/sql.properties";
        this.embarcacionRepository.setSQLQueriesFileName(sqlQueriesFileName);
    }

    @GetMapping("/showTiposEmbarcacion")
    public ModelAndView showTiposEmbarcacion() {
        this.modelAndView.setViewName("showTiposEmbarcacionView.html");

        TipoEmbarcacion[] tipos = TipoEmbarcacion.values();
        List<TipoResumen> resumenTipos = new ArrayList<>();

        for (TipoEmbarcacion tipo : tipos) {
            List<Embarcacion> embarcaciones = this.embarcacionRepository.findEmbarcacionesByTipo(tipo);
            int cantidad = (embarcaciones != null) ? embarcaciones.size() : 0;
            resumenTipos.add(new TipoResumen(tipo.name(), cantidad));
        }

        this.modelAndView.addObject("resumenTipos", resumenTipos);
        return modelAndView;
    }

    /** Clase auxiliar para representar el resumen en la vista **/
    public static class TipoResumen {
        private String tipo;
        private int cantidad;

        public TipoResumen(String tipo, int cantidad) {
            this.tipo = tipo;
            this.cantidad = cantidad;
        }

        public String getTipo() { return tipo; }
        public int getCantidad() { return cantidad; }
    }
}
