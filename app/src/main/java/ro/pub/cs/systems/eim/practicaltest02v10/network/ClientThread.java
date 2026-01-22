package ro.pub.cs.systems.eim.practicaltest02v10.network;

import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import ro.pub.cs.systems.eim.practicaltest02v10.general.Constants;
import ro.pub.cs.systems.eim.practicaltest02v10.general.Utilities;

public class ClientThread extends Thread {
    private String serverHost;
    private Integer serverPort;
    private String pokemonName;
    private TextView abilitiesTextView;
    private TextView typesTextView;

    public ClientThread(TextView abilitiesTextView, TextView typesTextView) {
        this.serverPort = Constants.SERVER_PORT;
        this.serverHost = Constants.SERVER_HOST;
        this.abilitiesTextView = abilitiesTextView;
        this.typesTextView = typesTextView;
    }

    public void startClient(EditText editText) {
        this.pokemonName = editText.getText().toString();
        this.start();
        Log.d(Constants.TAG, "Client thread started");
    }

    @Override
    public void run() {
        try {
            Socket socket = new Socket(serverHost, serverPort);
            PrintWriter printWriter = Utilities.getWriter(socket);
            BufferedReader bufferedReader = Utilities.getReader(socket);
            printWriter.println(pokemonName);
            printWriter.flush();

            String ability = bufferedReader.readLine();
            String typeName = bufferedReader.readLine();
            String imageUrl = bufferedReader.readLine();

            Log.d(Constants.TAG, ability);
            Log.d(Constants.TAG, typeName);
            Log.d(Constants.TAG, imageUrl);

            typesTextView.post(new Runnable() {
                @Override
                public void run() {
                    typesTextView.setText(typeName);
                }
            });

            abilitiesTextView.post(new Runnable() {
                @Override
                public void run() {
                    abilitiesTextView.setText(ability);
                }
            });

        } catch (UnknownHostException e) {
            Log.d(Constants.TAG, e.getMessage());
        } catch (IOException e) {
            Log.d(Constants.TAG, e.getMessage());
        }
    }
}
