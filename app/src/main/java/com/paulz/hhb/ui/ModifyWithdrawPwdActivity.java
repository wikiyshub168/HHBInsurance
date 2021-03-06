package com.paulz.hhb.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.core.framework.net.NetworkWorker;
import com.core.framework.util.DESUtil;
import com.core.framework.util.DialogUtil;
import com.core.framework.util.StringUtil;
import com.paulz.hhb.R;
import com.paulz.hhb.base.BaseActivity;
import com.paulz.hhb.common.APIUtil;
import com.paulz.hhb.common.AppUrls;
import com.paulz.hhb.httputil.HttpRequester;
import com.paulz.hhb.httputil.ParamBuilder;
import com.paulz.hhb.parser.gson.BaseObject;
import com.paulz.hhb.parser.gson.GsonParser;
import com.paulz.hhb.utils.AppUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by pualbeben on 17/5/21.
 * 修改提现密码
 */

public class ModifyWithdrawPwdActivity extends BaseActivity {


    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.et_captcha)
    EditText etCaptcha;
    @BindView(R.id.btn_get_captcha)
    TextView btnGetCaptcha;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_id)
    EditText etId;
    @BindView(R.id.et_new_pwd)
    EditText etNewPwd;
    @BindView(R.id.et_new_pwd_again)
    EditText etNewPwdAgain;
    @BindView(R.id.btn_save)
    TextView btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    private void initView() {
        setActiviyContextView(R.layout.activity_modify_withdraw_pwd, false, true);
        ButterKnife.bind(this);
        setTitleText("", "修改提现密码", 0, true);
        etPhone.setText(getIntent().getStringExtra("extra_tel"));
    }


    public static void invoke(Context context,String tel) {
        Intent intent = new Intent(context, ModifyWithdrawPwdActivity.class);
        intent.putExtra("extra_tel",tel);
        context.startActivity(intent);
    }


    @OnClick({R.id.btn_get_captcha, R.id.btn_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_get_captcha:
                getCaptcha();
                break;
            case R.id.btn_save:
                submit();
                break;
        }
    }



    private void submit(){
        String code=etCaptcha.getText().toString();
        if(code.length()==0){
            AppUtil.showToast(getApplicationContext(),"请输入验证码");
            return;
        }

        String name=etName.getText().toString();
        if(name.length()==0){
            AppUtil.showToast(getApplicationContext(),"请输入姓名");
            return;
        }

        if(!StringUtil.isMatchingChiness(name)){
            AppUtil.showToast(getApplicationContext(),"请输入中文姓名");
            return;
        }

        String sfz=etId.getText().toString();
        if(sfz.length()==0){
            AppUtil.showToast(getApplicationContext(),"请输入身份证");
            return;
        }
        if(!StringUtil.isMatchingPersonId(sfz)){
            AppUtil.showToast(getApplicationContext(),"身份证格式错误");
            return;
        }

        String pwd=etNewPwd.getText().toString();
        if(pwd.length()==0){
            AppUtil.showToast(getApplicationContext(),"请设置密码");
            return;
        }
        if(pwd.length()<6){
            AppUtil.showToast(getApplicationContext(),"密码长度不能小于6位");
            return;
        }
        String pwd2=etNewPwdAgain.getText().toString();
        if(!pwd2.equals(pwd)){
            AppUtil.showToast(getApplicationContext(),"两次输入密码不一致");
            return;
        }
        DialogUtil.showDialog(lodDialog);
        ParamBuilder params = new ParamBuilder();
        HttpRequester requester=new HttpRequester();
        requester.getParams().put("vkey",code);
        requester.getParams().put("password",pwd);
        requester.getParams().put("sfz",sfz);
        requester.getParams().put("name",name);
        NetworkWorker.getInstance().post(APIUtil.parseGetUrlHasMethod(params.getParamList(), AppUrls.getInstance().URL_MODIFY_WITHDRAW_PWD), new NetworkWorker.ICallback() {
            @Override
            public void onResponse(int status, String result) {
                if (!isFinishing()) DialogUtil.dismissDialog(lodDialog);
                if (status == 200) {
                    BaseObject<Object> object = GsonParser.getInstance().parseToObj(result, Object.class);
                    if (object != null && object.status == BaseObject.STATUS_OK ) {
                        AppUtil.showToast(getApplicationContext(), "设置成功");
                        finish();

                    } else {
                        AppUtil.showToast(getApplicationContext(), object!=null&&!AppUtil.isNull(object.msg)?object.msg:"设置失败");
                    }
                }else {
                    AppUtil.showToast(getApplicationContext(), "设置失败");
                }
            }
        },requester, DESUtil.SECRET_DES);


    }


    private void getCaptcha(){
        String tel=etPhone.getText().toString();
        if(tel.length()==0){
            AppUtil.showToast(getApplicationContext(),"未绑定手机，不能修改");
            return;
        }

        DialogUtil.showDialog(lodDialog);
        ParamBuilder params = new ParamBuilder();
        HttpRequester requester=new HttpRequester();
        requester.getParams().put("telephone",tel);
        NetworkWorker.getInstance().post(APIUtil.parseGetUrlHasMethod(params.getParamList(), AppUrls.getInstance().URL_GET_CAPTCHA_WITHDRAW), new NetworkWorker.ICallback() {
            @Override
            public void onResponse(int status, String result) {
                if (!isFinishing()) DialogUtil.dismissDialog(lodDialog);
                if (status == 200) {
                    BaseObject<Object> object = GsonParser.getInstance().parseToObj(result, Object.class);
                    if (object != null && object.status == BaseObject.STATUS_OK ) {
                        countDown();

                    } else {

                        AppUtil.showToast(getApplicationContext(), "获取失败");
                    }
                }
            }
        },requester, DESUtil.SECRET_DES);


    }


    CountDownTimer timer;

    private void countDown(){
        btnGetCaptcha.setText("60s");
        btnGetCaptcha.setEnabled(false);
        timer=new CountDownTimer(60*1000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long time=millisUntilFinished/1000;
                btnGetCaptcha.setText(time+"s");
            }

            @Override
            public void onFinish() {
                btnGetCaptcha.setEnabled(true);
                btnGetCaptcha.setText("重新获取");
            }
        }.start();
    }
}
