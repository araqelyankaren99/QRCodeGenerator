package am.lavr.admin.qrcodebarcodescannerandgenerator.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.zxing.BarcodeFormat;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import am.lavr.admin.qrcodebarcodescannerandgenerator.R;

public class YouTubeFragment extends BaseFragment implements View.OnClickListener {

    private View view;
    private EditText edtYouTubeURL;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_youtube, container, false);
        findViews(view);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_download:
            case R.id.btn_create:
                if (isYouTubeURL(edtYouTubeURL.getText().toString())) {
                    strQR = edtYouTubeURL.getText().toString();
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
        edtYouTubeURL = (EditText) view.findViewById(R.id.edt_youtube_url);
    }

    private Boolean isYouTubeURL(String string) {
        if(string.indexOf(getString(R.string.hint_youtube)) == 0) {
            return true;
        }
        edtYouTubeURL.setError(getResources().getString(R.string.error_youtube));
        return false;
    }
}
