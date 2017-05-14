package com.wcs.sms.mainact;

import com.example.sms.R;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;  
  
public class ReceiveSmsAct extends Activity implements OnClickListener{  
	
    private TextView sender,etSmsoutbox;  
    private Button sendsms;
  
    // Message ����ֵ  
    private static final int MSG_AIRPLANE = 1;  
    private static final int MSG_OUTBOXCONTENT = 2;  
  
    private SmsObserver smsContentObserver;  
  
    /** Called when the activity is first created. */  
    @Override  
    public void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        setContentView(R.layout.act_receivesms);  
        sender = (TextView) findViewById(R.id.sender_resms); 
        etSmsoutbox = (TextView) findViewById(R.id.smscontent_resms);  
        sendsms = (Button)findViewById(R.id.sendsms_resms);  
        sendsms.setOnClickListener(this);
        // ������������  
        smsContentObserver = new SmsObserver(ReceiveSmsAct.this,mHandler);  
        //ע�����ݹ۲���  
        registerContentObservers() ;  
    }  
  
    private void registerContentObservers() {  
        // �������ݹ۲��� ��ͨ�������ҷ���ֻ�ܼ�����Uri -----> content://sms  
        // ��������������Uri ����˵ content://sms/outbox  
        Uri smsUri = Uri.parse("content://sms");  
        getContentResolver().registerContentObserver(smsUri, true,smsContentObserver);  
    }  
  
    private Handler mHandler = new Handler() {  
  
        public void handleMessage(Message msg) {  
              
            System.out.println("---mHanlder----");  
            switch (msg.what) {  
            case MSG_OUTBOXCONTENT:  
                String outbox = (String) msg.obj;
                sender.setText(outbox.split("&&&&")[0]);
                etSmsoutbox.setText(outbox.split("&&&&")[1]);  
                break;  
            default:  
                break;  
            }  
        }  
    };

	@Override
	public void onClick(View v) {
		switch (v.getId()) {  
        case R.id.sendsms_resms:  
        	Intent intent = new Intent();
        	intent.setClass(ReceiveSmsAct.this,   
        			SendSmsAct.class); 
        	startActivity(intent);
        	
//        	//��ȡ�ֻ�����ķ��������Ǵ�����ĺ��붼��ȡ����
//        	TelephonyManager tm = (TelephonyManager)this.getSystemService(ReceiveSmsAct.this.TELEPHONY_SERVICE);  
//            System.out.println(tm.getLine1Number()+"2312324");
        	break;  
        default:  
            break;  
        }  
		
	}  
}  
