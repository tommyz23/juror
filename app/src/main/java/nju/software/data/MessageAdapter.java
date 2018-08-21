package nju.software.data;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.zhazh.login.R;

import java.util.List;

public class MessageAdapter extends BaseAdapter {
    private List<Message> list;
    private LayoutInflater inflater;
    private Context context;


    public MessageAdapter(Context context, List<Message> list) {
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

        Message message = (Message) this.getItem(position);

        MessageAdapter.ViewHolder viewHolder;

        if (convertView == null) {

            viewHolder = new MessageAdapter.ViewHolder();
            convertView = inflater.inflate(R.layout.list_message, null);
            viewHolder.mMessage = (TextView) convertView.findViewById(R.id.text_message);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (MessageAdapter.ViewHolder) convertView.getTag();
        }

        viewHolder.mMessage.setText(message.getMessage());
        viewHolder.mMessage.setTextSize(13);
        return convertView;
    }

    public static class ViewHolder {
        public TextView mMessage;
    }

}
