package com.xiaobailong.bluetoothfaultboardcontrol;

public class Relay
{

	public static final int Red=0xffff0000;
	public static final int Yellow=0xffffff00;
	public static final int Green=0xff00ff00;
	public static final int Gray=0xff7c7c7c;
	public static final int White=0xffffffff;
	
	public static final int Short=0;
	public static final int False=1;
	
	private int state=Green;
	
	private int id=0;
	private String showId="";
	
	private int index=0;
	
	private int Type=1;
	
	private Relay colleague;
	
	public Relay(){}
	
	public Relay(int id,String showId,int state)
	{
		this.id=id;
		this.state=state;
		this.showId=showId;
		colleague=null;
	}

	public int getState()
	{
		return state;
	}

	public void setState(int state)
	{
		if(this.state != Gray)
			this.state = state;
		
	}

	public void setForceState(int state)
	{
		this.state = state;
	}
	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}
	public String showId()
	{
		return showId;
	}

	public int getType() {
		return Type;
	}

	public void setType(int type) {
		Type = type;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public void setShowId(String showId)
	{
		this.showId = showId;
	}
	
	public void setColleague(Relay colleague)
	{
		this.colleague=colleague;
	}
	public Relay colleague()
	{
		return colleague;
	}
}
