//Kromosom -> satu konfigurasi puzzle mosaic 
//Gene -> satu sel mosaic  warna / tile ID

public class Kromosom implements Comparable<Kromosom> {
    private int[] genes;

    public Kromosom(int[] genes) {
        this.genes = genes;
    }

    public Kromosom(int PanjangKromosom) {
        this.genes = new int[PanjangKromosom];
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
        return new Kromosom(genes);
    }

    @Override
    public int compareTo(Kromosom o) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'compareTo'");
    }
    
}
