package questions;

public class TrueFalse implements Question{

    private String text;
    private String correctAnswer;

    /**
     * Constructor for TrueFalse question
     * @param text
     */

    public TrueFalse(String text, String correctAnswer) {

        if (!correctAnswer.equals("True") && !correctAnswer.equals("False")) {
            throw new IllegalArgumentException("The valid answer should be \"True\" or \"False\".");
        }

        this.text = text;
        this.correctAnswer = correctAnswer;
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

    //All true/false questions should be before any multiple-choice questions.
    @Override
    public int compareTo(Question o) {
        if (o instanceof TrueFalse) {
            return getText().compareTo(o.getText());
        } else {
            return -1;
        }
    }
}
