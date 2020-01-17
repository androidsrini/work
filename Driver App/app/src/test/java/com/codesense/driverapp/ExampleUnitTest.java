package com.codesense.driverapp;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        String value = "123456789";
        Long l = new Long(value);
        System.out.print(l+4L);
        assertEquals(4, 2 + 2);
    }

    @Test
    public void find_count_test() {
        String str = "srinivasan";
        Node[] nodes = new Node[str.length()];
        char[] cArray = str.toCharArray();
        for (int index=0; index<cArray.length; index++) {
            //find value are availabel and index
            char c = cArray[index];
            int indexValue = index;
            for (int i=0; i<nodes.length; i++) {
                Node node = nodes[i];
                if (null != node && c == node.c) {
                    indexValue = i;
                    break;
                }
            }
            if (index == indexValue) {
                nodes[indexValue] = new Node(c, 1);
            } else {
                nodes[indexValue] = new Node(c,  nodes[indexValue].count + 1);
            }
        }
        for (Node node: nodes) {
            if (null != node)
                System.out.println(" " + node.c + " : " + node.count);
        }
    }

    class Node {
        int count;
        char c;
        Node(char c, int count) {
            this.c = c;
            this.count = count;
        }
    }
}