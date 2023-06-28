//======================================================================================================================
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Scanner;
//----------------------------------------------------------------------------------------------------------------------
public class BoatFleet {
//----------------------------------------------------------------------------------------------------------------------
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        ArrayList<Boat> boatList=Boat.getBoatList();

        if (args.length!=0) {
            try {
                boatList = readCSVFileToList(args[0]);
                Boat.setBoatList(boatList);

            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        } else {
            try {
                boatList=readFleetData("C:\\Users\\asus\\FleetData.db");
                Boat.setBoatList(boatList);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                System.out.println("File not found ! Exiting!");
            }
        }

        System.out.print("Welcome to the Fleet Management System\r\n" +
                "--------------------------------------\r\n");

        String userChoice = "A";
        while(!userChoice.equalsIgnoreCase("X")) {
                    System.out.print("\r\n" + "(P)rint, (A)dd, (R)emove, (E)xpense, e(X)it : ");

            userChoice = input.nextLine().toUpperCase();
            switch(userChoice)
            {
                case "P":
                    System.out.println("Fleet report:");
                    for(Boat boat:boatList)
                        System.out.println(boat.toString());
                    System.out.println("Total\t\t\t\t\t\t\t: Paid $" + Boat.getPaidTotal() + ":Spent $" + Boat.getSpentTotal());
                    break;

                case "A":
                    System.out.print("Please enter the new boat CSV data : ");
                    String [] strArr=input.nextLine().split(",");
                    BoatType boatType=strArr[0].equalsIgnoreCase("POWER")?BoatType.POWER:BoatType.SAILING;
                    Boat boat=new Boat(boatType, strArr[1], Integer.parseInt(strArr[2]), strArr[3], Double.parseDouble(strArr[4]), Double.parseDouble(strArr[5]), 0);
                    boatList.add(boat);
                    break;

                case "R":
                    System.out.print("Which boat do you want to remove? : ");
                    String boatname=input.nextLine();
                    Boat boatToRTemove=null;
                    boolean boatExists=false;
                    for (Boat boatO:boatList) {
                        if (boatO.getName().equalsIgnoreCase(boatname)) {
                            boatExists = true;
                            boatToRTemove = boatO;
                        }
                    }
                    if (!boatExists)
                        System.out.println("Cannot find boat " + boatname);
                    else
                        boatList.remove(boatToRTemove);
                    break;

                case "E":
                    System.out.print("Which boat do you want to spend on? : ");
                    boatname = input.nextLine();
                    boatExists = false;
                    double amt = 0;
                    Boat boatToUpdate = null;

                    for (Boat boatO:boatList) {
                        if (boatO.getName().equalsIgnoreCase(boatname)) {
                            boatExists = true;
                            System.out.print("How much do you want to spend? : ");
                            amt = input.nextDouble();
                            input.nextLine();
                            boatToUpdate = boatO;
                        }
                    }
                    if (!boatExists)
                        System.out.println("Cannot find boat " + boatname);
                    else {
                        if (boatToUpdate.checkExpense(boatToUpdate.getExpense() + amt)) {
                            boatList.remove(boatToUpdate);
                            boatToUpdate.setExpense(boatToUpdate.getExpense() + amt);
                            boatList.add(boatToUpdate);
                            System.out.println("Expense authorized, $" + boatToUpdate.getExpense() + " spent.");
                        }
                    }
                    break;

                case "X":
                    System.out.println("Exiting the Fleet Management System");
                    writeSerializedFleetData(boatList);
                    break;

                default:
                    System.out.println("Invalid menu option, try again");
            }
        }

    }
//----------------------------------------------------------------------------------------------------------------------
   // method to read CSV file
    private static ArrayList<Boat> readCSVFileToList(String fileName) throws NumberFormatException {

        ArrayList<Boat> boatList=new ArrayList<>();
        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader(fileName));
            String line = null;

            while ((line = br.readLine()) != null) {
                String [] lines = line.split(",");
                BoatType boatType=lines[0].equalsIgnoreCase("POWER")?BoatType.POWER:BoatType.SAILING;
                Boat boat = new Boat(boatType, lines[1], Integer.parseInt(lines[2]), lines[3], Double.parseDouble(lines[4]), Double.parseDouble(lines[5]), 0);
                boatList.add(boat);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return boatList;
    }
//----------------------------------------------------------------------------------------------------------------------
  // method to read the DB file
    private static ArrayList<Boat> readFleetData(String fileName) throws IOException, ClassNotFoundException {

        ArrayList<Boat> boatList=new ArrayList<>();
        try {
            FileInputStream fileIn = new FileInputStream(fileName);
            ObjectInputStream objectIn = new ObjectInputStream(fileIn);
            Object obj = null;

            while((obj = objectIn.readObject())!=null) {
                boatList.add((Boat)obj);
            }
            objectIn.close();
        } catch (Exception e) {}

        return boatList;
    }
//----------------------------------------------------------------------------------------------------------------------
   // method to serialize the data
    private static void writeSerializedFleetData(ArrayList<Boat> boatList) {

        File secondPeopleFile = new File("C:\\Users\\asus\\FleetData.db");
        ObjectOutputStream writeObj = null;
        try {
            writeObj = new ObjectOutputStream(new FileOutputStream(secondPeopleFile));

            for(Boat boatO:boatList) {
                writeObj.writeObject(boatO);
            }

            writeObj.close();
        } catch (IOException e)
        {
            System.out.println("Problems writing to file");
            e.printStackTrace();
        }
    }
//----------------------------------------------------------------------------------------------------------------------
}
//======================================================================================================================
