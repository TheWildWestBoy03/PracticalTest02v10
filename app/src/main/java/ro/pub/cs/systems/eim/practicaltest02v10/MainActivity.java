package ro.pub.cs.systems.eim.practicaltest02v10;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import ro.pub.cs.systems.eim.practicaltest02v10.network.ClientThread;
import ro.pub.cs.systems.eim.practicaltest02v10.network.ServerThread;

public class MainActivity extends AppCompatActivity {

    private class ButtonClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.search_button) {
                EditText pokemonNameEdit = (EditText) findViewById(R.id.pokemon_edit_title);

                if (pokemonNameEdit.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "You need to insert a poke name", Toast.LENGTH_SHORT).show();
                    return;
                }

                TextView abilitiesTextView = (TextView) findViewById(R.id.abilities_section_result);
                TextView typesTextView = (TextView) findViewById(R.id.types_section_result);
                ClientThread newThread = new ClientThread(abilitiesTextView, typesTextView);
                newThread.startClient(pokemonNameEdit);
            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button searchButton = (Button) findViewById(R.id.search_button);
        searchButton.setOnClickListener(new ButtonClickListener());

        ServerThread serverThread = new ServerThread();
        serverThread.startServer();

    }
}