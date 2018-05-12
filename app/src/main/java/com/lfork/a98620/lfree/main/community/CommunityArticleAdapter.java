package com.lfork.a98620.lfree.main.community;

import android.app.Activity;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ListView;

import com.lfork.a98620.lfree.BR;
import com.lfork.a98620.lfree.R;

import java.util.List;

public class CommunityArticleAdapter extends RecyclerView.Adapter<CommunityArticleAdapter.ViewHolder> {

    private List<CommunityFragmentItemViewModel> itemViewModelList;

    public CommunityArticleAdapter(List<CommunityFragmentItemViewModel> itemViewModelList) {
        this.itemViewModelList = itemViewModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewHolder viewHolder = ViewHolder.create(LayoutInflater.from(parent.getContext()), parent, viewType);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bindTo(itemViewModelList.get(position));
        //holder.createImgListView(itemViewModelList.get(position).getImageUriList());
    }

    @Override
    public int getItemCount() {
        return itemViewModelList.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        ViewDataBinding binding;

        public ViewHolder(ViewDataBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        static ViewHolder create(LayoutInflater inflater, ViewGroup parent, int type) {
            ViewDataBinding binding = DataBindingUtil.inflate(inflater, R.layout.main_community_frag_recycler_view_item, parent, false);
            return new ViewHolder(binding);
        }

        public void bindTo(CommunityFragmentItemViewModel itemViewModel) {
            binding.setVariable(BR.viewModel, itemViewModel);
            binding.executePendingBindings();
        }
        public void createImgListView( List<CommunityFragmentImgItemViewModel> articleImgList) {
            ListView listView = (ListView) binding.getRoot().findViewById(R.id.img_list);
            ArticleImgAdapter adapter = new ArticleImgAdapter(binding.getRoot().getContext(), R.layout.main_community_frag_article_img_item, articleImgList);
            listView.setAdapter(adapter);
        }
    }
}
