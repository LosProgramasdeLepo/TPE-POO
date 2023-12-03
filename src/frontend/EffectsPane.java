package frontend;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Label;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.HBox;

public class EffectsPane extends HBox {

    final Label label = new Label("Efectos:\t");
    final CheckBox shadeBox = new CheckBox("Sombra");
    final CheckBox gradientBox = new CheckBox("Gradiente");
    final CheckBox bevelBox = new CheckBox("Biselado");
    final CheckBox[] effectsArr = {shadeBox, gradientBox, bevelBox};

    public EffectsPane() {
        for (CheckBox effect : effectsArr) {
            effect.setMinWidth(90);
            effect.setCursor(Cursor.HAND);
        }
        setAlignment(Pos.CENTER);
        getChildren().add(label);
        getChildren().addAll(effectsArr);
        setPadding(new Insets(5));
        setStyle("-fx-background-color: #999");
        setPrefWidth(100);
    }



}
