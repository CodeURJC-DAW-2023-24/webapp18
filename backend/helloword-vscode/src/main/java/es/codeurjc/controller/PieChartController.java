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
        model.addAttribute("loged", request.getUserPrincipal() != null);

        //find the lifeguards, go through their skill lists and put them on a map. Fill the model with the map

        HashMap<String, Integer> mapa = fillAttitudes();
        HashMap<String, Float> mapa2 = getDistribution(mapa);


        model.addAttribute("trust", mapa.get("Confianza"));
        model.addAttribute("attitude", mapa.get("Actitud positiva"));
        model.addAttribute("effort", mapa.get("Esfuerzo"));
        model.addAttribute("comunication", mapa.get("Comunicación"));
        model.addAttribute("resolution", mapa.get("Resolución de problemas"));
        model.addAttribute("leadership", mapa.get("Liderazgo"));
        return "pieChart";
    }

    public HashMap<String, Integer> fillAttitudes() {
        HashMap<String, Integer> map = new HashMap<String, Integer>();
        for (String skill : Lifeguard.getSkillsSet()) {
            map.put(skill, 0);
        }
        List<Lifeguard> l = userService.findAllLifeguard();
        for (Lifeguard lifeguard : l) {
            for (String skill : Lifeguard.getSkillsSet()) {
                if (lifeguard.getSkills().contains(skill)) {
                    map.put(skill, map.get(skill) + 1);
                }
            }
        }

        return map;
    }
    public HashMap<String, Float> getDistribution(HashMap<String, Integer> m){
        HashMap<String, Float> map = new HashMap<String, Float>();
        float cont = 0;
        for(String apt: m.keySet()){
            cont = cont+m.get(apt);
        }
        for(String apt: m.keySet()){
            map.put(apt, (m.get(apt)*100/cont));
        }
        return map;
    }
    
}