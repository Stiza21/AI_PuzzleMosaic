import java.util.Random;
public class Operation {
    private Random rndm;
    private double rateMutasi;
    private double crossoverRate;
    private double elitismRate;
    private boolean[] isFixed;
    
    public Operation(Random rndm, double rateMutasi, double crossoverRate,double elitismRate,boolean[] isFixed){
        this.rndm = rndm;
        this.rateMutasi = rateMutasi;
        this.crossoverRate = crossoverRate;
        this.elitismRate=elitismRate;
        this.isFixed = isFixed;
    }
    public Kromosom selectionFunction(Population populasi){
        //return rouletteWheelSelection(populasi);
        //return rankSelection(populasi);
        return tournamentSelection(populasi);
    }
    public Kromosom tournamentSelection(Population populasi){
        Kromosom best = populasi.getKromFromPopulation(rndm.nextInt(populasi.getSizePopulation()));
        int numOfParticipants = 3;
        for (int i= 0; i < numOfParticipants-1; i++) {//dilakukan -1 agar jumlah total participantnya 3 karena best sudah terhitung sebagai participant(line 18)
            Kromosom participant = populasi.getKromFromPopulation(rndm.nextInt(populasi.getSizePopulation()));
            if(participant.getNewFitness() > best.getNewFitness()){
                best=participant;
            }
        }
        return best;
    }


    public Kromosom rouletteWheelSelection(Population populasi){
         double minFitness = Double.MAX_VALUE;//nilai minimum yang akan digunakan untuk melakukan shiift agar bobot bernilai positif

    // Cari fitness terendah (paling negatif)
    for(int i = 0; i < populasi.getSizePopulation(); i++){
        double f = populasi.getKromFromPopulation(i).getNewFitness();
        if(f < minFitness) minFitness = f;
    }
        
        double totalFitness = 0.0;

        //hitung total fitness karena lebih kecil lebih baik maka fungsinya dibalik(lebih kecil nilai fitness lebih baik kromosom)
        for(int i = 0 ;i<populasi.getSizePopulation();i++){
           double weight = populasi.getKromFromPopulation(i).getNewFitness() - minFitness + 1;//dilakukan +1 agar bobot tidak [ernah berni;ao 0 dengan kromosom terburuk bernilai 1 dan kromosom terbaik bernilai besar
            totalFitness += weight;
        }

        //Ambil nilai acak dianatara 0 dan total fitness
        double rand = rndm.nextDouble()*totalFitness;
        //nilai yang akan digunakan untuk memilih kromosom
        double proporsi = 0.0;

        //iterasi untuk memilih kromosom
        for(int i = 0;i<populasi.getSizePopulation();i++){
            Kromosom krom = populasi.getKromFromPopulation(i);
             double weight = krom.getNewFitness() - minFitness + 1;
              proporsi +=weight;
            //jika nilai proporsi sudah melebihi random maka kromosom itulah yang dipilih
            if(proporsi >= rand){
                return krom;
            }
        }

        //fallback bila error terjadi
        return populasi.getKromFromPopulation(populasi.getSizePopulation()-1);//diambil paling akhir karena di awal akan digunakan untuk elitism
    }

    //ASUMSINYA ADALAH SEMUA KROMOSOM SUDAH TERURUT BERDASARKAN FITNESS 
    public Kromosom rankSelection(Population populasi){
        int n =populasi.getSizePopulation();

        //menghitung total bobot seleksi dengan bobot tertinggi bernilai n dan yang terendah berbobot 1
        int totalRank = n*(n+1)/2;

        int rand = rndm.nextInt(totalRank)+1;//di +1 agar rentang nilainya menjadi 1<= x <= totalRank

        int proporsi = 0;
        for(int i = 0;i<n;i++){
            int rank = n-i;//mulai dari yang memiliki nilai fitness terbaik memiliki rank sebanyak n hingga yang terburuk memiliki rank 1
            proporsi +=rank;

            if(proporsi>=rand){
                return populasi.getKromFromPopulation(i);
            }
        }

        //fallback untuk error
        return populasi.getKromFromPopulation(n-1);
    }


