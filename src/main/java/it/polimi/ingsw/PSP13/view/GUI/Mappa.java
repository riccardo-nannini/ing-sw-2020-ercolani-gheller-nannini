package it.polimi.ingsw.PSP13.view.GUI;

import it.polimi.ingsw.PSP13.immutables.MapVM;
import it.polimi.ingsw.PSP13.model.player.Color;
import it.polimi.ingsw.PSP13.model.player.Coords;
import it.polimi.ingsw.PSP13.network.client_dispatching.MessageClientsInfoCV;
import it.polimi.ingsw.PSP13.view.GUI.status.SetupStatus;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

public class Mappa implements Initializable {

    private GuiInput guiInput;

    private TurnStatus status;


    @FXML
    private TextArea textInfo1;

    @FXML
    private TextArea textInfo2;

    @FXML
    private TextArea textInfo3;

    @FXML
    private ImageView imageInfo1;

    @FXML
    private ImageView imageInfo2;

    @FXML
    private ImageView imageInfo3;


    @FXML
    private GridPane grid;

    @FXML
    private TextArea textEffect;

    @FXML
    private TextArea textInfo;

    @FXML
    private TextArea godName;

    private String clientGodName;
    private String clientGodEffect;
    private String god2;
    private String effect2;
    private String god3;
    private String effect3;

    HashMap<Color, ArrayList<ImageView>> buildersImages = new HashMap<>();

    private Pane[][] panes = new Pane[5][5];

