package bignumber;


public class BigNumberImpl implements BigNumber {

    private Node head;

    public BigNumberImpl() {
        head = new Node(0);
    }

    public BigNumberImpl(String number) {
        char last = number.charAt(number.length() - 1);
        if (!Character.isDigit(last)) {
            throw new IllegalArgumentException("the string passed to it does not represent a valid number.");
        }
        head = new Node(last - '0');
        Node node = head;

        for (int i = number.length() - 2; i >= 0; i--) {
            char c = number.charAt(i);
            if (!Character.isDigit(c)) {
                throw new IllegalArgumentException("the string passed to it does not represent a valid number.");
            }
            node.setNext(new Node(c - '0'));
            node = node.getNext();
        }
    }

    @Override
    public int length() {
        int count = 0;
        Node node = head;

        while (node != null) {
            count++;
            node = node.getNext();
        }
        return count;
    }

    @Override
    public void shiftLeft(int shiftNumber) {
        if (shiftNumber < 0) {
            shiftNumber = -shiftNumber;
            shiftRight(shiftNumber);
            return;
        }
        if (head.getValue() <= 0 && this.length() <= 1) {
            removeDigit();
            return;
        }

        while (shiftNumber > 0) {
            Node prev = new Node(0);
            prev.setNext(head);
            head = prev;
            shiftNumber--;
        }
        removeDigit();

    }

    private void removeDigit() {
        if (this.length() > 1 && head.getValue() == 0) {
            head = head.getNext();
        }
    }

    @Override
    public void shiftRight(int shiftNumber) {
        if (shiftNumber == 0) {
            return;
        }

        if (shiftNumber < 0) {
            shiftNumber = -shiftNumber;
            shiftLeft(shiftNumber);
            return;
        }

        if (head.getValue() <= 0 && this.length() <= 1) {
            removeDigit();
            return;
        }

        Node ptr = head;
        while (ptr.getNext() != null && shiftNumber > 0) {
            ptr = ptr.getNext();
            shiftNumber--;
        }
        head = ptr;
        if (shiftNumber > 0) {
            head.setNext(null);
            head.setValue(0);
        }
        removeDigit();
    }


//    @Override
//    public void shiftLeft(int shiftNumber) {
//        if (head.getValue() <= 0 && this.length() <= 1) {
//            return;
//        }
//
//        if (shiftNumber < 0) {
//            shiftNumber = -shiftNumber;
//            shiftRight(shiftNumber);
//            return;
//        }
//
//        while (shiftNumber > 0) {
//            Node prev = new Node(0);
//            prev.setNext(head);
//            head = prev;
//            shiftNumber--;
//        }
//    }
//
//    @Override
//    public void shiftRight(int shiftNumber) {
//        if (head.getValue() <= 0 && this.length() <= 1) {
//            return;
//        }
//
//        if (shiftNumber == 0) {
//            return;
//        }
//
//        if (shiftNumber < 0) {
//            shiftNumber = -shiftNumber;
//            shiftLeft(shiftNumber);
//            return;
//        }
//
//        Node ptr = head;
//        while (ptr.getNext() != null && shiftNumber > 0) {
//            ptr = ptr.getNext();
//            shiftNumber--;
//        }
//        head = ptr;
//        if (shiftNumber > 0) {
//            head.setNext(null);
//            head.setValue(0);
//        }
//    }

    @Override
    public void addDigit(int digit) {
        if (digit < 0 || digit > 9) {
            throw new IllegalArgumentException(" digit passed in should not be a single non-negative digit");
        }

        int res = digit + head.getValue();
        int overflow = 0;
        Node ptr = head;

        overflow = res / 10; // for next digit
        res = res % 10;
        ptr.setValue(res);
        ptr = ptr.getNext();

        while (overflow != 0 && ptr != null) {
            res = ptr.getValue() + overflow;
            overflow = res / 10;
            res = res % 10;
            ptr.setValue(res);
            ptr = ptr.getNext();
        }

        if (overflow != 0) {
            ptr = new Node(1);
        }
    }

    @Override
    public int getDigitAt(int position) {
        if (position < 0 || position >= this.length()) {
            throw new IllegalArgumentException("Invalid position");
        }
        Node ptr = head;
        while (position > 0) {
            ptr = ptr.getNext();
            position--;
        }
        return ptr.getValue();
    }

    @Override
    public BigNumber copy() {
        return new BigNumberImpl(this.toString());
    }

    @Override
    public BigNumber add(BigNumber number) {
        BigNumberImpl num = (BigNumberImpl) number;
        Node p1 = head;
        Node p2 = num.head;

        BigNumberImpl newNumber = new BigNumberImpl();
        Node p3 = newNumber.head;

        int overflow = 0;
        int res = 0;

        while (p1 != null && p2 != null) {
            res = p1.getValue() + p2.getValue() + overflow;
            overflow = res / 10;
            res = res % 10;
            p3.setValue(res);

            p1 = p1.getNext();
            p2 = p2.getNext();
            p3.setNext(new Node(0));
            p3 = p3.getNext();
        }

        while (p1 != null) {
            res = p1.getValue() + overflow;
            overflow = res / 10;
            res = res % 10;
            p3.setValue(res);

            p1 = p1.getNext();
            if (p1 != null) {
                p3.setNext(new Node(0));
            }
            p3 = p3.getNext();
        }

        while (p2 != null) {
            res = p2.getValue() + overflow;
            overflow = res / 10;
            res = res % 10;
            p3.setValue(res);

            p2 = p2.getNext();
            if (p2 != null) {
                p3.setNext(new Node(0));
            }
            p3 = p3.getNext();
        }

        if (overflow > 0) {
            p3.setValue(overflow);
        }

        return newNumber;
    }

    @Override
    public int compareTo(BigNumber number) {

        int length = this.length();
        int numLength = number.length();

        if (length == numLength) {
            reverse();
            number.reverse();
            Node p1 = head;
            Node p2 = number.getHead();

            while (p1 != null) {
                if (p1.getValue() != p2.getValue()) {
                    int m = p1.getValue();
                    int n = p2.getValue();
                    this.reverse();
                    number.reverse();
                    return m - n;
                }
                p1 = p1.getNext();
                p2 = p2.getNext();
            }
        }
        return length - numLength;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        Node ptr = head;
        while (ptr != null) {
            sb.append(ptr.getValue());
            ptr = ptr.getNext();
        }
        return sb.reverse().toString();
    }

    public void reverse() {
        Node n = this.head;
        Node prev = null;
        Node next;
        while (n != null) {
            next = n.getNext();
            n.setNext(prev);
            prev = n;
            n = next;
        }
        this.head = prev;
    }

    public Node getHead() {
        return head;
    }

    public void setHead(Node head) {
        this.head = head;
    }
}
