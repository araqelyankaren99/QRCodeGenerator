package am.lavr.admin.qrcodebarcodescannerandgenerator.fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.util.regex.Matcher;

import am.lavr.admin.qrcodebarcodescannerandgenerator.R;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class URLFragment extends BaseFragment implements View.OnClickListener {

    private View view;
    private EditText edtURL;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_url, container, false);
        findViews(view);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_download:
            case R.id.btn_create:
                if (isUrl(edtURL.getText().toString())) {
                    strQR = urlToString();
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
        edtURL = (EditText) view.findViewById(R.id.edt_url_url);
    }

    private boolean isUrl(String string) {
        Matcher m = Patterns.WEB_URL.matcher(string);
        if (m.find() && m.group().length() == string.length()){
            return true;
        }
        edtURL.setError(getResources().getString(R.string.error_url));
        return false;
    }

    private String urlToString() {
        String string = edtURL.getText().toString();
        if(string.indexOf(getString(R.string.hint_url)) != 0) {
            string = getString(R.string.hint_url) + string;
        }
        return string;
    }
}
