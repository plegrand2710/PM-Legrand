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
import android.widget.Toast;

import com.pauline.dm.ConviveCommande;
import com.pauline.dm.R;

import java.util.ArrayList;
import java.util.List;

public class FragmentPartage extends Fragments {
    private View view;
    private static final int MAX_CHOIX = 25;

    private ArrayAdapter<String> adapterPlats;
    private ArrayAdapter<String> adapterAccompagnements;
    private ArrayAdapter<String> adapterBoissons;

    private Button ajouterPlatButton;
    private Button ajouterAccompagnementButton;
    private Button ajouterBoissonButton;

    private LinearLayout containerPlats;
    private LinearLayout containerAccompagnements;
    private LinearLayout containerBoissons;

    private List<ConviveCommande> commandes = new ArrayList<>();

    private ConviveCommande commandeTable;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_partage, container, false);

        containerPlats = view.findViewById(R.id.containerPlats);
        containerAccompagnements = view.findViewById(R.id.containerAccompagnements);
        containerBoissons = view.findViewById(R.id.containerBoissons);

        adapterPlats = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, listePlats);
        adapterAccompagnements = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, listeAccompagnements);
        adapterBoissons = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, listeBoissons);

        ajouterPlatButton = view.findViewById(R.id.ajouterPlatButton);
        ajouterAccompagnementButton = view.findViewById(R.id.ajouterAccompagnementButton);
        ajouterBoissonButton = view.findViewById(R.id.ajouterBoissonButton);

        ajouterPlatButton.setOnClickListener(v -> ajouterCommande(containerPlats, "Plat partagé", adapterPlats));
        ajouterAccompagnementButton.setOnClickListener(v -> ajouterCommande(containerAccompagnements, "Accompagnement partagé", adapterAccompagnements));
        ajouterBoissonButton.setOnClickListener(v -> ajouterCommande(containerBoissons, "Boisson partagé", adapterBoissons));

        return view;
    }

    private void ajouterCommande(LinearLayout container, String type, ArrayAdapter<String> adapter) {
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

        container.addView(commandeLayout);

        choixEditText.setOnItemClickListener((parent, view, position, id) -> {
            String choix = choixEditText.getText().toString();
            String quantiteStr = quantiteEditText.getText().toString();
            int quantite = quantiteStr.isEmpty() ? 1 : Integer.parseInt(quantiteStr);

            Toast.makeText(getContext(), type + " ajouté : " + choix + " (" + quantite + ")", Toast.LENGTH_SHORT).show();
        });
    }

    public List<ConviveCommande> getCommandeTable() {
        return commandes;
    }
    public void setCommandeTable(ConviveCommande commandeTable) {
        this.commandeTable = commandeTable;
    }

}
