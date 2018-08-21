package com.example.zhazh.login;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import nju.software.data.Case;
import nju.software.data.CaseInformationAdapter;
import nju.software.util.HttpUtil;
import nju.software.util.JSONUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;

public class FragmentHome1 extends Fragment {
    String id;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home1, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        SharedPreferences read = getActivity().getSharedPreferences("user", MODE_PRIVATE);
        id = read.getString("id", "");
        getNewCase();
        getNewDoc();

    }

    public void getNewCase() {
        String param = "RequestType=GetCase&type=new&id=" + id;
        HttpUtil.sendRequest(param, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("error", e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final List<Case> list = JSONUtil.parseCaseJSON(response.body().string());
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showCase(list);
                    }
                });
            }
        });
    }

    public void getNewDoc() {
        String param = "RequestType=Doc&id=" + id;
        HttpUtil.sendRequest(param, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("error", e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String s = response.body().string();
                List<String> list = JSONUtil.parseDocJSON(s);
                List<Case> list2 = new ArrayList<Case>();
                for (String doc : list) {
                    Case c = new Case();
                    c.setIndex(doc);
                    c.setAddress("");
                    c.setId("");
                    c.setTime("");
                    list2.add(c);
                }
                final List<Case> docList = list2;
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showDoc(docList);
                    }
                });
            }
        });
    }

    public void showCase(List<Case> list) {
        ListView tableListView1 = getActivity().findViewById(R.id.list1);

        CaseInformationAdapter adapter1 = new CaseInformationAdapter(this.getActivity(), list);

        tableListView1.setAdapter(adapter1);

        tableListView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ListView listView = (ListView) adapterView;
                Case theCase = (Case) listView.getItemAtPosition(i);
                String case_id = theCase.getIndex();
                getTheCase(case_id, id);
            }
        });
    }

    public void showDoc(List<Case> list) {
        ListView tableListView2 = getActivity().findViewById(R.id.list2);

        CaseInformationAdapter adapter2 = new CaseInformationAdapter(this.getActivity(), list);

        tableListView2.setAdapter(adapter2);

        tableListView2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ListView listView = (ListView) adapterView;
                Case theCase = (Case) listView.getItemAtPosition(i);
                final String case_id = theCase.getIndex();
                //    通过AlertDialog.Builder这个类来实例化我们的一个AlertDialog的对象
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                //    设置Title的内容
                builder.setTitle("签字");
                //    设置Content来显示一个信息
                String message = case_id + "一案审理结束，需要你在裁判文书上签字，请签字";
                builder.setMessage(message);
                //    设置一个PositiveButton
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sign(case_id, id);
                    }
                });
                //    设置一个NegativeButton
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                //    显示出该对话框
                builder.show();
            }
        });
    }

    private void getTheCase(String case_id, String juror_id) {
        String param = "RequestType=CaseById&case_id=" + case_id + "&juror_id=" + juror_id;
        HttpUtil.sendRequest(param, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("getTheCase error", e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String s = response.body().string();
                final Case theCase = JSONUtil.parseOneCaseJSON(s);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //    通过AlertDialog.Builder这个类来实例化我们的一个AlertDialog的对象
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        //    设置Title的内容
                        builder.setTitle("详情");
                        //    设置Content来显示一个信息
                        String details = "案号：" + theCase.getIndex()
                                + "\n案件名称：" + theCase.getName()
                                + "\n承办人：" + theCase.getUndertaker()
                                + "\n开庭时间：" + theCase.getTime()
                                + "\n开庭地点：" + theCase.getAddress();
                        builder.setMessage(details);
                        //    设置一个NegativeButton
                        builder.setNegativeButton("返回", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                        //    显示出该对话框
                        builder.show();
                    }
                });
            }
        });
    }

    private void sign(String case_id, String juror_id) {
        String param = "RequestType=Sign&case_id=" + case_id + "&juror_id=" + juror_id;
        HttpUtil.sendRequest(param, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("onFailure: ", e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = JSONUtil.parseBoolJSON(response.body().string());
                if (result.equals("true")) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(), "签字成功", Toast.LENGTH_LONG).show();
                        }
                    });
                    getNewDoc();
                } else {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getActivity(), "签字失败，请稍后重试", Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
    }
}
