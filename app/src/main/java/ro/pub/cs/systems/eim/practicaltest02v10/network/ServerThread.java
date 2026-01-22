package ro.pub.cs.systems.eim.practicaltest02v10.network;

import android.util.Log;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import ro.pub.cs.systems.eim.practicaltest02v10.general.Constants;

public class ServerThread extends Thread {
    private ServerSocket serverSocket;
    private boolean isRunning = true;
    private String host;
    private Integer port;

    public ServerThread() {

    }

    public void startServer() {
        this.isRunning = true;
        this.host = Constants.SERVER_HOST;
        this.port = Constants.SERVER_PORT;
        this.start();
        Log.d(Constants.TAG, "startServer: ");
    }

    @Override
    public void run() {
        try {
            this.serverSocket = new ServerSocket(Constants.SERVER_PORT,
                    50, InetAddress.getByName(this.host));

            while (isRunning) {
                Socket socket = this.serverSocket.accept();
                CommunicationThread communicationThread = new CommunicationThread(socket);
                communicationThread.StartServer();
            }
        } catch (IOException e) {
            Log.d(Constants.TAG, e.getMessage());
        }
    }
}
