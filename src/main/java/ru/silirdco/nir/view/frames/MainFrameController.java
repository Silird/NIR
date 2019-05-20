package ru.silirdco.nir.view.frames;

import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.silirdco.nir.core.entities.Multioperation;
import ru.silirdco.nir.core.util.*;
import ru.silirdco.nir.core.util.entity.Operations;
import ru.silirdco.nir.view.util.FileUtil;
import ru.silirdco.nir.view.util.Structure;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.CompletableFuture;

// TODO: Сохранение результатов

@SuppressWarnings("unused")
public class MainFrameController implements Initializable {
    private static final Logger logger = LoggerFactory.getLogger(MainFrameController.class);

    @FXML
    private Button butCalculate;
    @FXML
    private Button butUndo;
    @FXML
    private CheckBox checkNull;
    @FXML
    private CheckBox checkOne;
    @FXML
    private CheckBox checkUniversal;
    @FXML
    private CheckBox checkSubstitution;
    @FXML
    private CheckBox checkMegaoperation;
    @FXML
    private CheckBox checkSuperposition;
    @FXML
    private CheckBox checkCross;
    @FXML
    private CheckBox checkUnion;
    @FXML
    private CheckBox checkAddition;
    @FXML
    private CheckBox checkTransposition;

    @FXML
    private Label labelCount;

    @FXML
    private ProgressBar progressBar;
    @FXML
    private Button butClear;
    @FXML
    private Button butCalculateClosedSet;

    @FXML
    private Label labelSelected;
    @FXML
    private Button butRestoreInitial;
    @FXML
    private Button butRestoreFinal;
    @FXML
    private Button butRemove;
    @FXML
    private Button butSave;
    @FXML
    private Button butLoad;
    @FXML
    private TableView<SavedIteration> table;

    @FXML
    private HBox hboxMultioperations;

    private List<CheckBox> checkBoxesMultioperations;

    private SavedIteration lastIteration;

    private ObjectProperty<SavedIteration> selectedIterationProperty = new SimpleObjectProperty<>();

