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

import android.content.SharedPreferences;
import java.util.ArrayList;
import java.util.List;
import org.androidsoft.games.utils.sound.SoundManager;

/**
 *
 * @author pierre
 */
public class Memory
{
    private int mSelectedCount;
    private int mMoveCount;
    private int mFoundCount;
    private int mLastPosition = -1;
    private Tile mT1;
    private Tile mT2;
    private static TileList mList = new TileList();
    private static int[] mTiles;
    private final OnMemoryListener mListener;
    
    private static final int[] mSounds = {
      R.raw.blop, R.raw.chime, R.raw.chtoing, R.raw.tic, R.raw.toc, 
      R.raw.toing, R.raw.toing2, R.raw.toing3, R.raw.toing4, R.raw.toing5,
      R.raw.toing6, R.raw.toong, R.raw.tzirlup, R.raw.whiipz
    };

    public Memory(int[] tiles , OnMemoryListener listener )
    {
        mTiles = tiles;
        mListener = listener;
    }

    void onResume(SharedPreferences prefs)
    {
        String serialized = prefs.getString(Constants.PREF_LIST, null);
        if (serialized != null)
        {
            mList = new TileList(serialized);
            mMoveCount = prefs.getInt(Constants.PREF_MOVE_COUNT, 0);
            ArrayList<Tile> list = mList.getSelected();
            mSelectedCount = list.size();
            mT1 = (mSelectedCount > 0) ? list.get(0) : null;
            mT2 = (mSelectedCount > 1) ? list.get(1) : null;
            mFoundCount = prefs.getInt(Constants.PREF_FOUND_COUNT, 0);
            mLastPosition = prefs.getInt(Constants.PREF_LAST_POSITION, -1);

        }

        initSounds();
    }

    void onPause(SharedPreferences preferences, boolean quit)
    {
        SharedPreferences.Editor editor = preferences.edit();
        if (!quit)
        {
            // Paused without quit - save state
            editor.putString(Constants.PREF_LIST, mList.serialize());
            editor.putInt(Constants.PREF_MOVE_COUNT, mMoveCount);
            editor.putInt(Constants.PREF_SELECTED_COUNT, mSelectedCount);
            editor.putInt(Constants.PREF_FOUND_COUNT, mFoundCount);
            editor.putInt(Constants.PREF_LAST_POSITION, mLastPosition);
        } else
        {
            editor.remove(Constants.PREF_LIST);
            editor.remove(Constants.PREF_MOVE_COUNT);
            editor.remove(Constants.PREF_SELECTED_COUNT);
            editor.remove(Constants.PREF_FOUND_COUNT);
            editor.remove(Constants.PREF_LAST_POSITION);
        }
        editor.apply();
    }

    int getCount()
    {
        return mList.size();
    }

    int getResId(int position)
    {
        return mList.get(position).getResId();
    }

    void reset()
    {
        mFoundCount = 0;
        mMoveCount = 0;
        mList.clear();
        for (Integer tile : getTileSet())
        {
            addRandomly(tile);
        }
    }

    private void initSounds()
    {
        SoundManager.instance().addSound( Constants.SOUND_FAILED , R.raw.failed );
        SoundManager.instance().addSound( Constants.SOUND_SUCCEED , R.raw.succeed );
        for( int i = 0 ; i < mSounds.length ; i++ )
        {
            SoundManager.instance().addSound( i , mSounds[i] );
        }
    }

    public interface OnMemoryListener
    {

        void onComplete(int moveCount);

        void onUpdateView();
    }

    void onPosition(int position)
    {
        if (position == mLastPosition)
        {
            // Same item clicked
            return;
        }
        mLastPosition = position;
        Tile tile = mList.get(position);
        tile.select();
        int sound = tile.mResId % mSounds.length;
        SoundManager.instance().playSound( sound );

        switch (mSelectedCount)
        {
            case 0:
                mT1 = tile;
                break;

            case 1:
                mT2 = tile;
                if (mT1.getResId() == mT2.getResId())
                {
                    mT1.setFound(true);
                    mT2.setFound(true);
                    mFoundCount += 2;
                    SoundManager.instance().playSound( Constants.SOUND_SUCCEED );
                }
                // Quebra o final do jogo
                /*else
                {
//                    SoundManager.instance().playSound( Constants.SOUND_FAILED );
                }*/
                break;

            case 2:
                if (mT1.getResId() != mT2.getResId())
                {
                    mT1.unselect();
                    mT2.unselect();
                }
                mSelectedCount = 0;
                mT1 = tile;
                break;
        }
        mSelectedCount++;
        mMoveCount++;
        updateView();
        checkComplete();
    }

    private void updateView()
    {
        mListener.onUpdateView();
    }

    private void checkComplete()
    {
        if (mFoundCount == mList.size())
        {
            mListener.onComplete(mMoveCount);
        }
    }

    /**
     * Add a pair of pieces randomly in the list
     * @param nResId The resid of the piece
     */
    private void addRandomly(int nResId)
    {
        double dPos = Math.random() * mList.size();
        int nPos = (int) dPos;
        mList.add(nPos, new Tile(nResId));
        dPos = Math.random() * mList.size();
        nPos = (int) dPos;
        mList.add(nPos, new Tile(nResId));

    }

    private int rand(int nSize)
    {
        double dPos = Math.random() * nSize;
        return (int) dPos;
    }

    private List<Integer> getTileSet()
    {
        List<Integer> list = new ArrayList<>();

        while (list.size() < Constants.SET_SIZE)
        {
            int n = rand(mTiles.length);
            int t = mTiles[n];
            if (!list.contains(t))
            {
                list.add(t);
            }
        }
        return list;
    }
}
