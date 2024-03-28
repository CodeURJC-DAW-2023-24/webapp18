package es.codeurjc.restcontroller;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import es.codeurjc.DTO.OfferDTO;
import es.codeurjc.model.Lifeguard;
import es.codeurjc.service.UserService;

import org.springframework.web.bind.annotation.GetMapping;;
@RestController
public class ChartRestController {
    
    @Autowired
    private UserService userService;

    @GetMapping("/api/pieChart/")
    public ResponseEntity<HashMap<String,Integer>> getPieChart(){
        HashMap<String, Integer> mapa = fillAttitudes();
        return ResponseEntity.status(HttpStatus.OK).body(mapa);
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
}
