package frontend;
import frontend.figureButtons.*;
import backend.CanvasState;
import backend.model.*;
import frontend.figureButtons.CircleButton;
import frontend.figureButtons.EllipseButton;
import frontend.figureButtons.RectangleButton;
import frontend.figureButtons.SquareButton;
import javafx.geometry.Insets;
import javafx.scene.Cursor;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
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
	final ToggleGroup tools = new ToggleGroup();

	// Selector de color de relleno
	ColorPicker fillColorPicker = new ColorPicker(DEFAULT_FILL_COLOR);

	// Dibujar una figura
	Point startPoint;

	// Seleccionar una figura
	Figure selectedFigure;

	// StatusBar
	StatusPane statusPane;

	// EffectsBar
	EffectsPane effectsPane;

	public PaintPane(CanvasState canvasState, StatusPane statusPane) {
		this.canvasState = canvasState;
		this.statusPane = statusPane;
		ToggleButton[] toolsArr = {selectionButton, rectangleButton, circleButton, squareButton, ellipseButton, deleteButton};
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

		canvas.setOnMouseClicked(event -> {
			if(selectionButton.isSelected()) {
				Point eventPoint = new Point(event.getX(), event.getY());
				boolean found = false;
				StringBuilder label = new StringBuilder("Se seleccionó: ");
				for (Figure figure : canvasState) {
					if(figure.figureBelongs(eventPoint)) {
						found = true;
						selectedFigure = figure;
						label.append(figure);
					}
				}
				if (found) {
					statusPane.updateStatus(label.toString());
				} else {
					selectedFigure = null;
					statusPane.updateStatus("Ninguna figura encontrada");
				}
				redrawCanvas();
			}
		});

		deleteButton.setOnAction(event -> {
			if (selectedFigure != null) {
				canvasState.remove(selectedFigure);
				selectedFigure = null;
				redrawCanvas();
			}
		});

		setLeft(buttonsBox);
		setRight(canvas);
	}

	private void onMousePressed(MouseEvent mouseEvent) {
		startPoint = new Point(mouseEvent.getX(), mouseEvent.getY());
	}

	private void onMouseRelease(MouseEvent mouseEvent) {
		Point endPoint = new Point(mouseEvent.getX(), mouseEvent.getY());
		if(startPoint == null) {
			return;
		}
		if(endPoint.getX() < startPoint.getX() || endPoint.getY() < startPoint.getY()) {
			return;
		}
		Toggle selectedButton = tools.getSelectedToggle();
		if(selectedButton == null){
			return;
		}
		if(selectedButton != selectionButton) {
			((FigureButton) selectedButton.getUserData()).createAndAddFigure(startPoint, endPoint);
		}
		startPoint = null;
		redrawCanvas();
	}

	private void onMouseMoved(MouseEvent mouseEvent) {
		Point eventPoint = new Point(mouseEvent.getX(), mouseEvent.getY());
		boolean found = false;
		StringBuilder label = new StringBuilder();
		for(Figure figure : canvasState) {
			if(figure.figureBelongs(eventPoint)) {
				found = true;
				label.append(figure);
			}
		}
		if(found) {
			statusPane.updateStatus(label.toString());
		} else {
			statusPane.updateStatus(eventPoint.toString());
		}
	}

	private void onMouseDragged(MouseEvent mouseEvent) {
		if(selectionButton.isSelected()) {
			Point eventPoint = new Point(mouseEvent.getX(), mouseEvent.getY());
			double diffX = (eventPoint.getX() - startPoint.getX());
			double diffY = (eventPoint.getY() - startPoint.getY());
			if(selectedFigure != null) selectedFigure.move(diffX, diffY);
			startPoint.move(diffX, diffY);
			redrawCanvas();
		}
	}

	void redrawCanvas() {
		gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
		for(Figure figure : canvasState) {
			if(figure == selectedFigure) { gc.setStroke(DEFAULT_SELECTED_LINE_COLOR); }
			else { gc.setStroke(DEFAULT_LINE_COLOR); }
			gc.setFill(figure.getFillColor());
			figure.draw(gc);
		}
	}

	public StatusPane getStatusPane(){
		return this.statusPane;
	}
	public Color getColorFromPicker(){
		return fillColorPicker.getValue();
	}

}
