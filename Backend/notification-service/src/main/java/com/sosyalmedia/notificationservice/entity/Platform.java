package com.sosyalmedia.notificationservice.entity;

public enum Platform {
    INSTAGRAM("Instagram", "#E4405F"),
    FACEBOOK("Facebook", "#1877F2"),
    TIKTOK("TikTok", "#000000");

    private final String displayName;
    private final String brandColor;

    Platform(String displayName, String brandColor) {
        this.displayName = displayName;
        this.brandColor = brandColor;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getBrandColor() {
        return brandColor;
    }
}