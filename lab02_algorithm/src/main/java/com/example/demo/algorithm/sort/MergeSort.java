package com.example.demo.algorithm.sort;

/**
 * 归并排序算法实现
 *
 * @author wxg
 * @since 2025/4/9
 */
public class MergeSort {

    /**
     * 归并排序的入口方法
     *
     * @param arr 待排序的整型数组
     */
    public static void mergeSort(int[] arr) {
        // 如果数组为空或只有一个元素，则无需排序，直接返回
        if (arr == null || arr.length <= 1) {
            return;
        }
        // 创建一个与原数组大小相同的临时数组，用于在合并过程中存储数据
        int[] temp = new int[arr.length];
        // 调用递归的归并排序方法进行排序
        mergeSort(arr, 0, arr.length - 1, temp);
    }

    /**
     * 递归地将数组分成两半并进行排序和合并
     *
     * @param arr   待排序的整型数组
     * @param left  当前处理子数组的左边界索引
     * @param right 当前处理子数组的右边界索引
     * @param temp  用于合并操作的临时数组
     */
    private static void mergeSort(int[] arr, int left, int right, int[] temp) {
        // 当左边界小于右边界时，说明子数组的长度大于 1，需要继续分割
        if (left < right) {
            // 计算中间索引，将数组分成左右两部分
            int mid = left + (right - left) / 2; // 防止 (left + right) / 2 可能导致的整数溢出
            // 递归排序左半部分 [left, mid]
            mergeSort(arr, left, mid, temp);
            // 递归排序右半部分 [mid + 1, right]
            mergeSort(arr, mid + 1, right, temp);
            // 合并已经排序好的左右两部分
            merge(arr, left, mid, right, temp);
        }
    }

    /**
     * 合并两个已排序的子数组
     *
     * @param arr   原始数组
     * @param left  左边有序子数组的起始索引
     * @param mid   左边有序子数组的结束索引，也是右边有序子数组的前一个索引
     * @param right 右边有序子数组的结束索引
     * @param temp  用于存储合并结果的临时数组
     */
    private static void merge(int[] arr, int left, int mid, int right, int[] temp) {
        // 初始化左边子数组的起始指针
        int i = left;
        // 初始化右边子数组的起始指针
        int j = mid + 1;
        // 初始化临时数组的索引
        int k = 0;

        // 比较左右两个子数组的元素，将较小的元素放入临时数组
        while (i <= mid && j <= right) {
            if (arr[i] <= arr[j]) {
                temp[k++] = arr[i++];
            } else {
                temp[k++] = arr[j++];
            }
        }

        // 将左边子数组剩余的元素放入临时数组
        while (i <= mid) {
            temp[k++] = arr[i++];
        }

        // 将右边子数组剩余的元素放入临时数组
        while (j <= right) {
            temp[k++] = arr[j++];
        }

        // 将临时数组中已排序的元素复制回原始数组的相应位置
        for (int l = 0; l < k; l++) {
            arr[left + l] = temp[l];
        }
    }

    /**
     * 主方法，用于测试归并排序
     *
     * @param args 命令行参数
     */
    public static void main(String[] args) {
        int[] arr = {8, 3, 1, 7, 0, 10, 2};
        System.out.println("排序前数组： " + java.util.Arrays.toString(arr));
        mergeSort(arr);
        System.out.println("排序后数组： " + java.util.Arrays.toString(arr)); // 输出: [0, 1, 2, 3, 7, 8, 10]
    }
}