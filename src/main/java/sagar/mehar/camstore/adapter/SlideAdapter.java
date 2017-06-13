package sagar.mehar.camstore.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.github.chrisbanes.photoview.PhotoView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.util.ArrayList;

import sagar.mehar.camstore.R;

/**
 * Created by Mountain on 05-12-2016.
 */

class SlideAdapter extends PagerAdapter {

    private Context context;

    private ArrayList<File> fileChild;
    private LayoutInflater inflater;




    SlideAdapter(Context context,ArrayList<File> fileChild)
    {
        this.context=context;
        this.fileChild=fileChild;
    }



    @Override
    public int getCount() {
        return fileChild.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }




    @Override
    public Object instantiateItem(ViewGroup container, int i) {

            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = inflater.inflate(R.layout.swipe, container, false);
            PhotoView sliderImg = (PhotoView) v.findViewById(R.id.sliderImage);

             try {
                 ImageLoader.getInstance().displayImage("file://" + fileChild.get(i).getPath(), sliderImg);

                 if (sliderImg.getParent() != null) {
                     ((ViewGroup) sliderImg.getParent()).removeView(sliderImg);
                 }
                 container.addView(sliderImg);
             }
             catch(Exception e)
             {

             }
            return sliderImg;

    }

    @Override
    public void destroyItem(ViewGroup container, int i, Object obj) {
        container.removeView((ImageView) obj);
    }





}
