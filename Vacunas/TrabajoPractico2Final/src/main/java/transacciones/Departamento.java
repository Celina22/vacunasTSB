package transacciones;

import back.TSBArrayList;

import java.util.Collection;

public class Departamento {
    private String key;
    private String nombre;
    private int vacHombre;
    private int vacMujer;
    private int vacPrimera;
    private int vacSegunda;
    private TSBArrayList tipoVacuna;

    public Departamento(String key, String nombre){
        this.key = key;
        this.nombre = nombre;
        vacHombre = 0;
        vacMujer = 0;
        vacPrimera = 0;
        vacSegunda = 0;
        tipoVacuna = new TSBArrayList();
    }

    public String getKey() {
        return key;
    }

    public void sumarGenero(String campo) {
        if (campo.compareTo("\"F\"")==0){vacMujer++;}
        else{vacHombre++;}
    }

    public void sumarDosis(String campo) {
        if (campo.compareTo("1")==0){vacPrimera++;}
        else{vacSegunda++;}
    }

    public void sumarVacuna(String campo) {
        for (int i = 0; i < this.tipoVacuna.size(); i++) {
            Vacuna vac = (Vacuna) tipoVacuna.get(i);
            if (vac.getKey().compareTo(campo)==0){
                vac.setCantidad( vac.getCantidad()+1 );
                return;
            }
        }
        Vacuna vac = new Vacuna(campo);
        this.tipoVacuna.add(vac);
    }

    @Override
    public String toString() {
        return " ( " +key+ " ) " + nombre;
    }

    public String getVacHombre() {
        return "Cantidad de hombres vacunados: "+vacHombre;
    }

    public String getVacMujer() {
        return "Cantidad de mujeres vacunados: "+vacMujer;
    }

    public String getPrimeraDosis() {
        return "Cantidad de primeras dosis aplicadas: "+vacPrimera;
    }
    public String getSegundaDosis() {
        return "Cantidad de segundas dosis aplicadas: "+vacSegunda;
    }

    public Collection getVacunas(){
        Collection collection;
        return collection = tipoVacuna;
    }

    public String getNombre() {
        return nombre;
    }
}