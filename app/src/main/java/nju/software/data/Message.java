package nju.software.data;

import org.litepal.crud.DataSupport;

public class Message extends DataSupport{
    private String index;
    private String message;

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Message(String message) {
        this.message = message;
    }

    public Message() {
    }
}
