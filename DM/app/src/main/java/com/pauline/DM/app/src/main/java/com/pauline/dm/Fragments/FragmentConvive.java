package com.pauline.dm.Fragments;
/*
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;


import com.pauline.dm.ConviveCommande;
import com.pauline.dm.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FragmentConvive extends Fragments {
    private View view;
    private static final int MAX_CHOIX = 25;
    private static final String TAG = "DMProjet";

    private ArrayAdapter<String> adapterPlats;
    private ArrayAdapter<String> adapterAccompagnements;
    private ArrayAdapter<String> adapterBoissons;

    private Button ajouterPlatButton;
    private Button ajouterAccompagnementButton;
    private Button ajouterBoissonButton;

    private LinearLayout containerPlats;
    private LinearLayout containerAccompagnements;
    private LinearLayout containerBoissons;

    private ConviveCommande conviveCommande;

    public static FragmentConvive newInstance(ConviveCommande commande) {
        FragmentConvive fragment = new FragmentConvive();
        Bundle args = new Bundle();
        args.putSerializable("commande", commande);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        remplirConviveCommandeDepuisUI();
        outState.putSerializable("conviveCommande", conviveCommande);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            conviveCommande = (ConviveCommande) savedInstanceState.getSerializable("conviveCommande");
        } else {
            conviveCommande = new ConviveCommande();
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_convive, container, false);

        containerPlats = view.findViewById(R.id.containerPlats);
        containerAccompagnements = view.findViewById(R.id.containerAccompagnements);
        containerBoissons = view.findViewById(R.id.containerBoissons);

        adapterPlats = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, listePlats);
        adapterAccompagnements = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, listeAccompagnements);
        adapterBoissons = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, listeBoissons);

        ajouterPlatButton = view.findViewById(R.id.ajouterPlatButton);
        ajouterAccompagnementButton = view.findViewById(R.id.ajouterAccompagnementButton);
        ajouterBoissonButton = view.findViewById(R.id.ajouterBoissonButton);

        ajouterPlatButton.setOnClickListener(v -> ajouterCommande(containerPlats, "Plat", adapterPlats));
        ajouterAccompagnementButton.setOnClickListener(v -> ajouterCommande(containerAccompagnements, "Accompagnement", adapterAccompagnements));
        ajouterBoissonButton.setOnClickListener(v -> ajouterCommande(containerBoissons, "Boisson", adapterBoissons));

        restaurerEtatConviveCommande();



        return view;
    }

    public void restaurerEtatConviveCommande() {
        if (conviveCommande == null) return;

        restaurerCommandes(containerPlats, "Plat", conviveCommande.get_plats(), conviveCommande.get_quantitesPlats(), adapterPlats);
        restaurerCommandes(containerAccompagnements, "Accompagnement", conviveCommande.get_accompagnements(), conviveCommande.get_quantitesAccompagnements(), adapterAccompagnements);
        restaurerCommandes(containerBoissons, "Boisson", conviveCommande.get_boissons(), conviveCommande.get_quantitesBoissons(), adapterBoissons);
    }

    public void restaurerCommandes(LinearLayout container, String type, List<String> choixList, List<Integer> quantitesList, ArrayAdapter<String> adapter) {
        for (int i = 0; i < choixList.size(); i++) {
            ajouterCommandeDepuisEtat(container, type, choixList.get(i), quantitesList.get(i), adapter);
        }
    }

    public void ajouterCommandeDepuisEtat(LinearLayout container, String type, String choix, int quantite, ArrayAdapter<String> adapter) {
        ajouterCommande(container, type, adapter, choix, quantite);
    }

    public void ajouterCommande(LinearLayout container, String type, ArrayAdapter<String> adapter) {
        ajouterCommande(container, type, adapter, "", 1);
    }

    public void ajouterCommande(LinearLayout container, String type, ArrayAdapter<String> adapter, String choixInitial, int quantiteInitial) {
        if (container.getChildCount() >= MAX_CHOIX) {
            Toast.makeText(getContext(), "Vous ne pouvez ajouter que " + MAX_CHOIX + " " + type + "s.", Toast.LENGTH_SHORT).show();
            return;
        }

        LinearLayout commandeLayout = new LinearLayout(getContext());
        commandeLayout.setOrientation(LinearLayout.VERTICAL);
        commandeLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));

        LinearLayout inputLayout = new LinearLayout(getContext());
        inputLayout.setOrientation(LinearLayout.HORIZONTAL);
        inputLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));

        AutoCompleteTextView choixEditText = new AutoCompleteTextView(getContext());
        choixEditText.setLayoutParams(new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1.0f
        ));
        choixEditText.setHint(type.toLowerCase());
        choixEditText.setAdapter(adapter);
        choixEditText.setThreshold(1);
        choixEditText.setText(choixInitial);

        EditText quantiteEditText = new EditText(getContext());
        quantiteEditText.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        quantiteEditText.setHint("Quantité");
        quantiteEditText.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);
        quantiteEditText.setText(String.valueOf(quantiteInitial));

        inputLayout.addView(choixEditText);
        inputLayout.addView(quantiteEditText);

        LinearLayout cuissonLayout = new LinearLayout(getContext());
        cuissonLayout.setOrientation(LinearLayout.VERTICAL);
        cuissonLayout.setVisibility(View.GONE);
        cuissonLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));

        commandeLayout.addView(inputLayout);
        commandeLayout.addView(cuissonLayout);

        container.addView(commandeLayout);

        choixEditText.setOnItemClickListener((parent, view, position, id) -> {
            String choix = choixEditText.getText().toString();
            String cuisson = mapPlats.getOrDefault(choix, "0");

            if (Objects.equals(cuisson, "1")) {
                cuissonLayout.setVisibility(View.VISIBLE);
                afficherOptionsCuisson(cuissonLayout, conviveCommande);
            } else {
                cuissonLayout.setVisibility(View.GONE);
            }
        });
    }


    public void remplirConviveCommandeDepuisUI() {
        try {
            conviveCommande.set_plats(extraireCommandes(containerPlats));
            conviveCommande.set_quantitesPlats(extraireQuantites(containerPlats));
            conviveCommande.set_accompagnements(extraireCommandes(containerAccompagnements));
            conviveCommande.set_quantitesAccompagnements(extraireQuantites(containerAccompagnements));
            conviveCommande.set_boissons(extraireCommandes(containerBoissons));
            conviveCommande.set_quantitesBoissons(extraireQuantites(containerBoissons));
        } catch (Exception e) {
            Log.e(TAG, "Erreur lors du remplissage de la commande depuis l'interface utilisateur", e);
            Toast.makeText(getContext(), "Erreur lors de l'extraction des données, veuillez réessayer.", Toast.LENGTH_LONG).show();
        }
    }


    public List<String> extraireCommandes(LinearLayout container) {
        List<String> commandes = new ArrayList<>();
        for (int i = 0; i < container.getChildCount(); i++) {
            View commandeView = container.getChildAt(i);
            if (commandeView instanceof LinearLayout) {
                LinearLayout inputLayout = (LinearLayout) ((LinearLayout) commandeView).getChildAt(0); // Récupérer le layout d'entrée
                if (inputLayout != null) {
                    try {
                        AutoCompleteTextView choixTextView = (AutoCompleteTextView) inputLayout.getChildAt(0);
                        String commande = choixTextView.getText().toString();
                        if (!commande.isEmpty()) {
                            commandes.add(commande);
                        } else {
                            Log.d(TAG, "Commande vide ignorée à l'index " + i);
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "Erreur lors de l'extraction de la commande à l'index " + i, e);
                    }
                }
            }
        }
        Log.d(TAG, "Commandes extraites : " + commandes);
        return commandes;
    }

    public List<Integer> extraireQuantites(LinearLayout container) {
        List<Integer> quantites = new ArrayList<>();
        for (int i = 0; i < container.getChildCount(); i++) {
            View commandeView = container.getChildAt(i);
            if (commandeView instanceof LinearLayout) {
                LinearLayout inputLayout = (LinearLayout) ((LinearLayout) commandeView).getChildAt(0); // Récupérer le layout d'entrée
                if (inputLayout != null) {
                    try {
                        EditText quantiteEditText = (EditText) inputLayout.getChildAt(1);
                        String quantiteStr = quantiteEditText.getText().toString();
                        int quantite = quantiteStr.isEmpty() ? 1 : Integer.parseInt(quantiteStr);
                        quantites.add(quantite);
                    } catch (NumberFormatException e) {
                        Log.e(TAG, "Quantité non valide à l'index " + i, e);
                        quantites.add(1); // Ajouter une quantité par défaut en cas d'erreur
                    } catch (Exception e) {
                        Log.e(TAG, "Erreur lors de l'extraction de la quantité à l'index " + i, e);
                    }
                }
            }
        }
        Log.d(TAG, "Quantités extraites : " + quantites);
        return quantites;
    }

    /*private void afficherRecapitulatif() {
        StringBuilder recap = new StringBuilder("Récapitulatif des commandes :\n");

        recap.append("Plats : ").append(conviveCommande.get_plats()).append("\n");
        recap.append("Accompagnements : ").append(conviveCommande.get_accompagnements()).append("\n");
        recap.append("Boissons : ").append(conviveCommande.get_boissons()).append("\n");

        Toast.makeText(getContext(), recap.toString(), Toast.LENGTH_LONG).show();
    }

    public ConviveCommande getConviveCommande() {
        remplirConviveCommandeDepuisUI();
        return conviveCommande;
    }

    public View getViewFragment() {
        return view;
    }

    public void setViewFragment(View view) {
        this.view = view;
    }

    public ArrayAdapter<String> getAdapterPlats() {
        return adapterPlats;
    }

    public void setAdapterPlats(ArrayAdapter<String> adapterPlats) {
        this.adapterPlats = adapterPlats;
    }

    public ArrayAdapter<String> getAdapterAccompagnements() {
        return adapterAccompagnements;
    }

    public void setAdapterAccompagnements(ArrayAdapter<String> adapterAccompagnements) {
        this.adapterAccompagnements = adapterAccompagnements;
    }

    public ArrayAdapter<String> getAdapterBoissons() {
        return adapterBoissons;
    }

    public void setAdapterBoissons(ArrayAdapter<String> adapterBoissons) {
        this.adapterBoissons = adapterBoissons;
    }

    public Button getAjouterPlatButton() {
        return ajouterPlatButton;
    }

    public void setAjouterPlatButton(Button ajouterPlatButton) {
        this.ajouterPlatButton = ajouterPlatButton;
    }

    public Button getAjouterAccompagnementButton() {
        return ajouterAccompagnementButton;
    }

    public void setAjouterAccompagnementButton(Button ajouterAccompagnementButton) {
        this.ajouterAccompagnementButton = ajouterAccompagnementButton;
    }

    public Button getAjouterBoissonButton() {
        return ajouterBoissonButton;
    }

    public void setAjouterBoissonButton(Button ajouterBoissonButton) {
        this.ajouterBoissonButton = ajouterBoissonButton;
    }

    public LinearLayout getContainerPlats() {
        return containerPlats;
    }

    public void setContainerPlats(LinearLayout containerPlats) {
        this.containerPlats = containerPlats;
    }

    public LinearLayout getContainerAccompagnements() {
        return containerAccompagnements;
    }

    public void setContainerAccompagnements(LinearLayout containerAccompagnements) {
        this.containerAccompagnements = containerAccompagnements;
    }

    public LinearLayout getContainerBoissons() {
        return containerBoissons;
    }

    public void setContainerBoissons(LinearLayout containerBoissons) {
        this.containerBoissons = containerBoissons;
    }

    public void setConviveCommande(ConviveCommande conviveCommande) {
        this.conviveCommande = conviveCommande;
    }
}*/

        import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.pauline.dm.ConviveCommande;
