import java.lang.Integer;
import java.util.Arrays;

public class ArbINTrary {

	String value;
	byte[] numArr;
	byte sign;
	
	public ArbINTrary() {
		value = "0";
		numArr = createArray(value);
	}

	public ArbINTrary(String inputVal) {
		value = inputVal;
		this.initialize();
		numArr = createArray(value);
	}

	public int getLength() {
		return this.numArr.length;
	}

	public byte getSign() {
		return this.sign;
	}

	public byte[] getArray() {
		return this.numArr;
	}

	public String toString() {
		String to = "";
		if (sign > 0) {
			to = "+";
		} else if (sign < 0) {
			to = "-";
		}
		for (int i = this.numArr.length - 1; i >= 0; i--) {
			to += Math.abs(this.numArr[i]);
		}
		return to;
	}

	public ArbINTrary abs() {
		ArbINTrary changed = new ArbINTrary(this.toString());
		if (this.sign < 0) {
			changed.setSign((byte) 1);
			return changed;
		}
		return changed;
	}

	public void initialize() {
		this.value = this.value.trim();
		if (this.value.contains("-")) {
			this.sign = -1;
			this.value = this.value.replace("-", "");
		} else {
			this.sign = 1;
			this.value = this.value.replace("+", "");
		}
		if (this.value.replace("0", "").equals("")){
			this.sign = 0;
			this.value = "0";
		}
		if (this.sign != 0) {
			while (this.value.substring(0, 1).equals("0")) {
				this.value = this.value.replaceFirst("0", "");
			}
		}
	}

	public byte[] createArray(String s) {
		byte[] tempArr = new byte[s.length()];
		for (int i = 0; i < tempArr.length; i++) {
			tempArr[i] = Byte.parseByte(String.valueOf(s.charAt(s.length() - 1 - i)));
		}
		return tempArr;
	}

	public byte valueAt(int index) {
		return this.numArr[index];
	}

	public void setValue(int index, byte val) {
		this.numArr[index] = val;
	}

	public void setSign(byte newSign) {
		this.sign = newSign;
	}

	public void changeSign() {
		if (this.sign != (byte) 0) {
			for (int i = 0; i < this.numArr.length; i++) {
				this.setValue(i, (byte) (this.valueAt(i) * -1));
			}
		}
	}

	public boolean equals(ArbINTrary that) {
		if ((this.numArr.length != that.getLength()) || (this.sign != that.getSign())) {
			return false;
		}
		int limit = this.numArr.length;
		for (int i = 0; i < limit; i++) {
			if (this.valueAt(i) != that.valueAt(i)) {
				return false;
			}
		}
		return true;
	}

	public boolean isGreaterThan(ArbINTrary that) {
		if (this.equals(that)) {
			return false;
		} else if (this.sign > that.getSign()) {
			return true;
		} else if (this.sign < that.getSign()) {
			return false;
		} else if (this.numArr.length < that.getLength() && this.sign * that.getSign() > 0) {
			return false;
		} else if (this.numArr.length > that.getLength() && this.sign * that.getSign() > 0) {
			return true;
		} else {
			int limit = this.numArr.length;
			for (int i = limit - 1; i >= 0; i--) {
				if (this.valueAt(i) > that.valueAt(i)) {
					return true;
				} else if (this.valueAt(i) < that.valueAt(i)) {
					return false;
				}
			}
		}
		return false;
	}

	public boolean isLessThan(ArbINTrary that) {
		if (!this.isGreaterThan(that) && !this.equals(that)) {
			return true;
		}
		return false;
	}

	public ArbINTrary plus(ArbINTrary that) {
		ArbINTrary smaller = this.abs().isLessThan(that.abs()) ? new ArbINTrary(this.toString()): new ArbINTrary(that.toString());
		ArbINTrary bigger = this.abs().isGreaterThan(that.abs()) ? new ArbINTrary(this.toString()): new ArbINTrary(that.toString());
		byte[] lowArray = smaller.getArray();
		byte[] highArray = bigger.getArray();

		if (this.abs().equals(that.abs())) {
			smaller = new ArbINTrary(this.toString());
			bigger = new ArbINTrary(that.toString());
			lowArray = smaller.getArray();
			highArray = bigger.getArray();
		}

		int lowLimit = lowArray.length;
		int highLimit = highArray.length;
		int limit = highArray.length + 1;
		byte[] tempAnswer = new byte[limit];
		boolean changedSmaller = false;
		boolean changedBigger = false;
		boolean shouldBeNeg = false;
		byte sum;
		byte carry = 1;
		String answerString = "";

		if (smaller.getSign() < 0 && bigger.getSign() >= 0) {
			smaller.changeSign();
			changedSmaller = true;
		} else if (smaller.getSign() >= 0 && bigger.getSign() < 0) {
			smaller.changeSign();
			changedSmaller = true;
			shouldBeNeg = true;
		} else if (smaller.getSign() < 0 && bigger.getSign() < 0) {
			shouldBeNeg = true;
		}

		for (int i = 0; i < lowLimit; i++) {
			sum = (byte) (highArray[i] + lowArray[i]);
			tempAnswer[i] += sum;
			if (tempAnswer[i] > 9) {
				tempAnswer[i + 1] += carry;
				tempAnswer[i] -= 10;
			}
			if (tempAnswer[i] < 0) {
				tempAnswer[i + 1] -= carry;
				tempAnswer[i] += 10;
			}
		}
		for (int i = lowLimit; i < highLimit; i++) {
			tempAnswer[i] += highArray[i];
			if (tempAnswer[i] > 9) {
				tempAnswer[i + 1] += carry;
				tempAnswer[i] -= 10;
			}
			if (tempAnswer[i] < 0) {
				tempAnswer[i + 1] -= carry;
				tempAnswer[i] += 10;
			}
		}
		for (int i = limit - 1; i >= 0; i--) {
			answerString += tempAnswer[i];
		}
		if (changedSmaller) {
			smaller.changeSign();
		}
		if (changedBigger) {
			bigger.changeSign();
		}
		if (shouldBeNeg) {
			answerString = "-" + answerString;
		}
		return new ArbINTrary(answerString);
	}

