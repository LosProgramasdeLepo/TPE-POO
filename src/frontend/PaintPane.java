package frontend;

import backend.CanvasState;
import backend.FigureGroups;
import backend.FigureSelection;
import backend.model.Figure;
import backend.model.Point;
import backend.model.Rectangle;
import frontend.figureButtons.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class PaintPane extends BorderPane {

	// BackEnd
	CanvasState canvasState;

	// Canvas y relacionados
	Canvas canvas = new Canvas(800, 600);
	GraphicsContext gc = canvas.getGraphicsContext2D();

	// Constantes de colores
	Color DEFAULT_LINE_COLOR = Color.BLACK;
	Color DEFAULT_FILL_COLOR = Color.YELLOW;
	Color DEFAULT_SELECTED_LINE_COLOR = Color.RED;

	// Botones Barra Izquierda
	final ToggleButton selectionButton = new ToggleButton("Seleccionar");
	final ToggleButton rectangleButton = new ToggleButton("Rectángulo");
	final ToggleButton circleButton = new ToggleButton("Círculo");
	final ToggleButton squareButton = new ToggleButton("Cuadrado");
	final ToggleButton ellipseButton = new ToggleButton("Elipse");
	final ToggleButton deleteButton = new ToggleButton("Borrar");
	final ToggleButton groupButton = new ToggleButton("Agrupar");
	final ToggleButton ungroupButton = new ToggleButton("Desagrupar");
	final ToggleButton rotateRightButton = new ToggleButton("Girar D");
	final ToggleButton flipHorizontallyButton = new ToggleButton("Voltear H");
	final ToggleButton flipVerticallyButton = new ToggleButton("Voltear V");
	final ToggleButton scaleUpButton = new ToggleButton("Escalar +");
	final ToggleButton scaleDownButton = new ToggleButton("Escalar -");

	final ToggleGroup tools = new ToggleGroup();

	// Selector de color de relleno
	ColorPicker fillColorPicker = new ColorPicker(DEFAULT_FILL_COLOR);

	// Dibujar una figura
	Point startPoint;

	// Set para figuras seleccionadas
	FigureSelection figureSelection = new FigureSelection();

	// Grupos de figuras agrupadas
	FigureGroups figureGroups = new FigureGroups();

	// StatusBar
	StatusPane statusPane;

	// EffectsBar
	EffectsPane effectsPane = new EffectsPane();

	private boolean selectionActive = false;

	public PaintPane(CanvasState canvasState, StatusPane statusPane) {
		this.canvasState = canvasState;
		this.statusPane = statusPane;

		ToggleButton[] toolsArr = {selectionButton, rectangleButton, circleButton, squareButton, ellipseButton, deleteButton,
				groupButton, ungroupButton, rotateRightButton, flipHorizontallyButton, flipVerticallyButton, scaleUpButton, scaleDownButton};

		for (ToggleButton tool : toolsArr) {
			tool.setMinWidth(90);
			tool.setToggleGroup(tools);
			tool.setCursor(Cursor.HAND);
		}

		VBox buttonsBox = new VBox(10);
		buttonsBox.getChildren().addAll(toolsArr);
		buttonsBox.getChildren().add(fillColorPicker);
		buttonsBox.setPadding(new Insets(5));
		buttonsBox.setStyle("-fx-background-color: #999");
		buttonsBox.setPrefWidth(100);
		gc.setLineWidth(1);

		rectangleButton.setUserData(new RectangleButton(this, canvasState));
		squareButton.setUserData(new SquareButton(this,canvasState));
		circleButton.setUserData(new CircleButton(this, canvasState));
		ellipseButton.setUserData(new EllipseButton(this, canvasState));

		canvas.setOnMousePressed(this::onMousePressed);
		canvas.setOnMouseReleased(this::onMouseRelease);
		canvas.setOnMouseMoved(this::onMouseMoved);
		canvas.setOnMouseDragged(this::onMouseDragged);
		canvas.setOnMouseClicked(this::getOnMouseClicked);

		//Botón para seleccionar
		selectionButton.setOnAction(event -> {
			if(selectionActive) {
				//Si había una selección activa, la saco
				selectionActive = false;
				figureSelection.clear();
				redrawCanvas();
			}
			//Si no, empiezo una
			selectionActive = true;
		});

		//Botón para borrar. Debe eliminar la selección actual.
		deleteButton.setOnAction(event -> {
			canvasState.removeAll(figureSelection);
			resetSelection();
			deleteButton.setSelected(false);
		});

		//Botón para agrupar. Debe eliminar la selección actual.
		groupButton.setOnAction(event -> {
			figureGroups.group(figureSelection);
			groupButton.setSelected(false);
		});

		//Botón para desagrupar. Debe eliminar la selección actual.
		ungroupButton.setOnAction(event -> { //todo esto no funciona
			figureGroups.ungroup(figureSelection);
			resetSelection();
			ungroupButton.setSelected(false);
		});

		rotateRightButton.setOnAction(event -> {

			redrawCanvas();
		});

		flipHorizontallyButton.setOnAction(event -> {

			redrawCanvas();
		});

		flipVerticallyButton.setOnAction(event -> {

			redrawCanvas();
		});

		scaleUpButton.setOnAction(event -> {

			redrawCanvas();
		});

		scaleDownButton.setOnAction(event -> {

			redrawCanvas();
		});

		//Botón para crear un rectángulo
		rectangleButton.setOnAction(event -> resetSelection());

		//Botón para crear un cuadrado
		squareButton.setOnAction(event -> resetSelection());

		//Botón para crear un elipse
		ellipseButton.setOnAction(event -> resetSelection());

		//Botón para crear un círculo
		circleButton.setOnAction(event -> resetSelection());

		setTop(effectsPane);
		setLeft(buttonsBox);
		setRight(canvas);
	}

	private void onMousePressed(MouseEvent mouseEvent) {
		startPoint = new Point(mouseEvent.getX(), mouseEvent.getY());
	}

	private void onMouseRelease(MouseEvent mouseEvent) {
		if(startPoint == null) return;

		Point endPoint = new Point(mouseEvent.getX(), mouseEvent.getY());
		if(endPoint.getX() < startPoint.getX() || endPoint.getY() < startPoint.getY()) return;

		Toggle selectedButton = tools.getSelectedToggle();
		if(selectedButton == null) return;

		//Si activé el botón de selección...
		if(selectedButton == selectionButton) {
			//Debo sacar lo seleccionado
			if(startPoint.distanceTo(endPoint) > 1) {
				figureSelection.clear();
				Rectangle container = Rectangle.createFrom(startPoint, endPoint);
				//Modifica figureSelection directamente
				canvasState.figuresContainedIn(container, figureSelection);
				//Si la colección de figuras seleccionadas está vacía, no se encontró ninguna
				if(figureSelection.isEmpty()) statusPane.updateStatus("Ninguna figura encontrada");
				//En otro caso, hay una o más
				else {
					//Recorro las figuras seleccionadas
					for (Figure figure : figureSelection) {
						//Si la figura actual pertenece a un grupo, añado a la selección a todas las figuras de ese grupo
						if (figureGroups.findGroup(figure) != null) {
							figureSelection.addAll(figureGroups.findGroup(figure));
						}
					}
					if (figureSelection.size() == 1) statusPane.updateStatus("Se seleccionó: %s".formatted(figureSelection.iterator().next()));
					else statusPane.updateStatus("Se seleccionaron %d figuras".formatted(figureSelection.size()));
				}
			}
			//Si el rectángulo formado es muy pequeño, solo elige la figura de arriba
			else {
				Figure topFigure = canvasState.getTopFigureAt(endPoint);
				//Si hay una figura...
				if (topFigure != null) {
					//Y pertenece a un grupo, selecciono a ese grupo
					if(figureGroups.findGroup(topFigure) != null) {
						figureSelection = figureGroups.findGroup(topFigure);
					}
					//En otro caso, agrego la figura de arriba a la selección
					else {
						figureSelection.add(topFigure);
					}
					statusPane.updateStatus(String.format("Se seleccionó %s", topFigure));
				}
			}

		}

		else {
			((FigureButton) selectedButton.getUserData()).createAndAddFigure(startPoint, endPoint);
		}

        startPoint = null;
		redrawCanvas();
	}

	private void onMouseMoved(MouseEvent mouseEvent) {
		if(figureSelection.isEmpty()) {
			Point eventPoint = new Point(mouseEvent.getX(), mouseEvent.getY());
			Figure topFigure = canvasState.getTopFigureAt(eventPoint);
			statusPane.updateStatus(topFigure == null ? eventPoint.toString() : topFigure.toString());
		}
	}

	private void onMouseDragged(MouseEvent mouseEvent) {
		if(!figureSelection.isEmpty()) {
			Point eventPoint = new Point(mouseEvent.getX(), mouseEvent.getY());
			double diffX = (eventPoint.getX() - startPoint.getX());
			double diffY = (eventPoint.getY() - startPoint.getY());
			for (Figure figure : figureSelection) {
				figure.move(diffX, diffY);
			}
			startPoint.move(diffX, diffY);
			redrawCanvas();
		}
	}

	private void getOnMouseClicked(MouseEvent mouseEvent) {
		if(selectionButton.isSelected()) {
			if(selectionActive) return;
			Point eventPoint = new Point(mouseEvent.getX(), mouseEvent.getY());

			//Recorro las figuras en el canvas, buscando si hay alguna en donde hice click
			Figure foundFigure = canvasState.getTopFigureAt(eventPoint);

			//Si encuentra una figura...
			if(foundFigure != null) {
				selectionActive = true;
				//Si esa figura está en un grupo, hace que la selección sea ese grupo
				if(figureGroups.findGroup(foundFigure) != null) {
					figureSelection.addAll(figureGroups.findGroup(foundFigure));
				}
				//Sino, le pone a la figura los efectos propios
				else {
					effectsPane.shadeBox.setSelected(foundFigure.hasShadow());
					effectsPane.gradientBox.setSelected(foundFigure.hasGradient());
					effectsPane.bevelBox.setSelected(foundFigure.hasBevel());
				}
			}

			//Si no encontró la figura, limpia la selección
			else {
				figureSelection.clear();
			}
		}
	}

	private void redrawCanvas() {
		gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
		for(Figure figure : canvasState) {
			if(figure.hasShadow()){
				figure.addShadow(gc);
			}
			if(figure.hasBevel()){
				figure.addBevel(gc);
			}
			gc.setStroke(figureSelection.contains(figure) ? DEFAULT_SELECTED_LINE_COLOR : DEFAULT_LINE_COLOR);
			figure.draw(gc);
		}
	}

	private void resetSelection() {
		selectionActive = false;
		selectionButton.setSelected(false);
		figureSelection.clear();
		redrawCanvas();
	}

	public StatusPane getStatusPane(){
		return this.statusPane;
	}

	public Color getColorFromPicker(){
		return fillColorPicker.getValue();
	}

	private class EffectsPane extends HBox {
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

			shadeBox.selectedProperty().addListener((ov, old_val, new_val) -> {
                if(!figureSelection.isEmpty()){
                    if(new_val) figureSelection.modifyShadow(true);
                    if(!new_val) figureSelection.modifyShadow(false);
                }
                redrawCanvas();
            });

			bevelBox.selectedProperty().addListener((observableValue, aBoolean, t1) -> {
                if(!figureSelection.isEmpty()){
                    if(t1) figureSelection.modifyBevel(true);
                    if(!t1) figureSelection.modifyBevel(false);
                }
                redrawCanvas();
            });

			gradientBox.selectedProperty().addListener((observableValue, aBoolean, t1) -> {
                if(!figureSelection.isEmpty()){
                    if(t1) figureSelection.modifyGradient(true);
                    if(!t1) figureSelection.modifyGradient(false);
                }
                redrawCanvas();
            });

		}

	}

}
