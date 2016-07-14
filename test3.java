import java.io.*;

class test3{
    public static void main(String a[]){

        /*StringWriter writer = new StringWriter(); //ouput will be stored here

        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptContext context = new SimpleScriptContext();

        context.setWriter(writer); //configures output redirection
        ScriptEngine engine = manager.getEngineByName("python");

        try {
            engine.eval(new FileReader("/home/christopher/MEGA/code/work/adptQuanDeploy.py"), context);
        } catch (ScriptException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        System.out.println(writer.toString());*/


        ProcessBuilder processBuilder = new ProcessBuilder("python", "/home/christopher/MEGA/code/.py", "11111101110111110111111111", "111110011111111000111111101111100001111111110011");
        processBuilder.redirectError();
        String s;

        try {
            Process p = processBuilder.start();
            BufferedReader in = new BufferedReader(new InputStreamReader(p.getInputStream()));
            while ((s = in.readLine()) != null)
                System.out.println(s);
        } catch (IOException e) {
            e.printStackTrace();
        }



        /*String s;
        try {
            Process p = Runtime.getRuntime().exec("python /home/christopher/adptQuanDeploy.py");

            BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
            BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));

            // read the output from the command
            System.out.println("Here is the standard output of the command:\n");
            while ((s = stdInput.readLine()) != null) {
                System.out.println(s);
            }

            // read any errors from the attempted command
            System.out.println("Here is the standard error of the command (if any):\n");
            while ((s = stdError.readLine()) != null) {
                System.out.println(s);
            }

            System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
        }*/


    }
}
