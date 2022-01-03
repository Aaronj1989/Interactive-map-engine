package application.controllers;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import application.model.RuleCreator;
import application.model.USState;
import cz.vutbr.web.css.CSSException;
import cz.vutbr.web.css.CSSFactory;
import cz.vutbr.web.css.Declaration;
import cz.vutbr.web.css.RuleFactory;
import cz.vutbr.web.css.RuleSet;
import cz.vutbr.web.css.StyleSheet;
import cz.vutbr.web.csskit.RuleFactoryImpl;
import cz.vutbr.web.csskit.TermFactoryImpl;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Accordion;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.HBox;

public class StateCustomizerController extends TitledPane {

	private USState state;
	@FXML
	protected ColorPicker stateColor;
	@FXML
	protected ColorPicker stateHoverColor;
	@FXML
	protected TextArea tooltipTextArea;
	@FXML
	protected TextArea actionLinkTextArea;
	@FXML
	private Label customizerLabel;
	@FXML
	private Label paneLabel;
	@FXML
	protected Button removeStateBttn;
	@FXML
	private CheckBox stateCheckBox;
	@FXML
	private HBox region;
	private RuleSet rule;
	private Map<String, USState> stateMap;
	private Declaration fill;
	private static Document defaultDoc;
	protected static Document customDoc;
	protected static StyleSheet styleSheet;
	private Element defaultElement;
	private Element customElement;
	private static InteractiveDashboardController dashboardParentController;
	protected final static String CUSTOM_CSS_FILE = "/styles/map-styles.css";
	protected final static String CUSTOM_HTML_FILE = "/images/maps/custom-map.html";

	public StateCustomizerController(USState state, Map<String, USState> stateMap) {
		this.state = state;
		this.stateMap = stateMap;
		loadFXML();
		generateCustomFile();
		readyTooltipListener();
		readyActionLinkListener();
		removeStateCheckBox();
	}

	public StateCustomizerController(USState multiState) {
		loadFXML();
	}

	public USState getState() {
		return state;
	}

	public void setState(USState state) {
		this.state = state;
	}

	public ColorPicker getStateColor() {
		return stateColor;
	}

	public void setStateColor(ColorPicker stateColor) {
		this.stateColor = stateColor;
	}

	public ColorPicker getStateHoveredColor() {
		return stateHoverColor;
	}

	public void setStateColorHovered(ColorPicker stateHoverColor) {
		this.stateHoverColor = stateHoverColor;
	}

	public CheckBox getStateCheckBox() {

		return stateCheckBox;
	}

	public void setStateCheckBox(CheckBox stateCheckBox) {
		this.stateCheckBox = stateCheckBox;
	}

	public TextArea getToolTipTextArea() {
		return tooltipTextArea;
	}

	public void setToolTipTextArea(TextArea tooltipTextArea) {
		this.tooltipTextArea = tooltipTextArea;
	}

	public TextArea getActionLinkTextArea() {
		return actionLinkTextArea;
	}

	public void setActionLinkTextArea(TextArea actionLinkTextArea) {
		this.actionLinkTextArea = actionLinkTextArea;
	}

	public CheckBox getCheckbox() {
		return this.stateCheckBox;
	}

	public void removeStateCheckBox() {
		stateCheckBox.setVisible(false);
		stateCheckBox.managedProperty().bind(stateCheckBox.visibleProperty());
	}

	public void addStateCheckBox() {
		stateCheckBox.setVisible(true);
		stateCheckBox.managedProperty().bind(stateCheckBox.visibleProperty());
	}

	public void setStateLabelText() {
		this.customizerLabel.setText(this.state.getName());
	}

	public Map<String, USState> getStateMap() {
		return stateMap;
	}

	public void setStateMap(Map<String, USState> stateMap) {
		this.stateMap = stateMap;
	}

	public Element getElementOriginal() {
		return this.defaultElement;
	}

	public void setElement(Element defaultElement) {
		this.defaultElement = defaultElement;
	}

