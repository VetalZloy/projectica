package com.vetalzloy.projectica.web.json;

public class ClosePositionJson {
	
	private boolean estimation;
	
	private String comment;
	
	public ClosePositionJson() {}

	public boolean isEstimation() {
		return estimation;
	}

	public void setEstimation(boolean estimation) {
		this.estimation = estimation;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}
	
}
