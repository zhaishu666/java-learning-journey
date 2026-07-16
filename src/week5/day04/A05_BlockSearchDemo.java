package week5.day04;

public class A05_BlockSearchDemo {
    static void main(String[] args) {
        int[] arr = {27, 22, 30, 40, 36,
                13, 19, 16, 20,
                7, 10,
                43, 50, 48
        };

        Block b1 = new Block(22, 40, 0, 4);
        Block b2 = new Block(13, 20, 5, 8);
        Block b3 = new Block(7, 10, 9, 10);
        Block b4 = new Block(43, 50, 11, 13);

        Block[] blockArr = {b1,b2,b3,b4};

        int num = 16;

        int index = getIndex(blockArr, num, arr);
        System.out.println(index);
    }

    public static int getIndex(Block[] blockArr, int num, int[] arr) {
        for (int i = 0; i < blockArr.length; i++) {
            if(num > blockArr[i].getMin() && num < blockArr[i].getMax()){
                for (int j = blockArr[i].getStratIndex(); j < blockArr[i].endIndex; j++) {
                    if(arr[j] == num){
                        return j;
                    }
                }
            }
        }
        return -1;
    }
}

class Block {
    int min;
    int max;
    int stratIndex;
    int endIndex;

    public Block(int min, int max, int stratIndex, int endIndex) {
        this.min = min;
        this.max = max;
        this.stratIndex = stratIndex;
        this.endIndex = endIndex;
    }

    public Block() {
    }

    /**
     * 获取
     * @return min
     */
    public int getMin() {
        return min;
    }

    /**
     * 设置
     * @param min
     */
    public void setMin(int min) {
        this.min = min;
    }

    /**
     * 获取
     * @return max
     */
    public int getMax() {
        return max;
    }

    /**
     * 设置
     * @param max
     */
    public void setMax(int max) {
        this.max = max;
    }

    /**
     * 获取
     * @return stratIndex
     */
    public int getStratIndex() {
        return stratIndex;
    }

    /**
     * 设置
     * @param stratIndex
     */
    public void setStratIndex(int stratIndex) {
        this.stratIndex = stratIndex;
    }

    /**
     * 获取
     * @return endIndex
     */
    public int getEndIndex() {
        return endIndex;
    }

    /**
     * 设置
     * @param endIndex
     */
    public void setEndIndex(int endIndex) {
        this.endIndex = endIndex;
    }

    public String toString() {
        return "Block{min = " + min + ", max = " + max + ", stratIndex = " + stratIndex + ", endIndex = " + endIndex + "}";
    }
}
