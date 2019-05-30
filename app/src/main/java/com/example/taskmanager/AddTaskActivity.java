package com.example.taskmanager;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;

public class AddTaskActivity extends AppCompatActivity {
    EditText etName, etDesc, etTime;
    Button btnAdd, btnCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        etDesc = findViewById(R.id.ediTextDesc);
        etName = findViewById(R.id.editTextName);
        etTime = findViewById(R.id.editTextTime);
        btnAdd = findViewById(R.id.buttonAdd);
        btnCancel = findViewById(R.id.buttonCancel);


        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = etName.getText().toString();
                String desc = etDesc.getText().toString();
                DBHelper dbh = new DBHelper(AddTaskActivity.this);
                long row_affected = dbh.insertTask(name, desc);
                dbh.close();
                if (row_affected != -1) {
                    showNotification(Integer.parseInt(etTime.getText().toString()));
                    Toast.makeText(AddTaskActivity.this, "Added successfully",
                            Toast.LENGTH_SHORT).show();
                    etName.setText("");
                    etDesc.setText("");
                    etTime.setText("");
                    Intent i = new Intent();
                    setResult(RESULT_OK, i);

                    finish();
                }
            }
        });


        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void showNotification(int time){
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.SECOND, time);

        Intent intent = new Intent(AddTaskActivity.this, NotificationReceiver.class);
        intent.putExtra("Data", etName.getText().toString());
        int requestCode = 888;
        PendingIntent pIntent = PendingIntent.getBroadcast(AddTaskActivity.this, requestCode, intent, PendingIntent.FLAG_CANCEL_CURRENT);

        AlarmManager am = (AlarmManager) getSystemService(Activity.ALARM_SERVICE);
        am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pIntent);
        setResult(RESULT_OK, intent);

        finish();
    }

}
