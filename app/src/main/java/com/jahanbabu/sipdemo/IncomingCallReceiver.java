/*
 * Copyright (C) 2010 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jahanbabu.sipdemo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.sip.*;
import android.util.Log;

/**
 * Listens for incoming SIP calls, intercepts and hands them off to WalkieTalkieActivity.
 */
public class IncomingCallReceiver extends BroadcastReceiver {
    /**
     * Processes the incoming call, answers it, and hands it over to the
     * WalkieTalkieActivity.
     * @param context The context under which the receiver is running.
     * @param intent The intent being received.
     */

    String TAG = IncomingCallReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e(TAG, "onReceive");
        SipAudioCall incomingCall = null;
        try {
            SipAudioCall.Listener listener = new SipAudioCall.Listener() {
                @Override
                public void onCallEstablished(SipAudioCall call) {
                    Log.e(TAG, "onCallEstablished");
//                    super.onCallEstablished(call);
//                    try {
//                        call.answerCall(30);
//                    } catch (SipException e) {
//                        e.printStackTrace();
//                    }

//                    call.toggleMute();
                    try {
                        call.continueCall(30);
                    } catch (SipException e) {
                        e.printStackTrace();
                    }
                    call.startAudio();
                    call.setSpeakerMode(true);
//                    if(call.isMuted()) {
//                        call.toggleMute();
//                    }
                }

                @Override
                public void onRinging(SipAudioCall call, SipProfile caller) {
                    Log.e(TAG, "onRinging");
                    try {
                        Log.e(TAG, "AuthUserName " + caller.getAuthUserName());
                        Log.e(TAG, "DisplayName " + caller.getDisplayName());
                        Log.e(TAG, "ProfileName " + caller.getProfileName());
                        Log.e(TAG, "Password " + caller.getPassword());
                        Log.e(TAG, "Protocol " + caller.getProtocol());
                        Log.e(TAG, "ProxyAddress " + caller.getProxyAddress());
                        Log.e(TAG, "SipDomain " + caller.getSipDomain());
                        Log.e(TAG, "Port " + caller.getPort());
                        Log.e(TAG, "UriString " + caller.getUriString());

                        call.answerCall(30);
//                        if(call.isMuted()) {
//                            call.toggleMute();
//                        }
//                        call.startAudio();
//                        call.setSpeakerMode(true);
                        if(call.isMuted()) {
                            call.toggleMute();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onCallEnded(SipAudioCall call) {
                    super.onCallEnded(call);
                    Log.e(TAG, "onCallEnded");
                }

                @Override
                public void onReadyToCall(SipAudioCall call) {
                    super.onReadyToCall(call);
                    Log.e(TAG, "onReadyToCall");
                }
            };

            WalkieTalkieActivity wtActivity = (WalkieTalkieActivity) context;

//            incomingCall = wtActivity.manager.takeAudioCall(intent, listener);
            incomingCall = wtActivity.manager.takeAudioCall(intent, null);
            incomingCall.setListener(listener, true);
//            incomingCall.answerCall(30);
//            incomingCall.startAudio();
//            incomingCall.setSpeakerMode(true);
//            if(incomingCall.isMuted()) {
//                incomingCall.toggleMute();
//            }

            wtActivity.call = incomingCall;
            wtActivity.updateStatus(incomingCall);

        } catch (Exception e) {
            if (incomingCall != null) {
                incomingCall.close();
            }
        }
    }

}
