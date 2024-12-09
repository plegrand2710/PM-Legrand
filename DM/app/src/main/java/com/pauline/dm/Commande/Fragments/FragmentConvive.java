package com.pauline.dm.Commande.Fragments;

import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.core.widget.TextViewCompat;

import com.pauline.dm.Commande.ConviveCommande;
import com.pauline.dm.Commande.ProduitCommande;
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

        TextViewCompat.setTextAppearance(ajouterBoissonButton, R.style.ButtonStyle);
        ajouterPlatButton.setOnClickListener(v -> ajouterCommande(containerPlats, "Plat", adapterPlats));
        ajouterAccompagnementButton.setOnClickListener(v -> ajouterCommande(containerAccompagnements, "Accompagnement", adapterAccompagnements));
        ajouterBoissonButton.setOnClickListener(v -> ajouterCommande(containerBoissons, "Boisson", adapterBoissons));

        restaurerEtatConviveCommande();

        return view;
    }

    public void restaurerEtatConviveCommande() {
        if (conviveCommande == null) return;

        restaurerCommandes(containerPlats, "Plat", conviveCommande.getPlats(), adapterPlats);
        restaurerCommandes(containerAccompagnements, "Accompagnement", conviveCommande.getAccompagnements(), adapterAccompagnements);
        restaurerCommandes(containerBoissons, "Boisson", conviveCommande.getBoissons(), adapterBoissons);
    }

    public void restaurerCommandes(LinearLayout container, String type, List<ProduitCommande> choixList, ArrayAdapter<String> adapter) {
        for (ProduitCommande produit : choixList) {
            String nom = produit.getNom();
            int quantite = produit.getQuantite();
            String cuisson = produit.getCuisson();

            ajouterCommandeDepuisEtat(container, type, nom, quantite, cuisson, adapter);
        }
    }

    public void ajouterCommandeDepuisEtat(LinearLayout container, String type, String choix, int quantite, String cuisson, ArrayAdapter<String> adapter) {
        ajouterCommande(container, type, adapter, choix, quantite, cuisson);
    }

    public void ajouterCommande(LinearLayout container, String type, ArrayAdapter<String> adapter) {
        String cuissonParDefaut = null;

        if ("Plat".equalsIgnoreCase(type) || "Accompagnement".equalsIgnoreCase(type)) {
            cuissonParDefaut = "";
        }

        ajouterCommande(container, type, adapter, "", 1, cuissonParDefaut);
    }

    public void ajouterCommande(LinearLayout container, String type, ArrayAdapter<String> adapter, String choixInitial, int quantiteInitial, String cuissonInitiale) {
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

        quantiteEditText.setFilters(new InputFilter[]{
                new InputFilter.LengthFilter(2),
                (source, start, end, dest, dstart, dend) -> {
                    try {
                        String newValue = dest.toString().substring(0, dstart) + source + dest.toString().substring(dend);
                        int input = Integer.parseInt(newValue);
                        if (input > 20) {
                            Toast.makeText(getContext(), "Quantité maximale: 20", Toast.LENGTH_SHORT).show();
                            return "";
                        }
                    } catch (NumberFormatException e) {
                    }
                    return null;
                }
        });

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

        ProduitCommande produitCommande = new ProduitCommande(choixInitial, quantiteInitial, cuissonInitiale);

        if (cuissonInitiale != null && !cuissonInitiale.isEmpty()) {
            cuissonLayout.setVisibility(View.VISIBLE);
            afficherOptionsCuisson(cuissonLayout, produitCommande);
        }

        choixEditText.setOnItemClickListener((parent, view, position, id) -> {
            String choix = choixEditText.getText().toString();
            String cuissonOption = mapPlats.getOrDefault(choix, "0");

            if (Objects.equals(cuissonOption, "1")) {
                cuissonLayout.setVisibility(View.VISIBLE);
                afficherOptionsCuisson(cuissonLayout, produitCommande);
            } else {
                cuissonLayout.setVisibility(View.GONE);
                produitCommande.setCuisson(null);
            }
        });
    }


    public void remplirConviveCommandeDepuisUI() {
        try {
            conviveCommande.setPlats(extraireCommandes(containerPlats));
            conviveCommande.setAccompagnements(extraireCommandes(containerAccompagnements));
            conviveCommande.setBoissons(extraireCommandes(containerBoissons));
        } catch (Exception e) {
            Log.e(TAG, "Erreur lors du remplissage de la commande depuis l'interface utilisateur", e);
            Toast.makeText(getContext(), "Erreur lors de l'extraction des données, veuillez réessayer.", Toast.LENGTH_LONG).show();
        }
    }


    public List<ProduitCommande> extraireCommandes(LinearLayout container) {
        List<ProduitCommande> commandes = new ArrayList<>();
        for (int i = 0; i < container.getChildCount(); i++) {
            View commandeView = container.getChildAt(i);
            if (commandeView instanceof LinearLayout) {
                LinearLayout inputLayout = (LinearLayout) ((LinearLayout) commandeView).getChildAt(0);
                LinearLayout cuissonLayout = (LinearLayout) ((LinearLayout) commandeView).getChildAt(1);

                if (inputLayout != null) {
                    try {
                        AutoCompleteTextView choixTextView = (AutoCompleteTextView) inputLayout.getChildAt(0);
                        String nom = choixTextView.getText().toString();

                        EditText quantiteEditText = (EditText) inputLayout.getChildAt(1);
                        int quantite = quantiteEditText.getText().toString().isEmpty()
                                ? 1
                                : Integer.parseInt(quantiteEditText.getText().toString());

                        if (quantite > 20) {
                            quantite = 20;
                            quantiteEditText.setText(String.valueOf(quantite));
                            Toast.makeText(getContext(), "Quantité limitée à 20.", Toast.LENGTH_SHORT).show();
                        }

                        String cuisson = null;
                        if (cuissonLayout.getVisibility() == View.VISIBLE) {
                            Log.d(TAG, "extraireCommandes: je recuppère les checkbox");
                            RadioGroup radioGroup = (RadioGroup) cuissonLayout.getChildAt(0);
                            int checkedId = radioGroup.getCheckedRadioButtonId();
                            if (checkedId != -1) {
                                RadioButton selectedRadioButton = cuissonLayout.findViewById(checkedId);
                                cuisson = selectedRadioButton.getText().toString();
                            }
                        }
                        if (!nom.isEmpty()) {
                            commandes.add(new ProduitCommande(nom, quantite, cuisson));
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
