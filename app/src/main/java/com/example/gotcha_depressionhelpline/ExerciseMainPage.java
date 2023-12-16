package com.example.gotcha_depressionhelpline;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ExerciseMainPage extends AppCompatActivity {

    Button Day1, Day2, Day3, Day4, Day5, Day6, Day7, Day8, Day9, Day10, Day11, Day12, Day13, Day14, Day15;
    String get_done_counter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_main_page);

        Day1 = findViewById(R.id.button1);
        Day2 = findViewById(R.id.button2);
        Day3 = findViewById(R.id.button3);
        Day4 = findViewById(R.id.button4);
        Day5 = findViewById(R.id.button5);
        Day6 = findViewById(R.id.button6);
        Day7 = findViewById(R.id.button7);
        Day8 = findViewById(R.id.button8);
        Day9 = findViewById(R.id.button9);
        Day10 = findViewById(R.id.button10);
        Day11 = findViewById(R.id.button11);
        Day12 = findViewById(R.id.button12);
        Day13 = findViewById(R.id.button13);
        Day14 = findViewById(R.id.button14);
        Day15 = findViewById(R.id.button15);

        Day1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent nav = new Intent(ExerciseMainPage.this, exerciseActivity.class);
                nav.putExtra("Day_ID", "1");
                startActivity(nav);
            }
        });

        Day2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent nav = new Intent(ExerciseMainPage.this, exerciseActivity.class);
                nav.putExtra("Day_ID", "2");
                startActivity(nav);
            }
        });

        Day3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent nav = new Intent(ExerciseMainPage.this, exerciseActivity.class);
                nav.putExtra("Day_ID", "3");
                startActivity(nav);
            }
        });

        Day4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent nav = new Intent(ExerciseMainPage.this, exerciseActivity.class);
                nav.putExtra("Day_ID", "4");
                startActivity(nav);
            }
        });

        Day5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent nav = new Intent(ExerciseMainPage.this, exerciseActivity.class);
                nav.putExtra("Day_ID", "5");
                startActivity(nav);
            }
        });

        Day6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent nav = new Intent(ExerciseMainPage.this, exerciseActivity.class);
                nav.putExtra("Day_ID", "6");
                startActivity(nav);
            }
        });

        Day7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent nav = new Intent(ExerciseMainPage.this, exerciseActivity.class);
                nav.putExtra("Day_ID", "7");
                startActivity(nav);
            }
        });

        Day8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent nav = new Intent(ExerciseMainPage.this, exerciseActivity.class);
                nav.putExtra("Day_ID", "8");
                startActivity(nav);
            }
        });

        Day9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent nav = new Intent(ExerciseMainPage.this, exerciseActivity.class);
                nav.putExtra("Day_ID", "9");
                startActivity(nav);
            }
        });

        Day10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent nav = new Intent(ExerciseMainPage.this, exerciseActivity.class);
                nav.putExtra("Day_ID", "10");
                startActivity(nav);
            }
        });

        Day11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent nav = new Intent(ExerciseMainPage.this, exerciseActivity.class);
                nav.putExtra("Day_ID", "11");
                startActivity(nav);
            }
        });

        Day12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent nav = new Intent(ExerciseMainPage.this, exerciseActivity.class);
                nav.putExtra("Day_ID", "12");
                startActivity(nav);
            }
        });

        Day13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent nav = new Intent(ExerciseMainPage.this, exerciseActivity.class);
                nav.putExtra("Day_ID", "13");
                startActivity(nav);
            }
        });

        Day14.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent nav = new Intent(ExerciseMainPage.this, exerciseActivity.class);
                nav.putExtra("Day_ID", "14");
                startActivity(nav);
            }
        });

        Day15.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent nav = new Intent(ExerciseMainPage.this, exerciseActivity.class);
                nav.putExtra("Day_ID", "15");
                startActivity(nav);
            }
        });



            Intent intent = getIntent();
            get_done_counter = intent.getStringExtra("done_ID");
            //     Toast.makeText(this, "Got done_ID from exerciseActivity /n " + get_done_counter, Toast.LENGTH_LONG).show();

//            String dataA= get_done_counter;
//            Toast.makeText(this, "/nString data " + dataA, Toast.LENGTH_LONG).show();
           try {
               switch (get_done_counter) {

                   case "done1": {
                       Day1.setEnabled(false);
                       break;
                   }
                   case "done2": {
                       Day2.setEnabled(false);
                       break;
                   }
                   case "done3": {
                       Day3.setEnabled(false);

                       break;
                   }
                   case "done4": {
                       Day4.setEnabled(false);
                       break;
                   }
                   case "done5": {
                       ;
                       Day5.setEnabled(false);
                       break;
                   }
                   case "done6": {
                       Day6.setEnabled(false);
                       break;
                   }
                   case "done7": {
                       Day7.setEnabled(false);
                       break;
                   }
                   case "done8": {
                       Day8.setEnabled(false);
                       break;
                   }
                   case "done9": {
                       Day9.setEnabled(false);
                       break;
                   }

                   case "done10": {
                       Day10.setEnabled(false);
                       break;
                   }

                   case "done11": {
                       Day11.setEnabled(false);
                       break;
                   }

                   case "done12": {
                       Day12.setEnabled(false);
                       break;
                   }

                   case "done13": {
                       Day13.setEnabled(false);
                       break;
                   }

                   case "done14": {
                       Day14.setEnabled(false);
                       break;
                   }

                   case "done15": {
                       Day15.setEnabled(false);
                       break;
                   }
                   default: {
                       Toast.makeText(this, "Please make selection!", Toast.LENGTH_SHORT).show();
                   }
               }
           }
           catch (Exception e)
           {
//               Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
           }

    }

}
