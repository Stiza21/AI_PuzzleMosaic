import java.util.Random;
public class Operation {
    private Random rndm;
    private double rateMutasi;
    private double crossoverRate;
    
    public Operation(Random rndm, int seed, double rateMutasi, double crossoverRate){
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
    public void crossover(Kromosom[] population, int startidx, Population populasi){
        
        int index = startidx;

        while(index < population.length){
            double rndmValue = rndm.nextDouble();

            if(rndmValue < crossoverRate){

                Kromosom parent1 = Tournament(populasi);
                Kromosom parent2 = Tournament(populasi);
                //buat anak dengan isi gene kosong dan panjang gene sepanjang parent
                Kromosom anak1 = new Kromosom(parent1.length());
                Kromosom anak2 = new Kromosom(parent1.length());

                int point = 1 + rndm.nextInt(parent1.length()-1);// di tambah 1 karena dalam 1 kromosom minimal ada 1 gene yang di crossover
                //set gene 1 per 1 sampai ke point crossovernya
                for (int i = 0; i < parent1.length(); i++) {
                    if(i < point){
                        anak1.setGene(i, parent1.getGene(index));
                        anak2.setGene(i, parent2.getGene(index));
                    }
                    else{
                        anak1.setGene(i, parent2.getGene(index));
                        anak2.setGene(i, parent1.getGene(index));
                    }
                }
                anak1.konversiFitness();
                anak2.konversiFitness();
                //repairChromosom(anak1);
                //repairChromosom(anak2);

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

}
