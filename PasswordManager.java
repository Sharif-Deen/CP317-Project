import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.FileReader;


public class PasswordManager {

    private final String MyFile = "accounts.txt";
    
    /*
     * New implimentation to check user email validity. 
     */

    public boolean isValidEmail(String email) {
        if(email == null) {
            return false;
        }

        int atIndex = email.indexOf('@');
        int comIndex = email.lastIndexOf(".com");

        if(atIndex == -1 || comIndex == -1) {
            return false; 
        }

        if(atIndex == 0) {      //Check for text prior to @ sign.
            return false;
        }

        if(comIndex <= atIndex + 1) {   //Check to see if there is text between @ and .com
            return false;
        }

        return true;
    }

    /*
     * New implmentation to check user duplication. 
     * For the purposes of our service, we are going to assume that multiple user accounts can be assigned to the same email, but accounts cannot share duplicate names. 
     *  
     */

    public boolean userExists(String username) {
        try(BufferedReader reader = new BufferedReader(new FileReader(MyFile))) {

            String line;
            line = reader.readLine();

            while(line != null) {
                String[] components = line.split(",");
                String readUsername = components[0];

                if(readUsername.equals(username)) {
                    return true;
                }
                line = reader.readLine();
            }

        } 
        catch(Exception e) {

        }
        return false;
    }

    public void UserInformation(User user) {
        try{
            String hashed = Hash.hashPassword(user.getPassword());         //Obtains the users password and hashes it.
            FileWriter w = new FileWriter(MyFile, true);           
            
            w.write(user.getUsername() + "," + hashed + "," + user.getCategory() + "," + user.getEmail() + "\n");        //Write the users hashed information in the format {name},{hashed_password},{Category/Role}

            w.close();
        }

        catch (Exception e){
            e.printStackTrace();
        }
    }

    public boolean userVerification(String username, String password, String category, String email) {
        if(!isValidEmail(email)) {
            System.out.println("Invalid email format. Ensure it adheres to the format: text@text.com");
        }

        try(BufferedReader reader = new BufferedReader(new FileReader(MyFile))) {
            String line;
            boolean found = false;
            line = reader.readLine();
            
            while(line != null) { 
                String[] components = line.split(",");

                String ReadUsername = components[0];            //Split user info into 3 parts from reading the information.
                String ReadHash = components[1];
                String ReadCategory = components[2];
                String ReadEmail = components[3];

                if(ReadUsername.equals(username)) {
                    found = true; 

                    if(!ReadCategory.equals(category)) {
                        System.out.println("Incorrect role. Is the account registered to a customer or staff member?");
                        return false;
                    }

                    if(!ReadEmail.equalsIgnoreCase(email)) {
                        System.out.println("Provided email does not match.");
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