    private IntegerProperty selectCountProperty = new SimpleIntegerProperty(0);

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initTable();
        initCheckBoxes();
        initListeners();
    }

    private void initTable() {
        TableColumn<SavedIteration, String> column = new TableColumn<>();
        table.getColumns().add(column);

        column.setText("Итерации");
        column.setCellValueFactory(new PropertyValueFactory<>("name"));
        //column.setCellValueFactory(param -> new SimpleObjectProperty<>(Structure.formatDateTime.format(param.getValue().getName())));

        table.setPlaceholder(new Label());
    }

    private void initListeners() {
        butCalculateClosedSet.setOnAction(event -> {
            butCalculateClosedSet.setDisable(true);
            butCalculate.setDisable(true);
            progressBar.setProgress(ProgressIndicator.INDETERMINATE_PROGRESS);
            CompletableFuture.runAsync(() -> {
                Operations operations = new Operations();
                if (checkNull.isSelected()) {
                    operations.setNulll(true);
                }
                if (checkOne.isSelected()) {
                    operations.setOne(true);
                }
                if (checkUniversal.isSelected()) {
                    operations.setUniversal(true);
                }

                if (checkTransposition.isSelected()) {
                    operations.setTransposition(true);
                }
                if (checkAddition.isSelected()) {
                    operations.setAddition(true);
                }
                if (checkMegaoperation.isSelected()) {
                    operations.setMegaoperation(true);
                }
                if (checkCross.isSelected()) {
                    operations.setCross(true);
                }
                if (checkUnion.isSelected()) {
                    operations.setUnion(true);
                }
                if (checkSubstitution.isSelected()) {
                    operations.setSubstitution(true);
                }
                if (checkSuperposition.isSelected()) {
                    operations.setSuperposition(true);
                }

                ChangeListener<? super Number> changeListener =
                        (observable, oldValue, newValue) ->
                                Platform.runLater(() ->
                                        progressBar.setProgress(VarUtils.getDouble((Double) newValue)));
                ClosedSetUtil.progressProperty.addListener((observable, oldValue, newValue) ->
                        Platform.runLater(() ->
                                progressBar.setProgress(VarUtils.getDouble((Double) newValue))));
                //ClosedSetUtil.progressProperty.bind(progressBar.progressProperty());
                //progressBar.progressProperty().bind(ClosedSetUtil.progressProperty);
                List<String> vectors = getCheckedMultioperations();
                Set<Multioperation> multioperations = new TreeSet<>();
                for (String vector : vectors) {
                    multioperations.add(new Multioperation(vector));
                }
                Collection<Collection<Multioperation>> closedSets = ClosedSetUtil.getClosedSets(operations, multioperations);
                Platform.runLater(() -> {
                    ClosedSetUtil.progressProperty.removeListener(changeListener);
                    progressBar.setProgress(0);
                    butCalculate.setDisable(false);
                    butCalculateClosedSet.setDisable(false);
                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Количество замкнутых множеств: " + closedSets.size());
                    alert.showAndWait();
                });
            });
        });

        butCalculate.setOnAction(event -> {
            butCalculateClosedSet.setDisable(true);
            butCalculate.setDisable(true);
            progressBar.setProgress(ProgressIndicator.INDETERMINATE_PROGRESS);
            CompletableFuture.runAsync(() -> {
                calculate();
                Platform.runLater(() -> {
                    progressBar.setProgress(0);
                    butCalculate.setDisable(false);
                    butCalculateClosedSet.setDisable(false);
                });
            });
        });

        butClear.setOnAction(event -> {
            for (CheckBox checkBox : checkBoxesMultioperations) {
                checkBox.setSelected(false);
            }
        });

        butUndo.setOnAction(event -> {
            if (lastIteration != null) {
                clearCheckBoxes();
                for (CheckBox checkBox : getCheckBoxes(lastIteration.getInitialMultioperations())) {
                    checkBox.setSelected(true);
                }
                lastIteration = null;
            }
        });

        selectCountProperty.addListener((observable, oldValue, newValue) ->
                labelCount.setText(String.valueOf(VarUtils.getInteger((Integer) newValue))));

        selectedIterationProperty.addListener(((observable, oldValue, newValue) -> {
            Platform.runLater(() -> {
                if (newValue != null) {
                    labelSelected.setText(newValue.getName());
                    butRestoreInitial.setDisable(false);
                    butRestoreFinal.setDisable(false);
                    butRemove.setDisable(false);
                    butSave.setDisable(false);
                }
                else {
                    labelSelected.setText("");
                    butRestoreInitial.setDisable(true);
                    butRestoreFinal.setDisable(true);
                    butRemove.setDisable(true);
                    butSave.setDisable(true);
                }
            });
        }));

        butRestoreInitial.setOnAction(event -> {
            SavedIteration selectedIteration = selectedIterationProperty.get();
            if (selectedIteration != null) {
                clearCheckBoxes();
                for (String multioperation : selectedIteration.getInitialMultioperations()) {
                    getCheckBox(multioperation).setSelected(true);
                }

                checkNull.setSelected(selectedIteration.getOperations().isNulll());
                checkOne.setSelected(selectedIteration.getOperations().isOne());
                checkUniversal.setSelected(selectedIteration.getOperations().isUniversal());
                checkSubstitution.setSelected(selectedIteration.getOperations().isSubstitution());
                checkMegaoperation.setSelected(selectedIteration.getOperations().isMegaoperation());
                checkSuperposition.setSelected(selectedIteration.getOperations().isSuperposition());
                checkCross.setSelected(selectedIteration.getOperations().isCross());
                checkUnion.setSelected(selectedIteration.getOperations().isUnion());
                checkAddition.setSelected(selectedIteration.getOperations().isAddition());
                checkTransposition.setSelected(selectedIteration.getOperations().isTransposition());
            }
            else {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Не выбрал элемент отката");
                alert.showAndWait();
            }
        });

        butRestoreFinal.setOnAction(event -> {
            SavedIteration selectedIteration = selectedIterationProperty.get();
            if (selectedIteration != null) {
                clearCheckBoxes();
                for (String multioperation : selectedIteration.getFinalMultioperations()) {
                    getCheckBox(multioperation).setSelected(true);
                }

                checkNull.setSelected(selectedIteration.getOperations().isNulll());
                checkOne.setSelected(selectedIteration.getOperations().isOne());
                checkUniversal.setSelected(selectedIteration.getOperations().isUniversal());
                checkSubstitution.setSelected(selectedIteration.getOperations().isSubstitution());
                checkMegaoperation.setSelected(selectedIteration.getOperations().isMegaoperation());
                checkSuperposition.setSelected(selectedIteration.getOperations().isSuperposition());
                checkCross.setSelected(selectedIteration.getOperations().isCross());
                checkUnion.setSelected(selectedIteration.getOperations().isUnion());
                checkAddition.setSelected(selectedIteration.getOperations().isAddition());
                checkTransposition.setSelected(selectedIteration.getOperations().isTransposition());
            }
            else {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Не выбрал элемент отката");
                alert.showAndWait();
            }
        });

        butRemove.setOnAction(event -> {
            SavedIteration selectedIteration = selectedIterationProperty.get();
            if (selectedIteration != null) {
                selectedIterationProperty.setValue(null);
                table.getItems().remove(selectedIteration);
                table.refresh();
            }
            else {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Не выбрал элемент отката");
                alert.showAndWait();
            }
        });

        butSave.setOnAction(event -> {
            SavedIteration selectedIteration = selectedIterationProperty.get();
            if (selectedIteration != null) {
                try {
                    File file = FileUtil.getSaveFile();
                    FileUtil.writeText(file, Structure.getGson().toJson(selectedIteration));
                }
                catch (IOException ex) {
                    ExceptionHandler.handle(logger, ex);

                    Alert alert = new Alert(Alert.AlertType.ERROR, "Ошибка сохранения файла");
                    alert.showAndWait();
                }
            }
            else {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Не выбрал элемент отката");
                alert.showAndWait();
            }
        });

        butLoad.setOnAction(event -> {
            try {
                File file = FileUtil.openFile();
                if (file != null) {
                    String iteration = FileUtil.readText(file);
                    SavedIteration savedIteration = Structure.getGson().fromJson(iteration, SavedIteration.class);
                    selectedIterationProperty.setValue(savedIteration);
                    table.getItems().add(savedIteration);
                }
            }
            catch (IOException ex) {
                ExceptionHandler.handle(logger, ex);

                Alert alert = new Alert(Alert.AlertType.ERROR, "Ошибка чтения файла");
                alert.showAndWait();
            }
        });

        table.setOnMouseClicked(t -> {
            if (t.getClickCount() == 1 && !table.getSelectionModel().isEmpty()) {
                selectedIterationProperty.setValue(table.getSelectionModel().getSelectedItem());
            }
        });

        labelSelected.setOnMouseClicked(t -> {
            if (t.getClickCount() == 2) {
                Pane parent = (Pane) labelSelected.getParent();
                parent.getChildren().remove(labelSelected);
                TextField textField = new TextField();
                textField.setText(labelSelected.getText());
                parent.getChildren().add(textField);
                textField.requestFocus();
                textField.positionCaret(textField.getText().length() - 1);
                textField.selectAll();
                textField.setOnAction(event -> {
                    selectedIterationProperty.get().setName(textField.getText());
                    labelSelected.setText(textField.getText());
                    parent.getChildren().remove(textField);
                    parent.getChildren().add(labelSelected);
                    table.refresh();
                });
                textField.focusedProperty().addListener(((observable, oldValue, newValue) -> {
                    if (!VarUtils.getBoolean(newValue) && parent.getChildren().contains(textField)) {
                        selectedIterationProperty.get().setName(textField.getText());
                        labelSelected.setText(textField.getText());
                        parent.getChildren().remove(textField);
                        parent.getChildren().add(labelSelected);
                        table.refresh();
                    }
                }));
            }
        });

        addCountListeners();
//        checkNull.selectedProperty().bindBidirectional(getCheckBox("0000").selectedProperty());
//        checkOne.selectedProperty().bindBidirectional(getCheckBox("1122").selectedProperty());
//        checkUniversal.selectedProperty().bindBidirectional(getCheckBox("3333").selectedProperty());
    }

    private void addCountListeners() {
        for (CheckBox checkBox : checkBoxesMultioperations) {
            checkBox.selectedProperty().addListener(((observable, oldValue, newValue) -> {
               if ((oldValue != null) && (newValue != null)) {
                   if (newValue && !oldValue) {
                       selectCountProperty.setValue(selectCountProperty.get() + 1);
                   }
                   else if (!newValue && oldValue) {
                       selectCountProperty.setValue(selectCountProperty.get() - 1);
                   }
               }
            }));
        }
    }

    private void calculate() {
        lastIteration = new SavedIteration();
        lastIteration.setName(Structure.formatDateTime.format(new Date()));
        lastIteration.setInitialMultioperations(new ArrayList<>());
        lastIteration.setFinalMultioperations(new ArrayList<>());
        lastIteration.setOperations(new Operations());

        List<String> vectors = getCheckedMultioperations();
        lastIteration.getInitialMultioperations().addAll(vectors);
        Set<Multioperation> multioperations = new TreeSet<>();
        for (String vector : vectors) {
            multioperations.add(new Multioperation(vector));
        }

        if (checkNull.isSelected()) {
            lastIteration.getOperations().setNulll(true);
            multioperations.add(new Multioperation("0000"));
        }
        if (checkOne.isSelected()) {
            lastIteration.getOperations().setOne(true);
            multioperations.add(new Multioperation("1122"));
            multioperations.add(new Multioperation("1212"));
        }
        if (checkUniversal.isSelected()) {
            lastIteration.getOperations().setUniversal(true);
            multioperations.add(new Multioperation("3333"));
        }

        if (checkTransposition.isSelected()) {
            lastIteration.getOperations().setTransposition(true);
        }
        if (checkAddition.isSelected()) {
            lastIteration.getOperations().setAddition(true);
        }
        if (checkMegaoperation.isSelected()) {
            lastIteration.getOperations().setMegaoperation(true);
        }
        if (checkCross.isSelected()) {
            lastIteration.getOperations().setCross(true);
        }
        if (checkUnion.isSelected()) {
            lastIteration.getOperations().setUnion(true);
        }
        if (checkSubstitution.isSelected()) {
            lastIteration.getOperations().setSubstitution(true);
        }
        if (checkSuperposition.isSelected()) {
            lastIteration.getOperations().setSuperposition(true);
        }

        Set<Multioperation> newMultioperations = new TreeSet<>();
        do {
            multioperations.addAll(newMultioperations);
            int left = 256 - multioperations.size();
            if (left <= 0) {
                break;
            }

            newMultioperations.clear();

            if (checkTransposition.isSelected()) {
                EnumerationUtil.enumerate(1, multioperations, oneList -> {
                    Multioperation newMultioperation = MultioperationUtil.getTransposition(oneList.get(0));
                    if (!multioperations.contains(newMultioperation)) {
                        newMultioperations.add(newMultioperation);
                    }
                });

                if (!newMultioperations.isEmpty()) {
                    continue;
                }
            }

            if (checkAddition.isSelected()) {
                EnumerationUtil.enumerate(1, multioperations, oneList -> {
                    Multioperation newMultioperation = MultioperationUtil.getAddition(oneList.get(0));
                    if (!multioperations.contains(newMultioperation)) {
                        newMultioperations.add(newMultioperation);
                    }
                });

                if (!newMultioperations.isEmpty()) {
                    continue;
                }
            }

            if (checkMegaoperation.isSelected()) {
                EnumerationUtil.enumerate(1, multioperations, oneList -> {
                    Multioperation newMultioperation = MultioperationUtil.getFirstMegaoperation(oneList.get(0));
                    if (!multioperations.contains(newMultioperation)) {
                        newMultioperations.add(newMultioperation);
                    }
                });

                if (!newMultioperations.isEmpty()) {
                    continue;
                }
            }

            if (checkCross.isSelected()) {
                EnumerationUtil.enumerate(2, multioperations, twoList -> {
                    Multioperation newMultioperation = MultioperationUtil.getCross(twoList.get(0), twoList.get(1));
                    if (!multioperations.contains(newMultioperation)) {
                        newMultioperations.add(newMultioperation);
                    }
                });

                if (!newMultioperations.isEmpty()) {
                    continue;
                }
            }

            if (checkUnion.isSelected()) {
                EnumerationUtil.enumerate(2, multioperations, twoList -> {
                    Multioperation newMultioperation = MultioperationUtil.getUnion(twoList.get(0), twoList.get(1));
                    if (!multioperations.contains(newMultioperation)) {
                        newMultioperations.add(newMultioperation);
                    }
                });

                if (!newMultioperations.isEmpty()) {
                    continue;
                }
            }

            if (checkSubstitution.isSelected()) {
                EnumerationUtil.enumerate(2, multioperations, twoList -> {
                    Multioperation newMultioperation = MultioperationUtil.getSubstitution(twoList.get(0), twoList.get(1));
                    if (!multioperations.contains(newMultioperation)) {
                        newMultioperations.add(newMultioperation);
                    }
                });

                if (!newMultioperations.isEmpty()) {
                    continue;
                }
            }

            if (checkSuperposition.isSelected()) {
                EnumerationUtil.enumerate(3, multioperations, threeList -> {
                    Multioperation newMultioperation = MultioperationUtil.getSuperposition(
                            threeList.get(0),
                            threeList.get(1),
                            threeList.get(2));
                    if (!multioperations.contains(newMultioperation)) {
                        newMultioperations.add(newMultioperation);
                    }
                });
            }
        }
        while (!newMultioperations.isEmpty());


        for (Multioperation multioperation : multioperations) {
            lastIteration.getFinalMultioperations().add(multioperation.toStringVector());
        }
        selectedIterationProperty.setValue(lastIteration);
        Platform.runLater(() -> {
            table.getItems().add(lastIteration);
            for (Multioperation multioperation : multioperations) {
                getCheckBox(multioperation.toStringVector()).setSelected(true);
            }
        });
    }

    private void initCheckBoxes() {
        checkBoxesMultioperations = new ArrayList<>();
        for (Node container : hboxMultioperations.getChildren()) {
            if (container instanceof VBox) {
                for (Node node : ((VBox) container).getChildren()) {
                    if (node instanceof CheckBox) {
                        checkBoxesMultioperations.add((CheckBox) node);
                    }
                }
            }
        }

        List<String> accessors = new ArrayList<>();
        accessors.add("0");
        accessors.add("1");
        accessors.add("2");
        for (CheckBox checkBox : getCheckBoxesAccessors(accessors)) {
            checkBox.setTextFill(Color.BLUE);
        }

        accessors.clear();
        accessors.add("3");
        accessors.add("1");
        accessors.add("2");
        for (CheckBox checkBox : getCheckBoxesAccessors(accessors)) {
            checkBox.setTextFill(Color.RED);
        }

        accessors.clear();
        accessors.add("1");
        accessors.add("2");
        for (CheckBox checkBox : getCheckBoxesAccessors(accessors)) {
            checkBox.setTextFill(Color.PURPLE);
        }
    }

    private List<String> getCheckedMultioperations() {
        List<String> result = new ArrayList<>();
        for (CheckBox checkBox : checkBoxesMultioperations) {
            if (checkBox.isSelected()) {
                result.add(checkBox.getText());
            }
        }
        return result;
    }

    private CheckBox getCheckBox(String multioperation) {
        /*
        for (CheckBox checkBox : checkBoxesMultioperations) {
            if (Objects.equals(checkBox.getText(), multioperation)) {
                return checkBox;
            }
        }
        logger.error("CheckBox для мультиоперации " + multioperation + " отсутствует");
        throw new IllegalArgumentException();
        */

        return getCheckBoxes(Collections.singletonList(multioperation)).get(0);
    }

    private List<CheckBox> getCheckBoxes(Collection<String> multioperations) {
        List<CheckBox> checkBoxes = new ArrayList<>();
        for (CheckBox checkBox : checkBoxesMultioperations) {
            if (multioperations.contains(checkBox.getText())) {
                checkBoxes.add(checkBox);
            }
        }
        if (checkBoxes.isEmpty()) {
            logger.error("CheckBox для мультиоперации " + multioperations + " отсутствует");
            throw new IllegalArgumentException();
        }

        return checkBoxes;
    }

    private List<CheckBox> getCheckBoxesAccessors(Collection<String> accessors) {
        List<CheckBox> checkBoxes = new ArrayList<>();
        for (CheckBox checkBox : checkBoxesMultioperations) {
            if (containOnly(checkBox.getText(), accessors)) {
                checkBoxes.add(checkBox);
            }
        }

        return checkBoxes;
    }

    private boolean containOnly(String target, Collection<String> accessors) {
        for (int i = 0; i < target.length(); i++) {
            if (!accessors.contains(target.substring(i, i + 1))) {
                return false;
            }
        }
        return true;
    }

    private void clearCheckBoxes() {
        for (CheckBox checkBox : checkBoxesMultioperations) {
            checkBox.setSelected(false);
        }
    }
}
