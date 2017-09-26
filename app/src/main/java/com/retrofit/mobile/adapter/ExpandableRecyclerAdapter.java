package com.retrofit.mobile.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;


public abstract class ExpandableRecyclerAdapter<T extends ExpandableRecyclerAdapter.ListItem> extends RecyclerView.Adapter<ExpandableRecyclerAdapter.ViewHolder> {
    protected Context mContext;
    protected List<T> allItems = new ArrayList();
    protected List<T> visibleItems = new ArrayList();
    private List<Integer> indexList = new ArrayList();
    private SparseIntArray expandMap = new SparseIntArray();
    private int mode;
    protected static final int TYPE_HEADER = 1000;
    private static final int ARROW_ROTATION_DURATION = 150;
    public static final int MODE_NORMAL = 0;
    public static final int MODE_ACCORDION = 1;

    public ExpandableRecyclerAdapter(Context context) {
        this.mContext = context;
    }

    public long getItemId(int i) {
        return (long)i;
    }

    public int getItemCount() {
        return this.visibleItems == null?0:this.visibleItems.size();
    }

    protected View inflate(int resourceID, ViewGroup viewGroup) {
        return LayoutInflater.from(this.mContext).inflate(resourceID, viewGroup, false);
    }

    public boolean toggleExpandedItems(int position, boolean notify) {
        if(this.isExpanded(position)) {
            this.collapseItems(position, notify);
            return false;
        } else {
            this.expandItems(position, notify);
            if(this.mode == 1) {
                this.collapseAllExcept(position);
            }

            return true;
        }
    }

    public void expandItems(int position, boolean notify) {
        int count = 0;
        int index = ((Integer)this.indexList.get(position)).intValue();
        int insert = position;

        int allItemsPosition;
        for(allItemsPosition = index + 1; allItemsPosition < this.allItems.size() && ((ExpandableRecyclerAdapter.ListItem)this.allItems.get(allItemsPosition)).ItemType != 1000; ++allItemsPosition) {
            ++insert;
            ++count;
            this.visibleItems.add(insert, this.allItems.get(allItemsPosition));
            this.indexList.add(insert, Integer.valueOf(allItemsPosition));
        }

        this.notifyItemRangeInserted(position + 1, count);
        allItemsPosition = ((Integer)this.indexList.get(position)).intValue();
        this.expandMap.put(allItemsPosition, 1);
        if(notify) {
            this.notifyItemChanged(position);
        }

    }

    public void collapseItems(int position, boolean notify) {
        int count = 0;
        int index = ((Integer)this.indexList.get(position)).intValue();

        int allItemsPosition;
        for(allItemsPosition = index + 1; allItemsPosition < this.allItems.size() && ((ExpandableRecyclerAdapter.ListItem)this.allItems.get(allItemsPosition)).ItemType != 1000; ++allItemsPosition) {
            ++count;
            this.visibleItems.remove(position + 1);
            this.indexList.remove(position + 1);
        }

        this.notifyItemRangeRemoved(position + 1, count);
        allItemsPosition = ((Integer)this.indexList.get(position)).intValue();
        this.expandMap.delete(allItemsPosition);
        if(notify) {
            this.notifyItemChanged(position);
        }

    }

    protected boolean isExpanded(int position) {
        int allItemsPosition = ((Integer)this.indexList.get(position)).intValue();
        return this.expandMap.get(allItemsPosition, -1) >= 0;
    }

    public int getItemViewType(int position) {
        return ((ExpandableRecyclerAdapter.ListItem)this.visibleItems.get(position)).ItemType;
    }

    public void setItems(List<T> items) {
        this.allItems = items;
        ArrayList visibleItems = new ArrayList();
        this.expandMap.clear();
        this.indexList.clear();

        for(int i = 0; i < items.size(); ++i) {
            if(((ExpandableRecyclerAdapter.ListItem)items.get(i)).ItemType == 1000) {
                this.indexList.add(Integer.valueOf(i));
                visibleItems.add(items.get(i));
            }
        }

        this.visibleItems = visibleItems;
        this.notifyDataSetChanged();
    }

