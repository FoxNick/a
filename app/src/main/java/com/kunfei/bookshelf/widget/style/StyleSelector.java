package com.kunfei.bookshelf.widget.style;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.kunfei.bookshelf.R;
import com.kunfei.bookshelf.help.DocumentHelper;
import com.kunfei.bookshelf.help.ReadBookControl;
import com.monke.mprogressbar.MHorProgressBar;
import com.monke.mprogressbar.OnProgressListener;

import java.io.File;

public class StyleSelector {
    private AlertDialog.Builder builder;
    private Context context;
    private String fontPath;
    private OnThisListener thisListener;
    private AlertDialog alertDialog;
    private TextView tv_label_1;
    private TextView tv_label_2;
    private TextView tv_label_3;
    private TextView tv_label_4;
    private TextView tv_label_5;
    private TextView tv_label_6;
    private TextView tv_label_7;
    private TextView tv_label_8;
    private TextView tv_label_9;
    private TextView tv_label_10;

    private ReadBookControl readBookControl = ReadBookControl.getInstance();


    public StyleSelector(Context context, String selectPath) {
        builder = new AlertDialog.Builder(context, R.style.alertDialogThemeRead);
        @SuppressLint("InflateParams") View view = LayoutInflater.from(context).inflate(R.layout.view_more_style, null);

        //----------------------行间距---------------------------//
        MHorProgressBar hpb_read_progress_1 = view.findViewById(R.id.hpb_read_progress_1);
        hpb_read_progress_1.setMaxProgress(100);
        hpb_read_progress_1.setDurProgress(readBookControl.getLineMultiplier());
        hpb_read_progress_1.setSpeed(1);
        tv_label_1 =  view.findViewById(R.id.tv_label_1);
        TextView tv_pre_1 =  view.findViewById(R.id.tv_pre_1);
        TextView tv_next_1 =  view.findViewById(R.id.tv_next_1);
        setTvLable_1();

        //减
        tv_pre_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //dialog.dismiss();
                hpb_read_progress_1.setDurProgress(readBookControl.getLineMultiplier()-1);

                if (thisListener != null) {
                    thisListener.setLineMultiplier(readBookControl.getLineMultiplier()-1);
                    setTvLable_1();
                }
            }
        });

        //加
        tv_next_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //dialog.dismiss();
                hpb_read_progress_1.setDurProgress(readBookControl.getLineMultiplier()+1);
                if (thisListener != null) {
                    thisListener.setLineMultiplier(readBookControl.getLineMultiplier()+1);
                    setTvLable_1();
                }
            }
        });

        //阅读进度
        hpb_read_progress_1.setProgressListener(new OnProgressListener() {
            @Override
            public void moveStartProgress(float dur) {

            }

            @Override
            public void durProgressChange(float dur) {

            }

            @Override
            public void moveStopProgress(float dur) {
                //float realDur = (float) Math.ceil(dur);
                String realDurStr = (new java.text.DecimalFormat("#").format(dur));
                float realDur = Float.parseFloat(realDurStr);
                if (hpb_read_progress_1.getDurProgress() != realDur)
                    hpb_read_progress_1.setDurProgress(realDur);

                if (thisListener != null) {
                    thisListener.setLineMultiplier(realDur);
                    setTvLable_1();
                }
            }

            @Override
            public void setDurProgress(float dur) {

            }
        });


	//------------------------段间距-------------------------//
		MHorProgressBar hpb_read_progress_2 = view.findViewById(R.id.hpb_read_progress_2);
        hpb_read_progress_2.setMaxProgress(200);
        hpb_read_progress_2.setDurProgress(readBookControl.getParagraphSize());
        hpb_read_progress_2.setSpeed(1);
        tv_label_2 =  view.findViewById(R.id.tv_label_2);
        TextView tv_pre_2 =  view.findViewById(R.id.tv_pre_2);
        TextView tv_next_2 =  view.findViewById(R.id.tv_next_2);
        setTvLable_2();

        //减
        tv_pre_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //dialog.dismiss();
                hpb_read_progress_2.setDurProgress(readBookControl.getParagraphSize()-1);

                if (thisListener != null) {
                    thisListener.setParagraphSize(readBookControl.getParagraphSize()-1);
                    setTvLable_2();
                }
            }
        });

        //加
        tv_next_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //dialog.dismiss();
                hpb_read_progress_2.setDurProgress(readBookControl.getParagraphSize()+1);
                if (thisListener != null) {
                    thisListener.setParagraphSize(readBookControl.getParagraphSize()+1);
                    setTvLable_2();
                }
            }
        });

        //阅读进度
        hpb_read_progress_2.setProgressListener(new OnProgressListener() {
            @Override
            public void moveStartProgress(float dur) {

            }

            @Override
            public void durProgressChange(float dur) {

            }

            @Override
            public void moveStopProgress(float dur) {
                //float realDur = (float) Math.ceil(dur);
                String realDurStr = (new java.text.DecimalFormat("#").format(dur));
                float realDur = Float.parseFloat(realDurStr);
                if (hpb_read_progress_2.getDurProgress() != realDur)
                    hpb_read_progress_2.setDurProgress(realDur);

                if (thisListener != null) {
                    thisListener.setParagraphSize(realDur);
                    setTvLable_2();
                }
            }

            @Override
            public void setDurProgress(float dur) {

            }
        });

	//------------------------上边距-------------------------//
	MHorProgressBar hpb_read_progress_3 = view.findViewById(R.id.hpb_read_progress_3);
        hpb_read_progress_3.setMaxProgress(50);
        hpb_read_progress_3.setDurProgress(readBookControl.getPaddingTop());
        hpb_read_progress_3.setSpeed(1);
        tv_label_3 =  view.findViewById(R.id.tv_label_3);
        TextView tv_pre_3 =  view.findViewById(R.id.tv_pre_3);
        TextView tv_next_3 =  view.findViewById(R.id.tv_next_3);
        setTvLable_3();

        //减
        tv_pre_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //dialog.dismiss();
                hpb_read_progress_3.setDurProgress(readBookControl.getPaddingTop()-1);

                if (thisListener != null) {
                    thisListener.setPaddingTop(readBookControl.getPaddingTop()-1);
                    setTvLable_3();
                }
            }
        });

        //加
        tv_next_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //dialog.dismiss();
                hpb_read_progress_3.setDurProgress(readBookControl.getPaddingTop()+1);
                if (thisListener != null) {
                    thisListener.setPaddingTop(readBookControl.getPaddingTop()+1);
                    setTvLable_3();
                }
            }
        });

        //阅读进度
        hpb_read_progress_3.setProgressListener(new OnProgressListener() {
            @Override
            public void moveStartProgress(float dur) {

            }

            @Override
            public void durProgressChange(float dur) {

            }

            @Override
            public void moveStopProgress(float dur) {
                //float realDur = (float) Math.ceil(dur);
                String realDurStr = (new java.text.DecimalFormat("#00").format(dur));
                int realDur = Integer.parseInt(realDurStr);
                if (hpb_read_progress_3.getDurProgress() != realDur)
                    hpb_read_progress_3.setDurProgress(realDur);

                if (thisListener != null) {
                    thisListener.setPaddingTop(realDur);
                    setTvLable_3();
                }
            }

            @Override
            public void setDurProgress(float dur) {

            }
        });
        
	//---------------------下边距----------------------------//
	MHorProgressBar hpb_read_progress_4 = view.findViewById(R.id.hpb_read_progress_4);
        hpb_read_progress_4.setMaxProgress(50);
        hpb_read_progress_4.setDurProgress(readBookControl.getPaddingBottom());
        hpb_read_progress_4.setSpeed(1);
        tv_label_4 =  view.findViewById(R.id.tv_label_4);
        TextView tv_pre_4 =  view.findViewById(R.id.tv_pre_4);
        TextView tv_next_4 =  view.findViewById(R.id.tv_next_4);
        setTvLable_4();

        //减
        tv_pre_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //dialog.dismiss();
                hpb_read_progress_4.setDurProgress(readBookControl.getPaddingBottom()-1);

                if (thisListener != null) {
                    thisListener.setPaddingBottom(readBookControl.getPaddingBottom()-1);
                    setTvLable_4();
                }
            }
        });

        //加
        tv_next_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //dialog.dismiss();
                hpb_read_progress_4.setDurProgress(readBookControl.getPaddingBottom()+1);
                if (thisListener != null) {
                    thisListener.setPaddingBottom(readBookControl.getPaddingBottom()+1);
                    setTvLable_4();
                }
            }
        });

        //阅读进度
        hpb_read_progress_4.setProgressListener(new OnProgressListener() {
            @Override
            public void moveStartProgress(float dur) {

            }

            @Override
            public void durProgressChange(float dur) {

            }

            @Override
            public void moveStopProgress(float dur) {
                //float realDur = (float) Math.ceil(dur);
                String realDurStr = (new java.text.DecimalFormat("#00").format(dur));
                int realDur = Integer.parseInt(realDurStr);
                if (hpb_read_progress_4.getDurProgress() != realDur)
                    hpb_read_progress_4.setDurProgress(realDur);

                if (thisListener != null) {
                    thisListener.setPaddingBottom(realDur);
                    setTvLable_4();
                }
            }

            @Override
            public void setDurProgress(float dur) {

            }
        });
	//------------------------左边距-------------------------//
	MHorProgressBar hpb_read_progress_5 = view.findViewById(R.id.hpb_read_progress_5);
        hpb_read_progress_5.setMaxProgress(50);
        hpb_read_progress_5.setDurProgress(readBookControl.getPaddingLeft());
        hpb_read_progress_5.setSpeed(1);
        tv_label_5 =  view.findViewById(R.id.tv_label_5);
        TextView tv_pre_5 =  view.findViewById(R.id.tv_pre_5);
        TextView tv_next_5 =  view.findViewById(R.id.tv_next_5);
        setTvLable_5();

        //减
        tv_pre_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //dialog.dismiss();
                hpb_read_progress_5.setDurProgress(readBookControl.getPaddingLeft()-1);

                if (thisListener != null) {
                    thisListener.setPaddingLeft(readBookControl.getPaddingLeft()-1);
                    setTvLable_5();
                }
            }
        });

        //加
        tv_next_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //dialog.dismiss();
                hpb_read_progress_5.setDurProgress(readBookControl.getPaddingLeft()+1);
                if (thisListener != null) {
                    thisListener.setPaddingLeft(readBookControl.getPaddingLeft()+1);
                    setTvLable_5();
                }
            }
        });

        //阅读进度
        hpb_read_progress_5.setProgressListener(new OnProgressListener() {
            @Override
            public void moveStartProgress(float dur) {

            }

            @Override
            public void durProgressChange(float dur) {

            }

            @Override
            public void moveStopProgress(float dur) {
                //float realDur = (float) Math.ceil(dur);
                String realDurStr = (new java.text.DecimalFormat("#00").format(dur));
                int realDur = Integer.parseInt(realDurStr);
                if (hpb_read_progress_5.getDurProgress() != realDur)
                    hpb_read_progress_5.setDurProgress(realDur);

                if (thisListener != null) {
                    thisListener.setPaddingLeft(realDur);
                    setTvLable_5();
                }
            }

            @Override
            public void setDurProgress(float dur) {

            }
        });
	//---------------------右边距----------------------------//
        MHorProgressBar hpb_read_progress_6 = view.findViewById(R.id.hpb_read_progress_6);
        hpb_read_progress_6.setMaxProgress(50);
        hpb_read_progress_6.setDurProgress(readBookControl.getPaddingRight());
        hpb_read_progress_6.setSpeed(1);
        tv_label_6 =  view.findViewById(R.id.tv_label_6);
        TextView tv_pre_6 =  view.findViewById(R.id.tv_pre_6);
        TextView tv_next_6 =  view.findViewById(R.id.tv_next_6);
        setTvLable_6();

        //减
        tv_pre_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //dialog.dismiss();
                hpb_read_progress_6.setDurProgress(readBookControl.getPaddingRight()-1);

                if (thisListener != null) {
                    thisListener.setPaddingRight(readBookControl.getPaddingRight()-1);
                    setTvLable_6();
                }
            }
        });

        //加
        tv_next_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //dialog.dismiss();
                hpb_read_progress_6.setDurProgress(readBookControl.getPaddingRight()+1);
                if (thisListener != null) {
                    thisListener.setPaddingRight(readBookControl.getPaddingRight()+1);
                    setTvLable_6();
                }
            }
        });

        //阅读进度
        hpb_read_progress_6.setProgressListener(new OnProgressListener() {
            @Override
            public void moveStartProgress(float dur) {

            }

            @Override
            public void durProgressChange(float dur) {

            }

            @Override
            public void moveStopProgress(float dur) {
                //float realDur = (float) Math.ceil(dur);
                String realDurStr = (new java.text.DecimalFormat("#00").format(dur));
                int realDur = Integer.parseInt(realDurStr);
                if (hpb_read_progress_6.getDurProgress() != realDur)
                    hpb_read_progress_6.setDurProgress(realDur);

                if (thisListener != null) {
                    thisListener.setPaddingRight(realDur);
                    setTvLable_6();
                }
            }

            @Override
            public void setDurProgress(float dur) {

            }
        });


        //---------------------Tip上边距----------------------------//
        MHorProgressBar hpb_read_progress_7 = view.findViewById(R.id.hpb_read_progress_7);
        hpb_read_progress_7.setMaxProgress(50);
        hpb_read_progress_7.setDurProgress(readBookControl.getTipPaddingTop());
        hpb_read_progress_7.setSpeed(1);
        tv_label_7 =  view.findViewById(R.id.tv_label_7);
        TextView tv_pre_7 =  view.findViewById(R.id.tv_pre_7);
        TextView tv_next_7 =  view.findViewById(R.id.tv_next_7);
        setTvLable_7();

        //减
        tv_pre_7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //dialog.dismiss();
                hpb_read_progress_7.setDurProgress(readBookControl.getTipPaddingTop()-1);

                if (thisListener != null) {
                    thisListener.setTipPaddingTop(readBookControl.getTipPaddingTop()-1);
                    setTvLable_7();
                }
            }
        });

        //加
        tv_next_7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //dialog.dismiss();
                hpb_read_progress_7.setDurProgress(readBookControl.getTipPaddingTop()+1);
                if (thisListener != null) {
                    thisListener.setTipPaddingTop(readBookControl.getTipPaddingTop()+1);
                    setTvLable_7();
                }
            }
        });

        //阅读进度
        hpb_read_progress_7.setProgressListener(new OnProgressListener() {
            @Override
            public void moveStartProgress(float dur) {

            }

            @Override
            public void durProgressChange(float dur) {

            }

            @Override
            public void moveStopProgress(float dur) {
                //float realDur = (float) Math.ceil(dur);
                String realDurStr = (new java.text.DecimalFormat("#00").format(dur));
                int realDur = Integer.parseInt(realDurStr);
                if (hpb_read_progress_7.getDurProgress() != realDur)
                    hpb_read_progress_7.setDurProgress(realDur);

                if (thisListener != null) {
                    thisListener.setTipPaddingTop(realDur);
                    setTvLable_7();
                }
            }

            @Override
            public void setDurProgress(float dur) {

            }
        });


        //---------------------Tip下边距----------------------------//
        MHorProgressBar hpb_read_progress_8 = view.findViewById(R.id.hpb_read_progress_8);
        hpb_read_progress_8.setMaxProgress(50);
        hpb_read_progress_8.setDurProgress(readBookControl.getTipPaddingBottom());
        hpb_read_progress_8.setSpeed(1);
        tv_label_8 =  view.findViewById(R.id.tv_label_8);
        TextView tv_pre_8 =  view.findViewById(R.id.tv_pre_8);
        TextView tv_next_8 =  view.findViewById(R.id.tv_next_8);
        setTvLable_8();

        //减
        tv_pre_8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //dialog.dismiss();
                hpb_read_progress_8.setDurProgress(readBookControl.getTipPaddingBottom()-1);

                if (thisListener != null) {
                    thisListener.setTipPaddingBottom(readBookControl.getTipPaddingBottom()-1);
                    setTvLable_8();
                }
            }
        });

        //加
        tv_next_8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //dialog.dismiss();
                hpb_read_progress_8.setDurProgress(readBookControl.getTipPaddingBottom()+1);
                if (thisListener != null) {
                    thisListener.setTipPaddingBottom(readBookControl.getTipPaddingBottom()+1);
                    setTvLable_8();
                }
            }
        });

        //阅读进度
        hpb_read_progress_8.setProgressListener(new OnProgressListener() {
            @Override
            public void moveStartProgress(float dur) {

            }

            @Override
            public void durProgressChange(float dur) {

            }

            @Override
            public void moveStopProgress(float dur) {
                //float realDur = (float) Math.ceil(dur);
                String realDurStr = (new java.text.DecimalFormat("#00").format(dur));
                int realDur = Integer.parseInt(realDurStr);
                if (hpb_read_progress_8.getDurProgress() != realDur)
                    hpb_read_progress_8.setDurProgress(realDur);

                if (thisListener != null) {
                    thisListener.setTipPaddingBottom(realDur);
                    setTvLable_8();
                }
            }

            @Override
            public void setDurProgress(float dur) {

            }
        });

        //---------------------Tip左边距----------------------------//
        MHorProgressBar hpb_read_progress_9 = view.findViewById(R.id.hpb_read_progress_9);
        hpb_read_progress_9.setMaxProgress(50);
        hpb_read_progress_9.setDurProgress(readBookControl.getTipPaddingLeft());
        hpb_read_progress_9.setSpeed(1);
        tv_label_9 =  view.findViewById(R.id.tv_label_9);
        TextView tv_pre_9 =  view.findViewById(R.id.tv_pre_9);
        TextView tv_next_9 =  view.findViewById(R.id.tv_next_9);
        setTvLable_9();

        //减
        tv_pre_9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //dialog.dismiss();
                hpb_read_progress_9.setDurProgress(readBookControl.getTipPaddingLeft()-1);

                if (thisListener != null) {
                    thisListener.setTipPaddingLeft(readBookControl.getTipPaddingLeft()-1);
                    setTvLable_9();
                }
            }
        });

        //加
        tv_next_9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //dialog.dismiss();
                hpb_read_progress_9.setDurProgress(readBookControl.getTipPaddingLeft()+1);
                if (thisListener != null) {
                    thisListener.setTipPaddingLeft(readBookControl.getTipPaddingLeft()+1);
                    setTvLable_9();
                }
            }
        });

        //阅读进度
        hpb_read_progress_9.setProgressListener(new OnProgressListener() {
            @Override
            public void moveStartProgress(float dur) {

            }

            @Override
            public void durProgressChange(float dur) {

            }

            @Override
            public void moveStopProgress(float dur) {
                //float realDur = (float) Math.ceil(dur);
                String realDurStr = (new java.text.DecimalFormat("#00").format(dur));
                int realDur = Integer.parseInt(realDurStr);
                if (hpb_read_progress_9.getDurProgress() != realDur)
                    hpb_read_progress_9.setDurProgress(realDur);

                if (thisListener != null) {
                    thisListener.setTipPaddingLeft(realDur);
                    setTvLable_9();
                }
            }

            @Override
            public void setDurProgress(float dur) {

            }
        });

        //---------------------Tip右边距----------------------------//
        MHorProgressBar hpb_read_progress_10 = view.findViewById(R.id.hpb_read_progress_10);
        hpb_read_progress_10.setMaxProgress(50);
        hpb_read_progress_10.setDurProgress(readBookControl.getTipPaddingRight());
        hpb_read_progress_10.setSpeed(1);
        tv_label_10 =  view.findViewById(R.id.tv_label_10);
        TextView tv_pre_10 =  view.findViewById(R.id.tv_pre_10);
        TextView tv_next_10 =  view.findViewById(R.id.tv_next_10);
        setTvLable_10();

        //减
        tv_pre_10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //dialog.dismiss();
                hpb_read_progress_10.setDurProgress(readBookControl.getTipPaddingRight()-1);

                if (thisListener != null) {
                    thisListener.setTipPaddingRight(readBookControl.getTipPaddingRight()-1);
                    setTvLable_10();
                }
            }
        });

        //加
        tv_next_10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //dialog.dismiss();
                hpb_read_progress_10.setDurProgress(readBookControl.getTipPaddingRight()+1);
                if (thisListener != null) {
                    thisListener.setTipPaddingRight(readBookControl.getTipPaddingRight()+1);
                    setTvLable_10();
                }
            }
        });

        //阅读进度
        hpb_read_progress_10.setProgressListener(new OnProgressListener() {
            @Override
            public void moveStartProgress(float dur) {

            }

            @Override
            public void durProgressChange(float dur) {

            }

            @Override
            public void moveStopProgress(float dur) {
                //float realDur = (float) Math.ceil(dur);
                String realDurStr = (new java.text.DecimalFormat("#00").format(dur));
                int realDur = Integer.parseInt(realDurStr);
                if (hpb_read_progress_10.getDurProgress() != realDur)
                    hpb_read_progress_10.setDurProgress(realDur);

                if (thisListener != null) {
                    thisListener.setTipPaddingRight(realDur);
                    setTvLable_10();
                }
            }

            @Override
            public void setDurProgress(float dur) {

            }
        });

        builder.setView(view);
        builder.setTitle(R.string.select_style);
        builder.setNegativeButton(R.string.jf_convert_o, null);
        //fontPath = FileUtil.getSdCardPath() + "/Fonts";

    }

    private void setTvLable_1(){
        float dur = readBookControl.getLineMultiplier();
        String realDurStr = (new java.text.DecimalFormat("#00").format(dur));
        tv_label_1.setText("行间距("+realDurStr+"):");
    }

    private void setTvLable_2(){
        float dur = readBookControl.getParagraphSize();
        String realDurStr = (new java.text.DecimalFormat("#00").format(dur));
        tv_label_2.setText("段间距("+realDurStr+"):");
    }

    private void setTvLable_3(){
        float dur = readBookControl.getPaddingTop();
        String realDurStr = (new java.text.DecimalFormat("#00").format(dur));
        tv_label_3.setText("上边距("+realDurStr+"):");
    }

    private void setTvLable_4(){
        float dur = readBookControl.getPaddingBottom();
        String realDurStr = (new java.text.DecimalFormat("#00").format(dur));
        tv_label_4.setText("下边距("+realDurStr+"):");
    }

    private void setTvLable_5(){
        float dur = readBookControl.getPaddingLeft();
        String realDurStr = (new java.text.DecimalFormat("#00").format(dur));
        tv_label_5.setText("左边距("+realDurStr+"):");
    }

    private void setTvLable_6(){
        float dur = readBookControl.getPaddingRight();
        String realDurStr = (new java.text.DecimalFormat("#00").format(dur));
        tv_label_6.setText("右边距("+realDurStr+"):");
    }

    private void setTvLable_7(){
        float dur = readBookControl.getTipPaddingTop();
        String realDurStr = (new java.text.DecimalFormat("#00").format(dur));
        tv_label_7.setText("Tip上边距("+realDurStr+"):");
    }

    private void setTvLable_8(){
        float dur = readBookControl.getTipPaddingBottom();
        String realDurStr = (new java.text.DecimalFormat("#00").format(dur));
        tv_label_8.setText("Tip下边距("+realDurStr+"):");
    }

    private void setTvLable_9(){
        float dur = readBookControl.getTipPaddingLeft();
        String realDurStr = (new java.text.DecimalFormat("#00").format(dur));
        tv_label_9.setText("Tip左边距("+realDurStr+"):");
    }

    private void setTvLable_10(){
        float dur = readBookControl.getTipPaddingRight();
        String realDurStr = (new java.text.DecimalFormat("#00").format(dur));
        tv_label_10.setText("Tip右边距("+realDurStr+"):");
    }

    public StyleSelector setListener(OnThisListener thisListener) {
        this.thisListener = thisListener;
        //builder.setPositiveButton(R.string.default_style, ((dialogInterface, i) -> thisListener.setDefault()));
        return this;
    }

    public StyleSelector setPath(String path) {
        fontPath = path;
        return this;
    }



    public StyleSelector create() {
       // adapter.upData(getFontFiles());
        builder.create();
        return this;
    }

    public void show() {
        alertDialog = builder.show();
    }

    private File[] getFontFiles() {
        try {
            DocumentHelper.createDirIfNotExist(fontPath);
            File file = new File(fontPath);
            return file.listFiles(pathName -> pathName.getName().toLowerCase().matches(".*\\.[ot]tf"));
        } catch (Exception e) {
            return null;
        }
    }

    public interface OnThisListener {
        void setLineMultiplier(float number);
        
        void setParagraphSize(float number);

        void setPaddingTop(int number);

        void setPaddingBottom(int number);

        void setPaddingLeft(int number);

        void setPaddingRight(int number);

        void setTipPaddingTop(int number);

        void setTipPaddingBottom(int number);

        void setTipPaddingLeft(int number);

        void setTipPaddingRight(int number);

    }
}
