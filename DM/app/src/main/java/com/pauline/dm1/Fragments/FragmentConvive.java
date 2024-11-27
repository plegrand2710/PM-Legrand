package com.pauline.dm.Fragments;

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

    private final String[] optionsCuisson = {"Bleu", "Saignant", "À Point", "Bien Cuit"};


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
        commandeLayout.setOrientation(LinearLayout.HORIZONTAL);
        commandeLayout.setLayoutParams(new LinearLayout.LayoutParams(
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

        commandeLayout.addView(choixEditText);
        commandeLayout.addView(quantiteEditText);

        CheckBox cuissonCheckBox = new CheckBox(getContext());
        cuissonCheckBox.setText("Sélectionnez la cuisson");
        cuissonCheckBox.setVisibility(View.GONE);
        commandeLayout.addView(cuissonCheckBox);

        container.addView(commandeLayout);

        //utilisation de ça pour faire apparaitre checkbox
        choixEditText.setOnItemClickListener((parent, view, position, id) -> {
            String choix = choixEditText.getText().toString();
            String quantiteStr = quantiteEditText.getText().toString();
            int quantite = quantiteStr.isEmpty() ? 1 : Integer.parseInt(quantiteStr);

            Toast.makeText(getContext(), type + " ajouté : " + choix + " (" + quantite + ")", Toast.LENGTH_SHORT).show();

            String cuisson = "0";

            // Vérifiez si le choix nécessite une cuisson
            if (type.equals("Plat") && mapPlats.containsKey(choix)) {
                Log.d(TAG, "ajouterCommande: je vérifie plat");
                cuisson = mapPlats.get(choix);
            } else if (type.equals("Accompagnement") && mapAccompagnements.containsKey(choix)) {
                cuisson = mapAccompagnements.get(choix);
            }

            Log.d(TAG, "ajouterCommande: cuisson = " + cuisson);
            // Afficher ou cacher les CheckBox en fonction de la valeur de cuisson
            if (Objects.equals(cuisson, "1")) {
                cuissonCheckBox.setVisibility(View.VISIBLE);
                Toast.makeText(getContext(), "Sélectionnez la cuisson pour : " + choix, Toast.LENGTH_SHORT).show();
            } else {
                cuissonCheckBox.setVisibility(View.GONE);
            }
        });
    }

    public void remplirConviveCommandeDepuisUI() {
        conviveCommande = new ConviveCommande();

        conviveCommande.set_plats(extraireCommandes(containerPlats));
        conviveCommande.set_quantitesPlats(extraireQuantites(containerPlats));

        conviveCommande.set_accompagnements(extraireCommandes(containerAccompagnements));
        conviveCommande.set_quantitesAccompagnements(extraireQuantites(containerAccompagnements));

        conviveCommande.set_boissons(extraireCommandes(containerBoissons));
        conviveCommande.set_quantitesBoissons(extraireQuantites(containerBoissons));

        //afficherRecapitulatif();
    }

    public List<String> extraireCommandes(LinearLayout container) {
        List<String> commandes = new ArrayList<>();
        for (int i = 0; i < container.getChildCount(); i++) {
            View commandeView = container.getChildAt(i);
            if (commandeView instanceof LinearLayout) {
                AutoCompleteTextView choixTextView = (AutoCompleteTextView) ((LinearLayout) commandeView).getChildAt(0);
                commandes.add(choixTextView.getText().toString());
            }
        }
        return commandes;
    }

    public List<Integer> extraireQuantites(LinearLayout container) {
        List<Integer> quantites = new ArrayList<>();
        for (int i = 0; i < container.getChildCount(); i++) {
            View commandeView = container.getChildAt(i);
            if (commandeView instanceof LinearLayout) {
                EditText quantiteEditText = (EditText) ((LinearLayout) commandeView).getChildAt(1);
                quantites.add(Integer.parseInt(quantiteEditText.getText().toString()));
            }
        }
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