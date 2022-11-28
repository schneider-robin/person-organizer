package de.schneider.robin.frontend.api;

import de.schneider.robin.frontend.backend.BackendClient;
import de.schneider.robin.lib.api.model.PersonResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

@Controller
public class PageController {

    private final BackendClient backendClient;

    public PageController(
            BackendClient backendClient
    ) {
        this.backendClient = backendClient;
    }

    @GetMapping("/people_v3")
    public ModelAndView getStartPage(Map<String, Object> model){
        model.put("title", "Title from PageController");
        List<PersonResponse> personResponseList = backendClient.fetchPersonResponseList();
        model.put("people", personResponseList);
        return new ModelAndView("people_v3", model); // people_v3.mustache file in resources/templates
    }
}
