package app;

public class Session {

    private static String email;
    private Session() {

    }

    private static String role;
    private static boolean loggedIn = false;

    public static boolean isLogged() {
        return loggedIn;
    }

    public static void login(String userEmail, String userRole) {
        email = userEmail;
        role = userRole;
        loggedIn = true;
    }

    public static void logout() {
        email = null;
        role = null;
        loggedIn = false;
    }


    public static String getEmail() {
        return email;
    }

    public static String getRole() {
        return role;
    }

    public static boolean isAdmin() {
        return "admin".equals(role);
    }
}