package com.lgt.paykredit.Models;

/**
 * Created by Ranjan on 3/16/2020.
 */
public class ModelSelectLanguage {

    private String languageId, languageName;
    int bgColor;

    public ModelSelectLanguage(String languageId, String languageName, int bgColor) {
        this.languageId = languageId;
        this.languageName = languageName;
        this.bgColor = bgColor;
    }

    public String getLanguageId() {
        return languageId;
    }

    public void setLanguageId(String languageId) {
        this.languageId = languageId;
    }

    public String getLanguageName() {
        return languageName;
    }

    public void setLanguageName(String languageName) {
        this.languageName = languageName;
    }

    public int getBgColor() {
        return bgColor;
    }

    public void setBgColor(int bgColor) {
        this.bgColor = bgColor;
    }
}
