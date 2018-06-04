package com.h.fileinput.Glin;

import android.util.Log;
import android.widget.BaseAdapter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import org.loader.glin.RawResult;
import org.loader.glin.Result;
import org.loader.glin.parser.Parser;

/**
 * Created by pc on 2018/6/4.
 */

public class CommParser extends Parser {

    public CommParser(String key){
        super(key);
    }

    @Override
    public <T> Result<T> parse(Class<T> klass, RawResult netResult) {
        Result<T> result = new Result<>();
        JSONObject baseObject = JSON.parseObject(netResult.getResponse());
        Log.e("glin","baseObject.toJSONString()111="+baseObject.toJSONString());
        //---------------------------
//        if(baseObject.containsKey("message")){
//            result.setMessage(baseObject.getString("message"));
//        }
//
//        result.setCode(baseObject.getIntValue("code"));
//        result.setObj(result.getCode());
//        result.ok(baseObject.getBooleanValue("ok"));
        if(baseObject.getJSONArray("books") != null){
            result.ok(true);
        }else {
            result.ok(false);
        }
        if(result.isOK()){
            if(baseObject.containsKey(mKey)){
                T t = baseObject.getObject(mKey,klass);
                result.setResult(t);
            }else{
                T t = (T) baseObject;
                result.setResult(t);
                Log.e("glin","t="+t.toString());
            }
        }
        //--------------------------------------------

        return result;
    }
}
