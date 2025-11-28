package io.github.daihaowxg.demo.algorithm.dfs;

import java.util.*;

/**
 * 基于递归遍历实现深度优先搜索 (DFS)
 * DFS 从起始节点出发，递归遍历所有可达的节点。
 * 如果节点已被访问，避免重复访问，保证每个节点只访问一次。
 */
public class DFSTraversal {

    /**
     * 深度优先搜索 (DFS) 遍历图
     *
     * @param graph   图的邻接表表示 (键: 节点, 值: 相邻节点列表)
     * @param node    当前访问的节点
     * @param visited 记录已访问节点的集合
     */
    private static void dfs(Map<Integer, List<Integer>> graph, int node, Set<Integer> visited) {
        if (visited.contains(node)) return; // 如果当前节点已访问过，则跳过

        System.out.print(node + " "); // 打印当前节点，表示访问该节点
        visited.add(node); // 将当前节点标记为已访问

        // 遍历当前节点的所有邻居，递归访问未访问的邻居
        for (int neighbor : graph.getOrDefault(node, new ArrayList<>())) {
            dfs(graph, neighbor, visited); // 递归访问邻居节点
        }
    }

    /**
     * 主方法: 执行 DFS 遍历示例
     */
    public static void main(String[] args) {
        // 构造图的邻接表表示
        Map<Integer, List<Integer>> graph = new HashMap<>();
        graph.put(0, Arrays.asList(1, 2)); // 节点 0 与节点 1 和 2 相邻
        graph.put(1, Arrays.asList(0, 3, 4)); // 节点 1 与节点 0, 3 和 4 相邻
        graph.put(2, Arrays.asList(0, 5, 6)); // 节点 2 与节点 0, 5 和 6 相邻
        graph.put(3, Arrays.asList(1)); // 节点 3 与节点 1 相邻
        graph.put(4, Arrays.asList(1, 7)); // 节点 4 与节点 1 和 7 相邻
        graph.put(5, Arrays.asList(2)); // 节点 5 与节点 2 相邻
        graph.put(6, Arrays.asList(2)); // 节点 6 与节点 2 相邻
        graph.put(7, Arrays.asList(4)); // 节点 7 与节点 4 相邻

        // 用于记录已访问节点的集合
        Set<Integer> visited = new HashSet<>();

        // 执行 DFS 遍历并打印结果
        System.out.print("DFS 递归遍历: ");
        dfs(graph, 0, visited); // 从节点 0 开始执行 DFS
    }
}