	public ArbINTrary minus(ArbINTrary that) {
		return this.plus((that.times(new ArbINTrary("-1"))));
	}

	public ArbINTrary times(ArbINTrary that) {
		if (this.equals(new ArbINTrary()) || that.equals(new ArbINTrary())) {
			return new ArbINTrary();
		}

		boolean minVal = false;

		if (that.toString().equals("-2147483648")) {
			minVal = true;
			that = that.plus(new ArbINTrary("1"));
		}

		ArbINTrary smaller = this.abs().isLessThan(that) ? this.abs() : that.abs();
		ArbINTrary bigger = this.abs().isGreaterThan(that) ? this.abs() : that.abs();

		if (this.abs().equals(that.abs())) {
			smaller = new ArbINTrary(this.toString());
			bigger = new ArbINTrary(that.toString());
		}

		ArbINTrary biggerInt = new ArbINTrary(bigger.toString().substring(1));
		ArbINTrary one = new ArbINTrary("1");
		ArbINTrary product = new ArbINTrary();

		if (smaller.valueAt(0) % 2 > 0) {
			product = product.plus(biggerInt);
		}

		while (smaller.isGreaterThan(one)) {
			biggerInt = biggerInt.plus(biggerInt);
			smaller = smaller.divideByTwo();
			if (smaller.valueAt(0) % 2 > 0) {
				product = product.plus(biggerInt);
			}
		}
		if (minVal) {
			product = product.plus(bigger);
		}
		if (this.getSign() != that.getSign()) {
			product.setSign((byte) -1);
		}
		return product;
	}

	public ArbINTrary divideByTwo() {
		ArbINTrary dividend = new ArbINTrary(this.toString());
        byte answer[] = new byte[this.numArr.length];
        byte carry = 1;
        String answerString = "";
        for (int i = dividend.getLength() - 1; i >= 0; i--) {
        	answer[i] = (byte) (dividend.valueAt(i) / 2);
        	if (dividend.valueAt(i) % 2 != 0 && i != 0) {
        		dividend.setValue(i - 1, (byte) (dividend.valueAt(i - 1) + carry * 10));
        	}
        }
        for (int i = this.numArr.length - 1; i >= 0; i--) {
            answerString += answer[i];
        }

        if (this.getSign() < 0) {
        	answerString = "-" + answerString;
        }

        return new ArbINTrary(answerString);
    }

    public ArbINTrary dividedBy(ArbINTrary that) {
    	try {
    		if (that.equals(new ArbINTrary())) {
    			throw new ArithmeticException("Arithmetic Exception: ArbINTrary divide by zero");
    		}

    		ArbINTrary dividend = new ArbINTrary(this.abs().toString());
    		ArbINTrary divisor = new ArbINTrary(that.abs().toString());
    		ArbINTrary quotient = new ArbINTrary();
    		ArbINTrary compare = new ArbINTrary();
    		ArbINTrary difference = new ArbINTrary(divisor.toString());
    		int power = 30;
    		String increment = "1000000000000000000000000000000000000000000000000000000000000";
    		ArbINTrary incremInt;

    		if (dividend.isLessThan(divisor)) {
    			return new ArbINTrary();
    		}

    		while (dividend.isGreaterThan(compare) && !difference.abs().isLessThan(divisor)) {
    			incremInt = new ArbINTrary(increment);
    			quotient = quotient.plus(incremInt);
    			compare = quotient.times(divisor);
    			difference = dividend.minus(compare);

    			if (!dividend.isGreaterThan(compare) && !difference.abs().isLessThan(divisor)) {
    				compare = new ArbINTrary();
    				quotient = quotient.minus(incremInt);
    				power -= 1;
    				increment = increment.substring(0, increment.length() - 1);
    			}
    			if (difference.abs().isLessThan(divisor) && !increment.equals("1")) {
    				compare = new ArbINTrary();
    				quotient = quotient.minus(incremInt);
    				difference = new ArbINTrary(divisor.toString());
    				power -= 1;
    				increment = "1";
    			}
    			
    		}
    		if (this.sign * that.getSign() < 0) {
    			quotient.setSign((byte) -1);
    		}

    		return quotient;
    	} catch (ArithmeticException e) {
    		System.out.println(e.getMessage());
    		return null;
    	}
    }

	public ArbINTrary modulo(ArbINTrary that) {
		return this.abs().minus(this.dividedBy(that).times(that).abs());
	}
}