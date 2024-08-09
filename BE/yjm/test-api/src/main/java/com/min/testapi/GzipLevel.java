package com.min.testapi;

import java.util.zip.Deflater;

public class GzipLevel {
    public static final GzipLevel DEFAULT = new GzipLevel();
    public static final int MIN_LEVEL = 1;
    public static final int MAX_LEVEL = 9;

    private final int level;

    private GzipLevel() {
        this.level = Deflater.BEST_SPEED;
    }

    public GzipLevel(final int level) {
        validate(level);
        this.level = level;
    }

    private void validate(final int level) {
        if (level < MIN_LEVEL || level > MAX_LEVEL) {
            throw new IllegalArgumentException("level must be between %d and %d".formatted(MIN_LEVEL, MAX_LEVEL));
        }
    }

    public int getLevel() {
        return level;
    }
}
