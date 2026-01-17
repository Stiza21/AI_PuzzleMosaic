import java.util.Random;

public class Population {
    private Kromosom [] population;
    int boardSize;
    public Population(int boardSize){
        this.boardSize = boardSize;
        this.population = new Kromosom[boardSize*boardSize];
    }
    public Population(Kromosom[] pop, int boardSize){
        this.population = pop.clone();
        this.boardSize=boardSize;
    }
    public void generatePop(int panjangKromosom, Random rndm){
        for(int i = 0; i<this.population.length; i++){
            this.population[i] = new Kromosom(panjangKromosom, rndm);
        }
    }
    public int getboardSize(){
        return this.boardSize;
    }
    public int getSizePopulation() {
        return population.length;
    }
    public Kromosom getKromFromPopulation(int idx){
        return population[idx];
    }
    public Kromosom getBest(){
        int best=0;
        int bestFitness=population[0].getNewFitness();
        for (int i=1;i<population.length;i++){
            if (bestFitness<population[i].getNewFitness()){
                best=i;
                bestFitness=population[i].getNewFitness();
            }
        }
        return population[best];
    }
    public void setKromosom(int idx, Kromosom krom){
        this.population[idx]=krom;
    }
    
}
