package com.pauline.dm.Commande;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.TextViewCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.pauline.dm.Admin.Commande;
import com.pauline.dm.Admin.Contient;
import com.pauline.dm.Commande.Fragments.FragmentConvive;
import com.pauline.dm.Commande.Fragments.FragmentPartage;
import com.pauline.dm.GestionBDD.DBAdapter;
import com.pauline.dm.R;

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
    private static String TAG = "DMProjet";
    private List<ConviveCommande> conviveCommandes = new ArrayList<>();
    private ConviveCommande commandeTable = null;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_convive);

        etNum = findViewById(R.id.nbConvives);
        boutNbConvive = findViewById(R.id.confirmConvivesButton);
        btnValiderCommande = findViewById(R.id.validerCommande);
        progressBar = findViewById(R.id.progressBar);
        viewPager = findViewById(R.id.viewpagerid);
        tabLayout = findViewById(R.id.tablayoutid);

        adapter = new ViewPagerAdapter(getSupportFragmentManager());

        if (getIntent().hasExtra("nbConvives")) {
            nbConvives = getIntent().getIntExtra("nbConvives", 0);
            etNum.setText(String.valueOf(nbConvives));
            setupFragments();
        }


        boutNbConvive.setOnClickListener(v -> ajouterConvives());

        btnValiderCommande.setOnClickListener(v -> validerCommande());
    }


    private void ajouterConvives() {
        try {
            int nouveauNombre = Integer.parseInt(etNum.getText().toString());
            if (nouveauNombre > 0 && nouveauNombre <= nbConvivesMax) {
                nbConvives = nouveauNombre;

                commandeTable = new ConviveCommande();
                conviveCommandes.clear();
                adapter.clearFragments();

                setupFragments();
                viewPager.setOffscreenPageLimit(nbConvives + 1);

                LinearLayout boutonContainer = findViewById(R.id.bouton_container);
                remplacerBouton(boutonContainer, "Mettre à jour le nombre de convives", v -> ajouterConvives());

                Toast.makeText(this, nbConvives + " convives ajoutés.", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Nombre de convives invalide ou trop élevé.", Toast.LENGTH_SHORT).show();
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Veuillez entrer un nombre valide.", Toast.LENGTH_SHORT).show();
        }
    }

    private void remplacerBouton(LinearLayout container, String boutonTexte, View.OnClickListener action) {
        if (container == null) {
            Log.e(TAG, "remplacerBouton: Conteneur introuvable.");
            return;
        }

        container.removeAllViews();

        ContextThemeWrapper contextThemeWrapper = new ContextThemeWrapper(this, R.style.ButtonStyle);
        Button nouveauBouton = new Button(contextThemeWrapper);
        nouveauBouton.setBackgroundResource(R.drawable.button);
        TextViewCompat.setTextAppearance(nouveauBouton, R.style.ButtonStyle);
        nouveauBouton.setId(View.generateViewId());
        nouveauBouton.setText(boutonTexte);
        int heightInDp = 40;
        float scale = getResources().getDisplayMetrics().density;

        int heightInPx = (int) (heightInDp * scale + 0.5f);
        nouveauBouton.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                heightInPx
        ));

        nouveauBouton.setOnClickListener(action);

        container.addView(nouveauBouton);

        container.setVisibility(View.VISIBLE);

        Log.d(TAG, "remplacerBouton: Bouton '" + boutonTexte + "' ajouté.");
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

        Fragment lastFragment = adapter.getItem(adapter.getCount() - 1);

        if (!(lastFragment instanceof FragmentPartage)) {
            return;
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
            fragmentConvive.remplirConviveCommandeDepuisUI();
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

    class ViewPagerAdapter extends FragmentStatePagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
            Log.d(TAG, "addFrag: Ajouté fragment avec titre : " + title + ", Fragment : " + fragment);
            notifyDataSetChanged();
        }

        public void clearFragments() {
            mFragmentList.clear();
            mFragmentTitleList.clear();
            Log.d(TAG, "clearFragments: Tous les fragments supprimés.");
            notifyDataSetChanged();
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        public void refreshFragments(List<Fragment> newFragmentList) {
            mFragmentList.clear();
            mFragmentList.addAll(newFragmentList);
            notifyDataSetChanged();

            Log.d(TAG, "refreshFragments: Contenu de l'adaptateur après rafraîchissement.");
            for (int i = 0; i < mFragmentList.size(); i++) {
                Fragment fragment = mFragmentList.get(i);
                Log.d(TAG, "Fragment " + i + ": " + fragment.toString());
            }
        }
    }
}