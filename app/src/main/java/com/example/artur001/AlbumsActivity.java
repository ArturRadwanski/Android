package com.example.artur001;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.util.Arrays;

public class AlbumsActivity extends AppCompatActivity {
    private ListView list;
    private ImageView addButton;
    private static File[] files;
    private static String[] directories;
    @Override
    public void onRestart()
    {
        super.onRestart();
        finish();
        startActivity(getIntent());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.albums);

        Bundle bundle = getIntent().getExtras();
        String directory = bundle.getString("directory").toString();
        Log.d("xxx", directory); // "śliwka"

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        this.list = findViewById(R.id.list);


        //final File[] files;
        File dir = this.getDefaultFolder();


        if(!directory.equals("main"))
        {
            Log.d("name", bundle.getString("directory"));
            File customDir = new File(bundle.getString("directory"));
            Log.d("name", customDir.getAbsolutePath());
            files = customDir.listFiles();
        }
        else{
            File dir1 = new File(dir, "Directory1");
            dir1.mkdir();
            File dir2 = new File(dir, "Directory2");
            dir2.mkdir();
            File dir3 = new File(dir, "Directory3");
            dir3.mkdir();
            files = dir.listFiles();
        }





        Arrays.sort(files);
        directories = new String[files.length];
        for (int i=0;i<files.length;i++) {
            directories[i] = files[i].getName();
        }
        for (File file : files)
            Log.d("findFiles", file.getName());

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                AlbumsActivity.this,
                R.layout.row,
                R.id.rowText,
                directories);

        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(AlbumsActivity.this, AlbumsActivity.class);
                if(files[i].isDirectory())
                    intent.putExtra("directory", files[i].getAbsolutePath());
                else
                    intent.putExtra("directory", bundle.getString("directory"));
                startActivity(intent);
            }
        });
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                AlertDialog.Builder alert = new AlertDialog.Builder(AlbumsActivity.this);
                alert.setMessage(R.string.deleteDialogMessage);
                alert.setTitle(R.string.deleteDialogTitle);
                alert.setPositiveButton(R.string.positive, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int w) {
                        AlbumsActivity.this.clearDirectory(files[i]);
                    }
                });
                alert.setNegativeButton(R.string.negative, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int w) {

                    }
                });
                AlertDialog dialog = alert.create();
                dialog.show();

                return true;
            }
        });
        addButton = findViewById(R.id.addFolder);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleClick(bundle.getString("directory", "main"));
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public void clearDirectory(File toClear){
        File[] files = toClear.listFiles();
        for (File file : files) {
            if(file.isDirectory())
                clearDirectory(file);
            else
                file.delete();
        }
        toClear.delete();
    }
    public void addDirectory(String name, String path)
    {
        File dir = new File(path + "/" + name);
        dir.mkdir();
    }
    public void handleClick(String path)
    {
        path = path == "main" ? this.getDefaultFolder().getAbsolutePath() : path;
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(R.string.dialogTitle);
        alert.setMessage(R.string.dialogMessage);
        EditText input = new EditText(this);
        input.setText("");
        alert.setView(input);

        String finalPath = path;
        alert.setPositiveButton(R.string.positive, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String name = String.valueOf(input.getText());
                Log.d("folder", name);
                File tempDir = new File(finalPath);
                File dir = new File(tempDir, name);
                if(dir.mkdir())
                {
                    Intent intent = new Intent(AlbumsActivity.this, AlbumsActivity.class);
                    intent.putExtra("directory", dir.getAbsolutePath());
                    startActivity(intent);
                }
            }
        });
        alert.setNegativeButton(R.string.negative, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Log.d("folder", Integer.toString(i));
            }
        });


        alert.show();
    }
    public File getDefaultFolder()
    {
        File pic = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES
        );
        File dir = new File(pic, "Radwanski");
        dir.mkdir();
        return dir;
    }
}

