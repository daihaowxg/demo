package io.github.daihaowxg.demo.algorithm.sort;

/**
 * 快速排序
 * @author wxg
 * @since 2025/4/9
 */
public class QuickSort {

    public static void main(String[] args) {
        int[] arr = {7, 2, 1, 6, 8, 5, 3, 4};
        System.out.println("排序前数组： " + java.util.Arrays.toString(arr));
        quickSort(arr, 0, arr.length - 1);
        System.out.println("排序后数组： " + java.util.Arrays.toString(arr)); // 输出: [1, 2, 3, 4, 5, 6, 7, 8]
    }

    public static void quickSort(int[] arr, int low, int high) {
        if (low < high) {
            // 分区操作，获取基准元素的最终位置
            int partitionIndex = partition(arr, low, high);

            // 递归地对基准左边的子数组进行排序
            quickSort(arr, low, partitionIndex - 1);

            // 递归地对基准右边的子数组进行排序
            quickSort(arr, partitionIndex + 1, high);
        }
    }

    private static int partition(int[] arr, int low, int high) {
        // 选择最后一个元素作为基准
        int pivot = arr[high];
        // 指向小于基准的元素的最后一个位置
        int i = (low - 1);

        for (int j = low; j < high; j++) {
            // 如果当前元素小于或等于基准
            if (arr[j] <= pivot) {
                // 将 i 加 1，并交换 arr[i] 和 arr[j]
                i++;
                swap(arr, i, j);
            }
        }

        // 将基准元素放到它最终排序后的位置
        swap(arr, i + 1, high);

        // 返回基准元素的索引
        return (i + 1);
    }

    private static void swap(int[] arr, int i, int j) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }
}