package com.kidueck.Fragment;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.kidueck.Activity.DetailActivity;
import com.kidueck.Common.ApplicationController;
import com.kidueck.Common.PointReceiver;
import com.kidueck.Common.URLInfo;
import com.kidueck.Concrete.PostingRepository;
import com.kidueck.ListData.Posting;
import com.kidueck.Model.PostingListModel;
import com.kidueck.R;
import com.kidueck.Util.ActivityResultBus;
import com.kidueck.Util.ActivityResultEvent;
import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Vector;

/**
 * Created by system777 on 2016-06-25.
 */
public class FeedFragment extends Fragment implements  AdapterView.OnItemClickListener, AbsListView.OnScrollListener, View.OnClickListener {

    public static int REQUEST_DETAIL = 100;

    View view;

    private ListView mListView = null;
    private ListViewAdapter  mAdapter = null;

    public PostingRepository postingRepository = new PostingRepository();
    public Vector<PostingListModel> vector;
    public boolean voteResult;

    //페이징 관련
    private boolean mLockListView;
    int pageNumber = 1;
    int preVectorSize=0;
    int nowVectorSize=0;
    boolean isFirstRoof = true;

    //카테고리
    int selectedCategoryIdx = 1;
    Button btn_new;
    Button btn_hot;
    Button btn_my;

    //UI REFRESH
    int selectedListviewPosition;

    ArrayList<Posting> datas = new ArrayList();

    public static FeedFragment newInstance() {
        FeedFragment fragment = new FeedFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {super.onCreate(savedInstanceState);    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        view = inflater.inflate(R.layout.fragment_feed, container, false);
        init(view);
        initListView(view);

        return view;
    }

    private void init(View view){
        btn_new = (Button) view.findViewById(R.id.bt_feed_new);
        btn_hot = (Button) view.findViewById(R.id.bt_feed_hot);
        btn_my = (Button) view.findViewById(R.id.bt_feed_my);

        btn_new.setOnClickListener(this);
        btn_hot.setOnClickListener(this);
        btn_my.setOnClickListener(this);

        initCategoryButton(selectedCategoryIdx);
    }

    private void initListView(View view){

        vector =  new Vector<PostingListModel>();
        mAdapter = new ListViewAdapter(getContext());
        // 리스트뷰 참조 및 Adapter달기
        mListView = (ListView) view.findViewById(R.id.lv_feed);

        mListView.setOnItemClickListener(this);
        mListView.setOnScrollListener(this);
        mListView.setAdapter(mAdapter);

        new GetPostingList().execute();

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //리스트뷰 행 클릭
        selectedListviewPosition = position;
        Intent intent = new Intent(getActivity(),DetailActivity.class);
        intent.putExtra("selectedPostingId", vector.get(position).postingId);
        getActivity().startActivityForResult(intent, REQUEST_DETAIL);

    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Don't forget to check requestCode before continuing your job
        if (requestCode == REQUEST_DETAIL) {
            // Do your job

            int getChangedCommentCnt = data.getIntExtra("commentCnt", 0);
            int getChangedIsUpDown = data.getIntExtra("isUpDownType", 0);


            Posting model = new Posting();
            model = datas.get(selectedListviewPosition);

            if(getChangedCommentCnt != 0){
                datas.get(selectedListviewPosition).setCommentCnt(String.valueOf(
                        Integer.parseInt(datas.get(selectedListviewPosition).getCommentCnt()) +
                getChangedCommentCnt));

            }

            if(getChangedIsUpDown == 1){
                datas.get(selectedListviewPosition).setTotalVoteCount(datas.get(selectedListviewPosition).getTotalVoteCount() +
                        1);
                datas.get(selectedListviewPosition).setUpButton(getResources().getDrawable(R.drawable.icon_up_on));


            }else if(getChangedIsUpDown == 2){
                datas.get(selectedListviewPosition).setTotalVoteCount(datas.get(selectedListviewPosition).getTotalVoteCount() -
                        1);
                datas.get(selectedListviewPosition).setDownButton(getResources().getDrawable(R.drawable.icon_down_on));


            }

            mAdapter.notifyDataSetChanged();


            Toast.makeText(getContext(), "댓글변화" + String.valueOf(getChangedCommentCnt)  +
                    "업다운변화" + String.valueOf(getChangedIsUpDown) ,Toast.LENGTH_SHORT).show();

        }
    }


    @Override
    public void onStart() {
        super.onStart();
        ActivityResultBus.getInstance().register(mActivityResultSubscriber);
    }

    @Override
    public void onStop() {
        super.onStop();
        ActivityResultBus.getInstance().unregister(mActivityResultSubscriber);
    }

    private Object mActivityResultSubscriber = new Object() {
        @Subscribe
        public void onActivityResultReceived(ActivityResultEvent event) {
            int requestCode = event.getRequestCode();
            int resultCode = event.getResultCode();
            Intent data = event.getData();
            onActivityResult(requestCode, resultCode, data);
        }
    };




    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        // 현재 가장 처음에 보이는 셀번호와 보여지는 셀번호를 더한값이
        // 전체의 숫자와 동일해지면 가장 아래로 스크롤 되었다고 가정합니다.
        int count = totalItemCount - visibleItemCount;

        if(firstVisibleItem >= count && totalItemCount != 0
                && mLockListView == false)
        {
            Log.i("LOG", "Loading next items");
            addNextDatas();
        }

    }

