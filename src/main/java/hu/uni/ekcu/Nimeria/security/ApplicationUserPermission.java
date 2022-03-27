package hu.uni.ekcu.Nimeria.security;

public enum ApplicationUserPermission {

    EXCERCISE_READ("exercise:read"),
    EXCERCISE_WRITE("exercise:write"),
    SOLUTION_READ("solution:read"),
    SOLUTION_WRITE("solution:write");

    private final String permission;

    ApplicationUserPermission(String permission) {
        this.permission = permission;
    }

    public String getPermission(){
        return permission;
    }
}
