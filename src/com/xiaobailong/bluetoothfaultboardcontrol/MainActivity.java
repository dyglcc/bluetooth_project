package com.xiaobailong.bluetoothfaultboardcontrol;

import java.util.ArrayList;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.TabHost.OnTabChangeListener;

public class MainActivity extends Activity implements OnClickListener, OnTouchListener,
OnScrollListener
{

	/**1-100继电器状态数据 */
	private ArrayList<Relay> shortList = new ArrayList<Relay>();
	
	/**1-20继电器状态数据 */
	private ArrayList<Relay> falseList = new ArrayList<Relay>();

	/**1-100继电器操作监听 */
	private Handler theFailurePointSetGVHandler = null;
	/**基本功能操作监听*/
	private Handler faultboardOptionHandler = null;

	private TheFailurePointSetAdapter theFailurePointSetAdapter = null;
	/**基本操作功能类*/
	private FaultboardOption faultboardOption = null;

	private Button ignitionButton = null;
	private Button startButton = null;
	private Button shutDownButton = null;
	private boolean hasStarted = false;
	
	private TabHost  tabHost=null;
	private TabWidget  tabWidget =null;
	
	
	private GridView theFailurePointSetGV=null;
	
	private int lastItem=0;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initHandler();
		initData();
		initView();
	}

	/**
	 * 初始化1-100继电器状态数据 
	 */
	private void initData()
	{
//		int[] color = new int[3];
//		color[0] = Relay.Green;
//		color[1] = Relay.Red;
//		color[2] = Relay.Yellow;
		Relay item;
		for (int i = 0; i < 49; i++)
		{
			Relay relay=new Relay();
			relay.setIndex(i);
			
			Integer id=KeyMap.Id2Key.get(i);
			if(id==null){
				id=-1;
			}
			relay.setId(id);
			
			String text=KeyMap.KeyMap[i];
			relay.setShowId(text);
			
			relay.setType(Relay.Short);
			
			shortList.add(relay);
		}
		
		item=shortList.get(38);
		item.setColleague(shortList.get(42));
		item=shortList.get(42);
		item.setColleague(shortList.get(38));
		
		
		item=shortList.get(39);
		item.setColleague(shortList.get(43));
		item=shortList.get(43);
		item.setColleague(shortList.get(39));
		
		item=shortList.get(40);
		item.setColleague(shortList.get(44));
		item=shortList.get(44);
		item.setColleague(shortList.get(40));
		
		item=shortList.get(41);
		item.setColleague(shortList.get(45));
		item=shortList.get(45);
		item.setColleague(shortList.get(41));
		
		
		
		for (int i = 0; i < 20; i++)
		{
			falseList.add(new Relay(i + 1+200,i + 1+"", Relay.Green));
		}

		faultboardOption = new FaultboardOption(this, faultboardOptionHandler, shortList);
	}

	private void initHandler()
	{
		theFailurePointSetGVHandler = new Handler()
		{
			@Override
			public void handleMessage(Message msg)
			{
				Relay relay = (Relay) msg.obj;
				int state = relay.getState();
				if(state != Relay.Gray)
					relay.setState(Relay.Yellow);
				
				if(state == Relay.Red)
				{
					faultboardOption.shutDown((byte) relay.getId(), msg.arg1);
				}else if(state == Relay.Green)
				{
					int id= relay.getId();
					faultboardOption.start((byte)id, msg.arg1);
					Relay item=relay.colleague();
					if(item !=null)
					{
						item.setState(Relay.Gray);
					}
					
				}else if(state==Relay.Yellow)
				{
					Toast.makeText(MainActivity.this, "Command had send !", Toast.LENGTH_SHORT).show();
				}
				theFailurePointSetAdapter.notifyDataSetChanged();
			}
		};

		faultboardOptionHandler = new Handler()
		{
			@Override
			public void handleMessage(Message msg)
			{
				theFailurePointSetAdapter.notifyDataSetChanged();
			}
		};
	}

	private void initView()
	{
//		GridView theFailurePointSetGV = (GridView) findViewById(R.id.GridView_TheFailurePointSet);
//		theFailurePointSetAdapter = new TheFailurePointSetAdapter(this, relayList, theFailurePointSetGVHandler);
//		theFailurePointSetGV.setAdapter(theFailurePointSetAdapter);
//		theFailurePointSetGV.setNumColumns(4);
		CreateList(shortList,0);
		// 点火
		ignitionButton = (Button) findViewById(R.id.Button_Ignition);
		ignitionButton.setOnClickListener(this);
		// 启动
		startButton = (Button) findViewById(R.id.Button_Start);
		startButton.setOnClickListener(this);
		startButton.setOnTouchListener(this);
		// 熄火
		shutDownButton = (Button) findViewById(R.id.Button_ShutDown);
		shutDownButton.setOnClickListener(this);
		// 故障点说明
		findViewById(R.id.Button_PointOfFailureThat).setOnClickListener(this);
		// 状态读取
		findViewById(R.id.Button_StateIsRead).setOnClickListener(this);
		// 全部设置
		findViewById(R.id.Button_SetAll).setOnClickListener(this);
		// 全部清除
		findViewById(R.id.Button_ClearAll).setOnClickListener(this);
	}

	@Override
	public void onClick(View v)
	{
		switch (v.getId())
		{
		case R.id.Button_Ignition:// 点火
			if(hasStarted)
			{
				Toast.makeText(this, "The machine had be started !", Toast.LENGTH_SHORT).show();
			}else
			{
				Toast.makeText(this, "The machine will be started !", Toast.LENGTH_SHORT).show();
				hasStarted = true;
				faultboardOption.ignition(R.id.Button_Ignition);
			}
			break;
		case R.id.Button_ShutDown:// 熄火
			if(hasStarted)
			{
				hasStarted = false;
				Toast.makeText(this, "The machine will be ShutDown !", Toast.LENGTH_SHORT).show();
				faultboardOption.shutDown((byte) 0x65, R.id.Button_ShutDown);
			}else
			{
				Toast.makeText(this, "The machine had not be started !", Toast.LENGTH_SHORT).show();
			}
			break;
		case R.id.Button_PointOfFailureThat:// 故障点说明
			PointOfFailureThatDialog pointOfFailureThatDialog = new PointOfFailureThatDialog(this);
			pointOfFailureThatDialog.show();
			break;
		case R.id.Button_StateIsRead:// 状态读取
			faultboardOption.stateIsRead(R.id.Button_StateIsRead);
			break;
		case R.id.Button_SetAll:// 全部设置
			faultboardOption.setAll(R.id.Button_SetAll);
			break;
		case R.id.Button_ClearAll:// 全部清除
			faultboardOption.clearAll(R.id.Button_ClearAll);
			break;
		}
	}

	public boolean onCreateOptionsMenu(Menu menu)
	 {
	    getMenuInflater().inflate(R.menu.main, menu);
	    return true;
	 }
	
	public boolean onOptionsItemSelected(MenuItem item)
	  {
	    switch (item.getItemId())
	    {
	    case R.id.action_settings:
	    	 this.faultboardOption.bluetoothConnect();
	    	 break;
	    case R.id.action_exit:
	    	 finish();
	    	 break;
	    	
	    default:
	    	break;
	   
	   
	    }
	      return true;
//	     

	  }
	 protected void onResume()
	 {
		 super.onResume();
		
				
	 }
	@Override
	public void finish()
	{
		faultboardOption.closeBluetoothSocket();
		super.finish();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		if(event.getKeyCode() == KeyEvent.KEYCODE_BACK)
		{
			Toast.makeText(this, getString(R.string.Button_Back_BeCanceled), Toast.LENGTH_SHORT).show();
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	

	@Override
	public boolean onTouch(View v, MotionEvent event)
	{
		switch (v.getId())
		{
		case R.id.Button_Start:// 启动
			if(hasStarted)
			{
				if(event.getAction() == MotionEvent.ACTION_UP)
				{
					Toast.makeText(this, "StartDown", Toast.LENGTH_SHORT).show();
					faultboardOption.shutDown((byte) 0x66, R.id.Button_Start);
				}else if(event.getAction() == MotionEvent.ACTION_DOWN)
				{
					Toast.makeText(this, "StartUp", Toast.LENGTH_SHORT).show();
					faultboardOption.start((byte) 0x66, R.id.Button_Start);
				}
			}else
			{
				Toast.makeText(this, "The machine had not be started !", Toast.LENGTH_SHORT).show();
			}
			break;
		}
		return true;
	}
	
	 //  @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
//    	int dd = simpleadapter.getCount();
//    	if (lastItem==simpleadapter.getCount() && scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
////            if ((map.size() - current_page * 10) > 0) {
//                RequestList();
//            }
//        }
    }
  //  @Override
    public void onScroll(AbsListView view, int firstVisibleItem,int visibleItemCount, int totalItemCount) 
    {
           lastItem=firstVisibleItem+visibleItemCount;
    }
    
	private void CreateList(ArrayList<Relay> achlist, int aResult) {
		{
		//	CloseWaittingDialog();
			//channellist = achlist;
			//IntiPayData();
			
			//if (this.tabHost == null)
			{
				tabHost = (TabHost) findViewById(R.id.tabhost);
				tabWidget = (TabWidget) findViewById(android.R.id.tabs);
				tabHost.setup();
				tabHost.bringToFront();
	
				int chcount = achlist.size();
				ArrayList<TabHost.TabSpec> hostlist = new ArrayList<TabHost.TabSpec>(
						2);
				//for (int i = 0; i < chcount; i++)
				{
					TabHost.TabSpec tabspec1 = tabHost.newTabSpec("1");
					tabspec1.setContent(R.id.GridView_TheFailurePointSet);
	//				tabspec1.setIndicator(achlist.get(i).name);
					TextView indicatorV = new TextView(this);
					indicatorV.setGravity(Gravity.CENTER);
					indicatorV.setBackgroundResource(R.drawable.channelsbg);
//					indicatorV.setBackgroundColor(Color.BLUE);
					indicatorV.setTextSize(16);
	//				indicatorV.setPadding(10, 0, 40, 40);
	//				indicatorV.setBackgroundDrawable(getResources()
	//						.getDrawable(R.drawable.channelimg));
					indicatorV.setText(R.string.shortfault);
					tabspec1.setIndicator(indicatorV);
	//				tabspec1.setIndicator(achlist.get(i).name, getResources()
	//						.getDrawable(R.drawable.channelimg));
					// tabHost.addTab(tabspec1);
					hostlist.add(tabspec1);
					
					
					tabspec1 = tabHost.newTabSpec("2");
					tabspec1.setContent(R.id.GridView_TheFailurePointSet);
	//				tabspec1.setIndicator(achlist.get(i).name);
					indicatorV = new TextView(this);
					indicatorV.setGravity(Gravity.CENTER);
					indicatorV.setBackgroundResource(R.drawable.channelsbg);
//					indicatorV.setBackgroundColor(Color.BLUE);
					indicatorV.setTextSize(16);
	//				indicatorV.setPadding(10, 0, 40, 40);
	//				indicatorV.setBackgroundDrawable(getResources()
	//						.getDrawable(R.drawable.channelimg));
					indicatorV.setText(R.string.falsefault);
					tabspec1.setIndicator(indicatorV);
	//				tabspec1.setIndicator(achlist.get(i).name, getResources()
	//						.getDrawable(R.drawable.channelimg));
					// tabHost.addTab(tabspec1);
					hostlist.add(tabspec1);
				}
	
				int j = hostlist.size();
				for (int i = 0; i < j; i++) {
					tabHost.addTab(hostlist.get(i));
				}
			//	tabHost.setOnTabChangedListener(this);
			}
			
			
			tabHost.setCurrentTab(0);
			View view = tabWidget.getChildAt(0);
			view.setBackgroundDrawable(getResources().getDrawable(R.drawable.presschannelbg));
	
		//	currentchannel = achlist.get(0);
		//	LoaderAdapter adapter = new LoaderAdapter(this, currentchannel);
			
			theFailurePointSetAdapter = new TheFailurePointSetAdapter(this, shortList, theFailurePointSetGVHandler);
			//theFailurePointSetAdapter.SetOnDownListener(this);
			
		//	simpleadapter = adapter;
	
			
			theFailurePointSetGV = (GridView) findViewById(R.id.GridView_TheFailurePointSet);
			theFailurePointSetGV.setAdapter(theFailurePointSetAdapter);
			theFailurePointSetGV.setOnScrollListener(this);
			theFailurePointSetGV.setVisibility(View.VISIBLE);
			theFailurePointSetGV.setBackgroundColor(Relay.White);
			theFailurePointSetGV.setNumColumns(2);
	
			// listView.setOnTouchListener(new OnTouchListener() {
			// public boolean onTouch(View v, MotionEvent event){
			// // event.getHistoricalSize(pos)
			// return true;
			// }
			// });
//	
//			listView.setOnItemClickListener((new OnItemClickListener() {
//				public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
//						long arg3) {
//					// 启动包月流程
//					if(currentchannel != null)
//					{
//						if(arg2 >= 0 && arg2 < currentchannel.iVideoList.size()){
//							operaType = 1;
//							orderTypeId = 0;
//							StartWaittingDialog("正在等待播放。。。");
//							StartOrderPay(definecomm.SEE_TYPE_LIVE,currentchannel.iVideoList.get(arg2).srcurl);
//							ReportData(currentchannel.chid, currentchannel.iVideoList.get(arg2).id, "1");
//							AddMysee(currentchannel.iVideoList.get(arg2).title, currentchannel.iVideoList.get(arg2).srcurl);
//						}
//					}
//				}
//			}));
//	
			tabHost.setOnTabChangedListener(new OnTabChangeListener() 
			{
				//@Override
				public void onTabChanged(String tabId) {
					
					 changeListData(tabId);
					 for (int i = 0; i < tabWidget.getChildCount(); i++) 
					 {
						 View view = tabWidget.getChildAt(i);
						 if (tabHost.getCurrentTab() == i) 
						 {
							 view.setBackgroundDrawable(getResources() .getDrawable(R.drawable.presschannelbg));
							// Toast.makeText(MainActivity.this, "Command had send !", Toast.LENGTH_SHORT).show();
						 } 
						 else
						 {
							 view.setBackgroundDrawable(getResources() .getDrawable(R.drawable.channelsbg));
//							 vvv.setBackgroundColor(Color.BLUE);
							// Toast.makeText(MainActivity.this, "Command had send1 !", Toast.LENGTH_SHORT).show();
						 }
					 }
				}
			});
			
		
		}
		
	}

	private void changeListData(String tabId)
	{
//		for(int i = 0; i < channellist.size(); i++)
//		{
//			if(tabId.equals(channellist.get(i).chid))
//			{
//				currentchindex = i;
//				currentchannel = channellist.get(i);
//				LoaderAdapter adapter = new LoaderAdapter(this, channellist.get(i));
//				
//				simpleadapter = adapter;
//				adapter.SetOnDownListener(this);
//				
//				listView.setAdapter(adapter);
//			}
//		}
		 if(tabId.equals("1"))
		 {
			//	LoaderAdapter adapter = new LoaderAdapter(this, shortList);
				
				theFailurePointSetAdapter = new TheFailurePointSetAdapter(this, shortList, theFailurePointSetGVHandler);
				
			//	simpleadapter = adapter;
				//theFailurePointSetAdapter.SetOnDownListener(this);
				
				theFailurePointSetGV.setAdapter(theFailurePointSetAdapter);
				theFailurePointSetGV.setNumColumns(2);
				faultboardOption.setArray(shortList,0);
		 }
		 else
		 {
			    //LoaderAdapter  adapter = new LoaderAdapter(this, falseList);
			    
			    theFailurePointSetAdapter = new TheFailurePointSetAdapter(this, falseList, theFailurePointSetGVHandler);
				
				//simpleadapter = adapter;
			    //theFailurePointSetAdapter.SetOnDownListener(this);
				
			    theFailurePointSetGV.setAdapter(theFailurePointSetAdapter);
			    theFailurePointSetGV.setNumColumns(6);
			    faultboardOption.setArray(falseList,100);
		 }
	}
}
