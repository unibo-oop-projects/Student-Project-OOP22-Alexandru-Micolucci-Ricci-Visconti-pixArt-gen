package it.unibo.pixArt.view.game;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import it.unibo.pixArt.controller.game.GameController;
import it.unibo.pixArt.model.timer.TimerThread;


import it.unibo.pixArt.view.AbstractFXView;
import it.unibo.pixArt.view.components.PixelsPane;
import it.unibo.pixArt.view.pages.PageLoader;
import it.unibo.pixArt.view.pages.Pages;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Popup;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

public class GameView extends AbstractFXView{

    @FXML
    private Label timer;

    @FXML
    private BorderPane root;

    private Color selectedColor = Color.WHITE;

    @FXML
    public void onMenuClick(){
        if (this.getGameController().getTimer().isRunning()) {
            this.getGameController().getTimer().stop();
        }
        PageLoader.getInstance().switchPage(getStage(), Pages.MENU, this.getController().getModel());
    }

    @Override
    public void init() {
        this.getGameController().setColorStack();
        this.getGameController().setColorStack();
        final var e = new EventHandler<ActionEvent>(){
            @Override
            public void handle(final ActionEvent event){
                final var button = (Button)event.getSource();
                boolean checkPixel = getGameController().checkPixel(GridPane.getColumnIndex(button), GridPane.getRowIndex(button), selectedColor);
                if (checkPixel){
                    button.setStyle("-fx-background-color: #" + selectedColor.toString().substring(2));
                }
                if(getGameController().colorStackIsEmpty()) {
                    onGameFinish();
                }

            }
        };

        final GridPane center = new PixelsPane.GridPaneBuilder()
        .setColumns(this.getGameController().getFrameSize())
        .setRows(this.getGameController().getFrameSize())
        .setGridLinesVisible(true)
        .setAction(e)
        .build();
        this.root.setCenter(center);
        this.root.setRight(createColorPane());
        
        center.getChildren().forEach(b -> b.addEventHandler(MouseEvent.MOUSE_ENTERED, event -> {
            if (getGameController().getIsDrawing()) {
                final var button = (Button) event.getSource();
                boolean checkPixel = getGameController().checkPixel(GridPane.getColumnIndex(button), GridPane.getRowIndex(button), selectedColor);
                if (checkPixel){
                    button.setStyle("-fx-background-color: #" + selectedColor.toString().substring(2));
                }
                if(getGameController().colorStackIsEmpty()) {
                   //
                }
            }
        }));

        center.addEventFilter(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            if (mouseEvent.getButton() == MouseButton.PRIMARY && mouseEvent.getClickCount() == 2) {
                getGameController().setIsrawing();;
            }
        });

        this.getGameController().getTimer().start();
        new TimerThread(this.getGameController().getTimer(), this::onTimeFinish, this::OnTimeUpdate).start();
    }

    private void OnTimeUpdate(){
        Platform.runLater(()->{
           this.timer.setText(timeToString(this.getGameController().getTimer().getRemainingTime()));
        });
    }

    private void onTimeFinish(){
        Platform.runLater(this::onGameFinish);
    }

    private void onGameFinish(){
        this.getGameController().getTimer().stop();
        this.gameOverPopUp();
    }

    private void gameOverPopUp(){
        String percentage = String.format("%.2f", this.getGameController().getPercentage());
        final GameOverPopUp gameOverPopUp = new GameOverPopUp(percentage);
        gameOverPopUp.onHomeClick(()->{
            gameOverPopUp.close();
            Platform.runLater(() -> PageLoader.getInstance().switchPage(this.getStage(), Pages.MENU, this.getController().getModel()));
        });
        gameOverPopUp.onNewGameClick(()-> {
            gameOverPopUp.close();
            Platform.runLater(()-> PageLoader.getInstance().switchPage(this.getStage(), Pages.GAMESETUP, this.getController().getModel()));
        });
        gameOverPopUp.show();
    }

    private String timeToString(final double remainingTime){
        double minutes = remainingTime/60;
        double seconds = remainingTime % 60;
        return Integer.toString((int) minutes) + ":" + Integer.toString((int) seconds);
    }

    /*Try to use streams */
    private ListView<Button> createColorPane() {
        final ListView<Button> colorList = new ListView<>();
        final List<Color> colors = getGameController().getColorStack().entrySet()
        .stream()
        .map(e -> e.getKey())
        .collect(Collectors.toList());
        
        final List<Button> btnList = new LinkedList<>();
        for(var elem : colors) {
            final Button btn = new Button();
            btn.setText(Integer.toString(colors.indexOf(elem)));
            btn.setStyle("-fx-background-color: #" + elem.toString().substring(2));
            btn.addEventHandler(MouseEvent.MOUSE_CLICKED, h -> selectedColor = elem);
            btn.setMinWidth(200);
            btnList.add(btn);
        }
        colorList.getItems().addAll(btnList);
        return colorList;
    }

    private GameController getGameController(){
        return (GameController) this.getController();
    }
    
}
