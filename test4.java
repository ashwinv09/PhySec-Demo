import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by ashwinv on 06.07.16 at 14:40.
 */
public class test4 {
    public static void main(String[] args) {
//        ProcessBuilder processBuilder = new ProcessBuilder("python", "/home/christopher/MEGA/code/.py", "11111101110111110111111111", "111110011111111000111111101111100001111111110011");
        ProcessBuilder processBuilder = new ProcessBuilder("python", "/home/christopher/MEGA/code/Entropy/noniid_main.py", "/home/christopher/MEGA/code/Entropy/latest/tmpKeyN2.bin", "1", "-v");
        processBuilder.redirectError();
        String s;
//        int yT;

        try {
            Process p = processBuilder.start();
            BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
            while ((s = in.readLine()) != null)
                System.out.println(s);
        } catch (IOException e) {
            e.printStackTrace();
        }

        /*try {
            Process p = processBuilder.start();
            BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));

            String minEntropyStr = in.readLine();
            System.out.println("Min entropy read from command line = " + minEntropyStr);

            *//*if (minEntropyStr.contains(" = ")) {
                int z = minEntropyStr.indexOf(" = ");
                minEntropyStr = minEntropyStr.substring(z+3);
            }*//*

            if (minEntropyStr != null) {
                if ((yT = minEntropyStr.indexOf("min-entropy = ")) >= 0)
                    s = minEntropyStr.substring(yT + 14);
                else
                    s = "0.00";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Final min-entropy value = " + s);*/
    }
}
