package am.lavr.admin.qrcodebarcodescannerandgenerator.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Hashtable;

import am.lavr.admin.qrcodebarcodescannerandgenerator.R;
import am.lavr.admin.qrcodebarcodescannerandgenerator.adapter.ImageAdapter;
import dmax.dialog.SpotsDialog;
import yuku.ambilwarna.AmbilWarnaDialog;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

abstract class BaseFragment extends Fragment implements View.OnClickListener {

    private static final int REQUEST_CODE_GALLERY = 100;
    private static final int REQUEST_CODE_PERMISSION = 100;

    private LinearLayout llEnterContent;
    protected ImageView ivQR;
    private Button btnCreate;
    private Button btnDownload;
    protected FrameLayout flEnterContent;
    private SeekBar sbSize;
    private TextView tvSize;
    private LinearLayout llSetColorsText;
    private RadioButton rbForeground;
    private RadioButton rbGradient;
    private TextView tvForegroundHex;
    private TextView tvGradientHex;
    private ImageView ivForeground;
    private ImageView ivGradient;
    private LinearLayout llSetColors;
    private LinearLayout llForeground;
    private LinearLayout llGradient;
    private ImageView ivEnterContentAdd;
    private ImageView ivSetColorsAdd;
    private LinearLayout llAddLogo;
    private LinearLayout llUpload;
    private ImageView ivAddLogoAdd;
    private ImageView ivUploadImage;
    private Button btnUploadImage;
    private Button btnRemoveLogo;
    private GridView gridView;
    protected Bitmap bitmap;
    private Bitmap logo;
    private Context context;
    protected String strQR;
    private AlertDialog alertDialog;
    private int width;
    private int height;
    private int color1;
    private int color2;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_base, container, false);
        context = view.getContext();
        findViews(view);
        setListeners();
        init();
        addContentView();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        flEnterContent.removeAllViews();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_download:
                alertDialog.show();
                (new MyAsyncTask()).execute(true);
//                onClick(btnCreate);
//                checkPermission();
                break;
            case R.id.btn_create:
                alertDialog.show();
                (new MyAsyncTask()).execute(false);
