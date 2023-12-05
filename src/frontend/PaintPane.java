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

	//BackEnd
	CanvasState canvasState;

	//Canvas y relacionados
	Canvas canvas = new Canvas(800, 600);
	GraphicsContext gc = canvas.getGraphicsContext2D();

	//Constantes de colores
	Color DEFAULT_LINE_COLOR = Color.BLACK;
	Color DEFAULT_FILL_COLOR = Color.YELLOW;
	Color DEFAULT_SELECTED_LINE_COLOR = Color.RED;

	//Botones Barra Izquierda
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

	//Selector de color de relleno
	ColorPicker fillColorPicker = new ColorPicker(DEFAULT_FILL_COLOR);

	//Dibujar una figura
	Point startPoint;

	//Set para figuras seleccionadas
	FigureSelection figureSelection = new FigureSelection();

	//Grupos de figuras agrupadas
	FigureGroups figureGroups = new FigureGroups();

	//StatusBar
	StatusPane statusPane;

	//EffectsBar
	EffectsPane effectsPane = new EffectsPane();

	//Verdadero si se estaban moviendo figuras
	private boolean wasMoving;

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

		//Crea la barra de herramientas
		VBox toolsBox = new VBox(10);
		toolsBox.getChildren().addAll(toolsArr);
		toolsBox.getChildren().add(fillColorPicker);
		toolsBox.setPadding(new Insets(5));
		toolsBox.setStyle("-fx-background-color: #999");
		toolsBox.setPrefWidth(100);
		gc.setLineWidth(1);

		//Crea las figuras adecuadas al tocar sus respectivos botones
		rectangleButton.setUserData(new RectangleButton(this, canvasState));
		squareButton.setUserData(new SquareButton(this,canvasState));
		circleButton.setUserData(new CircleButton(this, canvasState));
		ellipseButton.setUserData(new EllipseButton(this, canvasState));

		//Métodos para acciones del mouse
		canvas.setOnMousePressed(this::onMousePressed);
		canvas.setOnMouseReleased(this::onMouseRelease);
		canvas.setOnMouseMoved(this::onMouseMoved);
		canvas.setOnMouseDragged(this::onMouseDragged);

		//Método que define las acciones de los botones
		setButtons();

		//Posiciono el canvas, la barra de efectos y las herramientas
		setTop(effectsPane);
		setLeft(toolsBox);
		setRight(canvas);
	}

	//Cuando se presiona el mouse, se crea un punto
	private void onMousePressed(MouseEvent mouseEvent) {
		startPoint = new Point(mouseEvent.getX(), mouseEvent.getY());
	}

	private void onMouseRelease(MouseEvent mouseEvent) {
		if(startPoint == null) return;

		Point endPoint = new Point(mouseEvent.getX(), mouseEvent.getY());
		if(endPoint.getX() < startPoint.getX() || endPoint.getY() < startPoint.getY()) return;

		Toggle selectedButton = tools.getSelectedToggle();
		if(selectedButton == null) return;

		//Caso: El botón de selección está marcado
		if(selectedButton == selectionButton) {
			Figure foundFigure = canvasState.getTopFigureAt(endPoint);

			//Caso: Se estaban moviendo figuras (por lo que al liberar el click fuera de una figura se sale de la selección)
			if(wasMoving) {
				if(foundFigure == null) {
					wasMoving = false;
					figureSelection.clear();
					redrawCanvas();
				}
				return;
			}

			//Caso: No se estaban moviendo figuras
			else {
				//Caso: Se dibuja un rectángulo contenedor
				if(startPoint.distanceTo(endPoint) > 1) {
					Rectangle container = Rectangle.createFrom(startPoint, endPoint);
					canvasState.figuresContainedIn(container, figureSelection);

					//Caso: No hay figuras
					if (figureSelection.isEmpty()) statusPane.updateStatusGivenSelection(figureSelection);

					//Caso: Hay una o más
					else {
						for (Figure figure : figureSelection) {
							//Caso: Una figura pertenece a un grupo (agrego todas las de dicho grupo)
							if (figureGroups.findGroup(figure) != null) {					//todo esto podría ser una función
								figureSelection.addAll(figureGroups.findGroup(figure));
							}
							else{
								figureSelection.add(figure);
							}
						}
						statusPane.updateStatusGivenSelection(figureSelection);
					}
				}
				//Caso: Se hace click
				else {
					//Caso: No hay ninguna figura
					if(foundFigure != null) {
						if(!figureSelection.isEmpty()) {
							figureSelection.clear();
							redrawCanvas();
						}


						//Caso: La figura pertenece a un grupo
						if (figureGroups.findGroup(foundFigure) != null) {				//todo esto podría ser una función (se repite acá)
							figureSelection.addAll(figureGroups.findGroup(foundFigure));
						}

						//Caso: La figura no pertenece a un grupo
						else figureSelection.add(foundFigure);

						statusPane.updateStatusGivenSelection(figureSelection);
					}
					//Caso: No hay ninguna figura
					else figureSelection.clear();
				}
			}
		}
		//Caso: El botón de selección no está marcado (se crea una figura)
		else ((FigureButton) selectedButton.getUserData()).createAndAddFigure(startPoint, endPoint);

		redrawCanvas();
	}

	private void onMouseMoved(MouseEvent mouseEvent) {
		if(figureSelection.isEmpty()) {
			Point eventPoint = new Point(mouseEvent.getX(), mouseEvent.getY());
			Figure topFigure = canvasState.getTopFigureAt(eventPoint);
			//Si hay una figura, muestra su información (y si no las coordenadas)
			statusPane.updateStatus(topFigure == null ? eventPoint.toString() : topFigure.toString());
		}
	}

	private void onMouseDragged(MouseEvent mouseEvent) {
		if(!figureSelection.isEmpty()) {
			Point eventPoint = new Point(mouseEvent.getX(), mouseEvent.getY());
			double diffX = (eventPoint.getX() - startPoint.getX());
			double diffY = (eventPoint.getY() - startPoint.getY());
			for (Figure figure : figureSelection) figure.move(diffX, diffY);
			startPoint.move(diffX, diffY);
			redrawCanvas();
			wasMoving = true;
		}
	}

	private void redrawCanvas() {
		gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
		for(Figure figure : canvasState) {
			if(figure.hasShadow()) figure.addShadow(gc);
			if(figure.hasBevel()) figure.addBevel(gc);
			//Si la figura estaba seleccionada, le pone el borde rojo (y si no le pone el borde negro)
			gc.setStroke(figureSelection.contains(figure) ? DEFAULT_SELECTED_LINE_COLOR : DEFAULT_LINE_COLOR);
			figure.draw(gc);
		}
	}

	private void resetSelection() {
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

	private void setButtons() {
		//Botón para borrar
		deleteButton.setOnAction(event -> {
			canvasState.removeAll(figureSelection);
			resetSelection();
			deleteButton.setSelected(false);
		});

		//Botón para agrupar
		groupButton.setOnAction(event -> {
			figureGroups.group(figureSelection, canvasState);
			groupButton.setSelected(false);
			selectionButton.setSelected(true);
		});

		//Botón para desagrupar
		ungroupButton.setOnAction(event -> {
			figureGroups.ungroup(figureSelection);
			resetSelection();
			ungroupButton.setSelected(false); //todo quizas solo debería deseleccionar sí encontró algo para desagrupar;
			figureSelection.clear();
			redrawCanvas();
		});

		//Botón para rotar a la derecha
		rotateRightButton.setOnAction(event -> {
			rotateRightButton.setSelected(false);
			figureSelection.rotateRight();
			redrawCanvas();
		});

		//Botón para rotar horizontalmente
		flipHorizontallyButton.setOnAction(event -> {
			flipHorizontallyButton.setSelected(false);
			figureSelection.flipHorizontally();
			selectionButton.setSelected(true);
			redrawCanvas();
		});

		//Botón para rotar verticalmente
		flipVerticallyButton.setOnAction(event -> {
			flipVerticallyButton.setSelected(false);
			figureSelection.flipVertically();
			selectionButton.setSelected(true);
			redrawCanvas();
		});

		//Botón para aumentar el tamaño de la figura
		scaleUpButton.setOnAction(event -> {
			scaleUpButton.setSelected(false);
			figureSelection.scaleUp();
			redrawCanvas();
		});

		//Botón para disminuir el tamaño de la figura
		scaleDownButton.setOnAction(event -> {
			scaleDownButton.setSelected(false);
			figureSelection.scaleDown();
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
	}

	//Clase que compone al panel de efectos superior
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
