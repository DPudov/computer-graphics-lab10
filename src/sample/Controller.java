package sample;

import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class Controller {
    @FXML
    ColorPicker backgroundPicker;
    @FXML
    ColorPicker curvePicker;
    @FXML
    Button clearAllButton;
    @FXML
    ChoiceBox functionPicker;
    @FXML
    TextField inputXBegField;
    @FXML
    TextField inputXEndField;
    @FXML
    TextField inputDeltaXField;
    @FXML
    TextField inputZBegField;
    @FXML
    TextField inputZEndField;
    @FXML
    TextField inputDeltaZField;
    @FXML
    TextField inputOXRotateField;
    @FXML
    TextField inputOYRotateField;
    @FXML
    TextField inputOZRotateField;
    @FXML
    Button drawButton;
    @FXML
    Button rotateButton;
    @FXML
    Canvas canvas;

    private double absX = 0;
    private double absY = 0;
    private double absZ = 0;

    @FXML
    public void initialize() {
        setupColors();
        setupCaptions();
        clearAllButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            absX = 0;
            absY = 0;
            absZ = 0;
            clearCanvas();
        });

        drawButton.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            absX = 0;
            absY = 0;
            absZ = 0;
            draw();
        });

        rotateButton.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            try {
                double thetaX = Double.parseDouble(inputOXRotateField.getText());
                double thetaY = Double.parseDouble(inputOYRotateField.getText());
                double thetaZ = Double.parseDouble(inputOZRotateField.getText());
                absX += thetaX;
                absY += thetaY;
                absZ += thetaZ;
            } catch (Exception e) {
                setAlert("Ошибка ввода!");
            }
            clearCanvas();
            draw();
        });
    }

    private void setupCaptions() {
        inputXBegField.setText("-2");
        inputXEndField.setText("2");
        inputDeltaXField.setText("0.05");
        inputZBegField.setText("-2");
        inputZEndField.setText("2");
        inputDeltaZField.setText("0.05");
        inputOXRotateField.setText("0");
        inputOYRotateField.setText("0");
        inputOZRotateField.setText("0");
    }

    private void draw() {
        try {
            GraphicsContext gc = canvas.getGraphicsContext2D();
            double xMin = Double.parseDouble(inputXBegField.getText());
            double xMax = Double.parseDouble(inputXEndField.getText());
            double xStep = Double.parseDouble(inputDeltaXField.getText());
            double zMin = Double.parseDouble(inputZBegField.getText());
            double zMax = Double.parseDouble(inputZEndField.getText());
            double zStep = Double.parseDouble(inputDeltaZField.getText());


            if (xMax < xMin || zMax < zMin || xStep < 0 || zStep < 0) {
                setAlert("Ошибка ввода");
                return;
            }


            FloatHorizon.floatHorizon(gc,
                    0,
                    xMin, xMax, xStep,
                    zMin, zMax, zStep,
                    curvePicker.getValue(),
                    absX, absY, absZ);
        } catch (Exception e) {
            e.printStackTrace();
            setAlert("Ошибка ввода!");
        }

    }

    private void clearCanvas() {
        GraphicsContext gc = canvas.getGraphicsContext2D();
        gc.setFill(backgroundPicker.getValue());
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }

    private void setupColors() {
        backgroundPicker.setValue(Color.WHITE);
        curvePicker.setValue(Color.BLUE);
        clearCanvas();
    }

    private void setAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(message);
        alert.setTitle("Произошла ошибка :(");
        alert.setHeaderText("ОШИБКА");
        alert.show();
    }
}
