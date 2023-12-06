package frontend;

import javafx.scene.control.*;

import java.util.Optional;

public class AppMenuBar extends MenuBar {

    public AppMenuBar() {
        Menu file = new Menu("Archivo");
        MenuItem exitMenuItem = getMenuItem();

        file.getItems().add(exitMenuItem);
        Menu help = new Menu("Ayuda");
        MenuItem aboutMenuItem = new MenuItem("Acerca De");
        aboutMenuItem.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Acerca De");
            alert.setHeaderText("Paint");
            alert.setContentText("TPE Final POO Diciembre 2023");
            alert.showAndWait();
        });

        help.getItems().add(aboutMenuItem);
        getMenus().addAll(file, help);
    }

    private static MenuItem getMenuItem() {
        MenuItem exitMenuItem = new MenuItem("Salir");
        exitMenuItem.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Salir");
            alert.setHeaderText("Salir de la aplicación");
            alert.setContentText("¿Está seguro que desea salir de la aplicación?");
            Optional<ButtonType> result = alert.showAndWait();
            if(result.isPresent()) {
                if (result.get() == ButtonType.OK) {
                    System.exit(0);
                }
            }
        });
        return exitMenuItem;
    }

}
