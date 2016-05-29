package dev.siniy.atmsearch;

/**
 * Created by Ryou on 5/29/2016.
 */
public class Bank {
    private String idBank;
    private String nameBank;
    private String logoBank;

    public Bank(String idBank, String nameBank, String logoBank) {
        this.idBank = idBank;
        this.nameBank = nameBank;
        this.logoBank = logoBank;
    }

    public String getIdBank() {
        return idBank;
    }

    public void setIdBank(String idBank) {
        this.idBank = idBank;
    }

    public String getNameBank() {
        return nameBank;
    }

    public void setNameBank(String nameBank) {
        this.nameBank = nameBank;
    }

    public String getLogoBank() {
        return logoBank;
    }

    public void setLogoBank(String logoBank) {
        this.logoBank = logoBank;
    }

    @Override
    public String toString() {
        return getNameBank();
    }
}
