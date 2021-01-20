/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package remotecontrolrobot;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.StringTokenizer;
//import Main.GUI;
import java.text.DecimalFormat;

/**
 *
 * @author Wolez
 */
public class KNN {

    String[][] indat;

    String s, sp1;
    StringTokenizer st;
    ArrayList x = new ArrayList();

    int k;
    List kelas = new ArrayList();
    double[][] dataTesting;
    double[][] dataTraining;
    double[] atributTarget;
    int kelasOK = 0;
    String strKelas;
    String HasilKelas;
    
    public String getKelas(){
        return strKelas;
    }
    
    void setKelas(String outputKelas){
        strKelas = outputKelas;
    }

    public void bacaFileToArray(String fname) throws FileNotFoundException, IOException {
        FileReader fr = new FileReader(fname);
        BufferedReader buf = new BufferedReader(fr);

        int words = 0;
        int chars = 0;
        int lines = 0;

        while ((s = buf.readLine()) != null) {
            lines++;
            st = new StringTokenizer(s, "\t");
            while (st.hasMoreTokens()) {
                words++;
                s = st.nextToken();
                chars += s.length();
                String y = new String(s);
                x.add(y);
            }
        }

        String ct[] = new String[0];
        ct = (String[]) x.toArray(ct);

        indat = new String[lines][words / lines];
        int inval = 0;

        // Mencetak array dari pembacaan file
        //System.out.println("Data Sample :");
        for (int i = 0; i < lines; i++) {
            for (int j = 0; j < (words / lines); j++) {
                indat[i][j] = ct[inval];
                inval++;
                //System.out.print((indat[i][j]) + "\t");
            }
            //System.out.println();
        }
        //System.out.println("Jumlah Baris : " + indat.length);
        //System.out.println("Jumlah Kolom : " + indat[0].length);
    }

    public void input_K(int valK) throws IOException {
        // Membaca inputan k dari user
        /*BufferedReader buf1 = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("\nMasukkan Nilai K (jika ingin menggunakan standard K otomatis silahkan input 0): ");
        sp1 = buf1.readLine();

        Integer ky = new Integer(sp1);
        k = ky;*/

        // Membatasi nilai k, harus lebih kecil dari jumlah baris data dibagi 2
        double row = indat.length;
        if (valK < 0) {
            //System.out.println("\nSorry, please input k value > 0");
            System.exit(0);
        } else if (valK == 0) {
            k = (int) Math.round(Math.sqrt(row));
            //System.out.println("\nInisialisasi Nilai K = " + k);
        } else {
            k = valK;
            //System.out.println("\nInisialisasi Nilai K = " + k);
        }
    }

    public void inputDataTesting(float[] arrTesting) throws IOException {
        // Membaca inputan data testing dari user
        //System.out.println("\nInput Data Testing ");
        dataTesting = new double[1][indat[0].length - 1]; //1x2
        //BufferedReader buf1 = new BufferedReader(new InputStreamReader(System.in));
        for (int i = 0; i < indat[0].length - 1; i++) {
            //System.out.print("\nMasukkan Nilai Data Testing Kolom " + i + " : ");
            //sp1 = buf1.readLine();
            //Double ky = new Double(sp1);
            dataTesting[0][i] = arrTesting[i];
            //System.out.print(x[0][i]+"\t");
        }
        for (int i = 0; i < dataTesting[0].length; i++) {
            //System.out.print(dataTesting[0][i] + "\t");
        }
    }

    public void defenisiClass() {
        // Mendefenisikan klass ke number
        String temp = null;
        int idx = -1;
        List x = new ArrayList();
        //System.out.println();
        //System.out.println("\nDefenisi Class ke Number :");
        for (int i = 0; i < indat[0].length; i++) {
            String cek = null;
            for (int j = 0; j < indat.length; j++) {
                if (i == indat[0].length - 1) {
                    cek = indat[j][i];
                    if (temp != cek && !kelas.contains(cek)) {
                        idx++;
                        x.add(idx);
                        kelas.add(cek);
                    }
                    if (kelas.contains(cek)) {
                        indat[j][i] = String.valueOf(kelas.indexOf(cek));
                    } else {
                        indat[j][i] = String.valueOf(idx);
                    }
                    temp = cek;
                }
            }
        }
        /*System.out.println("Hasil :");
        for (int i = 0; i < indat.length; i++) {
            for (int j = 0; j < (indat[0].length); j++) {
                System.out.print((indat[i][j]) + "\t");
            }
            System.out.println();
        }
        System.out.println("Atribut Kelas : " + kelas);
        System.out.println("Defenisi Atribut : " + x);
        System.out.println("Jumlah Kelas = " + kelas.size());*/
    }

