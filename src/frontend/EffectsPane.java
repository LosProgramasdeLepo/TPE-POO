package frontend;

import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

import java.awt.*;

public class EffectsPane extends BorderPane {

    CheckBox shadeBox = new CheckBox("Sombra");
    CheckBox gradientBox = new CheckBox("Gradiente");
    CheckBox bevelBox = new CheckBox("Biselado");

    public EffectsPane() {
        CheckBox[] effectsArr = {shadeBox, gradientBox, bevelBox};
        CheckboxGroup effects = new CheckboxGroup();
        for (CheckBox effect : effectsArr) {
            effect.setMinWidth(90);
            effect.setCursor(Cursor.HAND);
        }
        HBox effectsBox = new HBox(20);
        effectsBox.getChildren().addAll(effectsArr);
        effectsBox.setPadding(new Insets(5));
        effectsBox.setStyle("-fx-background-color: #999");
        effectsBox.setPrefWidth(100);
    }

}
