import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class BankingSystem {
    private static List<User> users = new ArrayList<>();
    private static User currentUser = null;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new BankingSystem().createAndShow());
    }

    private void createAndShow() {
        JFrame frame = new JFrame("Banking System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new CardLayout());

        JPanel loginPanel = new JPanel(new GridLayout(4, 2));
        JPanel mainPanel = new JPanel(new GridLayout(5, 1));

        // Login Panel Components
        JTextField usernameField = new JTextField();
        JPasswordField passwordField = new JPasswordField();
        JButton loginButton = new JButton("Login");
        JButton registerButton = new JButton("Register");

        loginPanel.add(new JLabel("Username:"));
        loginPanel.add(usernameField);
        loginPanel.add(new JLabel("Password:"));
        loginPanel.add(passwordField);
        loginPanel.add(loginButton);
        loginPanel.add(registerButton);

        // Main Panel Components
        JButton depositButton = new JButton("Deposit");
        JButton withdrawButton = new JButton("Withdraw");
        JButton balanceButton = new JButton("Check Balance");
        JButton transactionsButton = new JButton("View Transactions");
        JButton logoutButton = new JButton("Logout");

        mainPanel.add(depositButton);
        mainPanel.add(withdrawButton);
        mainPanel.add(balanceButton);
        mainPanel.add(transactionsButton);
        mainPanel.add(logoutButton);

        frame.add(loginPanel, "Login");
        frame.add(mainPanel, "Main");

        CardLayout cl = (CardLayout) (frame.getContentPane().getLayout());

        // Action Listeners
        loginButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            User user = findUser(username);
            if (user != null && user.getPassword().equals(password)) {
                currentUser = user;
                cl.show(frame.getContentPane(), "Main");
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid username or password.");
            }
        });

        registerButton.addActionListener(e -> {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            if (findUser(username) != null) {
                JOptionPane.showMessageDialog(frame, "Username already exists.");
            } else {
                User newUser = new User(username, password);
                users.add(newUser);
                JOptionPane.showMessageDialog(frame, "Registration successful! Your account number is " + newUser.getAccountNumber());
            }
        });

        depositButton.addActionListener(e -> {
            String amountStr = JOptionPane.showInputDialog(frame, "Enter amount to deposit:");
            try {
                double amount = Double.parseDouble(amountStr);
                currentUser.deposit(amount);
                JOptionPane.showMessageDialog(frame, "Deposit successful.");
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Invalid amount.");
            }
        });

        withdrawButton.addActionListener(e -> {
            String amountStr = JOptionPane.showInputDialog(frame, "Enter amount to withdraw:");
            try {
                double amount = Double.parseDouble(amountStr);
                if (currentUser.withdraw(amount)) {
                    JOptionPane.showMessageDialog(frame, "Withdrawal successful.");
                } else {
                    JOptionPane.showMessageDialog(frame, "Withdrawal failed: Insufficient funds.");
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Invalid amount.");
            }
        });

        balanceButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(frame, "Current Balance: " + currentUser.getBalance());
        });

        transactionsButton.addActionListener(e -> {
            StringBuilder transactions = new StringBuilder("Transaction History:\n");
            for (String transaction : currentUser.getTransactions()) {
                transactions.append(transaction).append("\n");
            }
            JOptionPane.showMessageDialog(frame, transactions.toString());
        });

        logoutButton.addActionListener(e -> {
            currentUser = null;
            cl.show(frame.getContentPane(), "Login");
        });

        frame.setVisible(true);
    }

    private User findUser(String username) {
        for (User user : users) {
            if (user.getUsername().equals(username)) {
                return user;
            }
        }
        return null;
    }
}

class User {
    private static int accountCounter = 1000;
    private String username;
    private String password;
    private String accountNumber;
    private double balance;
    private List<String> transactions;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.accountNumber = "ACC" + (++accountCounter);
        this.balance = 0.0;
        this.transactions = new ArrayList<>();
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public double getBalance() {
        return balance;
    }

    public void deposit(double amount) {
        balance += amount;
        transactions.add("Deposited: " + amount);
    }

    public boolean withdraw(double amount) {
        if (amount > balance) {
            transactions.add("Failed Withdrawal: " + amount);
            return false;
        } else {
            balance -= amount;
            transactions.add("Withdrew: " + amount);
            return true;
        }
    }

    public List<String> getTransactions() {
        return transactions;
    }
}