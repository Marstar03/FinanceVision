package core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Contains logic for a user.
 */
public class User {
    private String username;
    private String password;
    private String fullName;
    private String email;
    private Account account;
    private Budget budget;


    public static final List<String> defaultExpenseCategories = new ArrayList<>(
        Arrays.asList("Food", "Clothes", "Housing", "Other")); //add more later
    
    public static final List<String> defaultIncomeCategories = new ArrayList<>(
        Arrays.asList("Salary", "Gift", "Interests", "Other")); //add more later


    /**
     * Creates a new user, using the given username, password, full name, email and account.
     *
     * @param username the given username
     * @param password the given password
     * @param fullName the given full name
     * @param email the given email
     * @param account the given account
     */
    public User(String username, String password, String fullName, String email, Account account) {
        setUsername(username);
        setPassword(password);
        setFullName(fullName);
        setEmail(email);
        this.account = account;
        this.budget = null;
    }

    public User() {
        
    }


    public String getUsername() {
        return username;
    }


    /**
     * Sets the username of the user.
     *
     * @param username the given username
     */
    public void setUsername(String username) {
        if (username.contains(" ")) {
            throw new IllegalArgumentException("username cannot include space");
        } else if (username.equals("")) {
            throw new IllegalArgumentException("username is empty.");
        }
        this.username = username;
    }


    public String getPassword() {
        return password;
    }


    /**
     * Sets the password of the user.
     *
     * @param password the given password
     */
    public void setPassword(String password) {
        if (password.length() < 8) {
            throw new IllegalArgumentException("Password is too short");
        }
        this.password = password;
    }


    public String getFullName() {
        return fullName;
    }


    /**
     * Sets the full name of the user.
     *
     * @param fullName the given full name
     */
    public void setFullName(String fullName) {
        if (!fullName.contains(" ")) {
            throw new IllegalArgumentException("Full name must include a space");
        }

        this.fullName = fullName;
    }


    public String getEmail() {
        return email;
    }

    /**
     * Sets the email of the user.
     *
     * @param email the given email
     */
    public void setEmail(String email) {

        //email må inneholde "@", og kan ikke inneholde andre symboler enn de oppgitt.
        if (!email.contains("@") || !email.contains(".")) {
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

