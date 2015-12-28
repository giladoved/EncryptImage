package gilad.oved.encryptimage;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Random;

public class EncryptionResults extends AppCompatActivity {

    public final static int NUMBER_OF_IMAGES = 2;

    Bitmap bitmapCopy;

    String input;
    byte[] bytes;

    ImageView encryptedImage;
    Button anotherOneBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encryption_results);

        encryptedImage = (ImageView)findViewById(R.id.encryptedImage);
        encryptedImage.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                String root = Environment.getExternalStorageDirectory().toString();
                File dir = new File(root + "/EncryptImage");
                dir.mkdirs();
                int n = dir.list().length + 1;
                String fname = "image" + n + ".jpg";
                File file = new File(dir, fname);

                if (file.exists())
                    file.delete();
                try {
                    FileOutputStream out = new FileOutputStream(file);
                    bitmapCopy.compress(Bitmap.CompressFormat.PNG, 100, out);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        Intent mediaScanIntent = new Intent(
                                Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                        Uri contentUri = Uri.fromFile(file);
                        mediaScanIntent.setData(contentUri);
                        sendBroadcast(mediaScanIntent);
                    } else {
                        sendBroadcast(new Intent(
                                Intent.ACTION_MEDIA_MOUNTED,
                                Uri.parse("file://"
                                        + Environment.getExternalStorageDirectory())));
                    }

                    out.flush();
                    out.close();
                    Toast.makeText(getApplicationContext(), "Image saved.", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Save Failed", Toast.LENGTH_SHORT).show();
                }

                return true;
            }
        });

        anotherOneBtn = (Button) findViewById(R.id.anotherOneBtnE);
        anotherOneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(EncryptionResults.this, MenuActivity.class);
                startActivity(i);
            }
        });

        Bundle extras = getIntent().getExtras();
        if (extras != null)  input = extras.getString("input");
        bytes = input.getBytes();

        Random random = new Random();
        int imageNum = random.nextInt(NUMBER_OF_IMAGES) + 1;

        int path = getApplicationContext().getResources().getIdentifier("image" + imageNum, "drawable", getPackageName());
        Bitmap bitmap = BitmapFactory.decodeResource(getApplicationContext().getResources(), path, new BitmapFactory.Options());
        Bitmap.Config conf = Bitmap.Config.ARGB_8888;
        bitmapCopy = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), conf);

        int byteCount = 0;

        for (int y = 0; y < bitmap.getHeight(); y++) {
            for (int x = 0; x < bitmap.getWidth(); x++) {
                int pixel = bitmap.getPixel(x, y);
                int red = (pixel >> 16) & 0xff;
                int green = (pixel >> 8) & 0xff;
                int blue = pixel & 0xff;
                int alpha = 255;
                if (byteCount < bytes.length) {
                    alpha = (int)bytes[byteCount++];
                } else if (byteCount == bytes.length) {
                    alpha = 0;
                    byteCount++;
                }

                bitmapCopy.setPixel(x, y, Color.argb(alpha, red, green, blue));
            }
        }

        encryptedImage.setImageBitmap(bitmapCopy);
        Toast.makeText(getApplicationContext(), "Image successfully encrypted!", Toast.LENGTH_SHORT).show();
    }
}
