package questions;

public class Likert implements Question {


    //can be answered on a fixed, 5-point Likert scale (Strongly Agree, Agree, Neither Agree nor Disagree, Disagree, Strongly Disagree).
    //This type of question can be created by passing the text of the question. Since this question asks an opinion, there is no "correct" answer.
    //An answer can be entered as one of the option numbers, numbered from 1 in the above order.
    //Any valid option number is a "correct" answer.

    private static final String[] answerScale = new String[]{"1","2","3","4","5"};
    private String text;

    /**
     * Constructor for Likert question
     * @param text
     */
    public Likert(String text) {
        this.text = text;
    }


    @Override
    public String getText() {
        return text;
    }

    @Override
    public String answer(String answer) {
        for(int i = 0; i < 5; i++){
            if (answer.equals(answerScale[i])) {
                return "Correct";
            }
        }
        return "Incorrect";
    }

    //All multiple-select questions should be before any Likert questions.
    @Override
    public int compareTo(Question otherQuestion) {
        if (otherQuestion instanceof Likert) {
            return this.getText().compareTo(otherQuestion.getText());
        } else {
            return 1;
        }
    }
}
