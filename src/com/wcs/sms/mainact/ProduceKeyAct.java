package com.wcs.sms.mainact;

import java.math.BigInteger;

import com.example.sms.R;
import com.wcs.sms.tools.DhKey;
import com.wcs.sms.tools.FileUtil;
import com.wcs.sms.tools.MD5Util;
import com.wcs.sms.tools.SQLiteHelper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ProduceKeyAct extends Activity implements OnClickListener{

	EditText sender,privateK;
	TextView publicK;
	Button produce;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_producek);
		sender = (EditText)findViewById(R.id.sender_prok);
		privateK = (EditText)findViewById(R.id.privatek_prok);
		publicK = (TextView)findViewById(R.id.publick_prok);
		produce = (Button)findViewById(R.id.produce_prok);
		produce.setOnClickListener(this);
		
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {  
        case R.id.produce_prok:  
        	String userName = sender.getText().toString();
        	String privateKey = privateK.getText().toString();
        	DhKey dhkey;
        	BigInteger publicKey;
        	if(privateKey!=null&&!privateKey.equals("")){
        		dhkey = new DhKey(MD5Util.encryph(privateKey));
        		publicKey = dhkey.getY();
        		publicK.setText(publicKey.toString().substring(0, 10)+"...\n"+"\n保存位置："+FileUtil.File_NAME+userName+".txt");
        		if(userName!=null&&!userName.equals("")){
        			System.out.println("@@@@@@@");
        			SQLiteHelper sqlLH = new SQLiteHelper(ProduceKeyAct.this);
        			//将私钥经MD5变化添加到本地数据库中，
        			if(sqlLH.queryPrivateKey(userName)!=null&&!sqlLH.queryPrivateKey(userName).equals("")){
        				sqlLH.update(userName, MD5Util.encryph(privateKey));
        			}else{
        				sqlLH.insert(userName, MD5Util.encryph(privateKey));
        			}
        			System.out.println("@@@@@@@");
        			//并将本机号码存放到本地文件
        			FileUtil.folderCreate();
        			FileUtil.writeFileSdcard(FileUtil.Folder_NAME+"/me.txt", userName);
//        			System.out.println(sqlLH.query(userName));
        			//将得到的自己的公钥存储到本地文件，以手机号命名
        			FileUtil.writeFileSdcard(FileUtil.File_NAME+userName+".txt", publicKey.toString());
//        			System.out.println(FileUnit.readFileSdcard(userName));
        		}else{
    				Toast.makeText(ProduceKeyAct.this, "无号码", 8000).show();
        		}
        	}else{
				Toast.makeText(ProduceKeyAct.this, "无私钥", 8000).show();
        	}
            break;  
        default:  
            break;  
        }  
	}

}
