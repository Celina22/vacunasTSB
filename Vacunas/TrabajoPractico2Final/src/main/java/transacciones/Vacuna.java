package transacciones;

public class Vacuna {
    private String key;
    private int cantidad;

    public Vacuna(String key){
        this.key = key;
        this.cantidad = 1;
    }

    public String getKey() {
        return key;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }


    @Override
    public String toString() {
        return "Vacuna: "+key+", Cantidad de Dosis aplicadas: "+cantidad;
    }
}