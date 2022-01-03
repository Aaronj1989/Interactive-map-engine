package application.controllers;

import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import application.model.USState;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public class MultiStateCustomizerController extends StateCustomizerController {

	private Object[] statesArr;

	public MultiStateCustomizerController(USState multiState) {
		super(multiState);
		// TODO Auto-generated constructor stub
		readyToolTip();
		readyActionLink();
	}

	@Override
	public void customizeStateColor() {
		customizeStateColors();
	}

	@Override
	public void customizeStateHoveringColor() {
		customizeStatesHoveringColor();
	}

	public void setStatesArr(Object[] statesArr2) {
		this.statesArr = statesArr2;
	}

	public void customizeStateColors() {
		// get all custom elements
		// and then write them to a file one time.
		for (Object customizer : statesArr) {
			String color = "#" + stateColor.getValue().toString().substring(2, 8);
			StateCustomizerController stateCustomizer = (StateCustomizerController) customizer;
			if (stateCustomizer.getCheckbox().isSelected()) {
				String elementID = "#" + stateCustomizer.getState().getAbbr();
				Element element = customDoc.select(elementID).first();
				element.attr("fill", color);
			}
		}
		updateHTML();
	}

	public void customizeStatesHoveringColor() {
		for (Object customizer : statesArr) {
			StateCustomizerController stateCustomizer = (StateCustomizerController) customizer;
			if (stateCustomizer.getCheckbox().isSelected()) {
				stateCustomizer.setStateColorHovered(stateHoverColor);
				stateCustomizer.customizeStateHoveringColor();
			}
		}
		updateCSS();
	}

	public void hideRemoveStateButton() {
		removeStateBttn.setVisible(false);
		removeStateBttn.managedProperty().bind(removeStateBttn.visibleProperty());
	}

	// assigns a tooltip to multiple elements
	public void readyToolTip() {
		this.getToolTipTextArea().textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(final ObservableValue<? extends String> observable, final String oldValue,
					final String newValue) {
				String info = "<div>" + newValue.replace("\n", "<br>") + "</div>";
				for (Object customizer : statesArr) {
					StateCustomizerController stateCustomizer = (StateCustomizerController) customizer;
					if (stateCustomizer.getCheckbox().isSelected()) {
						String elementID = "#" + stateCustomizer.getState().getAbbr();
						Element element = customDoc.select(elementID).first();
						element.attr("data-info", info);
					}
				}
				updateHTML();
			}
		});
	}

	public void readyActionLink() {
		this.getActionLinkTextArea().textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(final ObservableValue<? extends String> observable, final String oldValue,
					final String newValue) {
				String link = newValue;
				for (Object customizer : statesArr) {
					StateCustomizerController stateCustomizer = (StateCustomizerController) customizer;
					if (stateCustomizer.getCheckbox().isSelected()) {
						String elementID = "#" + stateCustomizer.getState().getAbbr();
						Element element = customDoc.select(elementID).first();
						element.attr("data-link", link);
					}
				}
				updateHTML();
			}
		});
	}
//selects state to be apart of the group customization list
	public void selectAll() {
		for (Object customizer : statesArr) {
			StateCustomizerController stateCustomizer = (StateCustomizerController) customizer;
			stateCustomizer.getCheckbox().setSelected(true);
		}
	}
//deselects state from being apart of the group customization list
	public void deselectAll() {
		for (Object customizer : statesArr) {
			StateCustomizerController stateCustomizer = (StateCustomizerController) customizer;
			stateCustomizer.getCheckbox().setSelected(false);
		}
	}
}
