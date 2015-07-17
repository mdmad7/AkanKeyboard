package com.mdmad7.michaeldavis.akankeyboard;

import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.media.AudioManager;
import android.text.Editable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.widget.EditText;

/**
 * Created by michaeldavis on 7/6/15.
 */
public class AkanIME extends InputMethodService
        implements KeyboardView.OnKeyboardActionListener {
    private KeyboardView keyboardView;
    private Keyboard qwertyKeyboard;
    private Keyboard qwertySymbolsKeyboard;
    private Keyboard numPad;
    private Keyboard numPadSymbols;
    private Keyboard simpleNumpad;
    private Keyboard qwertyGoKeyboard;

    public static final int codeSearch = 55000;

    private boolean caps = false; //caps lock

    @Override
    public View onCreateInputView() {
        keyboardView = (KeyboardView)getLayoutInflater().inflate(R.layout.keyboard, null);
        qwertyKeyboard = new Keyboard(this, R.xml.qwerty);
        qwertySymbolsKeyboard = new Keyboard(this, R.xml.qwerty_symbols);
        numPad = new Keyboard(this, R.xml.numpad);
        numPadSymbols = new Keyboard(this, R.xml.numpad_symbols);
        simpleNumpad = new Keyboard(this, R.xml.simple_numpad);
        qwertyGoKeyboard = new Keyboard(this, R.xml.qwerty_go);


        keyboardView.setKeyboard(qwertyKeyboard);
        keyboardView.setOnKeyboardActionListener(this);
        return keyboardView;
    }


    private void playClick(int keyCode){
        AudioManager am = (AudioManager)getSystemService(AUDIO_SERVICE);
        switch(keyCode){
            case 32:
                am.playSoundEffect(AudioManager.FX_KEYPRESS_SPACEBAR);
                break;
            case Keyboard.KEYCODE_DONE:
            case 10:
                am.playSoundEffect(AudioManager.FX_KEYPRESS_RETURN);
                break;
            case Keyboard.KEYCODE_DELETE:
                am.playSoundEffect(AudioManager.FX_KEYPRESS_DELETE);
                break;
            default: am.playSoundEffect(AudioManager.FX_KEYPRESS_STANDARD);
        }
    }

    @Override
    public void onStartInputView(EditorInfo info, boolean restarting) {
        super.onStartInputView(info, restarting);

//      Sets keyboard defaults whenever keyboard is reloaded
        keyboardView.setKeyboard(qwertyKeyboard);
    }

    @Override
    public void onStartInput(EditorInfo attribute, boolean restarting) {
        super.onStartInput(attribute, restarting);

    }

    // Implementation of KeyboardViewListener
    @Override
    public void onKey(int primaryCode, int[] keyCodes) {

        //handle Keys
        InputConnection ic = getCurrentInputConnection();
        playClick(primaryCode);

        char code = (char)primaryCode;
        switch(primaryCode){
            case Keyboard.KEYCODE_DELETE :
                ic.deleteSurroundingText(1, 0);
                break;
            case Keyboard.KEYCODE_SHIFT:
                caps = !caps;
                keyboardView.getKeyboard().setShifted(caps);
                keyboardView.invalidateAllKeys();
                break;
            case Keyboard.KEYCODE_DONE:
                if(ic.performEditorAction(EditorInfo.IME_ACTION_GO)){

                }else {
                    ic.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
                }
                break;

                //  Matches SYM Key switches for all keyboards with respective symbol keyboard
            case Keyboard.KEYCODE_MODE_CHANGE:
                Keyboard current = keyboardView.getKeyboard();
                if (current == qwertySymbolsKeyboard) {
                    keyboardView.setKeyboard(qwertyKeyboard);
                }else if(current == qwertyKeyboard){
                    keyboardView.setKeyboard(qwertySymbolsKeyboard);
                }else if(current == qwertyGoKeyboard) {
            keyboardView.setKeyboard(qwertySymbolsKeyboard);
               }else if (current == numPadSymbols){
                    keyboardView.setKeyboard(numPad);
                }
                else if(current == numPad){
                    keyboardView.setKeyboard(numPadSymbols);
                }
                break;

//            case 46:
//                ic.commitText(String.valueOf(code), 1);
//                if(ic.sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_SPACE))){
//                    caps =!caps;
//                    keyboardView.getKeyboard().setShifted(caps);
//                    keyboardView.invalidateAllKeys();
//                }
//
//                break;

            case 32:
                ic.commitText(String.valueOf(code), 1);
                if(keyboardView.getKeyboard().setShifted(caps)){
                    keyboardView.invalidateAllKeys();
                }
                break;

            default:
                if(Character.isLetter(code) && caps){
                    code = Character.toUpperCase(code);
                }
                ic.commitText(String.valueOf(code),1);

        }

    }


    @Override
    public void onPress(int primaryCode) {
    }


    @Override
    public void onRelease(int primaryCode) {

    }

    @Override
    public void onText(CharSequence text) {
    }

    @Override
    public void swipeDown() {
    }

    @Override
    public void swipeLeft() {
    }

    @Override
    public void swipeRight() {
    }

    @Override
    public void swipeUp() {
    }
}