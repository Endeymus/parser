package com.endeymus.parser.model;

import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.*;
import java.io.IOException;

@Component
public abstract class Parser {

    @Value("${weather.api}")
    protected String APIKEY;
    protected String city;

    public void setCity(String city) {
        this.city = city;
    }

    //создание API-ссылки
    abstract String getUrl();

    //собственно сам парсинг
    //далее преобразуем значения в карту {ключ=значение}
    abstract Map<String, String> parse();
    //из карты значения преобзауем в формат (XML, HTML, JSOM)
    abstract String unParse(Map<String, String> map);
    //получение тела ответа от API-запроса
    //та фигня, где мы получем данные от стороннего сервиса, откуда парсим данные
    //а дальше мы их просто в переменную формата String преобзауем
    String getContent() {
        String content = "";
        try {
            content = Jsoup.connect(getUrl()).ignoreContentType(true).execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }
}
