// -*- coding: utf-8 -*-

import java.awt.*;
import java.util.concurrent.*;

public class Mandelbrot {

    public static class TracerLigne implements Runnable{
        private int lineIndex;
        private final CountDownLatch startSignal;
        private final CountDownLatch doneSignal;

        public TracerLigne(int lineIndex, CountDownLatch startSignal, CountDownLatch doneSignal) {
            this.lineIndex = lineIndex;
            this.startSignal = startSignal;
            this.doneSignal = doneSignal;
        }

        public TracerLigne(int lineIndex){
            this.lineIndex = lineIndex;
            startSignal = null;
            doneSignal = null;
        }

        public int getLineIndex() {
            return lineIndex;
        }

        public void setLineIndex(int lineIndex) {
            this.lineIndex = lineIndex;
        }

        public CountDownLatch getStartSignal() {
            return startSignal;
        }

        public CountDownLatch getDoneSignal() {
            return doneSignal;
        }

        private void work(){
            for (int j = 0; j < taille; j++) {
                colorierPixel(j, lineIndex);
            }
        }

        @Override
        public void run() {
            //Question 4
            /*try {
                startSignal.await();
                work();
                doneSignal.countDown();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/
            work();
        }
    }

    final static Color noir =  new Color(0, 0, 0);
    final static Color blanc =  new Color(255, 255, 255);
    final static int taille = 500;   // nombre de pixels par ligne (et par colonne)
    // Il y a donc taille*taille pixels blancs ou noirs à déterminer
    final static Picture image = new Picture(taille, taille);

    final static double xc   = -.5 ;
    final static double yc   = 0 ; // Le point (xc,yc) est le centre de l'image
    final static double region = 2;
    // La région du plan considérée est un carré de côté égal à 2.
    // Elle s'étend donc du point (xc - 1, yc - 1) au point (xc + 1, yc + 1)
    // c'est-à-dire du point (-1.5, -1) en bas à gauche au point (0.5, 1) en haut
    // à droite

    final static int max = 20_000; 
    // C'est le nombre maximum d'itérations pour déterminer la couleur d'un pixel

    public static void main(String[] args)  {
        ExecutorService executeur = Executors.newFixedThreadPool(4);

        CountDownLatch startSignal = new CountDownLatch(0);
        CountDownLatch doneSignal = new CountDownLatch(taille);

        ExecutorCompletionService executorCompletionService = new ExecutorCompletionService(executeur);

        final long startTime = System.nanoTime();
        final long endTime;

        for (int i = 0; i < taille; i++) {
            //executeur.execute(new TracerLigne(i, startSignal, doneSignal));
            executorCompletionService.submit(new TracerLigne(i), null); //Question 5
        }


        for (int i = 0; i < taille; i++) {
            try {
                executorCompletionService.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        //Question 3
        //executeur.shutdown();
        //while(!executeur.isTerminated());

        //Question 4
/*      startSignal.countDown();
        try {
            doneSignal.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
*/



        endTime = System.nanoTime();
        final long duree = (endTime - startTime) / 1_000_000 ;
        System.out.println("Durée = " + (long) duree + " ms.");
        image.show();
    }    

    // La fonction colorierPixel(i,j) colorie le pixel (i,j) de l'image
    public static void colorierPixel(int i, int j) {
        double a = xc - region/2 + region*i/taille;
        double b = yc - region/2 + region*j/taille;
        // Le pixel (i,j) correspond au point (a,b)
        if (mandelbrot(a, b, max)) image.set(i, j, noir);
        else image.set(i, j, blanc); 
    }

    // La fonction mandelbrot(a, b, max) détermine si le point (a,b) est noir
    public static boolean mandelbrot(double a, double b, int max) {
        double x = 0;
        double y = 0;
        for (int t = 0; t < max; t++) {
            if (x*x + y*y > 4.0) return false; // Le point (a,b) est blanc
            double nx = x*x - y*y + a;
            double ny = 2*x*y + b;
            x = nx;
            y = ny;
        }
        return true; // Le point (a,b) est noir
    }
}


/* 
  $ javac Mandelbrot.java
  $ java Mandelbrot
  Durée = 17831 ms.
  $ make
  javac *.java 
  jar cvmf MANIFEST.MF Mandelbrot.jar *.class 
  ...
  $ java -jar Mandelbrot.jar
  Durée = 17869 ms.
*/
