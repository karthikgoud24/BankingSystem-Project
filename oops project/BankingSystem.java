import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class BankingSystem {
    public static void main(String[] args) {
        new UserAdminSelectionPage();
    }
}

class UserAdminSelectionPage {
    JFrame frame;
    JButton userButton, adminButton;

    UserAdminSelectionPage() {
        frame = new JFrame("Select User or Admin");
        frame.setSize(400, 300);
        frame.setLayout(new GridLayout(2, 1, 10, 10));

        userButton = new JButton("User");
        adminButton = new JButton("Admin");

        userButton.setBackground(new Color(144, 238, 144));  // Light Green
        adminButton.setBackground(new Color(144, 238, 144)); // Light Green
        userButton.setForeground(Color.BLACK);
        adminButton.setForeground(Color.BLACK);

        frame.add(userButton);
        frame.add(adminButton);

        userButton.addActionListener(e -> {
            new LoginPage();
            frame.dispose();
        });

        adminButton.addActionListener(e -> {
            new AdminLoginPage();
            frame.dispose();
        });

        frame.setVisible(true);
    }
}

class LoginPage {
    JFrame frame;
    JTextField userField;
    JPasswordField passField;
    JButton loginButton, registerButton, backButton;

    static HashMap<String, String> userCredentials = new HashMap<>();
    static HashMap<String, UserAccount> userAccounts = new HashMap<>();
    static ArrayList<LoanRequest> pendingLoans = new ArrayList<>();
    static ArrayList<String> pendingCards = new ArrayList<>();

    LoginPage() {
        frame = new JFrame("Login Page");
        frame.setSize(400, 300);
        frame.setLayout(null);
        frame.getContentPane().setBackground(Color.BLACK);

        JLabel userLabel = new JLabel("Username or Account No:");
        userLabel.setForeground(Color.WHITE);
        userLabel.setBounds(30, 50, 150, 30);
        frame.add(userLabel);

        userField = new JTextField();
        userField.setBounds(180, 50, 180, 30);
        frame.add(userField);

        JLabel passLabel = new JLabel("Password:");
        passLabel.setForeground(Color.WHITE);
        passLabel.setBounds(30, 100, 100, 30);
        frame.add(passLabel);

        passField = new JPasswordField();
        passField.setBounds(180, 100, 180, 30);
        frame.add(passField);

        loginButton = new JButton("Login");
        loginButton.setBounds(50, 160, 120, 30);
        loginButton.setBackground(new Color(144, 238, 144));  // Light Green
        loginButton.setForeground(Color.BLACK);
        frame.add(loginButton);

        registerButton = new JButton("Create Account");
        registerButton.setBounds(190, 160, 140, 30);
        registerButton.setBackground(new Color(144, 238, 144));  // Light Green
        registerButton.setForeground(Color.BLACK);
        frame.add(registerButton);

        backButton = new JButton("Back");
        backButton.setBounds(150, 200, 120, 30);
        backButton.setBackground(new Color(144, 238, 144));  // Light Green
        backButton.setForeground(Color.BLACK);
        frame.add(backButton);

        loginButton.addActionListener(e -> {
            String userInput = userField.getText();
            String pass = new String(passField.getPassword());
            if (userInput.equals("admin") && pass.equals("admin")) {
                new AdminPage();
                frame.dispose();
            } else {
                for (Map.Entry<String, UserAccount> entry : userAccounts.entrySet()) {
                    UserAccount acc = entry.getValue();
                    if ((acc.username.equals(userInput) || acc.accountNumber.equals(userInput))
                            && userCredentials.get(acc.username).equals(pass)) {
                        new UserPage(acc.username);
                        frame.dispose();
                        return;
                    }
                }
                JOptionPane.showMessageDialog(frame, "Invalid Credentials");
            }
        });

        registerButton.addActionListener(e -> {
            String user = userField.getText();
            String pass = new String(passField.getPassword());
            if (!userCredentials.containsKey(user)) {
                String accNum = String.format("%06d", new Random().nextInt(999999));
                UserAccount newUser = new UserAccount(user, accNum);
                userCredentials.put(user, pass);
                userAccounts.put(user, newUser);
                JOptionPane.showMessageDialog(frame, "Account Created\nUsername: " + user + "\nAccount No: " + accNum);
            } else {
                JOptionPane.showMessageDialog(frame, "User already exists");
            }
        });

        backButton.addActionListener(e -> {
            new UserAdminSelectionPage();
            frame.dispose();
        });

        frame.setVisible(true);
    }
}

