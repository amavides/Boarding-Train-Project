package sample;

public class Passenger {
    //Passenger Variables//
    String firstName;
    String lastName;
    Integer secondsInQueue;

    //Setting the passenger name//
    public void setName(){
        this.firstName=firstName;
        this.lastName=lastName;
    }

    //Retrieving the passenger name//
    public String getName(){
        return firstName+" "+lastName;
    }

    //Storing the waiting time//
    public void setSecondsInQueue(){
        this.secondsInQueue=secondsInQueue;
    }

    //Retrieving the waiting time//
    public Integer getSeconds(){
        return secondsInQueue;
    }

}
