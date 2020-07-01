package com.shivivats.kindcompanion;

public enum NoteType {
    NOTE_REMINDER(0),
    NOTE_VAULT(1);

    private int value;

    NoteType(int value) {
        this.value = value;
    }

    public static NoteType fromInt(int i) {
        for (NoteType b : NoteType.values()) {
            if (b.getValue() == i)
                return b;
        }
        return null;
    }

    public int getValue() {
        return value;
    }
}