package com.krt.base.inter;


public interface IBaseActivity {
    //绑定butterknife(这个方法需要在主项目的BaseActivity中实现)
    void bindButterKnife();

    //解绑参考上述
    void unbindButterknife();

    //初始化当前项目的逻辑配置(这个方法需要再主项目的BaseActivity中实现)
    void beforeBindLayout();

    void init();

    //绑定布局
    int bindLayout();

    //初始化视图
    void initView();

    //加载数据
    void loadData();

}
