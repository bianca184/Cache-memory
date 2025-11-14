package org.example.memorie;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.*;



public class CacheController {
    @FXML
    private Button readButton;

    @FXML
    private TextField addressField;

    @FXML
    private TextArea outputArea;

    @FXML private TableView<CacheSimulator.CacheLine> cacheTable;
    @FXML private TableColumn<CacheSimulator.CacheLine, Integer> indexColumn;
    @FXML private TableColumn<CacheSimulator.CacheLine, Boolean> validColumn;
    @FXML private TableColumn<CacheSimulator.CacheLine, Integer> tagColumn;
    @FXML private TableColumn<CacheSimulator.CacheLine, Integer> dataColumn;



    private CacheSimulator simulator = new CacheSimulator(8);
    private ObservableList<CacheSimulator.CacheLine> cacheData;

    private String mode;
    public void setMode(String mode) {
        this.mode = mode;
        outputArea.appendText("Mod selectat: " +mode+"\n");
    }

    @FXML
    public void initialize() {
        cacheData = FXCollections.observableArrayList(simulator.getCacheLines());
        cacheTable.setItems(cacheData);
        indexColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleIntegerProperty(
                        cacheData.indexOf(cellData.getValue())).asObject());
        validColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleBooleanProperty(cellData.getValue().valid).asObject());
        tagColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().tag).asObject());
        dataColumn.setCellValueFactory(cellData ->
                new javafx.beans.property.SimpleIntegerProperty(cellData.getValue().data).asObject());
    }


        @FXML
    protected void onRead() {
        try{
        int address = Integer.parseInt(addressField.getText());
        boolean hit = false;

        if(mode.equals("Direct Mapped"))
        {
            hit=simulator.read(address);
        } else if(mode.equals("Fully Associative (FIFO)"))
        {
            hit=simulator.readFIFO(address);
        }

        outputArea.appendText((hit ? "HIT" : "MISS") + " la adresa " + address + "\n");
            cacheTable.refresh();

        } catch (NumberFormatException e) {
        outputArea.appendText("Introduceți o adresă validă!\n");
}


        }
}