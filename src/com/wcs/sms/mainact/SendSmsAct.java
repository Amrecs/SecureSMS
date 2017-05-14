package com.wcs.sms.mainact;

import java.util.ArrayList;

import com.example.sms.R;
import com.wcs.sms.tools.AESUtil;
import com.wcs.sms.tools.DESUtil;
import com.wcs.sms.tools.DhKey;
import com.wcs.sms.tools.FileUtil;
import com.wcs.sms.tools.MD5Util;
import com.wcs.sms.tools.SQLiteHelper;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.method.PasswordTransformationMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.math.BigInteger;

import org.apache.http.util.EncodingUtils;

@SuppressWarnings("deprecation")
public class SendSmsAct extends Activity implements OnClickListener{

	EditText number,content;
	Button encrysend,send;
	TextView nokey;
	SmsManager sManager;
	String privateKey = "";
	String privateKey2 = "";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.act_sendsms);
		//获取SmsManager即信息管理器
		sManager = SmsManager.getDefault();
		//获取文本框和按钮
		number = (EditText)findViewById(R.id.number);
		content = (EditText)findViewById(R.id.content);
		encrysend = (Button)findViewById(R.id.encrysend);
		send = (Button)findViewById(R.id.send);
		nokey = (TextView)findViewById(R.id.nokey);
		//为按钮添加事件
		send.setOnClickListener(this);
		encrysend.setOnClickListener(this);
		nokey.setOnClickListener(this);
		SharedPreferences.Editor prefs = this.getSharedPreferences(
				"start.user", Activity.MODE_PRIVATE).edit();
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {  
        case R.id.send:  
        	if(!number.getText().toString().equals("")&&number.getText().toString()!=null){
				if(!content.getText().toString().equals("")&&content.getText().toString()!=null){
					//创建一个PendingIntent对象
					PendingIntent pi = PendingIntent.getActivity(SendSmsAct.this, 0, new Intent(), 0);
					//发送短信
					//短信具有字数限制，分条发送
//					 ArrayList<String> list = sManager.divideMessage(content.getText().toString());  //因为一条短信有字数限制，因此要将长短信拆分  
//			            for(String text:list){  
//							sManager.sendTextMessage(number.getText().toString(), null, text, pi, null);
//			            }
//						sManager.sendTextMessage(number.getText().toString(), null, content.getText().toString(), pi, null);
						
						if (content.getText().toString().length() > 70) {  
						    ArrayList<String> msgs = sManager.divideMessage(content.getText().toString());  
						    ArrayList<PendingIntent> sentIntents =  new ArrayList<PendingIntent>();  
						    for(int i = 0;i<msgs.size();i++){  
						       sentIntents.add(pi);  
						    }  
						    sManager.sendMultipartTextMessage(number.getText().toString(), null, msgs, sentIntents, null);  
						} else {  
							sManager.sendTextMessage(number.getText().toString(), null, content.getText().toString(), pi, null);  
						} 
					//提示短信发送成功
					Toast.makeText(SendSmsAct.this, "短信发送成功", 8000).show();
				}else{
					Toast.makeText(SendSmsAct.this, "无信息内容", 8000).show();
				}
			}else{
				Toast.makeText(SendSmsAct.this, "无号码", 8000).show();
			}
            break;  
        case R.id.encrysend:  
        	if(!number.getText().toString().equals("")&&number.getText().toString()!=null){
				if(!content.getText().toString().equals("")&&content.getText().toString()!=null){
					//从数据库中读取私钥,并使用MD5解密
					String userName = "";
					userName = FileUtil.readFileSdcard(FileUtil.Folder_NAME+"/me.txt");
					if(userName.equals("")){
						Toast.makeText(SendSmsAct.this, "未创建密钥", 8000).show();
					}else{
						
						SQLiteHelper sqlLH = new SQLiteHelper(SendSmsAct.this);
						privateKey2 = sqlLH.queryPrivateKey(userName);
						System.out.println("@@@@@@@@privateKey2"+privateKey2);
						if(privateKey2.equals("")){
							Toast.makeText(SendSmsAct.this, "未创建密钥", 8000).show();
						}else{
							//通过弹出窗口来输入私钥，获取privateKey
							AlertDialog.Builder builder = new Builder(SendSmsAct.this);
							builder.setTitle("请输入口令");    //设置对话框标题
							builder.setIcon(android.R.drawable.btn_star);   //设置对话框标题前的图标
							final EditText edit = new EditText(SendSmsAct.this);
							edit.setTransformationMethod(PasswordTransformationMethod.getInstance());
							builder.setView(edit);
							builder.setPositiveButton("确认", 
									new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
								privateKey = edit.getText().toString();
								System.out.println("@@@@@@@@privateKey"+privateKey);
								if(!MD5Util.encryph(privateKey).equals(privateKey2)){
									Toast.makeText(SendSmsAct.this, "口令不符", 8000).show();

									}else{
										//从本地文件中读取收件人的公钥
										String name = number.getText().toString();
										String publicKey = "";
										publicKey = FileUtil.readFileSdcard(FileUtil.File_NAME+name+".txt");
										if(!publicKey.equals("")){
											System.out.println("@@@@@@@@publicKey"+publicKey);
											DhKey dhkey = new DhKey(MD5Util.encryph(privateKey));
											BigInteger k = dhkey.getK(publicKey);
											System.out.println("@@@@@@@@K"+k);
											String message = content.getText().toString();
											System.out.println("@@@@@@@@message"+message);
//											AESUtil aes = new AESUtil();
//											String ciphermessage = "yyyy:"+aes.AESEncode(k.toString(), message);
											
											DESUtil Des = new DESUtil();
											String ciphermessage = "yyyy:"+Des.DESEncode(k.toString(), message);
											
											
											System.out.println("@@@@@@@@AESEncode"+ciphermessage);
											//创建一个PendingIntent对象
											PendingIntent pi = PendingIntent.getActivity(SendSmsAct.this, 0, new Intent(), 0);
											//发送短信
											//短信具有字数限制，分条发送
//											 ArrayList<String> list = sManager.divideMessage(ciphermessage);  //因为一条短信有字数限制，因此要将长短信拆分  
//									            for(String text:list){  
//													sManager.sendTextMessage(number.getText().toString(), null, text, pi, null);
//					
//									            }
//												sManager.sendTextMessage(number.getText().toString(), null, ciphermessage, pi, null);

												if (ciphermessage.length() > 70) {  
												    ArrayList<String> msgs = sManager.divideMessage(ciphermessage);  
												    ArrayList<PendingIntent> sentIntents =  new ArrayList<PendingIntent>();  
												    for(int i = 0;i<msgs.size();i++){  
												       sentIntents.add(pi);  
												    }  
												    sManager.sendMultipartTextMessage(number.getText().toString(), null, msgs, sentIntents, null);  
												} else {   
													sManager.sendTextMessage(number.getText().toString(), null, ciphermessage, pi, null);  
												}  
											//提示短信发送成功
											Toast.makeText(SendSmsAct.this, "短信发送成功", 8000).show();
										
										}else{
											Toast.makeText(SendSmsAct.this, "无收件人公钥，查证", 8000).show();
										}
									}
							}
							});
							builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
							}
							});
							builder.setCancelable(true);    //设置按钮是否可以按返回键取消,false则不可以取消
							AlertDialog dialog = builder.create();  //创建对话框
							dialog.setCanceledOnTouchOutside(true); //设置弹出框失去焦点是否隐藏,即点击屏蔽其它地方是否隐藏
							dialog.show();
						}
					}
					}else{
					Toast.makeText(SendSmsAct.this, "无信息内容", 8000).show();
				}
			}else{
				Toast.makeText(SendSmsAct.this, "无号码", 8000).show();
			}
            break; 
            
        case R.id.nokey:
        	Intent intent = new Intent();
        	intent.setClass(SendSmsAct.this,ProduceKeyAct.class); 
        	startActivity(intent);
        default:  
            break;  
        }  
	}

//	public String showDialog(final Context context){
//		
//	return privateKey;
//	}
	
}
