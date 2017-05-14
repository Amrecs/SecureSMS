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
		//��ȡSmsManager����Ϣ������
		sManager = SmsManager.getDefault();
		//��ȡ�ı���Ͱ�ť
		number = (EditText)findViewById(R.id.number);
		content = (EditText)findViewById(R.id.content);
		encrysend = (Button)findViewById(R.id.encrysend);
		send = (Button)findViewById(R.id.send);
		nokey = (TextView)findViewById(R.id.nokey);
		//Ϊ��ť����¼�
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
					//����һ��PendingIntent����
					PendingIntent pi = PendingIntent.getActivity(SendSmsAct.this, 0, new Intent(), 0);
					//���Ͷ���
					//���ž����������ƣ���������
//					 ArrayList<String> list = sManager.divideMessage(content.getText().toString());  //��Ϊһ���������������ƣ����Ҫ�������Ų��  
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
					//��ʾ���ŷ��ͳɹ�
					Toast.makeText(SendSmsAct.this, "���ŷ��ͳɹ�", 8000).show();
				}else{
					Toast.makeText(SendSmsAct.this, "����Ϣ����", 8000).show();
				}
			}else{
				Toast.makeText(SendSmsAct.this, "�޺���", 8000).show();
			}
            break;  
        case R.id.encrysend:  
        	if(!number.getText().toString().equals("")&&number.getText().toString()!=null){
				if(!content.getText().toString().equals("")&&content.getText().toString()!=null){
					//�����ݿ��ж�ȡ˽Կ,��ʹ��MD5����
					String userName = "";
					userName = FileUtil.readFileSdcard(FileUtil.Folder_NAME+"/me.txt");
					if(userName.equals("")){
						Toast.makeText(SendSmsAct.this, "δ������Կ", 8000).show();
					}else{
						
						SQLiteHelper sqlLH = new SQLiteHelper(SendSmsAct.this);
						privateKey2 = sqlLH.queryPrivateKey(userName);
						System.out.println("@@@@@@@@privateKey2"+privateKey2);
						if(privateKey2.equals("")){
							Toast.makeText(SendSmsAct.this, "δ������Կ", 8000).show();
						}else{
							//ͨ����������������˽Կ����ȡprivateKey
							AlertDialog.Builder builder = new Builder(SendSmsAct.this);
							builder.setTitle("���������");    //���öԻ������
							builder.setIcon(android.R.drawable.btn_star);   //���öԻ������ǰ��ͼ��
							final EditText edit = new EditText(SendSmsAct.this);
							edit.setTransformationMethod(PasswordTransformationMethod.getInstance());
							builder.setView(edit);
							builder.setPositiveButton("ȷ��", 
									new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog, int which) {
								privateKey = edit.getText().toString();
								System.out.println("@@@@@@@@privateKey"+privateKey);
								if(!MD5Util.encryph(privateKey).equals(privateKey2)){
									Toast.makeText(SendSmsAct.this, "�����", 8000).show();

									}else{
										//�ӱ����ļ��ж�ȡ�ռ��˵Ĺ�Կ
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
											//����һ��PendingIntent����
											PendingIntent pi = PendingIntent.getActivity(SendSmsAct.this, 0, new Intent(), 0);
											//���Ͷ���
											//���ž����������ƣ���������
//											 ArrayList<String> list = sManager.divideMessage(ciphermessage);  //��Ϊһ���������������ƣ����Ҫ�������Ų��  
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
											//��ʾ���ŷ��ͳɹ�
											Toast.makeText(SendSmsAct.this, "���ŷ��ͳɹ�", 8000).show();
										
										}else{
											Toast.makeText(SendSmsAct.this, "���ռ��˹�Կ����֤", 8000).show();
										}
									}
							}
							});
							builder.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
							}
							});
							builder.setCancelable(true);    //���ð�ť�Ƿ���԰����ؼ�ȡ��,false�򲻿���ȡ��
							AlertDialog dialog = builder.create();  //�����Ի���
							dialog.setCanceledOnTouchOutside(true); //���õ�����ʧȥ�����Ƿ�����,��������������ط��Ƿ�����
							dialog.show();
						}
					}
					}else{
					Toast.makeText(SendSmsAct.this, "����Ϣ����", 8000).show();
				}
			}else{
				Toast.makeText(SendSmsAct.this, "�޺���", 8000).show();
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
