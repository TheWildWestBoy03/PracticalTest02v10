package ro.pub.cs.systems.eim.practicaltest02v10.network;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import ro.pub.cs.systems.eim.practicaltest02v10.general.Constants;
import ro.pub.cs.systems.eim.practicaltest02v10.general.Utilities;

public class ClientThread extends Thread {
    private String serverHost;
    private Integer serverPort;
    private String pokemonName;
    private TextView abilitiesTextView;
    private TextView typesTextView;
    private ImageView imageView;

    public ClientThread(TextView abilitiesTextView, TextView typesTextView, ImageView imageView) {
        this.serverPort = Constants.SERVER_PORT;
        this.serverHost = Constants.SERVER_HOST;
        this.abilitiesTextView = abilitiesTextView;
        this.typesTextView = typesTextView;
        this.imageView = imageView;
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

            String result = bufferedReader.readLine();
            String[] results = result.split(" ");

            Log.d(Constants.TAG, results[0]);
            Log.d(Constants.TAG, results[1]);
            Log.d(Constants.TAG, results[2]);

            typesTextView.post(new Runnable() {
                @Override
                public void run() {
                    typesTextView.setText(results[0]);
                }
            });

            abilitiesTextView.post(new Runnable() {
                @Override
                public void run() {
                    abilitiesTextView.setText(results[1]);
                }
            });

            OkHttpClient httpClient = new OkHttpClient();
            String cartoonUrl = results[2];
            Request imageRequest = new Request.Builder().url(cartoonUrl).build();
            Response imageResponse = httpClient.newCall(imageRequest).execute();

            if (imageResponse.isSuccessful() && imageResponse.body() != null) {
                Bitmap bitmap = BitmapFactory.decodeStream(imageResponse.body().byteStream());
                imageView.post(new Runnable() {
                    @Override
                    public void run() {
                        imageView.setImageBitmap(bitmap);
                    }
                });
            }

        } catch (UnknownHostException e) {
            Log.d(Constants.TAG, e.getMessage());
        } catch (IOException e) {
            Log.d(Constants.TAG, e.getMessage());
        }
    }
}