    protected void notifyItemInserted(int allItemsPosition, int visiblePosition) {
        this.incrementIndexList(allItemsPosition, visiblePosition, 1);
        this.incrementExpandMapAfter(allItemsPosition, 1);
        if(visiblePosition >= 0) {
            this.notifyItemInserted(visiblePosition);
        }

    }

    protected void removeItemAt(int visiblePosition) {
        int allItemsPosition = ((Integer)this.indexList.get(visiblePosition)).intValue();
        this.allItems.remove(allItemsPosition);
        this.visibleItems.remove(visiblePosition);
        this.incrementIndexList(allItemsPosition, visiblePosition, -1);
        this.incrementExpandMapAfter(allItemsPosition, -1);
        this.notifyItemRemoved(visiblePosition);
    }

    private void incrementExpandMapAfter(int position, int direction) {
        SparseIntArray newExpandMap = new SparseIntArray();

        for(int i = 0; i < this.expandMap.size(); ++i) {
            int index = this.expandMap.keyAt(i);
            newExpandMap.put(index < position?index:index + direction, 1);
        }

        this.expandMap = newExpandMap;
    }

    private void incrementIndexList(int allItemsPosition, int visiblePosition, int direction) {
        ArrayList newIndexList = new ArrayList();

        for(int i = 0; i < this.indexList.size(); ++i) {
            if(i == visiblePosition && direction > 0) {
                newIndexList.add(Integer.valueOf(allItemsPosition));
            }

            int val = ((Integer)this.indexList.get(i)).intValue();
            newIndexList.add(Integer.valueOf(val < allItemsPosition?val:val + direction));
        }

        this.indexList = newIndexList;
    }

    public void collapseAll() {
        this.collapseAllExcept(-1);
    }

    public void collapseAllExcept(int position) {
        for(int i = this.visibleItems.size() - 1; i >= 0; --i) {
            if(i != position && this.getItemViewType(i) == 1000 && this.isExpanded(i)) {
                this.collapseItems(i, true);
            }
        }

    }

    public void expandAll() {
        for(int i = this.visibleItems.size() - 1; i >= 0; --i) {
            if(this.getItemViewType(i) == 1000 && !this.isExpanded(i)) {
                this.expandItems(i, true);
            }
        }

    }

    public static void openArrow(View view) {
        view.animate().setDuration(150L).rotation(90.0F);
    }

    public static void closeArrow(View view) {
        view.animate().setDuration(150L).rotation(0.0F);
    }

    public int getMode() {
        return this.mode;
    }

    public void setMode(int mode) {
        this.mode = mode;
    }

    public class ItemViewHolder extends ExpandableRecyclerAdapter<T>.ViewHolder {
        public ItemViewHolder(View view) {
            super(view);
        }
    }

    public class StaticViewHolder extends ExpandableRecyclerAdapter<T>.ViewHolder {
        public StaticViewHolder(View view) {
            super(view);
        }
    }

    public class HeaderViewHolder extends ExpandableRecyclerAdapter<T>.ViewHolder {
        ImageView arrow;

        public HeaderViewHolder(View view, ImageView arrow) {
            super(view);
            this.arrow = arrow;
            view.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    HeaderViewHolder.this.handleClick();
                }
            });
        }

        protected void handleClick() {
            if(ExpandableRecyclerAdapter.this.toggleExpandedItems(this.getLayoutPosition(), false)) {
                ExpandableRecyclerAdapter.openArrow(this.arrow);
            } else {
                ExpandableRecyclerAdapter.closeArrow(this.arrow);
            }

        }

        public void bind(int position) {
            this.arrow.setRotation(ExpandableRecyclerAdapter.this.isExpanded(position)?90.0F:0.0F);
        }
    }

    public class ViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder {
        public ViewHolder(View view) {
            super(view);
        }
    }

    public static class ListItem {
        public int ItemType;

        public ListItem(int itemType) {
            this.ItemType = itemType;
        }
    }
}