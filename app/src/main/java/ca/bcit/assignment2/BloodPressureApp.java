package ca.bcit.assignment2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class BloodPressureApp extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();

    DatabaseReference dbRef = database.getReference().child("BPReadings");

    ArrayList<BPReading> bpReadingsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try
        {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){}

        setContentView(R.layout.activity_main);

//        Button create_button = findViewById(R.id.bt_create);
//
//        create_button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                createReading(v);
//            }
//        });


        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                bpReadingsList.clear();

                for (DataSnapshot studentSnapshot : dataSnapshot.getChildren()) {
                    BPReading bpReading = studentSnapshot.getValue(BPReading.class);
                    bpReadingsList.add(bpReading);
                }

                displayReadings(bpReadingsList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        Button genReportBtn = findViewById(R.id.btn_GenReport);
        genReportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), MTDReport.class);
                startActivity(intent);
            }
        });

    }
    public void createReading(View view) {
        LinearLayout displayLayout = findViewById(R.id.displayLayout);
        removeAllChildViews(displayLayout);

        EditText editText = findViewById(R.id.UserId);
        String userId = editText.getText().toString();
        editText.setText("");

        editText = findViewById(R.id.txSystolicReading);
        String systolicReading = editText.getText().toString();
        editText.setText("");

        editText = findViewById(R.id.txDiastolicReading);
        String diastolicReading = editText.getText().toString();
        editText.setText("");


        BPReading bpReading = new BPReading(userId,
                systolicReading,
                diastolicReading);
        if(bpReading.condition.equals("HYPERTENSIVE")){
            Toast.makeText(BloodPressureApp.this,
                    "You should consult a doctor ASAP!",
                    Toast.LENGTH_SHORT).show();
        }
        Task setValueTask = dbRef.child(bpReading.id).setValue(bpReading);

        hideSoftKeyboard(view);

        setValueTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(BloodPressureApp.this,
                        getString(R.string.db_fetch_err) + e.toString(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void displayReadings(ArrayList<BPReading> bpReadingsList){
        for (int i = 0; i < bpReadingsList.size(); i++) {

            final int ADDED_MARGINS = 160;

            LinearLayout displayLayout = findViewById(R.id.displayLayout);
            int paddingBottom = displayLayout.getPaddingBottom();
            displayLayout.setPadding(0, 0, 0, paddingBottom + ADDED_MARGINS);

            LinearLayout displaySublayout1 = new LinearLayout(this);
            displaySublayout1.setOrientation(LinearLayout.HORIZONTAL);

            TextView userId = viewCreatorLine1(getString(R.string.prepend_userId),
                    bpReadingsList.get(i).userId);

            TextView systolicReading = viewCreatorLine1(getString(R.string.prepend_systolic),
                    bpReadingsList.get(i).systolicReading);

            TextView diastolicReading = viewCreatorLine1(getString(R.string.prepend_diastolic),
                    bpReadingsList.get(i).diastolicReading);

            displaySublayout1.addView(userId);
            displaySublayout1.addView(systolicReading);
            displaySublayout1.addView(diastolicReading);

            LinearLayout displaySublayout2 = new LinearLayout(this);
            displaySublayout2.setOrientation(LinearLayout.HORIZONTAL);

            TextView date  = viewCreatorLine2(getString(R.string.prepend_date),
                    bpReadingsList.get(i).date);

            TextView time  = viewCreatorLine2(getString(R.string.prepend_time),
                    bpReadingsList.get(i).time);

            displaySublayout2.addView(date);
            displaySublayout2.addView(time);

            LinearLayout displaySublayout3 = new LinearLayout(this);
            displaySublayout1.setOrientation(LinearLayout.HORIZONTAL);

            TextView condition = viewCreatorLine2(getString(R.string.prepend_condition),
                    bpReadingsList.get(i).condition);

            displaySublayout3.addView(condition);

            LinearLayout displaySublayout4 = new LinearLayout(this);
            displaySublayout1.setOrientation(LinearLayout.HORIZONTAL);


            final String id = bpReadingsList.get(i).id;

            TextView editReading = viewCreatorLine3("", getString(R.string.edit));
            editReading.setTypeface(Typeface.MONOSPACE, Typeface.BOLD_ITALIC);

            editReading.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    LinearLayout displayLayout = findViewById(R.id.displayLayout);
                    removeAllChildViews(displayLayout);

                    Intent intent = new Intent(view.getContext(), EditEntryActivity.class);
                    intent.putExtra("id", id);
                    startActivity(intent);
                }
            });

            TextView removeReadingButton = viewCreatorLine3("", getString(R.string.remove));
            removeReadingButton.setTypeface(Typeface.MONOSPACE, Typeface.BOLD_ITALIC);

            removeReadingButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    removeAllChildViews((LinearLayout) view.getParent().getParent().getParent());
                    dbRef.child(id).removeValue();
                }
            });

            displaySublayout4.addView(editReading);
            displaySublayout4.addView(removeReadingButton);

            LinearLayout readingWrapperLayout = new LinearLayout(this);
            readingWrapperLayout.setOrientation(LinearLayout.VERTICAL);


            readingWrapperLayout.addView(displaySublayout1);
            readingWrapperLayout.addView(displaySublayout2);
            readingWrapperLayout.addView(displaySublayout3);
            readingWrapperLayout.addView(displaySublayout4);
            displayLayout.addView(readingWrapperLayout);
        }
    }


    public TextView viewCreatorLine1(String prependText, String text){
        TextView readingText = new TextView(this);
        readingText.setText(prependText + text);
        readingText.setId(View.generateViewId());
        readingText.setTextSize(15);
        readingText.setPadding(10,10,10,0);

        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(10, 10, 10, 0);
        readingText.setLayoutParams(lp);

        return readingText;
    }

    public TextView viewCreatorLine2(String prependText, String text){
        TextView readingText = new TextView(this);
        readingText.setText(prependText + text);
        readingText.setId(View.generateViewId());
        readingText.setTextSize(12);
        readingText.setPadding(10,0,10,0);

        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(10, 0, 10, 0);
        readingText.setLayoutParams(lp);

        return readingText;
    }

    public TextView viewCreatorLine3(String prependText, String text){
        TextView readingText = new TextView(this);
        readingText.setText(prependText + text);
        readingText.setId(View.generateViewId());
        readingText.setTextSize(12);
        readingText.setPadding(10,0,10,10);

        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(10, 0, 10, 10);
        readingText.setLayoutParams(lp);

        return readingText;
    }


    public void hideSoftKeyboard(View view){
        InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }


    void removeAllChildViews(ViewGroup viewGroup) {

        while (viewGroup.getChildCount() > 0) {
            View child = viewGroup.getChildAt(0);
            viewGroup.removeView(child);
        }
    }
}
class BPReading {
    public String id;
    public String userId;
    public String time;
    public String date;
    public String systolicReading;
    public String diastolicReading;
    public String condition;

