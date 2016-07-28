package com.kidueck.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.kidueck.Concrete.MemberRepository;
import com.kidueck.Model.JoinModel;
import com.kidueck.R;

/**
 * Created by system777 on 2016-07-05.
 */
public class JoinActivity extends Activity implements View.OnClickListener{

    DatePicker dpBirth;
    EditText etId, etPw, etPw2, etRoot;
    RadioButton rbMale,rbFemale;
    Button btJoin;

    MemberRepository memberRepository = new MemberRepository();
    JoinModel joinModel = new JoinModel();
    String submitResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join);

        init();
    }

    private void init(){
        dpBirth = (DatePicker) findViewById(R.id.dp_join_birth);
        etId = (EditText) findViewById(R.id.et_join_id);
        etPw = (EditText) findViewById(R.id.et_join_pw);
        etPw2 = (EditText) findViewById(R.id.et_join_pw2);
        etRoot = (EditText) findViewById(R.id.et_join_root);
        rbMale = (RadioButton) findViewById(R.id.rb_join_male);
        rbFemale = (RadioButton) findViewById(R.id.rb_join_female);
        btJoin = (Button) findViewById(R.id.bt_join_join);
        btJoin.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.bt_join_join:
                if(isInValid()){
                    new SubmitJoin().execute();
                }
                break;
        }
    }


    //유효성 검사
    public boolean isInValid(){

        //아이디
        if(etId.getText().toString().trim().length() < 4){
            Toast.makeText(getApplicationContext(),"ID는 4자이상입니다.", Toast.LENGTH_SHORT).show();
            return false;
        }

        //비밀번호(4자이상)
        if((etPw.getText().toString().trim().length() < 4)){
            Toast.makeText(getApplicationContext(),"비밀번호는 4자 이상이어야 합니다.", Toast.LENGTH_SHORT).show();
            return false;
        }

        //비밀번호 확인
        if(!etPw.getText().toString().trim().equals(etPw2.getText().toString().trim())){
            Toast.makeText(getApplicationContext(),"입력한 비밀번호와 비밀번호 확인값이 다릅니다.", Toast.LENGTH_SHORT).show();
            return false;
        }


        //유입경로
        if(etRoot.getText().toString().trim().length() == 0){
            Toast.makeText(getApplicationContext(),"유입경로를 입력해주세요.", Toast.LENGTH_SHORT).show();
            return false;
        }

        //성별(둘다 체크 안된경우)
        if((rbMale.isChecked() ==false && rbFemale.isChecked() == false)){
            Toast.makeText(getApplicationContext(),"성별을 선택해주세요.", Toast.LENGTH_SHORT).show();
            return false;
        }else if(rbMale.isChecked()){
            joinModel.setSex("1");
        }else if(rbFemale.isChecked()){
            joinModel.setSex("2");
        }


        String birth = String.format("%d-%d-%d", dpBirth.getYear(), dpBirth.getMonth()+1, dpBirth.getDayOfMonth());
        joinModel.setId(etId.getText().toString().trim());
        joinModel.setPassword(etPw.getText().toString().trim());
        joinModel.setJoinRoot(etRoot.getText().toString().trim());
        joinModel.setBirth(birth);

        return true;
    }

    private class SubmitJoin extends AsyncTask<Boolean, Void, Void> {

        ProgressDialog progressDialog;

        @Override
        protected Void doInBackground(Boolean... params) {
            try
            {
                //서버에서 받아온 결과스트링(success, id중복 등)
                submitResult = memberRepository.join(joinModel);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            return null;

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progressDialog.dismiss();
            if(submitResult.equals("success")){
                Toast.makeText(getApplicationContext(), "가입이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                finish();
            }else{
                Toast.makeText(getApplicationContext(), "중복된 ID가 존재합니다.", Toast.LENGTH_SHORT).show();
            }

        }

        @Override
        protected void onPreExecute() {
            progressDialog = new ProgressDialog(JoinActivity.this);
            progressDialog.setMessage("가입처리중..");
            progressDialog.setCancelable(true);
            progressDialog.show();
        }

    }//Submit Class();;

}
