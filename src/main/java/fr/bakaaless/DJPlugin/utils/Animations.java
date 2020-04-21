package fr.bakaaless.DJPlugin.utils;

public enum Animations {

    RAINBOW(AnimationsType.DJ);

    private final AnimationsType type;

    Animations(final AnimationsType type) {
        this.type = type;
    }

    public AnimationsType getType() {
        return this.type;
    }

    public boolean isSameTypeAs(final Animations animations) {
        return this.getType().getValue().equals(animations.getType().getValue());
    }

    private enum AnimationsType {
        DJ("DJ");

        private final String value;

        AnimationsType(final String value) {
            this.value = value;
        }

        public String getValue() {
            return this.value;
        }
    }
}
