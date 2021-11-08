package back;

import transacciones.Departamento;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class TextFile {
    private File file;

    public TextFile (String path) {
        this.file = new File(path);
    }

    public String leerEncabezado(){
        String linea = "";
        try {
            Scanner scanner = new Scanner(file);
            if (scanner.hasNext()){
                linea = scanner.nextLine();
            }
        } catch (FileNotFoundException e) {
            System.out.println("No se encontro el archivo que se solicita" + file);
        }
        return linea;
    }

    public TSBHashTableDA identificarVacunados(){

        TSBHashTableDA hash = new TSBHashTableDA(11,0);
        String campos[];
        try {
            Scanner scanner = new Scanner(file);
            Departamento depto1 = new Departamento("0","Todos los que estan en la lista");
            hash.put("01", depto1);
                while (scanner.hasNext()){
                campos = scanner.nextLine().split(",");
                if (campos[7].compareTo("\"14\"") == 0){

                    if (hash.get(campos[9])==null){
                        Departamento depto = new Departamento(campos[9],campos[8]);
                        hash.put(depto.getKey(), depto);
                        depto.sumarGenero(campos[0]);
                        depto.sumarDosis(campos[13]);
                        depto.sumarVacuna(campos[11]);
                        depto1.sumarGenero(campos[0]);
                        depto1.sumarDosis(campos[13]);
                        depto1.sumarVacuna(campos[11]);
                    }
                    else {
                        Departamento depto = (Departamento) hash.get(campos[9]);
                        depto.sumarGenero(campos[0]);
                        depto.sumarDosis(campos[13]);
                        depto.sumarVacuna(campos[11]);
                        depto1.sumarGenero(campos[0]);
                        depto1.sumarDosis(campos[13]);
                        depto1.sumarVacuna(campos[11]);
                    }
                }


            }
        } catch (FileNotFoundException e) {
            System.out.println("No se encontro el archivo que usted solicita" + file);
        }
        return hash;
    }
}
