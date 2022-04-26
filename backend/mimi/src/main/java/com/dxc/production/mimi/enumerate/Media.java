package com.dxc.production.mimi.enumerate;

public enum Media {
    PNG(".png", "image/png"),
    JPEG(".jpeg", "image/jpeg"),
    JPG(".jpg", "image/jpg"),
    MP4("./MP4" , "video/mp4");

    private String type;
    private String format;

    Media(String type, String format) {
        this.type = type;
        this.format = format;
    }
    public String getType() {
        return type;
    }
    public String getFormat() {
        return format;
    }
    public static Media typeToMedia(String type) {
        for(Media r: Media.values()) {
            if(r.type.equals(type)) {
                return r;
            }
        }
        throw new IllegalArgumentException("No constant with type " + type + " found");
    }

    public static Media formatToMedia(String format) {
        for(Media r: Media.values()) {
            if(r.format.equals(format)) {
                return r;
            }
        }
        throw new IllegalArgumentException("No constant with format " + format + " found");
    }
}
