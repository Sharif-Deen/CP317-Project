public class User {
    private String username;
    private String password; 
    private String category; 

    public User(String username, String password, String category) {
        this.username = username; 
        this.password = password;
        this.category = category; 
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
}
