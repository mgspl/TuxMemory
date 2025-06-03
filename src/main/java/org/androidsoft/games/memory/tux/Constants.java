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

/**
 *
 * @author pierre
 */
public class Constants
{
    // Version Related
    final static int VERSION = 7;
    final static String KEY_VERSION = "version";
    final static int DEFAULT_VERSION = 1;

    // Prefs
    final static String PREF_STARTED = "started";
    final static String PREF_BEST_MOVE_COUNT = "best_move_count";
    static final String PREF_LIST = "list";
    static final String PREF_MOVE_COUNT = "move_count";
    static final String PREF_SELECTED_COUNT = "seleted_count";
    static final String PREF_FOUND_COUNT = "found_count";
    static final String PREF_LAST_POSITION = "last_position";

    // Tile size
    static final int MAX_TILES_PER_ROW = 6;
    static final int MIN_TILES_PER_ROW = 4;
    static final int SET_SIZE = (Constants.MAX_TILES_PER_ROW * Constants.MIN_TILES_PER_ROW ) / 2;

    // Sounds
    final static int SOUND_NEW_GAME = 1000;
    final static int SOUND_FAILED = 2000;
    static final int SOUND_SUCCEED = 2001;
}