import com.pauline.dm.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

    public class FragmentConvive extends Fragments {
        private View view;
        private static final int MAX_CHOIX = 25;
        private static final String TAG = "DMProjet";

        private ArrayAdapter<String> adapterPlats;
        private ArrayAdapter<String> adapterAccompagnements;
        private ArrayAdapter<String> adapterBoissons;

        private Button ajouterPlatButton;
        private Button ajouterAccompagnementButton;
        private Button ajouterBoissonButton;

        private LinearLayout containerPlats;
        private LinearLayout containerAccompagnements;
        private LinearLayout containerBoissons;

        private ConviveCommande conviveCommande;

        public static FragmentConvive newInstance(ConviveCommande commande) {
            FragmentConvive fragment = new FragmentConvive();
            Bundle args = new Bundle();
            args.putSerializable("commande", commande);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public void onSaveInstanceState(Bundle outState) {
            super.onSaveInstanceState(outState);

            remplirConviveCommandeDepuisUI();
            outState.putSerializable("conviveCommande", conviveCommande);
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            if (savedInstanceState != null) {
                conviveCommande = (ConviveCommande) savedInstanceState.getSerializable("conviveCommande");
            } else {
                conviveCommande = new ConviveCommande();
            }
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            view = inflater.inflate(R.layout.fragment_convive, container, false);

            containerPlats = view.findViewById(R.id.containerPlats);
            containerAccompagnements = view.findViewById(R.id.containerAccompagnements);
            containerBoissons = view.findViewById(R.id.containerBoissons);

            adapterPlats = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, listePlats);
            adapterAccompagnements = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, listeAccompagnements);
            adapterBoissons = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, listeBoissons);

            ajouterPlatButton = view.findViewById(R.id.ajouterPlatButton);
            ajouterAccompagnementButton = view.findViewById(R.id.ajouterAccompagnementButton);
            ajouterBoissonButton = view.findViewById(R.id.ajouterBoissonButton);

            ajouterPlatButton.setOnClickListener(v -> ajouterCommande(containerPlats, "Plat", adapterPlats));
            ajouterAccompagnementButton.setOnClickListener(v -> ajouterCommande(containerAccompagnements, "Accompagnement", adapterAccompagnements));
            ajouterBoissonButton.setOnClickListener(v -> ajouterCommande(containerBoissons, "Boisson", adapterBoissons));

            restaurerEtatConviveCommande();

            return view;
        }

        public void restaurerEtatConviveCommande() {
            if (conviveCommande == null) return;

            restaurerCommandes(containerPlats, "Plat", conviveCommande.get_plats(), conviveCommande.get_quantitesPlats(), adapterPlats);
            restaurerCommandes(containerAccompagnements, "Accompagnement", conviveCommande.get_accompagnements(), conviveCommande.get_quantitesAccompagnements(), adapterAccompagnements);
            restaurerCommandes(containerBoissons, "Boisson", conviveCommande.get_boissons(), conviveCommande.get_quantitesBoissons(), adapterBoissons);
        }

        public void restaurerCommandes(LinearLayout container, String type, List<String> choixList, List<Integer> quantitesList, ArrayAdapter<String> adapter) {
            for (int i = 0; i < choixList.size(); i++) {
                ajouterCommandeDepuisEtat(container, type, choixList.get(i), quantitesList.get(i), adapter);
            }
        }

        public void ajouterCommandeDepuisEtat(LinearLayout container, String type, String choix, int quantite, ArrayAdapter<String> adapter) {
            ajouterCommande(container, type, adapter, choix, quantite);
        }

        public void ajouterCommande(LinearLayout container, String type, ArrayAdapter<String> adapter) {
            ajouterCommande(container, type, adapter, "", 1);
        }

        public void ajouterCommande(LinearLayout container, String type, ArrayAdapter<String> adapter, String choixInitial, int quantiteInitial) {
            if (container.getChildCount() >= MAX_CHOIX) {
                Toast.makeText(getContext(), "Vous ne pouvez ajouter que " + MAX_CHOIX + " " + type + "s.", Toast.LENGTH_SHORT).show();
                return;
            }

            LinearLayout commandeLayout = new LinearLayout(getContext());
            commandeLayout.setOrientation(LinearLayout.VERTICAL);
            commandeLayout.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));

            LinearLayout inputLayout = new LinearLayout(getContext());
            inputLayout.setOrientation(LinearLayout.HORIZONTAL);
            inputLayout.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));

            AutoCompleteTextView choixEditText = new AutoCompleteTextView(getContext());
            choixEditText.setLayoutParams(new LinearLayout.LayoutParams(
                    0,
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    1.0f
            ));
            choixEditText.setHint(type.toLowerCase());
            choixEditText.setAdapter(adapter);
            choixEditText.setThreshold(1);
            choixEditText.setText(choixInitial);

            EditText quantiteEditText = new EditText(getContext());
            quantiteEditText.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            quantiteEditText.setHint("Quantité");
            quantiteEditText.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);
            quantiteEditText.setText(String.valueOf(quantiteInitial));

            inputLayout.addView(choixEditText);
            inputLayout.addView(quantiteEditText);

            LinearLayout cuissonLayout = new LinearLayout(getContext());
            cuissonLayout.setOrientation(LinearLayout.VERTICAL);
            cuissonLayout.setVisibility(View.GONE);
            cuissonLayout.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));

            commandeLayout.addView(inputLayout);
            commandeLayout.addView(cuissonLayout);

            container.addView(commandeLayout);

            // Récupérer la cuisson enregistrée
            String cuissonEnregistree = null;
            int indexPlat = conviveCommande.get_plats().indexOf(choixInitial);
            if (indexPlat >= 0 && indexPlat < conviveCommande.getCuissonsPlats().size()) {
                cuissonEnregistree = conviveCommande.getCuissonsPlats().get(indexPlat);
            }

            // Vérifier si la cuisson est nécessaire
            String cuisson = mapPlats.getOrDefault(choixInitial, "0"); // "1" pour les plats nécessitant une cuisson

            if (Objects.equals(cuisson, "1")) {
                cuissonLayout.setVisibility(View.VISIBLE);
                afficherOptionsCuisson(cuissonLayout, conviveCommande, cuissonEnregistree);
            }

            choixEditText.setOnItemClickListener((parent, view, position, id) -> {
                String choix = choixEditText.getText().toString();
                String cuissonOption = mapPlats.getOrDefault(choix, "0");

                if (Objects.equals(cuissonOption, "1")) {
                    cuissonLayout.setVisibility(View.VISIBLE);
                    afficherOptionsCuisson(cuissonLayout, conviveCommande, null);
                } else {
                    cuissonLayout.setVisibility(View.GONE);
                }
            });
            /*
            choixEditText.setOnItemClickListener((parent, view, position, id) -> {
                String choix = choixEditText.getText().toString();
                String cuisson = mapPlats.getOrDefault(choix, "0");

                if (Objects.equals(cuisson, "1")) {
                    cuissonLayout.setVisibility(View.VISIBLE);
                    afficherOptionsCuisson(cuissonLayout, conviveCommande);
                } else {
                    cuissonLayout.setVisibility(View.GONE);
                }
            });*/
        }


        public void remplirConviveCommandeDepuisUI() {
            try {
                conviveCommande.set_plats(extraireCommandes(containerPlats));
                conviveCommande.set_quantitesPlats(extraireQuantites(containerPlats));
                conviveCommande.set_accompagnements(extraireCommandes(containerAccompagnements));
                conviveCommande.set_quantitesAccompagnements(extraireQuantites(containerAccompagnements));
                conviveCommande.set_boissons(extraireCommandes(containerBoissons));
                conviveCommande.set_quantitesBoissons(extraireQuantites(containerBoissons));
            } catch (Exception e) {
                Log.e(TAG, "Erreur lors du remplissage de la commande depuis l'interface utilisateur", e);
                Toast.makeText(getContext(), "Erreur lors de l'extraction des données, veuillez réessayer.", Toast.LENGTH_LONG).show();
            }
        }


        public List<String> extraireCommandes(LinearLayout container) {
            List<String> commandes = new ArrayList<>();
            for (int i = 0; i < container.getChildCount(); i++) {
                View commandeView = container.getChildAt(i);
                if (commandeView instanceof LinearLayout) {
                    LinearLayout inputLayout = (LinearLayout) ((LinearLayout) commandeView).getChildAt(0); // Récupérer le layout d'entrée
                    if (inputLayout != null) {
                        try {
                            AutoCompleteTextView choixTextView = (AutoCompleteTextView) inputLayout.getChildAt(0);
                            String commande = choixTextView.getText().toString();
                            if (!commande.isEmpty()) {
                                commandes.add(commande);
                            } else {
                                Log.d(TAG, "Commande vide ignorée à l'index " + i);
                            }
                        } catch (Exception e) {
                            Log.e(TAG, "Erreur lors de l'extraction de la commande à l'index " + i, e);
                        }
                    }
                }
            }
            Log.d(TAG, "Commandes extraites : " + commandes);
            return commandes;
        }

        public List<Integer> extraireQuantites(LinearLayout container) {
            List<Integer> quantites = new ArrayList<>();
            for (int i = 0; i < container.getChildCount(); i++) {
                View commandeView = container.getChildAt(i);
                if (commandeView instanceof LinearLayout) {
                    LinearLayout inputLayout = (LinearLayout) ((LinearLayout) commandeView).getChildAt(0); // Récupérer le layout d'entrée
                    if (inputLayout != null) {
                        try {
                            EditText quantiteEditText = (EditText) inputLayout.getChildAt(1);
                            String quantiteStr = quantiteEditText.getText().toString();
                            int quantite = quantiteStr.isEmpty() ? 1 : Integer.parseInt(quantiteStr);
                            quantites.add(quantite);
                        } catch (NumberFormatException e) {
                            Log.e(TAG, "Quantité non valide à l'index " + i, e);
                            quantites.add(1); // Ajouter une quantité par défaut en cas d'erreur
                        } catch (Exception e) {
                            Log.e(TAG, "Erreur lors de l'extraction de la quantité à l'index " + i, e);
                        }
                    }
                }
            }
            Log.d(TAG, "Quantités extraites : " + quantites);
            return quantites;
        }

    /*private void afficherRecapitulatif() {
        StringBuilder recap = new StringBuilder("Récapitulatif des commandes :\n");

        recap.append("Plats : ").append(conviveCommande.get_plats()).append("\n");
        recap.append("Accompagnements : ").append(conviveCommande.get_accompagnements()).append("\n");
        recap.append("Boissons : ").append(conviveCommande.get_boissons()).append("\n");

        Toast.makeText(getContext(), recap.toString(), Toast.LENGTH_LONG).show();
    }*/

        public ConviveCommande getConviveCommande() {
            remplirConviveCommandeDepuisUI();
            return conviveCommande;
        }

        public View getViewFragment() {
            return view;
        }

        public void setViewFragment(View view) {
            this.view = view;
        }

        public ArrayAdapter<String> getAdapterPlats() {
            return adapterPlats;
        }

        public void setAdapterPlats(ArrayAdapter<String> adapterPlats) {
            this.adapterPlats = adapterPlats;
        }

        public ArrayAdapter<String> getAdapterAccompagnements() {
            return adapterAccompagnements;
        }

        public void setAdapterAccompagnements(ArrayAdapter<String> adapterAccompagnements) {
            this.adapterAccompagnements = adapterAccompagnements;
        }

        public ArrayAdapter<String> getAdapterBoissons() {
            return adapterBoissons;
        }

        public void setAdapterBoissons(ArrayAdapter<String> adapterBoissons) {
            this.adapterBoissons = adapterBoissons;
        }

        public Button getAjouterPlatButton() {
            return ajouterPlatButton;
        }

        public void setAjouterPlatButton(Button ajouterPlatButton) {
            this.ajouterPlatButton = ajouterPlatButton;
        }

        public Button getAjouterAccompagnementButton() {
            return ajouterAccompagnementButton;
        }

        public void setAjouterAccompagnementButton(Button ajouterAccompagnementButton) {
            this.ajouterAccompagnementButton = ajouterAccompagnementButton;
        }

        public Button getAjouterBoissonButton() {
            return ajouterBoissonButton;
        }

        public void setAjouterBoissonButton(Button ajouterBoissonButton) {
            this.ajouterBoissonButton = ajouterBoissonButton;
        }

        public LinearLayout getContainerPlats() {
            return containerPlats;
        }

        public void setContainerPlats(LinearLayout containerPlats) {
            this.containerPlats = containerPlats;
        }

        public LinearLayout getContainerAccompagnements() {
            return containerAccompagnements;
        }

        public void setContainerAccompagnements(LinearLayout containerAccompagnements) {
            this.containerAccompagnements = containerAccompagnements;
        }

        public LinearLayout getContainerBoissons() {
            return containerBoissons;
        }

        public void setContainerBoissons(LinearLayout containerBoissons) {
            this.containerBoissons = containerBoissons;
        }

        public void setConviveCommande(ConviveCommande conviveCommande) {
            this.conviveCommande = conviveCommande;
        }
    }
