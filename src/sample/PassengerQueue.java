package sample;

import com.mongodb.Block;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.bson.Document;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PassengerQueue {
    //Making a scanner object//
    Scanner sc = new Scanner(System.in);
    //Hashmap for loading data from coursework 1//
    HashMap<String, String> loading = new HashMap<>();
    //passenger arraylist//
    ArrayList<String> passenger = new ArrayList<>();
    //link list to retrieve and save cw2 data//
    LinkedList<String> saveAll = new LinkedList<>();
    //hashmap to save cw to data into mongoDb//
    HashMap<String,String> save = new HashMap<>();
    //Trainstation object to retrieve data from TrainStation class//
    TrainStation trainStation = new TrainStation();
    public static void main(String[] args) {

    }

    public void LoadBookingData(Scanner sc) {
        //clearing the waiting room trainqueue linked lists and passenger arraylist//
        trainStation.waitingRoom.clear();
        trainStation.trainQueue.clear();
        passenger.clear();
        while (true) {
            System.out.println("Enter 1 to load data for Colombo to Badulla");
            System.out.println("Enter 2 to load data from Badulla to Colombo");
            int journey = sc.nextInt();
            try {
                if (journey == 1) {
                    //clearing the loading hashmap//
                    loading.clear();
                    /*Getting mongodb data to the console*/
                    Logger.getLogger("org.mongodb.driver").setLevel(Level.SEVERE);
                    MongoClient mClient = MongoClients.create();
                    MongoDatabase mongoDatabase = mClient.getDatabase("Journey-1");
                    MongoCollection mCollection2;
                    mCollection2 = mongoDatabase.getCollection("Colombo_to_Badulla");
                    mCollection2.find().forEach(new Block<Document>() {
                        @Override
                        public void apply(Document document) {
                            /*Getting the data from the document and putting them into hashmaps*/
                            Iterator iterator = document.entrySet().iterator();

                            while (iterator.hasNext()) {
                                HashMap.Entry input = (HashMap.Entry) iterator.next();
                                if (input.getKey().equals("_id")) {

                                } else {
                                    String seat = String.valueOf(input.getKey());
                                    String name = String.valueOf(input.getValue());
                                    loading.put(seat, name);
                                }
                            }
                        }

                    });

                    break;
                } else if (journey == 2) {
                    //clearing the loading hashmap//
                    loading.clear();
                    Logger.getLogger("org.mongodb.driver").setLevel(Level.SEVERE);
                    MongoClient mClient = MongoClients.create();
                    MongoDatabase mongoDatabase = mClient.getDatabase("Journey-2");
                    MongoCollection mCollection1;
                    mCollection1 = mongoDatabase.getCollection("Badulla_to_Colombo");
                    mCollection1.find().forEach(new Block<Document>() {
                        @Override
                        public void apply(Document document) {
                            /*Getting the data from the document and putting them into hashmaps*/
                            Iterator iterator = document.entrySet().iterator();


                            while (iterator.hasNext()) {
                                HashMap.Entry input = (HashMap.Entry) iterator.next();
                                if (input.getKey().equals("_id")) {
                                }
                                else {
                                    String seat = String.valueOf(input.getKey());
                                    String name = String.valueOf(input.getValue());
                                    loading.put(seat, name);
                                }
                            }

                        }

                    });
                    break;
                } else {
                    System.out.println("Invalid Input");
                }

            } catch (Exception e) {
                System.out.println("Error Identified");
            }
        }


        for (String name : loading.values()) {
            //getting the names passenger names from cw 1 and splitting the first name and last name to passsenger objects//
            String[] splited = String.valueOf(loading.values()).split("\\w+");
            if (name.split("\\w+").length > 1) {
                Passenger customerName = new Passenger();
                String surname = name.substring(name.lastIndexOf(" ") + 1);
                String firstName = name.substring(0, name.lastIndexOf(' '));
                customerName.firstName=firstName;
                customerName.lastName=surname;
                customerName.setName();
                passenger.add(customerName.getName());

            }

        }
        //checking whether the passenger queue if full or not//
        if (passenger.size() <= 42) {
            trainStation.waitingRoom.addAll(passenger);
        }
        else {
            System.out.println("Queue is full maximum 42 passengers are allowed please recheck");
            System.exit(0);
        }

    }

    //adding passenger from the waiting room to the train queue//
    public void addPassenger(Scanner sc, HashMap<String, String> loading) {

        //generating a random number through a six-sided die//
        Random rand = new Random();
        int dice = rand.nextInt(6);
        int diceValue = dice + 1;

        while (true) {
            try {
                //checking whether the waiting rooms is empty or not //
                if (trainStation.waitingRoom.isEmpty()) {
                    System.out.println("There are no more customers in the waiting room");
                    break;
                }
                //checking whether there are already added customers from the waiting room to the train queue//
                else if (save.containsValue(trainStation.waitingRoom.getFirst())){
                    trainStation.waitingRoom.removeFirst();
                }

                //Adding passengers from the waiting rooms to the train queue according to the dice value//
                else {
                    System.out.println("Dice value generated is" + diceValue);

                    if (diceValue == 1 && trainStation.waitingRoom.size() >= 1) {
                        trainStation.trainQueue.add(trainStation.waitingRoom.getFirst());
                        trainStation.waitingRoom.removeFirst();
                        System.out.println("1 customer added to the train queue");
                        break;

                    } else if (diceValue == 2 && trainStation.waitingRoom.size() >= 2) {
                        trainStation.trainQueue.add(trainStation.waitingRoom.getFirst());
                        trainStation.waitingRoom.removeFirst();
                        trainStation.trainQueue.add(trainStation.waitingRoom.getFirst());
                        trainStation.waitingRoom.removeFirst();
                        System.out.println("2 customers added to the train queue");
                        break;

                    } else if (diceValue == 3 && trainStation.waitingRoom.size() >= 3) {
                        trainStation.trainQueue.add(trainStation.waitingRoom.getFirst());
                        trainStation.waitingRoom.removeFirst();
                        trainStation.trainQueue.add(trainStation.waitingRoom.getFirst());
                        trainStation.waitingRoom.removeFirst();
                        trainStation.trainQueue.add(trainStation.waitingRoom.getFirst());
                        trainStation.waitingRoom.removeFirst();
                        System.out.println("3 customers added to the train queue");
                        break;

                    } else if (diceValue == 4 && trainStation.waitingRoom.size() >= 4) {
                        trainStation.trainQueue.add(trainStation.waitingRoom.getFirst());
                        trainStation.waitingRoom.removeFirst();
                        trainStation.trainQueue.add(trainStation.waitingRoom.getFirst());
                        trainStation.waitingRoom.removeFirst();
                        trainStation.trainQueue.add(trainStation.waitingRoom.getFirst());
                        trainStation.waitingRoom.removeFirst();
                        trainStation.trainQueue.add(trainStation.waitingRoom.getFirst());
                        trainStation.waitingRoom.removeFirst();
                        System.out.println("4 customers added to the train queue");
                        break;

                    } else if (diceValue == 5 && trainStation.waitingRoom.size() >= 5) {
                        trainStation.trainQueue.add(trainStation.waitingRoom.getFirst());
                        trainStation.waitingRoom.removeFirst();
                        trainStation.trainQueue.add(trainStation.waitingRoom.getFirst());
                        trainStation.waitingRoom.removeFirst();
                        trainStation.trainQueue.add(trainStation.waitingRoom.getFirst());
                        trainStation.waitingRoom.removeFirst();
                        trainStation.trainQueue.add(trainStation.waitingRoom.getFirst());
                        trainStation.waitingRoom.removeFirst();
                        trainStation.trainQueue.add(trainStation.waitingRoom.getFirst());
                        trainStation.waitingRoom.removeFirst();
                        System.out.println("5 customers added to the train queue");
                        break;

                    } else if (diceValue == 6 && trainStation.waitingRoom.size() >= 6) {
                        trainStation.trainQueue.add(trainStation.waitingRoom.getFirst());
                        trainStation.waitingRoom.removeFirst();
                        trainStation.trainQueue.add(trainStation.waitingRoom.getFirst());
                        trainStation.waitingRoom.removeFirst();
                        trainStation.trainQueue.add(trainStation.waitingRoom.getFirst());
                        trainStation.waitingRoom.removeFirst();
                        trainStation.trainQueue.add(trainStation.waitingRoom.getFirst());
                        trainStation.waitingRoom.removeFirst();
                        trainStation.trainQueue.add(trainStation.waitingRoom.getFirst());
                        trainStation.waitingRoom.removeFirst();
                        trainStation.trainQueue.add(trainStation.waitingRoom.getFirst());
                        trainStation.waitingRoom.removeFirst();
                        System.out.println("6 customers added to the train queue");
                        break;

                        //checking if the waiting rooms customers are less than the dice value if they are less the remaining customers are added to the train queue//
                    } else if ((diceValue == 2 || diceValue == 3 || diceValue == 4 || diceValue == 5 || diceValue == 6) && trainStation.waitingRoom.size() < 2) {
                        trainStation.trainQueue.add(trainStation.waitingRoom.getFirst());
                        trainStation.waitingRoom.removeFirst();
                        System.out.println("1 remaining customer added to the train queue");
                        break;

                    } else if ((diceValue == 3 || diceValue == 4 || diceValue == 5 || diceValue == 6) && trainStation.waitingRoom.size() < 3) {
                        trainStation.trainQueue.add(trainStation.waitingRoom.getFirst());
                        trainStation.waitingRoom.removeFirst();
                        trainStation.trainQueue.add(trainStation.waitingRoom.getFirst());
                        trainStation.waitingRoom.removeFirst();
                        System.out.println("2 remaining customers added to the train queue");
                        break;

                    } else if ((diceValue == 4 || diceValue == 5 || diceValue == 6) && trainStation.waitingRoom.size() < 4) {
                        trainStation.trainQueue.add(trainStation.waitingRoom.getFirst());
                        trainStation.waitingRoom.removeFirst();
                        trainStation.trainQueue.add(trainStation.waitingRoom.getFirst());
                        trainStation.waitingRoom.removeFirst();
                        trainStation.trainQueue.add(trainStation.waitingRoom.getFirst());
                        trainStation.waitingRoom.removeFirst();
                        System.out.println("3 remaining customers added to the train queue");
                        break;

                    } else if ((diceValue == 5 || diceValue == 6) && trainStation.waitingRoom.size() < 5) {
                        trainStation.trainQueue.add(trainStation.waitingRoom.getFirst());
                        trainStation.waitingRoom.removeFirst();
                        trainStation.trainQueue.add(trainStation.waitingRoom.getFirst());
                        trainStation.waitingRoom.removeFirst();
                        trainStation.trainQueue.add(trainStation.waitingRoom.getFirst());
                        trainStation.waitingRoom.removeFirst();
                        trainStation.trainQueue.add(trainStation.waitingRoom.getFirst());
                        trainStation.waitingRoom.removeFirst();
                        System.out.println("4 remaining customers added to the train queue");
                        break;

                    } else if (diceValue == 6 && trainStation.waitingRoom.size() < 6) {
                        trainStation.trainQueue.add(trainStation.waitingRoom.getFirst());
                        trainStation.waitingRoom.removeFirst();
                        trainStation.trainQueue.add(trainStation.waitingRoom.getFirst());
                        trainStation.waitingRoom.removeFirst();
                        trainStation.trainQueue.add(trainStation.waitingRoom.getFirst());
                        trainStation.waitingRoom.removeFirst();
                        trainStation.trainQueue.add(trainStation.waitingRoom.getFirst());
                        trainStation.waitingRoom.removeFirst();
                        trainStation.trainQueue.add(trainStation.waitingRoom.getFirst());
                        trainStation.waitingRoom.removeFirst();
                        System.out.println("5 remaining customers added to the train queue");
                        break;
                    }

                }
            } catch (Exception e) {
                System.out.println("Error Identified");
                break;
            }

        }

        //Creating the GUI to display the waiting room//
        Stage stage = new Stage();
        TilePane flowPane = new TilePane();


        Iterator iterator = loading.entrySet().iterator();
        Button okButton = new Button("Confirm");
        okButton.setPadding(new Insets(10, 20, 10, 20));
        okButton.setLayoutX(260);
        okButton.setLayoutY(650);
        okButton.setOnAction(event -> stage.close());

        //creating waiting room passenger names for those who have booked seats in cw 1//
        while (iterator.hasNext()) {
            Button button = new Button();
            button.setStyle("-fx-background-color:green;-fx-text-fill:white");
            HashMap.Entry input = (HashMap.Entry) iterator.next();
            button.setText(String.valueOf(input.getValue()));
            button.setId(String.valueOf(input.getValue()));
            button.setMaxWidth(Double.MAX_VALUE);
            if (trainStation.waitingRoom.isEmpty()) {
                button.setStyle("-fx-background-color:red;-fx-text-fill:white");
            } else if (!trainStation.waitingRoom.contains(button.getId())) {
                button.setStyle("-fx-background-color:red;-fx-text-fill:white");
            }

            flowPane.setHgap(10);
            flowPane.setVgap(20);
            flowPane.getChildren().add(button);

        }

        //adding all data to flowpane and displaying it in the gui//
        flowPane.getChildren().add(okButton);
        flowPane.setStyle("-fx-background-color:black;");
        flowPane.setPrefWidth(900);
        stage.setResizable(false);
        stage.setScene(new Scene(flowPane));
        stage.setTitle("Add Passenger");
        stage.showAndWait();


    }


    //displaying train queue on GUI//
    public void viewTrainQueue() {
        Stage stage = new Stage();

        StackPane stackPane = new StackPane();
        AnchorPane anchorPane = new AnchorPane();

        /*Assigning the set of seat buttons created within a for loop to a VBox*/
        VBox vBox1 = new VBox();
        for (int r = 1; r < 15; r = r + 1) {
            int number = r;
            String seat = "Seat";
            Button button = new Button(seat + number);
            button.setStyle("-fx-background-color:green;-fx-text-fill:white");
            button.setId(String.valueOf(r));
            button.setMaxWidth(Double.MAX_VALUE);
            vBox1.getChildren().addAll(button);
            vBox1.setSpacing(6);
            vBox1.setLayoutX(20);
            vBox1.setLayoutY(150);
        }


        VBox vBox4 = new VBox();
        for (int r = 1; r < 15; r = r + 1) {
            int number = r;
            String seat = "Seat";
            String seatNo = seat + number;
            Label nameLabel = new Label();
            nameLabel.setId(seatNo);
            /*Creating customer names/not arrived/not booked in labels against their seats*/
            if (trainStation.trainQueue.contains(loading.get(nameLabel.getId()))) {
                nameLabel.setText(loading.get(nameLabel.getId()));
                nameLabel.setStyle("-fx-font: normal bold 15 arial; -fx-text-fill: white;");

            } else if (!loading.containsKey(nameLabel.getId())) {
                nameLabel.setText("SEAT NOT BOOKED");
                nameLabel.setStyle("-fx-font: normal bold 15 arial; -fx-text-fill: red;");
            } else {
                nameLabel.setText("CUSTOMER NOT ARRIVED");
                nameLabel.setStyle("-fx-font: normal bold 15 arial; -fx-text-fill: orange;");
            }
            nameLabel.setMaxWidth(Double.MAX_VALUE);
            nameLabel.setMaxHeight(Double.MAX_VALUE);

            vBox4.getChildren().addAll(nameLabel);
            vBox4.setSpacing(20);
            vBox4.setLayoutX(100);
            vBox4.setLayoutY(155);
        }

        /*Assigning the set of seat buttons created within a for loop to a VBox*/
        VBox vBox2 = new VBox();
        for (int r = 15; r < 29; r = r + 1) {
            int number = r;
            String seat = "Seat";
            String seatNo = seat + number;
            Button button = new Button(seat + number);
            button.setStyle("-fx-background-color:green;-fx-text-fill:white");
            button.setMaxWidth(Double.MAX_VALUE);
            vBox2.getChildren().add(button);
            vBox2.setSpacing(6);
            vBox2.setLayoutX(400);
            vBox2.setLayoutY(150);
        }

        VBox vBox5 = new VBox();
        for (int r = 15; r < 29; r = r + 1) {
            int number = r;
            String seat = "Seat";
            String seatNo = seat + number;
            Label nameLabel = new Label();
            nameLabel.setId(seatNo);
            /*Creating customer names/not arrived/not booked in labels against their seats*/
            if (trainStation.trainQueue.contains(loading.get(nameLabel.getId()))) {
                nameLabel.setText(loading.get(nameLabel.getId()));
                nameLabel.setStyle("-fx-font: normal bold 15 arial; -fx-text-fill: white;");
            } else if (!loading.containsKey(nameLabel.getId())) {
                nameLabel.setText("SEAT NOT BOOKED");
                nameLabel.setStyle("-fx-font: normal bold 15 arial; -fx-text-fill: red;");
            } else {
                nameLabel.setText("CUSTOMER NOT ARRIVED");
                nameLabel.setStyle("-fx-font: normal bold 15 arial; -fx-text-fill: orange;");
            }
            nameLabel.setMaxWidth(Double.MAX_VALUE);
            nameLabel.setMaxHeight(Double.MAX_VALUE);
            vBox5.getChildren().addAll(nameLabel);
            vBox5.setSpacing(20);
            vBox5.setLayoutX(480);
            vBox5.setLayoutY(155);
        }

        /*Assigning the set of seat buttons created within a for loop to a VBox*/
        VBox vBox3 = new VBox();
        for (int r = 30; r < 43; r = r + 1) {
            int number = r;
            String seat = "Seat";
            Button button = new Button(seat + number);
            button.setStyle("-fx-background-color:green;-fx-text-fill:white");
            button.setMaxWidth(Double.MAX_VALUE);
            vBox3.getChildren().add(button);
            vBox3.setSpacing(6);
            vBox3.setLayoutX(780);
            vBox3.setLayoutY(150);
        }

        VBox vBox6 = new VBox();
        for (int r = 30; r < 43; r = r + 1) {
            int number = r;
            String seat = "Seat";
            String seatNo = seat + number;
            Label nameLabel = new Label();
            nameLabel.setId(seatNo);
            /*Creating customer names/not arrived/not booked in labels against their seats*/
            if (trainStation.trainQueue.contains(loading.get(nameLabel.getId()))) {
                nameLabel.setText(loading.get(nameLabel.getId()));
                nameLabel.setStyle("-fx-font: normal bold 15 arial; -fx-text-fill: white;");

            } else if (!loading.containsKey(nameLabel.getId())) {
                nameLabel.setText("SEAT NOT BOOKED");
                nameLabel.setStyle("-fx-font: normal bold 15 arial; -fx-text-fill: red;");
            } else {
                nameLabel.setText("CUSTOMER NOT ARRIVED");
                nameLabel.setStyle("-fx-font: normal bold 15 arial; -fx-text-fill: orange;");
            }
            nameLabel.setMaxWidth(Double.MAX_VALUE);
            nameLabel.setMaxHeight(Double.MAX_VALUE);
            vBox6.getChildren().addAll(nameLabel);
            vBox6.setSpacing(20);
            vBox6.setLayoutX(860);
            vBox6.setLayoutY(155);
        }

        //displaying the train queue GUI//
        anchorPane.getChildren().addAll(vBox1, vBox2, vBox3, vBox4, vBox5, vBox6);
        anchorPane.getStylesheets().addAll(PassengerQueue.class.getResource("style2.css").toExternalForm());
        stackPane.getChildren().add(anchorPane);
        stage.setScene(new Scene(stackPane, 1150, 750));
        anchorPane.setId("trainView");
        stage.setResizable(false);
        stage.setTitle("Train-Queue");
        stage.showAndWait();
    }

    //deleting passenger from train queue//
    public void deletePassenger(Scanner sc) {
        System.out.println("Enter the first name of the customer you want to delete");
        String firstName = sc.next().toUpperCase();
        System.out.println("Enter the last name of the customer you want to delete");
        String lastName = sc.next().toUpperCase();
        if (trainStation.trainQueue.contains(firstName + " " + lastName)) {
            trainStation.trainQueue.remove(firstName + " " + lastName);
        }
    }


    //running the stimulation and generating the report//
    public void runSimulation() throws InterruptedException, IOException {

        //Linked hashmap for storing the customer names and their waiting times//
        LinkedHashMap<String,Integer> timeSpent = new LinkedHashMap<>();

        //clearing trainqueue waiting room and passenger list//
        trainStation.waitingRoom.clear();
        trainStation.trainQueue.clear();
        trainStation.waitingRoom.addAll(passenger);


        Stage stage = new Stage();

        StackPane stackPane = new StackPane();
        AnchorPane anchorPane = new AnchorPane();

        //initializing the waiting time variable to zero//
        int waitingTime =0;
        while (true) {
            //Ending the process if there are no customers in the waiting room//
            if (trainStation.waitingRoom.isEmpty()) {
                System.out.println("There are no more customers in the waiting room");
                break;
            }

            else {
                try {
                    while (true) {
                        try {
                            //generating a value from a six sided die//
                            Random rand = new Random();
                            int dice = rand.nextInt(6);
                            int diceValue = dice + 1;


                            if (trainStation.waitingRoom.isEmpty()) {
                                System.out.println("There are no more customers in the waiting room");
                                break;

                            //adding customers to the train queue from the waiting room according to the dice value//
                            } else {
                                System.out.println("Dice value generated is" + " " + diceValue);

                                if (diceValue == 1 && trainStation.waitingRoom.size() >= 1) {
                                    trainStation.trainQueue.add(trainStation.waitingRoom.getFirst());
                                    trainStation.waitingRoom.removeFirst();
                                    System.out.println("1 customer added to the training queue");
                                    break;


                                } else if (diceValue == 2 && trainStation.waitingRoom.size() >= 2) {
                                    trainStation.trainQueue.add(trainStation.waitingRoom.getFirst());
                                    trainStation.waitingRoom.removeFirst();
                                    trainStation.trainQueue.add(trainStation.waitingRoom.getFirst());
                                    trainStation.waitingRoom.removeFirst();
                                    System.out.println("2 customers added to the training queue");
                                    break;

                                } else if (diceValue == 3 && trainStation.waitingRoom.size() >= 3) {
                                    trainStation.trainQueue.add(trainStation.waitingRoom.getFirst());
                                    trainStation.waitingRoom.removeFirst();
                                    trainStation.trainQueue.add(trainStation.waitingRoom.getFirst());
                                    trainStation.waitingRoom.removeFirst();
                                    trainStation.trainQueue.add(trainStation.waitingRoom.getFirst());
                                    trainStation.waitingRoom.removeFirst();
                                    System.out.println("3 customers added to the train queue");
                                    break;


                                } else if (diceValue == 4 && trainStation.waitingRoom.size() >= 4) {
                                    trainStation.trainQueue.add(trainStation.waitingRoom.getFirst());
                                    trainStation.waitingRoom.removeFirst();
                                    trainStation.trainQueue.add(trainStation.waitingRoom.getFirst());
                                    trainStation.waitingRoom.removeFirst();
                                    trainStation.trainQueue.add(trainStation.waitingRoom.getFirst());
                                    trainStation.waitingRoom.removeFirst();
                                    trainStation.trainQueue.add(trainStation.waitingRoom.getFirst());
                                    trainStation.waitingRoom.removeFirst();
                                    System.out.println("4 customers added to the train queue");
                                    break;

                                } else if (diceValue == 5 && trainStation.waitingRoom.size() >= 5) {
                                    trainStation.trainQueue.add(trainStation.waitingRoom.getFirst());
                                    trainStation.waitingRoom.removeFirst();
                                    trainStation.trainQueue.add(trainStation.waitingRoom.getFirst());
                                    trainStation.waitingRoom.removeFirst();
                                    trainStation.trainQueue.add(trainStation.waitingRoom.getFirst());
                                    trainStation.waitingRoom.removeFirst();
                                    trainStation.trainQueue.add(trainStation.waitingRoom.getFirst());
                                    trainStation.waitingRoom.removeFirst();
                                    trainStation.trainQueue.add(trainStation.waitingRoom.getFirst());
                                    trainStation.waitingRoom.removeFirst();
                                    System.out.println("5 customers added to the train queue");
                                    break;


                                } else if (diceValue == 6 && trainStation.waitingRoom.size() >= 6) {
                                    trainStation.trainQueue.add(trainStation.waitingRoom.getFirst());
                                    trainStation.waitingRoom.removeFirst();
                                    trainStation.trainQueue.add(trainStation.waitingRoom.getFirst());
                                    trainStation.waitingRoom.removeFirst();
                                    trainStation.trainQueue.add(trainStation.waitingRoom.getFirst());
                                    trainStation.waitingRoom.removeFirst();
                                    trainStation.trainQueue.add(trainStation.waitingRoom.getFirst());
                                    trainStation.waitingRoom.removeFirst();
                                    trainStation.trainQueue.add(trainStation.waitingRoom.getFirst());
                                    trainStation.waitingRoom.removeFirst();
                                    trainStation.trainQueue.add(trainStation.waitingRoom.getFirst());
                                    trainStation.waitingRoom.removeFirst();
                                    System.out.println("6 customers added to the train queue");
                                    break;


                                    //process when the dice value is larget than the waiting room passenger count//
                                } else if ((diceValue == 2 || diceValue == 3 || diceValue == 4 || diceValue == 5 || diceValue == 6) && trainStation.waitingRoom.size() < 2) {
                                    trainStation.trainQueue.add(trainStation.waitingRoom.getFirst());
                                    trainStation.waitingRoom.removeFirst();
                                    System.out.println("1 remaining customer added to train queue");
                                    break;

                                } else if ((diceValue == 3 || diceValue == 4 || diceValue == 5 || diceValue == 6) && trainStation.waitingRoom.size() < 3) {
                                    trainStation.trainQueue.add(trainStation.waitingRoom.getFirst());
                                    trainStation.waitingRoom.removeFirst();
                                    trainStation.trainQueue.add(trainStation.waitingRoom.getFirst());
                                    trainStation.waitingRoom.removeFirst();
                                    System.out.println("2 remaining customers added to train queue");
                                    break;

                                } else if ((diceValue == 4 || diceValue == 5 || diceValue == 6) && trainStation.waitingRoom.size() < 4) {
                                    trainStation.trainQueue.add(trainStation.waitingRoom.getFirst());
                                    trainStation.waitingRoom.removeFirst();
                                    trainStation.trainQueue.add(trainStation.waitingRoom.getFirst());
                                    trainStation.waitingRoom.removeFirst();
                                    trainStation.trainQueue.add(trainStation.waitingRoom.getFirst());
                                    trainStation.waitingRoom.removeFirst();
                                    System.out.println("3 remaining customers added to train queue");
                                    break;

                                } else if ((diceValue == 5 || diceValue == 6) && trainStation.waitingRoom.size() < 5) {
                                    trainStation.trainQueue.add(trainStation.waitingRoom.getFirst());
                                    trainStation.waitingRoom.removeFirst();
                                    trainStation.trainQueue.add(trainStation.waitingRoom.getFirst());
                                    trainStation.waitingRoom.removeFirst();
                                    trainStation.trainQueue.add(trainStation.waitingRoom.getFirst());
                                    trainStation.waitingRoom.removeFirst();
                                    trainStation.trainQueue.add(trainStation.waitingRoom.getFirst());
                                    trainStation.waitingRoom.removeFirst();
                                    System.out.println("4 remaining customers added to train queue");
                                    break;

                                } else if (diceValue == 6 && trainStation.waitingRoom.size() < 6) {
                                    trainStation.trainQueue.add(trainStation.waitingRoom.getFirst());
                                    trainStation.waitingRoom.removeFirst();
                                    trainStation.trainQueue.add(trainStation.waitingRoom.getFirst());
                                    trainStation.waitingRoom.removeFirst();
                                    trainStation.trainQueue.add(trainStation.waitingRoom.getFirst());
                                    trainStation.waitingRoom.removeFirst();
                                    trainStation.trainQueue.add(trainStation.waitingRoom.getFirst());
                                    trainStation.waitingRoom.removeFirst();
                                    trainStation.trainQueue.add(trainStation.waitingRoom.getFirst());
                                    trainStation.waitingRoom.removeFirst();
                                    System.out.println("5 remaining customers added to train queue");
                                    break;
                                }

                            }

                        } catch (Exception e) {
                            System.out.println("Error Identified");
                            break;
                        }

                    }

                        while (!trainStation.trainQueue.isEmpty()) {
                            //Generating values for the waiting time from 3 six sided die//
                            try {
                                Random rand1 = new Random();
                                int dice1 = rand1.nextInt(6);
                                int diceValue1 = dice1 + 1;

                                Random rand2 = new Random();
                                int dice2 = rand2.nextInt(6);
                                int diceValue2 = dice2 + 1;

                                Random rand3 = new Random();
                                int dice3 = rand3.nextInt(6);
                                int diceValue3 = dice3 + 1;

                                int totalDiceValue =diceValue1+diceValue2+diceValue3;

                                waitingTime = waitingTime +totalDiceValue;
                                int totalWaitingTime = waitingTime;

                                //Adding the waiting time to Passenger class through a passenger object//
                                Passenger timeWaiting = new Passenger();
                                timeWaiting.secondsInQueue = totalDiceValue;
                                timeWaiting.setSecondsInQueue();


                                //1 st passenger to the train queue has no waiting time//
                                if (trainStation.trainQueue.getFirst()==passenger.get(0)){
                                    timeSpent.put(trainStation.trainQueue.getFirst(),0);
                                    System.out.println(trainStation.trainQueue.getFirst()+" "+"BOARDING THE TRAIN");
                                }

                                else {
                                    System.out.println("Next customer waiting time is" + " " + totalDiceValue + " " + "seconds");
                                    //delaying the program in relevant to the waiting time//
                                    TimeUnit.SECONDS.sleep(timeWaiting.getSeconds());
                                    System.out.println(trainStation.trainQueue.getFirst() + " " + "BOARDING THE TRAIN");
                                    timeSpent.put(trainStation.trainQueue.getFirst(), totalWaitingTime);
                                }
                                //removing the customer who have boarded the train from the train queue//
                                trainStation.trainQueue.removeFirst();

                            } catch (Exception e) {
                                break;
                            }


                        }

                    } catch(Exception e){
                        break;
                    }
                }
            }


        System.out.println("All customers have boarded the train");

        //Creating the GUI and text file report//
        FileWriter writer = new FileWriter("Report.txt", true);
        writer.write("\r\n");
        writer.write("\r\n");
        writer.write("Stimulation Report");
        writer.write("\r\n");
        writer.write("\r\n");
        for (String name:timeSpent.keySet()) {
            writer.write("Customer"+" "+name+" "+"have spent"+" "+timeSpent.get(name)+"s"+" "+"in the waiting room");
            writer.write("\r\n");
        }

        VBox vBox1 = new VBox();
        for (String name:timeSpent.keySet()) {
            Label nameLabel = new Label();
            nameLabel.setText(name);
            nameLabel.setMaxWidth(Double.MAX_VALUE);
            nameLabel.setMaxHeight(Double.MAX_VALUE);
            nameLabel.setMaxHeight(20);
            nameLabel.setStyle("-fx-font: normal bold 18 arial; -fx-text-fill: red;");
            vBox1.getChildren().add(nameLabel);
        }
        vBox1.setSpacing(20);
        vBox1.setLayoutX(100);
        vBox1.setLayoutY(100);
        vBox1.setPadding(new Insets(0,0,50,10));
        Label nameList = new Label("Customer Name");
        nameList.setStyle("-fx-font: normal bold 20 arial; -fx-text-fill: yellow;");
        nameList.setLayoutY(50);
        nameList.setLayoutX(100);

        VBox vBox2 = new VBox();

            for (Integer time:timeSpent.values()) {
                Label timeLabel = new Label();
                timeLabel.setText(time+"s");
                timeLabel.setMaxWidth(Double.MAX_VALUE);
                timeLabel.setMaxHeight(Double.MAX_VALUE);
                timeLabel.setMaxHeight(20);
                timeLabel.setStyle("-fx-font: normal bold 18 arial; -fx-text-fill: red;");
                vBox2.getChildren().add(timeLabel);
            }

        vBox2.setSpacing(20);
        vBox2.setLayoutX(500);
        vBox2.setLayoutY(100);
        vBox2.setPadding(new Insets(0,0,50,10));
        Label timeList = new Label("Waiting Time");
        timeList.setStyle("-fx-font: normal bold 20 arial; -fx-text-fill: yellow;");
        timeList.setLayoutX(500);
        timeList.setLayoutY(50);

        Label queueLength = new Label("Max Queue Length:"+" "+passenger.size());
        queueLength.setLayoutX(800);
        queueLength.setLayoutY(130);
        queueLength.setPadding(new Insets(0,100,0,0));
        queueLength.setStyle("-fx-font: normal bold 20 arial; -fx-text-fill: orange;");
        writer.write("\r\n");
        writer.write("\r\n");
        writer.write("\r\n");
        writer.write("Max Queue Length of passengers:"+" "+passenger.size());
        writer.write("\r\n");

        LinkedList<Integer> time = new LinkedList<>();
        time.addAll(timeSpent.values());

        Label maxTime = new Label("Max waiting time:"+" "+ time.getLast()+"s");
        maxTime.setLayoutX(800);
        maxTime.setLayoutY(180);
        maxTime.setPadding(new Insets(0,100,0,0));
        maxTime.setStyle("-fx-font: normal bold 20 arial; -fx-text-fill: orange;");
        writer.write("Max waiting time of all passengers:"+" "+ time.getLast()+"s");
        writer.write("\r\n");

        Label minTime = new Label("Min waiting time:"+" "+ time.getFirst()+"s");
        minTime.setLayoutX(800);
        minTime.setLayoutY(230);
        minTime.setPadding(new Insets(0,100,0,0));
        minTime.setStyle("-fx-font: normal bold 20 arial; -fx-text-fill: orange;");
        writer.write("Min waiting time of all passengers:"+" "+ time.getFirst()+"s");
        writer.write("\r\n");

        Label avgTime = new Label("Average waiting time:"+" "+ time.getLast()/passenger.size()+"s");
        avgTime.setLayoutX(800);
        avgTime.setLayoutY(280);
        avgTime.setPadding(new Insets(0,100,0,0));
        avgTime.setStyle("-fx-font: normal bold 20 arial; -fx-text-fill: orange;");
        writer.write("Average waiting time of all passengers:"+" "+ time.getLast()/passenger.size()+"s");
        writer.write("\r\n");
        writer.write("\r\n");
        writer.write("\r\n");
        writer.close();

        //Displaying the GUI//
        anchorPane.getChildren().addAll(vBox1,vBox2,queueLength,maxTime,nameList,timeList,minTime,avgTime);
        anchorPane.setStyle("-fx-background-color:black;-fx-background-size: cover;");
        ScrollPane scrollPane= new ScrollPane(anchorPane);
        stackPane.getChildren().addAll(scrollPane);
        stackPane.setMaxWidth(1300);
        stage.setResizable(false);
        stage.setScene(new Scene(stackPane));
        stage.setTitle("Stimulation Report");
        stage.showAndWait();

    }

    public  void  saveData1(){
        String name = "name";

        saveAll.addAll(trainStation.trainQueue);
        while (true) {
            try{
                save.put(saveAll.getFirst(),saveAll.getFirst());
                saveAll.removeFirst();

                if (saveAll.size()==0){
                    break;
                }


            }catch (Exception e){
                break;
            }

        }

        /*Getting mongodb to the console and creating clients databases and collections appropriately*/
        java.util.logging.Logger.getLogger("org.mongodb.driver").setLevel(Level.SEVERE);
        MongoClient mClient = MongoClients.create();
        MongoDatabase mongoDatabase = mClient.getDatabase("Journey-1");
        MongoCollection mCollection1;
        mCollection1 = mongoDatabase.getCollection("Train Boarding");
        /*Creating a document an appending the hashmap data to the document*/
        Document document = new Document();
        save.forEach(new BiConsumer<String, String>() {
            @Override
            public void accept(String s, String s2) {
                document.append(s,s2);
            }
        });
        mCollection1.insertOne(document);
    }

    public  void  saveData2(){
        String name = "name";

        saveAll.addAll(trainStation.trainQueue);
        while (true) {
            try{
                save.put(saveAll.getFirst(),saveAll.getFirst());
                saveAll.removeFirst();

                if (saveAll.size()==0){
                    break;
                }


            }catch (Exception e){
                break;
            }

        }

        /*Getting mongodb to the console and creating clients databases and collections appropriately*/
        java.util.logging.Logger.getLogger("org.mongodb.driver").setLevel(Level.SEVERE);
        MongoClient mClient = MongoClients.create();
        MongoDatabase mongoDatabase = mClient.getDatabase("Journey-2");
        MongoCollection mCollection1;
        mCollection1 = mongoDatabase.getCollection("Train Boarding");
        /*Creating a document an appending the hashmap data to the document*/
        Document document = new Document();
        save.forEach(new BiConsumer<String, String>() {
            @Override
            public void accept(String s, String s2) {
                document.append(s,s2);
            }
        });
        mCollection1.insertOne(document);
    }

    public void loadData1(){
        /*Getting mongodb data to the console*/
        java.util.logging.Logger.getLogger("org.mongodb.driver").setLevel(Level.SEVERE);
        MongoClient mClient = MongoClients.create();
        MongoDatabase mongoDatabase = mClient.getDatabase("Journey-1");
        MongoCollection mCollection1;
        mCollection1 = mongoDatabase.getCollection("Train Boarding");
        mCollection1.find().forEach(new Block<Document>() {
            @Override
            public void apply(Document document) {
                /*Getting the data from the document and putting them into hashmaps*/
                Iterator iterator =document.entrySet().iterator();


                while (iterator.hasNext()){
                    HashMap.Entry input = (HashMap.Entry) iterator.next();
                    if(input.getKey().equals("_id")){

                    }
                    else {
                        save.put(String.valueOf(input.getKey()),String.valueOf(input.getValue()));
                        trainStation.trainQueue.add(String.valueOf(input.getValue()));
                    }
                }

            }
        });

    }

    public void loadData2(){
        /*Getting mongodb data to the console*/
        java.util.logging.Logger.getLogger("org.mongodb.driver").setLevel(Level.SEVERE);
        MongoClient mClient = MongoClients.create();
        MongoDatabase mongoDatabase = mClient.getDatabase("Journey-2");
        MongoCollection mCollection1;
        mCollection1 = mongoDatabase.getCollection("Train Boarding");
        mCollection1.find().forEach(new Block<Document>() {
            @Override
            public void apply(Document document) {
                /*Getting the data from the document and putting them into hashmaps*/
                Iterator iterator =document.entrySet().iterator();


                while (iterator.hasNext()){
                    HashMap.Entry input = (HashMap.Entry) iterator.next();
                    if(input.getKey().equals("_id")){

                    }
                    else {
                        save.put(String.valueOf(input.getKey()),String.valueOf(input.getValue()));
                        trainStation.trainQueue.add(String.valueOf(input.getValue()));
                    }
                }

            }
        });

    }


}


