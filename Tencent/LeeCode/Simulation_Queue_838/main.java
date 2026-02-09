class Solution {
    StringBuilder resStr = new StringBuilder();

    public String pushDominoes(String dominoes) {
        int left = 0;
        char[] arr = dominoes.toCharArray();

        boolean isFirst = true;

        while (left < dominoes.length()) {
            // 定位推力源
            while (left < dominoes.length() && arr[left] == '.') {
                left++;
            }        
            // 遍历结束仍未找到有效推力
            if (left >= dominoes.length()) { 
                break;
            }
            // 先特殊处理开头到第一个推力的情况
            // 如果第一个推力是L，左侧的.都需要变成L
            if (isFirst) {
                if (arr[left] == 'L') {
                    // 我了个魔法数字 -1 ，特殊情况：
                    // 因为下面的是默认认为left不会是.，因此是从left+1开始编辑
                    // 所以这里要使用-1，才能从0开始编辑
                    handle(arr, -1, left, ' ', 'L');
                }
                isFirst = !isFirst;
            }
            
            // 找到左推力后开始寻找右推力
            int right = left + 1;
            while (right < dominoes.length() && arr[right] == '.') {
                right++;
            }
            // 处理[left, right)
            handle(arr, left, right, arr[left], (right < dominoes.length()) ? arr[right] : ' ');

            // 处理完区间后将left定位至right
            left = right;
        }

        return new String(arr);
    }

    private void handle(char[] arr, int l, int r, char le, char ri){
        int len = r - l - 1; // 区间实际长度
        if (len <= 0) {
            return; // 长度为零无结果
        }
        
        // 左R右L
        if (le == 'R' && ri == 'L') {
            int head = l + 1;
            int tail = r - 1;
            // head == tail 时左右同时到达，此时保持为.不变即可
            while (head < tail) {
                arr[head++] = 'R';
                arr[tail--] = 'L';
            }
        }

        // 左L/.右L
        if(ri == 'L' && (le == 'L' || le == ' ')) {
            // 范围内全部为L
            for (int i = l + 1; i < r; i++) {
                arr[i] = 'L';
            }
        }

        // 左R右R/.
        if(le == 'R' && (ri == 'R' || ri == ' ')) {
            for (int i = l + 1; i < r; i++) {
                arr[i] = 'R';
            }
        }

        // 左L + 右R/. → 中间保持.
        // 左L/. + 右R → 中间保持.
    }
}