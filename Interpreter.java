import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class Interpreter {
    public Interpreter(String programPath) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(programPath));

        ArrayList<String> program = new ArrayList<>();
        while (br.ready()) {
            program.add(br.readLine());
        }

        System.out.println("o");
    }

    public void run() {

    }
}
