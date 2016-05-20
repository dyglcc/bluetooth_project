package com.xiaobailong.bluetoothfaultboardcontrol;


import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class LoginActivity extends Activity{
	
	Button logigBtn;
	EditText idEdit;
	EditText pwdEdit;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.loginpage);
		//initHandler();
		//initData();
		//initView();
//		this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//				WindowManager.LayoutParams.FLAG_FULLSCREEN);
//		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		initLoginView();
	}
	
	public void initLoginView()
	{

		
		logigBtn = (Button) findViewById(R.id.login_loging_btn);
	//	passwordstate = (Button)findViewById(R.id.passwordstate);//passworld show state

		idEdit = (EditText) findViewById(R.id.login_id); // 帐号输入框
		pwdEdit = (EditText) findViewById(R.id.login_password); // 密码输入框


		if (logigBtn != null)
		{
			logigBtn.setOnClickListener(new OnClickListener() 
			{
				public void onClick(View v) 
				{
					checkLoginInfo();
				}
			});
		}
	}
	
	protected void checkLoginInfo()
	{
	
			String name;
			String password;
			name = idEdit.getText().toString();
			password = pwdEdit.getText().toString();
			if (name.equals("") || password.equals("")) 
			{ // 用户名或密码为空
				//Tools.showDialog("确定", "", "提示", "用户名或密码不能为空", 1, 0);
				// showDialog("用户名或密码不能为空", 1);// 显示对话框
				Toast.makeText(this,"用户名或密码不能为空", Toast.LENGTH_SHORT).show();
			} 
			else
			{// 用户名密码不为空
				if (name.equals("福特华北培训中心") && password.equals("000000")) 
				{
					Intent intent = new Intent();
				    intent.setClass(this, MainActivity.class);
				    startActivity(intent);
				    finish();
				        
				} 
				else 
				{
					Toast.makeText(this,"用户名或密码不正确", Toast.LENGTH_SHORT).show();
				}
			}
	
	}		
	
}
