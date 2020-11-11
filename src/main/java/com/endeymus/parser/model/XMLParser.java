package com.endeymus.parser.model;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class XMLParser extends Parser {
    private final static String mode = "XML";

    public XMLParser() {
    }

    public XMLParser(String city) {
        this.city = city;
    }

    @SneakyThrows
    private Document getDoc(){
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        return builder.parse(getUrl());
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
            Document document = getDoc();
            NodeList nodeList = document.getDocumentElement().getElementsByTagName("temperature");
                NamedNodeMap node = nodeList.item(0).getAttributes();
                map.put("temperature", String.format("%.2f", Double.
                        parseDouble(node.getNamedItem("value").getNodeValue()) - 273.15).
                        replaceAll(",","."));
            nodeList = document.getDocumentElement().getElementsByTagName("city");
                node = nodeList.item(0).getAttributes();
            map.put("city", node.getNamedItem("name").getNodeValue());
        }
        return map;
    }

    @Override
    String unParse(Map<String, String> map) {
        StringBuilder sb = new StringBuilder();
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>")
                .append("<weather>");
        map.forEach((k,v)->{
            sb.append("<").append(k).append(">").append(v).append("</").append(k).append(">");
        });
        sb.append("</weather>");
        return sb.toString();
    }

}
