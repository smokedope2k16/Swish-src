package dev.angel.api.module;

public enum Category {
    COMBAT("Combat"),
    MISC("Misc"),
    MOVEMENT("Movement"),
    PLAYER("Player"),
    RENDER("Render"),
    OTHER("Other");

    final String category;

    Category(String category) {
        this.category = category;
    }

    public String getLabel() {
        return category;
    }
}