class AdminLoginPage {
    JFrame frame;
    JTextField userField;
    JPasswordField passField;
    JButton loginButton, backButton;

    AdminLoginPage() {
        frame = new JFrame("Admin Login");
        frame.setSize(400, 300);
        frame.setLayout(null);
        frame.getContentPane().setBackground(Color.BLACK);

        JLabel userLabel = new JLabel("Admin Username:");
        userLabel.setForeground(Color.WHITE);
        userLabel.setBounds(30, 50, 150, 30);
        frame.add(userLabel);

        userField = new JTextField();
        userField.setBounds(180, 50, 180, 30);
        frame.add(userField);

        JLabel passLabel = new JLabel("Password:");
        passLabel.setForeground(Color.WHITE);
        passLabel.setBounds(30, 100, 100, 30);
        frame.add(passLabel);

        passField = new JPasswordField();
        passField.setBounds(180, 100, 180, 30);
        frame.add(passField);

        loginButton = new JButton("Login");
        loginButton.setBounds(50, 160, 120, 30);
        loginButton.setBackground(new Color(144, 238, 144));  // Light Green
        loginButton.setForeground(Color.BLACK);
        frame.add(loginButton);

        backButton = new JButton("Back");
        backButton.setBounds(190, 160, 120, 30);
        backButton.setBackground(new Color(144, 238, 144));  // Light Green
        backButton.setForeground(Color.BLACK);
        frame.add(backButton);

        loginButton.addActionListener(e -> {
            String pass = new String(passField.getPassword());
            if (userField.getText().equals("admin") && pass.equals("admin")) {
                new AdminPage();
                frame.dispose();
            } else {
                JOptionPane.showMessageDialog(frame, "Invalid Admin Credentials");
            }
        });

        backButton.addActionListener(e -> {
            new UserAdminSelectionPage();
            frame.dispose();
        });

        frame.setVisible(true);
    }
}

class AdminPage {
    JFrame frame;

    AdminPage() {
        frame = new JFrame("Admin Page");
        frame.setSize(500, 500);
        frame.setLayout(new GridLayout(6, 1));

        JButton approveLoanBtn = createButtonWithIcon("Approve Loans", "images/approve_loans.png");
        JButton approveCardBtn = createButtonWithIcon("Approve Credit Cards", "images/approve_credit_cards.png");
        JButton showAccountsBtn = createButtonWithIcon("Show All Accounts", "images/show_all_accounts.png");
        JButton chatBtn = createButtonWithIcon("Chat with Users", "images/chat_with_user.png");
        JButton logoutBtn = createButtonWithIcon("Logout", "images/logout.png");

        frame.add(approveLoanBtn);
        frame.add(approveCardBtn);
        frame.add(showAccountsBtn);
        frame.add(chatBtn);
        frame.add(logoutBtn);

        approveLoanBtn.addActionListener(e -> {
            if (!LoginPage.pendingLoans.isEmpty()) {
                LoanRequest req = LoginPage.pendingLoans.remove(0);
                UserAccount acc = LoginPage.userAccounts.get(req.username);
                int approval = JOptionPane.showConfirmDialog(frame, "Approve loan of Rs. " + req.amount + " for " + req.username + "?", "Loan Approval", JOptionPane.YES_NO_OPTION);
                if (approval == JOptionPane.YES_OPTION) {
                    acc.deposit(req.amount);
                    acc.transactions.add("Loan Approved: Rs. " + req.amount);
                    JOptionPane.showMessageDialog(frame, "Loan Approved");
                }
            } else {
                JOptionPane.showMessageDialog(frame, "No Loan Requests");
            }
        });

        approveCardBtn.addActionListener(e -> {
            if (!LoginPage.pendingCards.isEmpty()) {
                String user = LoginPage.pendingCards.remove(0);
                UserAccount acc = LoginPage.userAccounts.get(user);
                acc.setCreditCard(new CreditCard(acc.username));
                acc.transactions.add("Credit Card Approved");
                JOptionPane.showMessageDialog(frame, "Credit Card Approved for " + user);
            } else {
                JOptionPane.showMessageDialog(frame, "No Credit Card Requests");
            }
        });

        showAccountsBtn.addActionListener(e -> {
            StringBuilder sb = new StringBuilder();
            for (UserAccount acc : LoginPage.userAccounts.values()) {
                sb.append("User: ").append(acc.username)
                        .append(", Account No: ").append(acc.accountNumber)
                        .append(", Balance: Rs. ").append(acc.getBalance()).append("\n");
            }
            JOptionPane.showMessageDialog(frame, sb.toString());
        });

        chatBtn.addActionListener(e -> {
            String message = JOptionPane.showInputDialog(frame, "Enter message to send to users:");
            if (message != null) {
                JOptionPane.showMessageDialog(frame, "Message sent to all users: " + message);
            }
        });

        logoutBtn.addActionListener(e -> {
            new UserAdminSelectionPage();
            frame.dispose();
        });

        frame.setVisible(true);
    }

