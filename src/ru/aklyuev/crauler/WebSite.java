package ru.aklyuev.crauler;

/**
 * Created by Elishtar on 25/07/16.
 */
public class WebSite {

    private String name;
    private String url;
    private String stringMarker;
    private String firstConstraint;
    private String lastConstraint;
    private String customConstraint;

    public WebSite() {
    }

    public WebSite(String url, String stringMarker, String firstConstraint,
                   String lastConstraint, String customConstraint) {
        this.url = url;
        this.stringMarker = stringMarker;
        this.firstConstraint = firstConstraint;
        this.lastConstraint = lastConstraint;
        this.customConstraint = customConstraint;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getStringMarker() {
        return stringMarker;
    }

    public void setStringMarker(String stringMarker) {
        this.stringMarker = stringMarker;
    }

    public String getFirstConstraint() {
        return firstConstraint;
    }

    public void setFirstConstraint(String firstConstraint) {
        this.firstConstraint = firstConstraint;
    }

    public String getLastConstraint() {
        return lastConstraint;
    }

    public void setLastConstraint(String lastConstraint) {
        this.lastConstraint = lastConstraint;
    }

    public String getCustomConstraint() {
        return customConstraint;
    }

    public void setCustomConstraint(String customConstraint) {
        this.customConstraint = customConstraint;
    }
}
