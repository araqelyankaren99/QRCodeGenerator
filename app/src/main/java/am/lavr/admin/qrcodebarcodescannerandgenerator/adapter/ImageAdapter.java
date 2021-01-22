package am.lavr.admin.qrcodebarcodescannerandgenerator.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import am.lavr.admin.qrcodebarcodescannerandgenerator.R;

public class ImageAdapter extends BaseAdapter {
    private Context mContext;

    public ImageAdapter(Context c) {
        mContext = c;
    }

    public int getCount() {
        return mThumbIds.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            // if it's not recycled, initialize some attributes
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(100, 100));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }

        imageView.setImageResource(mThumbIds[position]);
        return imageView;
    }

    // references to our images
    public static Integer[] mThumbIds = {
            R.drawable.icon_youtube, R.drawable.icon_vk,
            R.drawable.icon_twitter, R.drawable.icon_tumblr,
            R.drawable.icon_soundcloud, R.drawable.icon_pinterest,
            R.drawable.icon_linkedin, R.drawable.icon_google_plus,
            R.drawable.icon_fb, R.drawable.icon_ezgif_com_gif_maker_1,
            R.drawable.icon_ezgif_com_gif_maker_2
    };
}
