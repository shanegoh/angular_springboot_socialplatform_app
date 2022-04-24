package com.dxc.production.mimi.enumerate;

public enum Role {
    ADMIN(0),
    USER(1);

    private int value;
    Role(int value) {
        this.value = value;
    }
    public int getValue() {
        return value;
    }
    public void setValue(int value) {
        this.value = value;
    }
    public static Role valueToRole(int value) {
        for(Role r: Role.values()) {
            if(r.value == value) {
                return r;
            }
        }
        throw new IllegalArgumentException("No constant with value " + value + " found");
    }
}
