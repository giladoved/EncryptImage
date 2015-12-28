package gilad.oved.encryptimage;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class MenuActivity extends AppCompatActivity {

    Button encryptBtn;
    Button decryptBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        encryptBtn = (Button) findViewById(R.id.encryptBtn);
        decryptBtn = (Button) findViewById(R.id.decryptBtn);

        encryptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MenuActivity.this, EncryptDetails.class);
                startActivity(i);
            }
        });

        decryptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MenuActivity.this, DecryptDetails.class);
                startActivity(i);
            }
        });
    }

    @Override
    public void onBackPressed() {
    }
}
