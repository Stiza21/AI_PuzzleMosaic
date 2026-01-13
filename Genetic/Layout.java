public class Layout {
    int[][] request;
    public Layout(int[][] request){
        this.request = new int[request.length][request[0].length];
        for (int i=0;i<request.length;i++){
            System.arraycopy(request[i], 0, this.request[i], 0, request[i].length);
        }
    }
}
