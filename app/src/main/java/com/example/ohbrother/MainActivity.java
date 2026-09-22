package com.example.ohbrother;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.widget.*;
import android.view.*;
import java.io.File;
import java.util.Arrays;

public class MainActivity extends Activity {

    LinearLayout layout;
    TextView pathText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        pathText = new TextView(this);
        pathText.setTextSize(18);
        pathText.setPadding(20, 20, 20, 20);

        layout.addView(pathText);

        setContentView(layout);

        showFolder(Environment.getExternalStorageDirectory());
    }

    private void showFolder(File folder) {
        pathText.setText(folder.getAbsolutePath());
        layout.removeViews(1, layout.getChildCount() - 1);

        File[] files = folder.listFiles();

        if (files == null) {
            Toast.makeText(this,
                    "Cannot access this folder",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        Arrays.sort(files, (a, b) ->
                a.getName().compareToIgnoreCase(b.getName()));

        for (File file : files) {
            Button button = new Button(this);
            button.setText(
                    (file.isDirectory() ? "📁 " : "📄 ")
                    + file.getName()
            );

            button.setOnClickListener(v -> {
                if (file.isDirectory()) {
                    showFolder(file);
                }
            });

            layout.addView(button);
        }
    }
}