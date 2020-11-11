package com.endeymus.parser.model;

import lombok.SneakyThrows;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;
@Component
public class JSONParser extends Parser {
    public JSONParser() {
    }

    public JSONParser(String city) {
        this.city = city;
    }

    @Override
    public String getUrl() {
        return "http://api.openweathermap.org/data/2.5/weather?q=" + city +  "&appid=" + APIKEY;
    }

    @SneakyThrows
    @Override
    public Map<String, String> parse() {
        Map<String, String> map = new LinkedHashMap<>();
        String url = getUrl();

        if(!url.isEmpty()){
            JSONObject json = new JSONObject(Jsoup.connect(url).ignoreContentType(true).execute().body());
            map.put("temperature", String
                    .format("%.2f", json.getJSONObject("main").getDouble("temp") - 273.15)
                    .replaceAll(",", "."));
            map.put("city", json.getString("name"));
        }
        return map;
    }

    @Override
    String unParse(Map<String, String> map) {
        JSONObject json = new JSONObject();

        map.forEach(json::accumulate);

        return json.toString();
    }


}
