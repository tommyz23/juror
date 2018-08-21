package nju.software.data;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.zhazh.login.R;

import java.util.List;

public class CaseInformationAdapter extends BaseAdapter {
    private List<Case> list;
    private LayoutInflater inflater;

    public CaseInformationAdapter(Context context, List<Case> list) {
        this.list = list;
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

        Case caseInformation = (Case) this.getItem(position);

        ViewHolder viewHolder;

        if (convertView == null) {

            viewHolder = new ViewHolder();

            convertView = inflater.inflate(R.layout.list_case_information, null);
            viewHolder.mNum = (TextView) convertView.findViewById(R.id.text_number);
            viewHolder.mCaseNum = (TextView) convertView.findViewById(R.id.text_case_number);
            viewHolder.mKtsj = (TextView) convertView.findViewById(R.id.text_ktsj);
            viewHolder.mKtdd = (TextView) convertView.findViewById(R.id.text_ktdd);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.mNum.setText(caseInformation.getId());
        viewHolder.mNum.setTextSize(13);
        viewHolder.mCaseNum.setText(caseInformation.getIndex());
        viewHolder.mCaseNum.setTextSize(13);
        viewHolder.mKtsj.setText(caseInformation.getTime());
        viewHolder.mKtsj.setTextSize(13);
        viewHolder.mKtdd.setText(caseInformation.getAddress());
        viewHolder.mKtdd.setTextSize(13);


        return convertView;
    }

    public static class ViewHolder {
        public TextView mNum;
        public TextView mCaseNum;
        public TextView mKtsj;
        public TextView mKtdd;

    }
}
