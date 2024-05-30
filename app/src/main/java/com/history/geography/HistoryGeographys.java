package com.history.geography;

public class HistoryGeographys {
    private int no;
    private String name_chs;
    private String province_name;
    private String municipality_name;
    private String county_name;
    private String township_name;
    private String village_name;
    private String history;
    private int history_id;
    private String memo;

    public HistoryGeographys(int no, String name_chs, String province_name, String municipality_name, String county_name, String township_name, String village_name, String history, int history_id, String memo) {
        this.no = no;
        this.name_chs = name_chs;
        this.province_name = province_name;
        this.municipality_name = municipality_name;
        this.county_name = county_name;
        this.township_name = township_name;
        this.village_name = village_name;
        this.history = history;
        this.history_id = history_id;
        this.memo = memo;
    }

    public int getNo() {
        return no;
    }

    public void setNo(int no) {
        this.no = no;
    }

    public String getName_chs() {
        return name_chs;
    }

    public void setName_chs(String name_chs) {
        this.name_chs = name_chs;
    }

    public String getProvince_name() {
        return province_name;
    }

    public void setProvince_name(String province_name) {
        this.province_name = province_name;
    }

    public String getMunicipality_name() {
        return municipality_name;
    }

    public void setMunicipality_name(String municipality_name) {
        this.municipality_name = municipality_name;
    }

    public String getCounty_name() {
        return county_name;
    }

    public void setCounty_name(String county_name) {
        this.county_name = county_name;
    }

    public String getTownship_name() {
        return township_name;
    }

    public void setTownship_name(String township_name) {
        this.township_name = township_name;
    }

    public String getVillage_name() {
        return village_name;
    }

    public void setVillage_name(String village_name) {
        this.village_name = village_name;
    }

    public String getHistory() {
        return history;
    }

    public void setHistory(String history) {
        this.history = history;
    }

    public int getHistory_id() {
        return history_id;
    }

    public void setHistory_id(int history_id) {
        this.history_id = history_id;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }


}
