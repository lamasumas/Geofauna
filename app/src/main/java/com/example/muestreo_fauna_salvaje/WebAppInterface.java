package com.example.muestreo_fauna_salvaje;

import android.content.Context;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

public class WebAppInterface {
    Context mContext;
    public WebAppInterface(Context mContext){
        this.mContext  = mContext;
    }

    @JavascriptInterface
    public void testToast(String toast){
        Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
    }
}
