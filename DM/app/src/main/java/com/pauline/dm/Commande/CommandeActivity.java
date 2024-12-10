package com.pauline.dm.Commande;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.pauline.dm.GestionBDD.DBAdapter;
import com.pauline.dm.R;

import java.util.List;

public class CommandeActivity extends AppCompatActivity {

    private static final String TAG = "DMProjet";
    private DBAdapter db;
    private GridLayout gridLayoutTables;
    private EditText etNum;
    private Integer tbSelectionnee;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commande);

        db = new DBAdapter(this);
        db.open();

        context = this;
        gridLayoutTables = findViewById(R.id.gridLayoutTables);
        etNum = findViewById(R.id.NumEntre);

        setupDynamicTables();
        setupManualTableInput();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
    }

    private void setupDynamicTables() {
        gridLayoutTables.removeAllViews();
        List<Integer> tables = db.getAllTableNumbers();

        for (Integer tableNum : tables) {
            FrameLayout frameLayout = new FrameLayout(this);

            ImageView tableImage = new ImageView(this);
            tableImage.setImageResource(R.drawable.table1);
            tableImage.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

            TextView tableNumber = new TextView(this);
            tableNumber.setText(String.valueOf(tableNum));
            tableNumber.setTextSize(18);
            tableNumber.setTextColor(getResources().getColor(android.R.color.white));
            tableNumber.setGravity(Gravity.CENTER);
            tableNumber.setBackgroundResource(R.drawable.table_number_drawable);

            frameLayout.setOnClickListener(v -> commander(tableAvecClient(tableNum), tableNum));

            frameLayout.addView(tableImage);
            frameLayout.addView(tableNumber);

            GridLayout.LayoutParams params = new GridLayout.LayoutParams();
            params.width = getResources().getDimensionPixelSize(R.dimen.table_button_size);
            params.height = getResources().getDimensionPixelSize(R.dimen.table_button_size);
            params.setMargins(10, 10, 10, 10);

            frameLayout.setLayoutParams(params);

            gridLayoutTables.addView(frameLayout);
        }
    }

    private void setupManualTableInput() {
        findViewById(R.id.valide).setOnClickListener(v -> {
            try {
                int tableNum = Integer.parseInt(etNum.getText().toString().trim());
                if (db.isTableExists(tableNum)) {
                    commander(tableAvecClient(tableNum), tableNum);
                } else {
                    Toast.makeText(context, "Numéro de table invalide.", Toast.LENGTH_SHORT).show();
                }
            } catch (NumberFormatException e) {
                Toast.makeText(context, "Veuillez entrer un numéro valide.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public Boolean tableAvecClient(Integer tableNum) {
        Log.d(TAG, "tableAvecClient: nu tab = " + tableNum);
        int nbConvives = db.getTableConvives(tableNum);
        Log.d(TAG, "tableAvecClient: nb convives = " + nbConvives);
        return nbConvives > 0;
    }

    public void commander(Boolean hasClient, Integer tableNum) {
        tbSelectionnee = tableNum;
        if (!hasClient) {
            Toast.makeText(context, "Créer une nouvelle commande", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(CommandeActivity.this, GestionConviveActivity.class));
            LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter("convives-nombre"));
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(CommandeActivity.this);
            builder.setMessage("Voulez-vous poursuivre la commande ou en créer une nouvelle ?")
                    .setCancelable(true)
                    .setPositiveButton("Poursuivre", (dialog, which) -> {
                        Toast.makeText(context, "Poursuivre la commande", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(CommandeActivity.this, GestionConviveActivity.class);
                        intent.putExtra("tableNum", tableNum);
                        startActivity(intent);
                    })
                    .setNegativeButton("Nouvelle", (dialog, which) -> {
                        Toast.makeText(context, "Créer une nouvelle commande", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(CommandeActivity.this, GestionConviveActivity.class));
                        LocalBroadcastManager.getInstance(context).registerReceiver(mMessageReceiver, new IntentFilter("convives-nombre"));
                    });
            AlertDialog alert = builder.create();
            alert.setTitle("Une commande existe déjà pour la table " + tableNum + " !");
            alert.show();
        }
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int nbConvives = intent.getIntExtra("nbConvives", 0);
            if (tbSelectionnee != null) {
                db.updateOrInsertTable(tbSelectionnee, nbConvives);
                Toast.makeText(CommandeActivity.this, "Table " + tbSelectionnee + " mise à jour avec " + nbConvives + " convives.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(CommandeActivity.this, "Numéro de table non valide", Toast.LENGTH_SHORT).show();
            }
        }
    };
}