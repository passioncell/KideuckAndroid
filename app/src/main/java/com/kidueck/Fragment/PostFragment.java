package com.kidueck.Fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.kidueck.Common.PointReceiver;
import com.kidueck.Concrete.PostingRepository;
import com.kidueck.R;

/**
 * Created by system777 on 2016-06-28.
 */
public class PostFragment  extends Fragment implements View.OnClickListener {

    FeedFragment feedFrag;

    EditText postContent;
    Button submitPost;

    PostingRepository postingRepository = new PostingRepository();
    String inputContent;
    boolean submitResult;

    public static PostFragment newInstance() {
        PostFragment fragment = new PostFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {super.onCreate(savedInstanceState);    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_post, container, false);


        init(view);

        return view;
    }

    private void init(View view){
        postContent = (EditText) view.findViewById(R.id.et_post_content);
        submitPost = (Button) view.findViewById(R.id.bt_post_submit);
        submitPost.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.bt_post_submit:
                submitPost.setEnabled(false);
                inputContent = postContent.getText().toString().trim();
                if(inputContent.length() < 5){
                    Toast.makeText(getContext(), "5자 이상 써주셔야 합니다.", Toast.LENGTH_SHORT).show();
                }else{
                    postContent.setText("");
                    new SubmitPost().execute();
                }

                break;
            default:
                break;
        }
    }

    public void submitPost(){
        submitPost.setEnabled(false);
        inputContent = postContent.getText().toString().trim();
        if(inputContent.length() < 5){
            Toast.makeText(getContext(), "5자 이상 써주셔야 합니다.", Toast.LENGTH_SHORT).show();
        }else{
            postContent.setText("");
            new SubmitPost().execute();
        }
    }

    public class SubmitPost extends AsyncTask<Boolean, Void, Void> {

        ProgressDialog progressDialog;

        @Override
        protected Void doInBackground(Boolean... params) {
            try
            {
                //Getting data from server
                submitResult = postingRepository.writePost(getUserId(), inputContent);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progressDialog.dismiss();
            if(submitResult){
                Toast.makeText(getContext(), "게시글이 등록되었습니다.", Toast.LENGTH_SHORT).show();
                feedFrag = FeedFragment.newInstance();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.ll_frag_content, feedFrag).commit();

                //포인트 갱신
                getContext().sendBroadcast(new Intent(getActivity(), PointReceiver.class));

                //Snackbar.make(getView(), "게시글이 등록되었습니다.", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
            }else{
                Toast.makeText(getContext(), "게시글 등록에 실패하였습니다.", Toast.LENGTH_SHORT).show();
            }

        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("게시글 등록중..");
            progressDialog.setCancelable(true);
            progressDialog.show();
        }

        private int getUserId(){
            SharedPreferences pref =  getActivity().getSharedPreferences("userId", Context.MODE_PRIVATE);
            return Integer.parseInt(pref.getString("userId", ""));
        }
    }//SubmitPost Class();;


}
