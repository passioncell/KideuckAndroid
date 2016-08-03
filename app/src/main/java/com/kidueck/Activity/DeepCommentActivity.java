package com.kidueck.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.kidueck.Adapter.DeepCommentAdapter;
import com.kidueck.Common.PointReceiver;
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


    //페이징 관련
    int pageNumber = 1;
    int preVectorSize = 0;
    int nowVectorSize = 0;
    boolean isFirstRoof = true;
    boolean lastitemVisibleFlag = false;
    private boolean mLockListView = false;

    //댓글의 댓글 쓰기 관련
    String inputDeepComment;
    boolean submitResult = false;


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
        header = getLayoutInflater().inflate(R.layout.header_deep_comment, null, false);
        m_ListView.addHeaderView(header);

        //헤더뷰 안의 컴포넌트 초기화 및 이벤트
        initHeaderComponent();

        //무한스크롤 리스너연결
        m_ListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                //현재 화면에 보이는 첫번째 리스트 아이템의 번호(firstVisibleItem) + 현재 화면에 보이는 리스트 아이템의 갯수(visibleItemCount)가 리스트 전체의 갯수(totalItemCount) -1 보다 크거나 같을때
                lastitemVisibleFlag = (totalItemCount > 0) && (firstVisibleItem + visibleItemCount >= totalItemCount);
            }

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                //OnScrollListener.SCROLL_STATE_IDLE은 스크롤이 이동하다가 멈추었을때 발생되는 스크롤 상태입니다.
                //즉 스크롤이 바닦에 닿아 멈춘 상태에 처리를 하겠다는 뜻
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && lastitemVisibleFlag && mLockListView == false) {
                    //TODO 화면이 바닦에 닿을때 처리
                    mLockListView = true;
                    pageNumber++;

                    new GetDeepCommentList().execute();

                    if((vector.size() % 10) != 0){
                        m_ListView.setOnScrollListener(null);
                    }
                }
            }

        });

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
                onBackPressed();
                break;
            case R.id.ib_deep_comment:
                inputDeepComment = et_comment_content.getText().toString();
                if (inputDeepComment.trim().equals("") ) {
                    Toast.makeText(getApplicationContext(), "글을 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    new SubmitDeepComment().execute();
                    DetailActivity.COMMENT_CNT = DetailActivity.COMMENT_CNT + 1;
                    System.out.println("DetailActivity.COMMENT_CNT : " + DetailActivity.COMMENT_CNT);
                }
                break;
            default:
                break;
        }
    }


    @Override
    public void onBackPressed() {
//        super.onBackPressed();

        finish();
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
                vector = commentRepository.getDeepCommentList(getUserId(), selectedCommentId, pageNumber);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onPostExecute(Void aVoid) {

            mLockListView = true;

            progressDialog.dismiss();
            if(isFirstRoof){ //첫루프
                nowVectorSize = vector.size();
                //어뎁터에 벡터 데이터 추가
                for(int i=(pageNumber-1)*10; i<vector.size(); i++){
                    m_Adapter.addItem(vector.get(i));
                }
                isFirstRoof = false;
            }else{
                preVectorSize = nowVectorSize;
                nowVectorSize = vector.size();

                if(preVectorSize == nowVectorSize){ //이전벡터와 루프를 실행한 벡터 사이즈가 같으면 더이상 불러올 리스트가없는것임.
                    //Toast.makeText(getApplicationContext(),"페이지의 끝", Toast.LENGTH_LONG).show();
                    m_ListView.setOnScrollListener(null);
                }else{
                    //어뎁터에 벡터 데이터 추가
                    for(int i=(pageNumber-1)*10; i<vector.size(); i++){
                        m_Adapter.addItem(vector.get(i));
                    }
                }
            }

            mLockListView = false;
            m_Adapter.notifyDataSetChanged();

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

    private class SubmitDeepComment extends AsyncTask<Boolean, Void, Void> {

        ProgressDialog progressDialog;

        @Override
        protected Void doInBackground(Boolean... params) {
            try
            {
                //Getting data from server
                submitResult = commentRepository.writeDeepComment(getUserId(), selectedCommentId, inputDeepComment);
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
                    et_comment_content.setText("");
                    Toast.makeText(getApplicationContext(), "댓글이 등록되었습니다", Toast.LENGTH_SHORT).show();
                    recreate();
                    sendBroadcast(new Intent(MainActivity.getInstace(), PointReceiver.class));

                }
            });
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(DeepCommentActivity.this);
            progressDialog.setMessage("댓글 등록중..");
            progressDialog.setCancelable(true);
            progressDialog.show();
        }

        private int getUserId(){
            SharedPreferences pref =  getSharedPreferences("userId", Context.MODE_PRIVATE);
            return Integer.parseInt(pref.getString("userId", ""));
        }
    }//SubmitComment Class();;


}
