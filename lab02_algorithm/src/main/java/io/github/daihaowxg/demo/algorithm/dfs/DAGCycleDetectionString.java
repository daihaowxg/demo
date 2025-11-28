package io.github.daihaowxg.demo.algorithm.dfs;

import java.util.*;

public class DAGCycleDetectionString {
    private static List<String> cyclePath = new ArrayList<>(); // 存储环的路径

    /**
     * 深度优先搜索 (DFS) 检测环
     *
     * @param graph   有向图的邻接表表示
     * @param node    当前访问的节点
     * @param visited 记录节点访问状态 (0: 未访问, 1: 访问中, 2: 访问完成)
     * @param path    记录当前 DFS 访问路径，用于回溯获取环
     * @return 如果检测到环返回 true，否则返回 false
     */
    private static boolean hasCycle(Map<String, List<String>> graph, String node, Map<String, Integer> visited, Stack<String> path) {
        if (visited.get(node) == 1) { // 发现环
            // **回溯找到环的路径**
            int index = path.indexOf(node);
            cyclePath = path.subList(index, path.size()); // 取出环的部分
            return true;
        }
        if (visited.get(node) == 2) return false; // 该节点已访问完成，跳过

        visited.put(node, 1); // 标记当前节点为 "访问中"
        path.push(node); // 记录路径

        // 遍历所有邻居节点
        for (String neighbor : graph.getOrDefault(node, new ArrayList<>())) {
            if (!visited.containsKey(neighbor)) {
                visited.put(neighbor, 0); // 确保所有节点被初始化
            }
            if (hasCycle(graph, neighbor, visited, path)) {
                return true; // 发现环，立即返回
            }
        }

        visited.put(node, 2); // 访问完成，标记为 "已访问"
        path.pop(); // 回溯，移除当前节点
        return false;
    }

    /**
     * 入口方法: 检测有向图是否存在环
     *
     * @param graph 有向图的邻接表表示
     * @return 如果存在环返回 true，否则返回 false
     */
    public static boolean detectCycleInDAG(Map<String, List<String>> graph) {
        Map<String, Integer> visited = new HashMap<>(); // 记录节点访问状态
        Stack<String> path = new Stack<>(); // 记录访问路径

        // **初始化所有节点的访问状态为 0 (未访问)**
        for (String node : graph.keySet()) {
            visited.put(node, 0);
        }

        // **遍历所有未访问的节点，执行 DFS 检测环**
        for (String node : graph.keySet()) {
            if (visited.get(node) == 0) {
                if (hasCycle(graph, node, visited, path)) {
                    return true; // 存在环
                }
            }
        }
        return false;
    }

    /**
     * 主方法: 运行环检测示例
     */
    public static void main(String[] args) {
        // **构造有向图 (邻接表表示)**
        Map<String, List<String>> graph = new HashMap<>();
        graph.put("A", Arrays.asList("B"));
        graph.put("B", Arrays.asList("C"));
        graph.put("C", Arrays.asList("G", "E", "D"));
        graph.put("D", Arrays.asList("B")); // 形成环 B → C → D → B
        graph.put("E", Arrays.asList("F"));

        // **执行环检测**
        boolean hasCycle = detectCycleInDAG(graph);

        // **输出检测结果**
        System.out.println("DAG 是否有环: " + hasCycle);
        if (hasCycle) {
            System.out.println("环的路径: " + cyclePath);
        }
    }
}
