package month9year26;

import java.util.Arrays;

public class KMPTrace {
    public static void main(String[] args) {
        String s = "aabaaf";   // 可以换成其他字符串观察，比如 "ababaca"
        System.out.println("模式串: " + s);
        System.out.println("目标: 对每个 i，求 s[0..i] 的最长公共前后缀长度\n");

        int[] next = new int[s.length()];
        getNext(next, s);

        System.out.println("\n最终 next 表: " + Arrays.toString(next));

        // 顺便演示主匹配
        String haystack = "aabaabaafa";
        System.out.println("\n在 \"" + haystack + "\" 中查找 \"" + s + "\":");
        int idx = strStr(haystack, s, next);
        System.out.println("结果下标: " + idx);
    }

    private static void getNext(int[] next, String s) {
        int j = 0;
        next[0] = 0;
        for (int i = 1; i < s.length(); i++) {
            System.out.printf("--- i=%d, 处理子串 \"%s\", 进入时 j=%d ---%n",
                    i, s.substring(0, i + 1), j);

            // 失配回退：利用 next[j-1] 跳过更短的候选前缀
            while (j > 0 && s.charAt(j) != s.charAt(i)) {
                System.out.printf("  失配: s[%d]='%c' != s[%d]='%c'，j 从 %d 回退到 next[%d]=%d%n",
                        j, s.charAt(j), i, s.charAt(i), j, j - 1, next[j - 1]);
                j = next[j - 1];
            }

            // 当前字符匹配，前后缀长度 +1
            if (s.charAt(j) == s.charAt(i)) {
                System.out.printf("  匹配: s[%d]='%c' == s[%d]='%c'，j: %d -> %d%n",
                        j, s.charAt(j), i, s.charAt(i), j, j + 1);
                j++;
            } else {
                System.out.printf("  仍不匹配: s[%d]='%c' != s[%d]='%c'，j 保持 0%n",
                        j, s.charAt(j), i, s.charAt(i));
            }

            next[i] = j;
            System.out.printf("  => next[%d] = %d%n", i, j);
        }
    }

    private static int strStr(String haystack, String needle, int[] next) {
        if (needle.length() == 0) return 0;

        int j = 0;
        for (int i = 0; i < haystack.length(); i++) {
            while (j > 0 && needle.charAt(j) != haystack.charAt(i)) {
                System.out.printf("  主匹配失配: haystack[%d]='%c' != needle[%d]='%c'，j 回退到 %d%n",
                        i, haystack.charAt(i), j, needle.charAt(j), next[j - 1]);
                j = next[j - 1];
            }
            if (needle.charAt(j) == haystack.charAt(i)) j++;
            if (j == needle.length()) return i - needle.length() + 1;
        }
        return -1;
    }
}