    private JButton createButtonWithIcon(String text, String imagePath) {
        JButton button = new JButton(text);
        ImageIcon icon = new ImageIcon(imagePath);
        Image img = icon.getImage().getScaledInstance(64, 64, Image.SCALE_SMOOTH);
        icon = new ImageIcon(img);
        button.setIcon(icon);
        button.setHorizontalTextPosition(SwingConstants.RIGHT);
        button.setVerticalTextPosition(SwingConstants.CENTER);
        button.setPreferredSize(new Dimension(200, 100));
        button.setBackground(new Color(144, 238, 144));  // Light Green
        button.setForeground(Color.BLACK);
        return button;
    }
}

class UserPage {
    JFrame frame;
    String username;

    UserPage(String username) {
        this.username = username;
        frame = new JFrame("User Page");
        frame.setSize(500, 1000);
        frame.setLayout(new GridLayout(8, 1, 5, 5));

        frame.add(createRow("Deposit", "images/deposit.png", "Deposit"));
        frame.add(createRow("Withdraw", "images/withdraw.png", "Withdraw"));
        frame.add(createRow("Check Balance", "images/check_balance.png", "Balance"));
        frame.add(createRow("Transaction History", "images/transaction_history.png", "History"));
        frame.add(createRow("Transfer Funds", "images/transfer_funds.png", "Transfer"));
        frame.add(createRow("Apply for Loan", "images/apply_for_loan.png", "Loan"));
        frame.add(createRow("Apply for Credit Card", "images/apply_for_credit_card.png", "Credit Card"));
        frame.add(createRow("Logout", "images/logout.png", "Logout"));

        frame.setVisible(true);
    }

    private JPanel createRow(String text, String imagePath, String action) {
        JPanel panel = new JPanel(new BorderLayout());
        JButton button = new JButton(text);
        ImageIcon icon = new ImageIcon(imagePath);
        Image img = icon.getImage().getScaledInstance(64, 64, Image.SCALE_SMOOTH);
        icon = new ImageIcon(img);
        button.setIcon(icon);
        button.setHorizontalTextPosition(SwingConstants.RIGHT);
        button.setVerticalTextPosition(SwingConstants.CENTER);

        button.setBackground(new Color(144, 238, 144));  // Light Green
        button.setForeground(Color.BLACK);
        panel.add(button, BorderLayout.CENTER);
        button.addActionListener(e -> handleAction(action));

        return panel;
    }

