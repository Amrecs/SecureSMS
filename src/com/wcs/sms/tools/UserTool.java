package com.wcs.sms.tools;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class UserTool {

	private static final String USER_SAVE_PATH = "start.user";
	/**
     * 保存账户密码（传入原始密码即可，方法中将进行加密）
     */
    public static void saveUser(Context context, String userName) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(
                USER_SAVE_PATH, Activity.MODE_PRIVATE).edit();
//        if (pass != null && !pass.equals(""))
//            pass = PassTool.encodePass(pass, name);// 使用用户名加密
        prefs.putString("userName", userName);
        prefs.commit();
    }

    /**
     * 获取账户密码
     */
    public static String getUser(Context context) {
        SharedPreferences mPref = context.getSharedPreferences(USER_SAVE_PATH,
                Activity.MODE_PRIVATE);
        String name = mPref.getString("userName", "");// 判断""
//        if (!name.equals("") && !pass.equals(""))
//            pass = PassTool.decodePass(pass, name);
        return name;
    }

}