    private void addNextDatas()
    {
        // 아이템을 추가하는 동안 중복 요청을 방지하기 위해 락을 걸어둡니다.
        mLockListView = true;
        pageNumber++;

        new GetPostingList().execute();

    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onClick(View v) {

        Tracker t = ((ApplicationController)getActivity().getApplication()).getTracker(ApplicationController.TrackerName.APP_TRACKER);

        switch (v.getId()){
            case R.id.bt_feed_new:
                if(selectedCategoryIdx != 1){
                    FeedFragment feedFrag;
                    feedFrag = FeedFragment.newInstance();
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.ll_frag_content, feedFrag).commit();
                    t.send(new HitBuilders.EventBuilder().setCategory("FeedFragment").setAction("Press NEW Category").setLabel("New Category Click").build());
                }
                break;
            case R.id.bt_feed_hot:
                if(selectedCategoryIdx != 2){
                    FeedHotFragment feedHotFrag;
                    feedHotFrag = FeedHotFragment.newInstance();
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.ll_frag_content, feedHotFrag).commit();
                    t.send(new HitBuilders.EventBuilder().setCategory("FeedFragment").setAction("Press Hot Category").setLabel("Hot Category Click").build());
                }
                break;
            case R.id.bt_feed_my:
                if(selectedCategoryIdx != 3){
                    FeedMyFragment feedMyFag;
                    feedMyFag = FeedMyFragment.newInstance();
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.ll_frag_content, feedMyFag).commit();
                    t.send(new HitBuilders.EventBuilder().setCategory("FeedFragment").setAction("Press MY Category").setLabel("My Category Click").build());
                }
                break;
            default:
                break;
        }

    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private void initCategoryButton(int selectedCategoryIdx){
        switch (selectedCategoryIdx){
            case 1:
                btn_new.setBackground(getResources().getDrawable(R.drawable.round_stroke_fill));
                btn_new.setTextColor(Color.WHITE);
                break;
            case 2:
                btn_hot.setBackground(getResources().getDrawable(R.drawable.round_stroke_fill));
                btn_hot.setTextColor(Color.WHITE);
                break;
            case 3:
                btn_my.setBackground(getResources().getDrawable(R.drawable.round_stroke_fill));
                btn_my.setTextColor(Color.WHITE);
                break;
            default:
                break;
        }
    }

    //ListView Adapter
    public class ListViewAdapter extends BaseAdapter {
        private Context mContext = null;

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
                convertView = inflater.inflate(R.layout.listitem_feed, null);

                holder.content = (TextView) convertView.findViewById(R.id.tv_feed_content); //본문
                holder.date = (TextView) convertView.findViewById(R.id.tv_feed_date); //날짜
                holder.commentCnt = (TextView) convertView.findViewById(R.id.tv_feed_comment_cnt); //댓글수
                holder.totalVoteCnt = (TextView) convertView.findViewById(R.id.tv_feed_total_cnt); //업다운 총점

                //업다운, 딜리트 버튼
                holder.upButton = (ImageButton) convertView.findViewById(R.id.ib_feed_up);
                holder.downButton = (ImageButton) convertView.findViewById(R.id.ib_feed_down);
                holder.writerIcon = (ImageView) convertView.findViewById(R.id.iv_feed_writer_icon);

                //첨부이미지
                holder.attachdImg = (ImageView) convertView.findViewById(R.id.iv_feed_attached_img);

                holder.upButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Tracker t = ((ApplicationController)getActivity().getApplication()).getTracker(ApplicationController.TrackerName.APP_TRACKER);

                        Integer pos = (Integer)v.getTag();
                        new VotePosting(datas.get(pos).getPostingId(), 1).execute();
                        if(datas.get(pos).isUpDown == 0){//투표한적없는경우 UP버튼 ON으로 변경 및 총점 +1
                            datas.get(pos).totalVoteCount += 1;
                            datas.get(pos).isUpDown = 1;
                            mAdapter.notifyDataSetChanged();
                            //포인트 갱신
                            getContext().sendBroadcast(new Intent(getActivity(), PointReceiver.class));
                            t.send(new HitBuilders.EventBuilder().setCategory("FeedFragment").setAction("Press UP Button").setLabel("UP Click").build());

                        }
                    }
                });
                holder.downButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Tracker t = ((ApplicationController)getActivity().getApplication()).getTracker(ApplicationController.TrackerName.APP_TRACKER);

                        Integer pos = (Integer)v.getTag();
                        new VotePosting(datas.get(pos).getPostingId(), 2).execute();
                        if(datas.get(pos).isUpDown == 0){//투표한적없는경우 DOWN버튼 ON으로 변경 및 총점 -1
                            datas.get(pos).totalVoteCount -= 1;
                            datas.get(pos).isUpDown = -1;
                            mAdapter.notifyDataSetChanged();
                            //포인트 갱신
                            getContext().sendBroadcast(new Intent(getActivity(), PointReceiver.class));
                            t.send(new HitBuilders.EventBuilder().setCategory("FeedFragment").setAction("Press DOWN Button").setLabel("DOWN Click").build());
                        }
                    }
                });

                convertView.setTag(holder);
            }else{
                holder = (ViewHolder) convertView.getTag();
            }

            Posting data = datas.get(position);

            holder.content.setText(data.content);
            holder.date.setText(data.date);
            holder.writerIcon.setImageDrawable(data.deleteButton);
            if(datas.get(position).isWriter) { //글쓴이가 아니면 감추기
                holder.writerIcon.setVisibility(View.VISIBLE);
            }else{
                holder.writerIcon.setVisibility(View.GONE);
            }
            holder.commentCnt.setText(data.commentCnt);
            holder.totalVoteCnt.setText(String.valueOf(data.totalVoteCount));

            //일단 전부다 OFF로 만듬(이상하게 나오는거 방지)
            holder.upButton.setBackground((getResources().getDrawable(R.drawable.icon_up_off)));
            holder.downButton.setBackground((getResources().getDrawable(R.drawable.icon_down_off)));

            //업다운 버튼 눌렀는지 확인후 on/off 적용
            if(datas.get(position).isUpDown == 1 ){ //UP을 눌렀었던 경우
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) { //SDK 15이상만 지원
                    holder.upButton.setBackground(getResources().getDrawable(R.drawable.icon_up_on));
                }
            }else if(datas.get(position).isUpDown == -1 ){ //DOWN을 눌렀었던 경우
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) { //SDK 15이상만 지원
                    holder.downButton.setBackground(getResources().getDrawable(R.drawable.icon_down_on));
                }
            }else{

            }

            holder.upButton.setTag(position);
            holder.downButton.setTag(position);

            holder.attachdImg.setImageDrawable(null);

            if(data.isImage){
                Picasso.with(getContext()).load(new URLInfo().getPostImgUploadUrl() + data.getPostingId() + "/1.jpg").into(holder.attachdImg);
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
        public void addItem(PostingListModel postingListModel) {
            Posting addInfo = null;
            addInfo = new Posting();

            addInfo.content = postingListModel.content;
            addInfo.date = postingListModel.writtenDate;
            addInfo.commentCnt = postingListModel.commentCnt;
            addInfo.totalVoteCount = postingListModel.totalVoteCnt;
            addInfo.isUpDown = postingListModel.isUpDown;
            addInfo.isWriter = postingListModel.isWriter;
            addInfo.postingId = Integer.parseInt(postingListModel.postingId);
            addInfo.isImage = postingListModel.isImage;

            datas.add(addInfo);
        }


    }//Adapter class ();;

    private class ViewHolder {
        public TextView content; //본문
        public TextView date; //날짜
        public ImageView writerIcon; //삭제버튼
        public TextView commentCnt; //댓글수
        public TextView totalVoteCnt; //업다운 총점
        public ImageButton upButton;
        public ImageButton downButton;
        public ImageView attachdImg;
    }//view holder class ();;

    //포스팅 리스트 가져올 내부클래스(쓰레드)
    private class GetPostingList extends AsyncTask<Boolean, Void, Void> {

        ProgressDialog progressDialog;

        @Override
        protected Void doInBackground(Boolean... params) {
            try
            {
                //Getting data from server
                vector = postingRepository.getPostingList(getUserId(), pageNumber);
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
                    Snackbar.make(getView(), "페이지의 끝입니다.", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
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
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("불러오는중..");
            progressDialog.setCancelable(true);
            progressDialog.show();
        }

        private int getUserId(){
            SharedPreferences pref =  getActivity().getSharedPreferences("userId", Context.MODE_PRIVATE);
            return Integer.parseInt(pref.getString("userId", ""));
        }
    }//GetPostingList Class();;

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
                getActivity().setResult(Activity.RESULT_OK, null);
                if(type == 1){
                    Snackbar.make(getView(), "해당 게시물을 UP 하였습니다.", Snackbar.LENGTH_SHORT).setAction("Action", null).setAction("닫기", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                        }
                    }).show();;
                }else{
                    Snackbar.make(getView(), "해당 게시물을 DOWN 하였습니다.", Snackbar.LENGTH_SHORT).setAction("Action", null).setAction("닫기", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                        }
                    }).show();
                }

            }else {
                getActivity().setResult(Activity.RESULT_CANCELED, null);
                Toast.makeText(getContext(), "이미 투표를 하셨습니다.", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("처리중..");
            progressDialog.setCancelable(true);
            progressDialog.show();
        }

        private int getUserId(){
            SharedPreferences pref =  getActivity().getSharedPreferences("userId", Context.MODE_PRIVATE);
            return Integer.parseInt(pref.getString("userId", ""));
        }
    }//VotePosting Class();;

}
