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

    /**
     * Fades the specified Tracks in to their previous Volume or, if 0, maxVolume
     *
     * @param milliseconds how long the fade happens
     * @param trackNames   the Tracks that will fade in
     * @return this Dynamic Music Instance
     */
    public DynamicMusic fadeIn(final float milliseconds, final String... trackNames) {
        for (String track : trackNames) {
            fadeIn(milliseconds, track);
        }
        return this;
    }

    /**
     * Fades every Track in to their previous Volume or, if 0, maxVolume
     *
     * @param milliseconds how long the fade happens
     * @return this Dynamic Music Instance
     */
    public DynamicMusic fadeIn(final float milliseconds) {
        for (String track : tracks.keySet()) {
            fadeIn(milliseconds, track);
        }
        return this;
    }

    /**
     * Fades the specified Tracks in to the specified Volume
     *
     * @param milliseconds how long the fade happens
     * @param volume       the max volume it should reach
     * @param trackNames   the Tracks that will fade in
     * @return this Dynamic Music Instance
     */
    public DynamicMusic fadeIn(final float milliseconds, final float volume, final String... trackNames) {
        for (String track : trackNames) {
            fadeIn(milliseconds, track, volume);
        }
        return this;
    }

    /**
     * Fades every Track in to the specified Volume
     *
     * @param milliseconds how long the fade happens
     * @param volume       the max volume it should reach
     * @return this Dynamic Music Instance
     */
    public DynamicMusic fadeIn(final float milliseconds, final float volume) {
        for (String track : tracks.keySet()) {
            fadeIn(milliseconds, track, volume);
        }
        return this;
    }

    /**
     * Fades out the specified Tracks to 0
     *
     * @param milliseconds how long the fade takes
     * @param trackNames   the Tracks that will fade out
     * @return this Dynamic Music Instance
     */
    public DynamicMusic fadeOut(final float milliseconds, final String... trackNames) {
        for (String track : trackNames) {
            fadeOut(milliseconds, track);
        }
        return this;
    }

    /**
     * Fades out every Track to 0
     *
     * @param milliseconds how long the fade takes
     * @return this Dynamic Music Instance
     */
    public DynamicMusic fadeOut(final float milliseconds) {
        for (String track : tracks.keySet()) {
            fadeOut(milliseconds, track);
        }
        return this;
    }

    /**
     * Fades out the specified Tracks to the specified Volume
     *
     * @param milliseconds how long the fade takes
     * @param volume       the min volume it should reach
     * @param trackNames   the Tracks that will fade out
     * @return this Dynamic Music Instance
     */
    public DynamicMusic fadeOut(final float milliseconds, final float volume, final String... trackNames) {
        for (String track : trackNames) {
            fadeOut(milliseconds, track, volume);
        }
        return this;
    }

    /**
     * Fades out every Track to the specified Volume
     *
     * @param milliseconds how long the fade takes
     * @param volume       the min volume it should reach
     * @return this Dynamic Music Instance
     */
    public DynamicMusic fadeOut(final float milliseconds, final float volume) {
        for (String track : tracks.keySet()) {
            fadeOut(milliseconds, track, volume);
        }
        return this;
    }

    /**
     * Crossfades two tracks.
     *
     * @param milliseconds time in milliseconds how long the fade should take.
     * @param trackIn      the track that should fade in.
     * @param trackOut     the track that should fade out.
     * @return this DynamicMusic Instance.
     */
    public DynamicMusic crossFade(final float milliseconds, final String trackIn, final String trackOut) {
        fadeIn(milliseconds, trackIn, tracks.get(trackOut).getVolume());
        fadeOut(milliseconds, trackOut);
        return this;
    }

    /**
     * Crossfades two tracks. The in-track will be fadid in to the specified volume.
     *
     * @param milliseconds time in milliseconds how long the fade should take.
     * @param volume       max volume the fadeIn-Track should reach.
     * @param trackIn      the track that should fade in.
     * @param trackOut     the track that should fade out.
     * @return this DynamicMusic Instance.
     */
    public DynamicMusic crossFade(final float milliseconds, final float volume, final String trackIn, final String trackOut) {
        fadeIn(milliseconds, trackIn, volume);
        fadeOut(milliseconds, trackOut);
        return this;
    }

    /**
     * Crossfades every track out and every specified track in.
     *
     * @param milliseconds time in milliseconds how long the fade should take.
     * @param tracksIn     the tracks that should fade in.
     * @return this DynamicMusic Instance.
     */
    public DynamicMusic crossFadeIn(final float milliseconds, final String... tracksIn) {
        for (String trackOut : tracks.keySet()) {
            Music track = tracks.get(trackOut);
            if (track.getVolume() > 0) fadeOut(milliseconds, trackOut);
        }
        fadeIn(milliseconds, tracksIn);
        return this;
    }

    /**
     * Crossfades every track out and every specified track in.
     *
     * @param milliseconds time in milliseconds how long the fade should take.
     * @param volume       max volume the fadeIn-Tracks should reach.
     * @param tracksIn     the tracks that should fade in.
     * @return this DynamicMusic Instance.
     */
    public DynamicMusic crossFadeIn(final float milliseconds, final float volume, final String... tracksIn) {
        for (String trackOut : tracks.keySet()) {
            Music track = tracks.get(trackOut);
            if (track.getVolume() > 0) fadeOut(milliseconds, trackOut);
        }
        fadeIn(milliseconds, volume, tracksIn);
        return this;
    }

    /**
     * Crossfades every track in and every specified track out.
     *
     * @param milliseconds time in milliseconds how long the fade should take.
     * @param tracksOut    the tracks that should fade out.
     * @return this DynamicMusic Instance.
     */
    public DynamicMusic crossFadeOut(final float milliseconds, final String... tracksOut) {
        for (String trackIn : tracks.keySet()) {
            Music track = tracks.get(trackIn);
            if (track.getVolume() < 0.0001) fadeIn(milliseconds, trackIn);
        }
        fadeOut(milliseconds, tracksOut);
        return this;
    }

    /**
     * Crossfades every track in and every specified track out.
     *
     * @param milliseconds time in milliseconds how long the fade should take.
     * @param volume       the min volume each track should reach.
     * @param tracksOut    the tracks that should fade out.
     * @return this DynamicMusic Instance.
     */
    public DynamicMusic crossFadeOut(final float milliseconds, final float volume, final String... tracksOut) {
        for (String trackIn : tracks.keySet()) {
            Music track = tracks.get(trackIn);
            if (track.getVolume() < 0.0001) fadeIn(milliseconds, trackIn);
        }
        fadeOut(milliseconds, volume, tracksOut);
        return this;
    }

    // Helper Methods:

    private void fadeIn(final float milliseconds, final String track) {
        fadeIn(milliseconds, track, -1);
    }

    private void fadeIn(final float milliseconds, final String track, final float maxVolume) {
        boolean useVolume = maxVolume >= 0 && maxVolume <= 1;
        new Thread(() -> {
            long startTime = System.nanoTime() / 1000;
            float percentage = (float) ((System.nanoTime() / 1000) - startTime) / milliseconds;
            float volume = useVolume ? maxVolume : tracks.get(track).getVolume();
            if (volume == 0) volume = 1;
            while (percentage * volume <= volume) {
                tracks.get(track).setVolume(percentage * volume);
                percentage = (float) ((System.nanoTime() / 1000) - startTime) / milliseconds;
            }
            tracks.get(track).setVolume(volume);
        }).start();
    }

    private void fadeOut(final float milliseconds, final String track) {
        fadeOut(milliseconds, track, -1);
    }

    private void fadeOut(final float milliseconds, final String track, final float minVolume) {
        boolean useVolume = minVolume >= 0 && minVolume <= 1;
        new Thread(() -> {
            long startTime = System.nanoTime() / 1000;
            float percentage = (float) ((System.nanoTime() / 1000) - startTime) / milliseconds;
            float volume = useVolume ? minVolume : 0;
            float maxVolume = tracks.get(track).getVolume();
            while (maxVolume * (1 - percentage) >= volume) {
                tracks.get(track).setVolume(maxVolume * (1 - percentage));
                percentage = (float) ((System.nanoTime() / 1000) - startTime) / milliseconds;
            }
            tracks.get(track).setVolume(volume);
        }).start();
    }
}
