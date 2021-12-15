package com.example.final_project_aa;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameOverActivity extends AppCompatActivity {
    DBHelper dbHelper;
    List<Float> list = new ArrayList<>();
    TextView score;
    Float v;
    Button mainMenu;
     @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

         setContentView(R.layout.activity_game_over);
         mainMenu=findViewById(R.id.mainMenu);
        dbHelper = new DBHelper(this);
        score=findViewById(R.id.score);
        Cursor result = dbHelper.getAll();
        String record ="";
         while(result.moveToNext()) {
            String name = result.getString(0);
            String pass = result.getString(1);
            String score = result.getString(2);
            String lat = result.getString(3);
            String lo = result.getString(4);
            record += "Name: " + name + ", NAME: " + pass + ", Score: " + score+"\n"+lat+lo;
            //Toast.makeText(GameOverActivity.this, record, Toast.LENGTH_LONG).show();
            v = Float.valueOf(score);
            list.add(v);
        }
         Collections.sort(list, Collections.reverseOrder());
         if (list.size()>2) {
             score.setText("SCORE " + list.get(0) + "\n" + list.get(1) + "\n" + list.get(2) + "\n");
         }else {
             score.setText("SCORE " + list.get(0));
         }
         mainMenu.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent i=new Intent(GameOverActivity.this,MainMenuActivity.class);
                 startActivity(i);
             }
         });
     }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this, MainMenuActivity.class));
        finish();
    }
}