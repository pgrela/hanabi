package com.pgrela;

import java.util.ArrayList;
import java.util.List;

public class S {
    public static void main(String[] args) {
        System.out.println(getX("AAABBBCCCCC",4));
    }
    static class P{
        int n;
        char c;
        public P(int n, char c) {
            this.n = n;
            this.c = c;
        }
    }

    private static int getX(String S, int K) {
        char[] chars = S.toCharArray();
        int[]h=new int[chars.length];
        char c=chars[0];
        List<P> ps=new ArrayList<>();
        int n=0;
        for (int i = 0; i < chars.length; i++) {
            if(chars[i]==c)++n;else{
                
            }
        }
        for (int i = 0; i < chars.length - K; i++) {
            int left;
            int right;
        }
        return 5;
    }
}
