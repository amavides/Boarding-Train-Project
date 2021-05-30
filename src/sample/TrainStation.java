package sample;

import javafx.application.Application;
import javafx.stage.Stage;
import java.util.*;

public class TrainStation extends Application {
    Scanner sc = new Scanner(System.in);
    //waiting room linked list//
    LinkedList<String> waitingRoom = new LinkedList<String>();
    //train queue linked list//
    LinkedList<String> trainQueue = new LinkedList<String>();

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        /*Making a passenger queue object and getting data from the passenger queue */
        PassengerQueue passengerQueue = new PassengerQueue();
        passengerQueue.LoadBookingData(sc);
        while (true) {

            /* Menu Display */
            System.out.println("Enter A to add customers to training queue");
            System.out.println("Enter V to view the training queue");
            System.out.println("Enter D to delete customer from training queue");
            System.out.println("Enter R to run the simulation");
            System.out.println("Enter S to save train queue details");
            System.out.println("Enter L to load train queue data from saved file");
            System.out.println("Enter Q to quit the program");
            System.out.println("Enter a value from the menu to proceed:");
            String input = sc.next();


            switch (input) {

                case "A":
                case "a":
                    passengerQueue.addPassenger(sc, passengerQueue.loading);
                    break;

                case "V":
                case "v":
                    passengerQueue.viewTrainQueue();
                    break;

                case "D":
                case "d":
                    passengerQueue.deletePassenger(sc);
                    break;

                case "R":
                case "r":
                    passengerQueue.runSimulation();
                    System.out.println("Returning to menu redirecting to load booking data ");
                    passengerQueue.LoadBookingData(sc);
                    break;

                case "S":
                case "s":
                    System.out.println("Enter 1 from journey from Colombo to Badulla");
                    System.out.println("Enter 2 from journey from Badulla to Colombo");
                    int input2 = sc.nextInt();
                    if (input2==1){
                        passengerQueue.saveData1();
                    }
                    else if(input2 == 2) {
                        passengerQueue.saveData2();
                    }

                    else {
                        System.out.println("Invalid Input");
                    }
                    break;

                case "L":
                case "l":
                    System.out.println("Enter 1 from journey from Colombo to Badulla");
                    System.out.println("Enter 2 from journey from Badulla to Colombo");
                    int input3 = sc.nextInt();
                    if (input3==1){
                        passengerQueue.loadData1();
                    }
                    else if(input3 == 2) {
                       passengerQueue.loadData2();
                    }

                    else {
                        System.out.println("Invalid Input");
                    }
                    break;

                case "Q":
                case "q":
                    System.exit(0);
                    break;

                default:
                    System.out.println("Invalid Input");
            }
        }
    }
}