    private final int BUILDERS_IMAGES_OFFSET = 27;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        savePane();
        TurnStatus.setMap(this);
    }

    //TODO ho scambiato row con column nella penultima riga per vedere di sistamre
    @FXML
    public void selectCell(MouseEvent e) {
        clear();

        Node source = (Node)e.getSource() ;
        int colIndex = ( GridPane.getColumnIndex(source) != null ? GridPane.getColumnIndex(source) : 0);
        int rowIndex = ( GridPane.getRowIndex(source) != null ? GridPane.getRowIndex(source) : 0);

        status.selectCell(new Coords(rowIndex,colIndex));
        grid.setDisable(true);
    }

    //TODO sistemare invio dell'effetto dei Dei mettendo tutti gli effetti in MessageCLientsInfoCV
    //TODO e sistemare quindi l'assegnazione di effect2 e effect3 e godEffectClient

    public void setUpClientsInfo(MessageClientsInfoCV clientsInfo) {

        textInfo1.setText(clientsInfo.getClientUsername());
        clientGodName = clientsInfo.getClientGod();
        File file1 = new File("resources/Icons/"+clientGodName+".png");
        Image image1 = new Image(file1.toURI().toString());
        imageInfo1.setImage(image1);
        setBuildersImages(clientsInfo.getClientColor(),clientsInfo.getClientGod());
        clientGodEffect = clientsInfo.getClientEffect();

        god2 = clientsInfo.getOpponentsGod().get(0);
        File file2 = new File("resources/Icons/"+god2+".png");
        Image image2 = new Image(file2.toURI().toString());
        imageInfo2.setImage(image2);
        textInfo2.setText(clientsInfo.getOpponentsUsernames().get(0));
        setBuildersImages(clientsInfo.getOpponentsColors().get(0),clientsInfo.getOpponentsGod().get(0));
        effect2 = clientsInfo.getOpponentsEffects().get(0);


        if (clientsInfo.getOpponentsUsernames().size() == 2) {
            god3 = clientsInfo.getOpponentsGod().get(1);
            File file3 = new File("resources/Icons/"+god3+".png");
            Image image3 = new Image(file3.toURI().toString());
            imageInfo3.setImage(image3);
            textInfo3.setText(clientsInfo.getOpponentsUsernames().get(1));
            setBuildersImages(clientsInfo.getOpponentsColors().get(1),clientsInfo.getOpponentsGod().get(1));
            effect3 = clientsInfo.getOpponentsEffects().get(1);

        }
    }


    public void setClientEffectDescription(String effect) {
        this.clientGodEffect = effect;
    }


    public void setBuildersImages(Color color, String god) {
        ImageView i1 = new ImageView("Icons/"+god+".png");
        ImageView i2 = new ImageView("Icons/"+god+".png");
        i1.setFitHeight(50);
        i1.setFitWidth(50);
        i2.setFitHeight(50);
        i2.setFitWidth(50);
        i1.setMouseTransparent(true);
        i2.setMouseTransparent(true);
        i1.setTranslateX(BUILDERS_IMAGES_OFFSET);
        i2.setTranslateX(BUILDERS_IMAGES_OFFSET);
        ArrayList<ImageView> i = new ArrayList<>();
        i.add(i1);
        i.add(i2);
        buildersImages.put(color,i);
    }

    Boolean firstUpdateBuilders = true;

    //TODO ho scambiato getx e gety per vedere di aggiustare
    @FXML
    public void updateBuiders(Color color, Coords[] coords) {
        if (!firstUpdateBuilders) {
            grid.getChildren().remove(buildersImages.get(color).get(0));
            grid.getChildren().remove(buildersImages.get(color).get(1));
        } else {
            firstUpdateBuilders = false;
        }
        grid.add(buildersImages.get(color).get(0), coords[0].getY(), coords[0].getX());
        grid.add(buildersImages.get(color).get(1), coords[1].getY(), coords[1].getX());

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                panes[i][j].getStyleClass().removeAll("highlighted");
            }
        }
    }

    String imageShowed = "";
    @FXML
    public void showEffect(MouseEvent e) {
        Node source = (Node) e.getSource();
        String id = source.getId();
        if (imageShowed.equals(id)) {
            textInfo.setVisible(true);
            textEffect.setVisible(false);
            godName.setVisible(false);
        } else {
            switch (id) {
                case "imageInfo1":
                    godName.setText(clientGodName);
                    textEffect.setText(clientGodEffect);
                    break;
                case "imageInfo2":
                    godName.setText(god2);
                    textEffect.setText(effect2);
                    break;
                case "imageInfo3":
                    godName.setText(god3);
                    textEffect.setText(effect3);
                    break;
            }
                imageShowed = id;
                textInfo.setVisible(false);
                godName.setVisible(true);
                textEffect.setVisible(true);
        }
    }


    //TODO ho scambiato j e i nell'ultimo if per vedere di aggiustare
    public void updateMap(MapVM mapVM) {
        int cellHeight;
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                cellHeight = mapVM.getMap()[i][j].getLevel().getHeight();
                boolean isDome = mapVM.getMap()[i][j].getDome();
                String buildingStyle = "";
                switch (cellHeight) {
                    case 0:
                        buildingStyle = isDome ? "dome" : "clear";
                        break;
                    case 1:
                        buildingStyle = isDome ? "level1Dome" : "level1";
                        break;
                    case 2:
                        buildingStyle = isDome ? "level2Dome" : "level2";
                        break;
                    case 3:
                        buildingStyle = isDome ? "level3Dome" : "level3";
                        break;
                }
                if (!buildingStyle.equals("clear")) panes[j][i].getStyleClass().add(buildingStyle);
            }
        }
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                panes[i][j].getStyleClass().removeAll("highlighted");
            }
        }
    }

    public void setPaneClickable(ArrayList<Coords> coordsList) {
        for (Node pane : grid.getChildren()) {
            Integer x = (GridPane.getColumnIndex(pane) != null ? GridPane.getColumnIndex(pane) : 0);
            Integer y = (GridPane.getRowIndex(pane) != null ? GridPane.getRowIndex(pane) : 0);
            Coords gridCoords = new Coords(x, y);
            if (coordsList.contains(gridCoords)) {
                pane.setMouseTransparent(false);
            } else {
                pane.setMouseTransparent(true);
            }
        }
    }

    @FXML
    public void initializeMap(MouseEvent e) {
        textInfo1.setText("Tony");
        textInfo2.setText("Nanno");
        textInfo3.setText("Simone");

        File file1 = new File("resources/podium-characters-Jason.png");
        Image image1 = new Image(file1.toURI().toString());
        imageInfo1.setImage(image1);

        File file2 = new File("resources/podium-characters-Minotaur.png");
        Image image2 = new Image(file2.toURI().toString());
        imageInfo2.setImage(image2);

        File file3 = new File("resources/podium-characters-Morpheus.png");
        Image image3 = new Image(file3.toURI().toString());
        imageInfo3.setImage(image3);

        grid.setDisable(false);

    }

    //TODO ho scambiato j e i nell'ultima riga per vedere di aggiustare
    public void savePane() {
        int i,j;
        for (Node child : grid.getChildren()) {
            i = ( GridPane.getColumnIndex(child) != null ? GridPane.getColumnIndex(child) : 0);
            j = ( GridPane.getRowIndex(child) != null ? GridPane.getRowIndex(child) : 0);
            panes[j][i] = (Pane) child;
        }
    }


    public void OpponentDisconnection() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "A player disconnected from the game, you will be put in the lobby again", ButtonType.OK);
        alert.showAndWait();
    }

    public void backToLobbySceneChange() throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(new URL("file:./resources/lobby.fxml"));

        AnchorPane pane = loader.<AnchorPane>load();
        Scene lobby = new Scene(pane);
        lobby.getStylesheets().add("lobby.css");

        Stage stage = (Stage) (grid.getScene().getWindow());
        stage.setScene(lobby);

        Lobby lobby1 = loader.<Lobby>getController();
        lobby1.setGuiInput(guiInput);
        guiInput.setLoginController(lobby1);
    }

    public boolean askPlayAgain() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to play again?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES){
            TurnStatus.map.getGuiInput().getController().notifyPlayAgain("yes");
            return true;
        }
        else {
            TurnStatus.map.getGuiInput().getController().notifyPlayAgain("no");
        }

        return false;

    }

    public void askEffect(String god) {
        textInfo.setText("Do you want to use the effect of " + god + " ?");

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to use the effect of " + god + " ?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES) {
            TurnStatus.map.getGuiInput().getController().notifyEffect("yes");
            textInfo.setText("you selected yes");
        }
        else {
            TurnStatus.map.getGuiInput().getController().notifyEffect("no");
            textInfo.setText("you selected no");
        }

    }


    public void removeBlock(List<Coords> removableBlocks, boolean error) {
        if(error)
            textInfo.setText("There was an error with your last selection, please try again.");
        else
            textInfo.setText("You can remove a block only from the highlighted cells.");

        highlightCells(removableBlocks);

        grid.setDisable(false);
    }

    private void clear() {
        for (Node pane : grid.getChildren()) {
            pane.getStyleClass().clear();
            pane.setDisable(true);
        }
    }

    //TODO ho scambiato xx e yy nelle iniz per vedere di sistemare
    private void highlightCells(List<Coords> checkMoveCells) {
        for (Node pane : grid.getChildren()) {
            int yy = (GridPane.getColumnIndex(pane) != null ? GridPane.getColumnIndex(pane) : 0);
            int xx = (GridPane.getRowIndex(pane) != null ? GridPane.getRowIndex(pane) : 0);
            Coords tempCoords = new Coords(xx, yy);
            if (checkMoveCells.contains(tempCoords)) {
                pane.getStyleClass().add("highlighted");
                pane.setDisable(false);
            }
        }
    }

    public void build(List<Coords> checkBuildCells) {

        textInfo.setText("Now, you have to build a block. You can build on the highlighted cells.");

        highlightCells(checkBuildCells);

        grid.setDisable(false);
    }

    public void move(List<Coords> checkMoveCells) {

        textInfo.setText("It's time to move your worker! You can move him only on the highlighted cells");

        highlightCells(checkMoveCells);

        grid.setDisable(false);
    }

    public void chooseBuilder() {

        textInfo.setText("It's your turn! Please select one of your builders");
        grid.setDisable(false);
    }


    public void builderSetup(boolean callnumber) {
        if (!callnumber)
            textInfo.setText("Please, place your second builder");
        else
            textInfo.setText("It's your turn! Please place your first builder");

        setupHighlight();

        grid.setDisable(false);
    }

    //TODO ho cambiato x e y nella penultima riga per vedere di sistemare
    /**
     * this cycles all the grid panes and check if any pane has an ImageView as child. if it does checks its url.
     * if the pane doesn't have an image or it's url is clear, a new Coords is created and added to checkSetupList
     */
    private void setupHighlight() {
        List<Coords> checkSetupList = new ArrayList<>();

        for(int i=0;i<5;i++)
        {
            for(int j=0;j<5;j++)
                checkSetupList.add(new Coords(i,j));
        }

        for(Node pane : grid.getChildren()) {
            int x = (GridPane.getColumnIndex(pane) != null ? GridPane.getColumnIndex(pane) : 0);
            int y = (GridPane.getRowIndex(pane) != null ? GridPane.getRowIndex(pane) : 0);

            ImageView img = null;
            if(pane instanceof ImageView)
                img = (ImageView)pane;
            else
                continue;

            if(!img.getImage().getUrl().equals("clear"))
                checkSetupList.remove(new Coords(y,x));
        }

        highlightCells(checkSetupList);
    }

    public void setStatus(TurnStatus status) {
        this.status = status;
    }

    public GuiInput getGuiInput() {
        return guiInput;
    }

    public void setGuiInput(GuiInput guiInput) {
        this.guiInput = guiInput;
    }


}
