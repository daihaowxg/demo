package io.github.daihaowxg.demo.algorithm.dfs;

import java.util.*;

/**
 * DAG（有向无环图）的环检测
 * <p>
 *     在 DFS 遍历过程中，使用三色标记法来标记节点的状态：
 *     <li>白色（未访问，0）：该节点尚未被访问。</li>
 *     <li>灰色（正在访问，1）：该节点正在 DFS 递归过程中，表示当前路径上的节点。</li>
 *     <li>黑色（已访问完成，2）：该节点及其所有子节点均已被访问。</li>
 *     如果在 DFS 过程中访问到灰色节点，则说明图中存在环。
 * </p>
 */
public class DAGCycleDetection {

    /**
     * 使用 DFS 算法检测图中是否存在环
     *
     * @param graph   图的邻接表表示 (键: 节点, 值: 相邻节点列表)
     * @param node    当前访问的节点
     * @param visited 存储节点状态的数组 (0: 未访问, 1: 访问中, 2: 已访问完成)
     * @return 如果存在环返回 true，否则返回 false
     */
    private static boolean hasCycle(Map<Integer, List<Integer>> graph, int node, int[] visited) {
        // 如果节点已经在当前路径中（灰色），说明存在环
        if (visited[node] == 1) return true;

        // 如果节点已经访问完成（黑色），则返回 false
        if (visited[node] == 2) return false;

        // 标记为正在访问（灰色）
        visited[node] = 1;

        // 递归访问所有相邻节点
        for (int neighbor : graph.getOrDefault(node, new ArrayList<>())) {
            if (hasCycle(graph, neighbor, visited)) {
                return true; // 如果发现环，立即返回
            }
        }

        // 访问完成，标记为已访问（黑色）
        visited[node] = 2;
        return false;
    }

    /**
     * 检测整个图是否存在环
     *
     * @param graph 图的邻接表表示
     * @param n     图中节点的数量
     * @return 如果存在环返回 true，否则返回 false
     */
    public static boolean detectCycleInDAG(Map<Integer, List<Integer>> graph, int n) {
        int[] visited = new int[n]; // 0: 未访问, 1: 访问中, 2: 已访问完成

        // 遍历每个节点，执行 DFS 检测
        for (int node = 0; node < n; node++) {
            if (visited[node] == 0) { // 如果节点未访问过，执行 DFS
                if (hasCycle(graph, node, visited)) {
                    return true; // 如果检测到环，返回 true
                }
            }
        }

        return false; // 如果没有检测到环，返回 false
    }

    /**
     * 主方法：执行环检测示例
     */
    public static void main(String[] args) {
        // 构建有向图的邻接表表示
        Map<Integer, List<Integer>> graph = new HashMap<>();
        graph.put(0, Arrays.asList(1, 2));
        graph.put(1, Arrays.asList(2));
        graph.put(2, Arrays.asList(3));
        graph.put(3, Arrays.asList(4));
        graph.put(4, Arrays.asList(1)); // 形成环 1 → 2 → 3 → 4 → 1

        int n = 5; // 图中节点的数量
        boolean hasCycle = detectCycleInDAG(graph, n); // 执行环检测
        System.out.println("DAG 是否有环: " + hasCycle); // 输出检测结果
    }
}
