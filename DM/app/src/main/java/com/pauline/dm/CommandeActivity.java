package com.pauline.dm;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.pauline.dm.GestionBDD.DBAdapter;

import java.util.ArrayList;

public class CommandeActivity extends AppCompatActivity {
    ImageButton ib1, ib2, ib3, ib4, ib5, ib6, ib7, ib8, ib9, ib10, ib11, ib12, ib13, ib14, ib15, ib16;
    Button bValide;
    EditText etNum ;
    ArrayList<Integer> tableOccupe = null;
    Integer nbTable = 16 ;
    DBAdapter db ;
    Integer request_Code = 1;
    Integer tbSelectionnee ;
    Context c ;
    String TAG = "DMProjet";
    private static final int REQUEST_CODE_CONVIVES = 1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commande);

        db = new DBAdapter(this);
        db.open();

        c = this ;
        bValide = (Button) findViewById(R.id.valide);
        etNum = (EditText) findViewById(R.id.NumEntre);
        ib1 = (ImageButton) findViewById(R.id.table1);
        ib2 = (ImageButton) findViewById(R.id.table2);
        ib3 = (ImageButton) findViewById(R.id.table3);
        ib4 = (ImageButton) findViewById(R.id.table4);
        ib5 = (ImageButton) findViewById(R.id.table5);
        ib6 = (ImageButton) findViewById(R.id.table6);
        ib7 = (ImageButton) findViewById(R.id.table7);
        ib8 = (ImageButton) findViewById(R.id.table8);
        ib9 = (ImageButton) findViewById(R.id.table9);
        ib10 = (ImageButton) findViewById(R.id.table10);
        ib11 = (ImageButton) findViewById(R.id.table11);
        ib12 = (ImageButton) findViewById(R.id.table12);
        ib13 = (ImageButton) findViewById(R.id.table13);
        ib14 = (ImageButton) findViewById(R.id.table14);
        ib15 = (ImageButton) findViewById(R.id.table15);
        ib16 = (ImageButton) findViewById(R.id.table16);

        tableOccupe = new ArrayList<>();
        tableOccupe.add(4);
        tableOccupe.add(8);
        tableOccupe.add(16);
        tableOccupe.add(2);

        //db.loadBD();

        listenerTable();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }

    public Boolean tableAvecClient(Integer x) {
        Cursor cursor = db.getTable(x);
        if (cursor != null && cursor.moveToFirst()) {
            int nbConvivesIndex = cursor.getColumnIndex(DBAdapter.KEY_NBCONVIVES);
            int nbConvives = cursor.getInt(nbConvivesIndex);
            Log.d(TAG, "tableAvecClient: nb convives = " + nbConvives);
            cursor.close();
            return nbConvives > 0;
        } else {
            if (cursor != null) {
                cursor.close();
            }
            return false;
        }
    }

    public void commander(Boolean b, Integer nb){
        tbSelectionnee = nb ;
        if(!b){
            Toast.makeText(getApplicationContext(), "Créer une nouvelle commande", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(CommandeActivity.this, GestionConviveActivity.class);
            startActivityForResult(intent, REQUEST_CODE_CONVIVES);
        }
        else if (b) {
            AlertDialog.Builder a_builder = new AlertDialog.Builder(CommandeActivity.this);
            a_builder.setMessage("voulez-vous poursuivre la commande ou en créer une nouvelle ?")
                    .setCancelable(true)
                    .setPositiveButton("Poursuivre",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(getApplicationContext(), "Poursuivre la commande", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("Nouvelle",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(getApplicationContext(), "Créer une nouvelle commande", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(CommandeActivity.this, GestionConviveActivity.class);
                            startActivityForResult(intent, REQUEST_CODE_CONVIVES);                        }
                    }) ;
            AlertDialog alert = a_builder.create();
            alert.setTitle("Une commande existe déja pour la table " + nb + " !");
            alert.show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            int nbConvives = extras.getInt("nbConvives", 0);
            int tableNum = tbSelectionnee;
            if (tableNum > 0) {
                long result = db.insertTable(tableNum, nbConvives, tableNum, tableNum);
                Toast.makeText(CommandeActivity.this, "resultats =  " + result, Toast.LENGTH_SHORT).show();
                if (result != -1) {
                    Toast.makeText(CommandeActivity.this, "Table " + tableNum + " ajoutée avec " + nbConvives + " convives", Toast.LENGTH_SHORT).show();
                } else {
                    db.updateTable(tableNum, nbConvives, tableNum, tableNum);
                    Toast.makeText(CommandeActivity.this, "Mise à jour du nombre de convives avec " + nbConvives, Toast.LENGTH_SHORT).show();
                }
            }
        }
    }




    private void listenerTable(){
        bValide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    int nombre = Integer.parseInt(String.valueOf(etNum.getText()));
                    if(nombre > 0 && nombre <= nbTable){
                        commander(tableAvecClient(nombre), nombre);
                    }
                    else{
                        Toast.makeText(getApplicationContext(), "Numéro de table saisi incorrect.", Toast.LENGTH_SHORT).show();

                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(getApplicationContext(), "Erreur : La chaîne ne peut pas être convertie en nombre.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        addTableClickListener(ib1, 1);
        addTableClickListener(ib2, 2);
        addTableClickListener(ib3, 3);
        addTableClickListener(ib4, 4);
        addTableClickListener(ib5, 5);
        addTableClickListener(ib6, 6);
        addTableClickListener(ib7, 7);
        addTableClickListener(ib8, 8);
        addTableClickListener(ib9, 9);
        addTableClickListener(ib10, 10);
        addTableClickListener(ib11, 11);
        addTableClickListener(ib12, 12);
        addTableClickListener(ib13, 13);
        addTableClickListener(ib14, 14);
        addTableClickListener(ib15, 15);
        addTableClickListener(ib16, 16);
    }

    private void addTableClickListener(ImageButton tableButton, Integer tableNum) {
        tableButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commander(tableAvecClient(tableNum), tableNum);
            }
        });
    }

}
