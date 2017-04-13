package com.example.denny.qrcode;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;
import java.sql.BatchUpdateException;
import java.util.List;

public class ReaderActivity extends AppCompatActivity {
    private Button scan_btn, scan_history, back;
    private PopupWindow popupWindow;
    private LayoutInflater layoutInflater;
    private RelativeLayout relativeLayout;

    private QRClassifier classifier;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("Debug", "oncreate called");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reader);
        scan_btn = (Button) findViewById(R.id.scan_btn);
        final Activity activity = this;
        scan_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IntentIntegrator integrator = new IntentIntegrator(activity);
                integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE_TYPES);
                integrator.setPrompt("Scan");
                integrator.setCameraId(0);
                integrator.setBeepEnabled(false);
                integrator.setBarcodeImageEnabled(false);
                integrator.initiateScan();
            }
        });

        scan_history = (Button) findViewById(R.id.scan_history);
        scan_history.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ListPage.class);
                startActivity(intent);
            }
        });

        DecisionTreeNode node1 = new DecisionTreeNode("mailto:", 4, 18, 3, 0);
        DecisionTreeNode node2 = new DecisionTreeNode(null, 0, 0, 2, 0);
        DecisionTreeNode node3 = new DecisionTreeNode("(SUB:.*;)|(subject)", 4, 18, 1, 0);
        DecisionTreeNode node4 = new DecisionTreeNode(null, 0, 0, 1, 0);
        DecisionTreeNode node5 = new DecisionTreeNode(" ", 4, 18, 0, 0);
        DecisionTreeNode node6 = new DecisionTreeNode(null, 4, 0, 0, 0);
        DecisionTreeNode node7 = new DecisionTreeNode(null, 0, 18, 0, 0);

        node1.setChildren(node2, node3);
        node3.setChildren(node4, node5);
        node5.setChildren(node6, node7);
        this.classifier = new QRClassifier();
        this.classifier.setTree(node1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(this, "You cancelled the scanning", Toast.LENGTH_LONG).show();
            }
            else {
                String resultText = result.getContents();


                String tag = this.classifier.classify(result.getContents());
                if(tag.equals("URL")) {
                    MyDBHandler dbHandler = new MyDBHandler(this, null, null, 0);

                        // use api to check if url is safe
                        // add corresponding entry in the database
                        HttpURL checker = new HttpURL();
                        try {
                            URL url = new URL(result.getContents());
                            String parsed = checker.parseURL(url);
                            String test = dbHandler.isInTable(parsed) + "";
                            Log.d("url", test);

                            if(dbHandler.isInTable(parsed) == 0) {
                                String safe = new HttpURL().execute(parsed).get();
                                cacheData cache = new cacheData(parsed, safe);
                                MyDBHandler db = new MyDBHandler(this, null ,null, 0);
                                db.addCache(cache);
                                Toast.makeText(this, safe + " \n" + resultText, Toast.LENGTH_LONG).show();

                            } else if(dbHandler.isInTable(parsed) == 1) {
                                Toast.makeText(this, "URL Safety Unknown: \n" + resultText, Toast.LENGTH_LONG).show();
                            } else if(dbHandler.isInTable(parsed) == 2) {
                                Toast.makeText(this, "Safe URL: \n" + resultText, Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(this, "Unsafe URL: \n" + resultText, Toast.LENGTH_LONG).show();
                            }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                } else if(tag.equals("TEXT")) {
                    Toast.makeText(this, "Text: \n" + resultText, Toast.LENGTH_LONG).show();
                } else if(tag.equals("EMAIL")) {
                    Toast.makeText(this, "Email: \n" + resultText, Toast.LENGTH_LONG).show();
                }

            }
        }
        else {
            super.onActivityResult(requestCode, resultCode, data);
        }

    }
}
