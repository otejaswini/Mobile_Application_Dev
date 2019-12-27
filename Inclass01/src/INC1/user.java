package INC1;
/*
 * - Assignment# In class Assignment 01
 * - user.java
 * - Tejaswini Oduru
 * */
public class user {
    String firstname;
    String lastname;
    int age;
    String email;
    String gender;
    String city;
    String state;

    @Override
    public String toString() {
        return "user{" +
                "firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", age=" + age +
                ", email='" + email + '\'' +
                ", gender='" + gender + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                '}';
    }

    public user(String firstname, String lastname, int age, String email, String gender, String city, String state) {
        this.firstname=firstname;
        this.lastname=lastname;
        this.age=age;
        this.email=email;
        this.gender=gender;
        this.city=city;
        this.state=state;
    }


}
