package com.thkskn.enyakinotel;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by tahaKeskin on 8/15/15.
 */
public class Message {
    public static void message(Context context,String message){
        Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
    }
}
