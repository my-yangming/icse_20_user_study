package com.yydcdut.markdowndemo.controller;

import android.text.Editable;
import android.widget.Toast;

import com.yydcdut.markdown.MarkdownEditText;

/**
 * Created by yuyidong on 16/7/14.
 */
public class CodeController {
    private MarkdownEditText mRxMDEditText;

    public CodeController(MarkdownEditText rxMDEditText) {
        mRxMDEditText = rxMDEditText;
    }

    public void doInlineCode() {
        int start = mRxMDEditText.getSelectionStart();
        int end = mRxMDEditText.getSelectionEnd();
        if (start == end) {
            mRxMDEditText.getText().insert(start, "``");
            mRxMDEditText.setSelection(start + 1, end + 1);
        } else if (end - start > 2) {//选中了4个以上
            int position0 = Utils.findBeforeNewLineChar(mRxMDEditText.getText(), start) + 1;
            int position00 = Utils.findBeforeNewLineChar(mRxMDEditText.getText(), end) + 1;
            if (position0 != position00) {
                Toast.makeText(mRxMDEditText.getContext(), "无法�?作多行", Toast.LENGTH_SHORT).show();
                return;
            }
            Editable editable = mRxMDEditText.getText();
            if ("`".equals(editable.subSequence(Utils.safePosition(start, editable), Utils.safePosition(start + "`".length(), editable)).toString()) &&
                    "`".equals(editable.subSequence(Utils.safePosition(end - "`".length(), editable), Utils.safePosition(end, editable)).toString())) {
                mRxMDEditText.getText().delete(end - "`".length(), end);
                mRxMDEditText.getText().delete(start, start + "`".length());
                mRxMDEditText.setSelection(start, end - "`".length() * 2);
            } else {
                mRxMDEditText.getText().insert(end, "`");
                mRxMDEditText.getText().insert(start, "`");
                mRxMDEditText.setSelection(start, end + "`".length() * 2);
            }
        } else {
            mRxMDEditText.getText().insert(end, "`");
            mRxMDEditText.getText().insert(start, "`");
            mRxMDEditText.setSelection(start, end + "`".length() * 2);
        }
    }

    public void doCode() {
        int start = mRxMDEditText.getSelectionStart();
        int end = mRxMDEditText.getSelectionEnd();
        if (start == end) {
            int position0 = Utils.findBeforeNewLineChar(mRxMDEditText.getText(), start) + 1;
            int position1 = Utils.findNextNewLineChar(mRxMDEditText.getText(), end);
            if (position1 == -1) {
                position1 = mRxMDEditText.length();
            }
            Editable editable = mRxMDEditText.getText();
            if (position0 >= 4 && position1 < mRxMDEditText.length() - 4) {
                boolean begin = "```".equals(editable.subSequence(Utils.safePosition(position0 - 1 - "```".length(), editable), Utils.safePosition(position0 - 1, editable)).toString());
                if (begin && "```\n".equals(editable.subSequence(Utils.safePosition(position1 + 1, editable), Utils.safePosition(position1 + 1 + "```\n".length(), editable)).toString())) {
                    mRxMDEditText.getText().delete(position1 + 1, position1 + 1 + "```\n".length());
                    mRxMDEditText.getText().delete(position0 - "\n```".length(), position0);
                    return;
                }
            }

            int selectedStart = mRxMDEditText.getSelectionStart();
            char c = mRxMDEditText.getText().charAt(position1 >= mRxMDEditText.length() ? mRxMDEditText.length() - 1 : position1);
            if (c == '\n') {
                mRxMDEditText.getText().insert(position1, "\n```");
            } else {
                mRxMDEditText.getText().insert(position1, "\n```\n");
            }
            mRxMDEditText.getText().insert(position0, "```\n");
            mRxMDEditText.setSelection(selectedStart + "```\n".length(), selectedStart + "```\n".length());
        } else if (end - start > 6) {
            Editable editable = mRxMDEditText.getText();
            if ("```".equals(editable.subSequence(Utils.safePosition(start, editable), Utils.safePosition(start + "```".length(), editable)).toString()) &&
                    "```".equals(editable.subSequence(Utils.safePosition(end - "```".length(), editable), Utils.safePosition(end, editable)).toString())) {
                int selectedStart = mRxMDEditText.getSelectionStart();
                int selectedEnd = mRxMDEditText.getSelectionEnd();
                mRxMDEditText.getText().delete(end - "\n```".length(), end);
                mRxMDEditText.getText().delete(start, start + "```\n".length());
                mRxMDEditText.setSelection(selectedStart, selectedEnd - 8);
                return;
            }

            code(start, end);
        } else {
            code(start, end);
        }
    }

    private void code(int start, int end) {
        int selectedStart = mRxMDEditText.getSelectionStart();
        int selectedEnd = mRxMDEditText.getSelectionEnd();
        int endAdd = 0;
        char c = mRxMDEditText.getText().charAt(end >= mRxMDEditText.length() ? mRxMDEditText.length() - 1 : end);
        if (c == '\n') {
            mRxMDEditText.getText().insert(end, "\n```");
            endAdd += 4;
        } else {
            mRxMDEditText.getText().insert(end, "\n```\n");
            endAdd += 5;
            selectedStart = selectedStart + 1;
        }
        char c1 = mRxMDEditText.getText().charAt(start - 1 < 0 ? 0 : start - 1);
        if (c1 == '\n' || start - 1 < 0) {
            mRxMDEditText.getText().insert(start, "```\n");
            endAdd += 4;
        } else {
            mRxMDEditText.getText().insert(start, "\n```\n");
            endAdd += 4;
        }
        mRxMDEditText.setSelection(selectedStart, selectedEnd + endAdd);
    }
}
