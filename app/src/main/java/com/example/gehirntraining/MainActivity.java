package com.example.gehirntraining;

import static android.view.View.FIND_VIEWS_WITH_CONTENT_DESCRIPTION;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    FrameLayout frameLayout;
    LinearLayout linearLayout;
    TableLayout tableLayout;

    TextView feedback_tv;
    TextView task_tv;
    TextView timer_tv;
    TextView progress_tv;
    TextView answer0_tv;
    TextView answer1_tv;
    TextView answer2_tv;
    TextView answer3_tv;

    Button playAgain_btn;

    int currentSolution;        //Die Lösung der aktuellen Aufgabe, die in der App angezeigt wird.
    int countCorrectAnswers;    //Anzahl der korrkten Antworten vom Spieler
    int countTotalTasks;        //Anzahl der insgesamt beantworteten Fragen (richtige UND falsche)
    boolean timeIsUp;           //True wenn der Timer abgelaufen ist

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void startOnClick(View view){



        view.setVisibility(View.INVISIBLE);
        linearLayout.setVisibility(View.VISIBLE);
        tableLayout.setVisibility((View.VISIBLE));
        feedback_tv.setVisibility(View.VISIBLE);
        Log.i("Infoo", "ich wurde aufgerufen" + ((Integer)getRandomNumberUsingInts(1, 50)).toString());


        currentSolution = generateTask();
        generateAnswers(currentSolution);
        timeIsUp = false;
        startTimer(31000,1000);
        updateProgress(countCorrectAnswers, countTotalTasks);
        feedback_tv.setVisibility(View.VISIBLE);

    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    public void playAgainOnClick(View btn){
        startOnClick(btn);
        playAgain_btn.setVisibility(View.INVISIBLE);
        feedback_tv.setText("");
        countTotalTasks=0;
        countCorrectAnswers=0;
    }


    //min inclusive, max exclusive
    @RequiresApi(api = Build.VERSION_CODES.N)
    public int getRandomNumberUsingInts(int min, int max) {
        Random random = new Random();
        return random.ints(min, max)
                .findFirst()
                .getAsInt();
    }


    //Generiert eine Aufgabe und legt diese in die task_tv ab. Außerdem wird die dazugehörige Lösung zurückgegeben
    @RequiresApi(api = Build.VERSION_CODES.N)
    public int generateTask(){
        int number_1 = getRandomNumberUsingInts(1, 20);
        int number_2 = getRandomNumberUsingInts(1, 20);
        currentSolution = number_1 + number_2;

        String task = number_1 + " + " + number_2  + " =";
        task_tv.setText(task);

        return currentSolution;


    }

    public void startTimer(long durationInMs, long stepsInMs){
        new CountDownTimer(durationInMs, stepsInMs) {

            public void onTick(long millisUntilFinished) {
                timer_tv.setText(String.valueOf(millisUntilFinished / stepsInMs));               //String.valueOF() konvertiert long --> String
            }

            public void onFinish() {
                timer_tv.setText("done!");
                timeIsUp=true;
                playAgain_btn.setVisibility(View.VISIBLE);
            }
        }.start();

    }


    //Generiert zufällige Antworten und steckt diese in die TextViews. Eine der Antworten ist die Lösung. Die Lösung wurde zuvor in der generateTask() Methode generiert und in currentSolution gespeichert
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void generateAnswers(int currentSolution){
        int temp = getRandomNumberUsingInts(0,4);
        String solution_str= ((Integer) currentSolution).toString();


        //Mache eine ArrayList, packe alle answer_tvs rein. Nehme dann eine zufällige answer_t aus der Liste und packe dort die Lösung (currentSolution) rein
        ArrayList<View> answers_list = new ArrayList<>();


        //Packe alle Elemente aus TableLayout in die ArrayList answer_list. Erste Schleife wählt ein TableRow aus der TableView aus und 2te Schleife packt alle TextViews aus der ausgewählten Tablerow in die ArraList
        //NOTE: Der Grund warum wir das mit Listen machen, ist, weil wir die solution an eine zufällige TextView packen wollen. Solution darf ja nicht immer in der selben TextView erscheinen, sonst macht das Spiel kein sinn.

        int childCountTableLayout = tableLayout.getChildCount();                //Ist 2 weil es 2 TableRows gibt
        for (int i=0; i<childCountTableLayout;i++){
            TableRow tableRow = (TableRow) tableLayout.getChildAt(i);           //Wir nehmen eine bestimmte TableRow
            int tableRowChildCount = tableRow.getChildCount();                  //Anzahl der Childs in der TableRow.

            for(int j=0; j<tableRowChildCount;j++){                             //In der Schleife packen wir dann alle Childs der TableRow (also alle TextViews der jeweiligen TableRow) in die ArrayList
                answers_list.add(tableRow.getChildAt(j));
            }
        }


        //Der Teil ist dann ez. Ich packe die Solution in eine zufällige Textview aus der Liste und entferne diese Textview aus der liste
        ((TextView)answers_list.get(temp)).setText(solution_str);
        answers_list.remove(temp);


        ArrayList<Integer> pickedNumbers = new ArrayList<>();
        //Zum Schluss kann ich jetzt in die restlichen TetViews der Liste einfach iwelche random-werte reinpacken.
        for (int i=0; i<answers_list.size();i++){
            Integer num_1 = (Integer)getRandomNumberUsingInts(1,40);
            //Wir checken hier, dass wir nicht 2 mal die gleiche Zahl nehmen. Damit gehen wir sicher, dass bei den Antworten nicht 2mal die gleiche Zahl angeboten wird.
            while(pickedNumbers.contains(num_1)){
                num_1 = (Integer)getRandomNumberUsingInts(1,40);
            }

                ((TextView) answers_list.get(i)).setText(num_1.toString());
            pickedNumbers.add(num_1);
        }


        Log.i("infoo","Liste hat so viele Elemente: " + answers_list.size());
        Log.i("infoo","tablelayout hat soe viele childs: " + tableLayout.getChildCount());



    }




    public void answerOnClick(TextView selectedAnswer){
        //progressbar aktualisieren
        if(selectedAnswer.getText().equals(String.valueOf(currentSolution))){
            Log.i("Infoooo", "RICHTIG");
        }


    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        frameLayout = findViewById(R.id.frameLayout);
        linearLayout = findViewById(R.id.linearLayout);
        tableLayout = findViewById((R.id.tableLayout));

        feedback_tv = findViewById(R.id.feedback_tv);
        task_tv = findViewById(R.id.task_tv);
        timer_tv = findViewById(R.id.timer_tv);
        progress_tv = findViewById(R.id.progress_tv);
        answer0_tv = findViewById(R.id.answer0_tv);
        answer1_tv = findViewById(R.id.answer1_tv);
        answer2_tv = findViewById(R.id.answer2_tv);
        answer3_tv = findViewById(R.id.answer3_tv);
        playAgain_btn = findViewById(R.id.playAgain_btn);




        answer0_tv.setOnClickListener(this);
        answer1_tv.setOnClickListener(this);
        answer2_tv.setOnClickListener(this);
        answer3_tv.setOnClickListener(this);

        tableLayout.setColumnShrinkable(0,false);
        tableLayout.setColumnShrinkable(1,false);
        tableLayout.setColumnShrinkable(0, false);
        tableLayout.setColumnShrinkable(1, false);

    }

    public void updateProgress(int countCorrectAnswers, int countTotalTasks){
        progress_tv.setText(countCorrectAnswers + "/" + countTotalTasks);
    }

    public void checkIfCorrectAndIncreeaseCounters(TextView clickedTextView){
        if( clickedTextView.getText().equals(((Integer)currentSolution).toString()) ){
            countCorrectAnswers+=1;
            countTotalTasks+=1;
            //Gibt visuelles Feedback, dass die Antwort korrekt war
            feedback_tv.setText("Correct!");
            feedback_tv.setTextColor(Color.parseColor("#0c6d3f"));

        } else {
            countTotalTasks+=1;
            //Gibt visuelles Feedback, dass die Antwort falsch war
            feedback_tv.setText("Wrong!");
            feedback_tv.setTextColor(Color.RED);
        }
    }

    //Set all the onClickListener for the textViews because it has to be done here in code and not in xml!
    //Funktioniert, ABER man sollte wohl nicht ein Switch benutzen. Besser ist If-Else laut Iternet. Ich lasse es jetzt aber erstmal als switch.
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View v) {

        if(!timeIsUp) {

            switch (v.getId()) {
                case R.id.answer0_tv:
                    Log.i("Info", "It is I, Answer0");
                    checkIfCorrectAndIncreeaseCounters((TextView) v);
                    updateProgress(countCorrectAnswers, countTotalTasks);
                    //create new Task and corresponding new answers
                    currentSolution = generateTask();
                    generateAnswers(currentSolution);
                    break;
                case R.id.answer1_tv:
                    Log.i("Info", "It is I, Answer1");
                    checkIfCorrectAndIncreeaseCounters((TextView) v);
                    updateProgress(countCorrectAnswers, countTotalTasks);
                    //create new Task and corresponding new answers
                    currentSolution = generateTask();
                    generateAnswers(currentSolution);
                    break;
                case R.id.answer2_tv:
                    Log.i("Info", "It is I, Answer2");
                    checkIfCorrectAndIncreeaseCounters((TextView) v);
                    updateProgress(countCorrectAnswers, countTotalTasks);
                    //create new Task and corresponding new answers
                    currentSolution = generateTask();
                    generateAnswers(currentSolution);
                    break;
                case R.id.answer3_tv:
                    Log.i("Info", "It is I, Answer3");
                    checkIfCorrectAndIncreeaseCounters((TextView) v);
                    updateProgress(countCorrectAnswers, countTotalTasks);
                    //create new Task and corresponding new answers
                    currentSolution = generateTask();
                    generateAnswers(currentSolution);
                    break;

            }
        }


        //Setze onClick-Methode für playAgain Button
        playAgain_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playAgainOnClick(v);
                progress_tv.setText("0/0");
            }
        });


    }


}




//NOTES ZU DEM CODE:
//Beachte wie ich die Width der Textviews in der TableRow auf 0dp gesetzt habe und nicht auf "Match parent". Das solte man sich glaub ich angewöhnen, weil, wenn man es nicht macht
//werden die spalten des Tablelayourts nicht gleich groß sein.