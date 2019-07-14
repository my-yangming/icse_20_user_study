package com.hankcs.hanlp.algorithm.ahocorasick.trie;

import java.util.*;

/**
 * <p>
 * 一个状�?有如下几个功能
 * </p>
 * <p/>
 * <ul>
 * <li>success; �?功转移到�?�一个状�?</li>
 * <li>failure; �?�?�顺�?�字符串跳转的�?，则跳转到一个浅一点的节点</li>
 * <li>emits; 命中一个模�?串</li>
 * </ul>
 * <p/>
 * <p>
 * 根节点�?有�?�?�，根节点没有 failure 功能，它的“failure�?指的是按照字符串路径转移到下一个状�?。其他节点则都有failure状�?。
 * </p>
 *
 * @author Robert Bor
 */
public class State
{

    /**
     * 模�?串的长度，也是这个状�?的深度
     */
    protected final int depth;

    /**
     * fail 函数，如果没有匹�?到，则跳转到此状�?。
     */
    private State failure = null;

    /**
     * �?��?这个状�?�?�达，则记录模�?串
     */
    private Set<String> emits = null;
    /**
     * goto 表，也称转移函数。根�?�字符串的下一个字符转移到下一个状�?
     */
    private Map<Character, State> success = new TreeMap<Character, State>();

    /**
     * 构造深度为0的节点
     */
    public State()
    {
        this(0);
    }

    /**
     * 构造深度为depth的节点
     * @param depth
     */
    public State(int depth)
    {
        this.depth = depth;
    }

    /**
     * 获�?�节点深度
     * @return
     */
    public int getDepth()
    {
        return this.depth;
    }

    /**
     * 添加一个匹�?到的模�?串（这个状�?对应�?�这个模�?串)
     * @param keyword
     */
    public void addEmit(String keyword)
    {
        if (this.emits == null)
        {
            this.emits = new TreeSet<String>();
        }
        this.emits.add(keyword);
    }

    /**
     * 添加一些匹�?到的模�?串
     * @param emits
     */
    public void addEmit(Collection<String> emits)
    {
        for (String emit : emits)
        {
            addEmit(emit);
        }
    }

    /**
     * 获�?�这个节点代表的模�?串（们）
     * @return
     */
    public Collection<String> emit()
    {
        return this.emits == null ? Collections.<String>emptyList() : this.emits;
    }

    /**
     * 获�?�failure状�?
     * @return
     */
    public State failure()
    {
        return this.failure;
    }

    /**
     * 设置failure状�?
     * @param failState
     */
    public void setFailure(State failState)
    {
        this.failure = failState;
    }

    /**
     * 转移到下一个状�?
     * @param character 希望按此字符转移
     * @param ignoreRootState 是�?�忽略根节点，如果是根节点自己调用则应该是true，�?�则为false
     * @return 转移结果
     */
    private State nextState(Character character, boolean ignoreRootState)
    {
        State nextState = this.success.get(character);
        if (!ignoreRootState && nextState == null && this.depth == 0)
        {
            nextState = this;
        }
        return nextState;
    }

    /**
     * 按照character转移，根节点转移失败会返回自己（永远�?会返回null）
     * @param character
     * @return
     */
    public State nextState(Character character)
    {
        return nextState(character, false);
    }

    /**
     * 按照character转移，任何节点转移失败会返回null
     * @param character
     * @return
     */
    public State nextStateIgnoreRootState(Character character)
    {
        return nextState(character, true);
    }

    public State addState(Character character)
    {
        State nextState = nextStateIgnoreRootState(character);
        if (nextState == null)
        {
            nextState = new State(this.depth + 1);
            this.success.put(character, nextState);
        }
        return nextState;
    }

    public Collection<State> getStates()
    {
        return this.success.values();
    }

    public Collection<Character> getTransitions()
    {
        return this.success.keySet();
    }

    @Override
    public String toString()
    {
        final StringBuilder sb = new StringBuilder("State{");
        sb.append("depth=").append(depth);
        sb.append(", emits=").append(emits);
        sb.append(", success=").append(success.keySet());
        sb.append(", failure=").append(failure);
        sb.append('}');
        return sb.toString();
    }
}
