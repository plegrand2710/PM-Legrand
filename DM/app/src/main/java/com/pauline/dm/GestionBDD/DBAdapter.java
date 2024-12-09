package com.pauline.dm.GestionBDD;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.pauline.dm.Admin.Produit;
import com.pauline.dm.Admin.Table;
import com.pauline.dm.Admin.Utilisateur;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DBAdapter {
    private static final String[] TABLES = {"utilisateurs", "tables", "produit", "commande", "contient"};
    static final String TABLE_UTILISATEURS = "utilisateurs";
    static final String KEY_IDUTILISATEUR = "idUtilisateur";
    static final String KEY_IDENTIFIANT = "identifiant";
    static final String KEY_MDP = "mdp";
    static final String KEY_ROLE = "role";
    static final String CREATE_TABLE_UTILISATEURS =
            "CREATE TABLE " + TABLE_UTILISATEURS + " (" +
                    KEY_IDUTILISATEUR + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    KEY_IDENTIFIANT + " TEXT UNIQUE NOT NULL, " +
                    KEY_MDP + " TEXT NOT NULL, " +
                    KEY_ROLE + " TEXT CHECK(" + KEY_ROLE + " IN ('Superviseur', 'Responsable', 'Serveur', 'Cuisinier')) NOT NULL);";

    public static final String TABLE_TABLES = "tables";
    static final String KEY_NUMTABLE = "numTable";
    public static final String KEY_NBCONVIVES = "nbConvives";
    static final String KEY_NBCOLONNE = "numColonne";
    static final String KEY_NBLIGNE = "numLigne";
    static final String CREATE_TABLE_TABLES =
            "CREATE TABLE " + TABLE_TABLES + " (" +
                    KEY_NUMTABLE + " INTEGER PRIMARY KEY, " +
                    KEY_NBCONVIVES + " INTEGER DEFAULT 0, " +
                    KEY_NBCOLONNE + " INTEGER NOT NULL, " +
                    KEY_NBLIGNE + " INTEGER NOT NULL);";

    public static final String TABLE_SALLE = "salle";
    static final String KEY_NUMSALLE = "numSalle";
    public static final String KEY_IMAGE = "image";
    static final String CREATE_TABLE_SALLE =
            "CREATE TABLE " + TABLE_SALLE + " (" +
                    KEY_NUMSALLE + " INTEGER PRIMARY KEY, " +
                    KEY_IMAGE + " TEXT NOT NULL);";

    static final String TABLE_PRODUIT = "produit";
    static final String KEY_IDPRODUIT = "idProduit";
    public static final String KEY_NOMPRODUIT = "nomProduit";
    public static final String KEY_CUISSON = "cuisson";
    static final String KEY_CATEGORIE = "categorie";
    static final String KEY_PRIX = "prix";
    static final String CREATE_TABLE_PRODUIT =
            "CREATE TABLE " + TABLE_PRODUIT + " (" +
                    KEY_IDPRODUIT + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    KEY_NOMPRODUIT + " TEXT NOT NULL, " +
                    KEY_CATEGORIE + " TEXT CHECK(" + KEY_CATEGORIE + " IN ('plat', 'boisson', 'accompagnement')) NOT NULL, " +
                    KEY_CUISSON + " INTEGER NOT NULL, " +
                    KEY_PRIX + " REAL NOT NULL);";

    static final String TABLE_COMMANDE = "commande";
    static final String KEY_IDCOMMANDE = "idCommande";
    static final String KEY_STATUS = "status";
    static final String KEY_CUISSON_COMMANDE = "cuisson_Commande";
    static final String KEY_NUMTABLE_FK = "numTable";
    static final String CREATE_TABLE_COMMANDE =
            "CREATE TABLE " + TABLE_COMMANDE + " (" +
                    KEY_IDCOMMANDE + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    KEY_STATUS + " TEXT CHECK(" + KEY_STATUS + " IN ('en cours', 'réglée')) DEFAULT 'en cours', " +
                    KEY_CUISSON_COMMANDE + " TEXT CHECK(" + KEY_CUISSON_COMMANDE + " IN ('0', '1', '2', '3', '4')) DEFAULT '0' NOT NULL, " +
                    KEY_NUMTABLE_FK + " INTEGER, " +
                    "FOREIGN KEY (" + KEY_NUMTABLE_FK + ") REFERENCES " + TABLE_TABLES + "(" + KEY_NUMTABLE + ") ON DELETE SET NULL);";

    static final String TABLE_CONTIENT = "contient";
    static final String KEY_IDCOMMANDE_FK = "idCommande";
    static final String KEY_IDPRODUIT_FK = "idProduit";
    static final String KEY_QUANTITE = "quantite";
    static final String KEY_TRAITEMENT_CONTIENT = "traitement_contient";
    static final String CREATE_TABLE_CONTIENT =
            "CREATE TABLE " + TABLE_CONTIENT + " (" +
                    KEY_IDCOMMANDE_FK + " INTEGER, " +
                    KEY_IDPRODUIT_FK + " INTEGER, " +
                    KEY_QUANTITE + " INTEGER NOT NULL, " +
                    KEY_TRAITEMENT_CONTIENT + " TEXT CHECK(" + KEY_TRAITEMENT_CONTIENT + " IN ('à préparer', 'en cours', 'terminé')), " +
                    "PRIMARY KEY (" + KEY_IDCOMMANDE_FK + ", " + KEY_IDPRODUIT_FK + "), " +
                    "FOREIGN KEY (" + KEY_IDCOMMANDE_FK + ") REFERENCES " + TABLE_COMMANDE + "(" + KEY_IDCOMMANDE + ") ON DELETE CASCADE, " +
                    "FOREIGN KEY (" + KEY_IDPRODUIT_FK + ") REFERENCES " + TABLE_PRODUIT + "(" + KEY_IDPRODUIT + ") ON DELETE CASCADE);";


    static final String TAG = "DMProjet";
    static final String DATABASE_NAME = "MyDB";
    static final int DATABASE_VERSION = 1;
    final Context context;
    DatabaseHelper DBHelper;
    SQLiteDatabase db;

    public DBAdapter(Context ctx) {
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            try {
                db.execSQL(CREATE_TABLE_UTILISATEURS);
                db.execSQL(CREATE_TABLE_TABLES);
                db.execSQL(CREATE_TABLE_PRODUIT);
                db.execSQL(CREATE_TABLE_COMMANDE);
                db.execSQL(CREATE_TABLE_CONTIENT);
                db.execSQL(CREATE_TABLE_SALLE);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_UTILISATEURS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_TABLES);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_PRODUIT);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMMANDE);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTIENT);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_SALLE);

            onCreate(db);
        }
    }

    public DBAdapter open() throws SQLException {
        db = DBHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        DBHelper.close();
    }

    public boolean insertUtilisateur(String username, String password, String role) {
        ContentValues values = new ContentValues();
        values.put(KEY_IDENTIFIANT, username);
        values.put(KEY_MDP, password);
        values.put(KEY_ROLE, role);

        long result = db.insert(TABLE_UTILISATEURS, null, values);
        return result != -1;
    }

    public long insertUtilisateur(Integer idUtilisateur, String identifiant, String mdp, String role) {
        ContentValues values = new ContentValues();
        values.put(KEY_IDUTILISATEUR, idUtilisateur);
        values.put(KEY_IDENTIFIANT, identifiant);
        values.put(DBAdapter.KEY_MDP, mdp);
        values.put(DBAdapter.KEY_ROLE, role);
        return db.insert(DBAdapter.TABLE_UTILISATEURS, null, values);
    }

    public Utilisateur getUtilisateur(int idIdentifiant) {
        Cursor cursor = db.query(DBAdapter.TABLE_UTILISATEURS, null, DBAdapter.KEY_IDUTILISATEUR + "=?", new String[]{String.valueOf(idIdentifiant)}, null, null, null);
        Utilisateur user = null;
        if (cursor != null && cursor.moveToFirst()) {
            String username = cursor.getString(cursor.getColumnIndexOrThrow(KEY_IDENTIFIANT));
            String password = cursor.getString(cursor.getColumnIndexOrThrow(KEY_MDP));
            String role = cursor.getString(cursor.getColumnIndexOrThrow(KEY_ROLE));
            user = new Utilisateur(idIdentifiant, username, password, role);
            cursor.close();
        }
        return user;
    }

    public boolean deleteUtilisateur(int userId) {
        if (db == null || !db.isOpen()) {
            open();
        }

        int rowsAffected = db.delete(TABLE_UTILISATEURS, KEY_IDUTILISATEUR + "=?", new String[]{String.valueOf(userId)});
        return rowsAffected > 0;
    }

    public long insertTable(int numTable, int nbConvives, int nbColonne, int nbLigne) {
        ContentValues values = new ContentValues();
        values.put(KEY_NUMTABLE, numTable);
        values.put(KEY_NBCONVIVES, nbConvives);
        values.put(KEY_NBCOLONNE, nbColonne);
        values.put(KEY_NBLIGNE, nbLigne);
        return db.insert(TABLE_TABLES, null, values);
    }

    public boolean insertTableBoolean(int numTable, int nbConvives, int nbColonnes, int nbLignes) {
        ContentValues values = new ContentValues();
        values.put(KEY_NUMTABLE, numTable);
        values.put(KEY_NBCONVIVES, nbConvives);
        values.put(KEY_NBCOLONNE, nbColonnes);
        values.put(KEY_NBLIGNE, nbLignes);

        long result = db.insert(TABLE_TABLES, null, values);
        return result != -1; // Retourne true si l'insertion a réussi
    }

    public Cursor getTable(int numTable) {
        return db.query(TABLE_TABLES, null, KEY_NUMTABLE + "=?", new String[]{String.valueOf(numTable)}, null, null, null);
    }

    public int updateTable(int numTable, int nbConvives, int nbColonne, int nbLigne) {
        ContentValues values = new ContentValues();
        values.put(KEY_NBCONVIVES, nbConvives);
        values.put(KEY_NBCOLONNE, nbColonne);
        values.put(KEY_NBLIGNE, nbLigne);
        return db.update(TABLE_TABLES, values, KEY_NUMTABLE + "=?", new String[]{String.valueOf(numTable)});
    }

    public Table getTableById(int tableId) {
        Cursor cursor = db.query(TABLE_TABLES, null, KEY_NUMTABLE + "=?", new String[]{String.valueOf(tableId)}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            int numTable = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_NUMTABLE));
            int nbConvives = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_NBCONVIVES));
            int nbColonnes = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_NBCOLONNE));
            int nbLignes = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_NBLIGNE));
            cursor.close();
            return new Table(numTable, nbConvives, nbColonnes, nbLignes);
        }
        if (cursor != null) {
            cursor.close();
        }
        return null;
    }

    public boolean updateTableBoolean(int tableId, int nbConvives, int nbColonnes, int nbLignes) {
        ContentValues values = new ContentValues();
        values.put(KEY_NBCONVIVES, nbConvives);
        values.put(KEY_NBCOLONNE, nbColonnes);
        values.put(KEY_NBLIGNE, nbLignes);
        int rowsAffected = db.update(TABLE_TABLES, values, KEY_NUMTABLE + "=?", new String[]{String.valueOf(tableId)});
        return rowsAffected > 0;
    }

    public long insertProduit(String nomProduit, String categorie, int cuisson, double prix) {
        ContentValues values = new ContentValues();
        values.put(KEY_NOMPRODUIT, nomProduit);
        values.put(KEY_CATEGORIE, categorie);
        values.put(KEY_CUISSON, cuisson);
        values.put(KEY_PRIX, prix);
        return db.insert(TABLE_PRODUIT, null, values);
    }

    public long insertProduit(Integer idProduit, String nomProduit, String categorie, int cuisson, double prix) {
        ContentValues values = new ContentValues();
        values.put(KEY_IDPRODUIT, idProduit);
        values.put(KEY_NOMPRODUIT, nomProduit);
        values.put(KEY_CATEGORIE, categorie);
        values.put(KEY_CUISSON, cuisson);
        values.put(KEY_PRIX, prix);
        Log.d(TAG, "insertProduit: insertion en cours");
        return db.insert(TABLE_PRODUIT, null, values);
    }

    public Cursor getProduit(int idProduit) {
        return db.query(TABLE_PRODUIT, null, KEY_IDPRODUIT + "=?", new String[]{String.valueOf(idProduit)}, null, null, null);
    }

    public Cursor getProduitsByCategorie(String categorie) {
        return db.query(TABLE_PRODUIT, null, KEY_CATEGORIE + "=?", new String[]{categorie}, null, null, null);
    }

    public int updateProduit(int idProduit, String nomProduit, String categorie, int cuisson, double prix) {
        ContentValues values = new ContentValues();
        values.put(KEY_NOMPRODUIT, nomProduit);
        values.put(KEY_CATEGORIE, categorie);
        values.put(KEY_CUISSON, cuisson);
        values.put(KEY_PRIX, prix);
        return db.update(TABLE_PRODUIT, values, KEY_IDPRODUIT + "=?", new String[]{String.valueOf(idProduit)});
    }

    public int deleteProduit(int idProduit) {
        return db.delete(TABLE_PRODUIT, KEY_IDPRODUIT + "=?", new String[]{String.valueOf(idProduit)});
    }

    public long insertCommande(String status, String cuisson, int numTable) {
        ContentValues values = new ContentValues();
        values.put(KEY_STATUS, status);
        values.put(KEY_CUISSON_COMMANDE, cuisson);
        values.put(KEY_NUMTABLE_FK, numTable);
        return db.insert(TABLE_COMMANDE, null, values);
    }

    public long insertCommande(Integer idCommande, String status, String cuisson, int numTable) {
        ContentValues values = new ContentValues();
        values.put(KEY_IDCOMMANDE, idCommande);
        values.put(KEY_STATUS, status);
        values.put(KEY_CUISSON_COMMANDE, cuisson);
        values.put(KEY_NUMTABLE_FK, numTable);
        return db.insert(TABLE_COMMANDE, null, values);
    }

    public Cursor getCommande(int idCommande) {
        return db.query(TABLE_COMMANDE, null, KEY_IDCOMMANDE + "=?", new String[]{String.valueOf(idCommande)}, null, null, null);
    }

    public int updateCommande(int idCommande, String status, String cuisson, int numTable) {
        ContentValues values = new ContentValues();
        values.put(KEY_STATUS, status);
        values.put(KEY_CUISSON_COMMANDE, cuisson);
        values.put(KEY_NUMTABLE_FK, numTable);
        return db.update(TABLE_COMMANDE, values, KEY_IDCOMMANDE + "=?", new String[]{String.valueOf(idCommande)});
    }

    public int deleteCommande(int idCommande) {
        return db.delete(TABLE_COMMANDE, KEY_IDCOMMANDE + "=?", new String[]{String.valueOf(idCommande)});
    }

    public long insertContient(int idCommande, int idProduit, int quantite, String traitement) {
        ContentValues values = new ContentValues();
        values.put(KEY_IDCOMMANDE_FK, idCommande);
        values.put(KEY_IDPRODUIT_FK, idProduit);
        values.put(KEY_QUANTITE, quantite);
        values.put(KEY_TRAITEMENT_CONTIENT, traitement);
        return db.insert(TABLE_CONTIENT, null, values);
    }

    public Cursor getContient(int idCommande, int idProduit) {
        return db.query(TABLE_CONTIENT, null, KEY_IDCOMMANDE_FK + "=? AND " + KEY_IDPRODUIT_FK + "=?", new String[]{String.valueOf(idCommande), String.valueOf(idProduit)}, null, null, null);
    }

    public int updateContient(int idCommande, int idProduit, int quantite, String traitement) {
        ContentValues values = new ContentValues();
        values.put(KEY_QUANTITE, quantite);
        values.put(KEY_TRAITEMENT_CONTIENT, traitement);
        return db.update(TABLE_CONTIENT, values, KEY_IDCOMMANDE_FK + "=? AND " + KEY_IDPRODUIT_FK + "=?", new String[]{String.valueOf(idCommande), String.valueOf(idProduit)});
    }

    public int deleteContient(int idCommande, int idProduit) {
        return db.delete(TABLE_CONTIENT, KEY_IDCOMMANDE_FK + "=? AND " + KEY_IDPRODUIT_FK + "=?", new String[]{String.valueOf(idCommande), String.valueOf(idProduit)});
    }

    public void loadBD(){
        insertTable(4, 4, 1, 4);
        insertTable(8, 6, 2, 4);
        insertTable(16, 7, 4, 4);
        insertTable(2, 3, 1, 2);

        insertProduit("Burger", "plat", 1, 1250);
        insertProduit("Salade César", "plat", 0, 900);
        insertProduit("Pizza Margherita", "plat", 0, 1000);
        insertProduit("Lasagne", "plat", 0, 1350);
        insertProduit("Pâtes Carbonara", "plat", 0, 1100);
        insertProduit("Steak Frites", "plat", 1, 1500);
        insertProduit("Entrecôte Frites", "plat", 1, 1800);
        insertProduit("Poulet Rôti", "plat", 0, 1400);
        insertProduit("Risotto aux Champignons", "plat", 0, 1200);
        insertProduit("Tartare de Boeuf", "plat", 0, 1300);
        insertProduit("Quiche Lorraine", "plat", 0, 850);

        insertProduit("Coca-Cola", "boisson", 0, 200);
        insertProduit("Eau Minérale", "boisson", 0, 150);
        insertProduit("Jus d'Orange", "boisson", 0, 250);
        insertProduit("Café Expresso", "boisson", 0, 180);
        insertProduit("Thé Vert", "boisson", 0, 200);
        insertProduit("Limonade", "boisson", 0, 220);
        insertProduit("Smoothie Fraise", "boisson", 0, 300);
        insertProduit("Vin Rouge", "boisson", 0, 500);
        insertProduit("Vin Blanc", "boisson", 0, 500);
        insertProduit("Bière", "boisson", 0, 350);

        insertProduit("Salade Verte", "accompagnement", 0, 200);
        insertProduit("Légumes Grillés", "accompagnement", 0, 400);
        insertProduit("Purée de Pommes de Terre", "accompagnement", 0, 300);
        insertProduit("Pommes de Terre Sautées", "accompagnement", 0, 350);
        insertProduit("Gratin Dauphinois", "accompagnement", 0, 450);
        insertProduit("Haricots Verts", "accompagnement", 0, 280);
        insertProduit("Chips Maison", "accompagnement", 0, 300);
        insertProduit("Onion Rings", "accompagnement", 0, 320);
    }

    public void clearTable(String tableName) {
        db.execSQL("DELETE FROM " + tableName);
    }

    public void insertFromJson(String tableName, JSONObject jsonObject) throws JSONException {
        ContentValues values = new ContentValues();

        switch (tableName) {
            case "produit":
                values.put(KEY_IDPRODUIT, jsonObject.getInt("idProduit"));
                values.put(KEY_NOMPRODUIT, jsonObject.getString("nomProduit"));
                values.put(KEY_CATEGORIE, jsonObject.getString("categorie"));
                values.put(KEY_CUISSON, jsonObject.getInt("cuisson"));
                values.put(KEY_PRIX, jsonObject.getDouble("prix"));
                db.insert(TABLE_PRODUIT, null, values);
                break;

            case "commande":
                values.put(KEY_IDCOMMANDE, jsonObject.getInt("idCommande"));
                values.put(KEY_STATUS, jsonObject.getString("status"));
                values.put(KEY_CUISSON_COMMANDE, jsonObject.getString("cuisson_Commande"));
                values.put(KEY_NUMTABLE_FK, jsonObject.getInt("numTable"));
                db.insert(TABLE_COMMANDE, null, values);
                break;

            case "utilisateurs":
                values.put(KEY_IDUTILISATEUR, jsonObject.getInt("idUtilisateur"));
                values.put(KEY_IDENTIFIANT, jsonObject.getString("identifiant"));
                values.put(KEY_MDP, jsonObject.getString("mdp"));
                values.put(KEY_ROLE, jsonObject.getString("role"));
                db.insert(TABLE_UTILISATEURS, null, values);
                break;

            case "contient":
                values.put(KEY_IDCOMMANDE_FK, jsonObject.getInt("idCommande"));
                values.put(KEY_IDPRODUIT_FK, jsonObject.getInt("idProduit"));
                values.put(KEY_QUANTITE, jsonObject.getInt("quantite"));
                values.put(KEY_TRAITEMENT_CONTIENT, jsonObject.getString("traitement_contient"));
                db.insert(TABLE_CONTIENT, null, values);
                break;

            case "tables":
                values.put(KEY_NUMTABLE, jsonObject.getInt("numTable"));
                values.put(KEY_NBCONVIVES, jsonObject.getInt("nbConvives"));
                values.put(KEY_NBCOLONNE, jsonObject.getInt("numColonne"));
                values.put(KEY_NBLIGNE, jsonObject.getInt("numLigne"));
                db.insert(TABLE_TABLES, null, values);
                break;

                case "salle":
                if (!jsonObject.has("numSalle") || !jsonObject.has("image")) {
                    Log.e("DBAdapter", "Clé(s) manquante(s) dans l'objet JSON pour la table salle : " + jsonObject.toString());
                    return;
                }

                int numSalle = jsonObject.getInt("numSalle");
                String imagePath = jsonObject.getString("image").replace("\\/", "/");

                Log.d("DBAdapter", "Insertion dans salle : numSalle=" + numSalle + ", image=" + imagePath);

                values.put("numSalle", numSalle);
                values.put("image", imagePath);
                db.insert(tableName, null, values);
                break;

            default:
                throw new IllegalArgumentException("Table inconnue : " + tableName);
        }
    }

    public String getConnexionRole(String username, String password) {
        String role = null;
        Log.d(TAG, "Début de la méthode getConnexionRole avec username: " + username);

        try {
            Cursor userCursor = db.query(
                    DBAdapter.TABLE_UTILISATEURS,
                    new String[]{DBAdapter.KEY_IDUTILISATEUR},
                    DBAdapter.KEY_IDENTIFIANT + "=? AND " + DBAdapter.KEY_MDP + "=?",
                    new String[]{username, password},
                    null,
                    null,
                    null
            );

            if (userCursor != null && userCursor.moveToFirst()) {
                int userId = userCursor.getInt(userCursor.getColumnIndexOrThrow(DBAdapter.KEY_IDUTILISATEUR));
                Log.d(TAG, "Utilisateur trouvé avec ID: " + userId);
                userCursor.close();

                role = getRoleByUserId(userId);
            } else {
                Log.d(TAG, "Utilisateur non trouvé ou identifiants incorrects.");
            }

            if (userCursor != null) {
                userCursor.close();
            }

        } catch (Exception e) {
            Log.e(TAG, "Erreur lors de la récupération du rôle utilisateur: ", e);
        }

        Log.d(TAG, "Fin de la méthode getConnexionRole. Rôle renvoyé: " + (role != null ? role : "null"));
        return role;
    }

    private String getRoleByUserId(int userId) {
        String role = null;
        Log.d(TAG, "Début de la méthode getRoleByUserId avec userId: " + userId);

        Cursor roleCursor = null;
        try {
            roleCursor = db.query(
                    DBAdapter.TABLE_UTILISATEURS,
                    new String[]{DBAdapter.KEY_ROLE},
                    DBAdapter.KEY_IDUTILISATEUR + "=?",
                    new String[]{String.valueOf(userId)},
                    null,
                    null,
                    null
            );

            if (roleCursor != null && roleCursor.moveToFirst()) {
                role = roleCursor.getString(roleCursor.getColumnIndexOrThrow(DBAdapter.KEY_ROLE));
                Log.d(TAG, "Rôle de l'utilisateur récupéré: " + role);
            } else {
                Log.d(TAG, "Aucun rôle trouvé pour l'utilisateur avec ID: " + userId);
            }
        } catch (Exception e) {
            Log.e(TAG, "Erreur lors de la récupération du rôle utilisateur: ", e);
        } finally {
            if (roleCursor != null) {
                roleCursor.close();
            }
        }

        Log.d(TAG, "Fin de la méthode getRoleByUserId. Rôle renvoyé: " + (role != null ? role : "null"));
        return role;
    }

    public List<Utilisateur> getAllUsers() {
        List<Utilisateur> userList = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_UTILISATEURS;

        Log.d(TAG, "Requête pour récupérer tous les utilisateurs : " + query);

        Cursor cursor = db.rawQuery(query, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_IDUTILISATEUR));
                String username = cursor.getString(cursor.getColumnIndexOrThrow(KEY_IDENTIFIANT));
                String password = cursor.getString(cursor.getColumnIndexOrThrow(KEY_MDP));
                String role = cursor.getString(cursor.getColumnIndexOrThrow(KEY_ROLE));

                Utilisateur user = new Utilisateur(id, username, password, role);
                userList.add(user);

                Log.d(TAG, "Utilisateur récupéré : ID=" + id + ", Nom=" + username + ", Rôle=" + role);
            } while (cursor.moveToNext());

            cursor.close();
        } else {
            Log.d(TAG, "Aucun utilisateur trouvé.");
        }

        return userList;
    }

    public boolean updateUtilisateur(int id, String username, String password, String role) {
        ContentValues values = new ContentValues();
        values.put(KEY_IDENTIFIANT, username);
        values.put(KEY_MDP, password);
        values.put(KEY_ROLE, role);
        return db.update(TABLE_UTILISATEURS, values, KEY_IDUTILISATEUR + " = ?", new String[]{String.valueOf(id)}) > 0;
    }

    public List<Table> getAllTables() {
        List<Table> tables = new ArrayList<>();
        Cursor cursor = db.query(TABLE_TABLES, null, null, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int numTable = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_NUMTABLE));
                int nbConvives = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_NBCONVIVES));
                int nbColonnes = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_NBCOLONNE));
                int nbLignes = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_NBLIGNE));

                tables.add(new Table(numTable, nbConvives, nbColonnes, nbLignes));
            } while (cursor.moveToNext());
            cursor.close();
        }

        return tables;
    }

    public boolean deleteTable(int numTable) {
        int rowsAffected = db.delete(TABLE_TABLES, KEY_NUMTABLE + "=?", new String[]{String.valueOf(numTable)});
        return rowsAffected > 0;
    }

    public boolean updateTableBoolean(Table table, int numTableOriginal) {
        ContentValues values = new ContentValues();
        values.put(KEY_NUMTABLE, table.getNumTable());
        values.put(KEY_NBCONVIVES, table.getNbConvives());
        values.put(KEY_NBCOLONNE, table.getNumColonne());
        values.put(KEY_NBLIGNE, table.getNumLigne());

        int rowsAffected = db.update(
                TABLE_TABLES,
                values,
                KEY_NUMTABLE + "=?",
                new String[]{String.valueOf(numTableOriginal)}
        );

        return rowsAffected > 0;
    }

    public long insertSalleImage(String imageUri) {
        long newRowId = -1;
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_IMAGE, imageUri);

            newRowId = db.insert(TABLE_SALLE, null, values);
            if (newRowId == -1) {
                Log.e("DBAdapter", "Erreur lors de l'insertion de l'image de la salle.");
            } else {
                Log.d("DBAdapter", "Image de la salle insérée avec succès. ID : " + newRowId);
            }
        } catch (Exception e) {
            Log.e("DBAdapter", "Erreur lors de l'insertion de l'image de la salle : ", e);
        }
        return newRowId; // Retourne l'ID de la nouvelle ligne insérée
    }

    public boolean updateSalleImage(String imageUri) {
        boolean success = false;
        try {
            ContentValues values = new ContentValues();
            values.put(KEY_IMAGE, imageUri);

            int rowsUpdated = db.update(TABLE_SALLE, values, null, null);
            success = rowsUpdated > 0;

            if (success) {
                Log.d("DBAdapter", "Image de la salle mise à jour avec succès.");
            } else {
                Log.d("DBAdapter", "Aucune ligne mise à jour pour l'image de la salle.");
            }
        } catch (Exception e) {
            Log.e("DBAdapter", "Erreur lors de la mise à jour de l'image de la salle : ", e);
        }
        return success; // Retourne true si la mise à jour a réussi
    }

    public long saveSalleImage(String imageUri) {
        long rowId = -1;
        try {
            Cursor cursor = db.query(TABLE_SALLE, null, null, null, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                // Une ligne existe déjà, effectue une mise à jour
                boolean success = updateSalleImage(imageUri);
                rowId = success ? 1 : -1; // Retourne 1 si la mise à jour a réussi
            } else {
                // Aucune ligne existante, effectue une insertion
                rowId = insertSalleImage(imageUri);
            }

            if (cursor != null) {
                cursor.close();
            }
        } catch (Exception e) {
            Log.e("DBAdapter", "Erreur lors de la sauvegarde de l'image de la salle : ", e);
        }
        return rowId; // Retourne l'ID de la nouvelle ligne ou 1 pour une mise à jour
    }

    public String getSalleImage() {
        String imageUri = null;
        Cursor cursor = null;
        try {
            cursor = db.query(TABLE_SALLE,
                    new String[]{KEY_IMAGE},
                    null,
                    null,
                    null,
                    null,
                    null);

            if (cursor != null && cursor.moveToFirst()) {
                imageUri = cursor.getString(cursor.getColumnIndexOrThrow(KEY_IMAGE));
            }
        } catch (Exception e) {
            Log.e("DBAdapter", "Erreur lors de la récupération de l'image de la salle : ", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return imageUri;
    }

    public List<Produit> getAllProduits() {
        List<Produit> produits = new ArrayList<>();
        Cursor cursor = null;

        try {
            // Requête pour récupérer tous les produits
            cursor = db.query(
                    TABLE_PRODUIT, // Table
                    null,          // Colonnes (null pour tout sélectionner)
                    null,          // Clause WHERE
                    null,          // Arguments WHERE
                    null,          // GROUP BY
                    null,          // HAVING
                    null           // ORDER BY
            );

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    int idProduit = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_IDPRODUIT));
                    String nomProduit = cursor.getString(cursor.getColumnIndexOrThrow(KEY_NOMPRODUIT));
                    String categorie = cursor.getString(cursor.getColumnIndexOrThrow(KEY_CATEGORIE));
                    boolean cuisson = cursor.getInt(cursor.getColumnIndexOrThrow(KEY_CUISSON)) == 1;
                    double prix = cursor.getDouble(cursor.getColumnIndexOrThrow(KEY_PRIX));

                    produits.add(new Produit(idProduit, nomProduit, categorie, cuisson, prix));
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("DBAdapter", "Erreur lors de la récupération des produits", e);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return produits;
    }
}