	public Element getCustomElement() {
		return this.customElement;
	}

	public void loadFXML() {
		try {
			FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/StateCustomizer.fxml"));
			fxmlLoader.setRoot(this);
			fxmlLoader.setController(this);
			fxmlLoader.load();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	// customizes default state color on the map
	public void customizeStateColor() {
		String color = "#" + stateColor.getValue().toString().substring(2, 8);
		if (this.customElement.attr("fill").equals(color)) {
			return;
		}

		this.customElement.attr("fill", color);
		updateHTML();
	}

	// customizes state color when a user hovers over it
	//if there is already a rule 
	public void customizeStateHoveringColor() {
		String color = stateHoverColor.getValue().toString().substring(2, 8);
		if (rule == null) {
			rule = RuleCreator.cssHoverColorRule(state, color);
			fill = rule.get(0);
			styleSheet.add(rule);
		} else {
			fill.clear();
			fill.add(TermFactoryImpl.getInstance().createColor(color));
		}
		updateCSS();
	}

	public void updateMap(String file, String fileContents) {
		try {
			FileWriter writer = new FileWriter(new File(getClass().getResource(file).toURI()));
			writer.write(fileContents);
			writer.flush();
			writer.close();
		} catch (IOException | URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dashboardParentController.reloadMapPreview();
	}

	public void updateHTML() {
		String content = customDoc.toString();
		updateMap(CUSTOM_HTML_FILE, content);
	}

	//filters out the stylesheets brackets and overwrites the css file
	public void updateCSS() {
		String content = styleSheet.toString().replaceAll("[\\[\\]]", "").replaceAll("(}\\s*,)", "} \n\n");
		updateMap(CUSTOM_CSS_FILE, content);
	}

	public static void setDashBoardParent(InteractiveDashboardController dashboardParentController) {
		StateCustomizerController.dashboardParentController = dashboardParentController;
	}

	// allows the tooltip text area to listen for tooltip/info changes for any state
	public void readyTooltipListener() {
		this.tooltipTextArea.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(final ObservableValue<? extends String> observable, final String oldValue,
					final String newValue) {
				customElement.attr("data-info", "<div>" + newValue.replace("\n", "<br>") + "</div>");
				updateHTML();
			}
		});
	}

	// customizes HTMLActionLink for any state
	public void readyActionLinkListener() {
		this.actionLinkTextArea.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> arg0, String oldURL, String newURL) {
				customElement.attr("data-link", newURL.trim());
				updateHTML();
			}
		});
	}

	// removes state from customization list
	public void removeStateCustomizer() {
		Accordion accordion = (Accordion) this.getParent();
		this.customElement.attr("fill", defaultElement.attr("fill"));
		this.customElement.attr("data-info", defaultElement.attr("data-info"));
		styleSheet.remove(rule);
		stateMap.remove(this.customizerLabel.getText());
		accordion.getPanes().remove(this);
		rule = null;
		updateHTML();
		updateCSS();
	}
//This function copies the elements from the default HTML file to a custom HTML file that the user can make changes on.
	public void generateCustomFile() {
		try {
			File input = new File(getClass().getResource("/images/maps/custom-map.html").toURI());
			if (defaultDoc == null) {
				defaultDoc = Jsoup.parse(input, "UTF-8");
				customDoc = defaultDoc.clone();
				styleSheet = CSSFactory.parse(getClass().getResource(CUSTOM_CSS_FILE), "UTF-8");
			}
		} catch (IOException | CSSException | URISyntaxException ex) {
			ex.printStackTrace();
			throw new RuntimeException(ex);
		}
		this.defaultElement = defaultDoc.select("#" + state.getAbbr()).first();
		this.customElement = customDoc.select("#" + state.getAbbr()).first();
	}

	// gets the interactive dashboard controller
	public static InteractiveDashboardController getDashboardParent() {
		return dashboardParentController;
	}

}
