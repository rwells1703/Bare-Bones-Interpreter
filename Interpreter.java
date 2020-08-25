import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class Interpreter {
    private ArrayList<String> program;
    private int maxLoopNestDepth = 0;

    public Interpreter(String programPath) throws IOException {
        load(programPath);
        check();
    }

    private void load(String programPath) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(programPath));

        program = new ArrayList<>();
        while (br.ready()) {
            program.add(br.readLine());
        }
    }

    private void check() {
        int syntaxStatus = checkSyntax();
        if (syntaxStatus != 0) {
            System.err.println("Syntax Error: On line " + syntaxStatus);
            System.exit(-1);
        }

        int loopStatus = checkLoops();
        if (loopStatus == -1) {
            System.err.println("Syntax Error: Not all while loops have been closed");
            System.exit(-1);
        } else if (loopStatus != 0) {
            System.err.println("Syntax Error: Unecessary end statement on line " + loopStatus);
            System.exit(-1);
        }

        System.out.println("Program ready to run!");
    }

    // Check syntax of entire program
    private int checkSyntax() {
        // Check each individual line
        Pattern lineSyntaxPattern = Pattern.compile("^(\t| )*((clear|incr|decr) [A-Z]|while [A-Z] (is|not) \\d+ do|end);$");

        for (int i = 0; i < program.size(); i++) {
            String line = program.get(i);
            if (!lineSyntaxPattern.matcher(line).matches()) {
                return i + 1;
            }
        }

        return 0;
    }

    private int checkLoops() {
        int loopNestDepth = 0;

        for (int i = 0; i < program.size(); i++) {
            String line = program.get(i);

            if (line.contains("while")) {
                loopNestDepth++;
            } else if (line.contains("end")) {
                loopNestDepth--;
            }

            // Increase the maximum loop depth if we have surpassed it
            if (loopNestDepth > maxLoopNestDepth) {
                maxLoopNestDepth = loopNestDepth;
            }

            // If we have reached negative loop depth, throw error
            if (loopNestDepth < 0) {
                return i + 1;
            }
        }

        // If not all loops have been closed with an end statement, throw error
        if (loopNestDepth != 0) {
            return -1;
        }

        return 0;
    }
}