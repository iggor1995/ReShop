package com.epam.igor.electronicsshop.entity;

import java.util.Locale;

/**
 * Created by User on 31.07.2017.
 */
public class EntityDescription extends BaseEntity{
    private String ruDescription;
    private String enDescription;

    public void setRuDescription(String ruDescription) {
        this.ruDescription = ruDescription;
    }

    public void setEnDescription(String enDescription) {
        this.enDescription = enDescription;
    }

    public String getRuDescription() {

        return ruDescription;
    }

    public String getEnDescription() {
        return enDescription;
    }
    public String getDescription(Locale locale){
        if(locale != null && locale.getLanguage().equals("ru")){
            return ruDescription;
        }
        else return enDescription;
    }

    @Override
    public String toString() {
        return "EntityDescription{" +
                "ruDescription='" + ruDescription + '\'' +
                ", enDescription='" + enDescription + '\'' +
                '}';
    }
}
