package ru.silirdco.nir.view.frames;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.silirdco.nir.core.entities.Multioperation;
import ru.silirdco.nir.core.util.EnumerationUtil;
import ru.silirdco.nir.core.util.MultioperationUtil;

import java.net.URL;
import java.util.*;

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
    private TextField textMeassage;
    @FXML
    private Button butClear;


    @FXML
    private HBox hboxMultioperations;

    private List<CheckBox> checkBoxesMultioperations;

    private List<String> undoList = new ArrayList<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initCheckBoxes();
        initListeners();
    }

    private void initListeners() {
        butCalculate.setOnAction(event -> calculate());

        butClear.setOnAction(event -> {
            for (CheckBox checkBox : checkBoxesMultioperations) {
                checkBox.setSelected(false);
            }
        });

        butUndo.setOnAction(event -> {
            if (!undoList.isEmpty()) {
                clearCheckBoxes();
                for (CheckBox checkBox : getCheckBoxes(undoList)) {
                    checkBox.setSelected(true);
                }
                undoList.clear();
            }
        });

//        checkNull.selectedProperty().bindBidirectional(getCheckBox("0000").selectedProperty());
//        checkOne.selectedProperty().bindBidirectional(getCheckBox("1122").selectedProperty());
//        checkUniversal.selectedProperty().bindBidirectional(getCheckBox("3333").selectedProperty());
    }

    private void calculate() {
        List<String> vectors = getCheckedMultioperations();
        undoList.addAll(vectors);
        Set<Multioperation> multioperations = new TreeSet<>();
        for (String vector : vectors) {
            multioperations.add(new Multioperation(vector));
        }

        if (checkNull.isSelected()) {
            multioperations.add(new Multioperation("0000"));
        }
        if (checkOne.isSelected()) {
            multioperations.add(new Multioperation("1122"));
        }
        if (checkUniversal.isSelected()) {
            multioperations.add(new Multioperation("3333"));
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
            getCheckBox(multioperation.toStringVector()).setSelected(true);
        }
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

    private void clearCheckBoxes() {
        for (CheckBox checkBox : checkBoxesMultioperations) {
            checkBox.setSelected(false);
        }
    }
}
