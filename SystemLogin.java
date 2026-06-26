import java.util.Scanner; 

public class SystemLogin {

    Scanner scanner = new Scanner(System.in);

    public void LoginMainMenu() { 
        //Method that will be used to call the main menu, where 
        // users will need to input login info.

        while(true) {

                System.out.println("Welcome to Laurier Food Services.");
                System.out.println("\n1. Login");
                System.out.println("2. Create Account");
                System.out.println("3. Exit"); 
                
                System.out.print("Please enter an option: "); 
                int selection = scanner.nextInt();
                scanner.nextLine();
                
                if(selection == 1) {
                    login();
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

    }

    public void CreateAccount() {

    }


}
