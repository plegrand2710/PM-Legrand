package com.pauline.dm;

import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.pauline.dm.GestionBDD.DBAdapter;

public class ManagerUtilisateursActivity extends AppCompatActivity {
    private DBAdapter dbAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_utilisateurs);

        dbAdapter = new DBAdapter(this);
        dbAdapter.open();

        ListView userListView = findViewById(R.id.userListView);

        UtilisateursAdapter userAdapter = new UtilisateursAdapter(this, dbAdapter.getAllUsers(), dbAdapter);
        userListView.setAdapter(userAdapter);

        dbAdapter.close();
    }
}
