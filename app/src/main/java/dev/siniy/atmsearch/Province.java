package dev.siniy.atmsearch;

/**
 * Created by Ryou on 5/29/2016.
 */
public class Province {

    private String idProvince;
    private String nameProvince;

    public Province(String idProvince, String nameProvince) {
        this.idProvince = idProvince;
        this.nameProvince = nameProvince;
    }

    public String getIdProvince() {
        return idProvince;
    }

    public void setIdProvince(String idProvince) {
        this.idProvince = idProvince;
    }

    public String getNameProvince() {
        return nameProvince;
    }

    public void setNameProvince(String nameProvince) {
        this.nameProvince = nameProvince;
    }

    @Override
    public String toString() {
        return getNameProvince();
    }
}
