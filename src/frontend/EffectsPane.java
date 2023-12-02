package frontend;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;

public class EffectsPane extends BorderPane {

    final Label label = new Label("Efectos:");
    final CheckBox shadeBox = new CheckBox("Sombra");
    final CheckBox gradientBox = new CheckBox("Gradiente");
    final CheckBox bevelBox = new CheckBox("Biselado");
    final CheckBox[] effectsArr = {shadeBox, gradientBox, bevelBox};

    public EffectsPane() {
        for (CheckBox effect : effectsArr) {
            effect.setMinWidth(90);
            effect.setCursor(Cursor.HAND);
        }
        HBox effectsBox = new HBox(20);
        effectsBox.getChildren().add(label);
        effectsBox.getChildren().addAll(effectsArr);
        effectsBox.setAlignment(Pos.CENTER);
        effectsBox.setPadding(new Insets(5));
        effectsBox.setStyle("-fx-background-color: #999");
        effectsBox.setPrefWidth(100);
    }

}
