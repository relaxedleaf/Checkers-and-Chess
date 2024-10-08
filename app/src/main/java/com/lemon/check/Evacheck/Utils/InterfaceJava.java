package com.lemon.check.Evacheck.Utils;

import android.webkit.JavascriptInterface;

import com.lemon.check.Evacheck.Activities.CallActivity;

public class InterfaceJava {
    CallActivity callActivity;

    public InterfaceJava(CallActivity callActivity) {
        this.callActivity = callActivity;
    }

    @JavascriptInterface
    public void onPeerConnected(){
        callActivity.onPeerConnected();
    }

}

