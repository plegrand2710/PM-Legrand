package com.pauline.dm;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBAdapter {

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

    static final String TABLE_TABLES = "tables";
    static final String KEY_NUMTABLE = "numTable";
    static final String KEY_NBCONVIVES = "nbConvives";
    static final String KEY_NBCOLONNE = "numColonne";
    static final String KEY_NBLIGNE = "numLigne";
    static final String CREATE_TABLE_TABLES =
            "CREATE TABLE " + TABLE_TABLES + " (" +
                    KEY_NUMTABLE + " INTEGER PRIMARY KEY, " +
                    KEY_NBCONVIVES + " INTEGER DEFAULT 0, " +
                    KEY_NBCOLONNE + " INTEGER NOT NULL, " +
                    KEY_NBLIGNE + " INTEGER NOT NULL);";

    static final String TABLE_PRODUIT = "produit";
    static final String KEY_IDPRODUIT = "idProduit";
    static final String KEY_NOMPRODUIT = "nomProduit";
    static final String KEY_CUISSON = "cuisson";
    static final String KEY_CATEGORIE = "categorie";
    static final String KEY_PRIX = "prix";
    static final String CREATE_TABLE_PRODUIT =
            "CREATE TABLE " + TABLE_PRODUIT + " (" +
                    KEY_IDPRODUIT + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    KEY_NOMPRODUIT + " TEXT NOT NULL, " +
                    KEY_CATEGORIE + " TEXT CHECK(" + KEY_CATEGORIE + " IN ('plat', 'boisson', 'accompagnement')) NOT NULL, " +
                    KEY_CUISSON + " BOOLEAN NOT NULL, " +
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

    public long insertUtilisateur(String identifiant, String mdp, String role) {
        ContentValues values = new ContentValues();
        values.put(KEY_IDENTIFIANT, identifiant);
        values.put(DBAdapter.KEY_MDP, mdp);
        values.put(DBAdapter.KEY_ROLE, role);
        return db.insert(DBAdapter.TABLE_UTILISATEURS, null, values);
    }

    public Cursor getUtilisateur(String identifiant) {
        return db.query(DBAdapter.TABLE_UTILISATEURS, null, DBAdapter.KEY_IDENTIFIANT + "=?", new String[]{identifiant}, null, null, null);
    }

    public int updateUtilisateur(int idUtilisateur, String identifiant, String mdp, String role) {
        ContentValues values = new ContentValues();
        values.put(KEY_IDENTIFIANT, identifiant);
        values.put(KEY_MDP, mdp);
        values.put(KEY_ROLE, role);
        return db.update(TABLE_UTILISATEURS, values, KEY_IDUTILISATEUR + "=?", new String[]{String.valueOf(idUtilisateur)});
    }

    public int deleteUtilisateur(int idUtilisateur) {
        return db.delete(TABLE_UTILISATEURS, KEY_IDUTILISATEUR + "=?", new String[]{String.valueOf(idUtilisateur)});
    }

    public long insertTable(int numTable, int nbConvives, int nbColonne, int nbLigne) {
        ContentValues values = new ContentValues();
        values.put(KEY_NUMTABLE, numTable);
        values.put(KEY_NBCONVIVES, nbConvives);
        values.put(KEY_NBCOLONNE, nbColonne);
        values.put(KEY_NBLIGNE, nbLigne);
        return db.insert(TABLE_TABLES, null, values);
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

    public int deleteTable(int numTable) {
        return db.delete(TABLE_TABLES, KEY_NUMTABLE + "=?", new String[]{String.valueOf(numTable)});
    }

    public long insertProduit(String nomProduit, String categorie, Boolean cuisson, double prix) {
        ContentValues values = new ContentValues();
        values.put(KEY_NOMPRODUIT, nomProduit);
        values.put(KEY_CATEGORIE, categorie);
        values.put(KEY_CUISSON, cuisson);
        values.put(KEY_PRIX, prix);
        return db.insert(TABLE_PRODUIT, null, values);
    }

    public Cursor getProduit(int idProduit) {
        return db.query(TABLE_PRODUIT, null, KEY_IDPRODUIT + "=?", new String[]{String.valueOf(idProduit)}, null, null, null);
    }

    public Cursor getProduitsByCategorie(String categorie) {
        return db.query(TABLE_PRODUIT, null, KEY_CATEGORIE + "=?", new String[]{categorie}, null, null, null);
    }
    
    public int updateProduit(int idProduit, String nomProduit, String categorie, Boolean cuisson, double prix) {
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
}
