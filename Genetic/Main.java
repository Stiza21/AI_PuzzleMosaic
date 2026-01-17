import java.io.File;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Main {
    static Layout layout;
    static Random rd;
    public static void main(String[] args) {
        int seed=0,populasiAwal=0, banyakGenerasi=0;
        double rateMutasi=0,crossoverRate=0,elitismRate=0;
        int boardSize=0;
        //input logic
        try {
            File fileParam = new File("Genetic/param.txt");
            Scanner scParam = new Scanner(fileParam);
            seed = Integer.parseInt(scParam.nextLine().strip().substring(6));
            populasiAwal = Integer.parseInt(scParam.nextLine().strip().substring(14));
            banyakGenerasi = Integer.parseInt(scParam.nextLine().strip().substring(16));
            rateMutasi = Double.parseDouble(scParam.nextLine().strip().substring(11));
            crossoverRate = Double.parseDouble(scParam.nextLine().strip().substring(14));
            elitismRate = Double.parseDouble(scParam.nextLine().strip().substring(12));

            scParam.close();
            File filePuzzle = new File("Genetic/puzzle.txt");
            Scanner scPuzzle = new Scanner(filePuzzle);
            ArrayList<int[]> request = new ArrayList<>();
            while(scPuzzle.hasNext()){
                String[] baris = scPuzzle.nextLine().strip().split(" ");
                boardSize++;
                int[] line = new int[baris.length];
                for (int i=0;i<baris.length;i++){
                    if (baris[i].equals("."))line[i]=-1;
                    else line[i]=Integer.parseInt(baris[i]);
                }
                request.add(line);
            }

            layout = new Layout(request.toArray(new int[boardSize][]));
            scPuzzle.close();
        } catch (Exception e) {
            System.out.println(e);
        }

        System.out.printf("using seed %d crossover rate:%f mutation rate:%f\n", seed,crossoverRate,rateMutasi);
        //pembuatan objek random dan operasi
        rd = new Random(seed);
        Operation op = new Operation(rd,rateMutasi,crossoverRate,elitismRate);
        //pembuatan populasi awal
        Population population = new Population(boardSize);
        population.generatePop(boardSize*boardSize,rd);
        Kromosom best = population.getBest();
        int bestFitness = best.getNewFitness();
        while(banyakGenerasi-->0){
            //perlu specify make tipe crossover yang mana
            population = op.crossover(population,  1);
            Kromosom generationBest = population.getBest();
            if (generationBest.getNewFitness()>bestFitness){
                best = generationBest;
                bestFitness = best.getNewFitness();
            }
            //System.out.println(banyakGenerasi+" generations left");
        }
        System.out.printf("best Found with seed %d: fitness %d\n", seed,bestFitness);
        int[] bestGenes = best.getGenes();
        for (int i=0;i<boardSize;i++){
            for (int j=0;j<boardSize;j++){
                System.out.print(bestGenes[i*boardSize+j]+" ");
            }
            System.out.println();
        }
    }
}
