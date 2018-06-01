package cn.edu.zafu.openfiredemo;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import cn.edu.zafu.openfiredemo.im.bean.Contacts;
import cn.edu.zafu.openfiredemo.im.db.bean.ChatRecord;

/**
 * FriendsAdapter
 * Created by xrc on 18/5/24.
 */

public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.RecordHolder> {
    private final List<Contacts> list;

    public FriendsAdapter(List<Contacts> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public FriendsAdapter.RecordHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new RecordHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull FriendsAdapter.RecordHolder holder, int position) {
        Contacts contacts = list.get(position);
        holder.tv.setText(contacts.getUser() + "   好友状态: " + contacts.getType());
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    static class RecordHolder extends RecyclerView.ViewHolder {

        TextView tv;

        RecordHolder(View itemView) {
            super(itemView);
            tv = itemView.findViewById(R.id.tv);
        }
    }
}
