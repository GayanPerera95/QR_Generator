package com.example.qrgenerator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {

    EditText enterinput;
    Button QRgenarate,save;
    ImageView QRoutput;


    OutputStream outputStream;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        enterinput = findViewById( R.id.txtinput );
        QRgenarate = findViewById( R.id.generate );
        QRoutput = findViewById(R.id.imgout);
        save = findViewById(R.id.save);

        final Bitmap bitmap = getIntent().getParcelableExtra( "pic" );
        QRoutput.setImageBitmap(bitmap);

        QRgenarate.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String sText = enterinput.getText().toString().trim();

                MultiFormatWriter writer = new MultiFormatWriter();

                try {
                    BitMatrix matrix = writer.encode( sText, BarcodeFormat.QR_CODE,350,350 );

                    BarcodeEncoder encoder = new BarcodeEncoder();
                    Bitmap bitmap = encoder.createBitmap( matrix );
                    QRoutput.setImageBitmap( bitmap );
                    InputMethodManager manager = (InputMethodManager)getSystemService(
                            Context.INPUT_METHOD_SERVICE
                    );
                    manager.hideSoftInputFromWindow( enterinput.getApplicationWindowToken(),0 );
                } catch (WriterException e) {
                    e.printStackTrace();
                }

            }
        } );

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String path = Environment.getStorageDirectory().toString();

                OutputStream fOutputStream = null;
                File file = new File(path + "/DCIM/", "screen.jpg");
                if (!file.exists()) {
                    file.mkdirs();
                }

                try {

                    fOutputStream = new FileOutputStream(file);

                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOutputStream);

                    fOutputStream.flush();
                    fOutputStream.close();
                    MediaStore.Images.Media.insertImage(getContentResolver(), file.getAbsolutePath(), file.getName(), file.getName());
                    Toast.makeText( getApplicationContext(),"Work",Toast.LENGTH_SHORT).show();

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    return;
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }
            }
        });

    }
}