package com.dzt.kit.fragment;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.dzt.androidkit.dialog.BasePopupWindow;
import com.dzt.androidkit.dialog.ScaleViewDlg;
import com.dzt.androidkit.fragment.FragmentBase;
import com.dzt.androidkit.utils.JFileKit;
import com.dzt.androidkit.utils.JLogKit;
import com.dzt.androidkit.utils.JPhotoKit;
import com.dzt.androidkit.utils.JPreferenceKit;
import com.dzt.kit.R;
import com.dzt.kit.contract.UserInfoContract;
import com.dzt.kit.databinding.FragmentUserInfoBinding;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropActivity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by M02323 on 2017/10/19.
 */

public class UserInfoFragment extends FragmentBase<FragmentUserInfoBinding>
		implements UserInfoContract.View {
	/**
	 * 调用相机拍照
	 */
	public static final int CALL_CAMERA = 1;
	/**
	 * 选择图片（单选）标识数据
	 */
	public static final int SELECTIMAGE_ONE = 2;
	private Uri resultUri;
	private BasePopupWindow popupWindow;
	private String current_datetime; // 当前时间
	private String photoPath; // 图片存储路径
	private String photoSavePath; // 图片存储位置
	private boolean isSelectImg = false;
	private UserInfoContract.Presenter presenter;

	@Override
	public int setContent() {
		return R.layout.fragment_user_info;
	}

	@Override
	protected void initWidgets() {
		showContentView();
		bindingView.ivAvatar.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				popupWindow.showAtLocation(bindingView.llUserInfo,
						Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
			}
		});

		bindingView.ivAvatar.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View view) {
				ScaleViewDlg dlg = new ScaleViewDlg(context);
				if (isSelectImg) {
					dlg.setImagePath(photoSavePath);
				} else {
					dlg.setImageUri(resultUri);
				}
				dlg.show();
				return false;
			}
		});
		initPath();
		initPopupWindow();
		roadImageView(resultUri);
	}

	@Override
	protected void initData() {
		String path = JPreferenceKit.getInstance().getStringValue("logo_img", "logo_img");
		if (TextUtils.isEmpty(path) || path.equals("logo_img")) {
			Resources r = getResources();
			resultUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://"
					+ r.getResourcePackageName(R.drawable.pic_loading_round) + "/"
					+ r.getResourceTypeName(R.drawable.pic_loading_round) + "/"
					+ r.getResourceEntryName(R.drawable.pic_loading_round));
		} else {
			resultUri = Uri.fromFile(new File(path));
		}
	}

	@Override
	public void setPresenter(UserInfoContract.Presenter presenter) {
		this.presenter = presenter;
	}

	@Override
	public void showPopupWindow() {

	}

	private void initPath() {
		//路径规则：SD卡路径（内部存储）/packageName/no_upload_media/yyyyMMddHHmmss.jpg
		photoPath = JFileKit.getDiskCacheDir(context) + "/upload_media";
		// 查询并创建文件夹
		JFileKit.createFolder(photoPath);
	}

	private void initPopupWindow() {
		popupWindow = new BasePopupWindow(context);
		popupWindow.setAnimationStyle(R.style.style_bottom_window_animation);
		View view = LayoutInflater.from(context).
				inflate(R.layout.camera_popupwindow, new LinearLayout(context), false);
		view.findViewById(R.id.btn_camera).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				popupWindow.dismiss();
				callCameraTakePhoto();
			}
		});
		view.findViewById(R.id.btn_photo_gallery).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				popupWindow.dismiss();
				// 图片单选，直接跳转至系统图片库
				Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(intent, SELECTIMAGE_ONE);
			}
		});
		view.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				popupWindow.dismiss();
			}
		});
		popupWindow.setContentView(view);
	}

	/**
	 * 调用相机拍照并存储照片
	 */
	private void callCameraTakePhoto() {
		Date currentDate = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINESE);
		current_datetime = sdf.format(currentDate); // 初始化当前时间值

		String name = current_datetime + ".jpg"; // 初始化图片文件名
		photoSavePath = photoPath + File.separator + name; // 初始化文件夹位置
		JLogKit.getInstance().e("path", photoSavePath);

		// 启动相机并拍照
		Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(photoSavePath)));
		startActivityForResult(intent, CALL_CAMERA);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == CALL_CAMERA) {
				if (data == null) {
					isSelectImg = true;
					JLogKit.getInstance().e("photoSavePath = " + photoSavePath);
					initUCrop(Uri.fromFile(new File(photoSavePath)));
				}
			} else if (requestCode == SELECTIMAGE_ONE) {
				if (data != null) {
					// 获取相册选择结果（保存路径）
					Uri selectedImage = data.getData();
					initUCrop(selectedImage);
				}
			} else if (requestCode == UCrop.REQUEST_CROP) {
				//剪切之后都会返回这里，在这里保存图片与显示图片
				isSelectImg = true;
				resultUri = UCrop.getOutput(data);
				roadImageView(resultUri);
			}
		}
	}

	//从Uri中加载图片 并将其转化成File文件返回
	private void roadImageView(Uri uri) {
		Glide.with(context).
				load(uri).
				diskCacheStrategy(DiskCacheStrategy.RESULT).
				bitmapTransform(new CropCircleTransformation(context)).
				thumbnail(0.5f).
				placeholder(R.mipmap.ic_launcher).
				priority(Priority.LOW).
				error(R.drawable.pic_loading).
				fallback(R.mipmap.ic_launcher).
				into(bindingView.ivAvatar);
		photoSavePath = JPhotoKit.getImageAbsolutePath(context, uri);
		JLogKit.getInstance().i("roadImageView photoSavePath = " + photoSavePath);
		JPreferenceKit.getInstance().setStringValue("logo_img", photoSavePath);
	}

	private void initUCrop(Uri uri) {
		Date currentDate = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINESE);
		current_datetime = sdf.format(currentDate); // 初始化当前时间值
		String name = current_datetime + ".jpg"; // 初始化图片文件名
		photoSavePath = photoPath + File.separator + name; // 初始化文件夹位置

		Uri destinationUri = Uri.fromFile(new File(photoPath, name));

		UCrop.Options options = new UCrop.Options();
		//设置裁剪图片可操作的手势
		options.setAllowedGestures(UCropActivity.SCALE, UCropActivity.ROTATE, UCropActivity.ALL);
		//设置隐藏底部容器，默认显示
		//options.setHideBottomControls(true);
		//设置toolbar颜色
		options.setToolbarColor(ActivityCompat.getColor(context, R.color.colorPrimary));
		//设置状态栏颜色
		options.setStatusBarColor(ActivityCompat.getColor(context, R.color.colorPrimaryDark));

		//开始设置
		//设置最大缩放比例
		options.setMaxScaleMultiplier(5);
		//设置图片在切换比例时的动画
		options.setImageToCropBoundsAnimDuration(666);
		//设置裁剪窗口是否为椭圆
		//options.setOvalDimmedLayer(true);
		//设置是否展示矩形裁剪框
		// options.setShowCropFrame(false);
		//设置裁剪框横竖线的宽度
		//options.setCropGridStrokeWidth(20);
		//设置裁剪框横竖线的颜色
		//options.setCropGridColor(Color.GREEN);
		//设置竖线的数量
		//options.setCropGridColumnCount(2);
		//设置横线的数量
		//options.setCropGridRowCount(1);

		UCrop.of(uri, destinationUri)
				.withAspectRatio(1, 1)
				.withMaxResultSize(1000, 1000)
				.withOptions(options)
				.start(context);
	}
}
