package com.pauline.dm.Admin;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.pauline.dm.GestionBDD.DBAdapter;
import com.pauline.dm.GestionBDD.GestionnaireActionBDDDistante;
import com.pauline.dm.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class ModifierDispositionSalleActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_IMPORT_IMAGE = 100;
    private static final String TAG = "DMProjet";

    private DBAdapter dbAdapter;
    private ImageView salleImageView;
    private EditText etNumTables;
    private Button btnImportLayout, btnSaveTables, btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modifier_disposition_salle);

        dbAdapter = new DBAdapter(this);
        dbAdapter.open();

        salleImageView = findViewById(R.id.salleImageView);
        etNumTables = findViewById(R.id.etNumTables);
        btnImportLayout = findViewById(R.id.btnImportLayout);
        btnSaveTables = findViewById(R.id.btnSaveTable);
        btnBack = findViewById(R.id.btnBack);

        loadSalleImage();

        btnImportLayout.setOnClickListener(v -> importSalleImage());
        btnSaveTables.setOnClickListener(v -> saveNumTables());
        btnBack.setOnClickListener(v -> finish());
    }

    private void loadSalleImage() {
        String imagePath = dbAdapter.getSalleImage();
        if (imagePath != null) {
            File imageFile = new File(imagePath);
            if (imageFile.exists()) {
                try {
                    Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
                    Bitmap orientedBitmap = getOrientedBitmap(bitmap, imageFile.getAbsolutePath());
                    salleImageView.setImageBitmap(orientedBitmap);
                } catch (IOException e) {
                    Log.e("ModifierDisposition", "Erreur lors du chargement de l'image", e);
                    Toast.makeText(this, "Erreur lors du chargement de l'image.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "L'image de salle est introuvable.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Aucune image de salle enregistrée.", Toast.LENGTH_SHORT).show();
        }
    }

    private Bitmap getOrientedBitmap(Bitmap bitmap, String imagePath) throws IOException {
        ExifInterface exif = new ExifInterface(imagePath);
        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        int rotationAngle = 0;
        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                rotationAngle = 90;
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                rotationAngle = 180;
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                rotationAngle = 270;
                break;
        }

        if (rotationAngle != 0) {
            Matrix matrix = new Matrix();
            matrix.postRotate(rotationAngle);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        }

        return bitmap;
    }

    private void importSalleImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Importer un dessin de salle"), REQUEST_CODE_IMPORT_IMAGE);
    }

    private void saveNumTables() {
        try {
            int numTables = Integer.parseInt(etNumTables.getText().toString().trim());
            if (numTables <= 0) {
                Toast.makeText(this, "Le nombre de tables doit être supérieur à 0.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Supprimer les tables locales
            dbAdapter.clearTable(DBAdapter.TABLE_TABLES);

            // Supprimer les tables distantes
            String clearTableParams = "table=" + DBAdapter.TABLE_TABLES;
            new GestionnaireActionBDDDistante(this, "clear", clearTableParams, success -> {
                if (success) {
                    // Si la suppression réussit, effectuer les insertions
                    insertTables(numTables);
                } else {
                    Toast.makeText(this, "Erreur lors de la suppression des données distantes.", Toast.LENGTH_SHORT).show();
                }
            });

        } catch (NumberFormatException e) {
            Toast.makeText(this, "Veuillez entrer un nombre valide.", Toast.LENGTH_SHORT).show();
        }
    }

    private void insertTables(int numTables) {
        for (int i = 1; i <= numTables; i++) {
            int row = (i - 1) / 4;
            int column = (i - 1) % 4;

            // Insertion locale
            long num = dbAdapter.insertTable(i, 0, column, row);

            // Insertion distante
            String insertParams = "table=" + DBAdapter.TABLE_TABLES +
                    "&numTable=" + i +
                    "&nbConvives=0" +
                    "&numColonne=" + column +
                    "&numLigne=" + row;

            new GestionnaireActionBDDDistante(this, "insert", insertParams, success -> {
                if (!success) {
                    Toast.makeText(this, "Erreur lors de l'insertion de la table " + num + " à distance.", Toast.LENGTH_SHORT).show();
                }
            });
        }

        Toast.makeText(this, "Nombre de tables mis à jour avec succès.", Toast.LENGTH_SHORT).show();
    }

    private Bitmap getOrientedBitmap(String imagePath) throws IOException {
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath);

        ExifInterface exif = new ExifInterface(imagePath);
        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        int rotationDegrees = 0;
        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                rotationDegrees = 90;
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                rotationDegrees = 180;
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                rotationDegrees = 270;
                break;
        }

        if (rotationDegrees != 0) {
            Matrix matrix = new Matrix();
            matrix.postRotate(rotationDegrees);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        }

        return bitmap;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_IMPORT_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            if (imageUri != null) {
                try {
                    String savedImagePath = saveImageToInternalStorage(imageUri);

                    Bitmap orientedBitmap = getOrientedBitmap(savedImagePath);
                    salleImageView.setImageBitmap(orientedBitmap);

                    long result = dbAdapter.saveSalleImage(savedImagePath);
                    String params = "table=" + DBAdapter.TABLE_SALLE + "&numSalle=" + result + "&image=" + savedImagePath;
                    new GestionnaireActionBDDDistante(this, "insert", params, success -> {
                        if (success) {
                            Toast.makeText(this, "Dessin de salle importé et sauvegardé à distance avec succès.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, "Échec de la sauvegarde à distance.", Toast.LENGTH_SHORT).show();
                        }
                    });
                    Toast.makeText(this, "Dessin de salle importé avec succès.", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    Log.e(TAG, "Erreur lors de l'importation de l'image", e);
                    Toast.makeText(this, "Erreur lors de l'importation de l'image.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private String saveImageToInternalStorage(Uri imageUri) throws IOException {
        InputStream inputStream = getContentResolver().openInputStream(imageUri);
        if (inputStream == null) {
            throw new IOException("Impossible de lire l'image sélectionnée.");
        }

        File imageFile = new File(getFilesDir(), "salle_image_" + System.currentTimeMillis() + ".jpg");
        try (FileOutputStream outputStream = new FileOutputStream(imageFile)) {
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
        } finally {
            inputStream.close();
        }

        return imageFile.getAbsolutePath();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dbAdapter != null) {
            dbAdapter.close();
        }
    }
}

