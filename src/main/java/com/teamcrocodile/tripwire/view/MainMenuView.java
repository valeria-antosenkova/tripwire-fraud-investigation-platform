package com.teamcrocodile.tripwire.view;

public class MainMenuView {
    private UserIO io = new UserIOConsoleImpl();

    public int printMenuAndGetSelection() {

        io.print("========================================");
        io.print("           TRIPWIRE SYSTEM");
        io.print("      Refund Fraud Detection Queue");
        io.print("========================================");
        io.print("");
        io.print("1. View All Refund Cases");
        io.print("2. View High Risk Cases");
        io.print("3. Search Refund by ID");
        io.print("4. Process Refund Decision");
        io.print("5. Exit");
        io.print("");

        return io.readInt("Select Option:");
    }
}
