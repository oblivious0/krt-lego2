package com.krt.base.inter;

/**
 * @author Marcus
 * @package krt.wid.inter
 * @description
 * @time 2018/3/1
 */

public interface IViewHolder<T> {
    void convert(T t, int position, int offsetTotal);
}
