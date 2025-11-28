package io.github.daihaowxg.demo.algorithm.sort;

/**
 * 冒泡排序
 * @author wxg
 * @since 2025/4/9
 */
public class BubbleSort {

    // 输入：6 3 9 4 1
    // 输出：1 3 4 6 9

    public static void main(String[] args) {
        int[] arr = {6, 3, 9, 4, 1};
        bubbleSort2(arr);
        for (int i : arr) {
            System.out.print(i + " ");
        }
    }

    /**
     * 冒泡排序（升序）
     * - 第一次外部循环 (i = 0): 内部循环会遍历整个未排序的数组（除了最后一个元素），比较相邻的元素。如果前一个元素比后一个元素大，则交换它们。经过这次循环，最大的元素会被移动到数组的最后一个位置。
     * - 第二次外部循环 (i = 1): 内部循环会遍历除了最后一个已经排好序的元素之外的剩余未排序部分。这次循环会将剩余未排序部分中最大的元素移动到倒数第二个位置。
     * - 以此类推: 每次外部循环都会将当前未排序部分中最大的元素移动到其最终排序位置。
     * @param arr
     */
    public static void bubbleSort(int[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {
            for (int j = 0; j < arr.length - 1 - i; j++) {
                if (arr[j] > arr[j + 1]) {
                    int temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                }
            }
        }
    }

    /**
     * 降序冒泡排序
     */
    public static void bubbleSort2(int[] arr) {
        for (int i = 0; i < arr.length - 1; i++) {
            for (int j = 0; j < arr.length - 1 - i; j++) {
                if (arr[j] < arr[j + 1]) { // 这里变成了小于号
                    int temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                }
            }
        }
    }
}
