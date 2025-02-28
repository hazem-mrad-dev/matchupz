package models.utilisateur;

public enum Role {
    ADMIN("Admin"),
    RESPONSABLE_SPORTIF("Responsable Sportif"),
    RESERVATEUR("Reservateur"),
    RESPONSABLE_SPONSORING("Responsable Sponsoring"),
    RESPONSABLE_LOGISTIQUE("Responsable Logistique"),
    RESPONSABLE_ESPACE_SPORTIF("Responsable espace sportif"),
    RESPONSABLE_COACH("Responsable coach"),
    UTILISATEUR("Utilisateur");


    private final String value;

    Role(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }























    public static Role fromString(String roleStr) {
        for (Role roleEnum : Role.values()) {
            if (roleEnum.getValue().equalsIgnoreCase(roleStr)) {
                return roleEnum;
            }
        }
        throw new IllegalArgumentException("No enum constant for value: " + roleStr);
    }
}
