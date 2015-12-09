package cn.six.open.view.customactionsheet;


import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;

public class CustomActionSheet extends View implements View.OnClickListener {
    private static final int TRANSLATE_DURATION = 200;
    private static final int ALPHA_DURATION = 300;
    public static final int BG_VIEW_ID = 1200;

    private final Activity actv;
    private FrameLayout wholeView;
    private View bgView, contentView;
    private ViewGroup decorView;

    public CustomActionSheet(Activity actv) {
        super(actv);
        this.actv = actv;
    }

    public void show(View sheetContentView){
        dismissInputMethod();

        wholeView = createView(sheetContentView); // init the bgView and flayContent, too
        decorView = (ViewGroup) actv.getWindow().getDecorView();
        decorView.addView(wholeView);

        bgView.startAnimation(AnimationUtil.createAlphaInAnimation(ALPHA_DURATION));
        contentView.startAnimation(AnimationUtil.createTranslationInAnimation(TRANSLATE_DURATION));


    }

    public void dismissInputMethod(){
        InputMethodManager imm = (InputMethodManager) actv.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            View focusView = actv.getCurrentFocus();
            if (focusView != null) {
                imm.hideSoftInputFromWindow(focusView.getWindowToken(), 0);
            }
        }
    }

    public FrameLayout createView(View content){
        FrameLayout parent = new FrameLayout(actv);
        parent.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        bgView = new View(actv);
        bgView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        bgView.setBackgroundColor(Color.argb(136, 0, 0, 0));
        bgView.setOnClickListener(this);
        bgView.setId(BG_VIEW_ID);

        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.BOTTOM;
        content.setLayoutParams(lp);
        contentView = content;

        parent.addView(bgView);
        parent.addView(content);

        return parent;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == BG_VIEW_ID){
            dismiss();
        }
    }

    public void dismiss(){
        if(contentView != null){
            contentView.startAnimation(AnimationUtil.createTranslationOutAnimation(TRANSLATE_DURATION));
            bgView.startAnimation(AnimationUtil.createAlphaOutAnimation(ALPHA_DURATION));
            wholeView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    wholeView.removeAllViews();
                    decorView.removeView(wholeView);
                }
            }, ALPHA_DURATION);
        }
    }


}