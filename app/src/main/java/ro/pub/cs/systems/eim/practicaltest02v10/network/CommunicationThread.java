package ro.pub.cs.systems.eim.practicaltest02v10.network;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import ro.pub.cs.systems.eim.practicaltest02v10.general.Constants;
import ro.pub.cs.systems.eim.practicaltest02v10.general.Utilities;

public class CommunicationThread extends Thread {
    private Socket socket;
    public CommunicationThread(Socket socket) {
        this.socket = socket;
    }
    public void StartServer() {
        this.start();
        Log.d(Constants.TAG, "Communication Thread started");
    }

    @Override
    public void run() {
        try {
            BufferedReader bufferedReader = Utilities.getReader(socket);
            PrintWriter printWriter = Utilities.getWriter(socket);

            String dictionaryWord = bufferedReader.readLine();

            OkHttpClient client = new OkHttpClient();
            String url = Constants.WEB_ADDRESS + "/" + dictionaryWord;
            Request request = new Request.Builder().url(url).build();
            Response response = client.newCall(request).execute();

            if (response.isSuccessful() && response.body() != null) {
                String stringifyResponse = response.body().string();

                JSONObject jsonObject = new JSONObject(stringifyResponse);
                JSONArray abilities = jsonObject.getJSONArray("abilities");
                JSONObject ability = abilities.getJSONObject(0);
                JSONObject ability1 = ability.getJSONObject("ability");
                String abilityName = ability1.getString("name");

                JSONArray types = jsonObject.getJSONArray("types");
                JSONObject type = types.getJSONObject(0);
                JSONObject type1 = type.getJSONObject("type");
                String typeName = type1.getString("name");

                JSONObject spritesObject = jsonObject.getJSONObject("sprites");
                String imageUrl =  spritesObject.getString("front_default");

                String result = abilityName + " " + typeName + " " + imageUrl;
                printWriter.println(result);
            }
        } catch (IOException | JSONException e) {
            Log.d(Constants.TAG, e.getMessage());
        }
    }
}
