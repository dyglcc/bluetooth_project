package com.xiaobailong.bluetoothfaultboardcontrol;

import java.util.ArrayList;
import java.util.HashMap;

import com.xiaobailong.bluetooth.BluetoothDevicesListDialog;
import com.xiaobailong.bluetooth.BluetoothListener;
import com.xiaobailong.bluetooth.BluetoothManager;
import com.xiaobailong.bluetooth.BluetoothMessage;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

@SuppressLint("HandlerLeak")
public class FaultboardOption implements BluetoothListener
{

	private final String DefaultPSW = "1234";
	private final int DataReadWrong = 0;
	private final int BluetoothError=1;

	private Handler mHandler = null;

	private Context context = null;

	private BluetoothManager blueToothManager = null;

	private ProgressDialog waitingDialog = null;

	private CommandCreater commandCreater = null;

	private DataParser dataParser = null;

	private ArrayList<Relay> relayList = null;
	private HashMap<Integer,Relay> relayHM=new HashMap<Integer, Relay>();
	
	private int index=0;
	
	private byte curCommand;

	public FaultboardOption(Context context, Handler mHandler,
			ArrayList<Relay> relayList)
	{
		this.context = context;
		this.mHandler = mHandler;
		blueToothManager = new BluetoothManager(context, this);
		commandCreater = new CommandCreater();
		dataParser = new DataParser();
		this.relayList = relayList;
		relayHM.clear();
		for (Relay relay : relayList) {
			relayHM.put(relay.getId(), relay);
		}
		openBluetooth();
		curCommand=-1;
	}

	@Override
	public void optionCallBack(int optionId, BluetoothMessage msg)
	{
		switch (optionId)
		{
		case SearchFinished:
			if (waitingDialog != null && waitingDialog.isShowing())
			{
				waitingDialog.dismiss();
			}
			showBlueToothDevice();
			break;
		case BluetoothDevicesSelected:
			int position = msg.getArg1();
			blueToothManager.connect(position, DefaultPSW);
			break;
		case BluetoothSearch:
			blueToothManager.search();
			showWaitingDialog(context.getString(R.string.bluetooth_searching));
			break;
		case BluetoothConnected:
			if (waitingDialog != null && waitingDialog.isShowing())
			{
				waitingDialog.dismiss();
			}
			Toast.makeText(context, "Bluetooth connect Success !",
					Toast.LENGTH_SHORT).show();
			break;
		}
	}

	private void showWaitingDialog(String msg)
	{
		if (context == null)
		{
			return;
		}
		if (waitingDialog == null)
		{
			waitingDialog = new ProgressDialog(context);
		}
		waitingDialog.setMessage(msg);
		if (!waitingDialog.isShowing())
		{
			waitingDialog.show();
		}
	}

	public void showBlueToothDevice()
	{
		BluetoothDevicesListDialog bluetoothDevicesListDialog = new BluetoothDevicesListDialog(
				context, blueToothManager.getAllBluetoothName(), this);
		bluetoothDevicesListDialog.show();
	}
	
	public void openBluetooth()
	{
		if (!blueToothManager.isBluetoothOpened())
		{
			blueToothManager.openBluetooth();
		}
	}

	public void bluetoothConnect()
	{
		blueToothManager.getBondedDevices();
		showBlueToothDevice();
	}
	
	
	public void setArray(ArrayList<Relay> relays, int index )
	{
		relayList =relays;
		relayHM.clear();
		for (Relay relay : relayList) {
			relayHM.put(relay.getId(), relay);
		}
        this.index=index;
	}

	/**
	 * 点火
	 * 
	 * @param id
	 */
	protected void ignition(int optionId)
	{
		byte[] command = commandCreater.create(CommandCreater.Start,
				(byte) 0x65);
		blueToothManager.sendData(command, optionId);
	}

	/**
	 * 启动
	 * 
	 * @param id
	 */
	protected void start(byte id, int optionId)
	{
		byte[] command = commandCreater.create(CommandCreater.Start, id);
		blueToothManager.sendData(command, optionId);
	}

	/**
	 * 熄火
	 * 
	 * @param id
	 */
	protected void shutDown(byte id, int optionId)
	{
		byte[] command = commandCreater.create(CommandCreater.ShutDown, id);
		blueToothManager.sendData(command, optionId);
	}

	/**
	 * 状态读取
	 * 
	 * @param id
	 */
	protected void stateIsRead(int optionId)
	{
		byte[] command = commandCreater.create(CommandCreater.StateIsRead,
				(byte) 0);
		blueToothManager.sendData(command, optionId);
	}

	/**
	 * 全部设置
	 * 
	 * @param id
	 */
	protected void setAll(int optionId)
	{
		byte[] command = commandCreater.create(CommandCreater.SetAll, (byte) 0);
		blueToothManager.sendData(command, optionId);
	}

	/**
	 * 全部清除
	 * 
	 * @param id
	 */
	protected void clearAll(int optionId)
	{
		curCommand =CommandCreater.ClearAll;
		byte[] command = commandCreater.create(CommandCreater.ClearAll,
				(byte) 0);
		blueToothManager.sendData(command, optionId);
	}

	protected void closeBluetoothSocket()
	{
		if (blueToothManager.isBluetoothCononected() == false)
		{
			return;
		}
		blueToothManager.close();
	}

	@Override
	public void log(String errorMsg)
	{
		Message msg = bluetoothListenerHandler.obtainMessage();
		msg.arg1 = BluetoothError;
		msg.obj=errorMsg;
		bluetoothListenerHandler.sendMessage(msg);
	}

	/**
	 * 解析蓝牙板反馈数据
	 */
	@Override
	public void inputData(byte[] data, int readDataLength, int id)
	{
//		System.out.println("readDataLength::"+readDataLength);
		byte[] state = dataParser.parse(data);
		if (readDataLength < 19)
		{
			Message msg = bluetoothListenerHandler.obtainMessage();
			msg.arg1 = DataReadWrong;
			msg.arg2 = readDataLength;
			bluetoothListenerHandler.sendMessage(msg);
			return;
		}
		int size = state.length;
//		System.out.println("size:"+size);
//		System.out.println("relayHM size:"+relayHM.size());
		for (int i = 0; i < size; i++)
		{
			Relay relay = relayHM.get(i+1);
			if(relay==null){
//				System.out.println("relay==null::"+i);
				continue;
			}
//			System.out.println(i+";"+(state[i] == 0)+";"+state[i]+";"+relay.getId());
			if (state[i+index] == 0)
			{
				if(curCommand ==CommandCreater.ClearAll)
				{
					
					relay.setForceState(Relay.Green);
				}
				else
				{
					relay.setState(Relay.Green);
				}
			} else
			{
				relay.setState(Relay.Red);
			}
		}
		curCommand=-1;
		//更新继电器状态
		Message msg = this.mHandler.obtainMessage();
		msg.arg1 = id;
		this.mHandler.sendMessage(msg);
	}

	private Handler bluetoothListenerHandler = new Handler()
	{
		public void handleMessage(android.os.Message msg)
		{
			switch (msg.arg1)
			{
			case DataReadWrong:
				Toast.makeText(
						context,
						"The length of read data is wrong ! The length should is 17,but now it is "
								+ msg.arg2 + ".", Toast.LENGTH_LONG).show();
				break;
			case BluetoothError:
				Toast.makeText(context, (String)msg.obj, Toast.LENGTH_SHORT).show();
				break;
			}
		};
	};

}
