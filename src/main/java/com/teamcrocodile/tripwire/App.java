package com.teamcrocodile.tripwire;

import com.teamcrocodile.tripwire.view.MainMenuView;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class App {

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);

        MainMenuView view = new MainMenuView();

        int userChoice = view.printMenuAndGetSelection();

        System.out.println("You selected option: " + userChoice);
    }
    }


