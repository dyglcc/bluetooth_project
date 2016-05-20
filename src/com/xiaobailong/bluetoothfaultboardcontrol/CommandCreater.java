package com.xiaobailong.bluetoothfaultboardcontrol;

public class CommandCreater
{

	public static final byte Start=1;
	public static final byte ShutDown=2;
	public static final byte StateIsRead=3;
	public static final byte SetAll=4;
	public static final byte ClearAll=5;
	public static final byte Test=6;
	
	private byte[] command=new byte[5];
	
	public byte[] create(byte type,byte id)
	{
		command[0]=(byte) 0xff;
		command[1]=(byte) 0xaa;
		switch(type)
		{
		case Start:
			command[2]=0x11;
			command[3]=id;
			break;
		case ShutDown:
			command[2]=0x21;
			command[3]=id;
			break;
		case StateIsRead:
			command[2]=(byte) 0x80;
			command[3]=id;
			break;
		case SetAll:
			command[2]=0x30;
			command[3]=id;
			break;
		case ClearAll:
			command[2]=0x40;
			command[3]=id;
			break;
		case Test:
			command[2]=0x01;
			command[3]=id;
			break;
		}
		command[4]=(byte) (~(command[0]+command[1]+command[2]+command[3])+1);
		return command;
	}
}
