package com.pauline.dm;

public class ConviveCommande {
    private String _plat ;
    private String _boisson ;
    private String _accompagnement ;

    ConviveCommande(){
    }

    public String get_accompagnement() {
        return _accompagnement;
    }

    public void set_accompagnement(String accompagnement) {
        _accompagnement = accompagnement;
    }

    public String get_boisson() {
        return _boisson;
    }

    public void set_boisson(String boisson) {
        _boisson = boisson;
    }

    public String get_plat() {
        return _plat;
    }

    public void set_plat(String plat) {
        _plat = plat;
    }
}
