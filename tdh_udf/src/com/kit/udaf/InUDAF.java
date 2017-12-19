package com.kit.udaf;


import org.apache.hadoop.hive.ql.exec.UDAF;
import org.apache.hadoop.hive.ql.exec.UDAFEvaluator;

/**
 * 
 * @author guohan
 * @createTime 2017-02-22
 * @description
 * udaf示列
 *
 */
public class InUDAF extends UDAF{

  public static class GroupByUDAFEvaluator implements UDAFEvaluator{
    
    public static class PartialResult{
      String filed3;
      String filed4;
      String filed5;
      String filed6;
      String delimiter;
    }
    
    private PartialResult partial;
    
    @Override
    public void init() {
      partial = null;
    }
    
    public boolean iterate(String filed3,String filed4,String filed5 ,String filed6){
      
      if(filed3 == null && filed4 == null && filed5 == null && filed6 == null){
        return true;
      }
      
      if(partial == null ){
        partial = new PartialResult();
        partial.filed3 = new String("");
        partial.filed4 = new String("");
        partial.filed5 = new String("");
        partial.filed6 = new String("");
        
        partial.delimiter = "-";
        //filed3 logical
        partial.filed3 = filed3;
      }
      
      //filed4 logical 
      partial.filed4 = filed4;
      //filed5 logical 
      if(filed5 != null && filed5.trim() != ""){
        partial.filed5 = filed5;
      }
    //filed6 logical
      if(filed6 != null && filed6.trim() != ""){
        if(partial.filed6.length() > 0 ){
          partial.filed6 = partial.filed6.concat(partial.delimiter);
        }
        partial.filed6 = partial.filed6.concat(filed6);
      }
      return true;
    }
    public PartialResult terminatePartial(){
      return partial;
    }
    public boolean merge(PartialResult othe){
      
      if (othe == null){
        return true;
      }
      
      if(partial == null ){
        partial = new PartialResult();
        
        partial.filed3 = new String(othe.filed3);
        partial.filed4 = new String(othe.filed4);
        partial.filed5 = new String(othe.filed5);
        partial.filed6 = new String(othe.filed6);
        
        partial.delimiter = new String(othe.delimiter);
        
      }else{
        
        //filed4 logical 
        partial.filed4 = othe.filed4;
        //filed5 logical 
        if(othe.filed5 != null && othe.filed5.trim() != ""){
          partial.filed5 = othe.filed5;
        }
        //filed6 logical 
        if(othe.filed6 != null && othe.filed6.trim() != ""){
          if (partial.filed6.length() >0){
            partial.filed6 = partial.filed6.concat(othe.delimiter);
          }
          partial.filed6 = partial.filed6.concat(othe.filed6);
        }
      }
      return true;
    }
    public String terminate(){
      return new String("filed3:"+partial.filed3+" filed4:" +partial.filed4+"  filed5 :"+partial.filed5+" filed6: "+partial.filed6);
    }
  }

}
