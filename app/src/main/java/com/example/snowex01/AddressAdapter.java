package com.example.snowex01;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class AddressAdapter extends BaseAdapter {
    ArrayList<Address> addArrayList = new ArrayList<>();
    Context mContext;

    public AddressAdapter(ArrayList<Address> mArrayList, Context mContext) {
        this.addArrayList = mArrayList;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return addArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return addArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.item,parent,false);

        TextView tvName = itemView.findViewById(R.id.textName);
        TextView tvPhone = itemView.findViewById(R.id.textPhone);

        tvName.setText(addArrayList.get(position).name);
        tvPhone.setText(addArrayList.get(position).phone);

        return itemView;
    }
}