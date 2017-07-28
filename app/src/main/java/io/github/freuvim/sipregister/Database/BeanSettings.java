package io.github.freuvim.sipregister.Database;

public class BeanSettings {
    private int idSetting;
    private String name_setting;
    private String value_setting;

    public BeanSettings() {
        idSetting = -1;
        value_setting = "";
        name_setting = "";
    }

    public int getIdSetting() {
        return idSetting;
    }

    public void setIdSetting(int idSetting) {
        this.idSetting = idSetting;
    }

    public String getValue_setting() {
        return value_setting;
    }

    public void setValue_setting(String value_setting) {
        this.value_setting = value_setting;
    }

    public String getName_setting() {
        return name_setting;
    }

    public void setName_setting(String name_setting) {
        this.name_setting = name_setting;
    }
}
