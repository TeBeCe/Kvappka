package com.example.martin.login;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.text.util.Linkify;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        holder.bloodGroup.setText(item.getBloodGroup());
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
                                Toast toast = Toast.makeText(context,task,Toast.LENGTH_SHORT);
                                toast.show();
                                volleyAddReport(task,position);
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .create();
                dialog.show();

            }
        });
    }
    public  void  volleyAddReport(final String reason,final int id_post) {
        String url = "http://147.175.105.140:8013/~xbachratym/public/index.php/api/report/add";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // response
                        Log.d("Response", response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // error
                        Log.d("Error.Response", error.toString());
                    }
                }
        ){
            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("id_reporter","1");
                params.put("id_reported_post", postList.get(id_post).getId());
                params.put("reason", reason);
                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(postRequest);
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
