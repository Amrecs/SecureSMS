package com.wcs.sms.mainact;

import java.math.BigInteger;

import com.wcs.sms.tools.AESUtil;
import com.wcs.sms.tools.DESUtil;
import com.wcs.sms.tools.DhKey;
import com.wcs.sms.tools.FileUtil;
import com.wcs.sms.tools.MD5Util;
import com.wcs.sms.tools.SQLiteHelper;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

public class SmsObserver extends ContentObserver
{
	private static String TAG = "SMSContentObserver";  
    
    private int MSG_OUTBOXCONTENT = 2 ;  
      
    private Context mContext  ;  
    private Handler mHandler ;   //����UI�߳�  
      
    public SmsObserver(Context context,Handler handler) {  
        super(handler);  
        mContext = context ;  
        mHandler = handler ;  
    }  
    /** 
     * ����������Uri�����ı�ʱ���ͻ�ص��˷��� 
     *  
     * @param selfChange  ��ֵ���岻�� һ������¸ûص�ֵfalse 
     */  
    @Override  
    public void onChange(boolean selfChange){  
          
      //��ȡ�ռ����еĶ��� 
        Uri outSMSUri = Uri.parse("content://sms/inbox") ;  
          
        Cursor c = mContext.getContentResolver().query(outSMSUri, null, null, null,"date desc");  
        
        if(c != null){  
              
            Log.i(TAG, "the number of send is"+c.getCount()) ;  
              
            StringBuilder sb = new StringBuilder() ;  
            //��ȡ���ݿ��е�һ������
            boolean hasDone =false;
            while(c.moveToNext()){  
            	String address = c.getString(c.getColumnIndex("address"));
            	String body = c.getString(c.getColumnIndex("body"));
            	System.out.println("@@@@@@@body"+body);
            	/*
            	 * ��������Կy���Լ��յ�������Ϣʱ�Զ�����
            	 * ��Կ������Ϣ��yyy��xxx...������ʽ���ͣ�������Ϣ�ԡ�yyyy��xxx...������ʽ����
            	 */
            	String[] sbody = body.split(":");
            	
            	if(sbody.length!=1){
            		if(sbody[0].equals("yyy")){
            			//���յ��Ĺ�Կ�洢�������ļ������ֻ�������
						System.out.println("@@@@@@@@########");

            			FileUtil.folderCreate();
            			FileUtil.writeFileSdcard(FileUtil.File_NAME+address.substring(3, address.length())+".txt",sbody[1]);
//            			
            		}else if(sbody[0].equals("yyyy")){
            			//������ת��Ϊ����
            			String enbody = body.substring(5,body.length());
            			System.out.println("@@@@@@@enbody"+enbody);
            			//�����ݿ��ж�ȡ˽Կ,��ʹ��MD5����
    					String userName = "";
    					String privateKey = "";
    					userName = FileUtil.readFileSdcard(FileUtil.Folder_NAME+"/me.txt");
    					if(userName.equals("")){
    						Toast.makeText(mContext, "δ������Կ", 8000).show();
    					}else{
    						SQLiteHelper sqlLH = new SQLiteHelper(mContext);
    						privateKey = sqlLH.queryPrivateKey(userName);
    						System.out.println("@@@@@@@@privateKey"+privateKey);
    						if(privateKey.equals("")){
    							Toast.makeText(mContext, "δ������Կ", 8000).show();
    						}else{
    							//�ӱ����ļ��ж�ȡ�����˵Ĺ�Կ
								String name = address.substring(3, address.length());
								String publicKey = "";
								publicKey = FileUtil.readFileSdcard(FileUtil.File_NAME+name+".txt");
								if(!publicKey.equals("")){
									//���ݷ����˵Ĺ�Կ�Լ�����˽Կ���н���
									System.out.println("@@@@@@@@publicKey"+publicKey);
									DhKey dhkey = new DhKey(privateKey);
									BigInteger k = dhkey.getK(publicKey);
									System.out.println("@@@@@@@@K"+k);
//									AESUtil aes = new AESUtil();
//									body = aes.AESDecode(k.toString(), enbody);
									
									DESUtil des = new DESUtil();
									body = des.DESDecode(k.toString(), enbody);
									System.out.println("@########@@body"+body);
								}else{
									Toast.makeText(mContext, "�޷����˹�Կ����֤", 8000).show();
								}
    						}
    					}
//            			body = AESUtil.AESDecode("4123896805332625768351534141400200148121835715116144639598450504384835163506568113674807762191990192917762112360705189604117128363052681464702659171755472535920835485371252849959494340722516320465513976470494242765409092307185410690216116946906617977337156601936612790341013879160597492506680578160143516754643115528412345279250114454207434168475160758927750899715807793895512059288851915521339258788222218553315140306007820766442047199582195199451613209008896844725629251287631119095765443512869104647815261514359105110618915928821425625515235026708941644470232071047535773401754148055759860342382283586114636706853", "dC2UxczvRuIi1ZDv0w4Xvg==");
            		}
            	}
                sb.append(address)  
                  .append("&&&&"+body);  
                if(sb.toString() !=null)//&& body.equals("��startMyActivity��"
                {
                	
                	
                  hasDone =true;
                  break;
                }
                if (hasDone)
                {
                  break;
                }
            }  
            c.close();            
            mHandler.obtainMessage(MSG_OUTBOXCONTENT, sb.toString()).sendToTarget();          
        }  
    }  
      
}  