    public BPReading() {}


    public BPReading(String userId, String systolicReading,
                     String diastolicReading) {

        this.id = String.valueOf(System.currentTimeMillis());
        this.userId = userId;
        Date currentDate = new Date(System.currentTimeMillis());
        SimpleDateFormat dateTimeFormatter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        String dateTime = dateTimeFormatter.format(currentDate);
        String[] splitDateTime = dateTime.split(" ");
        this.date = splitDateTime[0];
        this.time = splitDateTime[1];

        this.systolicReading = systolicReading;
        this.diastolicReading = diastolicReading;

        int systolicReadingInt = Integer.parseInt(this.systolicReading);
        int diastolicReadingInt = Integer.parseInt(this.diastolicReading);
        if(systolicReadingInt > 180 || diastolicReadingInt > 120){
            this.condition = ConditionTypes.HYPERTENSIVE.toString();
        } else if(systolicReadingInt >= 140 || diastolicReadingInt >= 90){
            this.condition = ConditionTypes.STAGE2.toString();
        } else if(systolicReadingInt >= 130 || diastolicReadingInt >= 80){
            this.condition = ConditionTypes.STAGE1.toString();
        } else if(systolicReadingInt >= 120){
            this.condition = ConditionTypes.ELEVATED.toString();
        } else {
            this.condition = ConditionTypes.NORMAL.toString();
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTime() {
        return time;
    }

    public void setTime() {
        Date currentDate = new Date(System.currentTimeMillis());
        SimpleDateFormat dateTimeFormatter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        String dateTime = dateTimeFormatter.format(currentDate);
        String[] splitDateTime = dateTime.split(" ");
        this.date = splitDateTime[0];
        this.time = splitDateTime[1];
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        Date currentDate = new Date(System.currentTimeMillis());
        SimpleDateFormat dateTimeFormatter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        String dateTime = dateTimeFormatter.format(currentDate);
        String[] splitDateTime = dateTime.split(" ");
        this.date = splitDateTime[0];
        this.time = splitDateTime[1];
    }

    public String getSystolicReading() {
        return systolicReading;
    }

    public void setSystolicReading(String systolicReading) {
        this.systolicReading = systolicReading;
    }

    public String getDiastolicReading() {
        return diastolicReading;
    }

    public void setDiastolicReading(String diastolicReading) {
        this.diastolicReading = diastolicReading;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition() {
        int systolicReadingInt = Integer.parseInt(this.getSystolicReading());
        int diastolicReadingInt = Integer.parseInt(this.getDiastolicReading());
        if(systolicReadingInt > 180 || diastolicReadingInt > 120){
            this.condition = ConditionTypes.HYPERTENSIVE.toString();
        } else if(systolicReadingInt >= 140 || diastolicReadingInt >= 90){
            this.condition = ConditionTypes.STAGE2.toString();
        } else if(systolicReadingInt >= 130 || diastolicReadingInt >= 80){
            this.condition = ConditionTypes.STAGE1.toString();
        } else if(systolicReadingInt >= 120){
            this.condition = ConditionTypes.ELEVATED.toString();
        } else {
            this.condition = ConditionTypes.NORMAL.toString();
        }
    }
}

enum ConditionTypes{
    NORMAL, ELEVATED, STAGE1, STAGE2, HYPERTENSIVE
}