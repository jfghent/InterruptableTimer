/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.jfghent.interruptabletimer;

import com.jfghent.interruptabletask.InterruptableTask;
import java.time.ZonedDateTime;

/**
 *
 * @author jon
 */
public class InterruptableTimer extends InterruptableTask{
    
    Long starttime = 0L;
    Long duration = 0L;
    Long elapsed = 0L;
            
    public InterfaceVoid if_onCancel;
    public InterfaceVoid if_onResume;
    public InterfaceVoid if_onPause;
    public InterfaceVoid if_onStart;
    public InterfaceVoid if_onFinish;
    public InterfaceVoid if_onFinal;
    
    public InterruptableTimer(String TaskName, long Time, InterfaceVoid onfinal) {
        super(TaskName);
        SetTimer(Time);
        if_onFinal = onfinal;
    }

    final public long LocalNow(){
        return ZonedDateTime.now().toInstant().toEpochMilli();
    }
    
    final public void SetTimer(long Time){
        SetTimer(Time,true);
    }
    final public void SetTimer(long Time,boolean ResetStartTime) {
        duration = Time;
        if(ResetStartTime) starttime = 0L;
    }
    
    final public long GetElapsed(){
        return LocalNow() - starttime + elapsed;
    }
    
    final public long GetRemaining(){
        return duration - GetElapsed();
    }
    
    @Override
    public void content(){
        starttime = LocalNow();
        if_onStart.run();
        try{
           Thread.sleep(duration-elapsed);
        }catch(InterruptedException e){
            //if_onInterrupt.run();
        }
        if_onFinish.run();
        if_onFinal.run();
        //onfinish();
    }
    
    
    @Override
    public void oncancel(){
        SetTimer(0);
        if_onCancel.run();
        if_onFinal.run();
    }
    
    @Override
    public void onpause(){
        elapsed = GetElapsed();
        starttime = 0L;
        if_onPause.run();
    }
    
    @Override
    public void onresume(){
        if_onResume.run();
        content();
    }
    
    @Override
    public void onfinish(){
        if_onFinish.run();
        
    }
    
}