//                createQr();
//                ivQR.setImageBitmap(bitmap);
                break;
            case R.id.ll_enter_content:
                switch (flEnterContent.getVisibility()) {
                    case GONE:
                        if(llSetColors.getVisibility() == VISIBLE) {
                            llSetColors.setVisibility(GONE);
                            ivSetColorsAdd.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.anim_close) );
                        }
                        if(gridView.getVisibility() == VISIBLE) {
                            ivAddLogoAdd.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.anim_close) );
                            gridView.setVisibility(GONE);
                            llUpload.setVisibility(GONE);
                        }
                        ivEnterContentAdd.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.anim_open) );
                        flEnterContent.setVisibility(View.VISIBLE);
                        break;
                    case VISIBLE:
                        flEnterContent.setVisibility(GONE);
                        ivEnterContentAdd.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.anim_close) );
                        hideKeyboard(getActivity());
                        break;
                }
                break;
            case R.id.ll_set_color:
                switch (llSetColors.getVisibility()) {
                    case GONE:
                        if(flEnterContent.getVisibility() == VISIBLE) {
                            ivEnterContentAdd.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.anim_close) );
                            flEnterContent.setVisibility(GONE);
                            hideKeyboard(getActivity());
                        }
                        if(gridView.getVisibility() == VISIBLE) {
                            ivAddLogoAdd.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.anim_close) );
                            gridView.setVisibility(GONE);
                            llUpload.setVisibility(GONE);
                        }
                        ivSetColorsAdd.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.anim_open) );
                        llSetColors.setVisibility(View.VISIBLE);
                        break;
                    case VISIBLE:
                        ivSetColorsAdd.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.anim_close) );
                        llSetColors.setVisibility(GONE);
                        break;
                }
                break;
            case R.id.ll_foreground:
                openColorPicker(ivForeground, tvForegroundHex);
                break;
            case R.id.ll_gradient:
                openColorPicker(ivGradient, tvGradientHex);
                break;
            case R.id.ll_add_logo_image:
                switch (gridView.getVisibility()) {
                    case GONE:

                        if(flEnterContent.getVisibility() == VISIBLE) {
                            flEnterContent.setVisibility(GONE);
                            ivEnterContentAdd.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.anim_close) );
                            hideKeyboard(getActivity());
                        }
                        if(llSetColors.getVisibility() == VISIBLE) {
                            ivSetColorsAdd.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.anim_close) );
                            llSetColors.setVisibility(GONE);
                        }
                        ivAddLogoAdd.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.anim_open) );
                        gridView.setVisibility(View.VISIBLE);
                        llUpload.setVisibility(VISIBLE);
                        break;
                    case VISIBLE:
                        ivAddLogoAdd.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.anim_close) );
                        gridView.setVisibility(GONE);
                        llUpload.setVisibility(GONE);
                        break;
                }
                break;
            case R.id.btn_upload:
                startActivityForResult(new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI), REQUEST_CODE_GALLERY);
                break;
            case R.id.btn_remove_logo:
                ivUploadImage.setImageBitmap(((BitmapDrawable) getResources().getDrawable(R.drawable.no_logo_image, null)).getBitmap());
                logo = null;
                btnRemoveLogo.setVisibility(GONE);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == REQUEST_CODE_GALLERY && resultCode == Activity.RESULT_OK) {
            Uri selectedImage = data.getData();
            try {
                logo = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImage);
                ivUploadImage.setImageBitmap(logo);
                btnRemoveLogo.setVisibility(VISIBLE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_PERMISSION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    downLoad();
                }
            }

        }
    }

    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_CODE_PERMISSION
            );
        } else {
            downLoad();
        }
    }

    private void setListeners() {
        llEnterContent.setOnClickListener(this);
        btnCreate.setOnClickListener(this);
        btnDownload.setOnClickListener(this);
        llSetColorsText.setOnClickListener(this);
        llSetColors.setOnClickListener(this);
        llForeground.setOnClickListener(this);
        llGradient.setOnClickListener(this);
        llAddLogo.setOnClickListener(this);
        btnUploadImage.setOnClickListener(this);
        btnRemoveLogo.setOnClickListener(this);
        sbSize.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvSize.setText((progress + 200) + "x" + (progress + 200) + " Px");
                width = progress + 200;
                height = progress + 200;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        rbGradient.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    llGradient.setVisibility(VISIBLE);
                } else {
                    llGradient.setVisibility(GONE);
                }
            }
        });

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                logo = BitmapFactory.decodeResource(getResources(), ImageAdapter.mThumbIds[position]);
                ivUploadImage.setImageBitmap(logo);
                btnRemoveLogo.setVisibility(VISIBLE);
            }
        });
    }

    private void findViews(View view) {
        llEnterContent = (LinearLayout) view.findViewById(R.id.ll_enter_content);
        flEnterContent = (FrameLayout) view.findViewById(R.id.fl_enter_content);
        btnCreate = (Button)  view.findViewById(R.id.btn_create);
        btnDownload = (Button) view.findViewById(R.id.btn_download);
        ivQR = (ImageView) view.findViewById(R.id.iv_qr);
        sbSize = (SeekBar) view.findViewById(R.id.sb_base_quality);
        tvSize = (TextView) view.findViewById(R.id.tv_base_size);
        llSetColorsText = (LinearLayout) view.findViewById(R.id.ll_set_color);
        rbForeground = (RadioButton) view.findViewById(R.id.rb_single_color);
        rbGradient = (RadioButton) view.findViewById(R.id.rb_color_gradient);
        tvForegroundHex = (TextView) view.findViewById(R.id.tv_foreground_hex);
        tvGradientHex = (TextView) view.findViewById(R.id.tv_gradient_hex);
        ivForeground = (ImageView) view.findViewById(R.id.iv_foreground_color);
        ivGradient = (ImageView) view.findViewById(R.id.iv_gradient_color);
        llSetColors = (LinearLayout) view.findViewById(R.id.ll_set_colors);
        llForeground = (LinearLayout) view.findViewById(R.id.ll_foreground);
        llGradient = (LinearLayout) view.findViewById(R.id.ll_gradient);
        ivEnterContentAdd = (ImageView) view.findViewById(R.id.iv_enter_content_add);
        ivSetColorsAdd = (ImageView) view.findViewById(R.id.iv_set_colors_add);
        llAddLogo = (LinearLayout) view.findViewById(R.id.ll_add_logo_image);
        ivAddLogoAdd = (ImageView) view.findViewById(R.id.iv_add_logo_add);
        llUpload = (LinearLayout) view.findViewById(R.id.ll_upload);
        ivUploadImage = (ImageView) view.findViewById(R.id.iv_upload_logo);
        btnUploadImage = (Button) view.findViewById(R.id.btn_upload);
        btnRemoveLogo = (Button) view.findViewById(R.id.btn_remove_logo);
        gridView = (GridView) view.findViewById(R.id.gridview);
    }

    private void init() {
        alertDialog = new SpotsDialog(context, "Generating");
        bitmap = null;
        logo = null;
        try {
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            bitmap = barcodeEncoder.encodeBitmap(getResources().getString(R.string.default_url), BarcodeFormat.QR_CODE, 500, 500);
        } catch(Exception e) {
            e.printStackTrace();
        }
        sbSize.setProgress(800);
        width = sbSize.getProgress() + 200;
        height = sbSize.getProgress() + 200;
        tvForegroundHex.setText(getString(R.string.color_black));
        tvGradientHex.setText(getString(R.string.color_black));
        btnRemoveLogo.setVisibility(GONE);
        gridView.setAdapter(new ImageAdapter(context));
        onClick(llEnterContent);
    }

    private void openColorPicker(final ImageView imageView, final TextView textView) {
        int currentColor = ((ColorDrawable) imageView.getBackground()).getColor();
        AmbilWarnaDialog dialog = new AmbilWarnaDialog(context, currentColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
            @Override
            public void onOk(AmbilWarnaDialog dialog, int color) {
                imageView.setBackgroundColor(color);
                textView.setText(String.format("#%06X", (0xFFFFFF & color)));
            }

            @Override
            public void onCancel(AmbilWarnaDialog dialog) {
//                Toast.makeText(getApplicationContext(), "Action canceled!", Toast.LENGTH_SHORT).show();
            }
        });
        dialog.show();
    }

    public void createImageBitmap(Bitmap originalBitmap) {
        int width = originalBitmap.getWidth();
        int height = originalBitmap.getHeight();
        Bitmap updatedBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(updatedBitmap);
        canvas.drawBitmap(originalBitmap, 0, 0, null);

        int color1 = ((ColorDrawable) ivForeground.getBackground()).getColor();
        int color2;
        if (rbGradient.isChecked()) {
            color2 = ((ColorDrawable) ivGradient.getBackground()).getColor();
        } else color2 = ((ColorDrawable) ivForeground.getBackground()).getColor();

        Paint paint = new Paint();
        LinearGradient shader = new LinearGradient(0, 0, 0, height, color1, color2, Shader.TileMode.CLAMP);
        paint.setShader(shader);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawRect(0, 0, width, height, paint);

        for (int x = 0; x < width; x++){
            for (int y = 0; y < height; y++){
                updatedBitmap.setPixel(x, y, originalBitmap.getPixel(x, y) != Color.WHITE ? updatedBitmap.getPixel(x,y): Color.WHITE);
            }
        }

        if(logo != null) {
            updatedBitmap = mergeBitmaps(logo, updatedBitmap);
        }
        bitmap = updatedBitmap;
    }

    protected void createQr() {
        try {
            Hashtable hints = new Hashtable();
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            bitmap = barcodeEncoder.encodeBitmap(strQR, BarcodeFormat.QR_CODE, 500, 500, hints);
            createImageBitmap(bitmap);
//            Toast.makeText(context, getString(R.string.toast_qr_created), Toast.LENGTH_SHORT).show();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void downLoad() {
        final String IMAGE_DIRECTORY = "/QRCode";
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        Bitmap resizedBitmap = bitmap;
        resizedBitmap = Bitmap.createScaledBitmap(resizedBitmap, width, height, false);
        resizedBitmap.compress(Bitmap.CompressFormat.PNG, 90, bytes);
        File wallpaperDirectory = new File(Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);

        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs();
        }

        try {
            File f = new File(wallpaperDirectory, Calendar.getInstance()
                    .getTimeInMillis() + ".png");

            if (!f.exists()) {
                f.createNewFile();
            }

            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(context,
                    new String[]{f.getPath()},
                    new String[]{"image/png"}, null);
            fo.close();
            Toast.makeText(context, getString(R.string.toast_image_saved), Toast.LENGTH_SHORT).show();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }

    protected abstract int getString(int toast_image_saved);

    public Bitmap mergeBitmaps(Bitmap logo, Bitmap qrcode) {

        Bitmap combined = Bitmap.createBitmap(qrcode.getWidth(), qrcode.getHeight(), qrcode.getConfig());
        Canvas canvas = new Canvas(combined);
        int canvasWidth = canvas.getWidth();
        int canvasHeight = canvas.getHeight();
        canvas.drawBitmap(qrcode, new Matrix(), null);

        Bitmap resizeLogo = Bitmap.createScaledBitmap(logo, canvasWidth / 7, canvasHeight / 7, false);
        int centreX = (canvasWidth - resizeLogo.getWidth()) /2;
        int centreY = (canvasHeight - resizeLogo.getHeight()) / 2;
        canvas.drawBitmap(resizeLogo, centreX, centreY, null);
        return combined;
    }

    private void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    abstract void addContentView();

    private class MyAsyncTask extends AsyncTask<Boolean, Void, Void> {
        private boolean download;

        @Override
        protected Void doInBackground(Boolean... booleans) {
            createQr();
            download = booleans[0];
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            ivQR.setImageBitmap(bitmap);
            if(download) {
                checkPermission();
            } else {
                Toast.makeText(context, getString(R.string.toast_qr_created), Toast.LENGTH_SHORT).show();
            }
            alertDialog.dismiss();
        }
    }
}
