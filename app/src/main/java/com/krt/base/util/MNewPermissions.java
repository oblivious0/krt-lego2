package com.krt.base.util;//package krt.wid.util;
//
//import android.app.AlertDialog;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.text.TextUtils;
//
//import com.yanzhenjie.permission.Action;
//import com.yanzhenjie.permission.AndPermission;
//import com.yanzhenjie.permission.Permission;
//import com.yanzhenjie.permission.Rationale;
//import com.yanzhenjie.permission.RequestExecutor;
//
//import java.util.List;
//
//import krt.wid.http.R;
//
///**
// * @author Marcus
// * @package krt.wid.util
// * @description
// * @time 2018/4/4
// */
//
//public class MNewPermissions {
//
//    private static class SingletionHolder {
//        private static final MNewPermissions instance = new MNewPermissions();
//
//    }
//
//    public static MNewPermissions getInstance() {
//        return SingletionHolder.instance;
//    }
//
//    public interface PermissionListener {
//        void onSuccess();
//    }
//
//
//    private MNewPermissions() {
//
//    }
//
//    /**
//     * 请求一个或多个权限组  适合电话 短信等私密权限时的权限申请
//     */
//    public void getPermission(final Context context, final PermissionListener listener, String... permission) {
//        AndPermission.with(context)
//                .runtime()
//                .permission(permission)
//                .rationale(new RuntimeRationale())
//                .onGranted(new Action<List<String>>() {
//                    @Override
//                    public void onAction(List<String> permissions) {
//                        listener.onSuccess();
//                    }
//                }).onDenied(new Action<List<String>>() {
//            @Override
//            public void onAction(List<String> permissions) {
//                dealFail(context, permissions);
//            }
//        }).start();
//    }
//
//    /**
//     * 获取权限组权限  此方法适合获取比较常用的一些权限 入定位 录音 文件读写等权限
//     */
//    public void getGroupPermission(final Context context, String[]... perission) {
//        AndPermission.with(context)
//                .runtime()
//                .permission(perission)
//                .rationale(new RuntimeRationale())
//                .onGranted(new Action<List<String>>() {
//                    @Override
//                    public void onAction(List<String> permissions) {
//
//                    }
//                }).onDenied(new Action<List<String>>() {
//            @Override
//            public void onAction(List<String> permissions) {
//                dealFail(context, permissions);
//            }
//        }).start();
//    }
//
//    private class RuntimeRationale implements Rationale<List<String>> {
//
//        @Override
//        public void showRationale(Context context, List<String> permissions, final RequestExecutor executor) {
//            List<String> permissionNames = Permission.transformText(context, permissions);
//            String message = "请授予以下权限" + "\n" + permissionNames;
//            new AlertDialog.Builder(context).setCancelable(false)
//                    .setTitle("提示")
//                    .setMessage(message)
//                    .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            executor.execute();
//                        }
//                    })
//                    .setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            executor.cancel();
//                        }
//                    })
//                    .show();
//        }
//    }
//
//    /**
//     * 处理当用户选择了不再询问的按钮。 给予提示继续让用户给予相关权限
//     */
//    private void dealFail(Context context, List<String> permissions) {
//        if (AndPermission.hasAlwaysDeniedPermission(context, permissions)) {
//            showSettingDialog(context, permissions);
//        }
//    }
//
//    /**
//     * 跳转到设置的界面
//     *
//     * @param context
//     * @param permissions
//     */
//    public void showSettingDialog(final Context context, final List<String> permissions) {
//        List<String> permissionNames = com.yanzhenjie.permission.runtime.Permission.transformText(context, permissions);
//        String message = context.getString(R.string.message_permission_always_failed,
//                TextUtils.join("\n", permissionNames));
//
//        new AlertDialog.Builder(context).setCancelable(false)
//                .setTitle("提示")
//                .setMessage(message)
//                .setPositiveButton("设置", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        AndPermission.with(context).runtime().setting().start(10001);
//                        dialog.dismiss();
//                    }
//                })
//                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        dialog.dismiss();
//                    }
//                })
//                .show();
//    }
//
//}
