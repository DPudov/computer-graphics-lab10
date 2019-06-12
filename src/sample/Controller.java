package sample;

import javafx.animation.AnimationTimer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.Arrays;

public class Controller {
    @FXML
    GridPane mainScene;
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

    private ArrayList<FloatHorizon> horizons = new ArrayList<>();
    private boolean goNorth = false;
    private boolean goSouth = false;
    private boolean goWest = false;
    private boolean goEast = false;

    @FXML
    public void initialize() {
        setupColors();
        setupCaptions();
        clearAllButton.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            horizons.clear();
            clearCanvas();
        });

        drawButton.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            draw();
        });

        rotateButton.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            try {
                double thetaX = Double.parseDouble(inputOXRotateField.getText());
                double thetaY = Double.parseDouble(inputOYRotateField.getText());
                double thetaZ = Double.parseDouble(inputOZRotateField.getText());
                for (FloatHorizon horizon : horizons) {
                    horizon.setAbsX(horizon.getAbsX() + thetaX);
                    horizon.setAbsY(horizon.getAbsY() + thetaY);
                    horizon.setAbsZ(horizon.getAbsZ() + thetaZ);
                }
            } catch (Exception e) {
                setAlert("Ошибка ввода!");
            }
            clearCanvas();
            redraw();
        });

        mainScene.setOnKeyPressed(keyEvent -> {
            switch (keyEvent.getCode()) {
                case W:
                    goNorth = true;
                    break;
                case A:
                    goWest = true;
                    break;
                case S:
                    goSouth = true;
                    break;
                case D:
                    goEast = true;
                    break;

                case UP:
                    goNorth = true;
                    break;
                case DOWN:
                    goSouth = true;
                    break;
                case LEFT:
                    goWest = true;
                    break;
                case RIGHT:
                    goEast = true;
                    break;
            }
        });

        mainScene.setOnKeyReleased(keyEvent -> {
            switch (keyEvent.getCode()) {
                case W:
                    goNorth = false;
                    break;
                case A:
                    goWest = false;
                    break;
                case S:
                    goSouth = false;
                    break;
                case D:
                    goEast = false;
                    break;

                case UP:
                    goNorth = false;
                    break;
                case DOWN:
                    goSouth = false;
                    break;
                case LEFT:
                    goWest = false;
                    break;
                case RIGHT:
                    goEast = false;
                    break;
            }
        });

        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                int thetaX = 0, thetaY = 0, thetaZ = 0;

                if (goNorth) {
                    thetaX += 2;
                }
                if (goSouth) {
                    thetaX -= 2;
                }
                if (goEast) {
                    thetaY += 2;
                }
                if (goWest) {
                    thetaY -= 2;
                }

                for (FloatHorizon horizon : horizons) {
                    horizon.setAbsX(horizon.getAbsX() + thetaX);
                    horizon.setAbsY(horizon.getAbsY() + thetaY);
                    horizon.setAbsZ(horizon.getAbsZ() + thetaZ);
                }
                clearCanvas();
                redraw();
            }
        };
        timer.start();
    }

    private void redraw() {
        for (FloatHorizon f : horizons) {
            f.floatHorizon(canvas.getGraphicsContext2D(),
                    functionPicker.getSelectionModel().getSelectedIndex(),
                    f.getxMin(), f.getxMax(), f.getxStep(),
                    f.getzMin(), f.getzMax(), f.getzStep(),
                    curvePicker.getValue(),
                    f.getAbsX(), f.getAbsY(), f.getAbsZ());
        }

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

        ObservableList<String> funcs = FXCollections.observableList(
                Arrays.asList(
                        "cos^2(x) - sin^2(z)",
                        "sqrt(x^2 + z^2) * sinc(sqrt(x^2 + z^2))",
                        "sinc(sin(x^2 + z^2))"
//                        DrawAlgorithms.ALG_BRESENHAM,
//                        DrawAlgorithms.ALG_MIDDLE_POINT,
//                        DrawAlgorithms.ALG_LIB
                ));
        functionPicker.setItems(funcs);
        functionPicker.getSelectionModel().selectFirst();
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

            FloatHorizon f = new FloatHorizon(xMax, xMin, -5, 5, (int) canvas.getWidth(),
                    (int) canvas.getHeight());
            f.floatHorizon(gc,
                    functionPicker.getSelectionModel().getSelectedIndex(),
                    xMin, xMax, xStep,
                    zMin, zMax, zStep,
                    curvePicker.getValue(),
                    0, 0, 0);
            horizons.add(f);
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
