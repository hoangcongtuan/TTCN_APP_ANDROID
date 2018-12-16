package com.qtt.hocbanglaixe.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SignDetail {
    @Expose
    private Integer iNDEX;
    @SerializedName("SIGNCATEGORY")
    @Expose
    private String sIGNCATEGORY;
    @SerializedName("DESC")
    @Expose
    private String dESC;
    @SerializedName("IMAGE")
    @Expose
    private String iMAGE;
    @SerializedName("NAME")
    @Expose
    private String nAME;

    public Integer getINDEX() {
        return iNDEX;
    }

    public void setINDEX(Integer iNDEX) {
        this.iNDEX = iNDEX;
    }

    public String getSIGNCATEGORY() {
        return sIGNCATEGORY;
    }

    public void setSIGNCATEGORY(String sIGNCATEGORY) {
        this.sIGNCATEGORY = sIGNCATEGORY;
    }

    public String getDESC() {
        return dESC;
    }

    public void setDESC(String dESC) {
        this.dESC = dESC;
    }

    public String getIMAGE() {
        return iMAGE;
    }

    public void setIMAGE(String iMAGE) {
        this.iMAGE = iMAGE;
    }

    public String getNAME() {
        return nAME;
    }

    public void setNAME(String nAME) {
        this.nAME = nAME;
    }
}
