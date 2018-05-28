package com.lfork.a98620.lfree.goodsupload;

import android.content.Context;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableField;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.lfork.a98620.lfree.base.viewmodel.GoodsViewModel;
import com.lfork.a98620.lfree.data.DataSource;
import com.lfork.a98620.lfree.data.entity.Goods;
import com.lfork.a98620.lfree.data.goods.GoodsDataRepository;
import com.lfork.a98620.lfree.data.user.UserDataRepository;
import com.lfork.a98620.lfree.util.StringUtil;
import com.lfork.a98620.lfree.util.ToastUtil;
import com.lfork.a98620.lfree.util.file.UriHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import top.zibin.luban.Luban;

/**
 * Created by 98620 on 2018/4/14.
 */

public class GoodsUploadViewModel extends GoodsViewModel {
    private static final String TAG = "GoodsUploadViewModel";

    public final ObservableField<String> tips = new ObservableField<>("已选择(0/5)");

    private Context activity;

    private GoodsUploadNavigator navigator;

    public void setNavigator(GoodsUploadNavigator navigator) {
        this.navigator = navigator;
    }

    private List<String> imagePathList;

    private GoodsDataRepository repository;

    public final ObservableArrayList<Boolean> imageVisible = new ObservableArrayList<>();

    private int imageCount;

    private final int MAX_COUNT = 5;

    GoodsUploadViewModel(Context context) {
        super(context);
        activity = context;
        setImageVisibility();
        imagePathList = new ArrayList<>();
        repository = GoodsDataRepository.getInstance();
        dataIsLoading.set(false);

    }

    private void setImageVisibility() {

        Log.d(TAG, "setImageVisibility1: " + "image count" + imageCount);
        if (imageCount == 0) {
            imageVisible.clear();
            imageVisible.add(false);//默认第一个可见
            imageVisible.add(false);
            imageVisible.add(false);
            imageVisible.add(false);
            imageVisible.add(false);
            return;
        }


        int i = 0;
        for (; i < imageCount; i++) {
            if (i == MAX_COUNT) {
                return;
            }
            imageVisible.set(i, true);
        }

        Log.d(TAG, "setImageVisibility: " + i + "image count" + imageCount);

        for (; i < MAX_COUNT; i++)
            imageVisible.set(i, false);

    }

    /**
     * 静态的图片配合动态的数据  每次添加图片后
     *
     * @param index
     */
    public void deleteImage(int index) {
        if (navigator != null)
            navigator.deleteImage(index);
    }

    /**
     * 这里就是根据选择的图片数量来设置imageView
     */
    public void selectImages() {

        if (imageCount == MAX_COUNT) {
            return;
        }
        if (navigator != null)
            navigator.showMyDialog(MAX_COUNT - imageCount);
    }

    public void setImages(List<Uri> mSelected) {


        imageCount = mSelected.size();
        imagePathList.clear();
        for (int i = 0; i < mSelected.size(); i++) {
            imagePathList.add(UriHelper.getPath(activity, mSelected.get(i)));
        }
        tips.set("已选择(" + imageCount + "/5)");
        setImageVisibility();
    }

    public void setImage(String path) {

        notifyChange();
        imagePathList.add(path);
        imageCount++;
        tips.set("已选择(" + imageCount + "/5)");
        setImageVisibility();
    }

    public void uploadGoods() {
        if (imagePathList.size() < 1) {
            ToastUtil.showShort(context, "请添加图片");
            return;
        }
        if (StringUtil.isNull(name.get())) {
            ToastUtil.showShort(context, "名称不能为空");
            return;
        }

        if (StringUtil.isNull(description.get())) {
            ToastUtil.showShort(context, "描述不能为空");
            return;
        }

        if (StringUtil.isNull(description.get())) {
            ToastUtil.showShort(context, "描述不能为空");
            return;
        }

        if (StringUtil.isNull(price.get())) {
            ToastUtil.showShort(context, "价格不能为空");
            return;
        }

        dataIsLoading.set(true);
        Goods g = new Goods();
        g.setName(name.get());
        g.setDescription(description.get());
        g.setCoverImagePath(imagePathList.get(0));
        g.setUserId(UserDataRepository.getInstance().getThisUser().getUserId());
        g.setOriginPrice(originPrice.get());
        g.setPrice(price.get());
        g.setCategoryId(1);


        List<File> newImages = new ArrayList<>();

        Flowable.just(imagePathList)
                .observeOn(Schedulers.io())
                .map(new Function<List<String>, List<File>>() {
                    @Override
                    public List<File> apply(@NonNull List<String> list) throws Exception {
                        // 同步方法直接返回压缩后的文件
                        newImages.addAll(
                                Luban.with(context)
                                        .load(list)
                                        .setTargetDir(context.getExternalCacheDir().toString())
                                        .get());
                        for (File str : newImages) {
                            Log.d(TAG, "onSuccess: " + str.getPath());

                        }

                        if (imagePathList.size() >= 1) {
                            String[] imagesArray = new String[newImages.size()];

                            for (int i = 0; i < newImages.size(); i++) {
                                imagesArray[i] = newImages.get(i).getPath();
                            }
                            g.setImagesPath(imagesArray);
                        }

                        repository.uploadGoods(new DataSource.GeneralCallback<String>() {
                            @Override
                            public void succeed(String data) {
                                dataIsLoading.set(false);

                                if (navigator != null)
                                    navigator.uploadSucceed();
                            }

                            @Override
                            public void failed(String log) {
                                dataIsLoading.set(false);
                                if (navigator != null)
                                    navigator.uploadFailed(log);

                            }
                        }, g);


                        return newImages;
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();

    }


    @Override
    public void start() {

    }

    @Override
    public void destroy() {
        navigator = null;
    }
}
