package com.kidueck.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.kidueck.Adapter.DeepCommentAdapter;
import com.kidueck.ListData.Comment;
import com.kidueck.R;

import java.util.ArrayList;

/**
 * Created by system777 on 2016-07-22.
 */
public class DeepCommentActivity extends Activity implements View.OnClickListener{

    ArrayList<Comment> list = new ArrayList<Comment>();

    private ListView m_ListView;
    private DeepCommentAdapter m_Adapter;

    //헤더 관련
    public View header;
    public ImageButton ib_back, ib_submit;
    public TextView tv_comment_content, tv_write_date;
    public EditText et_comment_content;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deep_comment);

        // 커스텀 어댑터 생성
        m_Adapter = new DeepCommentAdapter();

        // Xml에서 추가한 ListView 연결
        m_ListView = (ListView) findViewById(R.id.lv_deep_comment);

        //헤더추가
        header = getLayoutInflater().inflate(R.layout.header_deep_comment, null, false) ;
        m_ListView.addHeaderView(header);

        //헤더뷰 안의 컴포넌트 초기화 및 이벤트
        initHeaderComponent();

        // ListView에 어댑터 연결
        m_ListView.setAdapter(m_Adapter);

        // ListView에 아이템 추가
        m_Adapter.add("하스스톤");
        m_Adapter.add("몬스터 헌터");
        m_Adapter.add("디아블로");
        m_Adapter.add("와우");
        m_Adapter.add("하스스톤");
        m_Adapter.add("몬스터 헌터");
        m_Adapter.add("디아블로");
        m_Adapter.add("와우");
        m_Adapter.add("하스스톤");
        m_Adapter.add("몬스터 헌터");
        m_Adapter.add("디아블로");
        m_Adapter.add("와우");

    }

    public void initHeaderComponent(){
        ib_back = (ImageButton) header.findViewById(R.id.ib_deep_back);
        ib_submit = (ImageButton) header.findViewById(R.id.ib_deep_comment);
        tv_comment_content = (TextView) header.findViewById(R.id.tv_deep_content);
        tv_write_date = (TextView) header.findViewById(R.id.tv_deep_date);
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
}
