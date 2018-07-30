package sg.edu.rp.c346.reservation;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Size;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    EditText etName;
    EditText etNum;
    EditText etPax;
    EditText etDate;
    EditText etTime;
    RadioButton rbSmoke;
    RadioButton rbNonSmoke;
    Button btnSubmit;
    Button btnReset;

    DatePickerDialog myDateDialog;
    TimePickerDialog myTimeDialog;

    Calendar c = Calendar.getInstance();
    // Get current Date
    int Year = c.get(Calendar.YEAR);
    int month = c.get(Calendar.MONTH);
    int dayofMonth = c.get(Calendar.DAY_OF_MONTH);
    // Get current Time
    int hod = c.get(Calendar.HOUR_OF_DAY);
    int min = c.get(Calendar.MINUTE);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        etName = findViewById(R.id.editTextName);
        etNum = findViewById(R.id.editTextMobile);
        etPax = findViewById(R.id.editTextPax);
        etDate = findViewById(R.id.editTextDate);
        etTime = findViewById(R.id.editTextTime);
        rbSmoke = findViewById(R.id.radioButtonSmoke);
        rbNonSmoke = findViewById(R.id.radioButtonNonSmoke);
        btnSubmit = findViewById(R.id.buttonSubmit);
        btnReset = findViewById(R.id.buttonReset);

        etDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create the Listener to get the date
                DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                        etDate.setText(dayOfMonth + "/" + (monthOfYear+1) + "/" + year);
                        Year = year;
                        month = monthOfYear;
                        dayofMonth = dayOfMonth;
                    }
                };

                // Create the Date Picker Dialog
                myDateDialog = new DatePickerDialog(MainActivity.this, myDateListener, Year, month, dayofMonth);
                myDateDialog.updateDate(Year, month, dayofMonth);
                myDateDialog.show();
            }
        });

        etTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create the Listener to set the time
                TimePickerDialog.OnTimeSetListener myTimeListener = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
                        etTime.setText(hourOfDay + ":" + minute);
                        hod = hourOfDay;
                        min = minute;
                    }
                };

                // Create the Time Picker Dialog
                myTimeDialog = new TimePickerDialog(MainActivity.this, myTimeListener, hod, min, true);
                myTimeDialog.updateTime(hod, min);
                myTimeDialog.show();
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //name
                String name = "Name: " + etName.getText().toString();
                //mobile
                String mobile = "Mobile: " + etNum.getText().toString();
                //pax
                String pax = "Size: " + etPax.getText().toString();

                //area
                String smoking = "";
                if (rbSmoke.isChecked()) {
                    smoking = "Yes";
                }
                else if (rbNonSmoke.isChecked()) {
                    smoking = "No";
                }

                //date
                String date = "Date: " + etDate.getText().toString();

                //time
                String time = "Time: " + etTime.getText().toString();

                //Create the Dialog Builder
                final AlertDialog.Builder myBuilder = new AlertDialog.Builder(MainActivity.this);

                //Set the dialog details
                myBuilder.setTitle("Confirm Your Order");

                myBuilder.setMessage("New Reservation\n" + name + "\n" + mobile + "\n" + "Smoking: " + smoking + "\n" + pax + "\n" + date + "\n" + time);

                myBuilder.setCancelable(false);

                //Configure the 'positive' button
                myBuilder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });

                //Configure the 'neutral' button
                myBuilder.setNeutralButton("Cancel", null);
                AlertDialog myDialog = myBuilder.create();
                myDialog.show();
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                etName.setText(null);
                etNum.setText(null);
                etPax.setText(null);
                if (rbSmoke.isChecked()) {
                    rbSmoke.setChecked(false);
                }
                if (rbNonSmoke.isChecked()) {
                    rbNonSmoke.setChecked(false);
                }

                //Reset Dialog to current date
                dayofMonth = c.get(Calendar.DAY_OF_MONTH);
                month = c.get(Calendar.MONTH);
                Year = c.get(Calendar.YEAR);
                etDate.setText(dayofMonth + "/" + (month+1) + "/" + Year);

                //Reset Dialog to current time
                hod = c.get(Calendar.HOUR_OF_DAY);
                min = c.get(Calendar.MINUTE);
                etTime.setText(hod + ":" + min);


            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();

        //Step 1a: Obtain an instance of the SharedPreferences
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Step 1b: Obtain an instance of the SharedPreference Editor for update later
        SharedPreferences.Editor prefEdit = prefs.edit();
        //Step 1c: Add the key-value pair
        prefEdit.putString("name", etName.getText().toString());
        prefEdit.putString("num", etNum.getText().toString());
        if (rbNonSmoke.isChecked()) {
            prefEdit.putInt("smoking", 0);
        }
        if (rbSmoke.isChecked()) {
            prefEdit.putInt("smoking", 1);
        }
        prefEdit.putString("size", etPax.getText().toString());
        prefEdit.putString("date", etDate.getText().toString());
        prefEdit.putString("time", etTime.getText().toString());
        //Step 1d: Call commit() method to save the changes into the SharedPreferences
        prefEdit.commit();
    }

    @Override
    protected void onResume() {
        super.onResume();

        //Step 2a: Obtain an instance of the SharedPreferences
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Step 2b: Retrieve the saved data with the key "greeting from the SharedPreferences object
        String name = prefs.getString("name", "No Name");
        String num = prefs.getString("num", "No Mobile");
        int smoking = prefs.getInt("smoking", 0);
        String size = prefs.getString("size", "No Size");
        String date = prefs.getString("date", "No Date");
        String time = prefs.getString("time", "No Time");

        etName.setText(name);
        etNum.setText(num);
        etPax.setText(size);

        if (smoking == 0) {
            rbNonSmoke.setChecked(true);
        }
        if (smoking == 1) {
            rbSmoke.setChecked(true);
        }

        etDate.setText(date);

        etTime.setText(time);
    }
}