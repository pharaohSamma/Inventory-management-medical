package com.mypharmacy.controllers;

import com.mypharmacy.dao.CategoryDAO;
import com.mypharmacy.models.Category;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.Alert;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CategoryController {
    @FXML
    private TableView<Category> categoryTableView;
    @FXML
    private TableColumn<Category, Integer> categoryIdColumn;
    @FXML
    private TableColumn<Category, String> categoryNameColumn;
    @FXML
    private TableColumn<Category, String> categoryDescriptionColumn;
    @FXML
    private TableColumn<Category, String> categoryEditColumn;
    @FXML
    private TableColumn<Category, String> categoryDeleteColumn;
    @FXML
    private TextField categoryName;
    @FXML
    private TextField categoryDescription;
    private CategoryDAO categoryDAO = new CategoryDAO();
    private List<Category> listOfCategories = new ArrayList<Category>();


    public CategoryController(Category category) {
        if (category != null) {
            this.categoryName.setText(category.getCategoryName());
            this.categoryDescription.setText(category.getCategoryDescription());
        }
    }

    public CategoryController() {
    }
    public CategoryController(TextField categoryName, TextField categoryDescription, CategoryDAO categoryDAO) {
        this.categoryName = categoryName;
        this.categoryDescription = categoryDescription;
        this.categoryDAO = categoryDAO;
    }



//GETTERS AND SETTERS
    public List<Category> getListOfCategories() {
        return listOfCategories;
    }
    public void setListOfCategories(List<Category> listOfCategories) {
        this.listOfCategories = listOfCategories;
    }
//GETTERS AND SETTERS



//ACTION METHODS
    @FXML
    private void addButtonClicked() {
    String categName = categoryName.getText().trim();
    String categDescription = categoryDescription.getText().trim();
    if (!categName.isEmpty() && !categDescription.isEmpty()) {
        Category category = new Category();
        category.setCategoryName(categName);
        category.setCategoryDescription(categDescription);
        addCategory(category);
        CategoryDAO categ = new CategoryDAO();
        listOfCategories = categ.getAllCategories();
        showCategoriesInTable();
    } else {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText("Le nom / La description de la catégorie ne peuvent pas être vides.");
        alert.showAndWait();
    }
}
    @FXML
    private void initialize() {
        categoryDAO = new CategoryDAO();
        initializeTableView();
        showCategoriesInTable();
    }
//ACTION METHODS

    private void initializeTableView() {
        // Initialize columns
        categoryIdColumn.setCellValueFactory(new PropertyValueFactory<>("categoryId"));
        categoryNameColumn.setCellValueFactory(new PropertyValueFactory<>("categoryName"));
        categoryDescriptionColumn.setCellValueFactory(new PropertyValueFactory<>("categoryDescription"));
        categoryEditColumn.setCellFactory(column -> {
            return new TableCell<Category, String>() {
                private final Button updateButton = new Button("Modifier");

                {
                    updateButton.setStyle("-fx-background-color: #007bff; -fx-text-fill: white; -fx-font-weight: bold;");
                    updateButton.setOnAction(event -> {
                        Category category = getTableView().getItems().get(getIndex());

                        // Create a dialog for updating the category
                        Dialog<Category> dialog = new Dialog<>();
                        dialog.setTitle("Modifier la categorie : ");
                        dialog.setHeaderText("Modifier les détails de la catégorie : " + category.getCategoryName());

                        // Set the button types
                        ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
                        dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

                        // Create and add text fields for category details
                        TextField categoryNameField = new TextField(category.getCategoryName());
                        TextField categoryDescriptionField = new TextField(category.getCategoryDescription());

                        GridPane grid = new GridPane();
                        grid.add(new Label("Nom de categorie :"), 0, 0);
                        grid.add(categoryNameField, 1, 0);
                        grid.add(new Label("Description :"), 0, 1);
                        grid.add(categoryDescriptionField, 1, 1);
                        dialog.getDialogPane().setContent(grid);

                        // Convert the result to a category when the OK button is clicked
                        dialog.setResultConverter(dialogButton -> {
                            if (dialogButton == okButtonType) {
                                if (categoryNameField.getText().isEmpty() || categoryDescriptionField.getText().isEmpty()) {
                                    Alert alert = new Alert(Alert.AlertType.WARNING);
                                    alert.setTitle("Erreur de modification");
                                    alert.setContentText("Merci de remplir tous les champs.");
                                    alert.showAndWait();
                                    return null;
                                }
                                return new Category(categoryNameField.getText(), categoryDescriptionField.getText());
                            }
                            return null;
                        });

                        Optional<Category> result = dialog.showAndWait();

                        result.ifPresent(updatedCategory -> {
                            // Show confirmation dialog for updating the category
                            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
                            confirmationAlert.setTitle("Comfirmez la modification ");
                            confirmationAlert.setContentText("Êtes-vous sûr de vouloir mettre à jour la catégorie ?");

                            Optional<ButtonType> confirmationResult = confirmationAlert.showAndWait();
                            confirmationResult.ifPresent(buttonType -> {
                                if (buttonType == ButtonType.OK) {
                                    // User clicked OK, perform update action
                                    category.setCategoryName(updatedCategory.getCategoryName());
                                    category.setCategoryDescription(updatedCategory.getCategoryDescription());
                                    categoryDAO.updateCategory(category);
                                    listOfCategories = categoryDAO.getAllCategories();
                                    showCategoriesInTable();
                                }
                            });
                        });
                    });


                }

                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        setGraphic(updateButton);
                        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                        setAlignment(Pos.CENTER); // Center align the button
                    }
                }
            };
        });

        categoryDeleteColumn.setCellFactory(column -> {
            return new TableCell<Category, String>() {
                private final Button deleteButton = new Button("Supprimer");
                {
                    deleteButton.setStyle("-fx-background-color: #FF4D00; -fx-text-fill: white; -fx-font-weight: bold;");
                    deleteButton.setOnAction(event -> {
                        Category category = getTableView().getItems().get(getIndex());
                        // Show confirmation dialog
                        Alert alert = new Alert(Alert.AlertType.WARNING);
                        alert.setTitle("Comfirmez la supression");
                        alert.setHeaderText("Êtes-vous sûr de vouloir supprimer la catégorie " + category.getCategoryName().toUpperCase() + " ?");

                        ButtonType okButton = new ButtonType("Supprimmer", ButtonBar.ButtonData.OK_DONE);
                        ButtonType cancelButton = new ButtonType("Annuler", ButtonBar.ButtonData.CANCEL_CLOSE);

                        alert.getButtonTypes().setAll(okButton, cancelButton);

                        Optional<ButtonType> result = alert.showAndWait();

                        if (result.isPresent() && result.get() == okButton) {
                            // User clicked OK, perform delete action
                            categoryDAO.deleteCategory(category);
                            listOfCategories = categoryDAO.getAllCategories();
                            showCategoriesInTable();
                        }
                    });
                }

                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty) {
                        setGraphic(null);
                    } else {
                        setGraphic(deleteButton);
                        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
                        setAlignment(Pos.CENTER); // Center align the button
                    }
                }
            };
        });
    }
    private void showCategoriesInTable() {
        List<Category> categoryList = FXCollections.observableArrayList(categoryDAO.getAllCategories());
        categoryTableView.setItems((ObservableList<Category>) categoryList);
    }
    private void addCategory(Category category) {
        categoryDAO.addCategory(category);
    }
}
