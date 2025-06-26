/* Copyright (c) 2010-2011 Pierre LEVY androidsoft.org
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.androidsoft.games.memory.tux;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import org.androidsoft.games.utils.sound.SoundManager;

/**
 * AbstractMainActivity
 * @author pierre
 */
public abstract class AbstractMainActivity extends Activity implements OnClickListener
{
    protected boolean mQuit;
    private View mSplash;
    private Button mButtonPlay;
    private Button mButtonPlayInfinity;
    private Button mButtonCredits;
    private boolean mStarted;
    private boolean mStartedInfinity;
    protected abstract View getGameView();

    protected abstract void newGame();

    protected abstract void about();

    /**
     * {@inheritDoc }
     */
    @Override
    public void onCreate(Bundle icicle)
    {
        super.onCreate(icicle);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);        
        SoundManager.init(AbstractMainActivity.this);

        setContentView(R.layout.main);
        mSplash = findViewById(R.id.splash);

        mButtonPlay = findViewById(R.id.button_play);
        mButtonPlay.setOnClickListener(this);

        mButtonPlayInfinity = findViewById(R.id.button_play_infinity);
        mButtonPlayInfinity.setOnClickListener(this);

        mButtonCredits = findViewById(R.id.button_credits);
        mButtonCredits.setOnClickListener(this);

        checkLastVersion();
    }

    public void onBackPressed() {
        quit();
    }

    /**
     * {@inheritDoc }
     */
    @Override
    protected void onResume()
    {
        super.onResume();

        SharedPreferences prefs = getPreferences(0);
        mStarted = prefs.getBoolean(Constants.PREF_STARTED, false);
        mStartedInfinity = prefs.getBoolean(Constants.PREF_STARTED, false);
        if (mStarted || mStartedInfinity)
        {
            mSplash.setVisibility(View.GONE);
            getGameView().setVisibility(View.VISIBLE);
        } else
        {
            mSplash.setVisibility(View.VISIBLE);
            getGameView().setVisibility(View.GONE);
        }

        if (!SoundManager.isInitialized())
        {
            SoundManager.init(this);
        }
        SoundManager.instance().addSound(Constants.SOUND_NEW_GAME, R.raw.new_game);
        SoundManager.instance().addSound(Constants.SOUND_INFINITY, R.raw.infinity);
    }

    /**
     * {@inheritDoc }
     */
    @Override
    protected void onPause()
    {
        super.onPause();

        SharedPreferences.Editor editor = getPreferences(0).edit();
        if (!mQuit)
        {
            editor.putBoolean(Constants.PREF_STARTED, mStarted);
        } else
        {
            editor.remove(Constants.PREF_STARTED);
        }
        editor.apply();

        SoundManager.release();
    }


    private void onNewGame()
    {
        SoundManager.instance().playSound(Constants.SOUND_NEW_GAME);
        newGame();
    }

    /**
     * Quit the application
     */
    void quit()
    {
        mQuit = true;
        AbstractMainActivity.this.finish();
    }

    /**
     * {@inheritDoc }
     */
    public void onClick(View v)
    {
        if (v == mButtonPlay)
        {
            SoundManager.instance().playSound(Constants.SOUND_NEW_GAME);
            mSplash.setVisibility(View.GONE);
            getGameView().setVisibility(View.VISIBLE);
            getGameView().requestFocus();
            mStarted = true;
            Constants.mInfinityMode = false;
        }

        if (v == mButtonPlayInfinity)
        {
            SoundManager.instance().playSound(Constants.SOUND_NEW_GAME);
            mSplash.setVisibility(View.GONE);
            getGameView().setVisibility(View.VISIBLE);
            getGameView().requestFocus();
            mStartedInfinity = true;
            Constants.mInfinityMode = true;
        }

        if (v == mButtonCredits){
            about();
        }
    }

    protected void showEndDialog(String title, String message, int icon)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.DialogTheme);
        builder.setTitle(title);
        builder.setIcon(icon);
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setPositiveButton(getString(R.string.new_game),
                (dialog, id) -> {
                    dialog.cancel();
                    onNewGame();
                });
        builder.setNegativeButton(getString(R.string.quit),
                (dialog, id) -> quit());
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void checkLastVersion()
    {
        int resTitle;
        int resMessage;
        final int lastVersion = getVersion();
        if (lastVersion < Constants.VERSION)
        {
            if (lastVersion == 0)
            {
                // This is a new install
                resTitle = R.string.first_run_dialog_title;
                resMessage = R.string.first_run_dialog_message;
            } else
            {
                // This is an upgrade.
                resTitle = R.string.whats_new_dialog_title;
                resMessage = R.string.whats_new_dialog_message;
            }
            // show what's new message
            saveVersion();
            showWhatsNewDialog(resTitle, resMessage, R.drawable.icon);
        }
    }

    private int getVersion()
    {
        SharedPreferences prefs = getSharedPreferences(AbstractMainActivity.class.getName(), Activity.MODE_PRIVATE);
        return prefs.getInt(Constants.KEY_VERSION, Constants.DEFAULT_VERSION);
    }

    private void saveVersion()
    {
        SharedPreferences prefs = getSharedPreferences(AbstractMainActivity.class.getName(), Activity.MODE_PRIVATE);
        Editor editor = prefs.edit();
        editor.putInt(Constants.KEY_VERSION, Constants.VERSION);
        editor.apply();

    }

    protected void showWhatsNewDialog(int title, int message, int icon)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.DialogTheme);
        builder.setTitle(title);
        builder.setIcon(icon);
        builder.setMessage(message);
        builder.setPositiveButton(getString(R.string.ok),
                (dialog, id) -> {
                    dialog.cancel();
                    onNewGame();
                });
        AlertDialog alert = builder.create();
        alert.show();

    }
}
