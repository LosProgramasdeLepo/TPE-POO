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

	//Canvas
	Canvas canvas = new Canvas(800, 600);
	GraphicsContext gc = canvas.getGraphicsContext2D();

	//Constants
	private final Color DEFAULT_LINE_COLOR = Color.BLACK;
	private final Color DEFAULT_FILL_COLOR = Color.YELLOW;
	private final Color DEFAULT_SELECTED_LINE_COLOR = Color.RED;
	private final double DEFAULT_SCALE_PERCENT = 0.25;

	//Left bar buttons
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
	private ColorPicker fillColorPicker = new ColorPicker(DEFAULT_FILL_COLOR);

	//Dibujar una figura
	private Point startPoint;

	//Selected figures collection
	private FigureSelection figureSelection = new FigureSelection();

	//Grouped figures collection
	private FigureGroups figureGroups = new FigureGroups();

	//StatusBar
	private StatusPane statusPane;

	//EffectsBar
	EffectsPane effectsPane = new EffectsPane();

	//True if was moving figures
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

		//Toolbox bar
		VBox toolsBox = new VBox(10);
		toolsBox.getChildren().addAll(toolsArr);
		toolsBox.getChildren().add(fillColorPicker);
		toolsBox.setPadding(new Insets(5));
		toolsBox.setStyle("-fx-background-color: #999");
		toolsBox.setPrefWidth(100);
		gc.setLineWidth(1);

		//Figure creation from buttons
		rectangleButton.setUserData(new RectangleButton(this, canvasState));
		squareButton.setUserData(new SquareButton(this,canvasState));
		circleButton.setUserData(new CircleButton(this, canvasState));
		ellipseButton.setUserData(new EllipseButton(this, canvasState));

		//Mouse action methods
		canvas.setOnMousePressed(this::onMousePressed);
		canvas.setOnMouseReleased(this::onMouseRelease);
		canvas.setOnMouseMoved(this::onMouseMoved);
		canvas.setOnMouseDragged(this::onMouseDragged);

		//Button actions methods
		setButtons();

		//Tools canvas and effects positioning
		setTop(effectsPane);
		setLeft(toolsBox);
		setRight(canvas);
	}

	//When mouse is pressed a startPoint is created
	private void onMousePressed(MouseEvent mouseEvent) {
		startPoint = new Point(mouseEvent.getX(), mouseEvent.getY());
	}

	private void onMouseRelease(MouseEvent mouseEvent) {
		if(startPoint == null) return;

		Point endPoint = new Point(mouseEvent.getX(), mouseEvent.getY());
		if(endPoint.getX() < startPoint.getX() || endPoint.getY() < startPoint.getY()) return;

		Toggle selectedButton = tools.getSelectedToggle();
		if(selectedButton == null) return;

		//Case: selection button is pressed
		if(selectedButton == selectionButton) {
			Figure foundFigure = canvasState.getTopFigureAt(endPoint);

			//Case:Was moving figures (if click is outside the figures, they are de-selected)
			if(wasMoving) {
				if(foundFigure == null) {
					wasMoving = false;
					figureSelection.clear();
					redrawCanvas();
				}
				return;
			}

			//Case: Was NOT moving figures
			else {
				//Case: Container rectangle selection
				if(startPoint.distanceTo(endPoint) > 1) {
					Rectangle container = Rectangle.createFrom(startPoint, endPoint);
					canvasState.figuresContainedIn(container, figureSelection);

					//Case: No figures found
					if (figureSelection.isEmpty()) statusPane.updateStatusGivenSelection(figureSelection);

					//Case: There is one or more figures found
					else {
						for (Figure figure : figureSelection) {
							//Case: Figure belongs to a group (group is added to selection)
							if (figureGroups.findGroup(figure) != null) {
								figureSelection.addAll(figureGroups.findGroup(figure));
							}
							else figureSelection.add(figure);
						}
						statusPane.updateStatusGivenSelection(figureSelection);
						//todo acá poner que se modifican los checkbox
					}
				}
				//Case: Click selection
				else {
					//Case: Figures found
					if(foundFigure != null) {
						if(!figureSelection.isEmpty()) {
							figureSelection.clear();
							redrawCanvas();
						}

						//Case: Figure belongs to a group
						if (figureGroups.findGroup(foundFigure) != null) {
							figureSelection.addAll(figureGroups.findGroup(foundFigure));
						}

						//Case: Figure does not belong to a group
						else figureSelection.add(foundFigure);

						statusPane.updateStatusGivenSelection(figureSelection);
					}
					//Case: No figures found.
					else figureSelection.clear();
				}
			}
		}
		//Caso: No selection button is pressed (figure creation)
		else ((FigureButton) selectedButton.getUserData()).createAndAddFigure(startPoint, endPoint);

		redrawCanvas();
	}

	private void onMouseMoved(MouseEvent mouseEvent) {
		if(figureSelection.isEmpty()) {
			Point eventPoint = new Point(mouseEvent.getX(), mouseEvent.getY());
			Figure topFigure = canvasState.getTopFigureAt(eventPoint);
			//Figure information or coordinates shown
			statusPane.updateStatus(topFigure == null ? eventPoint.toString() : topFigure.toString());
		}
	}

	private void onMouseDragged(MouseEvent mouseEvent) {
		if(!figureSelection.isEmpty()) {
			Point eventPoint = new Point(mouseEvent.getX(), mouseEvent.getY());
			double diffX = (eventPoint.getX() - startPoint.getX());
			double diffY = (eventPoint.getY() - startPoint.getY());
			figureSelection.move(diffX,diffY);
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
			//Red border if figure is selected (otherwise back border)
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
		//figures deletion
		deleteButton.setOnAction(event -> {
			canvasState.removeAll(figureSelection);
			resetSelection();
			deleteButton.setSelected(false);
		});

		//figure grouping
		groupButton.setOnAction(event -> {
			figureGroups.group(figureSelection, canvasState);
			groupButton.setSelected(false);
			selectionButton.setSelected(true);
		});

		//figure degrouping
		ungroupButton.setOnAction(event -> {
			figureGroups.ungroup(figureSelection);
			resetSelection();
			ungroupButton.setSelected(false);
			figureSelection.clear();
			selectionButton.setSelected(true);
			redrawCanvas();
		});

		//figure rotation
		rotateRightButton.setOnAction(event -> {
			rotateRightButton.setSelected(false);
			figureSelection.rotateRight();
			selectionButton.setSelected(true);
			redrawCanvas();
		});

		//figure flipping (horizontal)
		flipHorizontallyButton.setOnAction(event -> {
			flipHorizontallyButton.setSelected(false);
			figureSelection.flipHorizontally();
			selectionButton.setSelected(true);
			redrawCanvas();
		});

		//figure flipping (vertical)
		flipVerticallyButton.setOnAction(event -> {
			flipVerticallyButton.setSelected(false);
			figureSelection.flipVertically();
			selectionButton.setSelected(true);
			redrawCanvas();
		});

		//Size augmentation
		scaleUpButton.setOnAction(event -> {
			scaleUpButton.setSelected(false);
			figureSelection.scale(DEFAULT_SCALE_PERCENT);
			selectionButton.setSelected(true);
			redrawCanvas();
		});

		//Size reduction
		scaleDownButton.setOnAction(event -> {
			scaleDownButton.setSelected(false);
			figureSelection.scale(-DEFAULT_SCALE_PERCENT);
			selectionButton.setSelected(true);
			redrawCanvas();
		});

		//Rectangle
		rectangleButton.setOnAction(event -> resetSelection());

		//Square
		squareButton.setOnAction(event -> resetSelection());

		//Ellipse
		ellipseButton.setOnAction(event -> resetSelection());

		//Circle
		circleButton.setOnAction(event -> resetSelection());
	}

	//Upper effects pannel class
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
                if(!figureSelection.isEmpty()) {
                    if(new_val) figureSelection.modifyShadow(true);
                    if(!new_val) figureSelection.modifyShadow(false);
                }
                redrawCanvas();
            });

			bevelBox.selectedProperty().addListener((observableValue, aBoolean, t1) -> {
                if(!figureSelection.isEmpty()) {
                    if(t1) figureSelection.modifyBevel(true);
                    if(!t1) figureSelection.modifyBevel(false);
                }
                redrawCanvas();
            });

			gradientBox.selectedProperty().addListener((observableValue, aBoolean, t1) -> {
                if(!figureSelection.isEmpty()) {
                    if(t1) figureSelection.modifyGradient(true);
                    if(!t1) figureSelection.modifyGradient(false);
                }
                redrawCanvas();
            });

		}

	}

}
