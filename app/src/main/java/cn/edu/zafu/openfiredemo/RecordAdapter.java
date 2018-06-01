package cn.edu.zafu.openfiredemo;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import cn.edu.zafu.openfiredemo.im.db.bean.ChatRecord;

/**
 * RecordAdapter
 * Created by xrc on 18/5/24.
 */

public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.RecordHolder> {
    private final List<ChatRecord> list;

    public RecordAdapter(List<ChatRecord> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public RecordAdapter.RecordHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RecordHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecordAdapter.RecordHolder holder, int position) {
        ChatRecord chatRecord = list.get(position);
        if (chatRecord.getIsReceive()) {
            holder.tv.setGravity(Gravity.START | Gravity.CENTER_VERTICAL);
        } else {
            holder.tv.setGravity(Gravity.END | Gravity.CENTER_VERTICAL);
        }
        holder.tv.setText(chatRecord.getContent());
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    static class RecordHolder extends RecyclerView.ViewHolder {

        TextView tv;

        public RecordHolder(View itemView) {
            super(itemView);
            tv = itemView.findViewById(R.id.tv);
        }
    }
}
