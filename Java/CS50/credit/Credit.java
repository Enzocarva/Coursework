//======================================================================================================================
import java.util.Scanner;
//----------------------------------------------------------------------------------------------------------------------
public class Credit {
//----------------------------------------------------------------------------------------------------------------------
    private static final Scanner keyboard = new Scanner(System.in);
//----------------------------------------------------------------------------------------------------------------------
    public static void main(String[] args) {

        long cardNum = 0;
        long[] cardDigits;
        int cardLength, index;
        String cardType;

        do {
            System.out.print("Enter credit card number : ");
            cardNum = keyboard.nextLong();
        } while (cardNum <= 0);

        cardLength = String.valueOf(cardNum).length();
        cardDigits = new long [cardLength];

        for (index = cardLength - 1; index >= 0; index--) {
            cardDigits[index] = cardNum % 10;
            cardNum = cardNum / 10;
        }

        cardType = checkCard(cardLength, cardDigits);
        System.out.println(cardType);
    }
//----------------------------------------------------------------------------------------------------------------------
    public static String checkCard(int length, long[] digits) {

        int index, i, multSum = 0, halfLength, totalSum = 0, nonDouble;
        halfLength = length / 2;
        long doubleDigit[] = new long [length];
        String card = "";

        for (index = length - 2, i = 0; index >= 0; index -= 2, i++) {
            doubleDigit[i] = digits [index] * 2;
        }

        for (index = 0; index <= ((length - 2) / 2); index++) {
            if (doubleDigit[index] > 9) {
                multSum = multSum + (int)((doubleDigit[index] / 10) + (doubleDigit[index] % 10));
            } else {
                multSum = multSum + (int)doubleDigit[index];
            }
        }

        for (index = length - 1; index >= 0; index -= 2) {
            nonDouble = (int)digits[index];
            totalSum = totalSum + nonDouble;
        }

        if ((multSum + totalSum) % 10 == 0) {
            switch((int)digits[0]) {
                case 3:
                    if (digits[1] == 3 || digits[1] == 7) {
                        card = "AMEX";
                    }
                    break;
                case 4:
                    card = "VISA";
                    break;
                case 5:
                    if (digits[1] == 1 || digits[1] == 2 || digits[1] == 3 || digits[1] == 4 || digits[1] == 5) {
                        card = "MASTERCARD";
                    }
                    break;
                default:
                    System.out.println("ERROR");
                    break;
            }
        } else {
            card = "INVALID";
        }
    return(card);
    }
}
