package com.example.michelle.finalattempt;

import android.app.AlertDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    public DoodleView drawView;
    private ImageButton currPaint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        drawView = (DoodleView)findViewById(R.id.view);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        GridLayout gridLayout = (GridLayout) findViewById(R.id.gridLayout);
        int count = gridLayout.getChildCount();
        ButtonClickHandler buttonClickHandler = new ButtonClickHandler();
        for(int i = 0; i < count; i++) {
            View v = gridLayout.getChildAt(i);
            if (v instanceof Button) {
                v.setOnClickListener(buttonClickHandler);
            }
        }

        SeekBar brushStroke = (SeekBar)findViewById(R.id.seekBarBRUSH);
        brushStroke.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressChanged = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressChanged = progress;
                drawView = (DoodleView)findViewById(R.id.view);
                drawView.changeBrushStroke(progressChanged);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
//                Toast.makeText(getApplicationContext(),"seek bar progress:"+progressChanged, Toast.LENGTH_SHORT).show();
            }
        });

        SeekBar brushOpacity = (SeekBar)findViewById(R.id.seekBarOPACITY);
        brushOpacity.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressChanged = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                progressChanged = progress;
                drawView = (DoodleView)findViewById(R.id.view);
                drawView.changeOpacity(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
//                Toast.makeText(getApplicationContext(),"seek bar progress:"+progressChanged, Toast.LENGTH_SHORT).show();
            }
        });


    }

    private class ButtonClickHandler implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if (v instanceof Button) {
                drawView = (DoodleView) findViewById(R.id.view);
                if (v.getTag().equals("clear")) {
                    drawView.clear();
                } else if (v.getTag().equals("undo")) {
                    drawView.undo();
                } else if (v.getTag().equals("redo")) {
                    drawView.redo();
                } else if (v.getTag().equals("save")) {
                    drawView.setDrawingCacheEnabled(true);
                    String imgSaved = MediaStore.Images.Media.insertImage(getContentResolver(), drawView.getDrawingCache(), UUID.randomUUID().toString()+".png", "drawing");

                    if(imgSaved!=null){
                        Toast savedToast = Toast.makeText(getApplicationContext(),
                                "Drawing saved to Gallery!", Toast.LENGTH_SHORT);
                        savedToast.show();
                    }
                    else{
                        Toast unsavedToast = Toast.makeText(getApplicationContext(),
                                "Oops! Image could not be saved.", Toast.LENGTH_SHORT);
                        unsavedToast.show();
                    }
                    drawView.destroyDrawingCache();
                }
                else {
                    Button buttonClicked = (Button) v;
                    String color = v.getTag().toString();
                    int c = Color.parseColor(color);
                    drawView.changeColor(c);
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

//    public void onButtonClick(View v) {
//        Toast toast = Toast.makeText(getApplicationContext(), "XML Hookup button clicked" + ((Button)v).getText(), Toast.LENGTH_SHORT);
//        toast.show();
//    }
}
