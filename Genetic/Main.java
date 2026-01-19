import java.io.File;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Main {
    //line 85 tipe crossover
    //operation selection function pilih 1
    //operation line 140 tipe mutasi
    static boolean[] isFixed;
    static int[] fixedValues;
    static Layout layout;
    static Random rd;
    static int boardSize;
    public static void main(String[] args) {
        String[] puzzles ={
            "5x5puzHard-8,150,315.txt",
            "5x5puzHard-9,779,048.txt",
            "10x10puzHard-3,042,336.txt",
            "10x10puzHard-4,359,451.txt",
            "10x10puzHard-4,359,451.txt",
            "15x15puzHard-2,321,406.txt",
            "15x15puzHard-4,515,219.txt",
            "20x20puzHard-4,287,083.txt",
            "20x20puzHard-9,839,579.txt",
        };
        int[] seeds={130,12521431,12222};
        int[] populasiAwals = {1000,5000,10000};
        int[] banyakGenerasis = {1000, 5000, 10000};
        double[] rateMutasis = {0.00150,0.002,0.003};
        double[] crossoverRates = {0.9,0.5,0.13};
        double[] elitismRates = {0.1,0.3,0.6};

        for (String soal:puzzles){
            try {
                boardSize=0;
                File filePuzzle = new File(soal);
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
                //input logic
                int totalGen = boardSize * boardSize;
                isFixed = new boolean[totalGen];
                fixedValues = new int[totalGen];
                applyHeuristics(request, boardSize);
                layout = new Layout(request.toArray(new int[boardSize][]));
                scPuzzle.close();
            } catch (Exception e) {
                System.out.println("salah path soal");
            }
            for (int seed:seeds){
                System.out.printf("soal %s seed%d baseline\n", soal, seed);
                GA(seed,populasiAwals[1], banyakGenerasis[1], rateMutasis[1],crossoverRates[1], elitismRates[1]);
                System.out.printf("soal %s seed%d populasiRendah\n", soal, seed);
                GA(seed,populasiAwals[0], banyakGenerasis[1], rateMutasis[1],crossoverRates[1], elitismRates[1]);
                System.out.printf("soal %s seed%d populasiTinggi\n", soal, seed);
                GA(seed,populasiAwals[2], banyakGenerasis[1], rateMutasis[1],crossoverRates[1], elitismRates[1]);
                System.out.printf("soal %s seed%d generasiSedikit\n", soal, seed);
                GA(seed,populasiAwals[1], banyakGenerasis[0], rateMutasis[1],crossoverRates[1], elitismRates[1]);
                System.out.printf("soal %s seed%d generasiBanyak\n", soal, seed);
                GA(seed,populasiAwals[1], banyakGenerasis[2], rateMutasis[1],crossoverRates[1], elitismRates[1]);
                System.out.printf("soal %s seed%d mutasiRendah\n", soal, seed);
                GA(seed,populasiAwals[1], banyakGenerasis[1], rateMutasis[0],crossoverRates[1], elitismRates[1]);
                System.out.printf("soal %s seed%d mutasiTinggi\n", soal, seed);
                GA(seed,populasiAwals[1], banyakGenerasis[1], rateMutasis[2],crossoverRates[1], elitismRates[1]);
                System.out.printf("soal %s seed%d crossoverRendah\n", soal, seed);
                GA(seed,populasiAwals[1], banyakGenerasis[1], rateMutasis[1],crossoverRates[0], elitismRates[1]);
                System.out.printf("soal %s seed%d crossoverTinggi\n", soal, seed);
                GA(seed,populasiAwals[1], banyakGenerasis[1], rateMutasis[1],crossoverRates[2], elitismRates[1]);
                System.out.printf("soal %s seed%d elitismRendah\n", soal, seed);
                GA(seed,populasiAwals[1], banyakGenerasis[1], rateMutasis[1],crossoverRates[1], elitismRates[0]);
                System.out.printf("soal %s seed%d elitismTinggi\n", soal, seed);
                GA(seed,populasiAwals[1], banyakGenerasis[1], rateMutasis[1],crossoverRates[1], elitismRates[2]);
                
            }
        }
    }
    public static void GA(int seed, int populasiAwal, int banyakGenerasi,double rateMutasi, double crossoverRate, double elitismRate){
        rd = new Random(seed);
        Operation op = new Operation(rd,rateMutasi,crossoverRate,elitismRate,isFixed);
        //pembuatan populasi awal
        Population population = new Population(boardSize,populasiAwal);
        
        //ganti sesuai encoding yang dipakai
        population.generatePop(boardSize*boardSize,rd,isFixed,fixedValues);
        //population.generateWeighted(boardSize*boardSize,rd);
        
        Kromosom best = population.getBest();
        int bestFitness = best.getNewFitness();
        for (int generasi = 0;generasi<banyakGenerasi;generasi++){
            //System.out.println(banyakGenerasi+" generasi tersisa");

            //perlu specify make tipe crossover yang mana
            population = op.crossover(population, 1);
            //population = op.blameCrossover(population, 0);

            Kromosom generationBest = population.getBest();
            if (generationBest.getNewFitness()>bestFitness){
                best = generationBest;
                bestFitness = best.getNewFitness();
            }
            if (bestFitness==0) break;

        }
        System.out.printf("best Found with seed %d: fitness %d\n", seed,bestFitness);
        int[] bestGenes = best.getGenes();
        for (int i=0;i<boardSize;i++){
            for (int j=0;j<boardSize;j++){
                System.out.print(bestGenes[i*boardSize+j]+" ");
            }
            System.out.println();
        }
        System.out.println();
    }
    public static void applyHeuristics(ArrayList<int[]> grid, int size) {
        for (int r = 0; r < size; r++) {
            for (int c = 0; c < size; c++) {
                int val = grid.get(r)[c];
                if (val == -1) continue;

                if (val == 0) {
                    fillFixed(r, c, 0, size); // Sekitar angka 0 pasti kosong 0
                } else if (val == 9) {
                    fillFixed(r, c, 1, size); // Sekitar angka 9 pasti berisi 1
                } else if (val == 4 && isCorner(r, c, size)) {
                    fillFixed(r, c, 1, size); // Angka 4 di sudut sisanya pasti 1
                } else if (val == 6 && isEdge(r, c, size)) {
                    fillFixed(r, c, 1, size); // Angka 6 di sisi siasnya pasti 1
                }
            }
        }
    }

    private static void fillFixed(int r, int c, int val, int size) {
        for (int i = r - 1; i <= r + 1; i++) {
            for (int j = c - 1; j <= c + 1; j++) {
                if (i >= 0 && i < size && j >= 0 && j < size) {
                    int index = i * size + j;
                    isFixed[index] = true;
                    fixedValues[index] = val;
                }
            }
        }
    }

    private static boolean isCorner(int r, int c, int size) {
        return (r == 0 || r == size - 1) && (c == 0 || c == size - 1);
    }

    private static boolean isEdge(int r, int c, int size) {
        if (isCorner(r, c, size)) return false;
        return r == 0 || r == size - 1 || c == 0 || c == size - 1;
    }
}
