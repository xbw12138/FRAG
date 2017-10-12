package com.example.frag_model;

public class ListWorkModel {
	// order
	private String Start_time = null;
	private String End_time = null;
	private String Work_position = null;
	private String Work_describe = null;
	private String Finish = null;
	private String WorkID = null;
	private String WorkSendTime = null;

	public void setStart_time(String ID) {
		this.Start_time = ID;
	}

	public String getStart_time() {
		return Start_time;
	}

	public void setEnd_time(String ID) {
		this.End_time = ID;
	}

	public String getEnd_time() {
		return End_time;
	}

	public void setWork_position(String ID) {
		this.Work_position = ID;
	}

	public String getWork_position() {
		return Work_position;
	}

	public void setWork_describe(String ID) {
		this.Work_describe = ID;
	}

	public String getWork_describe() {
		return Work_describe;
	}

	public void setFinish(String ID) {
		this.Finish = ID;
	}

	public String getFinish() {
		return Finish;
	}

	public void setWorkID(String ID) {
		this.WorkID = ID;
	}

	public String getWorkID() {
		return WorkID;
	}

	public void setWorkSendTime(String ID) {
		this.WorkSendTime = ID;
	}

	public String getWorkSendTime() {
		return WorkSendTime;
	}
}
