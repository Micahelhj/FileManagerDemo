package com.admin.file.manager;

/**
 * 项目名称：FileManagerDemo
 * 类描述：
 * 创建人：Michael-hj
 * 创建时间：2016/5/13 0013 13:50
 * 修改人：Michael-hj
 * 修改时间：2016/5/13 0013 13:50
 * 修改备注：
 */

public class test {

    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub

        Task.setThreadMaxNum(3);
        for (int i = 0; i < 15; i++) {
            new Task() {

                @Override
                public Object obtainData(Task task, Object parameter)
                        throws Exception {
                    // TODO Auto-generated method stub
                    Thread.sleep(500);
                    return task.taskID;
                }

            }
                    .setOnFinishListen(new Task.OnFinishListen() {

                        @Override
                        public void onFinish(Task task, Object data) {
                            // TODO Auto-generated method stub
                            System.err.println("任务编号" + task.taskID + "任务完成");
                        }
                    })
                    .setTaskID(i)
                    .start();
        }
    }

}