    private void handleAction(String action) {
        UserAccount acc = LoginPage.userAccounts.get(username);

        switch (action) {
            case "Deposit" -> {
                String amountStr = JOptionPane.showInputDialog(frame, "Enter deposit amount:");
                double amount = Double.parseDouble(amountStr);
                acc.deposit(amount);
                JOptionPane.showMessageDialog(frame, "Rs. " + amount + " deposited.");
            }
            case "Withdraw" -> {
                String amountStr = JOptionPane.showInputDialog(frame, "Enter withdrawal amount:");
                double amount = Double.parseDouble(amountStr);
                if (acc.withdraw(amount)) {
                    JOptionPane.showMessageDialog(frame, "Rs. " + amount + " withdrawn.");
                } else {
                    JOptionPane.showMessageDialog(frame, "Insufficient funds.");
                }
            }
            case "Balance" -> {
                JOptionPane.showMessageDialog(frame, "Balance: Rs. " + acc.getBalance());
            }
            case "History" -> {
                JOptionPane.showMessageDialog(frame, "Transaction History:\n" + acc.getTransactionHistory());
            }
            case "Transfer" -> {
                String recipientName = JOptionPane.showInputDialog(frame, "Enter recipient's username or account number:");
                UserAccount recipient = LoginPage.userAccounts.get(recipientName);
                if (recipient != null) {
                    String amountStr = JOptionPane.showInputDialog(frame, "Enter transfer amount:");
                    double amount = Double.parseDouble(amountStr);
                    if (acc.transfer(amount, recipient)) {
                        JOptionPane.showMessageDialog(frame, "Rs. " + amount + " transferred.");
                    } else {
                        JOptionPane.showMessageDialog(frame, "Transfer failed.");
                    }
                } else {
                    JOptionPane.showMessageDialog(frame, "Recipient not found.");
                }
            }
            case "Loan" -> {
                String loanAmount = JOptionPane.showInputDialog(frame, "Enter loan amount:");
                acc.applyForLoan(Double.parseDouble(loanAmount));
                JOptionPane.showMessageDialog(frame, "Loan application submitted.");
            }
            case "Credit Card" -> {
                String cardType = JOptionPane.showInputDialog(frame, "Select Card Type: Normal, Pro, or Premium");
                acc.applyForCreditCard(cardType);
                JOptionPane.showMessageDialog(frame, "Credit Card application submitted for " + cardType);
            }
            case "Logout" -> {
                new UserAdminSelectionPage();
                frame.dispose();
            }
        }
    }
}

class UserAccount {
    String username;
    String accountNumber;
    double balance;
    ArrayList<String> transactions;
    CreditCard creditCard;
    LoanRequest loan;

    UserAccount(String username, String accountNumber) {
        this.username = username;
        this.accountNumber = accountNumber;
        this.balance = 0;
        this.transactions = new ArrayList<>();
        this.creditCard = null;
        this.loan = null;
    }

    void deposit(double amount) {
        balance += amount;
        transactions.add("Deposited: Rs. " + amount);
    }

    boolean withdraw(double amount) {
        if (balance >= amount) {
            balance -= amount;
            transactions.add("Withdrew: Rs. " + amount);
            return true;
        }
        return false;
    }

    double getBalance() {
        return balance;
    }

    String getTransactionHistory() {
        StringBuilder history = new StringBuilder();
        for (String trans : transactions) {
            history.append(trans).append("\n");
        }
        return history.toString();
    }

    boolean transfer(double amount, UserAccount recipient) {
        if (withdraw(amount)) {
            recipient.deposit(amount);
            transactions.add("Transferred Rs. " + amount + " to " + recipient.username);
            recipient.transactions.add("Received Rs. " + amount + " from " + username);
            return true;
        }
        return false;
    }

    void applyForCreditCard(String cardType) {
        // Logic to apply for credit card based on cardType (Normal, Pro, Premium)
        transactions.add("Applied for " + cardType + " Credit Card");
    }

    void applyForLoan(double amount) {
        loan = new LoanRequest(username, amount);
        transactions.add("Applied for loan of Rs. " + amount);
        LoginPage.pendingLoans.add(loan);
    }

    void setCreditCard(CreditCard card) {
        this.creditCard = card;
    }
}

class CreditCard {
    String type;
    boolean active;

    CreditCard(String type) {
        this.type = type;
        this.active = true;
    }
}

class LoanRequest {
    String username;
    double amount;

    LoanRequest(String username, double amount) {
        this.username = username;
        this.amount = amount;
    }
}
