import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.FileReader;


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

    public boolean userVerification(String username, String password, String category) {
        try(BufferedReader reader = new BufferedReader(new FileReader(MyFile))) {
            String line;
            boolean found = false;
            line = reader.readLine();
            
            

            while(line != null) { 
                String[] components = line.split(",");

                String ReadUsername = components[0];            //Split user info into 3 parts from reading the information.
                String ReadHash = components[1];
                String ReadCategory = components[2];

                if(ReadUsername.equals(username)) {
                    found = true; 

                    if(!ReadCategory.equals(category)) {
                        System.out.println("Incorrect role. Is the account registered to a customer or staff member?");
                        return false;
                    }

                    String comparisonHash = Hash.hashPassword(password);
                    
                    if(ReadHash.equals(comparisonHash)) {
                        return true;
                    }
                    else {
                        System.out.println("Incorrect password.");
                        return false;
                    }
                }

                line = reader.readLine();
            }

            if(found == false) {
                System.out.println("User not found.");
            }

        } catch(Exception e) {
            e.printStackTrace();
            }

        return false;
    }

}
