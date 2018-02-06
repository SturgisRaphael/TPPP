public class main {

    public static void main(String[] args){
        int nbThread = 10;
        int nbTest = 500000000;

        Thread [] T = new Thread[nbThread];
        PIRunnable [] R = new PIRunnable[nbThread];

        for(int i = 0; i < nbThread; i++){
            R[i] = new PIRunnable(nbTest/nbThread);
            T[i] = new Thread(R[i]);
        }

        long startTime = System.currentTimeMillis();

        for(int i = 0; i < nbThread; i++){
            T[i].start();
        }

        double result = 0;

        for(int i = 0; i < nbThread; i++){
            try {
                T[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            result += R[i].getPiOver4();
        }

        long stopTime = System.currentTimeMillis();
        long elapsedTime = stopTime - startTime;

        System.out.printf("Pi/4= %f\nCalculated in %d milliseconds", (result/nbThread), elapsedTime);
    }
}
