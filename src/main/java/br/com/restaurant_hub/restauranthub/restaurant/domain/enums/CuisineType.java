package br.com.restaurant_hub.restauranthub.restaurant.domain.enums;

public enum CuisineType {
    ARABE("Árabe"),
    BRASILEIRA("Brasileira"),
    JAPONESA("Japonesa"),
    ITALIANA("Italiana"),
    PORTUGUESA("Portuguesa");
    
    private final String displayName;
    
    CuisineType(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
}