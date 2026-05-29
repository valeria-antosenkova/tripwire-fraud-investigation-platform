package com.teamcrocodile.tripwire.model;

public enum Status {
    UNDER_REVIEW,
    APPROVED,
    DENIED,
    UNASSIGNED;


    public static Status fromId(int id) {
        return switch (id) {
            case 1 -> UNDER_REVIEW;
            case 2 -> APPROVED;
            case 3 -> DENIED;
            case 4 -> UNASSIGNED;
            default -> throw new IllegalArgumentException("Unknown status id: " + id);
        };
    }


}
