package com.example.keepbook;

import java.util.Date;
import java.util.Locale;

public class Item implements java.io.Serializable {
	 
    // �s���B����ɶ��B�C��B���D�B���e�B�ɮצW�١B�g�n�סB�ק�B�w���
    private long id;
    private long datetime;
    private Colors color;
    private String title;
    private int direct;
    private String content;
    private String fileName;
    private double latitude;
    private double longitude;
    private long lastModify;
    private int clear;
    private boolean selected;
 
    public Item() {
        title = "";
        content = "";
        direct = 0;
        clear = 0;
        color = Colors.LIGHTGREY;
    }
 
    public Item(long id, long datetime, Colors color, int direct, String title,
            String content, int clear, String fileName, double latitude, double longitude,
            long lastModify) {
        this.id = id;
        this.datetime = datetime;
        this.color = color;
        this.direct = direct;
        this.title = title;
        this.content = content;
        this.clear = clear;
        this.fileName = fileName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.lastModify = lastModify;
    }
 
    public long getId() {
        return id;
    }
 
    public void setId(long id) {
        this.id = id;
    }
 
    public long getDatetime() {
        return datetime;
    }
 
    // �˸m�ϰ쪺����ɶ�
    public String getLocaleDatetime() {
        return String.format(Locale.getDefault(), "%tF  %<tR", new Date(datetime));
    }   
 
    // �˸m�ϰ쪺���
    public String getLocaleDate() {
        return String.format(Locale.getDefault(), "%tF", new Date(datetime));
    }
 
    // �˸m�ϰ쪺�ɶ�
    public String getLocaleTime() {
        return String.format(Locale.getDefault(), "%tR", new Date(datetime));
    }   
 
    public void setDatetime(long datetime) {
        this.datetime = datetime;
    }
 
    public Colors getColor() {
        return color;
    }
 
    public void setColor(Colors color) {
        this.color = color;
    }
 
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }

    public int getDirect() {
        return direct;
    }
 
    public void setDirect(int direct) {
        this.direct = direct;
    }
    
    public String getContent() {
        return content;
    }
 
    public void setContent(String content) {
        this.content = content;
    }
 
    public String getFileName() {
        return fileName;
    }
 
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
 
    public double getLatitude() {
        return latitude;
    }
 
    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }
 
    public double getLongitude() {
        return longitude;
    }
 
    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
 
    public long getLastModify() {
        return lastModify;
    }
 
    public void setLastModify(long lastModify) {
        this.lastModify = lastModify;
    }
 
    public int getClear() {
        return clear;
    }
    
    public void setClear(int clear) {
        this.clear = clear;
    }
    
    public boolean isSelected() {
        return selected;
    }
 
    public void setSelected(boolean selected) {
        this.selected = selected;
    }
 
}