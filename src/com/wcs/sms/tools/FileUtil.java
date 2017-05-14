package com.wcs.sms.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.http.util.EncodingUtils;

import android.os.Environment;

public class FileUtil {


public final static String Folder_NAME = "/sdcard/SMScryph"; 
public final static String File_NAME = Folder_NAME+"/pkey"; 
public static void folderCreate(){
	
	// ֱ��ʹ���ַ���������ǰ�װ�ڴ洢�����棬����Ҫʹ��sdcard2��������Ҫȷ���Ƿ��д洢��  
	File dirFirstFolder = new File(Folder_NAME);   
	if(!dirFirstFolder.exists())  
    { //������ļ��в����ڣ�����д���  
      dirFirstFolder.mkdirs();//�����ļ���  
  }    
}

public static void fileCreate(String name){
	// ֱ��ʹ���ַ���������ǰ�װ�ڴ洢�����棬����Ҫʹ��sdcard2��������Ҫȷ���Ƿ��д洢��  
	File file = new File(name);   
	if(!file.exists()){  
        try {  
            file.createNewFile() ;  
            //file is create  
        } catch (IOException e) {  
            // TODO Auto-generated catch block  
            e.printStackTrace();  
        }  
    }    
}
//д��/mnt/sdcard/Ŀ¼������ļ�
public static void writeFileSdcard(String name,String message){ 

    try{ 

     //FileOutputStream fout = openFileOutput(fileName, MODE_PRIVATE);

    FileOutputStream fout = new FileOutputStream(name);
     byte [] bytes = message.getBytes(); 
     fout.write(bytes); 
      fout.close(); 

     } 

    catch(Exception e){ 

     e.printStackTrace(); 

    } 

}



//����/mnt/sdcard/Ŀ¼������ļ�

public static String readFileSdcard(String name){

     String res=""; 

     try{ 

      FileInputStream fin = new FileInputStream(name); 

      int length = fin.available(); 

      byte [] buffer = new byte[length]; 

      fin.read(buffer);     

      res = EncodingUtils.getString(buffer, "UTF-8"); 

      fin.close();     

     } 

     catch(Exception e){ 

      e.printStackTrace(); 

     } 

     return res; 

}

}
