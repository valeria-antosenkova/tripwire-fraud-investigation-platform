package com.teamcrocodile.tripwire;

import com.teamcrocodile.tripwire.controller.tripwireController;
import com.teamcrocodile.tripwire.view.MainMenuView;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class App {

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
        tripwireController controller = new tripwireController();

        controller.run();
    }
    }


