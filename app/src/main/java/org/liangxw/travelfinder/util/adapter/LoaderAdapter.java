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


/**
 * Easy-use Adapter, you just nee to implements the binData() fuc.
 *
 * @param <T>
 * @author houxg
 */
public abstract class LoaderAdapter<T> extends BaseAdapter {
    protected Context context;
    protected DataLoader<T> loader;
    private LayoutInflater inflater;
    private int layoutId; // item's layout id
    /**
     * View ids in the layout file, which you want to bind data to it.
     */
    private int[] mChildViewIds; //
    ListViewHandler viewHandler;
    int maxLoadOnce = 20;

    public interface ListViewHandler{
        void onLoadFinish();
    }

    public LoaderAdapter(Context context, DataLoader<T> loader, int layoutID, int[] viewIds) {
        super();
        this.context = context;
        this.loader = loader;
        layoutId = layoutID;
        mChildViewIds = viewIds;
        inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public static class ViewHolder {
        protected SparseArray<View> arrayViews;

        public ViewHolder(View convertView, int[] viewIds) {
            arrayViews = new SparseArray<>();
            for (int id : viewIds) {
                View view = convertView.findViewById(id);
                arrayViews.put(id, view);
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
        convertView = inflater.inflate(layoutId, parent, false);
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
        return loader.getSize();
    }

    @Override
    public T getItem(int position) {
        return loader.getItem(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void changeDataSourse(DataLoader<T> newLoader) {
        this.loader = newLoader;
        notifyDataSetChanged();
    }

    public void refreshFooter() {
        loader.loadFooter(this, maxLoadOnce);
    }

    public void refreshHeader() {
        loader.loadHeader(this, maxLoadOnce);
    }

    public void refreshAll() {
        loader.loadAll(this);
    }

    public ListViewHandler getViewHandler() {
        return viewHandler;
    }

    public void setViewHandler(ListViewHandler viewHandler) {
        this.viewHandler = viewHandler;
    }

    public int getMaxLoadOnece() {
        return maxLoadOnce;
    }

    public void setMaxLoadOnece(int maxLoadOnce) {
        this.maxLoadOnce = maxLoadOnce;
    }

    public void onLoadFinish()
    {
        notifyDataSetChanged();
        if(viewHandler!=null)
        {
            viewHandler.onLoadFinish();
        }
    }
}
