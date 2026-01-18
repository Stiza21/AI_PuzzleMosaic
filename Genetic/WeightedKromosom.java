
import java.util.Random;

public class WeightedKromosom extends Kromosom{
    private int[] blame;
    public WeightedKromosom(int length){
        super(length);
        this.blame = new int[length];
    }
    public WeightedKromosom(int boardSize, Random rand){
        this(boardSize*boardSize);
        for (int i=0;i<boardSize*boardSize;i++){
            this.genes[i] = rand.nextDouble()<Main.layout.chance[i/boardSize][i%boardSize]?0:1;
        }
    }
    @Override
    public int getNewFitness(){
        if (this.fitness==1){
            this.fitness = this.fitness();
        }
        return this.fitness;
    }

    @Override
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
                if (deviation==0) continue;
                for (int k=i-1;k<=i+1;k++){
                    for (int l=j-1;l<=j+1;l++){
                        if (k>=0&&k<layout.request.length&&l>=0&&l<layout.request[i].length){
                            if ((deviation>0&&this.genes[k*layout.request[i].length+l]>0)||
                            (deviation<0&&this.genes[k*layout.request[i].length+l]==0)) 
                            this.blame[k*layout.request[i].length+l]++;
                        }
                    }
                }
                total+=Math.abs(deviation);
            }
        }
        return -total;
    }
    public boolean compareBlame(WeightedKromosom other, int index, Random rand){
        return rand.nextDouble()<0.5+((other.blame[index]-this.blame[index])/9.0);
    }
}
