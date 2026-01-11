import java.util.Random;
public class Operation {
    private Random rndm;
    private double rateMutasi;
    private double crossoverRate;
    
    public Operation( int seed, double rateMutasi, double crossoverRate){
        this.rndm = new Random(seed);
        this.rateMutasi = rateMutasi;
        this.crossoverRate = crossoverRate;
    }
    public Kromosom Tournament(Population populasi){
        Kromosom best = populasi.getKromFromPopulation(rndm.nextInt(populasi.getSizePopulation()));
        int numOfParticipants = 3;
        for (int i= 0; i < numOfParticipants; i++) {
            Kromosom participant = populasi.getKromFromPopulation(rndm.nextInt(populasi.getSizePopulation()));
            if(participant.getNewFitness() < best.getNewFitness()){
                best=participant;
            }
        }
        return best;
    }
    public void crossover(Kromosom[] population, int startidx, Population populasi, int tipeCrossover){
        
        int index = startidx;

        while(index < population.length){
            double rndmValue = rndm.nextDouble();

            if(rndmValue < crossoverRate){

                Kromosom parent1 = Tournament(populasi);
                Kromosom parent2 = Tournament(populasi);
                //buat anak dengan isi gene kosong dan panjang gene sepanjang parent
                Kromosom anak1 = new Kromosom(parent1.length());
                Kromosom anak2 = new Kromosom(parent1.length());

                switch (tipeCrossover) {
                    case 1: // One-Point Crossover
                        onePointCrossover(parent1, parent2, anak1, anak2);
                        break;
                    case 2: // Uniform Crossover
                        uniformCrossover(parent1, parent2, anak1, anak2);
                        break;
                    default:
                        onePointCrossover(parent1, parent2, anak1, anak2);
                }

                population[index] = anak1;
                if(index + 1 < population.length){
                    population[index+1] = anak2;
                }
                index +=2;
            }
            else{//jika tidak terjadi crossover, gene nya ambil dari parent terbaik
                population[index] = Tournament(populasi);
                index++;
            }
        }
    }
    public void mutasi(Kromosom [] population, int startidx, int boardSize){

        for (int i = startidx; i < population.length; i++) {//loop cek kromosom pada populasi
            Kromosom cekKromosom = population[i];
            for (int j = 0; j < boardSize*boardSize; j++) {
                double rate = rndm.nextDouble();
                if(rate < rateMutasi){
                    if(cekKromosom.getGene(j) == 0){
                        cekKromosom.setGene(j, 1);
                    }
                    else{
                        cekKromosom.setGene(j, 0);
                    }
                }
            }
            
        }
    }

    public void onePointCrossover(Kromosom parent1, Kromosom parent2, Kromosom anak1, Kromosom anak2){
        int point = 1 + rndm.nextInt(parent1.length()-1);// di tambah 1 karena dalam 1 kromosom minimal ada 1 gene yang di crossover
        //set gene 1 per 1 sampai ke point crossovernya
        for (int i = 0; i < parent1.length(); i++) {
            if(i < point){
                anak1.setGene(i, parent1.getGene(i));
                anak2.setGene(i, parent2.getGene(i));
            }
            else{
                anak1.setGene(i, parent2.getGene(i));
                anak2.setGene(i, parent1.getGene(i));
            }
        }

        anak1.konversiFitness();
        anak2.konversiFitness();
        //repairChromosom(anak1);
        //repairChromosom(anak2);
    }


    private void uniformCrossover(Kromosom parent1, Kromosom parent2, Kromosom anak1, Kromosom anak2) {
        // Set setiap gene 1 per 1 secara acak dari kedua parent dengan probabilitas seragam (0.5)
        for (int i = 0; i < parent1.length(); i++) {
            if(rndm.nextBoolean()){
                anak1.setGene(i, parent1.getGene(i));
                anak2.setGene(i, parent2.getGene(i));
            }
            else{
                anak1.setGene(i, parent2.getGene(i));
                anak2.setGene(i, parent1.getGene(i));
            }
        }

        anak1.konversiFitness();
        anak2.konversiFitness();
        //repairChromosom(anak1);
        //repairChromosom(anak2);
    }
}
