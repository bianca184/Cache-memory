package org.example.memorie;

import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.util.Duration;

public class CacheController {

    @FXML
    private Label modeLabel;

    @FXML
    private TextField addressField;

    @FXML
    private TextArea outputArea;

    @FXML
    private TableView<CacheSimulator.CacheLine> cacheTable;

    @FXML
    private TableColumn<CacheSimulator.CacheLine, Integer> indexColumn;

    @FXML
    private TableColumn<CacheSimulator.CacheLine, Boolean> validColumn;

    @FXML
    private TableColumn<CacheSimulator.CacheLine, Integer> tagColumn;

    @FXML
    private TableColumn<CacheSimulator.CacheLine, Integer> dataColumn;

    @FXML
    private Label totalAccessLabel;

    @FXML
    private Label hitLabel;

    @FXML
    private Label missLabel;

    @FXML
    private Label hitRateLabel;

    @FXML
    private Label durationLabel;

    @FXML
    private Button finishButton;

    @FXML
    private Button resetButton;


    @FXML
    private TextArea evictionArea;

    private CacheSimulator simulator;
    private ObservableList<CacheSimulator.CacheLine> cacheData;

    private String mode;
    private int lastAccessIndex = -1;
    private boolean lastHit = false;


    private long simulationStartTime;
    private int totalAccesses = 0;
    private int hitCount = 0;
    private int missCount = 0;
    private int evictionCount = 0;

    private Timeline timerTimeline;



    private void updateLiveDuration() {
        long now = System.currentTimeMillis();
        double seconds = (now - simulationStartTime) / 1000.0;
        durationLabel.setText(String.format("%.2f s", seconds));
    }

    public void initializeSimulator(String mode, int size) {
        this.mode = mode;

        modeLabel.setText("Mod: " + mode + " | mărime: " + size + " linii");

        simulator = new CacheSimulator(size);
        cacheData = FXCollections.observableArrayList(simulator.getCacheLines());
        cacheTable.setItems(cacheData);


        evictionCount = 0;
        if (evictionArea != null) {
            evictionArea.clear();
        }

        simulationStartTime = System.currentTimeMillis();

        initColumns();
        initRowHighlighting();


        timerTimeline = new Timeline(
                new KeyFrame(Duration.seconds(0.1), e -> updateLiveDuration())
        );
        timerTimeline.setCycleCount(Timeline.INDEFINITE);
        timerTimeline.play();


        addressField.setOnAction(e -> onRead());

        updateStats();
    }

    private void initColumns() {
        indexColumn.setCellValueFactory(c ->
                new SimpleIntegerProperty(cacheData.indexOf(c.getValue())).asObject());
        validColumn.setCellValueFactory(c ->
                new SimpleBooleanProperty(c.getValue().valid).asObject());
        tagColumn.setCellValueFactory(c ->
                new SimpleIntegerProperty(c.getValue().tag).asObject());
        dataColumn.setCellValueFactory(c ->
                new SimpleIntegerProperty(c.getValue().data).asObject());
    }

    private void initRowHighlighting() {
        cacheTable.setRowFactory(table -> new TableRow<CacheSimulator.CacheLine>() {
            @Override
            protected void updateItem(CacheSimulator.CacheLine item, boolean empty) {
                super.updateItem(item, empty);
                setStyle("");

                if (item == null || empty) {
                    return;
                }

                int rowIndex = getIndex();

                if (rowIndex == lastAccessIndex) {
                    if (lastHit) {
                        setStyle("-fx-background-color: #8CFF8C;");
                    } else {
                        setStyle("-fx-background-color: #FF8C8C;");
                    }

                    FadeTransition ft = new FadeTransition(Duration.millis(250), this);
                    ft.setFromValue(0.3);
                    ft.setToValue(1.0);
                    ft.play();
                }
            }
        });
    }



    @FXML
    protected void onRead() {
        try {
            int address = Integer.parseInt(addressField.getText());
            boolean hit;

            if (mode.equals("Direct Mapped")) {
                hit = simulator.read(address);
                lastAccessIndex = address % simulator.getCacheSize();
            } else {

                hit = simulator.readFIFO(address);
                lastAccessIndex = simulator.findTag(address);
            }

            lastHit = hit;

            totalAccesses++;
            if (hit) hitCount++;
            else missCount++;

            outputArea.appendText((hit ? "HIT" : "MISS") + " la adresa " + address + "\n");

            CacheSimulator.EvictionRecord ev = simulator.lastEviction;
            simulator.lastEviction = null;

            if (ev != null) {
                evictionCount++;


                int oldAddress;
                if (mode.equals("Direct Mapped")) {

                    oldAddress = ev.oldTag * simulator.getCacheSize() + ev.index;
                } else {

                    oldAddress = ev.oldTag;
                }

                if (evictionArea != null) {
                    evictionArea.appendText(
                            String.format(
                                    "Eviction #%d -> index=%d, oldTag=%d, oldAddr=%d, newTag=%d, addr=%d%n",
                                    evictionCount,
                                    ev.index,
                                    ev.oldTag,
                                    oldAddress,
                                    ev.newTag,
                                    ev.accessAddress
                            )
                    );
                }
            }


            cacheTable.refresh();
            updateStats();

        } catch (NumberFormatException e) {
            outputArea.appendText("Introduceți o adresă validă!\n");
        }
    }

    private void updateStats() {
        totalAccessLabel.setText(String.valueOf(totalAccesses));
        hitLabel.setText(String.valueOf(hitCount));
        missLabel.setText(String.valueOf(missCount));

        double rate = totalAccesses == 0 ? 0.0 : (hitCount * 100.0) / totalAccesses;
        hitRateLabel.setText(String.format("%.1f%%", rate));

        long now = System.currentTimeMillis();
        double seconds = (now - simulationStartTime) / 1000.0;
        durationLabel.setText(String.format("%.2f s", seconds));
    }

    @FXML
    protected void onFinish() {
        updateStats();

        Alert a = new Alert(Alert.AlertType.INFORMATION);
        a.setHeaderText("Rezultatele simulării");

        String content = "Total accesări: " + totalAccesses +
                "\nHIT-uri: " + hitCount +
                "\nMISS-uri: " + missCount +
                String.format("\nHit rate: %.1f%%",
                        (totalAccesses == 0 ? 0.0 : (hitCount * 100.0) / totalAccesses)) +
                "\nDurată: " + durationLabel.getText() +
                "\nEvictions: " + evictionCount;

        a.setContentText(content);
        a.showAndWait();
    }

    @FXML
    protected void onReset() {
        totalAccesses = 0;
        hitCount = 0;
        missCount = 0;
        lastAccessIndex = -1;
        lastHit = false;
        evictionCount = 0;

        outputArea.clear();
        if (evictionArea != null) {
            evictionArea.clear();
        }

        int size = simulator.getCacheSize();
        simulator = new CacheSimulator(size);
        cacheData.setAll(simulator.getCacheLines());

        simulationStartTime = System.currentTimeMillis();
        cacheTable.refresh();
        updateStats();
    }
}
