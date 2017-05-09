package com.andframe.impl.task;

import com.andframe.$;
import com.andframe.api.pager.Pager;
import com.andframe.api.task.Task;
import com.andframe.api.task.builder.Builder;
import com.andframe.api.task.builder.LoadBuilder;
import com.andframe.api.task.builder.WaitBuilder;
import com.andframe.api.task.handler.ExceptionHandler;
import com.andframe.api.task.handler.PrepareHandler;
import com.andframe.api.task.handler.WorkingHandler;

import java.util.List;

/**
 * 任务构建器
 * Created by SCWANG on 2017/4/28.
 */
@SuppressWarnings("WeakerAccess")
public class TaskBuilder implements Builder {

    public Runnable prepareRunnable;
    public PrepareHandler prepareHandler;
    public WorkingHandler workingHandler;
    public Runnable successRunnable;
    public ExceptionHandler exceptionHandler;

    @Override
    public Builder prepare(Runnable prepareRunnable) {
        this.prepareRunnable = prepareRunnable;
        return this;
    }

    @Override
    public Builder prepare(PrepareHandler prepareHandler) {
        this.prepareHandler = prepareHandler;
        return this;
    }

    @Override
    public Builder working(WorkingHandler workingHandler) {
        this.workingHandler = workingHandler;
        return this;
    }

    @Override
    public Builder success(Runnable successRunnable) {
        this.successRunnable = successRunnable;
        return this;
    }

    @Override
    public Builder exception(ExceptionHandler exceptionHandler) {
        this.exceptionHandler = exceptionHandler;
        return this;
    }

    @Override
    public WaitBuilder wait(Pager pager, String master) {
        return new WaitTaskBuilder(this, pager, master);
    }

    @Override
    public <T> LoadBuilder<T> load(Class<T> clazz) {
        return new LoadTaskBuilder<>(this, clazz);
    }

    @Override
    public <T> LoadBuilder<List<T>> loadList(Class<T> clazz) {
        return new LoadTaskBuilder<>(this, null);
    }

    @Override
    public Task build() {
        return new InternalTask(this);
    }

    @Override
    public Task post() {
        return $.task().postTask(build());
    }
}
