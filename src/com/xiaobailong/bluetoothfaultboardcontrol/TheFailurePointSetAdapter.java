package com.xiaobailong.bluetoothfaultboardcontrol;

import java.util.ArrayList;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

public class TheFailurePointSetAdapter extends BaseAdapter implements OnClickListener
{

	private ArrayList<Relay> list=null;
	
	private Context context=null;
	
	private Handler mHandler=null;
	
	public TheFailurePointSetAdapter(Context context,ArrayList<Relay> list,Handler mHandler)
	{
		this.list=list;
		this.context=context;
		this.mHandler=mHandler;
	}
	
	@Override
	public int getCount()
	{
		return list.size();
	}

	@Override
	public Object getItem(int position)
	{
		return list.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		Button button=null;
		
		if(convertView==null)
		{
			button=new Button(context);
			convertView=button;
		}else
		{
			button=(Button) convertView;
		}
		
		Relay relay=this.list.get(position);
		
		if(relay.showId()==null||relay.showId().length()==0){
			button.setText("");
			button.setBackgroundColor(Relay.White);
			button.setClickable(false);
		}else{
			if(relay.getType()==Relay.False){
				button.setText(relay.showId());
				button.setOnClickListener(this);
				button.setBackgroundColor(relay.getState());
			}else if(relay.getType()==Relay.Short){
				String showText=relay.showId();
//				System.out.println(showText);
				button.setText(showText);
				button.setTextSize(25);
				boolean canClick=canClick(relay.getIndex());
				if(canClick){
					button.setBackgroundColor(relay.getState());
					button.setOnClickListener(this);
				}else{
					button.setBackgroundColor(Relay.White);
					button.setClickable(canClick);
				}
			}
		}
		button.setTag(relay);
		//button.setWidth(40/2*3);
		//button.setHeight(40/2*3);
		
		return convertView;
	}
	
	private boolean canClick(int showId){
		int size=KeyMap.UnUsedKey.length;
		for (int i = 0; i < size; i++) {
			if(showId==KeyMap.UnUsedKey[i]){
//				System.out.println("showId:"+showId+";false");
				return false;
			}
		}
//		System.out.println("showId:"+showId+";true");
		return true;
	}

	@Override
	public void onClick(View v)
	{
		Message msg=this.mHandler.obtainMessage();
		msg.arg1=v.getId();
		msg.obj=v.getTag();
		this.mHandler.sendMessage(msg);
	}

}
