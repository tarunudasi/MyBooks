package debugbridge.mybooks.MyReceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.Status;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import debugbridge.mybooks.listener.OnSmsReceived;

public class SMSBroadcastReceiver extends BroadcastReceiver {

    private OnSmsReceived onSmsReceived;
    private boolean timeOut = false;

    @Override
    public void onReceive(Context context, Intent intent) {
        if (SmsRetriever.SMS_RETRIEVED_ACTION.equals(intent.getAction())) {
            Bundle extras = intent.getExtras();
            Status status = (Status) extras.get(SmsRetriever.EXTRA_STATUS);

            switch (status.getStatusCode()) {
                case CommonStatusCodes.SUCCESS:
                    // Get SMS message contents
                    String message = (String) extras.get(SmsRetriever.EXTRA_SMS_MESSAGE);
                    Pattern pattern = Pattern.compile("(\\d{6})");
                    Matcher matcher = pattern.matcher(message);
                    String val = "";
                    if (matcher.find()) {
                        if (onSmsReceived != null && !timeOut){
                            onSmsReceived.onOtpReceived(matcher.group(1));
                        }
                    }

                    break;
                case CommonStatusCodes.TIMEOUT:
                    this.timeOut = true;
                    break;
            }
        }
    }

    public void setOnSmsReceivedListener(OnSmsReceived onSmsReceived){
        this.onSmsReceived = onSmsReceived;
    }


}
