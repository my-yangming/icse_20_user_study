/*
 * Copyright (C) 2016 yydcdut (yuyidong2015@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.yydcdut.markdown.syntax.text;

import android.support.annotation.NonNull;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;

import com.yydcdut.markdown.MarkdownConfiguration;
import com.yydcdut.markdown.callback.OnLinkClickCallback;
import com.yydcdut.markdown.span.MDURLSpan;
import com.yydcdut.markdown.syntax.SyntaxKey;
import com.yydcdut.markdown.utils.CharacterProtector;
import com.yydcdut.markdown.utils.SyntaxUtils;
import com.yydcdut.markdown.utils.TextHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The implementation of syntax for hyper link.
 * syntax:
 * "[content](http://link.html)"
 * <p>
 * Created by yuyidong on 16/5/14.
 */
class HyperLinkSyntax extends TextSyntaxAdapter {
    private static final String PATTERN = ".*[\\[]{1}.*[\\](]{1}.*[)]{1}.*";
    private static final String AUTO_LINK_PATTERN = "https?://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";

    private int mColor;
    private boolean isUnderLine;
    private OnLinkClickCallback mOnLinkClickCallback;

    public HyperLinkSyntax(@NonNull MarkdownConfiguration markdownConfiguration) {
        super(markdownConfiguration);
        mColor = markdownConfiguration.getLinkFontColor();
        isUnderLine = markdownConfiguration.isShowLinkUnderline();
        mOnLinkClickCallback = markdownConfiguration.getOnLinkClickCallback();
    }

    @Override
    boolean isMatch(@NonNull String text) {
        return contains(text) ? Pattern.compile(PATTERN).matcher(text).matches() : Pattern.compile(AUTO_LINK_PATTERN).matcher(text).find();
    }

    @NonNull
    @Override
    boolean encode(@NonNull SpannableStringBuilder ssb) {
        boolean isHandledBackSlash = false;
        isHandledBackSlash |= replace(ssb, SyntaxKey.KEY_HYPER_LINK_BACKSLASH_LEFT, CharacterProtector.getKeyEncode());
        isHandledBackSlash |= replace(ssb, SyntaxKey.KEY_HYPER_LINK_BACKSLASH_MIDDLE, CharacterProtector.getKeyEncode1());
        isHandledBackSlash |= replace(ssb, SyntaxKey.KEY_HYPER_LINK_BACKSLASH_RIGHT, CharacterProtector.getKeyEncode3());
        return isHandledBackSlash;
    }

    @Override
    SpannableStringBuilder format(@NonNull SpannableStringBuilder ssb, int lineNumber) {
        parse(ssb);
        parseAutoLink(ssb);
        return ssb;
    }

    @NonNull
    @Override
    void decode(@NonNull SpannableStringBuilder ssb) {
        replace(ssb, CharacterProtector.getKeyEncode(), SyntaxKey.KEY_HYPER_LINK_BACKSLASH_LEFT);
        replace(ssb, CharacterProtector.getKeyEncode1(), SyntaxKey.KEY_HYPER_LINK_BACKSLASH_MIDDLE);
        replace(ssb, CharacterProtector.getKeyEncode3(), SyntaxKey.KEY_HYPER_LINK_BACKSLASH_RIGHT);
    }

