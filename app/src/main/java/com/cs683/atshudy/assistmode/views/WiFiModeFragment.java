package com.cs683.atshudy.assistmode.views;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.cs683.atshudy.assistmode.R;
import com.cs683.atshudy.assistmode.business.WiFiModeService;
import com.cs683.atshudy.assistmode.model.Task;
import com.cs683.atshudy.assistmode.model.TaskModeItem;
import com.cs683.atshudy.assistmode.model.WiFiModeDAO;
import com.cs683.atshudy.assistmode.model.WiFiModeDAOImpl;
import com.cs683.atshudy.assistmode.model.WiFiModeTask;

import java.util.ArrayList;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnFragmentInteractionListener}
 * interface.
 */
public class WiFiModeFragment extends Fragment implements AbsListView.OnItemClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private static final String TAG = "com.cs683.atshudy.assistmode.views.WiFiModeFragment";
    private OnFragmentInteractionListener mListener;

    /**
     * The fragment's ListView/GridView.
     */
    private ListView mListView;
    WiFiModeDAO wiFiModeDAO;
    ArrayList<WiFiModeTask> wiFiModeTaskArray;
    ArrayList<Task> taskItemArray = new ArrayList<Task>();

    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    private CustomListAdapter mAdapter;

    // TODO: Rename and change types of parameters
    public static WiFiModeFragment newInstance(String param1, String param2) {
        WiFiModeFragment fragment = new WiFiModeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public WiFiModeFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private void loadListView() {
        // access the database table object
        WiFiModeDAO wiFiModeDAO;
        wiFiModeDAO = new WiFiModeDAOImpl(getActivity());

        taskItemArray.clear();
        //return all the records
        wiFiModeTaskArray = wiFiModeDAO.getAllWiFiModeTaskModeTasks();
        taskItemArray.addAll(wiFiModeTaskArray);

        //close database handle
        wiFiModeDAO.close();

        mAdapter = new CustomListAdapter(getActivity(), taskItemArray);
        mListView.invalidateViews();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wifimode, container, false);

        mListView = (ListView) view.findViewById(R.id.wifiList);
        mListView.setItemsCanFocus(false);

        Button add = (Button)view.findViewById(R.id.add_btn);
        add.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Activity currentActivity = getActivity();
                Intent intent = new Intent(currentActivity, WiFiAddTaskItem.class);
                currentActivity.startActivity(intent);
            }
        });

        // Set OnItemClickListener so we can be notified on item clicks
        mListView.setOnItemClickListener(this);
        loadListView();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "called onResume() ***** ");
        loadListView();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "called onViewCreated() ***** ");
        super.onViewCreated(view, savedInstanceState);
        mListView.setAdapter(mAdapter);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (null != mListener) {
            // Notify the active callbacks interface (the activity, if the
            // fragment is attached to one) that an item has been selected.
            mListener.onFragmentInteraction(TaskModeItem.ITEMS.get(position).id);
        }
    }

    /**
     * The default content for this Fragment has a TextView that is shown when
     * the list is empty. If you would like to change the text, call this method
     * to supply the text it should use.
     */
    public void setEmptyText(CharSequence emptyText) {
        View emptyView = mListView.getEmptyView();

        if (emptyView instanceof TextView) {
            ((TextView) emptyView).setText(emptyText);
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(String id);
    }

}
