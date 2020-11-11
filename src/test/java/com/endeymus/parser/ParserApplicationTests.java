package com.endeymus.parser;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map;

@SpringBootTest
class ParserApplicationTests {


    private final Map<String, String> map = new LinkedHashMap<>();

    {
        map.put("city", "москва");
        map.put("temperature", "14.004");
    }

    @Test
    void toJson() {

        JSONObject json = new JSONObject();
        json.accumulate("city", map.get("city"));
        json.accumulate("temperature", map.get("temperature"));

        System.out.println(json);

    }

    @Test
    void toXML() throws ParserConfigurationException, TransformerException, IOException {
        Element root = createElement();
        print(root, "other.xml");
    }

    @Test
    void toHTML() throws ParserConfigurationException, TransformerException, IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("<!DOCTYPE html>")
                .append("<html lang=\"en\">")                                                                                                                  
                .append("<weather>")
                .append("<city>").append(map.get("city")).append("</city>")
                .append("<temperature>").append(map.get("temperature")).append("</temperature>")
                .append("</weather>")
                .append("</html>");
        Path path = Files.createFile(Path.of("other.html"));
        Files.writeString(path, sb.toString());
        Files.readAllLines(path).forEach(System.out::println);
        Files.delete(path);
    }

    private Element createElement() throws ParserConfigurationException {
        DocumentBuilder documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();

        Document document = documentBuilder.newDocument();

        Element root = document.createElement("weather");
        Element city = document.createElement("city");
        city.setTextContent(map.get(city.getTagName()));

        Element temp = document.createElement("temperature");
        temp.setTextContent(map.get(temp.getTagName()));

        root.appendChild(city);
        root.appendChild(temp);
        return root;
    }

    private void print(Node root, String file) throws TransformerException, IOException {
        Transformer tr = TransformerFactory.newInstance().newTransformer();
        DOMSource source = new DOMSource(root);
        FileOutputStream fos = new FileOutputStream(file);
        StreamResult result = new StreamResult(fos);
        tr.transform(source, result);
        fos.close();

        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line = "";
        while ( (line = reader.readLine()) != null) {
            System.out.println(line);
        }
        reader.close();
        Path path = Paths.get(file);
        Files.delete(path);
    }

}