    public void ambilAtributTarget() {
        //System.out.println("\nAtribut Target");
        atributTarget = new double[indat.length];
        for (int i = 0; i < indat.length; i++) {
            for (int j = 0; j < indat[0].length; j++) {
                if (j == (indat[0].length - 1)) {
                    atributTarget[i] = Double.parseDouble(indat[i][j]);
                }
                //System.out.print(atributTarget[i]);
            }
            //System.out.print("\n");
        }
        //System.out.println(atributTarget[0]);
    }

    public void ambilDataTraining() {
        //System.out.println("\nData Training");
        dataTraining = new double[indat.length][indat[0].length - 1];
        for (int i = 0; i < indat.length; i++) {
            for (int j = 0; j < indat[0].length - 1; j++) {
                dataTraining[i][j] = Double.parseDouble(indat[i][j]);
                //System.out.print(dataTraining[i][j] + "\t");
            }
            //System.out.print("\n");
        }
    }

    public void operasiKNN() {
        DecimalFormat df = new DecimalFormat("#.##");
        double TK = 15;
        //System.out.println("\nMenghitung Kalkulasi Jarak");

        // Langkah ke-1 Data Testing harus di transpose
        //System.out.println("\n1. Cari Nilai transpose dari Data Testing :");
        double[][] transpose = new double[dataTesting[0].length][dataTesting.length];
        if (dataTesting.length > 0) {
            for (int i = 0; i < dataTesting[0].length; i++) {
                for (int j = 0; j < dataTesting.length; j++) {
                    transpose[i][j] = dataTesting[j][i];
                    //System.out.print(transpose[i][j] + "\t");
                }
                //System.out.print("\n");
            }
        }
        // Langkah ke-2 menghitung Jarak Data Testing ke Semua Data Training
        //System.out.println("\n2. Hasil Perhitungan Jarak Data Testing ke Semua Data Training :");
        double[][] hasil = new double[dataTraining.length][transpose[0].length];
        double[] hasil2 = new double[dataTraining.length];
        for (int i = 0; i < hasil.length; i++) { //row
            for (int j = 0; j < hasil[0].length; j++) { //col
                for (int l = 0; l < dataTraining[0].length; l++) {
                    hasil[i][j] += (Math.pow(dataTraining[i][l] - transpose[l][j], 2));
                }
                hasil2[i] = (Math.sqrt(hasil[i][j]));
                //System.out.print(hasil2[i] + "\t");
            }
            //System.out.print("\n");
        }

        // Langkah ke-3 Gabungkan Array dari Atribut dan Jaraknya
        //System.out.println("\nGabungkan Data Training dan Jaraknya Menjadi 1 Array :");
        double hasil3[][] = new double[dataTraining.length][dataTraining[0].length + 2];
        for (int i = 0; i < dataTraining.length; i++) { //row
            for (int j = 0; j < dataTraining[0].length + 2; j++) { //col
                if (j == dataTraining[0].length) {
                    hasil3[i][j] = hasil2[i];
                } else if (j == (dataTraining[0].length + 1)) {
                    hasil3[i][j] = atributTarget[i];
                } else {
                    hasil3[i][j] = dataTraining[i][j];
                }
                //System.out.print(hasil3[i][j] + "\t");
            }
            //System.out.println();
        }

        //Langkah ke-4 Sorting Array Berdasarkan Kolom Jarak.
        //System.out.println("\nSorting Array Berdasarkan Kolom Jarak :");
        sortingArray(hasil3, hasil3[0].length - 2);
        //printArray(hasil3);

        //Langkah ke-5 Mengambil k Tetangga Terdekat Berdasarkan Rangkingnya.
        //System.out.println("\nMengambil " + k + " Tetangga Terdekat Berdasarkan Jaraknya :");
        for (int i = 0; i < k; i++) { //row
            for (int j = 0; j < dataTraining[0].length + 2; j++) { //col
                //System.out.print(hasil3[i][j] + "\t");
            }
            //System.out.println();
        }

        //Langkah ke-6 Gunakan Teori Mayoritas Untuk Memprediksi Data Baru 
        //System.out.println("\nGunakan Teori Mayoritas Untuk Memprediksi Data Baru");
        double temp = 0;
        for (int a = 0; a < kelas.size(); a++) {
            //System.out.println("\nData Pada Kelas " + a + " Adalah : ");
            double jml = 0;
            for (int i = 0; i < dataTraining[0].length + 2; i++) { //col
                for (int j = 0; j < k; j++) { //row
                    if (i == dataTraining[0].length + 1 && hasil3[j][i] == a) {
                        jml++;
                    }
                }
            }
            if (temp < jml) {
                kelasOK = a;
                temp = jml;
            }
            //System.out.println("Jumlah Data Pada Kelas " + a + " adalah : " + jml);
            
            if (jml > 0){
                setKelas(kelas.get(kelasOK).toString());
                //System.out.println("------------------------------------------- Data terkait adalah "+ kelas.get(kelasOK) +": "+ df.format(jml/TK));
//                GUI.TerText.setVerticalTextPosition(1);
            }
                            
        }
        setKelas(kelas.get(kelasOK).toString());
        //System.out.println("Hasil Akhir Data Baru Termasuk Pada Kelas : " + kelasOK);
        //System.out.println("Atau : (" + kelas.get(kelasOK) + ")"); 
        HasilKelas = kelas.get(kelasOK).toString();

//------Perhitungan F-Measure------------------------------------------------//
//        
//        double TP = temp ;
//        double FP = 15-temp ;
//        double FN = 15-temp;
//        double TN = 0;
//        double Precision = TP/(TP+FP);
//        double Recall = TP/(TP+FN);
//        String HPrecision = Double.toString(Precision);
//        GUI.PrecisionText.setText(HPrecision);
//        String HRecall = Double.toString(Recall);
//        GUI.RecallText.setText(HRecall);
//        
////        DecimalFormat df = new DecimalFormat("#.##");
//        double FMeasure = (2*((Precision*Recall)/(Precision+Recall)));
//        ///float DFMeasure = df.format(FMeasure);
//        //System.out.println("FMeasure = " + df.format(FMeasure));
//        //String HFMeasure = df.format(FMeasure);
//        GUI.FMeasureText.setText(df.format(FMeasure));
        
//--------Perhitungan Akurasi------------------------------------------------//
        double HasilAkurasi = (temp/TK) ; //(temp/15)*(100/100)
        double HasilError = (TK-temp)/TK ;  //((15-temp)/15)*(100/100)
//        System.out.println("\n Hasil Akurasi = "+HasilAkurasi);
//        DecimalFormat df = new DecimalFormat("#.##"); //membuat format decimal dengan dua digits dibelakang koma
//        String HAkurasi = Double.toString(HasilAkurasi);
//        GUI.AkurasiText.setText(df.format(HasilAkurasi));
//        String HError = Double.toString(HasilError);
//        GUI.ErrorText.setText(df.format(HasilError));
    }

    public void sortingArray(double[][] array, int col) {
        Arrays.sort(array, new Comparator<double[]>() {
            @Override
            public int compare(double[] s1, double[] s2) {
                return Double.compare(s1[col], s2[col]);
            }
        });
    }

    public void printArray(double[][] a) {
        for (int i = 0; i < a.length; i++) {
            for (int j = 0; j < a[0].length; j++) {
                System.out.print(a[i][j] + "\t");
            }
            System.out.println();
        }
    }

    /*
    @SuppressWarnings("empty-statement")
    public static void main(String args[]) throws Exception {
        float[] datauji = new float[5];
        datauji[0] = 61.51f;
        datauji[1] = 44.10f;
        datauji[2] = 43.45f;
        datauji[3] = 40.19f;
        datauji[4] = 90.90f;
        
        System.out.println("Program KNN dengan Tipe data Atribut Numerik");
        KNN knn = new KNN();
        knn.bacaFileToArray("file/Gabung.txt"); //"file/data_training.csv"
        knn.input_K(3);
        knn.inputDataTesting(datauji);
        knn.defenisiClass();
        knn.ambilDataTraining();
        knn.ambilAtributTarget();
        knn.operasiKNN();
    }
*/
}