package com.kidueck.Fragment;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.kidueck.Activity.DetailActivity;
import com.kidueck.Concrete.NoticeRepository;
import com.kidueck.ListData.Notice;
import com.kidueck.Model.NoticeListModel;
import com.kidueck.R;

import java.util.ArrayList;
import java.util.Vector;

/**
 * Created by system777 on 2016-06-25.
 */
public class NoticeFragment extends Fragment implements AdapterView.OnItemClickListener,  AbsListView.OnScrollListener {

    private ListView mListView = null;
    private ListViewAdapter  mAdapter = null;

    public NoticeRepository noticeRepository = new NoticeRepository();
    public Vector<NoticeListModel> vector = new Vector<NoticeListModel>();

    //페이징 관련
    private boolean mLockListView;
    int pageNumber = 1;
    int preVectorSize=0;
    int nowVectorSize=0;
    boolean isFirstRoof = true;


    public LinearLayout noDateLayout;

    public static NoticeFragment newInstance() {
        NoticeFragment fragment = new NoticeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {super.onCreate(savedInstanceState);    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_notice, container, false);
        initListView(view);

        return view;
    }

    private void initListView(View view){

        mAdapter = new ListViewAdapter(getContext());
        // 리스트뷰 참조 및 Adapter달기
        mListView = (ListView) view.findViewById(R.id.lv_notice);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);

        mListView.setOnScrollListener(this);
        mListView.setAdapter(mAdapter);

        noDateLayout = (LinearLayout) view.findViewById(R.id.ll_notice_no_data);

        new GetNoticeList().execute();

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
       //리스트뷰 행 클릭
        Intent intent = new Intent(getActivity(),DetailActivity.class);
        intent.putExtra("selectedPostingId", String.valueOf(vector.get(position).getTargetPostingId()));
        getActivity().startActivity(intent);

    }

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

        new GetNoticeList().execute();

        if((vector.size() % 10) != 0){
            //Snackbar.make(getView(), "페이지의 끝입니다.", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
            mListView.setOnScrollListener(null);
        }

    }

    //ListView Adapter
    public class ListViewAdapter extends BaseAdapter {
        private Context mContext = null;
        // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
        ArrayList<Notice> datas = new ArrayList();

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
                convertView = inflater.inflate(R.layout.listitem_notice, null);

                holder.noticeImage = (ImageView) convertView.findViewById(R.id.iv_notice_image); //프로필이미지
                holder.type = (TextView) convertView.findViewById(R.id.tv_notice_type); //타입
                holder.date = (TextView) convertView.findViewById(R.id.tv_notice_date); //날짜

                convertView.setTag(holder);
            }else{
                holder = (ViewHolder) convertView.getTag();
            }

            Notice data = datas.get(position);

            //알림 이미지 및 문구 셋팅
            if(datas.get(position).getLogCategory() == 1) { //UP
                holder.noticeImage.setBackground(getResources().getDrawable(R.drawable.icon_notice_up));
                holder.type.setText("회원님의 게시글을 누군가 좋아합니다.");
            }else if(datas.get(position).getLogCategory() == 2){ //DOWN
                holder.noticeImage.setBackground(getResources().getDrawable(R.drawable.icon_notice_down));
                holder.type.setText("회원님의 게시글을 누군가 싫어합니다.");
            }else if(datas.get(position).getLogCategory() == 3){ //COMMENT
                holder.noticeImage.setBackground(getResources().getDrawable(R.drawable.icon_notice_comment));
                holder.type.setText("회원님의 게시글에 댓글이 달렸습니다.");
            }else if(datas.get(position).getLogCategory() == 4){ // DELETE
                holder.noticeImage.setBackground(getResources().getDrawable(R.drawable.icon_notice_delete));
                holder.type.setText("회원님의 게시글이 -5가 되어 삭제되었습니다.");
            }else if(datas.get(position).getLogCategory() == 5){ // DEEP COMMENT
                holder.noticeImage.setBackground(getResources().getDrawable(R.drawable.icon_notice_comment));
                holder.type.setText("회원님의 댓글에 댓글이 달렸습니다.");
            }

            holder.date.setText(data.logDate.toString());

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
        public void addItem(NoticeListModel noticeListModel) {
            Notice addInfo = null;
            addInfo = new Notice();

            addInfo.noticeLogId = noticeListModel.getNoticeLogId();
            addInfo.logDate = noticeListModel.getLogDate();
            addInfo.logCategory = noticeListModel.getLogCategory();
            addInfo.isRead = noticeListModel.isRead();
            addInfo.logImage = ResourcesCompat.getDrawable(getResources(), R.drawable.user, null);

            datas.add(addInfo);
        }
    }//Adapter class ();;

    private class ViewHolder {
        public ImageView noticeImage; //프로필 이미지
        public TextView type; //타입
        public TextView date; //날짜
    }//view holder class ();;

    private class GetNoticeList extends AsyncTask<Boolean, Void, Void> {

        ProgressDialog progressDialog;

        @Override
        protected Void doInBackground(Boolean... params) {
            try
            {
                //Getting data from server
                vector = noticeRepository.getNoticeList(getUserId(), pageNumber);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progressDialog.dismiss();

//            mLockListView = false;
//
//            for(int i=(pageNumber-1)*10; i<vector.size(); i++){
//                mAdapter.addItem(vector.get(i));
//            }
//
//            mAdapter.notifyDataSetChanged();
//
//            if(pageNumber == 1 && vector.size() == 0){
//                noDateLayout.setVisibility(View.VISIBLE);
//            }

            if(isFirstRoof){ //첫루프
                if(pageNumber == 1 && vector.size() == 0){ //알림이 하나도없는경우
                    noDateLayout.setVisibility(View.VISIBLE);
                }else{
                    nowVectorSize = vector.size();
                    //어뎁터에 벡터 데이터 추가
                    for(int i=(pageNumber-1)*10; i<vector.size(); i++){
                        mAdapter.addItem(vector.get(i));
                    }
                    isFirstRoof = false;
                }
            }else{
                preVectorSize = nowVectorSize;
                nowVectorSize = vector.size();

                if(preVectorSize == nowVectorSize){ //이전벡터와 루프를 실행한 벡터 사이즈가 같으면 더이상 불러올 리스트가없는것임.
                    //Snackbar.make(getView(), "페이지의 끝입니다.", Snackbar.LENGTH_SHORT).setAction("Action", null).show();
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
    }//GetNoticeList Class();;

}
