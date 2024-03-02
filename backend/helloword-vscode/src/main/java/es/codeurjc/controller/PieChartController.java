package es.codeurjc.controller;

import java.util.HashMap;
import java.util.List;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import es.codeurjc.model.Lifeguard;
import es.codeurjc.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
@Controller
public class PieChartController {

    @Autowired
    private UserService userService;


    @GetMapping("/pieChart")
    public String showPieChart(Model model, HttpServletRequest request) {

        //CHECK USER LOGED OR NOT
        if (request.getUserPrincipal() != null){
            model.addAttribute("loged", true);
        }else{
            model.addAttribute("loged", false);
        }

        //find the lifeguards, go through their skill lists and put them on a map. Fill the model with the map

      HashMap<String, Integer> mapa = fillAttitudes();
      
      model.addAttribute("trust", mapa.get("Confianza"));
      model.addAttribute("attitude", mapa.get("Actitud positiva"));
      model.addAttribute("effort", mapa.get("Esfuerzo"));
      model.addAttribute("comunication", mapa.get("Comunicación"));
      model.addAttribute("resolution", mapa.get("Resolución de problemas"));
      model.addAttribute("leadership", mapa.get("Liderazgo"));
        return "pieChart";
    }

    public HashMap<String, Integer> fillAttitudes(){
        HashMap<String, Integer> map = new HashMap<String, Integer>();
        map.put("Confianza",0);
        map.put("Actitud positiva",0);
        map.put("Resolución de problemas",0);
        map.put("Esfuerzo",0);
        map.put("Comunicación",0);
        map.put("Liderazgo",0);
        List<Lifeguard> l = userService.findAllLifeguard();
        for (Lifeguard lifeguard : l) {
            for (String skill : lifeguard.getSkills()) {
                if(map.containsKey(skill)){
                    map.put(skill,map.get(skill)+1);
                }
            }
        }

        return map;

    }
}