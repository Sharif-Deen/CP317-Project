package features;
public class User {
    private String username;
    private String password; 
    private String category;
    private String email;

    public User(String username, String password, String category, String email) {
        this.username = username; 
        this.password = password;
        this.category = category;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password; 
    }

    public String getCategory() {
        return category;
    }

    public String getEmail() {
        return email;
    }
}
