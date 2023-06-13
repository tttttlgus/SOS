package com.example.snowex01;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class myRecyclerViewAdapter extends RecyclerView.Adapter {
    String TAG = "RecyclerViewAdapter";

    //리사이클러뷰에 넣을 데이터 리스트
    ArrayList<Friend> mfrndId;
    Context mcontext;

    //생성자를 통하여 데이터 리스트 context를 받음
    public myRecyclerViewAdapter(ArrayList<Friend> frndID, Context context){
        this.mfrndId = frndID;
        this.mcontext = context;
    }

    @Override
    public int getItemCount() {
        //데이터 리스트의 크기를 전달해주어야 함
        return mfrndId.size();
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG,"onCreateViewHolder");

        //자신이 만든 itemview를 inflate한 다음 뷰홀더 생성
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_shrd,parent,false);
        MyViewHolder viewHolder = new MyViewHolder(view);

        //생선된 뷰홀더를 리턴하여 onBindViewHolder에 전달한다.
        return viewHolder;
    }



    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Log.d(TAG,"onBindViewHolder");

        MyViewHolder myViewHolder = (MyViewHolder)holder;

        myViewHolder.textView.setText(mfrndId.get(position).getId());
        myViewHolder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(mcontext, position+"번째 텍스트 뷰 클릭", Toast.LENGTH_SHORT).show();
            }
        });

        myViewHolder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(mcontext, position+"번째 이미지 뷰 클릭", Toast.LENGTH_SHORT).show();//position은 0번 먼저 시작
                Toast.makeText(mcontext, mfrndId.get(position).getId()+"", Toast.LENGTH_SHORT).show(); //해당 아이디 출력

            }
        });

        if(mfrndId.get(position).getShow() == true){
            myViewHolder.imageView.setColorFilter(Color.parseColor("#55ff0000"));
        } else{
            myViewHolder.imageView.setColorFilter(Color.GRAY);
        }

    }





    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        ImageView imageView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textView =  itemView.findViewById(R.id.fdID);
            imageView = itemView.findViewById(R.id.imageview1);

        }
    }

}
