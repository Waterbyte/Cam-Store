package sagar.mehar.camstore.adapter;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.clans.fab.FloatingActionButton;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import sagar.mehar.camstore.R;
import sagar.mehar.camstore.util.FileComparator;


/**
 * Created by Mountain on 05-12-2016.
 */

public class SliderFragment extends Fragment {

    private ViewPager viewPager;
    private File f;
    private File[] folderXChild;
    private ArrayList<File> fileChild;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    final View view=inflater.inflate(R.layout.android_image_slider_activity, container, false);
        int folder_count=0;
        String path=getArguments().getString("parentpath");
        int position=getArguments().getInt("position");
        f=new File(path);
        File x=new File(path);
        folderXChild=x.listFiles();
        folderXChild=removeInv(folderXChild);
        fileChild= new ArrayList<>();
        Arrays.sort(folderXChild, new FileComparator());
        for(File f:folderXChild)
        {
            if(!f.isDirectory())
            {
                fileChild.add(f);
            }
            else
            {
                folder_count++;
            }
        }

        viewPager = (ViewPager)view.findViewById(R.id.viewPageAndroid);
        SlideAdapter slideAdapter=new SlideAdapter(this.getContext(),fileChild);
        viewPager.setOffscreenPageLimit(3);
        viewPager.setAdapter(slideAdapter);
        viewPager.setCurrentItem(Math.abs(position-folder_count),true);

        return view;
    }

    @Override
    public void onResume() {

        FloatingActionButton edit=(FloatingActionButton)(this.getActivity().findViewById(R.id.editBut));
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri=Uri.fromFile(fileChild.get(viewPager.getCurrentItem()));
                Intent editIntent = new Intent(Intent.ACTION_EDIT);
                editIntent.setDataAndType(uri, "image/*");
                editIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivity(Intent.createChooser(editIntent, null));
                Log.d("File Clicked",viewPager.getCurrentItem()+"");
            }
        });

        super.onResume();
    }
    private static File[] removeInv(File[] folderXCHILD)
    {
        ArrayList<File> visfolderXChild= new ArrayList<>();          //this step remove all the files starting with .
        int vis=0;
        for(File f:folderXCHILD)
        {
            if(f.getName().startsWith("."))
            {}
            else {
                visfolderXChild.add(f);
                ++vis;
            }
        }
        Iterator<File> it=visfolderXChild.iterator();
        File[] folderXChildvis=new File[vis];
        int ivis=0;
        while(it.hasNext())
        {
            folderXChildvis[ivis]=it.next();
            ivis++;
        }
        return folderXChildvis;
    }
}
