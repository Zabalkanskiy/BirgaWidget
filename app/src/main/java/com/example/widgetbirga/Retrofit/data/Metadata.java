package com.example.widgetbirga.Retrofit.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Metadata {

    @SerializedName("MARKETPRICE")
    @Expose
    private Marketprice marketprice;

    public Marketprice getMarketprice() {
        return marketprice;
    }

    public void setMarketprice(Marketprice marketprice) {
        this.marketprice = marketprice;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(Metadata.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("marketprice");
        sb.append('=');
        sb.append(((this.marketprice == null)?"<null>":this.marketprice));
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
        result = ((result* 31)+((this.marketprice == null)? 0 :this.marketprice.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof Metadata) == false) {
            return false;
        }
        Metadata rhs = ((Metadata) other);
        return ((this.marketprice == rhs.marketprice)||((this.marketprice!= null)&&this.marketprice.equals(rhs.marketprice)));
    }

}