package bignumber;

import org.junit.Test;

import java.util.Arrays;
import java.util.Random;

import static org.junit.Assert.assertEquals;

public class BigNumberImplTest {

    @Test
    public void testDefaultConstructor() {
        BigNumber n1 = new BigNumberImpl();
        assertEquals(1, n1.length());
        assertEquals(0, n1.getHead().getValue());
    }

    @Test
    public void testInputConstructor() {
        BigNumber n1 = new BigNumberImpl("19979");
        assertEquals(5, n1.length());
    }

    @Test
    public void testToString() {
        BigNumber n1 = new BigNumberImpl("19979");
        assertEquals("19979", n1.toString());
    }

    @Test
    public void testCopy() {
        BigNumber n1 = new BigNumberImpl("19979");
        BigNumber n2 = n1.copy();
        assertEquals("19979", n2.toString());
    }

    @Test
    public void testShift() {
        BigNumber n1 = new BigNumberImpl("19979");
        n1.shiftLeft(2);
        assertEquals("1997900", n1.toString());
        n1.shiftRight(4);
        assertEquals("199", n1.toString());
        n1.shiftRight(4);
        assertEquals("0", n1.toString());
        n1.shiftLeft(10);
        assertEquals("0", n1.toString());
    }

    @Test
    public void testShiftZero() {
        BigNumber n1 = new BigNumberImpl("0");
        n1.shiftLeft(2);
        assertEquals("0", n1.toString());
        n1.shiftRight(4);
        assertEquals("0", n1.toString());
    }


    @Test
    public void testAddDigit() {
        BigNumber n1 = new BigNumberImpl("19999");
        n1.addDigit(1);
        assertEquals("20000", n1.toString());
        n1.addDigit(1);
        assertEquals("20001", n1.toString());
    }

    @Test
    public void testGetDigitAt() {
        BigNumber n1 = new BigNumberImpl("12345");
        assertEquals(3, n1.getDigitAt(2));
        assertEquals(5, n1.getDigitAt(0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetDigitAtInvalidPosition() {
        BigNumber n1 = new BigNumberImpl("12345");
        n1.getDigitAt(5);
    }

    @Test
    public void testAdd() {
        BigNumber n1 = new BigNumberImpl("19999");
        BigNumber n2 = new BigNumberImpl("1234");
        BigNumber n3 = n1.add(n2);
        assertEquals("19999", n1.toString());
        assertEquals("1234", n2.toString());
        assertEquals("21233", n3.toString());
    }

    @Test
    public void testAdd2() {
        BigNumber n1 = new BigNumberImpl("99999");
        BigNumber n2 = new BigNumberImpl("99999");
        BigNumber n3 = n1.add(n2);
        assertEquals("99999", n1.toString());
        assertEquals("99999", n2.toString());
        assertEquals("199998", n3.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddInvalid() {
        BigNumber n1 = new BigNumberImpl("19999");
        BigNumber n2 = new BigNumberImpl("1234a");
        BigNumber n3 = n1.add(n2);
    }

    @Test
    public void testCompareTo() {
        BigNumber n1 = new BigNumberImpl("19999");
        BigNumber n2 = new BigNumberImpl("12345");
        BigNumber n3 = new BigNumberImpl("12346");
        BigNumber n4 = new BigNumberImpl("19");
        BigNumber n5 = new BigNumberImpl("0");
        BigNumber[] arr = new BigNumber[] {n1, n2, n3, n4, n5};
        Arrays.sort(arr);

        assertEquals("0", arr[0].toString());
        assertEquals("19", arr[1].toString());
        assertEquals("12345", arr[2].toString());
        assertEquals("12346", arr[3].toString());
        assertEquals("19999", arr[4].toString());
    }

    @Test(timeout = 3000)
    public void testLength() {
        Random r = new Random(200);
        StringBuilder expected = new StringBuilder();
        for (int i = 0; i < 5000; i++) {
            int digit = Math.abs(r.nextInt()) % 10;
            if ((i == 0) && (digit == 0)) {
                digit = Math.abs(r.nextInt()) % 9 + 1;
            }
            if ((expected.toString().length() != 0) || (digit != 0)) {
                expected.append(digit);
            }
            BigNumber number = new BigNumberImpl(expected.toString());
            assertEquals("Length of number is not correct", (i + 1), number.length());
        }
    }
}
