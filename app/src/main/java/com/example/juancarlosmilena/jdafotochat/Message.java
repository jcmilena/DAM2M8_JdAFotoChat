package com.example.juancarlosmilena.jdafotochat;

public class Message {

    String fromUser;
    String url;
    String msg;
    String fotoname;

    public Message(String fromUser, String url, String msg, String fotoname) {
        this.fromUser = fromUser;
        this.url = url;
        this.msg = msg;
        this.fotoname = fotoname;
    }

    public Message() {
    }

    public String getFromUser() {
        return fromUser;
    }

    public void setFromUser(String fromUser) {
        this.fromUser = fromUser;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getFotoname() {
        return fotoname;
    }

    public void setFotoname(String fotoname) {
        this.fotoname = fotoname;
    }
}
