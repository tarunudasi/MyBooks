package debugbridge.mybooks.AppData;

import android.app.Application;

import debugbridge.mybooks.MyReceiver.ConnectivityReceiver;

public class ReceiverInitiator extends Application {

    private static ReceiverInitiator mInstance;

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;
    }

    public static synchronized ReceiverInitiator getInstance() {
        return mInstance;
    }

    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }
}
