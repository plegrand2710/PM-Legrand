package com.pauline.dm.Admin;

public class Table {
    private int numTable;
    private int nbConvives;
    private int numColonne;
    private int numLigne;


    public Table(int numTable, int nbConvives, int numColonne, int numLigne) {
        this.numTable = numTable;
        this.nbConvives = nbConvives;
        this.numColonne = numColonne;
        this.numLigne = numLigne;
    }

    public int getNumTable() {
        return numTable;
    }

    public void setNumTable(int numTable) {
        this.numTable = numTable;
    }

    public int getNbConvives() {
        return nbConvives;
    }

    public void setNbConvives(int nbConvives) {
        this.nbConvives = nbConvives;
    }

    public int getNumColonne() {
        return numColonne;
    }

    public void setNbColonnes(int nbColonnes) {
        this.numColonne = nbColonnes;
    }

    public int getNumLigne() {
        return numLigne;
    }

    public void setNbLignes(int nbLignes) {
        this.numLigne = nbLignes;
    }

    @Override
    public String toString() {
        return "Table{" +
                "numTable=" + numTable +
                ", nbConvives=" + nbConvives +
                ", nbColonnes=" + numColonne +
                ", nbLignes=" + numLigne +
                '}';
    }
}