package com.dxc.production.mimi.enumerate;

public enum Status {
    AVAILABLE(0),
    DELETED(1);

    private int value;
    Status(int value) {
        this.value = value;
    }
    public int getValue() {
        return value;
    }
    public void setValue(int value) {
        this.value = value;
    }
    public static Status valueToRole(int value) {
        for(Status r: Status.values()) {
            if(r.value == value) {
                return r;
            }
        }
        throw new IllegalArgumentException("No constant with value " + value + " found");
    }
}
