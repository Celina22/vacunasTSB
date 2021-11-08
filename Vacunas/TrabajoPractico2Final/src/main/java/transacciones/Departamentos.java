package transacciones;

import back.TSBHashTableDA;
import back.TextFile;

import java.util.Collection;

public class Departamentos {
    private TSBHashTableDA departamentos;


    public Departamentos() {
        this.departamentos = new TSBHashTableDA(11,0);
    }

    public String contarVacunas(String path){
        TextFile file = new TextFile(path);
        departamentos = file.identificarVacunados();
        return departamentos.toString();
    }

    public Collection getDeptos(){
        Collection casiTodos = departamentos.values();
        casiTodos.remove(departamentos.get(01));
        System.out.println(departamentos.get("01"));
        return casiTodos;
    }

    public Departamento getDepto(){
        return (Departamento) departamentos.get("01");
    }

}