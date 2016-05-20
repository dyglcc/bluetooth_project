package com.xiaobailong.bluetoothfaultboardcontrol;

import java.util.HashMap;

public class KeyMap {
	
	public static final String[] KeyMap=new String[]{
		"一、左前车门操作试验",
		"",
		"左前门锁电机C5PL63-10对地短路故障",
		"左前门锁电机C5PL63-9对地短路故障",
		"左前门锁电机C5PL63-2对地短路故障",
		"左前门锁电机C5PL63-3对地短路故障",
		"左前门锁电机C5PL63-4对地短路故障",
		"左前门锁电机C5PK09-4对地短路故障",
		"左前门控制模块C5PL01-A-20对地短路故障",
		"左前窗控/电动后视镜调节开关C5PL22-3对地短路故障",
		"二、右前车门操作试验",
		"",
		"右前门锁电机C6PL64-4对地短路故障",
		"",
		"三、左后车门操作试验",
		"",
		"左后门锁电机C7PL71-4对地短路故障",
		"左后门控制模块C7PL01-A-20对地短路故障",
		"四、右后车门操作试验",
		"",
		"右后门锁电机C8PL72-4对地短路故障",
		"",
		"五、后备箱操作试验",
		"",
		"后备箱门锁电机C4PL17-3对地短路故障",
		"后备箱门锁电机C4PL17-4对地短路故障",
		"后备箱门锁电机C4PL45-A-1对地短路故障",
		"",
		"六、免钥匙操作试验",
		"",
		"左前门免钥匙门锁电机C5PK19-2对地短路故障",
		"左前门免钥匙门锁电机C5PK19-4对地短路故障",
		"七、雨刷操作试验",
		"",
		"雨刷电机C1RW01-A-3对地短路故障",
		"",
		"八、网络操作试验",
		"",
		"高速CANL对地短路故障",
		"高速CANH对地短路故障",
		"中速CANL对地短路故障",
		"中速CANH对地短路故障",
		"高速CANL对正短路故障",
		"高速CANH对正短路故障",
		"中速CANL对正短路故障",
		"中速CANH对正短路故障",
		"九、空调操作试验",
		"",
		"空调压力传感器C1H433-3对地短路故障"
	};

	public static final int[] UnUsedKey=new int[]{0,1,10,11,13,14,15,18,19,21,22,23,27,28,29,32,33,35,36,37,46,47};
	
	public static HashMap<Integer,Integer> Id2Key=new HashMap<Integer, Integer>();
	
	static{
		Id2Key.put(2,1);
		Id2Key.put(3,2);
		Id2Key.put(4,3);
		Id2Key.put(5,4);
		Id2Key.put(6,5);
		Id2Key.put(7,14);
		Id2Key.put(8,18);
		Id2Key.put(9,19);
		Id2Key.put(12,6);
		Id2Key.put(16,7);
		Id2Key.put(17,20);
		Id2Key.put(20,8);
		Id2Key.put(24,9);
		Id2Key.put(25,10);
		Id2Key.put(26,11);
		Id2Key.put(30,12);
		Id2Key.put(31,13);
		Id2Key.put(34,15);
		Id2Key.put(38,16);
		Id2Key.put(39,17);
		Id2Key.put(40,22);
		Id2Key.put(41,23);
		Id2Key.put(42,24);
		Id2Key.put(43,25);
		Id2Key.put(44,26);
		Id2Key.put(45,27);
		Id2Key.put(48,21);
	}
}
