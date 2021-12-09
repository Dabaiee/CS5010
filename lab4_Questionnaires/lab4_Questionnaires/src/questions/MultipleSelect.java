package questions;

import java.util.Arrays;
import java.util.HashSet;

public class MultipleSelect implements Question {

    private String[] options;
    private String text;
    private String correctAnswer;

    /**
     * Constructor for MultipleChoice question
     * @param text
     */
    public MultipleSelect(String text, String answer, String... options) {
        //A question may have at least 3 options, but no more than 8.
        if (options.length < 3 || options.length > 8) {
            throw new IllegalArgumentException("A question may have at least 3 options, but no more than 8");
        }
        this.text = text;
        this.correctAnswer = answer;
        this.options = options;
    }



    @Override
    public String getText() {
        return text;
    }

    @Override
    public String answer(String answer) {
        String[] userInput = answer.split("\\s+");
        String[] correct = correctAnswer.split("\\s+");
        HashSet<String> set = new HashSet<>(Arrays.asList(correct));
        for (String s : userInput) {
            if (set.contains(s)) {
                set.remove(s);
            } else {
                return "Incorrect";
            }
        }

        if (set.isEmpty()) {
            return "Correct";
        }
        return "Incorrect";
    }

    @Override
    public int compareTo(Question o) {
        if (o instanceof TrueFalse || o instanceof MultipleChoice) {
            return 1;
        } else if (o instanceof MultipleSelect) {
            return this.getText().compareTo(o.getText());
        } else {
            return -1;
        }
    }
}
