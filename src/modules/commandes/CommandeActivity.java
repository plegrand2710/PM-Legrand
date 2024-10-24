package com.pauline.dm;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class CommandeActivity extends AppCompatActivity {
    ImageButton ib1, ib2, ib3, ib4, ib5, ib6, ib7, ib8, ib9, ib10, ib11, ib12, ib13, ib14, ib15, ib16;
    Button bValide;
    EditText etNum ;
    ArrayList<Integer> tableOccupe = null;
    Integer nbTable = 16 ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commande);

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

        listenerTable();
    }

    public Boolean tableAvecClient(Integer x){
        for(int i = 0 ; i < tableOccupe.size() ; i ++){
            if(tableOccupe.get(i) == x){
                return true ;
            }
        }
        return false ;
    }

    public void commander(Boolean b, Integer nb){
        if(!b){
            Toast.makeText(getApplicationContext(), "Créer une nouvelle commande", Toast.LENGTH_SHORT).show();

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
                        }
                    }) ;
            AlertDialog alert = a_builder.create();
            alert.setTitle("Une commande existe déja pour la table " + nb + " !");
            alert.show();
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
        ib1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commander(tableAvecClient(1), 1);
            }
        });
        ib2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commander(tableAvecClient(2), 2);
            }
        });
        ib3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commander(tableAvecClient(3), 3);
            }
        });
        ib4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commander(tableAvecClient(4), 4);
            }
        });
        ib5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commander(tableAvecClient(5), 5);
            }
        });
        ib6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commander(tableAvecClient(6), 6);
            }
        });
        ib7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commander(tableAvecClient(7), 7);
            }
        });
        ib8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commander(tableAvecClient(8), 8);
            }
        });
        ib9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commander(tableAvecClient(9), 9);
            }
        });
        ib10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commander(tableAvecClient(10), 10);
            }
        });
        ib11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commander(tableAvecClient(11), 11);
            }
        });
        ib12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commander(tableAvecClient(12), 12);
            }
        });
        ib13.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commander(tableAvecClient(13), 13);
            }
        });
        ib14.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commander(tableAvecClient(14), 14);
            }
        });
        ib15.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commander(tableAvecClient(15), 15);
            }
        });
        ib16.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                commander(tableAvecClient(16), 16);
            }
        });
    }
}
