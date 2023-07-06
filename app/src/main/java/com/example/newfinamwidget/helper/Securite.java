package com.example.newfinamwidget.helper;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Securite {

    @SerializedName("SECID")
    @Expose
    private String secid;
    @SerializedName("SHORTNAME")
    @Expose
    private String shortname;
    //add Boolean for checkbox
    public Boolean checked = false;

    /**
     * No args constructor for use in serialization
     *
     */
    public Securite() {
    }

    /**
     *
     * @param shortname
     * @param secid
     */
    public Securite(String secid, String shortname) {
        super();
        this.secid = secid;
        this.shortname = shortname;
    }

    public String getSecid() {
        return secid;
    }

    public void setSecid(String secid) {
        this.secid = secid;
    }

    public String getShortname() {
        return shortname;
    }

    public void setShortname(String shortname) {
        this.shortname = shortname;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Securite.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("secid");
        sb.append('=');
        sb.append(((this.secid == null)?"<null>":this.secid));
        sb.append(',');
        sb.append("shortname");
        sb.append('=');
        sb.append(((this.shortname == null)?"<null>":this.shortname));
        sb.append(',');
        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = ((result* 31)+((this.shortname == null)? 0 :this.shortname.hashCode()));
        result = ((result* 31)+((this.secid == null)? 0 :this.secid.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Securite) == false) {
            return false;
        }
        Securite rhs = ((Securite) other);
        return (((this.shortname == rhs.shortname)||((this.shortname!= null)&&this.shortname.equals(rhs.shortname)))&&((this.secid == rhs.secid)||((this.secid!= null)&&this.secid.equals(rhs.secid))));
    }

}
