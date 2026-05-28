package com.teamcrocodile.tripwire.controller;

import com.teamcrocodile.tripwire.view.MainMenuView;

public class tripwireController {

    private MainMenuView menuView = new MainMenuView();

    public void run() {

        boolean keepGoing = true;

        while (keepGoing) {

            int userChoice = menuView.printMenuAndGetSelection();

            switch (userChoice) {

                case 1:
                    viewAllTransactions();
                    break;

                case 2:
                    viewHighRiskTransactions();
                    break;

                case 3:
                    searchTransactionById();
                    break;

                case 4:
                    updateTransactionStatus();
                    break;

                case 5:
                    deleteTransaction();
                    break;

                case 6:
                    keepGoing = false;
                    exitApplication();
                    break;

                default:
                    System.out.println("Invalid option.");
            }
        }
    }


// sql retrieval code yet to be added
    
    private void viewAllTransactions() {
        System.out.println("Viewing all transactions...");
    }

    private void viewHighRiskTransactions() {
        System.out.println("Viewing high risk transactions...");
    }

    private void searchTransactionById() {
        System.out.println("Searching transaction by ID...");
    }

    private void updateTransactionStatus() {
        System.out.println("Updating transaction status...");
    }

    private void deleteTransaction() {
        System.out.println("Deleting transaction...");
    }

    private void exitApplication() {
        System.out.println("Exiting Tripwire...");
    }
}