    public Population crossover(Population population, int tipeCrossover){
        Kromosom[] newPop = new Kromosom[population.getSizePopulation()];
        population.sortByFitnessDesc();
        int index = Math.max(0, (int)(elitismRate * population.getSizePopulation()));//elitism dengan asumsi tidak pernah 0
        // asumsi populasi sudah terurut dari terbaik ke terburuk
        for(int i = 0; i < index; i++){
        newPop[i] = population.getKromFromPopulation(i).copy();
        }

        while(index < population.getSizePopulation()){
            double rndmValue = rndm.nextDouble();

            Kromosom parent1 = selectionFunction(population);
            Kromosom parent2 = selectionFunction(population);
            if(rndmValue < crossoverRate){

                //buat anak dengan isi gene kosong dan panjang gene sepanjang parent
                Kromosom anak1 = new Kromosom(parent1.length());
                Kromosom anak2 = new Kromosom(parent1.length());

                switch (tipeCrossover) {
                    case 1: // One-Point Crossover
                        onePointCrossover(parent1, parent2, anak1, anak2);
                        break;
                    case 2: //Two-Point Crossover
                        twoPointCrossover(parent1, parent2, anak1, anak2);
                        break;
                    case 3: // Uniform Crossover
                        uniformCrossover(parent1, parent2, anak1, anak2);
                        break;
                    default:
                        onePointCrossover(parent1, parent2, anak1, anak2);
                }
                newPop[index]=anak1;
                if(index + 1 < newPop.length){
                    newPop[index+1]=anak2;
                }
                index +=2;
            }
            else{//jika tidak terjadi crossover, gene nya ambil dari parent terbaik
                newPop[index] = parent1.getNewFitness()>parent2.getNewFitness()? parent1:parent2; 
                index++;
            }
        }
        //perlu spesifikasi method mutasi
        mutasi(newPop,index,population.getboardSize(), 0);
        return new Population(newPop, population.getboardSize());
    }

    public Population blameCrossover(Population population, int startidx){
        WeightedKromosom[] newPop = new WeightedKromosom[population.getSizePopulation()];
        int index = startidx;
        while(index < population.getSizePopulation()){
            double rndmValue = rndm.nextDouble();

            WeightedKromosom parent1 = (WeightedKromosom)selectionFunction(population);
            WeightedKromosom parent2 = (WeightedKromosom)selectionFunction(population);
            if(rndmValue < crossoverRate){

                //buat anak dengan isi gene kosong dan panjang gene sepanjang parent
                WeightedKromosom anak1 = new WeightedKromosom(parent1.length());
                WeightedKromosom anak2 = new WeightedKromosom(parent1.length());

                blameCrossover(parent1,parent2, anak1, anak2);
                newPop[index]=anak1;
                if(index + 1 < newPop.length){
                    newPop[index+1]=anak2;
                }
                index +=2;
            }
            else{//jika tidak terjadi crossover, gene nya ambil dari parent terbaik
                newPop[index] = parent1.getNewFitness()>parent2.getNewFitness()? parent1:parent2; 
                index++;
            }
        }
        //perlu spesifikasi method mutasi
        mutasi(newPop,0,population.getboardSize(), 0);
        return new Population(newPop, population.getboardSize());
    }



    public void mutasi(Kromosom [] population, int startidx, int boardSize, int tipeMutasi){

        for (int i = startidx; i < population.length; i++) {//loop cek kromosom pada populasi
            Kromosom cekKromosom = population[i];
            if (rndm.nextDouble()<rateMutasi)
            switch (tipeMutasi) {
                case 1: // Bit-Flip
                    bitFlipMutation(cekKromosom, boardSize);
                    break;
                case 2: // Swap
                    swapMutation(cekKromosom, boardSize);
                    break;
                case 3: // Sub-Grid 
                    subGridMutation(cekKromosom, boardSize);
                    break;
                default:
                    bitFlipMutation(cekKromosom, boardSize);
            }
        }
    }

