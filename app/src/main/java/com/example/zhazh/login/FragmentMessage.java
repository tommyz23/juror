package com.example.zhazh.login;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import org.litepal.crud.DataSupport;
import nju.software.data.Message;
import java.util.ArrayList;
import java.util.List;
import nju.software.data.MessageAdapter;

public class FragmentMessage extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message,container,false);
        return view;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    public void init(){
        List<Message> list = DataSupport.findAll(Message.class);
        /**在这下面获取数据库信息加入list*/

        ListView tableListView = getActivity().findViewById(R.id.list_message);

        MessageAdapter adapter = new MessageAdapter(this.getActivity(), list);

        tableListView.setAdapter(adapter);
        tableListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ListView listView = (ListView) adapterView;
                final Message message = (Message)listView.getItemAtPosition(i);
                //    通过AlertDialog.Builder这个类来实例化我们的一个AlertDialog的对象
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                //    设置Title的内容
                builder.setTitle("删除");
                //    设置Content来显示一个信息
                builder.setMessage("确定你了解了本条信息内容并决定将其删除吗？");
                //    设置一个PositiveButton
                builder.setPositiveButton("删除", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        DataSupport.deleteAll(Message.class, "index=?", message.getIndex());
                        init();
                    }
                });
                //    设置一个NegativeButton
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog,int which)
                    {
                    }
                });
                //    显示出该对话框
                builder.show();
            }
        });
    }
}

