

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Personality {

    public static final int INVALID = -1;
    public static final int HALF_POINT = 50;
    public static final int AMOUNT_OF_PERSONALITY = 4;
    public static final String NO_ANSWER = "X";
    public static final String NO_PERSONALITY = "-";

    // The main method to process the data from the personality tests
    public static void main(String[] args) {
        // do not make any other Scanners connected to System.in
        Scanner keyboard = new Scanner(System.in);
        Scanner fileScanner = getFileScanner(keyboard);
        fileProcessing (fileScanner);
        fileScanner.close();
        keyboard.close();
    }


    //Process the data with a for loop depending on
    //the amount of user, which is displayed on the first line
    public static void fileProcessing (Scanner fileScanner) {
        int max = fileScanner.nextInt() ;
        fileScanner.nextLine();  //Skipping the hidden
        for (int i = 0; i < max; i++) {
            String name = fileScanner.nextLine();
            String answers = fileScanner.nextLine().toUpperCase();
            answer (answers, name);
        }
    }


    //Use the list of answers from the file to compute the letters
    //into the amount of A and B to
    //an array of the different personality type
    public static void answer (String answers, String name) {
        int differentBetweenInterval = 7;
        int[] answerA = new int[AMOUNT_OF_PERSONALITY];
        int[] answerB = new int[AMOUNT_OF_PERSONALITY];

        //Looping through each of the group of questions
        //There are 7 questions for the 10 groups which equals to 70 questions total
        for (int interval  = 0; interval < answers.length() ; interval += differentBetweenInterval) {
            String sevenQuestions = answers.substring(interval, interval + differentBetweenInterval);
            introvertExtrovert (sevenQuestions, answerA, answerB);
            sortingQuestionsIndices (sevenQuestions,answerA, answerB);
        }
        endResult(answerA, answerB, name);
    }


    //Compute for the Introvert/Extrovert which is in the 0 index of the indices.
    public static void introvertExtrovert (String sevenQuestions, int[] answerA, int[] answerB) {
        if (sevenQuestions.charAt(0) == 'A') {
            answerA[0]++;
        } else if (sevenQuestions.charAt(0) == ('B')) {
            answerB[0]++;
        }
    }

    //Sort the ones that has 2 questions within the 7 string of question
    //with different indices into the array
    public static void sortingQuestionsIndices (String sevenQuestions, int[] answerA, int[] answerB) {
        int indexOfEachQuestion = 1;
        for (int j = 1; j <= 3; j++) {
            if (sevenQuestions.charAt(indexOfEachQuestion) == 'A') {
                answerA[j]++;
            } else if (sevenQuestions.charAt(indexOfEachQuestion) == 'B') {
                answerB[j]++;
            }
            indexOfEachQuestion++;
            if (sevenQuestions.charAt(indexOfEachQuestion) == 'A') {
                answerA[j]++;
            } else if (sevenQuestions.charAt(indexOfEachQuestion) == 'B') {
                answerB[j]++;
            }
            indexOfEachQuestion++;
        }
    }

    //Calculate the B percentage
    public static int[] calculatePercentageB (int[] answerA, int[] answerB) {
        final int PERCENTAGE = 100;
        int[] percent = new int [AMOUNT_OF_PERSONALITY];
        for (int i = 0; i < answerA.length; i++) {
            double total = answerA[i]+answerB[i];
            if (total == 0) {
                percent[i] = INVALID;
            } else {
                percent[i] = (int) Math.round(answerB[i] / total * PERCENTAGE);
            }
        }
        return percent;
    }


    //Convert the values from percent and matches
    //with the letter (personality type) depending
    //on the percentage.
    public static String converter (int[] percent) {
        String resultConverter = "";
        String eSTJ = "ESTJ";
        String iNFP = "INFP";
        for (int i = 0; i < percent.length; i++) {
            if (percent[i] > HALF_POINT) {
                resultConverter += iNFP.charAt(i);
            } else if (percent[i] == INVALID){
                resultConverter += NO_PERSONALITY;
            } else if (percent[i] < HALF_POINT) {
                resultConverter += eSTJ.charAt(i);
            } else if (percent[i] == HALF_POINT) {
                resultConverter += NO_ANSWER;
            }
        }
        return resultConverter;
    }


    //Prints out the result using the methods above along with
    //exception such as no answers
    public static void endResult (int[] answerA, int[] answerB, String name) {
        int[] percentageB = calculatePercentageB (answerA, answerB);
        String result = converter (percentageB);
        System.out.printf("%30s: ", name);
        for (int i = 0; i < percentageB.length; i++) {
            if (percentageB[i] != INVALID) {
                System.out.printf("%10d ", percentageB[i]);
            } else {
                System.out.print("NO ANSWERS ");
            }
        }
        System.out.println("= " + result);

    }


    // Method to choose a file.
    // Asks user for name of file. 
    // If file not found create a Scanner hooked up to a dummy set of data
    public static Scanner getFileScanner(Scanner keyboard){
        Scanner result = null;
        try {
            System.out.print("Enter the name of the file with"
                    + " the personality data: ");
            String fileName = keyboard.nextLine().trim();
            System.out.println();
            result = new Scanner(new File(fileName));
        } catch(FileNotFoundException e) {
            System.out.println("Problem creating Scanner: " + e);
            System.out.println("Creating Scanner hooked up to default data " 
                    + e);
            String defaultData = "1\nDEFAULT DATA\n"
                + "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA"
                + "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA";
            result = new Scanner(defaultData);
        }
        return result;
    }
}
