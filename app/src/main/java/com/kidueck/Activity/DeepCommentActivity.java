package com.kidueck.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.kidueck.Adapter.DeepCommentAdapter;
import com.kidueck.Concrete.CommentRepository;
import com.kidueck.Model.DeepCommentListModel;
import com.kidueck.R;

import java.util.Vector;

/**
 * Created by system777 on 2016-07-22.
 */
public class DeepCommentActivity extends Activity implements View.OnClickListener{

    int selectedCommentId;
    CommentRepository commentRepository = new CommentRepository();
    public DeepCommentListModel deepCommentDetail; //루트 댓글 받을객체
    public Vector<DeepCommentListModel> vector = new Vector<DeepCommentListModel>();


    private ListView m_ListView;
    private DeepCommentAdapter m_Adapter;

    //헤더 관련
    public View header;
    public ImageButton ib_back, ib_submit;
    public TextView tv_comment_content, tv_cooment_date;
    public EditText et_comment_content;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deep_comment);

        Intent intent = getIntent();
        selectedCommentId = Integer.parseInt(intent.getStringExtra("selectedCommentId"));

        initListView();

        new SetDetailComment().execute();

        new GetDeepCommentList().execute();



    }

    public void initListView(){
        // 커스텀 어댑터 생성
        m_Adapter = new DeepCommentAdapter(vector);

        // Xml에서 추가한 ListView 연결
        m_ListView = (ListView) findViewById(R.id.lv_deep_comment);

        //헤더추가
        header = getLayoutInflater().inflate(R.layout.header_deep_comment, null, false) ;
        m_ListView.addHeaderView(header);

        //헤더뷰 안의 컴포넌트 초기화 및 이벤트
        initHeaderComponent();

        // ListView에 어댑터 연결
        m_ListView.setAdapter(m_Adapter);

    }

    public void initHeaderComponent(){
        ib_back = (ImageButton) header.findViewById(R.id.ib_deep_back);
        ib_submit = (ImageButton) header.findViewById(R.id.ib_deep_comment);
        tv_comment_content = (TextView) header.findViewById(R.id.tv_deep_content);
        tv_cooment_date = (TextView) header.findViewById(R.id.tv_deep_date);
        et_comment_content = (EditText) header.findViewById(R.id.et_deep_comment);

        ib_back.setOnClickListener(this);
        ib_submit.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ib_deep_back: //창닫기
                finish();
                break;
            case R.id.ib_deep_comment:
                Toast.makeText(getApplicationContext(),"전송",Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

    private class SetDetailComment extends AsyncTask<Boolean, Void, Void> {

        ProgressDialog progressDialog;

        @Override
        protected Void doInBackground(Boolean... params) {
            try
            {
                //Getting data from server
                deepCommentDetail = commentRepository.getDeepCommentDetail(getUserId(),selectedCommentId);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progressDialog.dismiss();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    tv_comment_content.setText(deepCommentDetail.getContent());
                    tv_cooment_date.setText(deepCommentDetail.getWriteDate());
                }
            });

        }


        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(DeepCommentActivity.this);
            progressDialog.setMessage("불러오는중..");
            progressDialog.setCancelable(true);
            progressDialog.show();
        }

        private int getUserId(){
            SharedPreferences pref =  getSharedPreferences("userId", Context.MODE_PRIVATE);
            return Integer.parseInt(pref.getString("userId", ""));
        }
    }//SetDetailList Class();;

    private class GetDeepCommentList extends AsyncTask<Boolean, Void, Void> {

        ProgressDialog progressDialog;

        @Override
        protected Void doInBackground(Boolean... params) {
            try
            {
                //Getting data from server
                vector = commentRepository.getDeepCommentList(getUserId(), selectedCommentId, 1);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progressDialog.dismiss();
            if(vector.size() != 0){
                m_Adapter.updateList(vector);
            }



        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(DeepCommentActivity.this);
            progressDialog.setMessage("불러오는중..");
            progressDialog.setCancelable(true);
            progressDialog.show();
        }

        private int getUserId(){
            SharedPreferences pref =  getSharedPreferences("userId", Context.MODE_PRIVATE);
            return Integer.parseInt(pref.getString("userId", ""));
        }
    }//GetCommentList Class();;

}
