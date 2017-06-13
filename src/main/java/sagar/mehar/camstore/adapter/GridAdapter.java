package sagar.mehar.camstore.adapter;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import sagar.mehar.camstore.MainActivity;
import sagar.mehar.camstore.R;
import sagar.mehar.camstore.util.FileComparator;


public class GridAdapter extends BaseAdapter  {

    private File[] data;
    private Context context;
    private boolean[] checkBoxState;
    private LayoutInflater inflater = null;


    public GridAdapter(Context context, File[] data) {
        this.context = context;
        this.data = data;
        checkBoxState = new boolean[getCount()];
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public class Holder {
        TextView tv;
        ImageView img;
        CheckBox chk;
    }
    public void setSelectedAll()                     // set all items checked.
    {
        for(int i=0;i<data.length;i++)
        {
            checkBoxState[i] = !checkBoxState[i];
        }
        GridAdapter.super.notifyDataSetChanged();
    }
    public ArrayList<File> getSelected()
    {
        ArrayList<File> temp=new ArrayList<>();
        for(int i=0;i<data.length;i++)
        {
            if(checkBoxState[i])
                temp.add(data[i]);
        }
        return  temp;
    }


    @Override
    public int getCount() {
        return data.length;
    }

    @Override
    public Object getItem(int position) {
        return data[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        final Holder holder = new Holder();

        View rowView;

        if (convertView == null) {

            rowView = inflater.inflate(R.layout.gridout, parent, false);

        } else {
            rowView = convertView;
        }
        holder.tv = (TextView) rowView.findViewById(R.id.textView1);
        holder.img = (ImageView) rowView.findViewById(R.id.imageView1);
        holder.chk = (CheckBox) rowView.findViewById(R.id.checkBox);

        boolean isShow = ((MainActivity) context).isShow();
        if (isShow) {
            holder.chk.setVisibility(View.VISIBLE);
        } else {
            holder.chk.setVisibility(View.GONE);
        }
        final File f = data[position];

        if (f.isDirectory())
        {
            //directory part
            try {
                int countpic=-1;

                // this would display the inner images in a directory instead of folder drawable.
                File[] fileArrayList=f.listFiles();
                Arrays.sort(fileArrayList, new FileComparator());
                if(fileArrayList.length>0) {
                    for (int ii = 0; ii < fileArrayList.length; ii++) {
                       //if the file is of .dthumb type then this if is necessary.
                        if (fileArrayList[ii].length() == 0||fileArrayList[ii].isDirectory()) {
                           // holder.img.setImageResource(R.mipmap.folderpic);
                            holder.img.setBackgroundColor(Color.BLACK);

                        }else if(getExtension(fileArrayList[ii]).equals("jpg") ||getExtension(fileArrayList[ii]).equals("png") ||getExtension(fileArrayList[ii]).equals("jpeg")
                                ||getExtension(fileArrayList[ii]).equals("mp4") ||getExtension(fileArrayList[ii]).equals("3gp")) {
                            countpic = ii;
                            break;
                        }
                    }
                   // if a image file is found, it is set as folder picture else folderpic is set as folder picture.
                    if (countpic != -1)
                        ImageLoader.getInstance().displayImage("file://" + fileArrayList[countpic].getPath(), holder.img);
                    else {
                     //   holder.img.setImageResource(R.mipmap.folderpic);
                        holder.img.setBackgroundColor(Color.BLACK);
                    }
                }
                else
                {
                   // holder.img.setImageResource(R.mipmap.folderpic); //in case no file is present inside the picture.
                    holder.img.setBackgroundColor(Color.BLACK);
                }
                holder.tv.setShadowLayer(1.5f,1,1, Color.BLACK);
                holder.tv.setText(f.getName());
                holder.chk.setChecked(checkBoxState[position]);
            } catch (Exception e) {
                // in case of exception default things happen
               // holder.img.setImageResource(R.mipmap.folderpic);
                holder.img.setBackgroundColor(Color.BLACK);
                holder.tv.setShadowLayer(1.5f, 1, 1, Color.BLACK);
                holder.tv.setText(f.getName());
                holder.chk.setChecked(checkBoxState[position]);
            }
        }
        else
        {
            //file part
            try {
                if (f.length() == 0) {
//                    holder.img.setBackgroundColor(0x00FFFFFF);
//                    holder.img.setImageResource(R.drawable.menu_save);
//                    holder.tv.setText(R.string.nullfiles);
//                    holder.chk.setChecked(checkBoxState[position]);
                } else if (getExtension(f).equals("jpg") || getExtension(f).equals("png") || getExtension(f).equals("jpeg")) {

                    ImageLoader.getInstance().displayImage("file://" + f.getPath(), holder.img);

                    holder.tv.setBackgroundResource(R.color.colorSecond);
                    holder.tv.setText("");
                    holder.chk.setChecked(checkBoxState[position]);
                } else if (getExtension(f).equals("mp4") || getExtension(f).equals("3gp")) {
                    ImageLoader.getInstance().displayImage("file://" + f.getPath(), holder.img);

                    holder.tv.setText("");
                    ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams) holder.tv
                            .getLayoutParams();
                    ViewGroup.LayoutParams layoutParams = holder.tv.getLayoutParams();
                    layoutParams.height = 60;
                    mlp.setMargins(0, 0, 0, 70);
                    holder.tv.setBackgroundResource(R.drawable.tvholder);
                    holder.chk.setChecked(checkBoxState[position]);
                } else if (getExtension(f).equals("cslock")) {
                    holder.img.setBackgroundColor(0x00FFFFFF);
                    holder.img.setImageResource(R.mipmap.encrypteditems);
                    holder.tv.setBackgroundResource(R.color.colorSecond);
                    holder.tv.setText(f.getName());
                    holder.chk.setChecked(checkBoxState[position]);
                } else {
//                    holder.img.setBackgroundColor(0x00FFFFFF);
//                    holder.img.setImageResource(R.drawable.menu_save);
//                    holder.tv.setText(f.getName());
//                    holder.tv.setBackgroundResource(R.color.colorSecond);
//                    holder.chk.setChecked(checkBoxState[position]);
                }
            }
            catch (Exception e)
            {
                Toast.makeText(context,"Problem displaying data",Toast.LENGTH_SHORT).show();
            }
        }

        holder.chk.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                checkBoxState[position] = ((CheckBox) v).isChecked();
            }
        });



             rowView.setOnClickListener(new View.OnClickListener() {
                 @Override
                 public void onClick(View v) {
                 if(f.isDirectory())
                 {
                     ((MainActivity)context).display(f.getPath());
                 }
                 else if(getExtension(f).equals("mp4") || getExtension(f).equals("3gp"))
                 {
                     Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(f.getPath()));
                     intent.setDataAndType(Uri.parse(f.getPath()), "video/*");
                     context.startActivity(intent);
                 }
                     else
                 {
                     android.support.v4.app.FragmentManager fragmentManager=((MainActivity) context).getSupportFragmentManager();
                     android.support.v4.app.FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
                     SliderFragment sliderFragment=new SliderFragment();

                     Bundle bundle = new Bundle();
                     bundle.putString("parentpath",f.getParent());
                     bundle.putInt("position",position);
                     sliderFragment.setArguments(bundle);
                     fragmentTransaction.add(R.id.activity_main,sliderFragment,"tag");
                     fragmentTransaction.commit();
                 }
                 }
             });


            rowView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    ((MainActivity) context).isShow(true);
                    GridAdapter.super.notifyDataSetChanged();
                    ((MainActivity)context).invalidateOptionsMenu();
                    return true;
                }
            });





        return rowView;
    }

    @Override
    public void notifyDataSetChanged() {
        checkBoxState = new boolean[getCount()];
        super.notifyDataSetChanged();
    }




    private  String getExtension(File f) {
        String ext = null;
        String s = f.getName();
        int i;
        if(s.contains(".")){
            i = s.lastIndexOf('.');
        }
        else
        {
            return "null";
        }

        if (i > 0 && i < s.length() - 1) {
            ext = s.substring(i + 1).toLowerCase();
        }
        return ext;
    }

}
