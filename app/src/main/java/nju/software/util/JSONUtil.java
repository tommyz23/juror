package nju.software.util;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import nju.software.data.Case;
import nju.software.data.Holiday;
import nju.software.data.User;

/**
 * 解析JSON
 * Created by Administrator on 2018/7/30.
 */

public class JSONUtil {
    public static List<Case> parseCaseJSON(String response) {
        Log.d("response", response);
        List<Case> caseList = new ArrayList<Case>();
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONArray allCases = new JSONArray(response);
                for (int i = 0; i < allCases.length(); i++) {
                    JSONObject case_i = allCases.getJSONObject(i);
                    Case singleCase = new Case();
                    singleCase.setId(case_i.getString("id"));
                    singleCase.setIndex(case_i.getString("index"));
                    singleCase.setAddress(case_i.getString("address"));
                    singleCase.setTime(case_i.getString("time"));
                    caseList.add(singleCase);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return caseList;
    }

    public static List<Holiday> parseHolidayJSON(String response) {
        List<Holiday> holidaysList = new ArrayList<Holiday>();
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONArray allCases = new JSONArray(response);
                for (int i = 0; i < allCases.length(); i++) {
                    JSONObject case_i = allCases.getJSONObject(i);
                    Holiday holiday = new Holiday();
                    holiday.setStartTime(case_i.getString("start"));
                    holiday.setEndTime(case_i.getString("end"));
                    holiday.setReason(case_i.getString("reason"));
                    holidaysList.add(holiday);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return holidaysList;
    }

    public static User parseUserJSON(String response) {
        User user = new User();
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONObject allResponse = new JSONObject(response);
                JSONObject data = allResponse.getJSONObject("user");
                user.setId(data.getString("id"));
                user.setName(data.getString("name"));
                user.setPassword(data.getString("password"));
                user.setPhoneNumber(data.getString("phoneNumber"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return user;
    }

    public static String parseBoolJSON(String response) {
        String result = "";
        if (!TextUtils.isEmpty(response)){
            try {
            JSONObject object = new JSONObject(response);
            result = object.getString("result");
        } catch (JSONException e) {
            e.printStackTrace();
        }}
        return result;
    }

    public static List<Case> parseNotifyJSON(String response) {
        List<Case> caseList = new ArrayList<Case>();
        Case singleCase = new Case();
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONArray allCases = new JSONArray(response);
                for (int i = 0; i < allCases.length(); i++) {
                    JSONObject case_i = allCases.getJSONObject(i);
                    singleCase = new Case();
                    singleCase.setAddress(case_i.getString("address"));
                    singleCase.setIndex(case_i.getString("index"));
                    singleCase.setTime(case_i.getString("time"));
                    caseList.add(singleCase);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return caseList;
    }

    public static List<String> parseDocJSON(String response) {
        List<String> list = new ArrayList<>();
        String doc = "";
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONArray allDoc = new JSONArray(response);
                for (int i = 0; i < allDoc.length(); i++) {
                    JSONObject doc_i = allDoc.getJSONObject(i);
                    doc = doc_i.getString("index");
                    list.add(doc);
                    doc = "";
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    public static List<String> parseNotifyDocJSON(String response) {
        List<String> list = new ArrayList<>();
        String doc = "";
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONArray allDoc = new JSONArray(response);
                for (int i = 0; i < allDoc.length(); i++) {
                    JSONObject doc_i = allDoc.getJSONObject(i);
                    doc = doc_i.getString("index");
                    list.add(doc);
                    doc = "";
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    public static Case parseOneCaseJSON(String response) {
        Case singleCase = new Case();
        if (!TextUtils.isEmpty(response)) {
            JSONObject case_i = null;
            try {
                Log.d("parseOneCaseJSON: ", response);
                case_i = new JSONObject(response);
                //singleCase.setId(case_i.getString("id"));
                singleCase.setIndex(case_i.getString("index"));
                singleCase.setAddress(case_i.getString("address"));
                singleCase.setTime(case_i.getString("time"));
                singleCase.setName(case_i.getString("name"));
                singleCase.setUndertaker(case_i.getString("undertaker"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return singleCase;
    }
}
