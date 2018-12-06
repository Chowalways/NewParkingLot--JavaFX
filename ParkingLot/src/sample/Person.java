package sample;

public class Person extends BasicObj {
    private Gender gender;
    private int age;

    public void setGender(String gender){
        if (gender.equalsIgnoreCase("male")){
            this.gender = Gender.MALE;
        }
        else if(gender.equalsIgnoreCase("female")){
            this.gender = Gender.FEMAlE;
        }
    }

    public void setID(String id){
        super.Id = id;
    }

    public  void setAge(int age){
        this.age = age;
    }
}
