package dev.siniy.atmsearch;

/**
 * Created by Ryou on 5/29/2016.
 */
public class District {
    private String idDistrict;
    private String nameDistrict;
    private String idProvince;

    public District(String idDistrict, String nameDistrict, String idProvince) {
        this.idDistrict = idDistrict;
        this.nameDistrict = nameDistrict;
        this.idProvince = idProvince;
    }

    public District(String idDistrict, String nameDistrict) {
        this.idDistrict = idDistrict;
        this.nameDistrict = nameDistrict;
    }

    public String getIdDistrict() {
        return idDistrict;
    }

    public void setIdDistrict(String idDistrict) {
        this.idDistrict = idDistrict;
    }

    public String getNameDistrict() {
        return nameDistrict;
    }

    public void setNameDistrict(String nameDistrict) {
        this.nameDistrict = nameDistrict;
    }

    public String getIdProvince() {
        return idProvince;
    }

    public void setIdProvince(String idProvince) {
        this.idProvince = idProvince;
    }

    @Override
    public String toString() {
        return getNameDistrict();
    }
}
