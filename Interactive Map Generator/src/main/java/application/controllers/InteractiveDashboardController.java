package application.controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import java.util.HashMap;

import java.util.Map;
import java.util.Scanner;

import application.model.FileDuplicator;
import application.model.HTMLSaveFilter;
import application.model.USState;

import javafx.fxml.FXML;

import javafx.scene.control.Accordion;
import javafx.scene.control.Button;

import javafx.scene.control.ListView;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;

import javafx.scene.layout.Pane;

import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class InteractiveDashboardController {
	@FXML
	private Button doneBttn;
	@FXML
	private ListView statesListView;
	@FXML
	private WebView mapPreview;
	@FXML
	private Pane interactiveContentPane;
	@FXML
	private ScrollPane interactiveDashboardContainer;
	@FXML
	private RadioButton multiEditButton;
	@FXML
	private RadioButton selectAllBttn;
	@FXML
	private Accordion customStateContainer;
	@FXML
	private Accordion multiStateContainer;
	@FXML
	private Button helloBttn;
	private MultiStateCustomizerController multiStateCustomizer;
	private Object[] statesArr;
	private Map<String, USState> stateMap;
	private FileChooser fileChooser;

	public InteractiveDashboardController() {
		stateMap = new HashMap<String, USState>();
		readyFileChooser();
	}

	public WebView getMapPreview() {
		return mapPreview;
	}

	public void setMapPreview(WebView mapPreview) {
		this.mapPreview = mapPreview;
	}

	public ListView getStatesListView() {
		return statesListView;
	}

	public void setStatesListView(ListView statesListView) {
		this.statesListView = statesListView;
	}

	/**
	 * adds state to customization list when clicked if a state is already in the
	 * statemap, then it won't be added again to avoid duplicate custom states
	 */
	public void addStateToCustomize() {
		USState state = (USState) this.statesListView.getSelectionModel().getSelectedItem();
		StateCustomizerController stateCustomizer = null;
		if (StateCustomizerController.getDashboardParent() == null) {
			StateCustomizerController.setDashBoardParent(this);
		}
		if (stateMap.get(state.getName()) == null) {
			stateMap.put(state.getName(), state);
			stateCustomizer = new StateCustomizerController(state, stateMap);
			stateCustomizer.setStateLabelText();
			customStateContainer.getPanes().add(stateCustomizer);
		}
	}

//enables and adds multistate editor to top of the list
	public void addMultiStateEditor() {
		if (multiStateContainer.getPanes().isEmpty()) {
			USState multiState = new USState("multi");
			multiStateCustomizer = new MultiStateCustomizerController(multiState);
			multiStateContainer.getPanes().add(multiStateCustomizer);
		}
		multiStateContainer.setVisible(true);
		multiStateContainer.managedProperty().bind(multiStateContainer.visibleProperty());
		multiStateCustomizer.removeStateCheckBox();
		multiStateCustomizer.hideRemoveStateButton();
		multiStateCustomizer.setStatesArr(statesArr);
	}

//disables and removes multistate editor 
	public void removeMultiStateEditor() {
		multiStateContainer.setVisible(false);
		multiStateCustomizer.setExpanded(false);
		multiStateContainer.managedProperty().bind(multiStateContainer.visibleProperty());
	}

//loads map preview into web browser
	public void loadMap() {
		WebView browser = this.getMapPreview();
		WebEngine engine = browser.getEngine();
		URL url = this.getClass().getResource("/images/maps/custom-map.html");
		engine.load(url.toString());
	}

	/**
	 * enables multi edit mode where you can edit multiple states at the same time.
	 * makes checkboxes visible on multiple states which allows them to be apart of
	 * the multi edit. When unselected, returns to singular mode removing the
	 * checkboxes from the states.
	 */
	public void multiEditMode() {
		if (customStateContainer.getPanes().isEmpty()) {
			return;
		}
		statesArr = customStateContainer.getPanes().toArray();
		for (Object stateController : statesArr) {
			StateCustomizerController controller = (StateCustomizerController) stateController;
			if (multiEditButton.isSelected()) {
				controller.addStateCheckBox();
			} else {
				controller.removeStateCheckBox();
			}
		}
		if (multiEditButton.isSelected()) {
			addMultiStateEditor();
		} else {
			removeMultiStateEditor();
		}
	}

	// reloads map in the viewport
	public void reloadMapPreview() {
		WebView browser = this.getMapPreview();
		WebEngine engine = browser.getEngine();
		engine.reload();
	}

	// saves html file to disc
	public void saveMap() {
		Stage parentFrame = new Stage();
		File userDirectory = null;
		fileChooser.setTitle("Save HTML file");
		fileChooser.getExtensionFilters().addAll(new ExtensionFilter("html file", "*.html"));
		File customHtml = null;
		File fileToSave = null;
		try {
			customHtml = new File(getClass().getResource("/images/maps/custom-map.html").toURI());
			fileToSave = fileChooser.showSaveDialog(parentFrame);
			String userDirectoryString = System.getProperty("user.home");
			userDirectory = new File(userDirectoryString);
			fileChooser.setInitialDirectory(userDirectory);

		} catch (Exception ex) {

			ex.printStackTrace();
		}
		if (fileToSave != null) {
			FileDuplicator duplicator = new FileDuplicator();
			duplicator.duplicateFile(customHtml, fileToSave, true);
		}
	}
//loads file chooser
	public void readyFileChooser() {
		fileChooser = new FileChooser();
	}

	// selects all states to be affected by the multistate customizer
	public void selectAll() {
		if (!multiEditButton.isSelected()) {
			selectAllBttn.setSelected(false);
			return;
		}

		if (selectAllBttn.isSelected()) {
			multiStateCustomizer.selectAll();
		} else {
			multiStateCustomizer.deselectAll();
		}
	}
}
