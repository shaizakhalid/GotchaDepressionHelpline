package com.example.gotcha_depressionhelpline;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class exerciseActivity extends AppCompatActivity {
    private ListView listView;
    private ExerciseAdapter mAdapter;
    ArrayList<Exercise> exerciseList = new ArrayList<>();
    String day_ID, done_counter;
    Button done_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise);
        listView = findViewById(R.id.exercise_list);

        done_button = findViewById(R.id.button_Done);

        Intent intent = getIntent();
        day_ID = intent.getStringExtra("Day_ID");

        switch (day_ID) {
            case "1": {

                exerciseList.add(new Exercise("Walk", "Direction:\n" +
                        "Choose a location of your liking and take a talk. Try to choose green spaces.\t \n" +
                        "Duration: 30 min \n", R.drawable.walk_icon)); //add node
                exerciseList.add(new Exercise("Concentration Meditation", "Direction\n" +
                        "Choose an object and concentrate on it. If your mind wanders, simply refocus your awareness on the chosen object of attention each time. Through this process, your ability to concentrate improves.\n" +
                        "Duration: 15 minutes\n", R.drawable.meditation));
                exerciseList.add(new Exercise("Write Journal", "Direction\n" +
                        "Write up what you feel in your journal today. Journaling helps control your symptoms and improve your mood.\n" +
                        "Duration: Your choice\n", R.drawable.journaling_icon));
                done_counter = "done1";
//                Toast.makeText(this, done_counter, Toast.LENGTH_SHORT).show();
                break;
            }

            case "2": {

                exerciseList.add(new Exercise("Walk", "Direction:\n" +
                        "Choose a location of your liking and take a talk. Try to choose green spaces.\t \n" +
                        "Duration: 40 min \n", R.drawable.walk_icon)); //add node
                exerciseList.add(new Exercise("Concentration Meditation", "Direction\n" +
                        "Choose an object and concentrate on it. If your mind wanders, simply refocus your awareness on the chosen object of attention each time. Through this process, your ability to concentrate improves.\n" +
                        "Duration: 20 minutes\n", R.drawable.meditation));
                exerciseList.add(new Exercise("Write Journal", "Direction\n" +
                        "Write up what you feel in your journal today. Journaling helps control your symptoms and improve your mood.\n" +
                        "Duration: Your choice\n", R.drawable.journaling_icon));
                done_counter = "done2";
                break;
            }

            case "3": {

                exerciseList.add(new Exercise("Walk", "Direction:\n" +
                        "Choose a location of your liking and take a talk. Try to choose green spaces.\t \n" +
                        "Duration: 1 hour \n", R.drawable.walk_icon));
                exerciseList.add(new Exercise("Concentration Meditation", "Direction\n" +
                        "Choose an object and concentrate on it. If your mind wanders, simply refocus your awareness on the chosen object of attention each time. Through this process, your ability to concentrate improves.\n" +
                        "Duration: 20 minutes\n", R.drawable.meditation));
                exerciseList.add(new Exercise("Write Journal", "Direction\n" +
                        "Write up what you feel in your journal today. Journaling helps control your symptoms and improve your mood.\n" +
                        "Duration: Your choice\n", R.drawable.journaling_icon));
                done_counter = "done3";
                break;
            }

            case "4": {

                exerciseList.add(new Exercise("Walk", "Direction:\n" +
                        "Choose a location of your liking and take a talk. Try to choose green spaces.\t \n" +
                        "Duration: 1 hour \n", R.drawable.walk_icon));
                exerciseList.add(new Exercise("Concentration Meditation", "Direction\n" +
                        "Choose an object and concentrate on it. If your mind wanders, simply refocus your awareness on the chosen object of attention each time. Through this process, your ability to concentrate improves.\n" +
                        "Duration: 30 minutes\n", R.drawable.meditation));
                exerciseList.add(new Exercise("Write Journal", "Direction\n" +
                        "Write up what you feel in your journal today. Journaling helps control your symptoms and improve your mood.\n" +
                        "Duration: Your choice\n", R.drawable.journaling_icon));
                done_counter = "done4";
                break;
            }

            case "5": {

                exerciseList.add(new Exercise("Walk", "Direction:\n" +
                        "Choose a location of your liking and take a talk. Try to choose green spaces.\t \n" +
                        "Duration: 1 hour \n", R.drawable.walk_icon));
                exerciseList.add(new Exercise("Concentration Meditation", "Direction\n" +
                        "Choose an object and concentrate on it. If your mind wanders, simply refocus your awareness on the chosen object of attention each time. Through this process, your ability to concentrate improves.\n" +
                        "Duration: 30 minutes\n", R.drawable.meditation));
                exerciseList.add(new Exercise("Write Journal", "Direction\n" +
                        "Write up what you feel in your journal today. Journaling helps control your symptoms and improve your mood.\n" +
                        "Duration: Your choice\n", R.drawable.journaling_icon));
                done_counter = "done5";
                break;
            }

            case "6": {

                exerciseList.add(new Exercise("Jog", "Direction:\n" +
                        "Choose a location of your liking and go for a jog. \n" +
                        "Duration: 30 minutes \n ", R.drawable.jog_icon));
                exerciseList.add(new Exercise("Concentration Meditation", "Direction\n" +
                        "Choose an object and concentrate on it. If your mind wanders, simply refocus your awareness on the chosen object of attention each time. Through this process, your ability to concentrate improves.\n" +
                        "Duration: 30 minutes\n", R.drawable.meditation));
                exerciseList.add(new Exercise("Write Journal", "Direction\n" +
                        "Write up what you feel in your journal today. Journaling helps control your symptoms and improve your mood.\n" +
                        "Duration: Your choice\n", R.drawable.journaling_icon));
                done_counter = "done6";
                break;
            }

            case "7": {

                exerciseList.add(new Exercise("Jog", "Direction:\n" +
                        "Choose a location of your liking and go for a jog. \n" +
                        "Duration: 30 minutes \n ", R.drawable.jog_icon));
                exerciseList.add(new Exercise("Concentration Meditation", "Direction\n" +
                        "Choose an object and concentrate on it. If your mind wanders, simply refocus your awareness on the chosen object of attention each time. Through this process, your ability to concentrate improves.\n" +
                        "Duration: 30 minutes\n", R.drawable.meditation));
                exerciseList.add(new Exercise("Write Journal", "Direction\n" +
                        "Write up what you feel in your journal today. Journaling helps control your symptoms and improve your mood.\n" +
                        "Duration: Your choice\n", R.drawable.journaling_icon));
                done_counter = "done7";
                break;
            }
            case "8": {

                exerciseList.add(new Exercise("Jog", "Direction:\n" +
                        "Choose a location of your liking and go for a jog. \n" +
                        "Duration: 45 minutes \n ", R.drawable.jog_icon));
                exerciseList.add(new Exercise("Mindfulness Meditation", "Direction\n" +
                        "Sit in a quite comfortable place. Sit a timer and focus on your breathing. Feel the breath  moving in and out of your body as you breathe. It will help you to slow down racing thoughts  and calm both your mind and body.\n" +
                        "Duration: 15 minutes\n", R.drawable.meditation));
                exerciseList.add(new Exercise("Write Journal", "Direction\n" +
                        "Write up what you feel in your journal today. Journaling helps control your symptoms and improve your mood.\n" +
                        "Duration: Your choice\n", R.drawable.journaling_icon));
                done_counter = "done8";
                break;
            }
            case "9": {

                exerciseList.add(new Exercise("Jog", "Direction:\n" +
                        "Choose a location of your liking and go for a jog. \n" +
                        "Duration: 45 minutes \n ", R.drawable.jog_icon)); //add node
                exerciseList.add(new Exercise("Mindfulness Meditation", "Direction\n" +
                        "Sit in a quite comfortable place. Sit a timer and focus on your breathing. Feel the breath  moving in and out of your body as you breathe. It will help you to slow down racing thoughts  and calm both your mind and body.\n" +
                        "Duration: 15 minutes\n", R.drawable.meditation));
                exerciseList.add(new Exercise("Write Journal", "Direction\n" +
                        "Write up what you feel in your journal today. Journaling helps control your symptoms and improve your mood.\n" +
                        "Duration: Your choice\n", R.drawable.journaling_icon));
                done_counter = "done9";
                break;
            }
            case "10": {

                exerciseList.add(new Exercise("Jog", "Direction:\n" +
                        "Choose a location of your liking and go for a jog. \n" +
                        "Duration: 45 minutes \n ", R.drawable.jog_icon));
                exerciseList.add(new Exercise("Mindfulness Meditation", "Direction\n" +
                        "Sit in a quite comfortable place. Sit a timer and focus on your breathing. Feel the breath  moving in and out of your body as you breathe. It will help you to slow down racing thoughts  and calm both your mind and body.\n" +
                        "Duration: 20 minutes\n", R.drawable.meditation));
                exerciseList.add(new Exercise("Write Journal", "Direction\n" +
                        "Write up what you feel in your journal today. Journaling helps control your symptoms and improve your mood.\n" +
                        "Duration: Your choice\n", R.drawable.journaling_icon));
                done_counter = "done10";
                break;
            }
            case "11": {

                exerciseList.add(new Exercise("Jog", "Direction:\n" +
                        "Choose a location of your liking and go for a jog. \n" +
                        "Duration: 1 hour \n ", R.drawable.jog_icon));
                exerciseList.add(new Exercise("Mindfulness Meditation", "Direction\n" +
                        "Sit in a quite comfortable place. Sit a timer and focus on your breathing. Feel the breath  moving in and out of your body as you breathe. It will help you to slow down racing thoughts  and calm both your mind and body.\n" +
                        "Duration: 20 minutes\n", R.drawable.meditation));
                exerciseList.add(new Exercise("Write Journal", "Direction\n" +
                        "Write up what you feel in your journal today. Journaling helps control your symptoms and improve your mood.\n" +
                        "Duration: Your choice\n", R.drawable.journaling_icon));
                done_counter = "done11";
                break;
            }
            case "12": {

                exerciseList.add(new Exercise("Jog", "Direction:\n" +
                        "Choose a location of your liking and go for a jog. \n" +
                        "Duration: 1 hour \n ", R.drawable.jog_icon));
                exerciseList.add(new Exercise("Mindfulness Meditation", "Direction\n" +
                        "Sit in a quite comfortable place. Sit a timer and focus on your breathing. Feel the breath  moving in and out of your body as you breathe. It will help you to slow down racing thoughts  and calm both your mind and body.\n" +
                        "Duration: 20 minutes\n", R.drawable.meditation));
                exerciseList.add(new Exercise("Write Journal", "Direction\n" +
                        "Write up what you feel in your journal today. Journaling helps control your symptoms and improve your mood.\n" +
                        "Duration: Your choice\n", R.drawable.journaling_icon));
                done_counter = "done12";
                break;
            }
            case "13": {

                exerciseList.add(new Exercise("Jog", "Direction:\n" +
                        "Choose a location of your liking and go for a jog. \n" +
                        "Duration: 1 hour  \n ", R.drawable.jog_icon));
                exerciseList.add(new Exercise("Mindfulness Meditation", "Direction\n" +
                        "Sit in a quite comfortable place. Sit a timer and focus on your breathing. Feel the breath  moving in and out of your body as you breathe. It will help you to slow down racing thoughts  and calm both your mind and body.\n" +
                        "Duration: 30 minutes\n", R.drawable.meditation));
                exerciseList.add(new Exercise("Write Journal", "Direction\n" +
                        "Write up what you feel in your journal today. Journaling helps control your symptoms and improve your mood.\n" +
                        "Duration: Your choice\n", R.drawable.journaling_icon));
                done_counter = "done13";
                break;
            }
            case "14": {

                exerciseList.add(new Exercise("Jog", "Direction:\n" +
                        "Choose a location of your liking and go for a jog. \n" +
                        "Duration: 1 hour \n ", R.drawable.jog_icon));
                exerciseList.add(new Exercise("Mindfulness Meditation", "Direction\n" +
                        "Sit in a quite comfortable place. Sit a timer and focus on your breathing. Feel the breath  moving in and out of your body as you breathe. It will help you to slow down racing thoughts  and calm both your mind and body.\n" +
                        "Duration: 30 minutes\n", R.drawable.meditation));
                exerciseList.add(new Exercise("Write Journal", "Direction\n" +
                        "Write up what you feel in your journal today. Journaling helps control your symptoms and improve your mood.\n" +
                        "Duration: Your choice\n", R.drawable.journaling_icon));
                done_counter = "done14";
                break;
            }

            case "15": {

                exerciseList.add(new Exercise("Jog", "Direction:\n" +
                        "Choose a location of your liking and go for a jog. \n" +
                        "Duration: 1 hour \n ", R.drawable.jog_icon));
                exerciseList.add(new Exercise("Mindfulness Meditation", "Direction\n" +
                        "Sit in a quite comfortable place. Sit a timer and focus on your breathing. Feel the breath  moving in and out of your body as you breathe. It will help you to slow down racing thoughts  and calm both your mind and body.\n" +
                        "Duration: 30 minutes\n", R.drawable.meditation));
                exerciseList.add(new Exercise("Write Journal", "Direction\n" +
                        "Write up what you feel in your journal today. Journaling helps control your symptoms and improve your mood.\n" +
                        "Duration: Your choice\n", R.drawable.journaling_icon));
                done_counter = "done15";
                break;
            }

            default:

                Intent intentEx = new Intent(this, ExerciseMainPage.class);
                intentEx.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intentEx);
//                finish();
        }
        mAdapter = new ExerciseAdapter(this, exerciseList);
        listView.setAdapter(mAdapter);

        done_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent nav = new Intent(exerciseActivity.this, ExerciseMainPage.class);
                nav.putExtra("done_ID", done_counter);
                nav.addFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(nav, 123);
            }
        });
    }
}


