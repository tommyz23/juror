package nju.software.data;

/**
 * Created by Administrator on 2018/7/30.
 */

public class Case {
    private String index;
    private String address;
    private String time;
    private String id;
    private String name;
    private String undertaker;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUndertaker() {
        return undertaker;
    }

    public void setUndertaker(String undertaker) {
        this.undertaker = undertaker;
    }

    public String getAddress() {
        return address;
    }

    public String getIndex() {
        return index;
    }

    public String getTime() {
        return time;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Case() {
    }
}
