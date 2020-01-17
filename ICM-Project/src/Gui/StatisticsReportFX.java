package Gui;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

import Common.Interval;
import LogicController.StatisticsReportController;
import Utilities.MessageObject;
import Utilities.RequestType;
import Utilities.Statistics;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class StatisticsReportFX extends BaseFX {
	// Side-Panel *********************************
	@FXML
	private AnchorPane isdPane;

	@FXML
	private VBox supervisorPanel;
	
	@FXML
	private VBox all_roles;
	
	// Request Report by Status *******************
	@FXML
	private StackPane statusGraphPane;
	
	@FXML
	private DatePicker statusFrom;
	@FXML
	private DatePicker statusTo;
	@FXML
	private ComboBox<String> statusComboBox;
	@FXML
	private Button statusIssueReportBtn;
	@FXML
	private Text statusIssueReportLabel;
	@FXML
	private Text statusStdLabel;
	@FXML
	private Text statusMedianLabel;
	
	// Request Report by Rejection ****************
	@FXML
	private StackPane rejectionGraphPane;
	
	@FXML
	private DatePicker rejectionFrom;
	@FXML
	private DatePicker rejectionTo;
	@FXML
	private Button rejectionIssueReportBtn;
	@FXML
	private Text rejectionIssueReportLabel;
	@FXML
	private Text rejectionStdLabel;
	@FXML
	private Text rejectionMedianLabel;
	
	// Request Report by Active Days ****************
	@FXML
	private StackPane activeDaysGraphPane;
	
	@FXML
	private DatePicker activeDaysFrom;
	@FXML
	private DatePicker activeDaysTo;
	@FXML
	private Button activeDaysIssueReportBtn;
	@FXML
	private Text activeDaysIssueReportLabel;
	@FXML
	private Text activeDaysStdLabel;
	@FXML
	private Text activeDaysMedianLabel;
	
	// Request Report by Extensions ***************
	@FXML
	private StackPane extensionsGraphPane;
	@FXML
	private Button extensionsIssueReportBtn;
	@FXML
	private Text extensionsIssueReportLabel;
	@FXML
	private Text extensionsStdLabel;
	@FXML
	private Text extensionsMedianLabel;
	
	// Request Report by Durations ****************
	@FXML
	private StackPane durationsGraphPane;
	@FXML
	private Button durationsIssueReportBtn;
	@FXML
	private Text durationsIssueReportLabel;
	@FXML
	private Text durationsStdLabel;
	@FXML
	private Text durationsMedianLabel;
	
	// Request Report by Delays *******************
	@FXML
	private StackPane delaysGraphPane;
	@FXML
	private Button delaysIssueReportBtn;
	@FXML
	private Text delaysIssueReportLabel;
	@FXML
	private Text delaysStdLabel;
	@FXML
	private Text delaysMedianLabel;
	@FXML
	private ComboBox<String> delaysComboBox;
	
	// Tab Panes **********************************
	@FXML
	private TabPane activityTabPane;
	@FXML
	private TabPane performancesTabPane;
	@FXML
	private TabPane delaysTabPane;
	
	// Tab Pane Buttons ***************************
	@FXML
	private Button activityBtn;
	@FXML
	private Button performanceBtn;
	@FXML
	private Button delayBtn;

	// Controller *********************************
	/** Logical controller of {@link StatisticsReportFX}*/
	private StatisticsReportController statisticsReportController;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		statisticsReportController = new StatisticsReportController(new Statistics());
		statisticsReportController.initPanelHBoxes(isdPane, all_roles, supervisorPanel);
		statisticsReportController.initStatusComboBox(statusComboBox);
		
		initGetInfoSystems();
	}

	public void clearFields() {
	}
	
	@FXML
	private void activityBtnWasPressed(ActionEvent event) {
		activityTabPane.setVisible(true);
		performancesTabPane.setVisible(false);
		delaysTabPane.setVisible(false);
	}
	
	@FXML
	private void performanceBtnWasPressed(ActionEvent event) {
		activityTabPane.setVisible(false);
		performancesTabPane.setVisible(true);
		delaysTabPane.setVisible(false);
	}
	
	@FXML
	private void delayBtnWasPressed(ActionEvent event) {
		activityTabPane.setVisible(false);
		performancesTabPane.setVisible(false);
		delaysTabPane.setVisible(true);
	}
	
	public void initGetInfoSystems() {
		statisticsReportController.initGetInfoSystems();
	}
	
	public void handleGetInfoSystems(MessageObject message) {
		delaysComboBox.getItems().clear();
		delaysComboBox.getItems().add("All");
		delaysComboBox.getItems().addAll((ArrayList<String>)message.getArgs().get(0));
	}
	
	// Request Report by Status ***************************************
	private void initGetReportStatusData(String status, LocalDate from, LocalDate to) {
		statusGraphPane.getChildren().clear();
		statisticsReportController.initGetReportStatusData(status, from, to);
	}
	
	public void handleGetReportStatusData(MessageObject message) {
		ArrayList<Interval> intervals = (ArrayList<Interval>)message.getArgs().get(0);
		String status = message.getArgs().get(1).toString();
		
		updateStatusReportLabels(intervals);
		fillGraph(statusGraphPane, intervals, status, status + " Report", "Status");
	}
	
	private void updateStatusReportLabels(ArrayList<Interval> intervals) {
		if (intervals == null) return;
		
		ArrayList<Integer> valueList = new ArrayList<>();
		for (int i = 0; i < intervals.size(); i++)
			valueList.add(intervals.get(i).getValue());
		
		float standardDeviation = statisticsReportController.getStatisticsUtility().GetStandardDeviation(valueList);
		float median = statisticsReportController.getStatisticsUtility().GetMedian(valueList);
		statusStdLabel.setText("Standard Deviation:\t\t" + standardDeviation);
		statusMedianLabel.setText("Median:\t\t\t\t" + median);
	}
	
	@FXML
	public void statusIssueReportBtnWasPressed(ActionEvent event) {
		if (!statisticsReportController.isValidDates(statusFrom, statusTo)) {
			statusIssueReportLabel.setText("Invalid Dates!");
			return;
		}
		if (statusComboBox.getValue() == null) {
			statusIssueReportLabel.setText("Choose Status!");
			return;
		}
		initGetReportStatusData(statusComboBox.getValue(), statusFrom.getValue(), statusTo.getValue());
		statusIssueReportLabel.setText("Report Issued!");
	}
	
	// Request Report by Rejection ************************************
	private void initGetReportRejectedData(LocalDate from, LocalDate to) {
		rejectionGraphPane.getChildren().clear();
		statisticsReportController.initGetReportStatusData(from, to);
	}
	
	public void handleGetReportRejectedData(MessageObject message) {
		ArrayList<Interval> intervals = (ArrayList<Interval>)message.getArgs().get(0);
		String status = message.getArgs().get(1).toString();
		
		updateRejectionReportLabels(intervals);
		fillGraph(rejectionGraphPane,intervals, status, status + " Report", "Rejection");
	}
	
	private void updateRejectionReportLabels(ArrayList<Interval> intervals) {
		if (intervals == null) return;
		
		ArrayList<Integer> valueList = new ArrayList<>();
		for (int i = 0; i < intervals.size(); i++)
			valueList.add(intervals.get(i).getValue());
		
		float standardDeviation = statisticsReportController.getStatisticsUtility().GetStandardDeviation(valueList);
		float median = statisticsReportController.getStatisticsUtility().GetMedian(valueList);
		rejectionStdLabel.setText("Standard Deviation:\t\t" + standardDeviation);
		rejectionMedianLabel.setText("Median:\t\t\t\t" + median);
	}
	
	@FXML
	public void rejectionIssueReportBtnWasPressed(ActionEvent event) {
		if (!statisticsReportController.isValidDates(rejectionFrom, rejectionTo)) {
			rejectionIssueReportLabel.setText("Invalid Dates!");
			return;
		}
		initGetReportRejectedData(rejectionFrom.getValue(), rejectionTo.getValue());
		rejectionIssueReportLabel.setText("Report Issued!");
	}
	
	// Request Report by Active Days **********************************
	private void initGetReportActiveDaysData(LocalDate from, LocalDate to) {
		activeDaysGraphPane.getChildren().clear();
		statisticsReportController.initGetReportActiveDaysData(from, to);
	}
	
	public void handleGetReportActiveDaysData(MessageObject message) {
		ArrayList<Interval> intervals = (ArrayList<Interval>)message.getArgs().get(0);
		String status = message.getArgs().get(1).toString();
		
		updateActiveDaysReportLabels(intervals);
		fillGraph(activeDaysGraphPane, intervals, status, status + " Report", "Active Days");
	}
	
	@FXML
	public void activeDaysIssueReportBtnWasPressed(ActionEvent event) {
		if (!statisticsReportController.isValidDates(activeDaysFrom, activeDaysTo)) {
			rejectionIssueReportLabel.setText("Invalid Dates!");
			return;
		}
		initGetReportActiveDaysData(activeDaysFrom.getValue(), activeDaysTo.getValue());
		activeDaysIssueReportLabel.setText("Report Issued!");
	}
	
	private void updateActiveDaysReportLabels(ArrayList<Interval> intervals) {
		if (intervals == null) return;
		
		ArrayList<Integer> valueList = new ArrayList<>();
		for (int i = 0; i < intervals.size(); i++)
			valueList.add(intervals.get(i).getValue());
		
		float standardDeviation = statisticsReportController.getStatisticsUtility().GetStandardDeviation(valueList);
		float median = statisticsReportController.getStatisticsUtility().GetMedian(valueList);
		activeDaysStdLabel.setText("Standard Deviation:\t\t" + standardDeviation);
		activeDaysMedianLabel.setText("Median:\t\t\t\t" + median);
	}
	
	// Request Report by Extensions ***********************************
	private void initGetReportExtensionsData() {
		extensionsGraphPane.getChildren().clear();
		statisticsReportController.initGetReportExtensionsData();
	}
	
	public void handleGetReportExtensionsData(MessageObject message) {
		ArrayList<Interval> intervals = (ArrayList<Interval>)message.getArgs().get(0);
		String status = message.getArgs().get(1).toString();
		
		updateExtensionsReportLabels(intervals);
		fillGraph(extensionsGraphPane, intervals, status, status + " Report", "Extensions");
	}
	
	@FXML
	public void extensionsIssueReportBtnWasPressed(ActionEvent event) {
		initGetReportExtensionsData();
		extensionsIssueReportLabel.setText("Report Issued!");
	}
	
	private void updateExtensionsReportLabels(ArrayList<Interval> intervals) {
		if (intervals == null) return;
		
		ArrayList<Integer> valueList = new ArrayList<>();
		for (int i = 0; i < intervals.size(); i++)
			valueList.add(intervals.get(i).getValue());
		
		float standardDeviation = statisticsReportController.getStatisticsUtility().GetStandardDeviation(valueList);
		float median = statisticsReportController.getStatisticsUtility().GetMedian(valueList);
		extensionsStdLabel.setText("Standard Deviation:\t\t" + standardDeviation);
		extensionsMedianLabel.setText("Median:\t\t\t\t" + median);
	}
	
	// Request Report by Durations ***********************************
	private void initGetReportDurationsData() {
		durationsGraphPane.getChildren().clear();
		statisticsReportController.initGetReportDurationsData();
	}
	
	public void handleGetReportDurationsData(MessageObject message) {
		ArrayList<Interval> intervals = (ArrayList<Interval>)message.getArgs().get(0);
		String status = message.getArgs().get(1).toString();
		
		updateDurationsReportLabels(intervals);
		fillGraph(durationsGraphPane, intervals, status, status + " Report", "Durations");
	}
	
	@FXML
	public void durationsIssueReportBtnWasPressed(ActionEvent event) {
		initGetReportDurationsData();
		durationsIssueReportLabel.setText("Report Issued!");
	}
	
	private void updateDurationsReportLabels(ArrayList<Interval> intervals) {
		if (intervals == null) return;
		
		ArrayList<Integer> valueList = new ArrayList<>();
		for (int i = 0; i < intervals.size(); i++)
			valueList.add(intervals.get(i).getValue());
		
		float standardDeviation = statisticsReportController.getStatisticsUtility().GetStandardDeviation(valueList);
		float median = statisticsReportController.getStatisticsUtility().GetMedian(valueList);
		durationsStdLabel.setText("Standard Deviation:\t\t" + standardDeviation);
		durationsMedianLabel.setText("Median:\t\t\t\t" + median);
	}
	
	// Request Report by Delays ***********************************
	private void initGetReportDelaysData() {
		delaysGraphPane.getChildren().clear();
		statisticsReportController.initGetReportDelaysData(delaysComboBox.getValue());
	}
	
	public void handleGetReportDelaysData(MessageObject message) {
		ArrayList<Interval> intervals = (ArrayList<Interval>)message.getArgs().get(0);
		String status = message.getArgs().get(1).toString();
		
		updateDelaysReportLabels(intervals);
		fillGraph(delaysGraphPane, intervals, status, status + " Report", "Delays");
	}
	
	@FXML
	public void delaysIssueReportBtnWasPressed(ActionEvent event) {
		if (delaysComboBox.getValue() == null)
			 delaysIssueReportLabel.setText("Choose Info System!");	 
		else {
			delaysIssueReportLabel.setText("Report Issued!");
			initGetReportDelaysData();
		}
	}
	
	private void updateDelaysReportLabels(ArrayList<Interval> intervals) {
		if (intervals == null) return;
		
		ArrayList<Integer> valueList = new ArrayList<>();
		for (int i = 0; i < intervals.size(); i++)
			valueList.add(intervals.get(i).getValue());
		
		float standardDeviation = statisticsReportController.getStatisticsUtility().GetStandardDeviation(valueList);
		float median = statisticsReportController.getStatisticsUtility().GetMedian(valueList);
		delaysStdLabel.setText("Standard Deviation:\t\t" + standardDeviation);
		delaysMedianLabel.setText("Median:\t\t\t\t" + median);
	}
	
	// Graphical ******************************************************
	private void fillGraph(StackPane graphPane, ArrayList<Interval> intervals, String seriesName, String graphTitle, String xLabel) {
		CategoryAxis xAxis = new CategoryAxis();
    	xAxis.setLabel(xLabel);
    	
    	ArrayList<String> categories = new ArrayList<>();
    	for (int i = 0; i < intervals.size(); i++)
    		if (!categories.contains(intervals.get(i).getCategoryName()))
    			categories.add(intervals.get(i).getCategoryName());
    	xAxis.setCategories(FXCollections.<String>observableArrayList(categories));
    	
    	NumberAxis yAxis = new NumberAxis();
    	yAxis.setLabel("Amount");
    	
    	StackedBarChart<String, Number> barChart = new StackedBarChart<String, Number>(xAxis, yAxis);
    	barChart.setTitle(graphTitle);
        
        XYChart.Series<String, Number> series = new XYChart.Series<String, Number>();
        
        
        series.setName(seriesName);
        for (int i = 0; i < intervals.size(); i++)
        	series.getData().add(new XYChart.Data<String, Number>(intervals.get(i).getCategoryName(), intervals.get(i).getValue()));
        
        Platform.runLater(() -> {
        barChart.getData().add(series);
        graphPane.getChildren().add(barChart);
        StackPane.setAlignment(barChart, Pos.CENTER);
        });
	}

	
	// Side Panel *****************************************************
	/** This method switches the scene back to the main panel page */
	@FXML
	public void backWasPressed(ActionEvent event) {
		statisticsReportController.switchScene("Panel");
	}
	/**
	 * This event handler switches scenes back to the Login page
	 * @param event
	 */
	@FXML
	public void logOutWasPressed(ActionEvent event) {
		statisticsReportController.logOutWasPressed(event);
	}
	
	/**
	 * This event handler switches scenes to Create New Request page
	 * @param event
	 */
	@FXML
	public void newChangeRequestWasPressed(ActionEvent event) {
		statisticsReportController.newChangeRequestWasPressed(event);
	}
	
	@FXML
	public void ViewAllRequestsWasPressed(ActionEvent event) {
		statisticsReportController.ViewAllRequestsWasPressed(event);
	}
	
	@FXML
	public void managePermissionsWasPressed(ActionEvent event) {
		statisticsReportController.managePermissionsWasPressed(event);
	}
	
	@FXML
	public void viewAllSystemDataWasPressed(ActionEvent event) {
		statisticsReportController.viewAllSystemDataWasPressed(event);
	}
}
