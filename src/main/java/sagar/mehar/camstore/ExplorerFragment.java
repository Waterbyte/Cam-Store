package sagar.mehar.camstore;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import sagar.mehar.camstore.adapters.GridAdapter;
import sagar.mehar.camstore.utils.CommonUtility;
import sagar.mehar.camstore.utils.Constants;
import sagar.mehar.camstore.utils.SupportedFileFilter;

/**
 * Created by Mountain on 04-11-18.
 * */

public class ExplorerFragment extends Fragment {
    GridAdapter gridAdapter = null;
    private RecyclerView recyclerView = null;
    private TextView fragmentDescription = null;
    private ArrayList<String> supportedFiles = null;
    private SupportedFileFilter supportedFileFilter;
    private Context localContext = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.explorer_fragment, null);
        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        localContext = getActivity();
        recyclerView = view.findViewById(R.id.fragmentView);
        recyclerView.setHasFixedSize(true);
        fragmentDescription = view.findViewById(R.id.fragmentDescription);
        supportedFileFilter = new SupportedFileFilter();
        supportedFiles = new ArrayList<>();
    }

    public void setDisplayPath(String path) {
        supportedFiles.clear();
        File pathFile = new File(path);
        File[] supFiles = pathFile.listFiles(supportedFileFilter);
        if (supFiles.length > 0) {
            fragmentDescription.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);

            for (File file : supFiles) {
                supportedFiles.add(file.getAbsolutePath());
            }

            recyclerView.setLayoutManager(new GridLayoutManager(localContext, Constants.GRIDCOLUMNS));
            gridAdapter = new GridAdapter(localContext, supportedFiles);
            gridAdapter.setClickListener((MainActivity) localContext);
            recyclerView.setAdapter(gridAdapter);

        } else {
            fragmentDescription.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            fragmentDescription.setText("You can create folder and save pictures in it.");
        }
    }


    public void onClickListener(int position) {

        if (gridAdapter.getIsInChoiceMode()) {
            gridAdapter.switchSelectedState(position);
        } else {
            String filePath = supportedFiles.get(position);
            File clickedFile = new File(filePath);
            if (clickedFile.isDirectory()) {
                ((MainActivity) localContext).setCurrentPath(filePath);
            } else if (filePath.endsWith(".jpg") || filePath.endsWith("png")) {
                Uri photoURI = Uri.parse("file://"+clickedFile.getAbsolutePath());
                CommonUtility.openImage(localContext, photoURI);
            }
        }
    }

    public boolean onLongClickListener(int position) {
        gridAdapter.beginChoiceMode(position);
        return true;
    }

    public boolean ifMultiSelectEnabled() {
        return gridAdapter.getIsInChoiceMode();
    }

    public void stopMultiSelect() {
        Log.v("explorer fragment","stop multi select called");
        gridAdapter.setIsInChoiceMode(false);
        gridAdapter.clearSelectedState();
    }

    public ArrayList<String> getSelectedItems() {
        ArrayList<String> selectedItems = new ArrayList<>();
        for (Integer i : gridAdapter.getSelectedItems()) {
            selectedItems.add(gridAdapter.getItem(i));
        }
        return selectedItems;
    }

    public List<Integer> getSelectedItemsPosition() {
        return gridAdapter.getSelectedItems();
    }

    public int getSelectedItemCount() {
        return gridAdapter.getSelectedItemCount();
    }

    public void removeSelectedItems(List<Integer> selection){gridAdapter.deleteSelectedState(selection);}



}
