package com.example.newfinamwidget.Retrofit.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MarketData {

    @SerializedName("marketdata")
    @Expose
    private Marketdata marketdata;

    public Marketdata getMarketdata() {
        return marketdata;
    }

    public void setMarketdata(Marketdata marketdata) {
        this.marketdata = marketdata;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(MarketData.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("marketdata");
        sb.append('=');
        sb.append(((this.marketdata == null)?"<null>":this.marketdata));
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
        result = ((result* 31)+((this.marketdata == null)? 0 :this.marketdata.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof MarketData) == false) {
            return false;
        }
        MarketData rhs = ((MarketData) other);
        return ((this.marketdata == rhs.marketdata)||((this.marketdata!= null)&&this.marketdata.equals(rhs.marketdata)));
    }

}