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
	
	// 直接使用字符串，如果是安装在存储卡上面，则需要使用sdcard2，但是需要确认是否有存储卡  
	File dirFirstFolder = new File(Folder_NAME);   
	if(!dirFirstFolder.exists())  
    { //如果该文件夹不存在，则进行创建  
      dirFirstFolder.mkdirs();//创建文件夹  
  }    
}

public static void fileCreate(String name){
	// 直接使用字符串，如果是安装在存储卡上面，则需要使用sdcard2，但是需要确认是否有存储卡  
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
//写在/mnt/sdcard/目录下面的文件
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



//读在/mnt/sdcard/目录下面的文件

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
