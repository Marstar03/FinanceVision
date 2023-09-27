package core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class User {
    private String username;
    private String password;
    private String fullName;
    private String email;
    private Account account;
    private Budget budget;


    public static final List<String> defaultExpenseCategories = new ArrayList<>(Arrays.asList("Food", "Clothes", "Housing", "Other")); //add more later
    public static final List<String> defaultIncomeCategories = new ArrayList<>(Arrays.asList("Salary", "Gift", "Interests", "Other")); //add more later


    public User(String username, String password, String fullName, String email, Account account) {
        setUsername(username);
        setPassword(password);
        setFullName(fullName);
        setEmail(email);
        this.account = account;
        this.budget = null;
    }


    public String getUsername() {
        return username;
    }


    public void setUsername(String username) {
        if (username.contains(" ")) {
            throw new IllegalArgumentException("username cannot include space");
        }
        else if (username.equals("") || username.equals(null)){
            throw new IllegalArgumentException("username is empty.");
        }
        this.username = username;
    }


    public String getPassword() {
        return password;
    }


    public void setPassword(String password) {
        if (password.length() < 8){
            throw new IllegalArgumentException("Password is too short");
        }
        this.password = password;
    }


    public String getFullName() {
        return fullName;
    }


    public void setFullName(String fullName) {
        if (!fullName.contains(" ")){
            throw new IllegalArgumentException("Full name must include a space");
        }

        this.fullName = fullName;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {

        if (!email.contains("@") || !email.contains(".")) { //email må inneholde "@", og kan ikke inneholde andre symboler enn de oppgitt.
            throw new IllegalArgumentException("Invalid email");
        }
        this.email = email;
    }


    public Account getAccount() {
        return account;
    }


    public void setAccount(Account account) {
        this.account = account; 
    }


    public Budget getBudget() {
        return budget;
    }


    public void setBudget(Budget budget) {
        this.budget = budget;
    }
    
}
