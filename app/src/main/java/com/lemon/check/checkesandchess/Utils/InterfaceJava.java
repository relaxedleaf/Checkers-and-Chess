package com.lemon.check.checkesandchess.Utils;

import android.webkit.JavascriptInterface;

import com.lemon.check.checkesandchess.Activities.CallActivity;

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

