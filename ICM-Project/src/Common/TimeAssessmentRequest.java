package Common;

import java.io.IOException;
import java.util.ArrayList;

import com.jfoenix.controls.JFXAlert;

import Gui.ManageApprovesFX;
import Utilities.MessageObject;
import Utilities.RequestType;
import Utilities.ScreenManager;
import client.Client;
import javafx.beans.property.SimpleStringProperty;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;

public class TimeAssessmentRequest {

	private SimpleStringProperty requestId,userID,jobDescription,startTime,endTime;

	public TimeAssessmentRequest(String requestId, String userID,
			String jobDescription, String startTime, String endTime) {
		this.requestId = new SimpleStringProperty(requestId);
		this.userID = new SimpleStringProperty(userID);
		this.jobDescription = new SimpleStringProperty(jobDescription);
		this.startTime = new SimpleStringProperty(startTime);
		this.endTime = new SimpleStringProperty(endTime);
	}
	
	public TimeAssessmentRequest(ArrayList<String> row) {
		this.requestId = new SimpleStringProperty(row.get(0));
		this.userID = new SimpleStringProperty(row.get(1));
		this.jobDescription = new SimpleStringProperty(row.get(2));
		this.startTime = new SimpleStringProperty(row.get(3));
		this.endTime = new SimpleStringProperty(row.get(4));
	}
	
	public String getRequestID() {
		return requestId.get();
	}

	public void setRequestID(SimpleStringProperty requestId) {
		this.requestId = requestId;
	}

	public String getEvaluatorID() {
		return userID.get();
	}

	public void setEvaluatorID(SimpleStringProperty evaluatorID) {
		this.userID = evaluatorID;
	}

	public String getEvaluatorName() {
		return jobDescription.get();
	}

	public void setEvaluatorName(SimpleStringProperty evaluatorName) {
		this.jobDescription = evaluatorName;
	}

	public String getStartDate() {
		return startTime.get();
	}

	public void setStartDate(SimpleStringProperty startTime) {
		this.startTime = startTime;
	}

	public String getEndDate() {
		return endTime.get();
	}

	public void setEndDate(SimpleStringProperty endTime) {
		this.endTime = endTime;
	}
	
	public Button getAccept() {
		Button accept = new Button("Accept");
		accept.setPrefWidth(128);
		accept.setOnAction((ActionEvent event) -> {
			MessageObject response = new MessageObject(RequestType.AcceptTimeAssessment, new ArrayList());
			response.getArgs().add(requestId.get());
			response.getArgs().add(endTime.get());
			try {
				Client.getInstance().sendToServer(response);
			} catch (IOException e) {e.printStackTrace();}
			((ManageApprovesFX) ScreenManager.getInstance().getCurrentFX()).refreshTables();
		});
		return accept;
	}
	
	public Button getReject() {
		Button reject = new Button("Reject");
		reject.setPrefWidth(128);
		reject.setOnAction((ActionEvent event) -> {
			JFXAlert<Void> rejectionDetailsPopup = new JFXAlert<Void>();
			AnchorPane anchorPane = new AnchorPane();
			TextArea node = new TextArea();
			
			// Set TextArea node
			node.setPromptText("Please Enter Rejection Details");
			node.setPrefHeight(200);
			node.setWrapText(true);
			node.setEditable(true);
			node.setLayoutX(7);
			node.setLayoutY(7);
			
			// Set Send Button
			Button send = new Button("Send");
			send.setOnAction((ActionEvent sendEvent) -> {
				if (node.getText().equals("")) {
					send.requestFocus();
					return;
				}
				MessageObject response = new MessageObject(RequestType.RejectTimeAssessment, new ArrayList());
				response.getArgs().add(requestId.get());
				response.getArgs().add(node.getText());
				response.getArgs().add(userID.get());
				try {
					Client.getInstance().sendToServer(response);
				} catch (IOException e) {e.printStackTrace();}
				rejectionDetailsPopup.close();
			});
			send.setLayoutX(220);
			send.setLayoutY(220);
			
			anchorPane.getChildren().addAll(node, send);
			
			// Set JFXAlert Dialogue
			rejectionDetailsPopup.setTitle("Time Assessment Rejection Details: ");
			rejectionDetailsPopup.setContent(anchorPane);
			rejectionDetailsPopup.setSize(492, 250);
			rejectionDetailsPopup.showAndWait();
			((ManageApprovesFX) ScreenManager.getInstance().getCurrentFX()).refreshTables();
		});
		return reject;
	}
	
	@Override
	public boolean equals(Object request_obj) {
		if (!(request_obj instanceof TimeAssessmentRequest)) return false;
		TimeAssessmentRequest request = (TimeAssessmentRequest) request_obj;
		return requestId.get().equals(request.requestId.get());
	}
	
}