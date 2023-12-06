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
	private CanvasState canvasState;

	//Canvas
	private Canvas canvas = new Canvas(800, 600);
	private GraphicsContext gc = canvas.getGraphicsContext2D();

	//Constants
	private final Color DEFAULT_LINE_COLOR = Color.BLACK;
	private final Color DEFAULT_FILL_COLOR = Color.YELLOW;
	private final Color DEFAULT_SELECTED_LINE_COLOR = Color.RED;
	private final double DEFAULT_SCALE_PERCENT = 0.25;

	//Left bar buttons
	private final ToggleButton selectionButton = new ToggleButton("Seleccionar");
	private final ToggleButton rectangleButton = new ToggleButton("Rectángulo");
	private final ToggleButton circleButton = new ToggleButton("Círculo");
	private final ToggleButton squareButton = new ToggleButton("Cuadrado");
	private final ToggleButton ellipseButton = new ToggleButton("Elipse");
	private final ToggleButton deleteButton = new ToggleButton("Borrar");
	private final ToggleButton groupButton = new ToggleButton("Agrupar");
	private final ToggleButton ungroupButton = new ToggleButton("Desagrupar");
	private final ToggleButton rotateRightButton = new ToggleButton("Girar D");
	private final ToggleButton flipHorizontallyButton = new ToggleButton("Voltear H");
	private final ToggleButton flipVerticallyButton = new ToggleButton("Voltear V");
	private final ToggleButton scaleUpButton = new ToggleButton("Escalar +");
	private final ToggleButton scaleDownButton = new ToggleButton("Escalar -");
	private final ToggleGroup tools = new ToggleGroup();

	//Selector de color de relleno
	private final ColorPicker fillColorPicker = new ColorPicker(DEFAULT_FILL_COLOR);

	//Dibujar una figura
	private Point startPoint;

	//Selected figures collection
	private final FigureSelection figureSelection = new FigureSelection();

	//Grouped figures collection
	private final FigureGroups figureGroups = new FigureGroups();

	//StatusBar
	private final StatusPane statusPane;

	//EffectsBar
	private EffectsPane effectsPane = new EffectsPane();

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

			//Case: Was moving figures (if click is outside the figures, they are de-selected)
			if(wasMoving) {
				if(foundFigure == null) {
					wasMoving = false;
					figureSelection.clear();
					effectsPane.clearIndeterminate();
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
					if (figureSelection.isEmpty()) {
						statusPane.updateStatusGivenSelection(figureSelection);
						effectsPane.clearIndeterminate();
					}

					//Case: There is one or more figures found
					else {
						for (Figure figure : figureSelection) {
							//Case: Figure belongs to a group (group is added to selection)
							if (figureGroups.findGroup(figure) != null) {
								figureSelection.addAll(figureGroups.findGroup(figure));
							}
							else {
								figureSelection.add(figure);
							}
						}
						effectsPane.getSelectionEffects(figureSelection);
						statusPane.updateStatusGivenSelection(figureSelection);
					}
				}
				//Case: Click selection
				else {
					//Case: Figures found
					if(foundFigure != null) {
						if(!figureSelection.isEmpty()) {
							figureSelection.clear();
							effectsPane.clearIndeterminate();
							redrawCanvas();
						}

						//Case: Figure belongs to a group
						if (figureGroups.findGroup(foundFigure) != null) {
							figureSelection.addAll(figureGroups.findGroup(foundFigure));
							effectsPane.getSelectionEffects(figureSelection);
						}

						//Case: Figure does not belong to a group
						else {
							figureSelection.add(foundFigure);
							effectsPane.getFigureEffects(foundFigure);
						}

						statusPane.updateStatusGivenSelection(figureSelection);
					}
					//Case: No figures found.
					else {
						figureSelection.clear();
						effectsPane.clearIndeterminate();
					}
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

	public StatusPane getStatusPane() {
		return this.statusPane;
	}

	public Color getColorFromPicker() {
		return fillColorPicker.getValue();
	}

	private void setButtons() {
		//Figures deletion
		deleteButton.setOnAction(event -> {
			canvasState.removeAll(figureSelection);
			resetSelection();
			deleteButton.setSelected(false);
		});

		//Figure grouping
		groupButton.setOnAction(event -> {
			figureGroups.group(figureSelection, canvasState);
			groupButton.setSelected(false);
			selectionButton.setSelected(true);
		});

		//Figure degrouping
		ungroupButton.setOnAction(event -> {
			figureGroups.ungroup(figureSelection);
			resetSelection();
			ungroupButton.setSelected(false);
			redrawCanvas();
		});

		//Figure rotation
		rotateRightButton.setOnAction(event -> {
			rotateRightButton.setSelected(false);
			figureSelection.rotateRight();
			selectionButton.setSelected(true);
			redrawCanvas();
		});

		//Figure flipping (horizontal)
		flipHorizontallyButton.setOnAction(event -> {
			flipHorizontallyButton.setSelected(false);
			figureSelection.flipHorizontally();
			selectionButton.setSelected(true);
			redrawCanvas();
		});

		//Figure flipping (vertical)
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

	public boolean getCanvasShadow() {
		return effectsPane.shadowBox.isSelected();
	}

	public boolean getCanvasGradient() {
		return effectsPane.gradientBox.isSelected();
	}

	public boolean getCanvasBevel() {
		return effectsPane.bevelBox.isSelected();
	}

	//Upper effects panel class
	private class EffectsPane extends HBox {

		private final Label label = new Label("Efectos:\t");
		private final CheckBox shadowBox = new CheckBox("Sombra");
		private final CheckBox gradientBox = new CheckBox("Gradiente");
		private final CheckBox bevelBox = new CheckBox("Biselado");
		private final CheckBox[] effectsArr = {shadowBox, gradientBox, bevelBox};

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

			shadowBox.selectedProperty().addListener((observableValue, old_val, new_val) -> {
                if(!figureSelection.isEmpty()) {
                    if(new_val) figureSelection.modifyShadow(true);
                    if(!new_val) figureSelection.modifyShadow(false);
                }
                redrawCanvas();
            });

			bevelBox.selectedProperty().addListener((observableValue, old_val, new_val) -> {
                if(!figureSelection.isEmpty()) {
                    if(new_val) figureSelection.modifyBevel(true);
                    if(!new_val) figureSelection.modifyBevel(false);
                }
                redrawCanvas();
            });

			gradientBox.selectedProperty().addListener((observableValue, old_val, new_val) -> {
                if(!figureSelection.isEmpty()) {
                    if(new_val) figureSelection.modifyGradient(true);
                    if(!new_val) figureSelection.modifyGradient(false);
                }
                redrawCanvas();
            });

		}

		private void getFigureEffects(Figure figure) {
			effectsPane.shadowBox.setSelected(figure.hasShadow());
			effectsPane.gradientBox.setSelected(figure.hasGradient());
			effectsPane.bevelBox.setSelected(figure.hasBevel());
		}

		private void getSelectionEffects(FigureSelection figureSelection) {
			if(figureSelection.atLeastOneHasShadow() && !figureSelection.hasShadow()) effectsPane.shadowBox.setIndeterminate(true);
			else effectsPane.shadowBox.setSelected(figureSelection.hasShadow());
			if(figureSelection.atLeastOneHasGradient() && !figureSelection.hasGradient()) effectsPane.gradientBox.setIndeterminate(true);
			else effectsPane.gradientBox.setSelected(figureSelection.hasGradient());
			if(figureSelection.atLeastOneHasBevel() && !figureSelection.hasBevel()) effectsPane.bevelBox.setIndeterminate(true);
			else effectsPane.bevelBox.setSelected(figureSelection.hasBevel());
		}

		//Necessary for removing the indeterminate status from the checkboxes
		private void clearIndeterminate() {
			effectsPane.shadowBox.setIndeterminate(false);
			effectsPane.gradientBox.setIndeterminate(false);
			effectsPane.bevelBox.setIndeterminate(false);
		}

	}

}
