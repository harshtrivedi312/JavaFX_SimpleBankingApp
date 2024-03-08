package ATMApplication;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ATMGUI extends Application {
    private List<Customer> customers = new ArrayList<>();
    private ATM atm;
    private Stage primaryStage;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        showMainScreen();
    }

    private void showMainScreen() {
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10));
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        Label titleLabel = new Label("My Teller Application");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        GridPane.setConstraints(titleLabel, 0, 0, 2, 1);
        GridPane.setMargin(titleLabel, new Insets(0, 0, 10, 0));

        Button existingCustomerButton = new Button("Existing Customer");
        Button newCustomerButton = new Button("New Customer");

        GridPane.setConstraints(existingCustomerButton, 0, 1);
        GridPane.setConstraints(newCustomerButton, 1, 1);

        gridPane.getChildren().addAll(titleLabel, existingCustomerButton, newCustomerButton);

        existingCustomerButton.setOnAction(e -> showExistingCustomerScreen());
        newCustomerButton.setOnAction(e -> showNewCustomerScreen());

        Scene scene = new Scene(gridPane, 800, 500);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Simple Bank Application");
        primaryStage.show();
    }

    private void showExistingCustomerScreen() {
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10));
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        Label titleLabel = new Label("Select an Existing Customer:");
        GridPane.setConstraints(titleLabel, 0, 0, 2, 1);

        ListView<Customer> listView = new ListView<>();
        listView.getItems().addAll(customers);
        listView.setPrefWidth(700);
        listView.setPrefHeight(400);
        listView.setCellFactory(param -> new ListCell<Customer>() {
            @Override
            protected void updateItem(Customer customer, boolean empty) {
                super.updateItem(customer, empty);
                if (empty || customer == null) {
                    setText(null);
                } else {
                    setText(customer.getName());
                }
            }
        });
        GridPane.setConstraints(listView, 0, 1, 2, 1);

        Button backButton = new Button("Back");
        GridPane.setConstraints(backButton, 0, 2);

        Button selectButton = new Button("Select");
        GridPane.setConstraints(selectButton, 1, 2);

        Label customerDetailsLabel = new Label("Customer Details:");
        GridPane.setConstraints(customerDetailsLabel, 0, 3, 2, 1);

        TextArea customerDetailsTextArea = new TextArea();
        customerDetailsTextArea.setEditable(false);
        customerDetailsTextArea.setPrefWidth(700);
        customerDetailsTextArea.setPrefHeight(400);
        GridPane.setConstraints(customerDetailsTextArea, 0, 4, 2, 1);

        Button depositButton = new Button("Deposit");
        GridPane.setConstraints(depositButton, 0, 5);

        Button withdrawButton = new Button("Withdraw");
        GridPane.setConstraints(withdrawButton, 1, 5);

        Button checkBalanceButton = new Button("Check Balance");
        GridPane.setConstraints(checkBalanceButton, 0, 6, 2, 1);

        gridPane.getChildren().addAll(titleLabel, listView, backButton, selectButton, customerDetailsLabel,
                customerDetailsTextArea, depositButton, withdrawButton, checkBalanceButton);

        backButton.setOnAction(e -> showMainScreen());

        selectButton.setOnAction(e -> {
            Customer selectedCustomer = listView.getSelectionModel().getSelectedItem();
            if (selectedCustomer != null) {
                TextInputDialog pinDialog = new TextInputDialog();
                pinDialog.setTitle("Enter PIN");
                pinDialog.setHeaderText("Enter the PIN for " + selectedCustomer.getName());
                pinDialog.setContentText("PIN:");

                Optional<String> result = pinDialog.showAndWait();
                result.ifPresent(pin -> {
                    if (Integer.parseInt(pin) == selectedCustomer.getPin()) {
                        // Display customer details
                        customerDetailsTextArea.setText("Name: " + selectedCustomer.getName() + "\nAccount Number: "
                                + selectedCustomer.getAccountNumber() + "\nBalance: " + selectedCustomer.getBalance());
                    } else {
                        showAlert(Alert.AlertType.ERROR, "Invalid PIN", "Incorrect PIN entered.");
                    }
                });
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Please select a customer.");
            }
        });


        depositButton.setOnAction(e -> {
            Customer selectedCustomer = listView.getSelectionModel().getSelectedItem();
            if (selectedCustomer != null) {
                TextInputDialog dialog = new TextInputDialog();
                dialog.setTitle("Deposit");
                dialog.setHeaderText("Enter the amount to deposit:");
                dialog.setContentText("Amount:");

                Optional<String> result = dialog.showAndWait();
                result.ifPresent(amount -> {
                    try {
                        double depositAmount = Double.parseDouble(amount);
                        selectedCustomer.setBalance(selectedCustomer.getBalance() + depositAmount);
                        customerDetailsTextArea.setText("Name: " + selectedCustomer.getName() + "\nAccount Number: "
                                + selectedCustomer.getAccountNumber() + "\nBalance: " + selectedCustomer.getBalance());
                        showAlert(Alert.AlertType.INFORMATION, "Success", "Deposit successful.");
                    } catch (NumberFormatException ex) {
                        showAlert(Alert.AlertType.ERROR, "Error", "Invalid amount entered.");
                    }
                });
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Please select a customer.");
            }
        });

        withdrawButton.setOnAction(e -> {
            Customer selectedCustomer = listView.getSelectionModel().getSelectedItem();
            if (selectedCustomer != null) {
                TextInputDialog dialog = new TextInputDialog();
                dialog.setTitle("Withdraw");
                dialog.setHeaderText("Enter the amount to withdraw:");
                dialog.setContentText("Amount:");

                Optional<String> result = dialog.showAndWait();
                result.ifPresent(amount -> {
                    try {
                        double withdrawAmount = Double.parseDouble(amount);
                        if (selectedCustomer.getBalance() >= withdrawAmount) {
                            selectedCustomer.setBalance(selectedCustomer.getBalance() - withdrawAmount);
                            customerDetailsTextArea.setText("Name: " + selectedCustomer.getName() + "\nAccount Number: "
                                    + selectedCustomer.getAccountNumber() + "\nBalance: " + selectedCustomer.getBalance());
                            showAlert(Alert.AlertType.INFORMATION, "Success", "Withdrawal successful.");
                        } else {
                            showAlert(Alert.AlertType.ERROR, "Error", "Insufficient balance.");
                        }
                    } catch (NumberFormatException ex) {
                        showAlert(Alert.AlertType.ERROR, "Error", "Invalid amount entered.");
                    }
                });
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Please select a customer.");
            }
        });

        checkBalanceButton.setOnAction(e -> {
            Customer selectedCustomer = listView.getSelectionModel().getSelectedItem();
            if (selectedCustomer != null) {
                customerDetailsTextArea.setText("Name: " + selectedCustomer.getName() + "\nCurrent Balance: " + selectedCustomer.getBalance());
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Please select a customer.");
            }
        });

        Scene scene = new Scene(gridPane, 800, 500);
        primaryStage.setScene(scene);
    }



    private void showCustomerOptions(Customer customer) {
        Dialog<String> optionsDialog = new Dialog<>();
        optionsDialog.setTitle("Customer Details");
        optionsDialog.setHeaderText("Hello: " + customer.getName() + "\nCurrent Balance: " + customer.getBalance());

        ButtonType depositButton = new ButtonType("Deposit", ButtonBar.ButtonData.OK_DONE);
        ButtonType withdrawButton = new ButtonType("Withdraw", ButtonBar.ButtonData.OK_DONE);
        ButtonType balanceButton = new ButtonType("Check Balance", ButtonBar.ButtonData.OK_DONE);

        optionsDialog.getDialogPane().getButtonTypes().addAll(depositButton, withdrawButton, balanceButton);

        optionsDialog.setResultConverter(buttonType -> {
            if (buttonType == depositButton) {
                // Handle Deposit
                // You can implement the deposit functionality here
                return "Deposit success";
            } else if (buttonType == withdrawButton) {
                // Handle Withdraw
                // You can implement the withdrawal functionality here
                return "Withdraw";
            } else if (buttonType == balanceButton) {
                // Handle Check Balance
                // You can implement the check balance functionality here
                return "Check Balance";
            }
            return null;
        });

        Optional<String> result = optionsDialog.showAndWait();
        result.ifPresent(action -> {
            // Perform action based on user choice
            if ("Deposit".equals(action)) {
                // Perform Deposit action
            } else if ("Withdraw".equals(action)) {
                // Perform Withdraw action
            } else if ("Check Balance".equals(action)) {
                // Perform Check Balance action
            }
        });
    }


    private void showNewCustomerScreen() {
        GridPane gridPane = new GridPane();
        gridPane.setPadding(new Insets(10));
        gridPane.setHgap(10);
        gridPane.setVgap(10);

        Button backButton = new Button("Back");
        gridPane.add(backButton, 0, 0);

        Label nameLabel = new Label("Name:");
        TextField nameField = new TextField();
        nameField.setPromptText("Name");

        Label balanceLabel = new Label("Balance:");
        TextField balanceField = new TextField();
        balanceField.setPromptText("Balance");

        Label pinLabel = new Label("PIN:");
        PasswordField pinField = new PasswordField();
        pinField.setPromptText("PIN");

        Button submitButton = new Button("Submit");

        gridPane.add(nameLabel, 0, 1);
        gridPane.add(nameField, 1, 1);
        gridPane.add(balanceLabel, 0, 2);
        gridPane.add(balanceField, 1, 2);
        gridPane.add(pinLabel, 0, 3);
        gridPane.add(pinField, 1, 3);
        gridPane.add(submitButton, 1, 4);

        backButton.setOnAction(e -> showMainScreen());

        submitButton.setOnAction(e -> {
            try {
                String name = nameField.getText();
                double balance = Double.parseDouble(balanceField.getText());
                int pin = Integer.parseInt(pinField.getText());
                Customer customer = new Customer(name, balance, pin);
                customers.add(customer);
                atm = new ATM(customer);
                showAlert(Alert.AlertType.INFORMATION, "Success", "New customer added: " + customer.getName());
                showMainScreen(); // Navigate back to the home screen
            } catch (NumberFormatException ex) {
                showAlert(Alert.AlertType.ERROR, "Error", "Please enter valid information.");
                ex.printStackTrace(); // Print stack trace for debugging
            }
        });

        Scene scene = new Scene(gridPane, 800, 500);
        primaryStage.setScene(scene);
    }

    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

}
