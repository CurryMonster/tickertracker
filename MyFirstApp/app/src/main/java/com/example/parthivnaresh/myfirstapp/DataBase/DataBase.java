package com.example.parthivnaresh.myfirstapp.DataBase;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.parthivnaresh.myfirstapp.R;


public class DataBase extends AppCompatActivity {

    DatabaseHelper myDB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.blank);

        myDB = new DatabaseHelper(this);
        boolean isinserted = myDB.insertData("Olive Oil", 124, 14f, 0f, 0f);
        if (isinserted) {
            Toast.makeText(this, "Data inserted", Toast.LENGTH_SHORT);
            System.out.println("MADE IT");
        } else {
            Toast.makeText(this, "Data not inserted", Toast.LENGTH_SHORT);
            System.out.println("DIDN'T MADE IT");
        }

        Cursor res = myDB.getAllData();
        if (res.getCount()==0) {
            System.out.println("NOTHING");
            return;
        } else {
            StringBuffer buffer = new StringBuffer();
            while (res.moveToNext()) {
                buffer.append("ID: " + res.getString(0) + "\n");
                buffer.append("NAME: " + res.getString(1) + "\n");
                buffer.append("CALORIES: " + res.getString(2) + "\n");
                buffer.append("FAT: " + res.getString(3) + "\n");
                buffer.append("PROTEIN: " + res.getString(4) + "\n");
                buffer.append("CARBOHYDRATES: " + res.getString(5) + "\n\n");

            }
            System.out.println("SOMETHING " + buffer.toString());
        }

        boolean isUpdated = myDB.updateData("1", "Extra Virgin Olive Oil", 112, 12.6f, 0f, 0f);
        if (isUpdated) {
            System.out.println("UPDATED CORRECTLY");
        } else {
            System.out.println("DIDN'T UPDATE CORRECTLY");
        }
    }

}
