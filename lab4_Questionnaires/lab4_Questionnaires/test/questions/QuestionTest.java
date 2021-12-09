package questions;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class QuestionTest {

    @Test
    public void testLikert() {
        Question q = new Likert("are you satisfied with our service?");
        assertEquals("are you satisfied with our service?", q.getText());
    }

    @Test
    public void testAnswerLikert() {
        Question q = new Likert("are you satisfied with our service?");

        assertEquals("Correct", q.answer("1"));
        assertEquals("Correct", q.answer("2"));
        assertEquals("Correct", q.answer("3"));
        assertEquals("Correct", q.answer("4"));
        assertEquals("Correct", q.answer("5"));
        assertEquals("Incorrect", q.answer("6"));
        assertEquals("Incorrect", q.answer("-1"));
    }

    @Test
    public void testMultipleChoice() {
        Question q = new MultipleChoice("what is the result of 1+1?", "2",
                new String[] {"1", "2", "3", "4"});

        assertEquals("what is the result of 1+1?", q.getText());
        assertEquals("Incorrect", q.answer("3"));
        assertEquals("Correct", q.answer("2"));
        assertEquals("Incorrect", q.answer("-1"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWrongOptionsMultipleChoice() {
        Question q = new MultipleChoice("what is the result of 1+1?", "1",
                new String[] {"1", "2"});
    }


    @Test
    public void testMultipleSelect() {
        Question q = new MultipleSelect("what is the wrong result of 1+1?", "1 3 4",
                new String[] {"1", "2", "3", "4"});

        assertEquals("what is the wrong result of 1+1?", q.getText());
        assertEquals("Correct", q.answer("1 3 4"));
        assertEquals("Correct", q.answer("3 1 4"));
        assertEquals("Incorrect", q.answer("2"));
        assertEquals("Incorrect", q.answer("1 2"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWrongOptionsMultipleSelect() {
        Question q = new MultipleSelect("what is the wrong result of 1+1?", "1 3 4",
                new String[] {"1", "2"});
    }


    @Test
    public void testTrueFalse() {
        Question q = new TrueFalse("Is tomorrow sunny?", "False");

        assertEquals("Is tomorrow sunny?", q.getText());
        assertEquals("Incorrect", q.answer("True"));
        assertEquals("Correct", q.answer("False"));
        assertEquals("Incorrect", q.answer("No"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWrongAnswerTrueFalse() {
        Question d = new TrueFalse("Is tomorrow sunny?", "Yes");
    }


    @Test
    public void testSort() {
        Question a = new Likert("are you satisfied with our service?");
        Question b = new MultipleChoice("what is the result of 1+1?", "2",
                new String[] {"1", "2", "3", "4"});
        Question c = new MultipleSelect("what is the wrong result of 1+1?", "1 3 4",
                new String[] {"1", "2", "3", "4"});

        Question d = new TrueFalse("Is tomorrow sunny?", "False");
        Question e = new MultipleSelect("Awhat is the wrong result of 1+1?", "1 3 4",
                new String[] {"1", "2", "3", "4"});


        Question[] questionBank = new Question[] {d, b, e, c, a};

        Arrays.sort(questionBank);

        assertEquals("Is tomorrow sunny?", questionBank[0].getText());
        assertEquals("what is the result of 1+1?", questionBank[1].getText());
        assertEquals("Awhat is the wrong result of 1+1?", questionBank[2].getText());
        assertEquals("what is the wrong result of 1+1?", questionBank[3].getText());
        assertEquals("are you satisfied with our service?", questionBank[4].getText());
    }








}