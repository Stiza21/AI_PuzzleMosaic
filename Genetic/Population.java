public class Population {
    private Kromosom [] population;
    public Population(int numOfKrom){
        population = new Kromosom[numOfKrom];
    }
    public void generatePop(int panjangKromosom){
        for(int i = 0; i<this.population.length; i++){
            this.population[i] = new Kromosom(panjangKromosom);
        }
    }
    public int getSizePopulation() {
        return population.length;
    }
    public Kromosom getKromFromPopulation(int idx){
        return population[idx];
    }
    
}
