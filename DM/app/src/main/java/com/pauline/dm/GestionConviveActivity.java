package com.pauline.dm;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.pauline.dm.Fragments.FragmentConvive;
import com.pauline.dm.Fragments.FragmentPartage;

import java.util.ArrayList;
import java.util.List;

public class GestionConviveActivity extends AppCompatActivity {

    private EditText etNum;
    private Button boutNbConvive, btnValiderCommande;
    private ProgressBar progressBar;
    private int nbConvives = 0, nbConvivesMax = 15;
    private ViewPager viewPager;
    private TabLayout tabLayout;
    private ViewPagerAdapter adapter;
    private int progressStatus = 0;

    private List<ConviveCommande> conviveCommandes = new ArrayList<>();
    private ConviveCommande commandeTable = null;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_convive);

        etNum = findViewById(R.id.nbConvives);
        boutNbConvive = findViewById(R.id.confirmConvives);
        btnValiderCommande = findViewById(R.id.validerCommande);
        progressBar = findViewById(R.id.progressBar);
        viewPager = findViewById(R.id.viewpagerid);
        tabLayout = findViewById(R.id.tablayoutid);

        adapter = new ViewPagerAdapter(getSupportFragmentManager());

        boutNbConvive.setOnClickListener(v -> ajouterConvives());

        btnValiderCommande.setOnClickListener(v -> validerCommande());
    }

    private void ajouterConvives() {
        try {
            int nouveauNombre = Integer.parseInt(etNum.getText().toString());
            if (nouveauNombre > 0 && nouveauNombre <= nbConvivesMax) {
                nbConvives = nouveauNombre;
                commandeTable = new ConviveCommande();
                adapter.clearFragments();
                setupFragments();
                viewPager.setOffscreenPageLimit(nbConvives+1);
                Toast.makeText(this, nbConvives + " convives ajoutés.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Nombre de convives invalide ou trop élevé.", Toast.LENGTH_SHORT).show();
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Veuillez entrer un nombre valide.", Toast.LENGTH_SHORT).show();
        }
    }

    private void setupFragments() {
        for (int i = 0; i < nbConvives; i++) {
            conviveCommandes.add(new ConviveCommande());
            adapter.addFrag(new FragmentConvive(), "Convive " + (i + 1));
        }
        adapter.addFrag(new FragmentPartage(), "Table");

        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            private int previousPosition = 0;

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                if (previousPosition < conviveCommandes.size()) {
                    FragmentConvive fragmentConvive = (FragmentConvive) adapter.getItem(previousPosition);
                    conviveCommandes.set(previousPosition, fragmentConvive.getConviveCommande());
                }

                previousPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });
    }

    private void validerCommande() {

        sauvegarderConviveActuel();

        if (conviveCommandes.isEmpty() || commandeTable == null) {
            Toast.makeText(this, "Veuillez d'abord confirmer le nombre de convives.", Toast.LENGTH_SHORT).show();
            return;
        }

        for (int i = 0; i < conviveCommandes.size(); i++) {
            ConviveCommande commande = conviveCommandes.get(i);
            Toast.makeText(this, "Convive " + (i + 1) + " : " + commande, Toast.LENGTH_SHORT).show();
        }

        FragmentPartage fragmentPartage = (FragmentPartage) adapter.getItem(adapter.getCount() - 1);
        ConviveCommande commandeTable = fragmentPartage.getConviveCommande();
        Toast.makeText(this, "Commande Table : " + commandeTable, Toast.LENGTH_SHORT).show();

        processusValidation();

        retournerValeursACommandeActivity();
    }

    private void sauvegarderConviveActuel() {
        int positionActuelle = viewPager.getCurrentItem();
        if (positionActuelle < conviveCommandes.size()) {
            FragmentConvive fragmentConvive = (FragmentConvive) adapter.getItem(positionActuelle);
            fragmentConvive.remplirConviveCommandeDepuisUI(); // Force la sauvegarde des données
            conviveCommandes.set(positionActuelle, fragmentConvive.getConviveCommande());
        }
    }

    private void retournerValeursACommandeActivity() {
        try {
            Intent data = new Intent("convives-nombre");
            data.putExtra("nbConvives", nbConvives);
            LocalBroadcastManager.getInstance(GestionConviveActivity.this).sendBroadcast(data);
        } catch (Exception e) {
            Toast.makeText(GestionConviveActivity.this, "Veuillez entrer un nombre valide.", Toast.LENGTH_SHORT).show();
        }
    }

    private void processusValidation() {
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setProgress(0);
        progressBar.setMax(100);
        progressStatus = 0;

        new Thread(() -> {
            while (progressStatus < 100) {
                progressStatus += 10;
                handler.post(() -> progressBar.setProgress(progressStatus));
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            handler.post(() -> {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(GestionConviveActivity.this, "Validation réussie !", Toast.LENGTH_SHORT).show();
            });
        }).start();
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> fragmentList = new ArrayList<>();
        private final List<String> fragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            fragmentList.add(fragment);
            fragmentTitleList.add(title);
        }

        public void clearFragments() {
            fragmentList.clear();
            fragmentTitleList.clear();
            notifyDataSetChanged();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return fragmentTitleList.get(position);
        }
    }
}