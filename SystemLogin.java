import java.util.Scanner; 

public class SystemLogin {

    Scanner scanner = new Scanner(System.in);
    PasswordManager pManager = new PasswordManager();

    public void LoginMainMenu() { 
        //Method that will be used to call the main menu, where 
        // users will need to input login info.

        while(true) {

                System.out.println("Welcome to Laurier Food Services.");
                System.out.println("\n1. Login");
                System.out.println("2. Create Account");
                System.out.println("3. Exit"); 
                
                System.out.print("Please enter an option: "); 
                int selection = Integer.parseInt(scanner.nextLine());
                
                
                if(selection == 1) {
                    login();
                    break;
                }
                else if(selection == 2) {
                    CreateAccount();
                }
                else if(selection == 3) {
                    break;
                }
                else {
                    System.out.println("Invalid choice. Please select an option 1-3. ");
                }
        }
    }

    public void login() {
        System.out.print("Enter username: "); 
        String username = scanner.nextLine();

        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        System.out.println("Enter 1 if you are a customer.");
        System.out.println("Enter 2 if you are a staff member.");

        int selection = Integer.parseInt(scanner.nextLine());
        String category;

        if(selection == 1) {
            category = "customer";
        }

        else if(selection == 2) {
            category = "staff";
        }

        else {
            return;
        }

        boolean verified = pManager.userVerification(username, password, category);
        if(verified == true) {
            System.out.printf("Login successful, welcome %s\n", username);
        }
        else {
            System.out.println("Login unsuccessful."); 
        }
    }

    public void CreateAccount() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        String password; 

        while(true) {
            System.out.println("Enter password: ");
            password = scanner.nextLine();

            if(PasswordValidity(password) == true)
                break;

            System.out.println("Try again.");
        }

        System.out.println("Enter 1 if you are a customer.");
        System.out.println("Enter 2 if you are a staff member.");

        int selection = Integer.parseInt(scanner.nextLine());
        
        String category;

        if(selection == 1) {
            category = "customer";
        }

        else if(selection == 2) {
            category = "staff";
        }

        else {
            return;
        }

        User user = new User(username, password, category);
        pManager.UserInformation(user);
        
    }

    public boolean PasswordValidity(String password) {
        if(password.length() < 8) {
            System.out.println("Password must be 8 characters long minimum.");
            return false;
        }

        boolean upper = false;
        boolean lower = false;
        boolean number = false;
        boolean specialCase = false;

        for(int i = 0; i < password.length(); i++) {
            char c = password.charAt(i);

            if(Character.isUpperCase(c))
                upper = true;
            
            else if(Character.isLowerCase(c))
                lower = true;

            else if (Character.isDigit(c))
                number = true;

            else
                specialCase = true; 
        }

        if(!upper || !lower || !number || !specialCase) {
            System.out.println("Enesure the password contains: 1 uppercase letter, 1 lowercase letter, 1 number, and one special case character.");
            return false;
        }
        
        return true;  
    }


}
