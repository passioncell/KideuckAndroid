package com.kidueck.Activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareButton;
import com.facebook.share.widget.ShareDialog;
import com.kidueck.Common.PointReceiver;
import com.kidueck.Concrete.CommentRepository;
import com.kidueck.Concrete.PostingRepository;
import com.kidueck.ListData.Comment;
import com.kidueck.Model.CommentListModel;
import com.kidueck.Model.DetailPostingModel;
import com.kidueck.R;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;

/**
 * Created by system777 on 2016-06-27.
 */
public class DetailActivity extends Activity implements  AdapterView.OnItemClickListener, AbsListView.OnScrollListener,
        View.OnClickListener{

    public int selectedPostingId;

    private ListView mListView = null;
    private ListViewAdapter mAdapter = null;

    public CommentRepository commentRepository = new CommentRepository();
    public Vector<CommentListModel> vector = new Vector<CommentListModel>();

    public PostingRepository postingRepository = new PostingRepository();
    public Vector<DetailPostingModel> detailVector = new Vector<DetailPostingModel>();
    public TextView writtenDate, content;
    public EditText commentContent;
    public ImageButton submitComment, backButton;
    public Button btn_up, btn_down;

    public ImageView attachedImg;
    ProgressDialog pd;

    //공유하기
    public ShareButton shareButton;
    CallbackManager callbackManager;
    ShareDialog shareDialog;
    ShareLinkContent linkContent;


    //페이징 관련
    private boolean mLockListView;
    int pageNumber = 0;
    int tempCount= 0;
    int nowVectorSize;
    int preVectorSize;
    boolean isFirstRoof = true;


    public boolean commentSubmitResult = false;
    String inputComment="";

    public int changedCommentCnt = 0;
    public int changedIsUpDown = 0;

    //UP DOWN
    boolean voteResult = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        init();
        initShare();

    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void init(){

        Intent intent = getIntent();
        selectedPostingId = Integer.parseInt(intent.getStringExtra("selectedPostingId"));


        mListView = (ListView) findViewById(R.id.lv_detail_comment);
        mAdapter = new ListViewAdapter(this);

        View header = getLayoutInflater().inflate(R.layout.header_detail, null, false);
        mListView.addHeaderView(header);

        writtenDate = (TextView) findViewById(R.id.tv_detail_date);
        content = (TextView) findViewById(R.id.tv_detail_content);
        commentContent = (EditText) findViewById(R.id.et_detail_comment);
        btn_up = (Button) findViewById(R.id.bt_detail_up);
        btn_down = (Button) findViewById(R.id.bt_detail_down);
        shareButton = (ShareButton) findViewById(R.id.bt_detail_share);
        submitComment = (ImageButton) findViewById(R.id.ib_detail_comment);
        attachedImg = (ImageView) findViewById(R.id.iv_detail_attached_img);
        attachedImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetailActivity.this, DetailPostImage.class);
                startActivity(intent);
            }
        });

        submitComment.setOnClickListener(this);
        backButton = (ImageButton) findViewById(R.id.ib_detail_back);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        btn_up.setOnClickListener(this);
        btn_down.setOnClickListener(this);

        mListView.setOnItemClickListener(this);
        mListView.setOnScrollListener(this);
        mListView.setAdapter(mAdapter);


        new SetDetailPosting().execute();

        //첨부 이미지 표시
        try {
            getattachedImg();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void initShare(){

        //공유하기
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);
        // this part is optional
        shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
            @Override
            public void onSuccess(Sharer.Result result) {
                //Toast.makeText(getApplicationContext(), "성공", Toast.LENGTH_LONG).show();

            }

            @Override
            public void onCancel() {
                //Toast.makeText(getApplicationContext(), "취소", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(FacebookException error) {
                Toast.makeText(getApplicationContext(), "오류발생", Toast.LENGTH_LONG).show();
            }
        });


    }

    public void getattachedImg() throws IOException {
        attachedImg.setVisibility(View.VISIBLE);
        Picasso.with(getApplicationContext()).load("http://dayot.seobuchurch.or.kr/upload/post/395/1.jpg").into(attachedImg);

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //리스트뷰 행 클릭
        Intent intent = new Intent(DetailActivity.this, TestActivity.class);
        startActivity(intent);

    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        // 현재 가장 처음에 보이는 셀번호와 보여지는 셀번호를 더한값이
        // 전체의 숫자와 동일해지면 가장 아래로 스크롤 되었다고 가정합니다.
        if(tempCount == 0){
            int count = (totalItemCount+1) - visibleItemCount;

            if(firstVisibleItem >= count && totalItemCount != 0
                    && mLockListView == false)
            {
                Log.i("LOG", "Loading next items");
                addNextDatas();
            }
            tempCount++;
        }else{
            int count = totalItemCount - visibleItemCount;

            if(firstVisibleItem >= count && totalItemCount != 0
                    && mLockListView == false)
            {
                Log.i("LOG", "Loading next items");
                addNextDatas();
            }
        }
    }

    private void addNextDatas()
    {
        // 아이템을 추가하는 동안 중복 요청을 방지하기 위해 락을 걸어둡니다.
        mLockListView = true;
        pageNumber++;

        new GetCommentList().execute();

        if((vector.size() % 10) != 0){
            mListView.setOnScrollListener(null);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ib_detail_comment:
                inputComment = commentContent.getText().toString();
                if (inputComment.trim().equals("") ) {
                    Toast.makeText(getApplicationContext(), "글을 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    submitComment.setEnabled(false);
                    new SubmitComment().execute();
                }
                break;
            case R.id.bt_detail_up:
                new VotePosting(selectedPostingId,  1).execute();
                break;
            case R.id.bt_detail_down:
                new VotePosting(selectedPostingId,  2).execute();
                break;
            case R.id.bt_detail_share:
                if (shareDialog.canShow(ShareLinkContent.class)) {
                    ShareLinkContent linkContent = new ShareLinkContent.Builder()
                            .setContentUrl(Uri.parse("https://play.google.com/store/apps/details?id=com.kideuck"))
                            .setContentTitle("키득")
                            .setContentDescription((content.getText().toString()))
                            .setImageUrl(Uri.parse("http://dayot.seobuchurch.or.kr/upload/etc/icon_kidueck.jpg"))
                            .setQuote("\" 우리동네 SNS \"")
                            .build();
                    shareDialog.show(linkContent);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
       // super.onBackPressed();

        Intent intent = new Intent(Intent.ACTION_SEND);
        // 댓글과 UP DOWN 변화를 FeedFragment에 알림.
        intent.putExtra("commentCnt", changedCommentCnt);
        intent.putExtra("isUpDownType", changedIsUpDown);

        setResult(RESULT_OK, intent);
        finish();

    }


    //ListView Adapter
    public class ListViewAdapter extends BaseAdapter {
        private Context mContext = null;
        // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
        ArrayList<Comment> datas = new ArrayList();

        // ListViewAdapter의 생성자
        public ListViewAdapter(Context mContext) {
            super();
            this.mContext = mContext;
        }

        // Adapter에 사용되는 데이터의 개수를 리턴. : 필수 구현
        @Override
        public int getCount() {
            return datas.size() ;
        }

        // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴. : 필수 구현
        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            if(convertView == null){
                holder = new ViewHolder();

                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.listitem_comment, null);

                holder.comment = (TextView) convertView.findViewById(R.id.tv_comment_content);
                holder.date = (TextView) convertView.findViewById(R.id.tv_comment_date);
                holder.isCommenterIcon = (ImageView) convertView.findViewById(R.id.ib_comment_writer);

                convertView.setTag(holder);
            }else{
                holder = (ViewHolder) convertView.getTag();
            }

            Comment data = datas.get(position);

            holder.comment.setText(data.commentContent);
            holder.date.setText(data.writtenDate);

            if(datas.get(position).isCommenter){
                holder.isCommenterIcon.setVisibility(View.VISIBLE);
            }else{
                holder.isCommenterIcon.setVisibility(View.GONE);
            }

            return convertView;
        }

        // 지정한 위치(position)에 있는 데이터와 관계된 아이템(row)의 ID를 리턴. : 필수 구현
        @Override
        public long getItemId(int position) {
            return position ;
        }

        // 지정한 위치(position)에 있는 데이터 리턴 : 필수 구현
        @Override
        public Object getItem(int position) {
            return datas.get(position) ;
        }

        // 아이템 데이터 추가를 위한 함수. 개발자가 원하는대로 작성 가능.
        public void addItem(CommentListModel commentListModel) {
            Comment addInfo = null;
            addInfo = new Comment();

            addInfo.commentId = commentListModel.getCommentId();
            addInfo.writtenDate = commentListModel.getWriteDate();
            addInfo.commentContent = commentListModel.getContent();
            addInfo.isCommenter = commentListModel.isCommenter();
            addInfo.lat = commentListModel.getLat();
            addInfo.lon = commentListModel.getLon();
            addInfo.isVisible = commentListModel.isVisible();

            datas.add(addInfo);
        }

        public void clearAllData()
        {
            datas.clear();
            this.notifyDataSetChanged();
        }
    }//Adapter class ();;


    private class ViewHolder {
        public ImageView isCommenterIcon;
        public TextView comment;
        public TextView date;
    }//ViewHolder Class();


    private class GetCommentList extends AsyncTask<Boolean, Void, Void> {

        ProgressDialog progressDialog;

        @Override
        protected Void doInBackground(Boolean... params) {
            try
            {
                //Getting data from server
                vector = commentRepository.getCommentList(getUserId(), selectedPostingId, pageNumber);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progressDialog.dismiss();

            if(isFirstRoof){ //첫루프
                nowVectorSize = vector.size();
                //어뎁터에 벡터 데이터 추가
                for(int i=(pageNumber-1)*10; i<vector.size(); i++){
                    mAdapter.addItem(vector.get(i));
                }
                isFirstRoof = false;
            }else{
                preVectorSize = nowVectorSize;
                nowVectorSize = vector.size();

                if(preVectorSize == nowVectorSize){ //이전벡터와 루프를 실행한 벡터 사이즈가 같으면 더이상 불러올 리스트가없는것임.
                    //Toast.makeText(getApplicationContext(),"페이지의 끝", Toast.LENGTH_LONG).show();
                    mListView.setOnScrollListener(null);
                }else{
                    //어뎁터에 벡터 데이터 추가
                    for(int i=(pageNumber-1)*10; i<vector.size(); i++){
                        mAdapter.addItem(vector.get(i));
                    }
                }
            }

            mLockListView = false;
            mAdapter.notifyDataSetChanged();

        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(DetailActivity.this);
            progressDialog.setMessage("불러오는중..");
            progressDialog.setCancelable(true);
            progressDialog.show();
        }

        private int getUserId(){
            SharedPreferences pref =  getSharedPreferences("userId", Context.MODE_PRIVATE);
            return Integer.parseInt(pref.getString("userId", ""));
        }
    }//GetCommentList Class();;

    private class SetDetailPosting extends AsyncTask<Boolean, Void, Void> {

        ProgressDialog progressDialog;

        @Override
        protected Void doInBackground(Boolean... params) {
            try
            {
                //Getting data from server
                detailVector = postingRepository.getDetailPosting(selectedPostingId, getUserId());
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
                @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public void run() {
                    writtenDate.setText(detailVector.get(0).getWrittenDate());
                    content.setText(detailVector.get(0).getContent());

                    //업다운 표시
                    setUpDownButton(detailVector.get(0).isUpDown);

                    //공유하기
                    linkContent = new ShareLinkContent.Builder()
                            .setContentUrl(Uri.parse("https://play.google.com/store/apps/details?id=com.kideuck"))
                            .setContentTitle("키득")
                            .setContentDescription((content.getText().toString()))
                            .setImageUrl(Uri.parse("http://dayot.seobuchurch.or.kr/upload/etc/icon_kidueck.jpg"))
                           // .setQuote("\" 우리동네 SNS \"")
                            .build();

                    shareButton.setShareContent(linkContent);

                }
            });

        }

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        public void setUpDownButton(int isUpDown) {
            if(isUpDown == 1){
                btn_up.setBackground(getResources().getDrawable(R.drawable.round_stroke_fill));
                btn_up.setTextColor(Color.WHITE);
            }else if(isUpDown == -1){
                btn_down.setBackground(getResources().getDrawable(R.drawable.round_stroke_fill));
                btn_down.setTextColor(Color.WHITE);
            }
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(DetailActivity.this);
            progressDialog.setMessage("불러오는중..");
            progressDialog.setCancelable(true);
            progressDialog.show();
        }

        private int getUserId(){
            SharedPreferences pref =  getSharedPreferences("userId", Context.MODE_PRIVATE);
            return Integer.parseInt(pref.getString("userId", ""));
        }
    }//SetDetailList Class();;

    private class SubmitComment extends AsyncTask<Boolean, Void, Void> {

        ProgressDialog progressDialog;

        @Override
        protected Void doInBackground(Boolean... params) {
            try
            {
                //Getting data from server
                commentSubmitResult = commentRepository.writeComment(getUserId(), selectedPostingId, inputComment);
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
                    commentContent.setText("");
                    submitComment.setEnabled(true);
                    Toast.makeText(getApplicationContext(), "댓글이 등록되었습니다", Toast.LENGTH_SHORT).show();
                    recreate();
                    sendBroadcast(new Intent(MainActivity.getInstace(), PointReceiver.class));

                }
            });
            changedCommentCnt++;


        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(DetailActivity.this);
            progressDialog.setMessage("댓글 등록중..");
            progressDialog.setCancelable(true);
            progressDialog.show();
        }

        private int getUserId(){
            SharedPreferences pref =  getSharedPreferences("userId", Context.MODE_PRIVATE);
            return Integer.parseInt(pref.getString("userId", ""));
        }
    }//SubmitComment Class();;


    private class VotePosting extends AsyncTask<Boolean, Void, Void> {

        ProgressDialog progressDialog;
        int postingId;
        int type;

        public VotePosting(int postingId, int type){
            this.postingId = postingId;
            this.type =type;
        }

        @Override
        protected Void doInBackground(Boolean... params) {
            try
            {
                //Getting data from server
                voteResult = postingRepository.votePosting(getUserId(), postingId, type);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progressDialog.dismiss();

            if (voteResult) {
                    setResult(Activity.RESULT_OK, null);
            if(type == 1){
                setUpDownButton(1);
                changedIsUpDown = 1;
            }else{
                setUpDownButton(-1);
                changedIsUpDown = 2;
            }

            }else {
                setResult(Activity.RESULT_CANCELED, null);
                Toast.makeText(getApplicationContext(), "이미 투표를 하셨습니다.", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(DetailActivity.this);
            progressDialog.setMessage("처리중..");
            progressDialog.setCancelable(true);
            progressDialog.show();
        }

        private int getUserId(){
            SharedPreferences pref =  getSharedPreferences("userId", Context.MODE_PRIVATE);
            return Integer.parseInt(pref.getString("userId", ""));
        }

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        public void setUpDownButton(int isUpDown) {
            if(isUpDown == 1){
                btn_up.setBackground(getResources().getDrawable(R.drawable.round_stroke_fill));
                btn_up.setTextColor(Color.WHITE);
            }else if(isUpDown == -1){
                btn_down.setBackground(getResources().getDrawable(R.drawable.round_stroke_fill));
                btn_down.setTextColor(Color.WHITE);
            }
        }
    }//VotePosting Class();;

}
