package es.codeurjc.dataClasses;

public class person {
    private String name;
    private String surname;
    private String description;
    private String dni;
    private int phone;
    private String country;
    private String locality;
    private String province;
    private String street;
    
    public person(String[] person){
        this.name =person[0];
        this.surname =person[1];
        this.description =person[2];
        this.dni =person[3];
        this.phone = Integer.parseInt(person[4]);
        this.country =person[5];
        this.locality =person[6];
        this.province =person[7];
        this.street =person[8];
    }
    
}
