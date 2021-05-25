package com.zw.admin.server.task;

/**
 * 定时任务接口，所有定时任务都要实现该接口
 *
 * @author THF
 */
public interface ITask {

    /**
     * 执行定时任务接口
     *
     * @param
     */
    void run();
}
