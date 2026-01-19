import java.io.File;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;

public class Main {
    static boolean[] isFixed;
    static int[] fixedValues;
    static Layout layout;
    static Random rd;
    public static void main(String[] args) {
        int seed=0,populasiAwal=0, banyakGenerasi=0;
        double rateMutasi=0,crossoverRate=0,elitismRate=0;
        int boardSize=0;
        //input logic
        try {
            File fileParam = new File("param.txt");
            Scanner scParam = new Scanner(fileParam);
            seed = Integer.parseInt(scParam.nextLine().strip().substring(6));
            populasiAwal = Integer.parseInt(scParam.nextLine().strip().substring(14));
            banyakGenerasi = Integer.parseInt(scParam.nextLine().strip().substring(16));
            rateMutasi = Double.parseDouble(scParam.nextLine().strip().substring(11));
            crossoverRate = Double.parseDouble(scParam.nextLine().strip().substring(14));
            elitismRate = Double.parseDouble(scParam.nextLine().strip().substring(12));

            scParam.close();
            File filePuzzle = new File("puzzle.txt");
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
        int totalGen = boardSize * boardSize;
        isFixed = new boolean[totalGen];
        fixedValues = new int[totalGen];

        applyHeuristics(request, boardSize);
            scPuzzle.close();
        } catch (Exception e) {
            System.out.println(e);
        }

        System.out.printf("using seed %d crossover rate:%f mutation rate:%f\n", seed,crossoverRate,rateMutasi);

        //pembuatan objek random dan operasi
        rd = new Random(seed);
        Operation op = new Operation(rd,rateMutasi,crossoverRate,elitismRate,isFixed);
        //pembuatan populasi awal
        Population population = new Population(boardSize,populasiAwal);
        
        //ganti sesuai encoding yang dipakai
        population.generatePop(boardSize*boardSize,rd,isFixed,fixedValues);
        //population.generateWeighted(boardSize*boardSize,rd);
        
        Kromosom best = population.getBest();
        int bestFitness = best.getNewFitness();
        while(banyakGenerasi-->0){
            //System.out.println(banyakGenerasi+" generasi tersisa");

            //perlu specify make tipe crossover yang mana
            population = op.crossover(population, 1);
            //population = op.blameCrossover(population, 0);

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
