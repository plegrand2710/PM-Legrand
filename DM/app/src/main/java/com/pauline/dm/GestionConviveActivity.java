package com.pauline.dm;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class GestionConviveActivity extends AppCompatActivity {

    private EditText etNum ;
    private Button boutNbConvive, bValideCommande ;
    private Integer nbConvives , nbConvivesMax = 15 ;
    private ViewPager vp ;
    private TabLayout tl ;
    private ViewPagerAdapter adapter ;
    private ArrayList<Fragment> fragments ;
    private ProgressBar progressBar;
    private Button btnValiderCommande;
    private Handler handler = new Handler();
    private int progressStatus = 0;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_convive);

        etNum = findViewById(R.id.nbConvives);
        boutNbConvive = findViewById(R.id.confirmConvives);
        tl = findViewById(R.id.tablayoutid);
        vp = findViewById(R.id.viewpagerid);
        btnValiderCommande = (Button) findViewById(R.id.validerCommande);
        progressBar = findViewById(R.id.progressBar);

        listenerConvive();
    }

    public void initialiseFragments() {
        fragments = new ArrayList<>();
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        if (nbConvives > 0) {
            for (int i = 0; i < nbConvives; i++) {
                FragmentConvive fragmentConvive = new FragmentConvive();
                fragments.add(fragmentConvive);
            }
            FragmentPartage fragmentPartage = new FragmentPartage();
            fragments.add(fragmentPartage);

        }
        setupViewPager(vp);
    }

    public void setupViewPager(ViewPager viewPager) {
        adapter.clearFragments();
        for (int i = 0; i < fragments.size() - 1; i++) {
            adapter.addFrag(fragments.get(i), "Convive " + (i + 1));
        }
        adapter.addFrag(fragments.get(fragments.size()-1), "Table");
        viewPager.setAdapter(adapter);
        tl.setupWithViewPager(viewPager);
    }

    private void processusValidation() {
        progressBar.setVisibility(View.VISIBLE);
        progressBar.setProgress(0);
        progressBar.setMax(100);
        progressStatus = 0;

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (progressStatus < 100) {
                    progressStatus += doSomeWork();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setProgress(progressStatus);
                        }
                    });
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(GestionConviveActivity.this, "Commande validée avec succès", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            private int doSomeWork() {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                return 10;
            }
        }).start();
    }

    private void listenerConvive() {
        boutNbConvive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    int nombre = Integer.parseInt(etNum.getText().toString());
                    if (nombre > 0 && nombre <= nbConvivesMax) {
                        nbConvives = nombre;
                        initialiseFragments();
                    } else {
                        Toast.makeText(getApplicationContext(), "Nombre de convives trop important.", Toast.LENGTH_SHORT).show();
                    }
                } catch (NumberFormatException e) {
                    Toast.makeText(getApplicationContext(), "Erreur : Veuillez entrer un nombre valide.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnValiderCommande.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processusValidation();
                try {
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("nbConvives", nbConvives);

                    setResult(RESULT_OK, resultIntent);
                    //finish();
                } catch (NumberFormatException e) {
                    Toast.makeText(GestionConviveActivity.this, "Veuillez entrer un nombre valide.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> fragmentList = new ArrayList<>();
        private final List<String> fragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
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
