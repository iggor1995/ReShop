package com.epam.igor.electronicsshop.entity;

import java.util.Locale;

/**
 * Created by User on 31.07.2017.
 */
public class LocaleName extends BaseEntity {
    private String ruName;
    private String enName;

    public void setRuName(String ruName) {
        this.ruName = ruName;
    }

    public void setEnName(String enName) {
        this.enName = enName;
    }

    public String getRuName() {
        return ruName;
    }

    public String getEnName() {
        return enName;
    }

    @Override
    public String toString() {
        return "LocaleName{" +
                "ruName='" + ruName + '\'' +
                ", enName='" + enName + '\'' +
                '}';
    }

    public String getName(Locale locale){
        if(locale != null && locale.getLanguage().equals("ru")){
            return ruName;
        }
        else return enName;
    }
}
