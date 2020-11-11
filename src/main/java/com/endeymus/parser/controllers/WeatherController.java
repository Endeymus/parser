package com.endeymus.parser.controllers;

import com.endeymus.parser.model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class WeatherController {

    @Autowired
    private Weather weather;

    @Autowired
    private List<Parser> parserList;

    @PostMapping("/")
    public String weatherPost(@RequestParam String select, String search_box, Model model) {
        Parser parser = getParser(select);
        parser.setCity(search_box);

        weather.parse(parser);
        model.addAttribute("title", search_box);
        model.addAttribute("before", weather.getContentBefore());
        model.addAttribute("after", weather.getContentAfter());
        model.addAttribute("toFormat", weather.getToFormat());
        return "weather";
    }
    private Parser getParser(String select) {
        Parser parser = null;
        switch (select) {
            case "XML":
                parser = parserList.get(2);
                break;
            case "JSON":
                parser = parserList.get(1);
                break;
            case "HTML":
                parser = parserList.get(0);
                break;
        }
        return parser;
    }

}
