package com.example.ohbrother;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.widget.*;
import java.io.File;
import java.util.Arrays;

public class MainActivity extends Activity {

    LinearLayout fileList;
    TextView pathText;
    ScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setBackgroundColor(Color.WHITE);

        // Current path
        pathText = new TextView(this);
        pathText.setTextSize(17);
        pathText.setTextColor(Color.DKGRAY);
        pathText.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        pathText.setPadding(24, 20, 24, 20);

        root.addView(pathText);

        // Scrollable file area
        scrollView = new ScrollView(this);

        fileList = new LinearLayout(this);
        fileList.setOrientation(LinearLayout.VERTICAL);
        fileList.setPadding(10, 0, 10, 20);

        scrollView.addView(fileList);
        root.addView(
                scrollView,
                new LinearLayout.LayoutParams(
                        -1,
                        0,
                        1
                )
        );

        setContentView(root);

        showFolder(Environment.getExternalStorageDirectory());
    }

    private void showFolder(File folder) {

        pathText.setText(folder.getAbsolutePath());
        fileList.removeAllViews();

        File[] files = folder.listFiles();

        if (files == null) {
            Toast.makeText(
                    this,
                    "Cannot access this folder",
                    Toast.LENGTH_SHORT
            ).show();
            return;
        }

        Arrays.sort(files, (a, b) -> {
            if (a.isDirectory() && !b.isDirectory()) return -1;
            if (!a.isDirectory() && b.isDirectory()) return 1;
            return a.getName().compareToIgnoreCase(b.getName());
        });

        for (File file : files) {

            TextView item = new TextView(this);

            String icon = file.isDirectory() ? "📁  " : "📄  ";

            item.setText(icon + file.getName());
            item.setTextSize(17);
            item.setTextColor(Color.DKGRAY);
            item.setGravity(Gravity.CENTER_VERTICAL);
            item.setPadding(20, 18, 20, 18);

            item.setBackgroundColor(Color.rgb(245, 245, 245));

            LinearLayout.LayoutParams params =
                    new LinearLayout.LayoutParams(
                            -1,
                            64
                    );

            params.setMargins(0, 4, 0, 4);

            fileList.addView(item, params);

            item.setOnClickListener(v -> {
                if (file.isDirectory()) {
                    showFolder(file);
                }
            });
        }
    }

    @Override
    public void onBackPressed() {

        File current = new File(pathText.getText().toString());
        File parent = current.getParentFile();

        if (parent != null &&
                parent.exists() &&
                !current.getAbsolutePath().equals(
                        Environment.getExternalStorageDirectory()
                                .getAbsolutePath())) {

            showFolder(parent);
        } else {
            super.onBackPressed();
        }
    }
}