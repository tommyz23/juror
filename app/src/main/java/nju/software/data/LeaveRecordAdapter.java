package nju.software.data;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhazh.login.LeaveRecordActivity;
import com.example.zhazh.login.R;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import nju.software.util.HttpUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;

public class LeaveRecordAdapter extends BaseAdapter {
    private List<Holiday> list;
    private LayoutInflater inflater;
    private Context context;
    private int removed = 0;

    public LeaveRecordAdapter(Context context, List<Holiday> list) {
        this.list = list;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        int ret = 0;
        if (list != null) {
            ret = list.size();
        }
        return ret;
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final Holiday leaveRecord = (Holiday) this.getItem(position);

        LeaveRecordAdapter.ViewHolder viewHolder;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.list_leave_record2, null);
            viewHolder.mDateBegin = (TextView) convertView.findViewById(R.id.text_date_begin);
            viewHolder.mDateEnd = (TextView) convertView.findViewById(R.id.text_date_end);
            viewHolder.mReason = (TextView) convertView.findViewById(R.id.text_reason);
            viewHolder.mLeave = (Button) convertView.findViewById(R.id.button_leave);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (LeaveRecordAdapter.ViewHolder) convertView.getTag();
        }

        viewHolder.mDateBegin.setText(leaveRecord.getStartTime());
        viewHolder.mDateBegin.setTextSize(13);
        viewHolder.mDateEnd.setText(leaveRecord.getEndTime());
        viewHolder.mDateEnd.setTextSize(13);
        viewHolder.mReason.setText(leaveRecord.getReason());
        viewHolder.mReason.setTextSize(13);
        viewHolder.mLeave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                //    设置Title的内容
                builder.setTitle("确认销假");
                //    设置Content来显示一个信息
                builder.setMessage("确定要对这条请假进行销假吗？");
                //    设置一个PositiveButton
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        boolean legalTime = checkDate(leaveRecord.getStartTime());
                        if(legalTime){
                            removeRecord(leaveRecord.getStartTime(), leaveRecord.getEndTime());
                            while(removed==0);
                            RemoveHoliday removed = (RemoveHoliday) context;
                            removed.dataChanged();
                        }else{
                            Toast.makeText(context, "已错过可销假时间", Toast.LENGTH_LONG).show();
                        }
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
        return convertView;
    }

    public static class ViewHolder {
        public TextView mDateBegin;
        public TextView mDateEnd;
        public TextView mReason;
        public Button mLeave;
    }

    private boolean checkDate(String startTime){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");// HH:mm:ss
        Date date = new Date(System.currentTimeMillis());
        try {
            Date start = simpleDateFormat.parse(startTime);
            Log.e("start", startTime );
            Log.e("new", simpleDateFormat.format(date));
            if(start.after(date))
                return true;
            else
                return false;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void removeRecord(String start, String end){
        SharedPreferences read = context.getSharedPreferences("user", MODE_PRIVATE);
        String id = read.getString("id", "");
        String param = "RequestType=Remove&start=" + start + "&end=" + end + "&id=" + id;
        HttpUtil.sendRequest(param, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("remove", e.toString());
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.e("remove", "remove");
                removed =  1;
            }
        });
    }
}
