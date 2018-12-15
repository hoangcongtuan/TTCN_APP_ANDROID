package com.qtt.hocbanglaixe.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Question {

    @SerializedName("ID")
    @Expose
    private Integer iD;
    @SerializedName("QUESTIONTYPE")
    @Expose
    private Integer qUESTIONTYPE;
    @SerializedName("QUESTIONCONTENT")
    @Expose
    private String qUESTIONCONTENT;
    @SerializedName("ANSWERDEST")
    @Expose
    private String aNSWERDEST;
    @SerializedName("ANSWERS")
    @Expose
    private String aNSWERS;
    @SerializedName("OPTION1")
    @Expose
    private String oPTION1;
    @SerializedName("OPTION2")
    @Expose
    private String oPTION2;
    @SerializedName("OPTION3")
    @Expose
    private String oPTION3;
    @SerializedName("OPTION4")
    @Expose
    private String oPTION4;

    public Integer getID() {
        return iD;
    }

    public void setID(Integer iD) {
        this.iD = iD;
    }

    public Integer getQUESTIONTYPE() {
        return qUESTIONTYPE;
    }

    public void setQUESTIONTYPE(Integer qUESTIONTYPE) {
        this.qUESTIONTYPE = qUESTIONTYPE;
    }

    public String getQUESTIONCONTENT() {
        return qUESTIONCONTENT;
    }

    public void setQUESTIONCONTENT(String qUESTIONCONTENT) {
        this.qUESTIONCONTENT = qUESTIONCONTENT;
    }

    public String getANSWERDEST() {
        return aNSWERDEST;
    }

    public void setANSWERDEST(String aNSWERDEST) {
        this.aNSWERDEST = aNSWERDEST;
    }

    public String getANSWERS() {
        return aNSWERS;
    }

    public void setANSWERS(String aNSWERS) {
        this.aNSWERS = aNSWERS;
    }

    public String getOPTION1() {
        return oPTION1;
    }

    public void setOPTION1(String oPTION1) {
        this.oPTION1 = oPTION1;
    }

    public String getOPTION2() {
        return oPTION2;
    }

    public void setOPTION2(String oPTION2) {
        this.oPTION2 = oPTION2;
    }

    public String getOPTION3() {
        return oPTION3;
    }

    public void setOPTION3(String oPTION3) {
        this.oPTION3 = oPTION3;
    }

    public String getOPTION4() {
        return oPTION4;
    }

    public void setOPTION4(String oPTION4) {
        this.oPTION4 = oPTION4;
    }

}
