package com.kidueck.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kidueck.Model.DeepCommentListModel;
import com.kidueck.R;

import java.util.Vector;

/**
 * Created by system777 on 2016-07-22.
 */
public class DeepCommentAdapter extends BaseAdapter {

    private Vector<DeepCommentListModel> deepCommentList;
    TextView deepCommentContent;
    TextView deepCommentDate;
    ImageView isCommenter;

    // 생성자
    public DeepCommentAdapter(Vector<DeepCommentListModel> data) {
        deepCommentList = data;
    }

    public void updateList(Vector<DeepCommentListModel> data) {
        deepCommentList = null;
        deepCommentList = data;
        notifyDataSetChanged();
    }


    // 현재 아이템의 수를 리턴
    @Override
    public int getCount() {
        return deepCommentList.size();
    }

    // 현재 아이템의 오브젝트를 리턴, Object를 상황에 맞게 변경하거나 리턴받은 오브젝트를 캐스팅해서 사용
    @Override
    public Object getItem(int position) {
        return deepCommentList.get(position);
    }

    // 아이템 position의 ID 값 리턴
    @Override
    public long getItemId(int position) {
        return position;
    }

    // 출력 될 아이템 관리
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();
        CustomHolder    holder  = null;

        // 리스트가 길어지면서 현재 화면에 보이지 않는 아이템은 converView가 null인 상태로 들어 옴
        if ( convertView == null ) {
            // view가 null일 경우 커스텀 레이아웃을 얻어 옴
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listitem_deep_comment, parent, false);

            deepCommentContent = (TextView) convertView.findViewById(R.id.tv_deep_list_content);
            deepCommentDate = (TextView) convertView.findViewById(R.id.tv_deep_list_date);
            isCommenter = (ImageView) convertView.findViewById(R.id.ib_deep_list_delete);

            // 홀더 생성 및 Tag로 등록
            holder = new CustomHolder();
            holder.deepCommentContent  = deepCommentContent;
            holder.deepCommentDate  = deepCommentDate;
            holder.isCommenter = isCommenter;
            convertView.setTag(holder);

        }else{
            holder  = (CustomHolder) convertView.getTag();
            deepCommentContent    = holder.deepCommentContent;
            deepCommentDate     = holder.deepCommentDate;
            isCommenter = holder.isCommenter;
        }

        deepCommentContent.setText(deepCommentList.get(position).getContent());
        deepCommentDate.setText(deepCommentList.get(position).getWriteDate());

        isCommenter.setVisibility(View.GONE);
        if(deepCommentList.get(position).isDeepCommenter()){
            isCommenter.setVisibility(View.VISIBLE);
        }

        return convertView;
    }

    // 외부에서 아이템 추가 요청 시 사용
    public void addItem(DeepCommentListModel model){
        deepCommentList.add(model);
    }


    // 외부에서 아이템 삭제 요청 시 사용
    public void remove(int _position) {
        deepCommentList.remove(_position);
    }

    private class CustomHolder {
        TextView  deepCommentContent;
        TextView deepCommentDate;
        ImageView isCommenter;
    }
}