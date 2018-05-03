package com.example.martin.login;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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
        public TextView name,date,content,bloodGroup;
        public ImageButton menuDotsButton;

        public MyPostViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.namePost);
            date = itemView.findViewById(R.id.datePost);
            content = itemView.findViewById(R.id.contentPost);
            Linkify.addLinks(content,Linkify.WEB_URLS);
            menuDotsButton = itemView.findViewById(R.id.imageButton);
            bloodGroup = itemView.findViewById(R.id.logoBloodGroup);
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
                final EditText reportEditText = new EditText(context);
                reportEditText.setLines(2);
                AlertDialog dialog = new AlertDialog.Builder(context)
                        .setTitle("Add a report")
                        .setMessage("Write your report here")
                        .setView(reportEditText)
                        .setPositiveButton("Report", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String task = String.valueOf(reportEditText.getText());
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .create();
                dialog.show();

            }
        });
    }

    public void addItem(PostEntity postEntity){
        postList.add(postEntity);
        notifyDataSetChanged();
    }

    public List<PostEntity> returnList(){
        return postList;
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }
}
