package cn.seckill.enums;

/**
 * Created by huangfei on 2016/12/30.
 * 使用枚举表示常量数据字段
 */
public enum  SeckillStatEnum {
    SUCCESS(1,"秒杀成功"),
    END(0,"秒杀结束"),
    REPEAT_KILL(-1,"重复秒杀"),
    INSERT_ERROR(-2,"系统异常"),
    DATA_REWRITE(-3,"数据常改");
    private int state;

    private String stateInfo;

    SeckillStatEnum(int state, String stateInfo) {
        this.state = state;
        this.stateInfo = stateInfo;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    public void setStateInfo(String stateInfo) {
        this.stateInfo = stateInfo;
    }

    public static SeckillStatEnum stateOf(int index){
        for(SeckillStatEnum statEnum : values()){
            if(statEnum.getState()==index){
                return statEnum;
            }
        }
        return null;
    }
}
