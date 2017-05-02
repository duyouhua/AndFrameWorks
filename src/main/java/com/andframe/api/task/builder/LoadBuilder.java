package com.andframe.api.task.builder;

import com.andframe.api.pager.Pager;
import com.andframe.api.task.handler.ExceptionHandler;
import com.andframe.api.task.handler.LoadSuccessHandler;
import com.andframe.api.task.handler.LoadingHandler;
import com.andframe.api.task.handler.PrepareHandler;
import com.andframe.api.task.handler.WorkingHandler;

/**
 * 数据加载任务构建器
 * Created by SCWANG on 2017/4/28.
 */

public interface LoadBuilder<T> extends Builder {

    /**
     * 特有接口
     */
    LoadBuilder<T> loading(LoadingHandler<T> handler);
    LoadBuilder<T> loadSuccess(LoadSuccessHandler<T> handler);
    LoadBuilder<T> loadEmpty(Runnable runnable);

    /**
     * 重写接口
     */
    LoadBuilder<T> prepare(Runnable runnable);
    LoadBuilder<T> prepare(PrepareHandler handler);
    LoadBuilder<T> exception(ExceptionHandler handler);
    WaitLoadBuilder<T> wait(Pager pager, String master);

    /**
     * 禁用接口
     */
    @Deprecated
    Builder working(WorkingHandler handler);
    @Deprecated
    Builder success(Runnable runnable);
    @Deprecated
    <TT> LoadBuilder<TT> load(Class<TT> clazz);
}
