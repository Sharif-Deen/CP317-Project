package features;
public class User {
    private int id;
    private String username;
    private String password; 
    private String category;
    private String email;

    public User(int id, String username, String password, String category, String email) {
        this.id = id;
        this.username = username; 
        this.password = password;
        this.category = category;
        this.email = email;
    }

    public int getId() {return id;}
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
