package com.example.asus.KMP;

/**
 * Created by asus on 2016/10/9.
 */
public class KMP {


    /**
     * 获得字符串的next函数值
     *
     * @param t 字符串
     * @return next函数值
     */
    public static int[] next(char[] t) {
        int[] next = new int[t.length];
        next[0] = -1;
        int i = 0;
        int j = -1;
        while (i < t.length - 1) {
            if (j == -1 || t[i] == t[j]) {
                i++;
                j++;
                if (t[i] != t[j]) {
                    next[i] = j;
                } else {
                    next[i] = next[j];
                }
            } else {
                j = next[j];
            }
        }
        return next;
    }

    /**
     * KMP匹配字符串
     *
     * @param s 主串
     * @param t 模式串
     * @return 若匹配成功，返回下标，否则返回-1
     */
    public static int KMP_Index(char[] s, char[] t) {
        int[] next = next(t);
        int i = 0;
        int j = 0;
        while (i <= s.length - 1 && j <= t.length - 1) {
            if (j == -1 || s[i] == t[j]) {
                i++;
                j++;
            } else {
                j = next[j];
            }
        }
        if (j < t.length) {
            return -1;
        } else
            return i - t.length; // 返回模式串在主串中的头下标
    }

    public static final String[] SENSITIVE_WORDS = new String[]{"滚", "cao", "操", "滚蛋", "滚犊子",
            "傻瓜", "傻逼", "变态", "蠢",
            "草", "尼玛", "你妈", "你他妈",
            "妈的", "操你妈", "日", "日你妹", "问候你祖宗", "去死", "去死吧", "脑子有病", "猪脑",
            "奶", "奶奶的", "大爷", "畜生", "扑街",
            "猪", "傻", "妈的智障", "妈","混蛋","你爹","你妹","爸"};

}
