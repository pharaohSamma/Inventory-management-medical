package com.mypharmacy;

import javafx.fxml.FXML;
import javafx.scene.control.TableView;

public class ApplicationController {
    @FXML
    private TableView<?> categoryTableView;

    @FXML
    private void goToHomePage() {
        // Logic to switch to the home page
        System.out.println("Navigating to the home page...");
    }

    @FXML
    private void goToCategoryTable() {
        // Logic to switch to the category table page
        System.out.println("Navigating to the category table...");
    }

    @FXML
    private void goToAnotherPage() {
        // Logic to switch to another page
        System.out.println("Navigating to another page...");
    }
}