
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

class repairChromosom {

    private static final Random rdm = new Random();
    private static final int iterasi = 10;

    public static void repairChromosom(int[][] boardAwal, Kromosom diCek) {
        int panjang = boardAwal.length;
        int[] gene = diCek.getGenes();
        for (int itr = 0; itr < iterasi; itr++) {
            boolean berubah = false;
            for (int baris = 0; baris < panjang; baris++) {
                for (int kolom = 0; kolom < panjang; kolom++) {
                    if (boardAwal[baris][kolom] == -1) {
                        //jika kosong maka skip
                        continue;
                    }
                    //cari tau kotak hitam di tetangga
                    int petak = boardAwal[baris][kolom];
                    List<Integer> kotakHitam = new ArrayList<>();
                    List<Integer> kotakPutih = new ArrayList<>();

                    //di tetangga yang di select
                    for (int i = baris - 1; i <= baris + 1; i++) {
                        for (int j = kolom - 1; j <= kolom + 1; j++) {
                            if (i >= 0 && i < panjang && j >= 0 && j < panjang) {
                                int idx = i * panjang + j;
                                if (gene[idx] == 1) {
                                    kotakHitam.add(idx);
                                } else {
                                    kotakPutih.add(idx);
                                }
                            }
                        }
                    }
                    //cek jika kotak hitam kurang
                    int banyakHitam = kotakHitam.size();
                    int target = boardAwal[baris][kolom];

                    if (banyakHitam > target) {
                        int perbaikan = banyakHitam - target;
                        for (int a = 0; a < perbaikan && kotakHitam.isEmpty(); a++) {
                            int idx = kotakHitam.remove(rdm.nextInt(kotakHitam.size()));
                            gene[idx] = 1;
                            berubah = true;
                        }

                    } else if (banyakHitam < target) {
                        int hilang = target - banyakHitam;
                        for (int a = 0; a < hilang && kotakPutih.isEmpty(); a++) {
                            int idx = kotakPutih.remove(rdm.nextInt(kotakPutih.size()));
                            gene[idx] = 0;
                            berubah = true;
                        }
                    }

                    //cek jika kotak hitam lebih
                }
            }
            if (!berubah) {
                break;
            }
        }

    }

    // private static int checkKotakHitam(int baris, int kolom,int panjang) {
    //    int i = panjang * baris;
    //    int j = kolom;
    //    int lokasi = i+j;//lokasi angka bernilai ditemukan
    // }
}
