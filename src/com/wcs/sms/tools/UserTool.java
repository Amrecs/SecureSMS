package com.wcs.sms.tools;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class UserTool {

	private static final String USER_SAVE_PATH = "start.user";
	/**
     * �����˻����루����ԭʼ���뼴�ɣ������н����м��ܣ�
     */
    public static void saveUser(Context context, String userName) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(
                USER_SAVE_PATH, Activity.MODE_PRIVATE).edit();
//        if (pass != null && !pass.equals(""))
//            pass = PassTool.encodePass(pass, name);// ʹ���û�������
        prefs.putString("userName", userName);
        prefs.commit();
    }

    /**
     * ��ȡ�˻�����
     */
    public static String getUser(Context context) {
        SharedPreferences mPref = context.getSharedPreferences(USER_SAVE_PATH,
                Activity.MODE_PRIVATE);
        String name = mPref.getString("userName", "");// �ж�""
//        if (!name.equals("") && !pass.equals(""))
//            pass = PassTool.decodePass(pass, name);
        return name;
    }

}
