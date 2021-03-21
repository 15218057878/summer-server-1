package cn.cerc.mis.task;

import cn.cerc.core.ISession;
import cn.cerc.mis.core.Application;
import cn.cerc.mis.core.Handle;
import cn.cerc.mis.core.ISystemTable;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class AbstractTask extends Handle implements Runnable {

    public ISystemTable systemTable;
    private String describe;

    private ISession session;

    /**
     * 缓存过期时间 单位：秒
     **/
    private int interval;
    private String time = "";

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    /**
     * 外部执行入口
     */
    @Override
    public void run() {
        systemTable = Application.getSystemTable();
        try {
            this.setHandle(new Handle(session));
            this.execute();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            session.close();
        }
    }

    @Override
    public ISession getSession() {
        return session;
    }

    @Override
    public void setSession(ISession session) {
        this.session = session;
    }

    // 具体业务逻辑代码
    public abstract void execute() throws Exception;
}
