
//Kromosom -> satu konfigurasi puzzle mosaic 
//Gene -> satu sel mosaic  warna / tile ID
import java.util.Random;

public class Kromosom implements Comparable<Kromosom> {
    protected int[] genes;
    protected int fitness=1;

    public Kromosom(int[] genes) {
        this.genes = genes.clone();
    }

    // Set seed random di awal eksperimen
    // public static void setSeed(long seed) {
    //     rand = new Random(seed);
    // }
    public Kromosom(int panjangKromosom){
        this.genes = new int[panjangKromosom];
    }

    public Kromosom(int panjangKromosom, Random rand) {
        this.genes = new int[panjangKromosom];
        //inisiasi kromosom untuk populasi awal dengan random
         for (int i = 0; i < panjangKromosom; i++) {
            this.genes[i] = rand.nextInt(2); // 0 atau 1
        }
    }
    //untuk weighted

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
    
    public int getNewFitness(){ //isi sesudah ada fitness
        if (this.fitness==1) this.fitness=fitness();
        return this.fitness;
    }
    public void konversiFitness(){ //isi sesudah ada fitness
        this.fitness = fitness();
    }

    public int fitness(){
        Layout layout = Main.layout;
        int total=0;
        for (int i=0;i<layout.request.length;i++){
            for (int j=0;j<layout.request[i].length;j++){
                if (layout.request[i][j]==-1) continue;
                int deviation =layout.request[i][j];
                for (int k=i-1;k<=i+1;k++){
                    for (int l=j-1;l<=j+1;l++){
                        if (k>=0&&k<layout.request.length&&l>=0&&l<layout.request[i].length) deviation-=this.genes[k*layout.request[i].length+l];
                    }
                }
                total+=Math.abs(deviation);
            }
        }
        return -total;
    }

}
