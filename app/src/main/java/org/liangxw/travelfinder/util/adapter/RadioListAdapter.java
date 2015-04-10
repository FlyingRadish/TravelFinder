package org.liangxw.travelfinder.util.adapter;

import android.content.Context;

import java.util.List;

/**
 * Des:
 * Created by houxg on 2015/2/27.
 */
public abstract class RadioListAdapter<T> extends ListAdapter<T>{

    int selectIndex = -1;

    public RadioListAdapter(Context context, List<T> data, int layoutID, int[] viewIds) {
        this(context, data, layoutID, viewIds, -1);
    }

    public RadioListAdapter(Context context, List<T> data, int layoutID, int[] viewIds, int selectIndex) {
        super(context, data, layoutID, viewIds);
        this.selectIndex = selectIndex;
    }

    public int getSelectIndex() {
        return selectIndex;
    }

    public void setSelectIndex(int selectIndex) {
        this.selectIndex = selectIndex;
    }
}
