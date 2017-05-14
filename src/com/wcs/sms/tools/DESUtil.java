package com.wcs.sms.tools;

import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class DESUtil {

	public String DESEncode(String encodeRules,String content) {              
        try{  
        SecureRandom random = new SecureRandom();  
        DESKeySpec desKey = new DESKeySpec(encodeRules.getBytes());  
        //����һ���ܳ׹�����Ȼ��������DESKeySpecת����  
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");  
        SecretKey securekey = keyFactory.generateSecret(desKey);  
        //Cipher����ʵ����ɼ��ܲ���  
        Cipher cipher = Cipher.getInstance("DES");  
        //���ܳ׳�ʼ��Cipher����  
        cipher.init(Cipher.ENCRYPT_MODE, securekey, random);  
        //���ڣ���ȡ���ݲ�����  
        //��ʽִ�м��ܲ���  
        byte [] byte_encode=content.getBytes("utf-8");
        byte [] byte_DES=cipher.doFinal(byte_encode);
        String DES_encode=new String(new BASE64Encoder().encode(byte_DES));

        return DES_encode;  
        }catch(Throwable e){  
                e.printStackTrace();  
        }  
        return null;  
}  
	
	
	public String DESDecode(String encodeRules,String content)  {  
		try{
			// DES�㷨Ҫ����һ�������ε������Դ  
	        SecureRandom random = new SecureRandom();  
	        // ����һ��DESKeySpec����  
	        DESKeySpec desKey = new DESKeySpec(encodeRules.getBytes());  
	        // ����һ���ܳ׹���  
	        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");  
	        // ��DESKeySpec����ת����SecretKey����  
	        SecretKey securekey = keyFactory.generateSecret(desKey);  
	        // Cipher����ʵ����ɽ��ܲ���  
	        Cipher cipher = Cipher.getInstance("DES");  
	        // ���ܳ׳�ʼ��Cipher����  
	        cipher.init(Cipher.DECRYPT_MODE, securekey, random);  
	        // ������ʼ���ܲ���  
	        byte [] byte_content= new BASE64Decoder().decodeBuffer(content);
            /*
             * ����
             */
            byte [] byte_decode=cipher.doFinal(byte_content);
            String AES_decode=new String(byte_decode,"utf-8");
	        return AES_decode;  
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
        
}  
}
