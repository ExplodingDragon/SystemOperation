package tech.openEdgn.test.system

/**
 * 进程状态
 *
 * @constructor
 */
enum class ProcessStatus(val type: String) {
    /**
     *全部线程
     */
    ALL("全部"),

    /**
     * 进程建立，准备启动，但未启动
     */
    CREATE("未启动"),

    /**
     * 进程活动就绪
     */
    RUN_READY("活动就绪"),

    /**
     * 进程静止就绪
     */
    STOP_READY("静止就绪"),

    /**
     * 进程活动阻塞
     */
    RUN_WAIT("活动阻塞"),

    /**
     * 进程静止阻塞
     */
    STOP_WAIT("静止阻塞"),

    /**
     * 进程运行
     */
    RUN("运行"),

    /**
     * 进程结
     */
    FINISH("结束")


}