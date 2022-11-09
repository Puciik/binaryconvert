import java.util.Locale;
import java.util.Scanner;

public class Main {

    static private final Scanner scan = new Scanner(System.in);
    static private boolean mainFlag;
    static private double input_d;
    static private long input_i;
    static public int move;
    static private int sign;
    static public int amountOfZeros = 0;
    static public int amountOfSame = 0;

    static {
        mainFlag = false;
    }

    public static void main(String[] args) {

        while (!mainFlag) {
            showMainQuestion();
            scannerInit(Locale.ENGLISH);
            try {
                chooseAnalysis(scan.nextInt());
            } catch (Exception IOException) {
                wrong();
                String s = scan.nextLine();
            }
        }
    }

    private static void scannerInit(Locale locale) {
        Scanner scan = new Scanner(System.in).useLocale(locale);
    }

    private static void showMainQuestion() {
        System.out.println("Make your choice:");
        System.out.println("Type \"1\". Integer number");
        System.out.println("Type \"2\". Float number");
        System.out.println("Type \"3\". Exit program");
        System.out.print("Number: ");
    }

    private static void chooseAnalysis(int choice) {
        switch (choice) {
            case 1 -> inputIntegerNumber();
            case 2 -> inputFloatNumber();
            case 3 -> mainFlag = true;
        }
    }

    private static void wrong() {
        System.out.println("Error!");
    }

    private static void inputFloatNumber() {
        boolean flagDouble = false;
        boolean inputChange;
        String baseInput = scan.nextLine();

        while (!flagDouble) {
            System.out.println();
            System.out.print("Input float number: ");
            baseInput = scan.nextLine();
            inputChange = readFloatNumber(baseInput, Locale.CANADA);
            if (!inputChange)
                inputChange = readFloatNumber(baseInput, Locale.FRANCE);
            if (inputChange) {
                System.out.println("float: " + toCorrectBinFormat32bit(input_d));
                System.out.println("double: " + toCorrectBinFormat64bit(input_d));
                flagDouble = true;
            }
            else wrong();
        }
    }

    private static boolean readFloatNumber(String input, Locale locale) {
        boolean inputChange = false;
        Scanner sc_ca = new Scanner(input).useLocale(locale);
        try {
            input_d = sc_ca.nextDouble();
            inputChange = true;
        } catch (Exception ignored) {
        }
        return inputChange;
    }

    private static void inputIntegerNumber() {
        boolean intFlag = false;
        while (!intFlag) {
            System.out.println();
            System.out.print("Input integer number: ");
            try {
                input_i = scan.nextLong();
                intFlag = true;
                amountOfSame = 0;
            } catch (Exception e) {
                amountOfSame++;
                if (amountOfSame == 5) {
                    System.out.println();
                    System.out.println("ты серьёзно?");
                    System.out.println();
                    mainFlag = false;
                    amountOfSame = 0;
                } else {
                    wrong();
                }
                String s = scan.nextLine();
            }
        }
        toCorrectBinaryInteger();
    }

    private static String mantissa(double num, int length) {
        String big = (toBinaryInteger((long) num));
        move = big.length() - 1;
        int coefficientSmall = (int) Math.pow(10, (String.valueOf(num).length() - String.valueOf((int) num).length() - 1));
        String small = "";
        if ((num - (int) num) != 0) small = toBinaryFractional((int) Math.round((num % 1) * coefficientSmall));
        String result = big.substring(1) + small;
        while (result.length() < length) {
            result += "0";
        }
        if (result.length() > length) result = result.substring(0, length - 1);
        return result;
    }

    public static String toBinaryInteger(long num) {
        gettingSign(num);
        if (sign == 1) num *= -1;
        StringBuilder res = new StringBuilder();
        while (num > 0) {
            res.insert(0, num % 2);
            num = num / 2;
        }
        return String.valueOf(res);
    }

    public static void toCorrectBinaryInteger() {
        System.out.println();
        long orig = input_i;
        StringBuilder result = new StringBuilder(toBinaryInteger(input_i));
        int len = result.length();
        amountOfZeros += result.length();
        gettingSign(input_i);
        if (len <= 8) {
            System.out.println("byte:" + sign + addZeros(8, result));
        } if (len <= 16) {
            System.out.println("short:" + sign + addZeros(16, result));
        } if (len <= 32) {
            System.out.println("int:" + sign + addZeros(32, result));
        } if (len <= 64) {
            System.out.println("long:" + sign + addZeros(64, result));
        } if (orig > 3.4E-38) {
            System.out.println("float: " + toCorrectBinFormat32bit(Double.parseDouble(String.valueOf(orig))));
        }
        System.out.println("double: " + toCorrectBinFormat64bit(Double.parseDouble(String.valueOf(orig))));
    }

    public static StringBuilder addZeros(int size, StringBuilder result) {
        size--;
        int amountOfIterations = size - amountOfZeros;
        for (int i = 0; i < amountOfIterations; i++) {
            result.insert(0, "0");
            amountOfZeros++;
        }
        return result;
    }

    public static String toBinaryFractional(int origNum) {
        double num = Double.parseDouble("0." + origNum);
        StringBuilder res = new StringBuilder();

        for (int i = 0; i < 23 - move; i++) {
            res.append(String.valueOf(num * 2).charAt(0));
            num = num * 2;
            num = num - (int) num;
        }
        return res.toString();
    }


    private static String toCorrectBinFormat32bit(double input) {

        gettingSign(Double.parseDouble(String.valueOf(input)));
        if (sign == 1) {
            input *= -1;
        }
        StringBuilder mantissa = new StringBuilder(mantissa(input, 23));
        String exponent = toBinaryInteger(127 + move);

        return sign + " " + exponent + " " + mantissa;
    }

    private static String toCorrectBinFormat64bit(double input) {

        gettingSign(Double.parseDouble(String.valueOf(input)));
        if (sign == 1) {
            input *= -1;
        }
        StringBuilder mantissa = new StringBuilder(mantissa(input, 52));
        String exponent = toBinaryInteger(1023 + move);

        return sign + " " + exponent + " " + mantissa;
    }

    private static void gettingSign(double num) {
        sign = num < 0 ? 1: 0;
    }
}