package com.diplabels.labelmaster30;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

/**
 * Application for verification of genuine of a specially designed label
 *
 * @author Piotr Dymala p.dymala@gmail.com
 * @version 1.0
 * @since 2020-06-03
 */


    /*

    Use e.g. NFC TOOLS to write to NFC TAG. In the future it will be implemented in this app.

    MIME TYPE = text/plain
    language=  EN
    textdata= e.g. AA123 (the same as in 2D code)

    Payload from the tag will be trimmed by 3 first symbols. Those are tag type and language, only pure data is parsed to previous intent.



     */
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "LabelMaster";
    Button buttonScanQR;
    Button buttonScanNFC;
    Button buttonReset;
    TextView textQRStatus;
    TextView textDMStatus;
    TextView textCodesVerif;
    View viewColoredBar;
    View viewColoredBar2;
    private String qrValue = "";
    private String nfcValue = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        permission();


    }

    private void permission() {

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                            Manifest.permission.CAMERA
                    },
                    100);

        }

        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.NFC) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{
                            Manifest.permission.NFC
                    },
                    200);

        }


    }




    public void init() {

        buttonScanQR =  findViewById(R.id.buttonScanQR);
        buttonScanNFC =  findViewById(R.id.buttonScanDM);
        buttonReset =  findViewById(R.id.buttonReset);
        textQRStatus =  findViewById(R.id.textQRStatus);
        textDMStatus =  findViewById(R.id.textDMStatus);
        textCodesVerif =  findViewById(R.id.textCodesVerif);
        viewColoredBar =  findViewById(R.id.colored_bar);
        viewColoredBar2 =  findViewById(R.id.colored_bar2);
        viewColoredBar.setBackgroundColor(Color.WHITE);
        viewColoredBar2.setBackgroundColor(Color.WHITE);


    }

    public void scanQRCode(View view) {


        int LAUNCH_SECOND_ACTIVITY = 100;
        Intent i = new Intent(this, CameraActivity.class);
        i.putExtra("CodeType", "QR");
        startActivityForResult(i, LAUNCH_SECOND_ACTIVITY);


    }

    public void scanNFCCode(View view) {
        int LAUNCH_SECOND_ACTIVITY = 200;
        Intent i = new Intent(this, NFCActivity.class);
        startActivityForResult(i, LAUNCH_SECOND_ACTIVITY);





    }

    public void checkCodes() throws UnsupportedEncodingException, NoSuchAlgorithmException {

        if (qrValue.isEmpty() || nfcValue.isEmpty()) {
            textCodesVerif.setText("Scan codes");
        } else {


            if (qrValue.equals(nfcValue)) {
                textCodesVerif.setText("Codes match");
                viewColoredBar.setBackgroundColor(Color.GREEN);
                viewColoredBar2.setBackgroundColor(Color.GREEN);
            } else {
                textCodesVerif.setText("Codes do not match");
                viewColoredBar.setBackgroundColor(Color.RED);

                viewColoredBar2.setBackgroundColor(Color.RED);

            }


        }

    }

    public void reset(View view) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        textQRStatus.setText("X");
        textDMStatus.setText("X");
        qrValue = "";
        nfcValue = "";
        viewColoredBar.setBackgroundColor(Color.WHITE);
        viewColoredBar2.setBackgroundColor(Color.WHITE);

        checkCodes();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100) {
            if (resultCode == Activity.RESULT_OK) {

                qrValue = data.getStringExtra("result");
                try {
                    checkCodes();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }

                textQRStatus.setText("OK");
                Toast.makeText(this, "Scanning successful", Toast.LENGTH_SHORT).show();



            }
            if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(this, "Scanning canceled", Toast.LENGTH_SHORT).show();

            }

        } else if (requestCode == 200) {
            if (resultCode == Activity.RESULT_OK) {
                nfcValue = data.getStringExtra("result");
                try {
                    checkCodes();
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }

                textDMStatus.setText("OK");

                Toast.makeText(this, "Scanning successful", Toast.LENGTH_SHORT).show();


            }
            if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(this, "Scanning canceled", Toast.LENGTH_SHORT).show();

            }

        }

    }
}