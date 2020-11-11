package com.endeymus.parser.model;

import org.springframework.stereotype.Controller;

import java.util.Map;

@Controller
public class Weather {

    private String contentBefore;
    private String contentAfter;
    private String toFormat;

    public String getToFormat() {
        return toFormat;
    }

    public void setToFormat(String toFormat) {
        this.toFormat = toFormat;
    }

    public void parse(Parser parser) {
        StringBuilder sb = new StringBuilder();
        Map<String, String> map = parser.parse();
        map.forEach((k,v)->{
            sb.append(k).append("=").append(v).append(";");
        });
        setContentAfter(sb.toString());
        setContentBefore(parser.getContent());
        setToFormat(parser.unParse(map));
    }

    public String getContentBefore() {
        return contentBefore;
    }

    private void setContentBefore(String contentBefore) {
        this.contentBefore = contentBefore;
    }

    public String getContentAfter() {
        return contentAfter;
    }

    private void setContentAfter(String contentAfter) {
        this.contentAfter = contentAfter;
    }
}
