package gilad.oved.encryptimage;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EncryptDetails extends AppCompatActivity {

    EditText inputText;
    Button generateBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encrypt_details);

        inputText = (EditText) findViewById(R.id.inputText);
        generateBtn = (Button) findViewById(R.id.generateBtn);
        generateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = inputText.getText().toString();
                if (str != null && str.trim().length() > 0) {
                    Intent i = new Intent(EncryptDetails.this, EncryptionResults.class);
                    i.putExtra("input", str.trim());
                    startActivity(i);
                } else {
                    Toast.makeText(getApplicationContext(), "Please enter something to encrypt!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
