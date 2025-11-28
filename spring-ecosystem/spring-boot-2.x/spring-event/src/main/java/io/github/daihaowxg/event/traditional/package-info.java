/**
 * 传统方式实现（不使用事件机制）
 * <p>
 * 本包展示传统的直接调用方式，用于对比说明事件机制的优势
 * <p>
 * 特点：
 * - ❌ 高耦合：UserService 需要依赖所有后续服务
 * - ❌ 难扩展：添加新功能需要修改 UserService 代码
 * - ❌ 违反开闭原则和单一职责原则
 * - ❌ 测试困难：需要 mock 所有依赖
 *
 * @author wxg
 */
package io.github.daihaowxg.event.traditional;
