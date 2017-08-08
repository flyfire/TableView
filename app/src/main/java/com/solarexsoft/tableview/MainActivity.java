package com.solarexsoft.tableview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.solarexsoft.solarextableview.TableView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TableView tableView = (TableView)findViewById(R.id.table);
        MatrixTableAdapter<String> matrixTableAdapter = new MatrixTableAdapter<String>(this,new String[][]{
                {"Header1","Header2","Header3","Header4","Header5","Header6","Header7","Header8","Header9","Header10"},
                {"S","U","N","N","Y","S","U","N","N","Y"},
                {"S","O","L","A","R","S","O","L","A","R"},
                {"T","A","B","L","E","T","A","B","L","E"},
                {"S","U","N","N","Y","S","U","N","N","Y"},
                {"S","O","L","A","R","S","O","L","A","R"},
                {"T","A","B","L","E","T","A","B","L","E"},
                {"S","U","N","N","Y","S","U","N","N","Y"},
                {"S","O","L","A","R","S","O","L","A","R"},
                {"T","A","B","L","E","T","A","B","L","E"},
                {"S","U","N","N","Y","S","U","N","N","Y"},
                {"S","O","L","A","R","S","O","L","A","R"},
                {"T","A","B","L","E","T","A","B","L","E"},
                {"S","U","N","N","Y","S","U","N","N","Y"},
                {"S","O","L","A","R","S","O","L","A","R"},
                {"T","A","B","L","E","T","A","B","L","E"},
                {"S","U","N","N","Y","S","U","N","N","Y"},
                {"S","O","L","A","R","S","O","L","A","R"},
                {"T","A","B","L","E","T","A","B","L","E"},
                {"S","U","N","N","Y","S","U","N","N","Y"},
                {"S","O","L","A","R","S","O","L","A","R"},
                {"T","A","B","L","E","T","A","B","L","E"},
                {"S","U","N","N","Y","S","U","N","N","Y"},
                {"S","O","L","A","R","S","O","L","A","R"},
                {"T","A","B","L","E","T","A","B","L","E"},
                {"S","U","N","N","Y","S","U","N","N","Y"},
                {"S","O","L","A","R","S","O","L","A","R"},
                {"T","A","B","L","E","T","A","B","L","E"},
                {"S","U","N","N","Y","S","U","N","N","Y"},
                {"S","O","L","A","R","S","O","L","A","R"},
                {"T","A","B","L","E","T","A","B","L","E"},
                {"S","U","N","N","Y","S","U","N","N","Y"},
                {"S","O","L","A","R","S","O","L","A","R"},
                {"T","A","B","L","E","T","A","B","L","E"},
                {"S","U","N","N","Y","S","U","N","N","Y"},
                {"S","O","L","A","R","S","O","L","A","R"},
                {"T","A","B","L","E","T","A","B","L","E"}
        });
        tableView.setAdapter(matrixTableAdapter);
    }
}
