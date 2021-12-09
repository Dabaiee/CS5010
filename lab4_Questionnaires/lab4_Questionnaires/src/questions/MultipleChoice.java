package questions;

/**
 * Multiple choice: offers several options, only one of which is correct.
 * This type of question can be created by passing the text of the question,the correct answer and the options.
 * A question may have at least 3 options, but no more than 8.
 * An answer can be entered as one of the option numbers in a string.
 * For example, "1", "3", etc. Option numbers start at 1.
 *
 */

public class MultipleChoice implements Question {

    private String[] options;
    private String text;
    private String correctAnswer;

    /**
     * Constructor for MultipleChoice question
     * @param text
     */
    public MultipleChoice(String text, String answer, String... options) {
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
        if (correctAnswer.equals(answer)) {
            return "Correct";
        } else {
            return "Incorrect";
        }
    }

    /**
     * Returns options of this question.
     *
     * @return a String array of options
     */
    public String[] getOptions() {
        return options;
    }

    /**
     * Returns the correct answer
     *
     * @return the correct answer
     */
    public String getAnswer() {
        return correctAnswer;
    }


    //All multiple-choice questions should be before any multiple-select questions.
    @Override
    public int compareTo(Question o) {
        if (o instanceof TrueFalse) {
            return 1;
        } else if (o instanceof MultipleChoice) {
            return getText().compareTo(o.getText());
        } else {
            return -1;
        }
    }
}
