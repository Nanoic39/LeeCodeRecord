package Tencent.LeeCode.Array_Hash_String_49;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;
import java.util.List;

class Solution {
    public List<List<String>> groupAnagrams(String[] strs) {

        // 边界
        if (strs.length == 1) {
            return Arrays.asList(Arrays.asList(strs));
        }

        // 哈希表，储存排序后的字符串和相关的所有异位词
        Map<String, List<String>> map = new HashMap<>();

        // 遍历每个字符串
        for (String str : strs) {
            char[] charArray = str.toCharArray();
            Arrays.sort(charArray);
            String sortedStr = new String(charArray);
            // 如果哈希表中不存在这个排序后的字符串，就创建一个新的列表
            if (!map.containsKey(sortedStr)) {
                map.put(sortedStr, new ArrayList<>());
            }
            // 把当前字符串加入到对应的列表中
            map.get(sortedStr).add(str);
        }

        // 将Map的值转换为结果
        return new ArrayList<>(map.values());
    }
}
