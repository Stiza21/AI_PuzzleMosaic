// public class TestCopySalah {

//     static class Kromosom {
//         int[] genes;

//         Kromosom(int[] genes) {
//             this.genes = genes; // ❌ masih referensi sama
//         }

//         Kromosom copy() {
//             return new Kromosom(this.genes); // ❌
//         }

//         void setGene(int idx, int val) {
//             genes[idx] = val;
//         }

//         void print(String name) {
//             System.out.println(name + ": " + genes[0] + " " + genes[1] + " " + genes[2]);
//         }
//     }

//     public static void main(String[] args) {
//         Kromosom parent = new Kromosom(new int[]{0, 0, 0});
//         Kromosom child  = parent.copy(); // kelihatan aman tapi tidak

//         child.setGene(2, 1);

//         parent.print("Parent");
//         child.print("Child");
//     }
// }

public class TestCopyBenar {

    static class Kromosom {
        int[] genes;

        Kromosom(int[] genes) {
            this.genes = genes.clone(); //DEEP COPY
        }

        Kromosom copy() {
            return new Kromosom(this.genes); // constructor clone
        }

        void setGene(int idx, int val) {
            genes[idx] = val;
        }

        void print(String name) {
            System.out.println(name + ": " + genes[0] + " " + genes[1] + " " + genes[2]);
        }
    }

    public static void main(String[] args) {
        Kromosom parent = new Kromosom(new int[]{0, 0, 0});
        Kromosom child  = parent.copy(); // ✅ AMAN

        child.setGene(0, 1);

        parent.print("Parent");
        child.print("Child");
    }
}

