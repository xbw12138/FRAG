package com.example.mysql;

import java.util.ArrayList;
import java.util.List;

import com.example.frag_model.ListWorkModel;

public class Singleton {

	private Singleton() {
	}
	//�������� �̰߳�ȫ
	private static final Singleton single = new Singleton();
	
	private List<ListWorkModel>listWork=new ArrayList<ListWorkModel>(); 
	
	private String Start_time=null;
	private String End_time=null;
	private String Work_position=null;
	private String Work_describe=null;
	private String Is_finish=null;
	private String Send_time=null;
	
	//user
	private String Name=null;
	private String Age=null;
	private String Sex=null;
	private String Add=null;
	private String Sign=null;
	private String Time=null;
	private String Head=null;
	private String BG=null;
	private String VIP=null;
	private String MSG=null;
	// ��̬��������
	public static Singleton getInstance() {
		return single;
	}
	public void setStart_time(String ID){
		this.Start_time=ID;
	}
	public String getStart_time(){
		return Start_time;
	}
	public void setEnd_time(String ID){
		this.End_time=ID;
	}
	public String getEnd_time(){
		return End_time;
	}
	public void setWork_position(String ID){
		this.Work_position=ID;
	}
	public String getWork_position(){
		return Work_position;
	}
	public void setWork_describe(String ID){
		this.Work_describe=ID;
	}
	public String getWork_describe(){
		return Work_describe;
	}
	public void setIs_finish(String ID){
		this.Is_finish=ID;
	}
	public String getIs_finish(){
		return Is_finish;
	}
	public void setSend_time(String ID){
		this.Send_time=ID;
	}
	public String getSend_time(){
		return Send_time;
	}
	public void setName(String ID){
		this.Name=ID;
	}
	public String getName(){
		return Name;
	}
	public void setAge(String ID){
		this.Age=ID;
	}
	public String getAge(){
		return Age;
	}
	public void setSex(String ID){
		this.Sex=ID;
	}
	public String getSex(){
		return Sex;
	}
	public void setAdd(String ID){
		this.Add=ID;
	}
	public String getAdd(){
		return Add;
	}
	public void setSign(String ID){
		this.Sign=ID;
	}
	public String getSign(){
		return Sign;
	}
	public void setTime(String ID){
		this.Time=ID;
	}
	public String getTime(){
		return Time;
	}
	public void setHead(String ID){
		this.Head=ID;
	}
	public String getHead(){
		return Head;
	}
	public void setBG(String ID){
		this.BG=ID;
	}
	public String getBG(){
		return BG;
	}
	public void setMSG(String ID){
		this.MSG=ID;
	}
	public String getMSG(){
		return MSG;
	}
	public void setVIP(String ID){
		this.VIP=ID;
	}
	public String getVIP(){
		return VIP;
	}
	
	public void setlistWork(List<ListWorkModel> ID){
		this.listWork=ID;
	}
	public List<ListWorkModel> getlistWork(){
		return listWork;
	}

}
