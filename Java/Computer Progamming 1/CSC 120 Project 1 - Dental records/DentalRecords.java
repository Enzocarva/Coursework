import java.util.Scanner;
//======================================================================================================================
public class DentalRecords {
    //----------------------------------------------------------------------------------------------------------------------
    private static final Scanner keyboard = new Scanner(System.in);
    private static final int NUM_PEOPLE_MAX = 6;
    private static final int TEETH_ROWS = 2;
    private static final int MAX_TEETH = 8;

    //----------------------------------------------------------------------------------------------------------------------
    public static void main(String[] args) {

        //----Variables
        String[] names;
        int numPeople;
        int[][] numTeeth;
        char[][][] teethInfo;
        char menuOption;

        //----Print welcome message
        System.out.println("Welcome to the Floridian Tooth Records");
        System.out.println("--------------------------------------");

        //----Get number of people in family
        System.out.print("Please enter number of people in the family : ");
        numPeople = keyboard.nextInt();
        while (numPeople < 0 || numPeople > NUM_PEOPLE_MAX) {
            System.out.print("Invalid number of people, try again         : ");
            numPeople = keyboard.nextInt();
        }
        //----Establish arrays
        names = new String[numPeople];
        numTeeth = new int[2][numPeople];
        teethInfo = new char[numPeople][TEETH_ROWS][MAX_TEETH];

        //----Input data
        getData(names, numPeople, numTeeth, teethInfo);

        //----Menu options
        System.out.println();
        System.out.print("(P)rint, (E)xtract, (R)oot, e(X)it         : ");
        menuOption = keyboard.next().charAt(0);
        while (menuOption != 'X' && menuOption != 'x') {
            switch (menuOption) {
                case 'P':
                case 'p':
                    printMenuOption(names, numPeople, numTeeth, teethInfo);
                    System.out.print("\n(P)rint, (E)xtract, (R)oot, e(X)it          : ");
                    break;
                case 'E':
                case 'e':
                    extractMenuOption(names, numPeople, numTeeth, teethInfo);
                    System.out.print("\n(P)rint, (E)xtract, (R)oot, e(X)it          : ");
                    break;
                case 'R':
                case 'r':
                    rootMenuOption(names, numPeople, numTeeth, teethInfo);
                    System.out.print("\n(P)rint, (E)xtract, (R)oot, e(X)it          : ");
                    break;
                default:
                    System.out.print("Invalid menu option, try again             : ");
            }
            menuOption = keyboard.next().charAt(0);
        }
        System.out.print("\nExiting the Floridian Tooth Records :-)");
    }

    //----------------------------------------------------------------------------------------------------------------------
    public static void getData(String[] namesFam, int numFamMembers, int[][] numberOfTeeth, char[][][] teethInformation) {

        int index, i;
        String upperRow;
        String lowerRow;

        //----Get names for string array, get string for upper and lower rows
        for (index = 0; index < numFamMembers; index++) {
            System.out.print("Please enter the name for family member " + (index + 1) + "   : ");
            namesFam[index] = keyboard.next();

            upperRow = getRowsOfTeeth(index, namesFam[index], "uppers");
            numberOfTeeth[0][index] = upperRow.length();
            for (i = 0; i < upperRow.length(); i++) {
                teethInformation[index][0][i] = upperRow.charAt(i);
            }
            lowerRow = getRowsOfTeeth(index, namesFam[index], "lowers");
            numberOfTeeth[1][index] = lowerRow.length();
            for (i = 0; i < lowerRow.length(); i++) {
                teethInformation[index][1][i] = lowerRow.charAt(i);
            }

        }
    }

    //----------------------------------------------------------------------------------------------------------------------
    public static String getRowsOfTeeth(int index, String name, String upperOrLower) {

        String teethRow;
        boolean verification = true;
        int lengthCheck = 0;

        System.out.printf("Please enter the %s for %-8s       : ", upperOrLower, name);
        teethRow = keyboard.next();

        //----Verification check and length check of input
        verification = verificationCheck(teethRow);
        lengthCheck = teethRow.length();
        while (!verification || lengthCheck > MAX_TEETH) {
            if (!verification) {
                System.out.print("Invalid teeth types, try again              : ");
            } else {
                System.out.print("Too many teeth, try again                   : ");
            }
            teethRow = keyboard.next();
            lengthCheck = teethRow.length();
            verification = verificationCheck(teethRow);
        }
        teethRow = teethRow.toUpperCase();
        return teethRow;
    }

    //----------------------------------------------------------------------------------------------------------------------
    //----Ensures input is one of the tooth type options
    public static boolean verificationCheck(String teethLine) {

        boolean verificationTeeth = false;
        int index;

        for (index = 0; index < teethLine.length(); index++) {
            switch (teethLine.charAt(index)) {
                case 'I':
                case 'i':
                case 'B':
                case 'b':
                case 'M':
                case 'm':
                    verificationTeeth = true;
                    break;
                default:
                    verificationTeeth = false;
            }

        }
        return verificationTeeth;
    }

