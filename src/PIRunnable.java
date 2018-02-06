import java.util.Random;

public class PIRunnable implements Runnable{
    public int nbTirage;
    public int nbPoint;

    double piOver4;

    PIRunnable(int nbTirage)
    {
        this.nbTirage = nbTirage;
        this.nbPoint = 0;
    }

    @Override
    public void run() {

        double x, y;

        Random rand = new Random();

        for(int i = 0; i < nbTirage; i++){
            x = rand.nextDouble();
            y = rand.nextDouble();

            if(x*x+y*y <= 1)
                nbPoint++;
        }
        piOver4 = (1.0 * nbPoint)/nbTirage;
    }

    public double getPiOver4() {
        return piOver4;
    }
}
