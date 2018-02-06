import java.util.Random;

public class mainSerial {
    public static void main(String[] args){
        int nbTest = 500000000;
        int tiragesDansLeDisque = 0;
        Double x, y, result;
        Random rand = new Random();

        long startTime = System.currentTimeMillis();

        for(int i = 0; i < nbTest; i++){
            x = rand.nextDouble();
            y = rand.nextDouble();
            if(x * x + y * y <= 1)
                tiragesDansLeDisque++;
        }

        result = (1.0 * tiragesDansLeDisque)/nbTest;

        long stopTime = System.currentTimeMillis();
        long elapsedTime = stopTime - startTime;


        System.out.printf("Pi/4= %f\nCalculated in %d milliseconds", result, elapsedTime);
    }
}