    /**
     * check the key, whether the text contains hyper link keys
     *
     * @param text
     * @return
     */
    private static boolean contains(String text) {
        if (text.length() < 4 || TextUtils.equals(text, SyntaxKey.KEY_HYPER_LINK_EMPTY)) {
            return true;
        }
        char[] array = text.toCharArray();
        final int length = array.length;
        char[] findArray = new char[]{SyntaxKey.KEY_HYPER_LINK_LEFT_CHAR, SyntaxKey.KEY_HYPER_LINK_MIDDLE_LEFT_CHAR,
                SyntaxKey.KEY_HYPER_LINK_MIDDLE_RIGHT_CHAR, SyntaxKey.KEY_HYPER_LINK_RIGHT_CHAR};
        int findPosition = 0;
        for (int i = 0; i < length; i++) {
            if (TextHelper.getChar(array, i) != 0 && TextHelper.getChar(array, i) == TextHelper.getChar(findArray, findPosition)) {
                if (findPosition == 1) {//]�?��?�必须得是(
                    if (TextHelper.getChar(array, ++i) == 0 || TextHelper.getChar(findArray, ++findPosition) == 0) {
                        return false;
                    }
                    if (TextHelper.getChar(array, ++i) != TextHelper.getChar(findArray, ++findPosition)) {
                        findPosition--;
                    } else {
                        findPosition++;
                    }
                } else {
                    findPosition++;
                }
                if (findPosition == findArray.length - 1) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * parse [link](url)
     *
     * @param ssb the original content
     */
    @NonNull
    private void parse(@NonNull SpannableStringBuilder ssb) {
        String text = ssb.toString();
        SpannableStringBuilder tmp = new SpannableStringBuilder();
        String tmpTotal = text;
        while (true) {
            int position4Key0 = tmpTotal.indexOf(SyntaxKey.KEY_HYPER_LINK_LEFT);
            int position4Key1 = tmpTotal.indexOf(SyntaxKey.KEY_HYPER_LINK_MIDDLE);
            int position4Key2 = tmpTotal.indexOf(SyntaxKey.KEY_HYPER_LINK_RIGHT);
            if (position4Key0 == -1 || position4Key1 == -1 || position4Key2 == -1) {
                break;
            }
            if (position4Key0 < position4Key1 && position4Key1 < position4Key2) {
                //处�?�aa[bb[b](cccc)dddd
                int tmpCenter = tmpTotal.indexOf(SyntaxKey.KEY_HYPER_LINK_MIDDLE);
                String tmpLeft = tmpTotal.substring(0, tmpCenter);
                //正常�?程
                int positionHeader = tmpLeft.lastIndexOf(SyntaxKey.KEY_HYPER_LINK_LEFT);
                tmp.append(tmpTotal.substring(0, positionHeader));
                int index = tmp.length();
                tmpTotal = tmpTotal.substring(positionHeader + SyntaxKey.KEY_HYPER_LINK_LEFT.length(), tmpTotal.length());
                int positionCenter = tmpTotal.indexOf(SyntaxKey.KEY_HYPER_LINK_MIDDLE);
                ssb.delete(tmp.length(), tmp.length() + SyntaxKey.KEY_HYPER_LINK_LEFT.length());
                tmp.append(tmpTotal.substring(0, positionCenter));
                tmpTotal = tmpTotal.substring(positionCenter + SyntaxKey.KEY_HYPER_LINK_MIDDLE.length(), tmpTotal.length());
                int positionFooter = tmpTotal.indexOf(SyntaxKey.KEY_HYPER_LINK_RIGHT);
                String link = tmpTotal.substring(0, positionFooter);
                ssb.setSpan(new MDURLSpan(link, mColor, isUnderLine, mOnLinkClickCallback), index, tmp.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                ssb.delete(tmp.length(), tmp.length() + SyntaxKey.KEY_HYPER_LINK_MIDDLE.length() + link.length() + SyntaxKey.KEY_HYPER_LINK_RIGHT.length());
                tmpTotal = tmpTotal.substring(positionFooter + SyntaxKey.KEY_HYPER_LINK_RIGHT.length(), tmpTotal.length());
            } else if (position4Key0 < position4Key1 && position4Key0 < position4Key2 && position4Key2 < position4Key1) {
                //111[22)22](33333)
                tmpTotal = replaceFirstOne(tmpTotal, SyntaxKey.KEY_HYPER_LINK_RIGHT, SyntaxKey.PLACE_HOLDER);
            } else if (position4Key1 < position4Key0 && position4Key1 < position4Key2) {
                //](在最�?�?�的情况 111](2222[333)4444  1111](2222)3333[4444
                tmp.append(tmpTotal.substring(0, position4Key1 + SyntaxKey.KEY_HYPER_LINK_MIDDLE.length()));
                tmpTotal = tmpTotal.substring(position4Key1 + SyntaxKey.KEY_HYPER_LINK_MIDDLE.length(), tmpTotal.length());
            } else if (position4Key2 < position4Key0 && position4Key2 < position4Key1) {
                //)在最�?�?�的情况 111)2222](333[4444  1111)2222[3333](4444
                tmp.append(tmpTotal.substring(0, position4Key2 + SyntaxKey.KEY_HYPER_LINK_RIGHT.length()));
                tmpTotal = tmpTotal.substring(position4Key2 + SyntaxKey.KEY_HYPER_LINK_RIGHT.length(), tmpTotal.length());
            }
        }
    }

    /**
     * parse links, noy by syntax
     *
     * @param ssb the original content
     */
    private void parseAutoLink(@NonNull SpannableStringBuilder ssb) {
        String text = ssb.toString();
        List<String> matchList = new ArrayList<>();
        Matcher m = Pattern.compile(AUTO_LINK_PATTERN, Pattern.MULTILINE).matcher(text);
        while (m.find()) {
            matchList.add(m.group());
        }
        if (matchList.size() == 0) {
            return;
        }
        final int count = matchList.size();
        for (int i = 0; i < count; i++) {
            String url = matchList.get(i);
            int index = text.indexOf(url);
            if (SyntaxUtils.existHyperLinkSyntax(ssb, index, url.length())) {
                continue;
            }
            ssb.setSpan(new MDURLSpan(url, mColor, isUnderLine, mOnLinkClickCallback), index, index + url.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            if (i == count - 1) {
                break;
            }
            StringBuilder sb = new StringBuilder();
            sb.append(text.substring(0, index));
            sb.append(TextHelper.getPlaceHolder(url));
            sb.append(text.substring(index + url.length()));
            text = sb.toString();
        }
    }

    /**
     * replace the key words
     *
     * @param content     the original content
     * @param target      the key words
     * @param replacement the replacement string
     * @return
     */
    @NonNull
    private static String replaceFirstOne(@NonNull String content, @NonNull String target, @NonNull String replacement) {
        int matchStart = content.indexOf(target, 0);
        if (matchStart == -1) {
            return content;
        }
        int targetLength = target.length();
        if (targetLength == 0) {
            int resultLength = content.length() + (content.length() + 1) * replacement.length();
            StringBuilder result = new StringBuilder(resultLength);
            result.append(replacement);
            for (int i = 0; i != content.length(); ++i) {
                result.append(content.charAt(i));
                result.append(replacement);
            }
            return result.toString();
        }
        StringBuilder result = new StringBuilder(content.length());
        for (int i = 0; i < matchStart; ++i) {
            result.append(content.charAt(i));
        }
        result.append(replacement);
        int over = matchStart + targetLength;
        for (int i = over; i < content.length(); ++i) {
            result.append(content.charAt(i));
        }
        return result.toString();
    }
}
