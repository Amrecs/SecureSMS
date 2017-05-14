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
        //创建一个密匙工厂，然后用它把DESKeySpec转换成  
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");  
        SecretKey securekey = keyFactory.generateSecret(desKey);  
        //Cipher对象实际完成加密操作  
        Cipher cipher = Cipher.getInstance("DES");  
        //用密匙初始化Cipher对象  
        cipher.init(Cipher.ENCRYPT_MODE, securekey, random);  
        //现在，获取数据并加密  
        //正式执行加密操作  
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
			// DES算法要求有一个可信任的随机数源  
	        SecureRandom random = new SecureRandom();  
	        // 创建一个DESKeySpec对象  
	        DESKeySpec desKey = new DESKeySpec(encodeRules.getBytes());  
	        // 创建一个密匙工厂  
	        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");  
	        // 将DESKeySpec对象转换成SecretKey对象  
	        SecretKey securekey = keyFactory.generateSecret(desKey);  
	        // Cipher对象实际完成解密操作  
	        Cipher cipher = Cipher.getInstance("DES");  
	        // 用密匙初始化Cipher对象  
	        cipher.init(Cipher.DECRYPT_MODE, securekey, random);  
	        // 真正开始解密操作  
	        byte [] byte_content= new BASE64Decoder().decodeBuffer(content);
            /*
             * 解密
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