    private void onePointCrossover(Kromosom parent1, Kromosom parent2, Kromosom anak1, Kromosom anak2){
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

     private void twoPointCrossover(Kromosom parent1, Kromosom parent2, Kromosom anak1, Kromosom anak2){
        int point1 = 1 + rndm.nextInt(parent1.length()-1);// di tambah 1 karena dalam 1 kromosom minimal ada 1 gene yang di crossover
        int point2 = rndm.nextInt((parent1.length()) - point1) + point1 + 1;
        //set gene 1 per 1 sampai ke point crossovernya
        for (int i = 0; i < parent1.length(); i++) {
            if(i < point1){
                anak1.setGene(i, parent1.getGene(i));
                anak2.setGene(i, parent2.getGene(i));
            }
            else if (i < point2) {
                anak1.setGene(i, parent2.getGene(i));
                anak2.setGene(i, parent1.getGene(i));
            }
            else{
                anak1.setGene(i, parent1.getGene(i));
                anak2.setGene(i, parent2.getGene(i));
            }
        }

        anak1.konversiFitness();
        anak2.konversiFitness();
        //repairChromosom(anak1);
        //repairChromosom(anak2);
    }

    private void uniformCrossover(Kromosom parent1, Kromosom parent2, Kromosom anak1, Kromosom anak2){
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


    private void bitFlipMutation(Kromosom cekKromosom, int boardSize){
        
        for (int j = 0; j < boardSize * boardSize; j++) {
            if(isFixed != null && isFixed[j]) continue;//heuristik
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

    private void swapMutation(Kromosom cekKromosom, int boardSize) {
        if(rndm.nextDouble() < rateMutasi){
            int pos1 = rndm.nextInt(boardSize * boardSize);
            int pos2 = rndm.nextInt(boardSize * boardSize);
            if(isFixed != null && (isFixed[pos1] || isFixed[pos2])) return;

            int gene1 = cekKromosom.getGene(pos1);
            int gene2 = cekKromosom.getGene(pos2);

            // Perform the swap
            cekKromosom.setGene(pos1, gene2);
            cekKromosom.setGene(pos2, gene1);
        }
    }

    private void subGridMutation(Kromosom cekKromosom, int boardSize) {
        if(rndm.nextDouble() < rateMutasi){
            // Titik tengah random (0 sampai 24)
            int centerIdx = rndm.nextInt(boardSize * boardSize);
            
            // Index 1D ke koordinat 2D
            int centerX = centerIdx % boardSize;
            int centerY = centerIdx / boardSize;

            // Loop 3x3 neighbor (-1 sampai +1)
            for (int dy = -1; dy <= 1; dy++) {
                for (int dx = -1; dx <= 1; dx++) {
                    int nx = centerX + dx;
                    int ny = centerY + dy;

                    // Cek boundary
                    if(nx >= 0 && nx < boardSize && ny >= 0 && ny < boardSize){
                        // Memetakan koordinat 2D ke index 1D
                        int targetIdx = (ny * boardSize) + nx;
                        int currVal = cekKromosom.getGene(targetIdx);
                        if (isFixed != null && isFixed[targetIdx]) {
                        continue; 
                    }

                        if(currVal == 0){
                            cekKromosom.setGene(targetIdx, 1);
                        } 
                        else{
                            cekKromosom.setGene(targetIdx, 0);
                        }
                    }
                }
            }
        }
    }


    private void blameCrossover(Kromosom parent1, Kromosom parent2, Kromosom anak1, Kromosom anak2){
        // Set setiap gene 1 per 1 secara acak dari kedua parent dengan probabilitas seragam (0.5)
        for (int i = 0; i < parent1.length(); i++) {
            if(((WeightedKromosom)parent1).compareBlame((WeightedKromosom) parent2, i, rndm)){
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
