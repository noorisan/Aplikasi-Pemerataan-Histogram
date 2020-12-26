package Teknik;

import java.awt.image.BufferedImage;
import java.io.File;
/*
 * 	Menjelaskan teknik apa yang dibutuhkan agar program dapat berfungsi
 */
public interface Technique{
    float operation(File file,int parameter0,int parameter1); //Parameter tidak selalu digunakan, mengembalikan waktu yang dibutuhkan
    void clear(); // Menghapus array sehingga teknik dapat digunakan kembali
    File getFile(String fileName); //Mendapatkan file
    BufferedImage getBufferedImage(); //Mendapatkan gambar
    int getWidth();
    int getHeight();
}
