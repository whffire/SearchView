package com.tools.fj.searchview;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

public class SearchHistoryAdapter extends BaseAdapter {


    private List<String> mData = new ArrayList<String>();
    private Context mContext;

    public SearchHistoryAdapter(Context mContext, List<String> mData) {
        super();
        if (mData.size() % 2 != 0) {
            mData.add("");
        }
        this.mContext = mContext;

        this.mData = mData;
    }

    public SearchHistoryAdapter(Context mContext) {
        super();
        this.mContext = mContext;

    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_search_history, null);
            holder = new ViewHolder();

            holder.textView = convertView.findViewById(R.id.atv_history);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.textView.setText(mData.get(position));
        return convertView;

    }


    static class ViewHolder {
        AppCompatTextView textView;


    }

    public List<String> getmData() {
        return mData;
    }

    public void setmData(List<String> mData) {
        if (mData.size() % 2 != 0) {
            mData.add("");
        }
        this.mData = mData;
        notifyDataSetChanged();
    }

    public void Refresh() {
        if (mData.size() % 2 != 0) {
            mData.add("");
        }
        this.mData = mData;
        notifyDataSetChanged();
    }

    public void clear() {
        mData.clear();
        notifyDataSetChanged();
    }
}
