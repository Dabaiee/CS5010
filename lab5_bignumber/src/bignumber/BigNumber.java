package bignumber;

public interface BigNumber extends Comparable<BigNumber> {

    public int length();

    public void shiftLeft(int shiftNumber);

    public void shiftRight(int shiftNumber);

    public void addDigit(int digit);

    public int getDigitAt(int position);

    public BigNumber copy();

    public BigNumber add(BigNumber number);

    @Override
    public int compareTo(BigNumber number);

    public void reverse();

    public Node getHead();

}
