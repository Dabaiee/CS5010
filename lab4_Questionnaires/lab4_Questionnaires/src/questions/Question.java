package questions;

public interface Question extends Comparable<Question>{

    /**
     * Get the text of the question
     * @return text of the question
     */
    public String getText();

    /**
     * Get the evaluation result of the answer.
     *
     * @param answer input answer
     * @return evaluation result of the answer, either "Correct" or "Incorrect".
     */
    public String answer(String answer);
}
