package com.krt.lego.oc.imp.surface;

import android.Manifest;
import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.jaeger.library.StatusBarUtil;
import com.krt.base.util.MPermissions;
import com.krt.lego.R;
import com.krt.lego.oc.core.tools.BroadCastMessageWarp;
import com.krt.lego.oc.imp.custom.bottombar.BottomLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * @author: MaGua
 * @create_on:2021/10/2 11:04
 * @description
 */
public abstract class Main extends AppCompatActivity implements BottomLayout.InstanceFragmentImp {
    private BottomLayout bottomLayout;
    private String startCid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        beforeBindLayout();
        setContentView(R.layout.layout_frame);
        StatusBarUtil.setTranslucentForImageView(this, 20, null);
        EventBus.getDefault().register(this);
        startCid = getIntent().getStringExtra("name");
        initViews();
        init();
    }

    protected abstract void beforeBindLayout();

    protected abstract void init();

    private void initViews() {

        FrameLayout frameLayout = findViewById(R.id.content);

        bottomLayout = new BottomLayout(this,
                startCid + ".json", this);
        frameLayout.addView(bottomLayout);
        MPermissions.getInstance().request(this,
                getApplyPermissions()
                , value -> {

                });
    }

    /**
     * 申请界面需要的权限
     * 对SD卡读写权限已在缓存Asset目录时申请
     *
     * @return
     */
    protected String[] getApplyPermissions() {
        return new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE};
    }

    public Lump getFragmentByCid(String cid) {
        Lump module = null;
        for (Fragment fragment : bottomLayout.getFragments()) {
            if (fragment instanceof Lump) {
                module = (Lump) fragment;
                if (module.jsonFile.equals(cid)) return module;
            }
        }
        return module;
    }

    public Fragment getCurrentFragment() {
        return bottomLayout.getCurrentFragment();
    }


    /**
     * 如果不在基类进行注册
     *
     * @param bean
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void receiveBroadCast(BroadCastMessageWarp bean) {
        try {
            if (bean.getBroadCastName().equals("tabChange")) {
                int index = Integer.parseInt(bean.getParams().get("index"));
                page2Index(index);
            }
        } catch (Exception e) {

        }
    }

    public void page2Index(int index) {
        bottomLayout.setCurrentPage(index);
    }

    @Override
    protected void onResume() {
        super.onResume();
        bottomLayout.onResume();
    }

}
