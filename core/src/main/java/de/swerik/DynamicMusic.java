package de.swerik;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * The DynamicMusic class enables use of Dynamic Music based on LibGDX' Music Class
 *
 * @author Swerik
 */
public class DynamicMusic {
    private final HashMap<String, Music> tracks;
    private AssetManager assetManager;

    /**
     * Instantiates Dynamic music.
     *
     * @param assetManager the AssetManager used to load the MusicTracks
     */
    public DynamicMusic(AssetManager assetManager) {
        tracks = new HashMap<>();
        this.assetManager = assetManager;
    }

    /**
     * Add more Tracks.
     *
     * @param fileNames the file names of the Tracks. Can be relative or absolute.
     * @return the DynamicMusic instance
     */
    public DynamicMusic add(String... fileNames) {
        ArrayList<AssetDescriptor<Music>> tracksToAdd = new ArrayList<>();
        // add all Tracks to loading queue in the AssetManager
        for (int i = 0; i < fileNames.length; i++) {
            tracksToAdd.add(new AssetDescriptor<>(fileNames[i], Music.class));
            assetManager.load(tracksToAdd.get(i));
        }
        // finish loading of all Tracks
        assetManager.finishLoading();
        // add all Tracks to HashMap
        for (AssetDescriptor<Music> track : tracksToAdd) {
            tracks.put(track.fileName, assetManager.get(track.fileName));
        }
        return this;
    }

    /**
     * Remove one or multiple Tracks. Also unloads them from the AssetManager.
     *
     * @param trackNames the name(s) of the Track(s).
     * @return the DynamicMusic instance
     */
    public DynamicMusic remove(String... trackNames) {
        for (String trackName : trackNames) {
            assetManager.unload(assetManager.getAssetFileName(tracks.remove(trackName)));
        }
        return this;
    }

    /**
     * Remove all Tracks. Also unloads them from the AssetManager.
     *
     * @return the DynamicMusic instance
     */
    public DynamicMusic remove() {
        for (String key : tracks.keySet()) {
            assetManager.unload(assetManager.getAssetFileName(tracks.remove(key)));
        }
        return this;
    }

    /**
     * Adds a new track-name for a track for the play, pause, stop methods.
     * Actually just adds another key-value-pair and preserves the old pair. So you can still find a track by FileName but also by the new Key.
     *
     * @param oldTrackName the name(s) of the Track(s).
     * @param newTrackName the name(s) of the Track(s).
     * @return the DynamicMusic instance
     */
    public DynamicMusic changeTrackName(String oldTrackName, String newTrackName) {
        tracks.put(newTrackName, tracks.get(oldTrackName));
        return this;
    }

    /**
     * Sets asset manager.
     *
     * @param assetManager the asset manager
     * @return the DynamicMusic instance
     */
    public DynamicMusic setAssetManager(AssetManager assetManager) {
        this.assetManager = assetManager;
        return this;
    }

    /**
     * Play one or multiple tracks.
     *
     * @param trackNames the track name(s)
     * @return the DynamicMusic instance
     */
    public DynamicMusic play(String... trackNames) {
        tracks.get(trackNames[1]).play();
        for (int i = 1; i < trackNames.length; i++) {
            tracks.get(trackNames[i]).play();
            tracks.get(trackNames[i]).setPosition(tracks.get(trackNames[1]).getPosition());
        }
        return this;
    }

    /**
     * Play all tracks.
     *
     * @return the DynamicMusic instance
     */
    public DynamicMusic play() {
        Music firstTrack = null;
        for (Music track : tracks.values()) {
            track.play();
            if (firstTrack == null) firstTrack = track;
            else track.setPosition(firstTrack.getPosition());
        }
        return this;
    }

    /**
     * Pause one or multiple tracks.
     *
     * @param trackNames the track name(s)
     * @return the DynamicMusic instance
     */
    public DynamicMusic pause(String... trackNames) {
        tracks.get(trackNames[1]).pause();
        float position = tracks.get(trackNames[1]).getPosition();
        for (int i = 1; i < trackNames.length; i++) {
            tracks.get(trackNames[i]).pause();
            tracks.get(trackNames[i]).setPosition(position);
        }
        return this;
    }

    /**
     * Pause all tracks.
     *
     * @return the DynamicMusic instance
     */
    public DynamicMusic pause() {
        float firstPosition = 0;
        for (Music track : tracks.values()) {
            track.pause();
            if (firstPosition == 0) firstPosition = track.getPosition();
            else track.setPosition(firstPosition);
        }
        return this;
    }

    /**
     * Stop one or multiple tracks.
     *
     * @param trackNames the track name(s)
     * @return the DynamicMusic instance
     */
    public DynamicMusic stop(String... trackNames) {
        for (String trackName : trackNames) {
            tracks.get(trackName).stop();
        }
        return this;
    }

    /**
     * Stop all tracks.
     *
     * @return the DynamicMusic instance
     */
    public DynamicMusic stop() {
        for (Music track : tracks.values()) {
            track.stop();
        }
        return this;
    }

    /**
     * Sets the volume of one or multiple tracks.
     *
     * @param volume     the volume
     * @param trackNames the track name(s)
     * @return the DynamicMusic instance
     */
    public DynamicMusic setVolume(float volume, String... trackNames) {
        for (String trackName : trackNames) {
            tracks.get(trackName).setVolume(volume);
        }
        return this;
    }

    /**
     * Sets the volume of all tracks.
     *
     * @param volume the volume
     * @return the DynamicMusic instance
     */
    public DynamicMusic setVolume(float volume) {
        for (Music track : tracks.values()) {
            track.setVolume(volume);
        }
        return this;
    }
}
