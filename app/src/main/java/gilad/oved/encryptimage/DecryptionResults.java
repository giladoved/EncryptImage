package gilad.oved.encryptimage;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class DecryptionResults extends AppCompatActivity {

    String message;

    TextView messageText;
    Button anotherOneBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decryption_results);

        messageText = (TextView)findViewById(R.id.messageText);
        anotherOneBtn = (Button)findViewById(R.id.anotherOneBtnD);
        anotherOneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DecryptionResults.this, MenuActivity.class);
                startActivity(i);
            }
        });

        Bundle extras = getIntent().getExtras();
        if (extras != null)  message = extras.getString("message");

        messageText.setText(message);
    }
}
