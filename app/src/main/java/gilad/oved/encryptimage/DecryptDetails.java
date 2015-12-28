package gilad.oved.encryptimage;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

public class DecryptDetails extends AppCompatActivity {

    private static final int SELECT_PHOTO = 100;

    Button chooseImageBtn;
    Button decryptBtn;
    ImageView chosenImage;

    Bitmap selectedBitmap;
    ByteArrayOutputStream byteStream;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decrypt_details);

        byteStream = new ByteArrayOutputStream();

        chosenImage = (ImageView) findViewById(R.id.chosenImage);
        chooseImageBtn = (Button) findViewById(R.id.chooseImageBtn);
        chooseImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent imagePickerIntent = new Intent(Intent.ACTION_PICK);
                imagePickerIntent.setType("image/*");
                startActivityForResult(imagePickerIntent, SELECT_PHOTO);
            }
        });

        decryptBtn = (Button) findViewById(R.id.decryptBtn);
        decryptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                outer: for (int y = 0; y < selectedBitmap.getHeight(); y++) {
                    for (int x = 0; x < selectedBitmap.getWidth(); x++) {
                        int pixel = selectedBitmap.getPixel(x, y);
                        int alpha = (pixel >> 24) & 0xff;
                        if (alpha == 0) {
                            break outer;
                        }

                        byteStream.write(alpha);
                    }
                }

                try {
                    byte[] bytes = byteStream.toByteArray();
                    String message = new String(bytes, "UTF-8");

                    Intent i = new Intent(DecryptDetails.this, DecryptionResults.class);
                    i.putExtra("message", message);
                    startActivity(i);
                } catch (UnsupportedEncodingException e) {

                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch(requestCode) {
            case SELECT_PHOTO:
                if(resultCode == RESULT_OK){
                    try {
                        Uri selectedImageUri = imageReturnedIntent.getData();
                        InputStream imageStream = getContentResolver().openInputStream(selectedImageUri);
                        selectedBitmap = BitmapFactory.decodeStream(imageStream);

                        Bitmap reducedBitmap = decodeUri(selectedImageUri);
                        chosenImage.setImageBitmap(reducedBitmap);
                    } catch (FileNotFoundException e) {

                    }
                }
        }
    }

    //http://stackoverflow.com/questions/2507898/how-to-pick-an-image-from-gallery-sd-card-for-my-app
    private Bitmap decodeUri(Uri selectedImage) throws FileNotFoundException {

        // Decode image size
        BitmapFactory.Options o = new BitmapFactory.Options();
        o.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage), null, o);

        // The new size we want to scale to
        final int REQUIRED_SIZE = 140;

        // Find the correct scale value. It should be the power of 2.
        int width_tmp = o.outWidth, height_tmp = o.outHeight;
        int scale = 1;
        while (true) {
            if (width_tmp / 2 < REQUIRED_SIZE
                    || height_tmp / 2 < REQUIRED_SIZE) {
                break;
            }
            width_tmp /= 2;
            height_tmp /= 2;
            scale *= 2;
        }

        // Decode with inSampleSize
        BitmapFactory.Options o2 = new BitmapFactory.Options();
        o2.inSampleSize = scale;
        return BitmapFactory.decodeStream(getContentResolver().openInputStream(selectedImage), null, o2);

    }
}
