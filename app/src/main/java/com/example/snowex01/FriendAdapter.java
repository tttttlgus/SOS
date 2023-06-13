package com.example.snowex01;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class FriendAdapter extends BaseAdapter {
    ArrayList<Friend> frndArrayList = new ArrayList<>();
    Context mContext;


    public FriendAdapter(ArrayList<Friend> ArrayList, Context mContext) {
        this.frndArrayList = ArrayList;
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return frndArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return frndArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.item_frnd,parent,false);

        TextView tvID = itemView.findViewById(R.id.textID);
        Switch switchView = itemView.findViewById(R.id.switch1);

        tvID.setText(frndArrayList.get(position).id);
        switchView.setChecked(frndArrayList.get(position).show);

        switchView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){                      //스위치 enabled
                    Toast.makeText(mContext, tvID.getText()+ "의 실시간 위치가 메인 화면에 표시됩니다.", Toast.LENGTH_SHORT).show();
/*                    Intent intent = new Intent(mContext, SharetextActivity.class);
                    intent.putExtra("위치를 확인할 ID", tvID.getText());
                    mContext.startActivity(intent);*/

                }else{                      //스위치 disabled
                    Toast.makeText(mContext, "DISABLE", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return itemView;
    }

}
