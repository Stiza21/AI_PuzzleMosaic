public class Layout {
    int[][] request;
    double[][] chance;
    public Layout(int[][] request){
        this.request = new int[request.length][request[0].length];
        double[][] chanceBlack = new double[request.length][request[0].length];
        double[][] chanceWhite = new double[request.length][request[0].length];
        this.chance = new double[request.length][request[0].length];
        for (int i=0;i<request.length;i++){
            for (int j=0;j<request[i].length;j++){
                this.request[i][j]=request[i][j];
                chanceBlack[i][j]=1;
                chanceWhite[i][j]=1;
                for (int k=i-1;k<=i+1;k++){
                    for (int l=j-1;l<=j+1;l++){
                        if (k>=0&&k<request.length&&l>=0&&l<request[k].length&&request[k][l]!=-1){
                            chanceBlack[i][j]*=request[k][l]/9.0;
                            chanceWhite[i][j]*=(9.0-request[k][l])/9.0;
                        }
                    }
                }
                this.chance[i][j] = chanceBlack[i][j]/(chanceBlack[i][j]+chanceWhite[i][j]);
            }
        }
        System.out.println("layout terbuat");
    }
}
