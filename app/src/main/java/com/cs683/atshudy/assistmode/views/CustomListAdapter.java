package com.cs683.atshudy.assistmode.views;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.cs683.atshudy.assistmode.R;
import com.cs683.atshudy.assistmode.model.DrivingModeDAO;
import com.cs683.atshudy.assistmode.model.DrivingModeDAOImpl;
import com.cs683.atshudy.assistmode.model.DrivingModeTask;
import com.cs683.atshudy.assistmode.model.SilentModeDAO;
import com.cs683.atshudy.assistmode.model.SilentModeDAOImpl;
import com.cs683.atshudy.assistmode.model.SilentModeTask;
import com.cs683.atshudy.assistmode.model.Task;
import com.cs683.atshudy.assistmode.model.WiFiModeDAO;
import com.cs683.atshudy.assistmode.model.WiFiModeDAOImpl;
import com.cs683.atshudy.assistmode.model.WiFiModeTask;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * This allows me to dynamically create and delete task items.
 * Below is a tutorial on how to set it up.
 *
 * http://www.androidhub4you.com/2013/02/muftitouch-listview-multi-click.html#ixzz3UTeWauSl
 * http://www.android-ios-tutorials.com/android/android-custom-listview-example/
 */
public class CustomListAdapter extends ArrayAdapter<Task> {
	Context context;
	int layoutResourceId;
	ArrayList<Task> data = new ArrayList<Task>();
    static final String TAG = "com.cs683.atshudy.assistmode.views.CustomListAdapter";
    final int TAB_ONE = 1;
    final int TAB_TWO = 2;

    final int TAB_THREE = 3;

	public CustomListAdapter(Context context, ArrayList<Task> data) {
        super(context, R.layout.row, data);
        this.layoutResourceId = R.layout.row;
        this.context = context;
        this.data = data;
    }

    /**
     * This method get called for every item in the listview.
     *
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        TaskItemHolder holder = null;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new TaskItemHolder();
            holder.taskImg = (ImageView) row.findViewById(R.id.taskImage);
            holder.taskName = (TextView) row.findViewById(R.id.textView1);
            holder.btnEdit = (ImageButton) row.findViewById(R.id.editBtn);
            holder.btnDelete = (ImageButton) row.findViewById(R.id.deleteBtn);
            row.setTag(holder);
        } else {
            holder = (TaskItemHolder) row.getTag();
        }
        Task item = data.get(position);
        holder.taskName.setText(item.getName());
        holder.btnEdit.setOnClickListener(new CustomEditOnClickListener(item));
        holder.btnDelete.setOnClickListener(new CustomDeleteOnClickListener(item, this));
        return row;
        }


    /**
     * Class that handles the Edit button click on the item
     */
    public class CustomEditOnClickListener implements OnClickListener
    {

        Task clickedItem;
        public CustomEditOnClickListener(Task item) {
            this.clickedItem = item;
        }

        @Override
        public void onClick(View v) {
            Log.i(TAG, "Edit Button Clicked **********");
            Intent intent = null;
            switch (clickedItem.getParentTab()){
                case  TAB_ONE:
                    Log.i(TAG, "Editing a Silent Mode task");
                    //Toast.makeText(context, "Editing a Silent Mode task item "+ clickedItem.getName(), Toast.LENGTH_SHORT).show();
                        intent = new Intent(context, SilentEditTaskItemActivity.class);
                        intent.putExtra("taskName", clickedItem.getName());
                        context.startActivity(intent);
                        break;
                case  TAB_TWO:
                    Log.i(TAG, "Editing a WIFI Mode task");
                    //Toast.makeText(context, "Editing a WIFI Mode task "+ clickedItem.getName(), Toast.LENGTH_SHORT).show();
                        intent = new Intent(context, WiFiEditTaskItem.class);
                        intent.putExtra("taskName", clickedItem.getName());
                        context.startActivity(intent);
                        break;
                case  TAB_THREE:
                    Log.i(TAG, "Editing a Driving Mode task");
                    //Toast.makeText(context, "Editing a Driving Mode task "+ clickedItem.getName(), Toast.LENGTH_SHORT).show();
                        intent = new Intent(context, DrivingEditTaskItem.class);
                        intent.putExtra("taskName", clickedItem.getName());
                        context.startActivity(intent);
                        break;
                default:
                    Log.e(TAG, "Task item was click from an Unknown list view");
            }

        }

    }

    /**
     * class the handles the delete button click on the item
     */
    public class CustomDeleteOnClickListener implements OnClickListener
    {

        Task clickedItem;
        CustomListAdapter adapter;
        public CustomDeleteOnClickListener(Task item, CustomListAdapter adapter) {
            this.clickedItem = item;
            this.adapter = adapter;

        }

        @Override
        public void onClick(View v) {
            Log.i(TAG, "Deleting a Silent Mode task"+clickedItem.getName());
            deleteTaskItem(clickedItem, adapter);
            adapter.notifyDataSetChanged();
        }

        /**
         *
         * @param Item
         * @param adapter
         */
        void deleteTaskItem(Task Item, CustomListAdapter adapter) {
            Log.i(TAG, "Need to delete Item from database table");
            switch (Item.getParentTab()){
                case  TAB_ONE:
                    Log.i(TAG, "Delete from the Silent Mode table");
                    SilentModeDAO silentModeDAO;
                    silentModeDAO = (SilentModeDAO) new SilentModeDAOImpl(context);
                    try {
                        silentModeDAO.deleteSilentModeTask(new SilentModeTask(Item.getName()));
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    break;
                case  TAB_TWO:
                    Log.i(TAG, "Delete from WIFI Mode table");
                    WiFiModeDAO wifiModeDAO;
                    wifiModeDAO = (WiFiModeDAO) new WiFiModeDAOImpl(context);
                    try {
                        wifiModeDAO.deleteWiFiModeTask(new WiFiModeTask(Item.getName()));
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    break;
                case  TAB_THREE:
                    Log.i(TAG, "Delete from Driving Mode table");
                    DrivingModeDAO drivingModeDAO;
                    try {
                        drivingModeDAO = new DrivingModeDAOImpl(context);
                        drivingModeDAO.deleteDrivingModeTask(new DrivingModeTask(Item.getName()));
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    Log.e(TAG, "Task item was click from an Unknown list view");
            }
            data.remove(clickedItem);
        }



    }

    /**
     *  class that contain the listview item
     */
    static class TaskItemHolder {
        ImageView taskImg;
        TextView taskName;
        ImageButton btnEdit;
        ImageButton btnDelete;

        public String getName(){
            return this.taskName.getText().toString();
        }

        public void setName(String name){
            this.taskName.setText(name);
        }
    }
}