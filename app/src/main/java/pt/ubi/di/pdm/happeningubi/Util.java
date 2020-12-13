package pt.ubi.di.pdm.happeningubi;

import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class Util extends AppCompatActivity {

    private static final String FILE_NAME = "data.txt";

    public void MSG(String s){  //para mostrar toasts

        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
    }

    public void writeUser(String s){ //para escrever utilizador atual no ficheiro

        FileOutputStream fos = null;

        try {

            fos = openFileOutput(FILE_NAME, MODE_PRIVATE);

            fos.write(s.getBytes());

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public String readUser(){   //para ler utilizador do ficheiro

        FileInputStream fis = null;

        try {
            fis = openFileInput(FILE_NAME);

            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String text;

            while ((text = br.readLine()) != null){
                sb.append(text).append("");
            }

            return sb.toString();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return null;

    }
}
