package com.example.martin.login;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Martin on 25. 4. 2018.
 */

public class PostsListAdapter extends RecyclerView.Adapter<PostsListAdapter.MyPostViewHolder> {
    private Context context;
    private List<PostEntity> postList = new ArrayList<>();

    public class MyPostViewHolder extends RecyclerView.ViewHolder{
        public ImageView thumbnail;
        public TextView name,date,content,bloodgroup;
        public ImageButton menuDotsButton;

        public MyPostViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.namePost);
            date = itemView.findViewById(R.id.datePost);
            content = itemView.findViewById(R.id.contentPost);
            menuDotsButton = itemView.findViewById(R.id.imageButton);
        }
    }

    public PostsListAdapter(Context context,List<PostEntity> postsList){
        this.context = context;
        this.postList = postsList;
    }

    @Override
    public MyPostViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.post_list_item, parent, false);

        return new MyPostViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyPostViewHolder holder, final int position) {
        final PostEntity item = postList.get(position);
        holder.name.setText(item.getName());
        holder.date.setText(item.getDate().getTime().toString());
        holder.content.setText(item.getContent());
        holder.menuDotsButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Toast toast = Toast.makeText(context,"position" + position,Toast.LENGTH_SHORT);
                toast.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }
}
