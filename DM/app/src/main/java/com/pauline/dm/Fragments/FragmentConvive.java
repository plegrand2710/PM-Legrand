package com.pauline.dm.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import com.pauline.dm.ConviveCommande;
import com.pauline.dm.R;

import java.util.ArrayList;
import java.util.List;

public class FragmentConvive extends Fragments {
    private View view;
    private static final int MAX_CHOIX = 25;

    private ArrayAdapter<String> adapterPlats;
    private ArrayAdapter<String> adapterAccompagnements;
    private ArrayAdapter<String> adapterBoissons;

    private Button ajouterPlatButton;
    private Button ajouterAccompagnementButton;
    private Button ajouterBoissonButton;

    private LinearLayout contient;

    private List<ConviveCommande> commandes = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_convive, container, false);
        contient = view.findViewById(R.id.container);

        adapterPlats = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, listePlats);
        AutoCompleteTextView platEditText = view.findViewById(R.id.platEditText);
        platEditText.setThreshold(1);
        platEditText.setAdapter(adapterPlats);

        adapterAccompagnements = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, listeAccompagnements);
        AutoCompleteTextView accompagnementEditText = view.findViewById(R.id.accompagnementEditText);
        accompagnementEditText.setThreshold(1);
        accompagnementEditText.setAdapter(adapterAccompagnements);

        adapterBoissons = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, listeBoissons);
        AutoCompleteTextView boissonEditText = view.findViewById(R.id.boissonEditText);
        boissonEditText.setThreshold(1);
        boissonEditText.setAdapter(adapterBoissons);

        ajouterPlatButton = view.findViewById(R.id.ajouterPlatButton);
        ajouterAccompagnementButton = view.findViewById(R.id.ajouterAccompagnementButton);
        ajouterBoissonButton = view.findViewById(R.id.ajouterBoissonButton);

        ajouterPlatButton.setOnClickListener(v -> ajouterPlat());
        ajouterAccompagnementButton.setOnClickListener(v -> ajouterAccompagnement());
        ajouterBoissonButton.setOnClickListener(v -> ajouterBoisson());

        return view;
    }

    private void ajouterCommande(String type, ArrayAdapter<String> adapter) {
        if (contient.getChildCount() >= MAX_CHOIX) {
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

        EditText quantiteEditText = new EditText(getContext());
        quantiteEditText.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        quantiteEditText.setHint("Quantité");
        quantiteEditText.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);
        quantiteEditText.setText("1");

        commandeLayout.addView(choixEditText);
        commandeLayout.addView(quantiteEditText);

        contient.addView(commandeLayout);

        choixEditText.setOnItemClickListener((parent, view, position, id) -> {
            String choix = choixEditText.getText().toString();
            String quantiteStr = quantiteEditText.getText().toString();
            int quantite = quantiteStr.isEmpty() ? 1 : Integer.parseInt(quantiteStr);

            Toast.makeText(getContext(), "Choisi : " + choix + " (" + quantite + ")", Toast.LENGTH_SHORT).show();
        });
    }

    private void ajouterPlat() {
        ajouterCommande("Plat", adapterPlats);
    }

    private void ajouterAccompagnement() {
        ajouterCommande("Accompagnement", adapterAccompagnements);
    }

    private void ajouterBoisson() {
        ajouterCommande("Boisson", adapterBoissons);
    }

    public List<ConviveCommande> getCommandes() {
        return commandes;
    }
}