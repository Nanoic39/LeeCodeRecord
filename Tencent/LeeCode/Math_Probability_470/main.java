package Tencent.LeeCode.Math_Probability_470;

/**
 * The rand7() API is already defined in the parent class SolBase.
 * public int rand7();
 * @return a random integer in the range 1 to 7
 */
class Solution extends SolBase {
    // 没太看懂，看题解就是构造7*7的表格算每个数字概率，然后选择其中十个概率一样的
    public int rand10() {
        int row, col, idx;
        do {
            row = rand7();
            col = rand7();
            idx = col + (row - 1) * 7;
        } while (idx > 40);

        return 1 + (idx - 1) % 10;
    }
}