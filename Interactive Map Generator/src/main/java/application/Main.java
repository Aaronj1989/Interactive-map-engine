package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.CookieHandler;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Scanner;

import javax.tools.JavaCompiler;

import application.controllers.InteractiveDashboardController;
import application.model.FileDuplicator;
import application.model.USState;
import javafx.application.Application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;

import javafx.scene.Scene;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;


public class Main extends Application {

	private InteractiveDashboardController dashboardController;
	private File defaultHtmlFile = null;
	private File customHtmlFile = null;
	@Override
	public void start(Stage primaryStage) {
	setUpCustomMap();
		ObservableList<USState> statesList = FXCollections.observableArrayList(
				new USState("Alabama", "AL", "/images/flags/Alabama.svg.png"),
				new USState("Alaska", "AK", "/images/flags/Alaska.svg.png"),
				new USState("Arizona", "AZ", "/images/flags/Arizona.svg.png"),
				new USState("Arkansas", "AR", "/images/flags/Arkansas.svg.png"),
				new USState("California", "CA", "/images/flags/California.svg.png"),
				new USState("Colorado", "CO", "/images/flags/Colorado.svg.png"),
				new USState("Connecticut", "CT", "/images/flags/Connecticut.svg.png"),
				new USState("Delaware", "DE", "/images/flags/Delaware.svg.png"),
				new USState("Florida", "FL", "/images/flags/Florida.svg.png"),
				new USState("Georgia", "GA", "/images/flags/Georgia.svg.png"),
				new USState("Hawaii", "HI", "/images/flags/Hawaii.svg.png"),
				new USState("Idaho", "ID", "/images/flags/Idaho.svg.png"),
				new USState("Illinois", "IL", "/images/flags/Illinois.svg.png"),
				new USState("Indiana", "IN", "/images/flags/Indiana.svg.png"),
				new USState("Iowa", "IA", "/images/flags/Iowa.svg.png"),
				new USState("Kansas", "KS", "/images/flags/Kansas.svg.png"),
				new USState("Kentucky", "KY", "/images/flags/Kentucky.svg.png"),
				new USState("Louisiana", "LA", "/images/flags/Louisiana.svg.png"),
				new USState("Maine", "ME", "/images/flags/Maine.svg.png"),
				new USState("Maryland", "MD", "/images/flags/Maryland.svg.png"),
				new USState("Massachusetts", "MA", "/images/flags/Massachusetts.svg.png"),
				new USState("Michigan", "MI", "/images/flags/Michigan.svg.png"),
				new USState("Minnesota", "MN", "/images/flags/Minnesota.svg.png"),
				new USState("Mississippi", "MS", "/images/flags/Mississippi.svg.png"),
				new USState("Missouri", "MO", "/images/flags/Missouri.svg.png"),
				new USState("Montana", "MT", "/images/flags/Montana.svg.png"),
				new USState("Nebraska", "NE", "/images/flags/Nebraska.svg.png"),
				new USState("Nevada", "NV", "/images/flags/Nevada.svg.png"),
				new USState("New_Hampshire", "NH", "/images/flags/New_Hampshire.svg.png"),
				new USState("New_Jersey", "NJ", "/images/flags/New_Jersey.svg.png"),
				new USState("New_Mexico", "NM", "/images/flags/New_Mexico.svg.png"),
				new USState("New_York", "NY", "/images/flags/New_York.svg.png"),
				new USState("North_Carolina", "NC", "/images/flags/North_Carolina.svg.png"),
				new USState("North_Dakota", "ND", "/images/flags/North_Dakota.svg.png"),
				new USState("Ohio", "OH", "/images/flags/Ohio.svg.png"),
				new USState("Oklahoma", "OK", "/images/flags/Oklahoma.svg.png"),
				new USState("Oregon", "OR", "/images/flags/Oregon.svg.png"),
				new USState("Pennsylvania", "PA", "/images/flags/Pennsylvania.svg.png"),
				new USState("Rhode_Island", "RI", "/images/flags/Rhode_Island.svg.png"),
				new USState("South_Carolina", "SC", "/images/flags/South_Carolina.svg.png"),
				new USState("South_Dakota", "SD", "/images/flags/South_Dakota.svg.png"),
				new USState("Tennessee", "TN", "/images/flags/Tennessee.svg.png"),
				new USState("Texas", "TX", "/images/flags/Texas.svg.png"),
				new USState("Utah", "UT", "/images/flags/Utah.svg.png"),
				new USState("Vermont", "VT", "/images/flags/Vermont.svg.png"),
				new USState("Virginia", "VA", "/images/flags/Virginia.svg.png"),
				new USState("Washington", "WA", "/images/flags/Washington.svg.png"),
				new USState("West_Virginia", "WV", "/images/flags/West_Virginia.svg.png"),
				new USState("Wisconsin", "WI", "/images/flags/Wisconsin.svg.png"),
				new USState("Wyoming", "WY", "/images/flags/Wyoming.svg.png"));
                  
		try {
			FXMLLoader root = new FXMLLoader(getClass().getResource("/InteractiveDashboard.fxml"));
			Scene scene = new Scene(root.load());
	
			primaryStage.setScene(scene);
			resetFilesToDefault(primaryStage);
			dashboardController = root.getController();
	        dashboardController.loadMap();
			primaryStage.show();
			
			ListView statesListView = dashboardController.getStatesListView();
			statesListView.setItems(statesList);
			statesListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
			statesListView.setCellFactory(param -> new ListCell<USState>() {
				private ImageView displayImage = new ImageView();

				@Override
				public void updateItem(USState state, boolean empty) {
					super.updateItem(state, empty);

					if (empty) {
						setText(null);
						setGraphic(null);
					} else {
						Image image = new Image(state.getFlag());
						displayImage.setFitHeight(45);
						displayImage.setFitWidth(70);
						setText(state.getName());
						displayImage.setImage(image);
						setGraphic(displayImage);
					}
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
    //copies default map html contents to custom map html
	private void setUpCustomMap() {
		try {
	     defaultHtmlFile = new File(getClass().getResource("/setup/mapdefault.html").toURI());
		 customHtmlFile = new File(getClass().getResource("/images/maps/custom-map.html").toURI());
		}
		catch(URISyntaxException ex) {
			ex.printStackTrace();
		}
		FileDuplicator duplicator = new FileDuplicator();
		duplicator.duplicateFile(defaultHtmlFile, customHtmlFile, false);	
	}
 
	//clears changes and resets css and HTML files to their default state when user closes the main application window
	private void resetFilesToDefault(Stage primaryStage) {
	    FileDuplicator duplicator = new FileDuplicator();
		primaryStage.setOnCloseRequest(WindowEvent -> {
			try {
				File cssFile = new File(getClass().getResource("/styles/map-styles.css").toURI());
			    duplicator.duplicateFile(new File("defaultCss.css"), cssFile,false);
			} catch (URISyntaxException e) {
				e.printStackTrace();
			}
			duplicator.duplicateFile(defaultHtmlFile, customHtmlFile,false);
		});	
	}

	public static void main(String[] args) throws FileNotFoundException {
		launch(args);
	}
}
