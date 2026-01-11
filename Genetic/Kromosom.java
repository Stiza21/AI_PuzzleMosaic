
//Kromosom -> satu konfigurasi puzzle mosaic 
//Gene -> satu sel mosaic  warna / tile ID
import java.util.Random;

public class Kromosom implements Comparable<Kromosom> {
    private int[] genes;
    private int fitness;
    private static Random rand;

    public Kromosom(int[] genes) {
        this.genes = genes.clone();
    }

    // Set seed random di awal eksperimen
    public static void setSeed(long seed) {
        rand = new Random(seed);
    }

    public Kromosom(int PanjangKromosom) {
        this.genes = new int[PanjangKromosom];

        //inisiasi kromosom untuk populasi awal dengan random
         for (int i = 0; i < PanjangKromosom; i++) {
            genes[i] = rand.nextInt(2); // 0 atau 1
        }
    }

    public int getGene(int idx) {
        return genes[idx];
    }

    public void setGene(int index, int value) {
        if (value != 0 && value != 1) {
            throw new IllegalArgumentException(
                "Nilai gen harus 0 (putih) atau 1 (hitam)"
            );
        }
        genes[index] = value;
    }

    public int length() {
        return genes.length;
    }

    public int[] getGenes() {
        return genes.clone();
    }

     public Kromosom copy() {
        Kromosom clone = new Kromosom(this.genes);
        clone.fitness = this.fitness;
        return clone;
    }

    @Override
    public int compareTo(Kromosom o) {
        return Integer.compare(this.fitness, o.fitness);
    }
    
    public double getNewFitness(){ //isi sesudah ada fitness
        return 0.0;
    }
    public double konversiFitness(){ //isi sesudah ada fitness
        return 0.0;
    }
}
