package com.h.fileinput.Glin;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import org.loader.glin.RawResult;
import org.loader.glin.Result;
import org.loader.glin.parser.Parser;

/**
 * Created by pc on 2018/6/4.
 */

public class ListParser extends Parser {

    public ListParser(String key){
        super(key);
    }

    @Override
    public <T> Result<T> parse(Class<T> klass, RawResult netResult) {
        Result<T> result = new Result<>();
        JSONObject baseObject = JSON.parseObject(netResult.getResponse());
        Log.e("glin","baseObject.toJSONString()="+baseObject.toJSONString());
//        if(baseObject.containsKey("message")){
//            result.setMessage(baseObject.getString("message"));
//            Log.e("glin","baseObject.getString(\"message\")");
//        }
//
//        result.setCode(baseObject.getIntValue("code"));
//        result.setObj(result.getCode());
//        result.ok(baseObject.getBooleanValue("ok"));
        if(baseObject.getIntValue("total") != 0){
            result.ok(true);
        }else{
            result.ok(false);
        }

        if(result.isOK()){
            if(baseObject.containsKey(mKey)){
                JSONArray arr = baseObject.getJSONArray(mKey);
                T t = (T)JSON.parseArray(arr.toString(),klass);
                result.setResult(t);
            }
        }
        return result;
    }
}