    //----------------------------------------------------------------------------------------------------------------------
    //----Executes "Print" menu option
    public static void printMenuOption(String[] namesFam, int numFamMembers, int[][] numberOfTeeth, char[][][] teethInformation) {

        int numFamIndex, teethIndex;

        for (numFamIndex = 0; numFamIndex < numFamMembers; numFamIndex++) {
            System.out.println(namesFam[numFamIndex]);
            System.out.print("  Uppers:  ");
            for (teethIndex = 0; teethIndex < numberOfTeeth[0][numFamIndex]; teethIndex++) {
                System.out.printf("%3d:%S", (teethIndex + 1), teethInformation[numFamIndex][0][teethIndex]);
            }
            System.out.println();
            System.out.print("  Lowers:  ");
            for (teethIndex = 0; teethIndex < numberOfTeeth[1][numFamIndex]; teethIndex++) {
                System.out.printf("%3d:%S", (teethIndex + 1), teethInformation[numFamIndex][1][teethIndex]);
            }
            System.out.println();
        }
    }
    //----------------------------------------------------------------------------------------------------------------------
    public static void extractMenuOption(String[] namesFam, int numFamMembers, int[][] numberOfTeeth, char[][][] teethInformation) {

        String famMember;
        int index;
        boolean exists = false;
        boolean rowUOrL = false;
        int memberNum = 0;
        int toothNum;
        int toothLine = 0;
        char toothRow;

        System.out.print("Which family member                         : ");
        famMember = keyboard.next();

        //----Verification of family member name
        do{
            for (index = 0; index < numFamMembers; index++) {
                if (famMember.equalsIgnoreCase(namesFam[index])) {
                    exists = true;
                    memberNum = index;
                }
            }
            if (!exists) {
                System.out.print("Invalid family member, try again            : ");
                famMember = keyboard.next();
            }
        }while (!exists);

        //----Get tooth row and number
        System.out.print("Which tooth layer (U)pper or (L)ower        : ");
        toothRow = keyboard.next().charAt(0);
        do {
            switch (toothRow) {
                case 'U':
                case 'u':
                    rowUOrL = true;
                    toothLine = 0;
                    break;
                case 'L':
                case 'l':
                    rowUOrL = true;
                    toothLine = 1;
                    break;
                default:
                    System.out.print("Invalid layer, try again                    : ");
                    toothRow = keyboard.next().charAt(0);
            }
        }while (!rowUOrL);

        System.out.print("Which tooth number                          : ");
        toothNum = keyboard.nextInt();

        //----verification of tooth, ensure it isn't a missing tooth
        while (toothNum > numberOfTeeth[toothLine][memberNum] || toothNum <= 0 || teethInformation[memberNum][toothLine][toothNum -1] == 'M') {
            if (toothNum > numberOfTeeth[toothLine][memberNum] || toothNum <= 0) {
                System.out.print("Invalid tooth number, try again             : ");
            } else if (teethInformation[memberNum][toothLine][toothNum -1] == 'M') {
                System.out.print("Missing tooth, try again                    : ");
            }
            toothNum = keyboard.nextInt();
        }
        teethInformation[memberNum][toothLine][toothNum -1] = 'M';
    }
    //----------------------------------------------------------------------------------------------------------------------
    public static void rootMenuOption(String[] namesFam, int numFamMembers, int[][] numberOfTeeth, char[][][] teethInformation) {

        double a, b, c;
        int numFamIndex, teethIndex, rowIndex;
        double root1, root2;
        double result;
        double totalI = 0.0;
        double totalB = 0.0;
        double totalM = 0.0;

        //----Calculate root canal (formula = Ix^2 +Bx - M)
        for (numFamIndex = 0; numFamIndex < numFamMembers; numFamIndex++) {
            for (rowIndex = 0; rowIndex < TEETH_ROWS; rowIndex++) {
                for (teethIndex = 0; teethIndex < numberOfTeeth[rowIndex][numFamIndex]; teethIndex++) {
                    switch (teethInformation[numFamIndex][rowIndex][teethIndex]) {
                        case 'I':
                            totalI += 1;
                            break;
                        case 'B':
                            totalB += 1;
                            break;
                        case 'M':
                            totalM += 1;
                            break;
                        default:
                            System.out.print("Error in reading teeth information   : ");

                    }
                }
            }
        }
        a = totalI;
        b = totalB;
        c = -1 * totalM;

        result = b * b - 4.0 * a * c;
        if (result == 0.0) {
            root1 = root2 = -b / (2.0 * a);
            System.out.printf("One root canal at %.2f\n", root1);
            System.out.println();
        }else if (result > 0.0) {
            root1 = (-b + Math.pow(result, 0.5)) / (2.0 * a);
            root2 = (-b - Math.pow(result, 0.5)) / (2.0 * a);
            System.out.printf("One root canal at %.2f\nAnother root canal at %.2f", root1, root2);
            System.out.println();
        }else {
            System.out.println("No result");
        }
    }
}