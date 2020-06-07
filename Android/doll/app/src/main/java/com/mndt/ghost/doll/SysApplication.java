package com.mndt.ghost.doll;

import android.app.Activity;
import android.app.Application;
import android.support.v7.app.AppCompatActivity;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Ghost on 2017/10/31.
 */
public class SysApplication extends Application {
    //运用list来保存们每一个activity是关键
    private List<AppCompatActivity> mList = new LinkedList<AppCompatActivity>();
    //为了实现每次使用该类时不创建新的对象而创建的静态对象
    private static SysApplication instance;
    //构造方法
    private SysApplication(){}
    //实例化一次
    public synchronized static SysApplication getInstance(){
        if (null == instance) {
            instance = new SysApplication();
        }
        return instance;
    }
    // add Activity
    public void addActivity(AppCompatActivity activity) {
        mList.add(activity);
    }

    //关闭每一个list内的activity
    public void fnClose() {
        try {
            for (AppCompatActivity activity:mList) {
                if (activity != null)
                    activity.finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //关闭每一个list内的activity
    public void exit() {
        try {
            for (AppCompatActivity activity:mList) {
                if (activity != null)
                    activity.finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.exit(0);
        }
    }
    //杀进程
    public void onLowMemory() {
        super.onLowMemory();
        System.gc();
    }
}