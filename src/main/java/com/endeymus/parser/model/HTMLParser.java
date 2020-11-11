package com.endeymus.parser.model;

import lombok.SneakyThrows;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class HTMLParser extends Parser {
    private final static String mode = "HTML";

    public HTMLParser() {
    }

    public HTMLParser(String city) {
        this.city = city;
    }

    @Override
    public String getUrl() {
        return "http://api.openweathermap.org/data/2.5/weather?q=" + city +  "&appid=" + APIKEY + "&mode=" + mode.toLowerCase();
    }

    @SneakyThrows
    @Override
    public Map<String, String> parse() {
        Map<String, String> map = new LinkedHashMap<>();
        String url = getUrl();

        if(!url.isEmpty()){
            Document document = Jsoup.connect(url).get();
            for (Element element : document.getAllElements()) {

                if (element.attr("title").equals("Current Temperature")){
                    map.put("temperature", element.text().substring(0, 3));
                }
            }
            map.put("city", document.getElementsByTag("div").get(0).text());
        }
        return map;
    }

    @Override
    String unParse(Map<String, String> map) {
        StringBuilder sb = new StringBuilder();
        sb.append("<!DOCTYPE html>")
                .append("<html lang=\"en\">")
                .append("<weather>");
        map.forEach((k,v)->{
            sb.append("<").append(k).append(">").append(v).append("</").append(k).append(">");
        });
        sb.append("</weather>")
                .append("</html>");
        return sb.toString();
    }


}
