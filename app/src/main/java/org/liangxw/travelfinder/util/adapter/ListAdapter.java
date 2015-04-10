package org.liangxw.travelfinder.util.adapter;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Easy-use Adapter, you just nee to implements the binData() fuc.
 *
 * @param <T>
 * @author houxg
 */
public abstract class ListAdapter<T> extends BaseAdapter {
    protected List<T> mData;
    private LayoutInflater mInflater;
    private int mLayoutId; // item's layout id
    /**
     * View ids in the layout file, which you want to bind data to it.
     */
    private int[] mChildViewIds; //

    public ListAdapter(Context context, List<T> data, int layoutID, int[] viewIds) {
        super();
        mData = data;
        mLayoutId = layoutID;
        mChildViewIds = viewIds;
        mInflater = LayoutInflater.from(context);
    }


    public static class ViewHolder {
        protected SparseArray<View> arrayViews;

        public ViewHolder(View convertView, int[] viewIds) {
            arrayViews = new SparseArray<View>();
            if (viewIds != null) {
                for (int id : viewIds) {
                    View view = convertView.findViewById(id);
                    arrayViews.put(id, view);
                }
            }
        }

        public View getView(int id) {
            return arrayViews.get(id);
        }

        public TextView getTextView(int id) {
            return (TextView) arrayViews.get(id);
        }

        public ImageView getImageView(int id) {
            return (ImageView) arrayViews.get(id);
        }

        public Button getButton(int id) {
            return (Button) arrayViews.get(id);
        }

    }

    /**
     * generate View and its ViewHolder
     *
     * @param convertView
     * @param parent
     * @return
     */
    protected View genView(View convertView, ViewGroup parent) {
        convertView = mInflater.inflate(mLayoutId, parent, false);
        ViewHolder holder = new ViewHolder(convertView, mChildViewIds);
        convertView.setTag(holder);
        return convertView;
    }

    /**
     * bind data to the View
     *
     * @param holder   convertView's ViewHolder, use holder.getViewById() to get View
     * @param item
     * @param position
     */
    abstract protected void bindData(ViewHolder holder, T item, int position);

    /**
     * if you need, it only run when convertView is null
     *
     * @param holder
     * @param position
     */
    protected void onGenView(ViewHolder holder, int position) {
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = genView(convertView, parent);
            onGenView((ViewHolder) convertView.getTag(), position);
        }
        holder = (ViewHolder) convertView.getTag();
        bindData(holder, getItem(position), position);
        return convertView;
    }

    @Override
    public int getCount() {
        if (mData == null) {
            return 0;
        } else {
            return mData.size();
        }
    }

    @Override
    public T getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void changeDataSourse(List<T> data) {
        mData = data;
        notifyDataSetChanged();
    }

}
