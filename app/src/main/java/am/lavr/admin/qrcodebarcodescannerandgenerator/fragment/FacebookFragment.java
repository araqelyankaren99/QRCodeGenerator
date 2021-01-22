package am.lavr.admin.qrcodebarcodescannerandgenerator.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;

import com.google.zxing.BarcodeFormat;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import am.lavr.admin.qrcodebarcodescannerandgenerator.R;

public class FacebookFragment extends BaseFragment implements View.OnClickListener {

    private View view;
    private RadioButton rbFb;
    private EditText edtFaceBookURL;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_facebook, container, false);
        findViews(view);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_download:
            case R.id.btn_create:
                if (isFacebookURL(edtFaceBookURL.getText().toString())) {
                    strQR = fbURLToString();
                    super.onClick(v);
                }
                break;
            default:
                super.onClick(v);
        }
    }

    @Override
    void addContentView() {
        flEnterContent.removeAllViews();
        flEnterContent.addView(view);
    }

    private void findViews(View view) {
        rbFb = (RadioButton) view.findViewById(R.id.rb_facebook_fb);
        edtFaceBookURL = (EditText) view.findViewById(R.id.edt_facebook_url);
    }

    private Boolean isFacebookURL(String string) {
        if(string.indexOf(getString(R.string.hint_facebook)) == 0) {
            return true;
        }
        edtFaceBookURL.setError(getResources().getString(R.string.error_facebook));
        return false;
    }

    private String fbURLToString() {
        StringBuilder builder = new StringBuilder();
        if(rbFb.isChecked()) {
            builder.append(edtFaceBookURL.getText().toString());
        } else {
            builder.append(getString(R.string.hint_facebook))
                    .append(getString(R.string.facebook_share));
            int index = getString(R.string.hint_url).length() + 1;
            if(edtFaceBookURL.getText().toString().length() <= index + 1)
                builder.append("%2F")
                    .append(edtFaceBookURL.getText().toString().substring(index));
        }
        return builder.toString();
    }
}
