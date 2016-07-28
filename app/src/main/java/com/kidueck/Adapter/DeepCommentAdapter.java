package com.kidueck.Adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.kidueck.ListData.Comment;
import com.kidueck.R;

import java.util.ArrayList;

/**
 * Created by system777 on 2016-07-22.
 */
public class DeepCommentAdapter extends ArrayAdapter<Comment> {

    private final Activity context;
    private final ArrayList<Comment> commentList;

    public DeepCommentAdapter(Activity context,
                      ArrayList<Comment> commentList) {
        super(context, R.layout.listitem_deep_comment, commentList);
        this.context = context;
        this.commentList = commentList;
    }


    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.list_single, null, true);

        TextView comment = (TextView) rowView.findViewById(R.id.test_comment);
        TextView date = (TextView) rowView.findViewById(R.id.test_date);
        //ImageView isCommenterIcon = (ImageView) rowView.findViewById(R.id.ib_detail_writer);

        comment.setText(commentList.get(position).getCommentContent());
        date.setText(commentList.get(position).getWrittenDate());


        return rowView;
    }



//    Context mContext;
//
//    public DeepCommentAdapter(Context context, int textViewResourceId) {
//        super(context, textViewResourceId);
//    }
//
//    public DeepCommentAdapter(Context context, int resource, ArrayList<Comment> items) {
//        super(context, resource, items);
//        mContext = context;
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//
//        View view = convertView;
//
//        if (view == null) {
//            LayoutInflater vi;
//            vi = LayoutInflater.from(mContext);
//            view = vi.inflate(R.layout.listitem_deep_comment, null);
//        }
//
//        Comment commentItem = getItem(position);
//
//        if (commentItem != null) {
//            TextView comment = (TextView) convertView.findViewById(R.id.tv_deep_content);
//            TextView date = (TextView) convertView.findViewById(R.id.tv_deep_date);
//            ImageView isCommenterIcon = (ImageView) convertView.findViewById(R.id.ib_detail_writer);
//
//            if (comment != null) {
//                comment.setText(commentItem.getCommentContent());
//            }
//
//            if (date != null) {
//                date.setText(commentItem.getWrittenDate());
//            }
//
//            if (isCommenterIcon != null) {
//                isCommenterIcon.setImageDrawable(view.getResources().getDrawable(R.drawable.icon_delete));
//            }
//        }
//
//        return view;
//    }

}
