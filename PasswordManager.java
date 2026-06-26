import java.io.FileWriter;

public class PasswordManager {

    private final String MyFile = "accounts.txt";
    
    public void UserInformation(User user) {
        try{
            
            String hashed = Hash.hashPassword(user.getPassword());         //Obtains the users password and hashes it.
            FileWriter w = new FileWriter(MyFile, true);           
            
            w.write(user.getUsername() + "," + hashed + "," + user.getCategory() + "\n");        //Write the users hashed information in the format {name},{hashed_password},{Category/Role}

            w.close();
        }

        catch (Exception e){
            e.printStackTrace();
        }
    }
}
