package cn.yunchuang.im.model;

import java.io.Serializable;

/**
 * Created by zhou_yuntao on 2019/4/17.
 */

public class ShaixuanModel implements Serializable {

    private int xbSelected;
    private int fromAge;
    private int toAge;
    private int fromHeight;
    private int toHeight;

    public int getXbSelected() {
        return xbSelected;
    }

    public void setXbSelected(int xbSelected) {
        this.xbSelected = xbSelected;
    }

    public int getFromAge() {
        return fromAge;
    }

    public void setFromAge(int fromAge) {
        this.fromAge = fromAge;
    }

    public int getToAge() {
        return toAge;
    }

    public void setToAge(int toAge) {
        this.toAge = toAge;
    }

    public int getFromHeight() {
        return fromHeight;
    }

    public void setFromHeight(int fromHeight) {
        this.fromHeight = fromHeight;
    }

    public int getToHeight() {
        return toHeight;
    }

    public void setToHeight(int toHeight) {
        this.toHeight = toHeight;
    }
}
