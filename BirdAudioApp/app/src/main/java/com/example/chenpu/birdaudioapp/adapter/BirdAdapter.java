package com.example.chenpu.birdaudioapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.chenpu.birdaudioapp.R;
import com.example.chenpu.birdaudioapp.SearchResultActivity;
import com.example.chenpu.birdaudioapp.entity.BirdShortInfo;

import java.util.List;

public class BirdAdapter extends RecyclerView.Adapter<BirdAdapter.BirdViewHolder> {

    private List<BirdShortInfo> birdList;
    private Context context;

    public BirdAdapter(Context context, List<BirdShortInfo> birdList) {
        this.context = context;
        this.birdList = birdList;
    }

    @Override
    public BirdViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_bird, parent, false);
        return new BirdViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BirdViewHolder holder, int position) {
        BirdShortInfo bird = birdList.get(position);
        holder.birdName.setText(bird.getChinese());
        Glide.with(context)
                .load(bird.getPicture())
                .into(holder.birdImage);

        // 设置点击事件
        holder.itemView.setOnClickListener(view -> {
            // 获取点击的鸟类名称
            String birdName = bird.getChinese();

            // 创建 Intent 并传递数据
            Intent intent = new Intent(context, SearchResultActivity.class);
            intent.putExtra("query", birdName); // 将鸟的名称传递给 SearchResultActivity
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return birdList.size();
    }

    public static class BirdViewHolder extends RecyclerView.ViewHolder {
        TextView birdName;
        ImageView birdImage;

        public BirdViewHolder(View itemView) {
            super(itemView);
            birdName = itemView.findViewById(R.id.birdName);
            birdImage = itemView.findViewById(R.id.birdImage);
        }
    }
}
